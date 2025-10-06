#!/bin/bash

# Загружаем конфигурацию
source "$(dirname "${BASH_SOURCE[0]}")/test-config.sh"

# =================================
# УПРАВЛЕНИЕ ANDROID УСТРОЙСТВАМИ
# =================================

# Функция получения списка подключенных устройств
get_connected_devices() {
    "$ADB" devices | grep -E '\tdevice$' | cut -f1
}

# Функция проверки доступности устройства
is_device_ready() {
    local device_id="$1"
    local boot_completed=$("$ADB" -s "$device_id" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r\n')
    [[ "$boot_completed" == "1" ]]
}

# Функция ожидания готовности устройства
wait_for_device_ready() {
    local device_id="$1"
    local timeout="${2:-$DEVICE_READY_TIMEOUT}"

    log_step "Ожидание готовности устройства $device_id..."

    local elapsed=0
    while [[ $elapsed -lt $timeout ]]; do
        if is_device_ready "$device_id"; then
            log_success "Устройство $device_id готово"
            return 0
        fi

        log_info "Устройство $device_id еще не готово. Ожидание... ($elapsed/$timeout сек)"
        sleep 5
        elapsed=$((elapsed + 5))
    done

    log_error "Устройство $device_id не готово после $timeout секунд"
    return 1
}

# Функция запуска эмулятора
start_emulator() {
    local avd_name="${1:-$EMULATOR_NAME}"

    log_step "Запуск эмулятора $avd_name..."

    # Проверяем, есть ли уже запущенный эмулятор
    local running_devices
    running_devices=$(get_connected_devices)
    if [[ -n "$running_devices" ]]; then
        log_info "Найдены запущенные устройства: $running_devices"
        for device in $running_devices; do
            if wait_for_device_ready "$device" 30; then
                log_success "Используем уже запущенное устройство: $device"
                echo "$device"
                return 0
            fi
        done
    fi

    # Запускаем новый эмулятор
    log_info "Запуск нового эмулятора..."
    "$EMULATOR" -avd "$avd_name" -no-snapshot-save -no-snapshot-load -wipe-data -gpu swiftshader_indirect &
    local emulator_pid=$!

    # Ждем появления устройства
    log_step "Ожидание появления эмулятора в списке устройств..."
    local elapsed=0
    while [[ $elapsed -lt $EMULATOR_BOOT_TIMEOUT ]]; do
        local devices
        devices=$(get_connected_devices)
        if [[ -n "$devices" ]]; then
            for device in $devices; do
                if [[ "$device" =~ emulator- ]]; then
                    log_info "Эмулятор найден: $device"
                    if wait_for_device_ready "$device" 120; then
                        log_success "Эмулятор $device готов к работе"
                        echo "$device"
                        return 0
                    fi
                fi
            done
        fi

        log_info "Эмулятор загружается... ($elapsed/$EMULATOR_BOOT_TIMEOUT сек)"
        sleep 10
        elapsed=$((elapsed + 10))
    done

    log_error "Эмулятор не запустился в течение $EMULATOR_BOOT_TIMEOUT секунд"
    kill $emulator_pid 2>/dev/null || true
    return 1
}

# Функция остановки всех эмуляторов
stop_all_emulators() {
    log_step "Остановка всех эмуляторов..."

    local devices
    devices=$(get_connected_devices)
    for device in $devices; do
        if [[ "$device" =~ emulator- ]]; then
            log_info "Остановка эмулятора $device"
            "$ADB" -s "$device" emu kill 2>/dev/null || true
        fi
    done

    # Принудительно убиваем процессы эмулятора
    pkill -f "qemu-system" 2>/dev/null || true
    pkill -f "emulator" 2>/dev/null || true

    log_success "Эмуляторы остановлены"
}

# Функция подготовки устройства для тестирования
prepare_device_for_testing() {
    local device_id="$1"

    log_step "Подготовка устройства $device_id для тестирования..."

    # Разблокируем экран
    "$ADB" -s "$device_id" shell input keyevent KEYCODE_WAKEUP
    "$ADB" -s "$device_id" shell input keyevent KEYCODE_MENU

    # Отключаем анимации
    "$ADB" -s "$device_id" shell settings put global window_animation_scale 0
    "$ADB" -s "$device_id" shell settings put global transition_animation_scale 0
    "$ADB" -s "$device_id" shell settings put global animator_duration_scale 0

    # Устанавливаем язык на английский (опционально)
    "$ADB" -s "$device_id" shell "setprop persist.sys.locale en-US; setprop persist.sys.language en; setprop persist.sys.country US"

    # Очищаем логи
    "$ADB" -s "$device_id" logcat -c

    log_success "Устройство $device_id подготовлено для тестирования"
}

# Функция получения информации об устройстве
get_device_info() {
    local device_id="$1"

    log_info "Информация об устройстве $device_id:"
    echo "  Android версия: $("$ADB" -s "$device_id" shell getprop ro.build.version.release)"
    echo "  API уровень: $("$ADB" -s "$device_id" shell getprop ro.build.version.sdk)"
    echo "  Модель: $("$ADB" -s "$device_id" shell getprop ro.product.model)"
    echo "  Производитель: $("$ADB" -s "$device_id" shell getprop ro.product.manufacturer)"
    echo "  Архитектура: $("$ADB" -s "$device_id" shell getprop ro.product.cpu.abi)"
}

# Экспорт функций
export -f get_connected_devices is_device_ready wait_for_device_ready
export -f start_emulator stop_all_emulators prepare_device_for_testing get_device_info
