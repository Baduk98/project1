#!/bin/bash

# Загружаем конфигурацию
source "$(dirname "${BASH_SOURCE[0]}")/test-config.sh"
source "$(dirname "${BASH_SOURCE[0]}")/device-manager.sh"

# =================================
# ЗАПУСК ТЕСТОВ И СБОР РЕЗУЛЬТАТОВ
# =================================

# Функция сборки проекта
build_project() {
    log_step "Сборка проекта..."

    cd "$PROJECT_ROOT"

    # Очистка проекта
    "$GRADLE_WRAPPER" clean

    # Сборка APK приложения и тестов
    "$GRADLE_WRAPPER" assembleDebug assembleDebugAndroidTest

    local build_status=$?
    if [[ $build_status -eq 0 ]]; then
        log_success "Проект успешно собран"
        return 0
    else
        log_error "Ошибка сборки проекта"
        return 1
    fi
}

# Функция установки APK на устройство
install_apks() {
    local device_id="$1"

    log_step "Установка APK на устройство $device_id..."

    local app_apk="${PROJECT_ROOT}/${APP_MODULE}/build/outputs/apk/debug/${APP_MODULE}-debug.apk"
    local test_apk="${PROJECT_ROOT}/${APP_MODULE}/build/outputs/apk/androidTest/debug/${APP_MODULE}-debug-androidTest.apk"

    # Проверка существования APK файлов
    if [[ ! -f "$app_apk" ]]; then
        log_error "APK приложения не найден: $app_apk"
        return 1
    fi

    if [[ ! -f "$test_apk" ]]; then
        log_error "APK тестов не найден: $test_apk"
        return 1
    fi

    # Удаление старых установок
    "$ADB" -s "$device_id" uninstall "$APP_PACKAGE" 2>/dev/null || true
    "$ADB" -s "$device_id" uninstall "${APP_PACKAGE}.test" 2>/dev/null || true

    # Установка новых APK
    log_info "Установка APK приложения..."
    "$ADB" -s "$device_id" install "$app_apk"

    log_info "Установка APK тестов..."
    "$ADB" -s "$device_id" install "$test_apk"

    log_success "APK установлены на устройство $device_id"
}

# Функция запуска тестов через Gradle
run_tests_gradle() {
    local device_id="$1"

    log_step "Запуск тестов через Gradle на устройстве $device_id..."

    cd "$PROJECT_ROOT"

    # Устанавливаем переменную окружения для конкретного устройства
    export ANDROID_SERIAL="$device_id"

    # Запускаем тесты с таймаутом
    timeout $TEST_EXECUTION_TIMEOUT "$GRADLE_WRAPPER" connectedDebugAndroidTest \
        -Pandroid.testInstrumentationRunner="$TEST_RUNNER" \
        --continue \
        --info

    local test_status=$?

    if [[ $test_status -eq 0 ]]; then
        log_success "Тесты завершены успешно"
        return 0
    elif [[ $test_status -eq 124 ]]; then
        log_error "Тесты прерваны по таймауту ($TEST_EXECUTION_TIMEOUT сек)"
        return 1
    else
        log_warning "Тесты завершены с ошибками (код: $test_status)"
        return $test_status
    fi
}

# Функция запуска тестов через ADB
run_tests_adb() {
    local device_id="$1"
    local test_class="${2:-}"

    log_step "Запуск тестов через ADB на устройстве $device_id..."

    # Формируем команду запуска тестов
    local test_command="am instrument -w"

    if [[ -n "$test_class" ]]; then
        test_command="$test_command -e class $test_class"
    fi

    test_command="$test_command ${TEST_PACKAGE}/io.qameta.allure.android.runners.AllureAndroidJUnit4"

    log_info "Команда запуска: $test_command"

    # Запуск тестов
    "$ADB" -s "$device_id" shell "$test_command"

    local test_status=$?

    if [[ $test_status -eq 0 ]]; then
        log_success "Тесты через ADB завершены успешно"
        return 0
    else
        log_error "Ошибка выполнения тестов через ADB (код: $test_status)"
        return $test_status
    fi
}

# Функция сбора результатов Allure с устройства
collect_allure_results() {
    local device_id="$1"

    log_step "Сбор результатов Allure с устройства $device_id..."

    # Создаем временную директорию для результатов
    local temp_results_dir="${BUILD_DIR}/temp-allure-results"
    mkdir -p "$temp_results_dir"

    # Пытаемся получить результаты из разных возможных локаций
    local possible_paths=(
        "/sdcard/allure-results"
        "/sdcard/googletest/test_outputfiles/allure-results"
        "/data/data/${APP_PACKAGE}.test/files/allure-results"
        "/storage/emulated/0/allure-results"
    )

    local found_results=false

    for path in "${possible_paths[@]}"; do
        log_info "Поиск результатов в $path..."

        if "$ADB" -s "$device_id" shell "test -d $path" 2>/dev/null; then
            log_info "Найдены результаты в $path"

            # Копируем файлы результатов
            "$ADB" -s "$device_id" shell "cd $path && tar cf - ." | tar xf - -C "$temp_results_dir" 2>/dev/null || {
                # Альтернативный способ копирования
                "$ADB" -s "$device_id" pull "$path" "$temp_results_dir" 2>/dev/null || continue
            }

            found_results=true
            break
        fi
    done

    if [[ "$found_results" == "true" ]]; then
        # Перемещаем результаты в финальную директорию
        if [[ -d "$temp_results_dir" ]] && [[ "$(ls -A "$temp_results_dir" 2>/dev/null)" ]]; then
            cp -r "$temp_results_dir"/* "$ALLURE_RESULTS_DIR/" 2>/dev/null || true
            log_success "Результаты Allure собраны в $ALLURE_RESULTS_DIR"
        else
            log_warning "Временная директория результатов пуста"
        fi
    else
        log_warning "Результаты Allure не найдены на устройстве"

        # Пытаемся собрать стандартные результаты из Gradle
        local gradle_results="${PROJECT_ROOT}/${APP_MODULE}/build/outputs/androidTest-results/connected"
        if [[ -d "$gradle_results" ]]; then
            log_info "Используем результаты из Gradle: $gradle_results"
            cp -r "$gradle_results"/* "$ALLURE_RESULTS_DIR/" 2>/dev/null || true
        fi
    fi

    # Очистка временной директории
    rm -rf "$temp_results_dir"

    # Проверяем наличие результатов
    if [[ "$(ls -A "$ALLURE_RESULTS_DIR" 2>/dev/null)" ]]; then
        log_success "Результаты тестирования готовы для генерации отчета"
        return 0
    else
        log_error "Не удалось собрать результаты тестирования"
        return 1
    fi
}

# Функция сбора логов с устройства
collect_device_logs() {
    local device_id="$1"

    log_step "Сбор логов с устройства $device_id..."

    local logs_dir="${REPORTS_DIR}/device-logs"
    mkdir -p "$logs_dir"

    # Сбор логкатов
    "$ADB" -s "$device_id" logcat -d > "${logs_dir}/logcat-${device_id}.txt" 2>/dev/null || true

    # Сбор системной информации
    "$ADB" -s "$device_id" shell dumpsys meminfo > "${logs_dir}/meminfo-${device_id}.txt" 2>/dev/null || true
    "$ADB" -s "$device_id" shell dumpsys cpuinfo > "${logs_dir}/cpuinfo-${device_id}.txt" 2>/dev/null || true

    log_success "Логи собраны в $logs_dir"
}

# Функция полного запуска тестов
run_full_test_suite() {
    local use_emulator="${1:-true}"
    local test_class="${2:-}"

    log_step "Запуск полного набора тестов..."

    # Подготовка окружения
    check_dependencies
    create_directories
    cleanup_previous_results

    # Сборка проекта
    if ! build_project; then
        log_error "Не удалось собрать проект"
        return 1
    fi

    # Подготовка устройства
    local device_id
    if [[ "$use_emulator" == "true" ]]; then
        device_id=$(start_emulator)
        if [[ -z "$device_id" ]]; then
            log_error "Не удалось запустить эмулятор"
            return 1
        fi
    else
        local devices
        devices=$(get_connected_devices)
        if [[ -z "$devices" ]]; then
            log_error "Не найдено подключенных устройств"
            return 1
        fi
        device_id=$(echo "$devices" | head -n1)
        log_info "Используем устройство: $device_id"
    fi

    # Информация об устройстве
    get_device_info "$device_id"

    # Подготовка устройства
    prepare_device_for_testing "$device_id"

    # Установка APK
    if ! install_apks "$device_id"; then
        log_error "Не удалось установить APK"
        return 1
    fi

    # Запуск тестов
    local test_result
    if [[ -n "$test_class" ]]; then
        run_tests_adb "$device_id" "$test_class"
        test_result=$?
    else
        run_tests_gradle "$device_id"
        test_result=$?
    fi

    # Сбор результатов
    collect_allure_results "$device_id"
    collect_device_logs "$device_id"

    # Очистка
    if [[ "$use_emulator" == "true" ]]; then
        stop_all_emulators
    fi

    return $test_result
}

# Экспорт функций
export -f build_project install_apks run_tests_gradle run_tests_adb
export -f collect_allure_results collect_device_logs run_full_test_suite
