#!/bin/bash

# =================================
# ГЛАВНЫЙ СКРИПТ ЗАПУСКА ТЕСТОВ
# =================================

# Получаем директорию скрипта
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Загружаем все модули
source "$SCRIPT_DIR/scripts/test-config.sh"
source "$SCRIPT_DIR/scripts/device-manager.sh"
source "$SCRIPT_DIR/scripts/test-runner.sh"
source "$SCRIPT_DIR/scripts/allure-manager.sh"

# Функция показа помощи
show_help() {
    echo "Использование: $0 [ОПЦИИ]"
    echo ""
    echo "ОПЦИИ:"
    echo "  -h, --help              Показать эту справку"
    echo "  -e, --emulator          Использовать эмулятор (по умолчанию)"
    echo "  -d, --device            Использовать реальное устройство"
    echo "  -c, --class CLASS       Запустить конкретный тестовый класс"
    echo "  -s, --serve             Запустить Allure сервер после генерации отчета"
    echo "  -o, --open              Открыть отчет в браузере"
    echo "  -a, --archive           Создать архив с результатами"
    echo "  -r, --report-only       Только сгенерировать отчет из существующих результатов"
    echo "  -p, --port PORT         Порт для Allure сервера (по умолчанию случайный)"
    echo "  --single-file           Создать одностраничный HTML отчет"
    echo "  --cleanup-days DAYS     Очистить архивы старше указанного количества дней"
    echo ""
    echo "ПРИМЕРЫ:"
    echo "  $0                      Запустить все тесты на эмуляторе"
    echo "  $0 -d -s                Запустить все тесты на устройстве и показать отчет"
    echo "  $0 -c AuthorizationTest Запустить только AuthorizationTest"
    echo "  $0 -r -o                Сгенерировать отчет из существующих результатов"
    echo "  $0 --single-file        Создать одностраничный HTML отчет"
}

# Разбор аргументов командной строки
USE_EMULATOR=true
TEST_CLASS=""
SERVE_REPORT=false
OPEN_REPORT=false
ARCHIVE_RESULTS=false
REPORT_ONLY=false
SERVER_PORT=""
SINGLE_FILE_REPORT=false
CLEANUP_DAYS=""

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -e|--emulator)
            USE_EMULATOR=true
            shift
            ;;
        -d|--device)
            USE_EMULATOR=false
            shift
            ;;
        -c|--class)
            TEST_CLASS="$2"
            shift 2
            ;;
        -s|--serve)
            SERVE_REPORT=true
            shift
            ;;
        -o|--open)
            OPEN_REPORT=true
            shift
            ;;
        -a|--archive)
            ARCHIVE_RESULTS=true
            shift
            ;;
        -r|--report-only)
            REPORT_ONLY=true
            shift
            ;;
        -p|--port)
            SERVER_PORT="$2"
            shift 2
            ;;
        --single-file)
            SINGLE_FILE_REPORT=true
            shift
            ;;
        --cleanup-days)
            CLEANUP_DAYS="$2"
            shift 2
            ;;
        *)
            log_error "Неизвестная опция: $1"
            show_help
            exit 1
            ;;
    esac
done

# Функция обработки сигналов прерывания
cleanup_on_exit() {
    log_warning "Получен сигнал прерывания. Очистка..."

    # Остановка эмуляторов
    if [[ "$USE_EMULATOR" == "true" ]]; then
        stop_all_emulators
    fi

    # Убиваем фоновые процессы
    jobs -p | xargs -r kill 2>/dev/null || true

    log_info "Очистка завершена"
    exit 130
}

# Устанавливаем обработчики сигналов
trap cleanup_on_exit SIGINT SIGTERM

# Главная функция
main() {
    # Заголовок
    echo "========================================"
    echo "  АВТОМАТИЗИРОВАННОЕ ТЕСТИРОВАНИЕ"
    echo "  Мобильное приложение 'Мобильный хоспис'"
    echo "========================================"
    echo ""

    log_info "Начало выполнения скрипта: $(date)"
    log_info "Рабочая директория: $PROJECT_ROOT"

    # Очистка старых архивов (если указано)
    if [[ -n "$CLEANUP_DAYS" ]]; then
        cleanup_old_reports "$CLEANUP_DAYS"
    fi

    # Режим "только отчет"
    if [[ "$REPORT_ONLY" == "true" ]]; then
        log_info "Режим: только генерация отчета"

        # Проверяем наличие результатов
        if [[ ! -d "$ALLURE_RESULTS_DIR" ]] || [[ -z "$(ls -A "$ALLURE_RESULTS_DIR" 2>/dev/null)" ]]; then
            log_error "Результаты для генерации отчета не найдены в $ALLURE_RESULTS_DIR"
            exit 1
        fi

        # Получаем статистику
        get_report_statistics

        # Генерируем отчет
        if [[ "$SINGLE_FILE_REPORT" == "true" ]]; then
            generate_single_file_report
        else
            generate_allure_report
        fi

        # Архивирование (если нужно)
        if [[ "$ARCHIVE_RESULTS" == "true" ]]; then
            archive_reports
        fi

        # Открытие отчета
        if [[ "$SERVE_REPORT" == "true" ]]; then
            serve_allure_report "$ALLURE_RESULTS_DIR" "$SERVER_PORT"
        elif [[ "$OPEN_REPORT" == "true" ]] && [[ -f "${ALLURE_REPORT_DIR}/index.html" ]]; then
            log_info "Открытие отчета в браузере..."
            if command -v xdg-open &> /dev/null; then
                xdg-open "${ALLURE_REPORT_DIR}/index.html"
            elif command -v open &> /dev/null; then
                open "${ALLURE_REPORT_DIR}/index.html"
            elif command -v start &> /dev/null; then
                start "${ALLURE_REPORT_DIR}/index.html"
            else
                log_info "Откройте файл в браузере: ${ALLURE_REPORT_DIR}/index.html"
            fi
        fi

        log_success "Генерация отчета завершена"
        exit 0
    fi

    # Обычный режим - запуск тестов
    log_info "Режим: полный запуск тестов"
    log_info "Использовать эмулятор: $USE_EMULATOR"
    if [[ -n "$TEST_CLASS" ]]; then
        log_info "Тестовый класс: $TEST_CLASS"
    else
        log_info "Запуск: все тесты"
    fi

    # Запуск тестов
    local test_start_time=$(date +%s)

    if run_full_test_suite "$USE_EMULATOR" "$TEST_CLASS"; then
        local test_result="УСПЕШНО"
        local test_exit_code=0
    else
        local test_result="С ОШИБКАМИ"
        local test_exit_code=1
    fi

    local test_end_time=$(date +%s)
    local test_duration=$((test_end_time - test_start_time))
    local test_duration_formatted=$(printf '%02d:%02d:%02d' $((test_duration/3600)) $((test_duration%3600/60)) $((test_duration%60)))

    log_info "Результат тестирования: $test_result"
    log_info "Время выполнения: $test_duration_formatted"

    # Получаем статистику результатов
    get_report_statistics

    # Генерация отчета
    log_step "Генерация Allure отчета..."

    if [[ "$SINGLE_FILE_REPORT" == "true" ]]; then
        if generate_single_file_report; then
            log_success "Одностраничный отчет создан"
        else
            log_error "Ошибка создания одностраничного отчета"
        fi
    else
        if generate_allure_report; then
            log_success "Allure отчет сгенерирован"
        else
            log_error "Ошибка генерации Allure отчета"
        fi
    fi

    # Архивирование результатов
    if [[ "$ARCHIVE_RESULTS" == "true" ]]; then
        archive_reports
    fi

    # Открытие отчета
    if [[ "$SERVE_REPORT" == "true" ]]; then
        log_info "Запуск Allure сервера..."
        serve_allure_report "$ALLURE_RESULTS_DIR" "$SERVER_PORT"
    elif [[ "$OPEN_REPORT" == "true" ]]; then
        if [[ "$SINGLE_FILE_REPORT" == "true" ]]; then
            local report_file="${REPORTS_DIR}/allure-report.html"
        else
            local report_file="${ALLURE_REPORT_DIR}/index.html"
        fi

        if [[ -f "$report_file" ]]; then
            log_info "Открытие отчета в браузере: $report_file"
            if command -v xdg-open &> /dev/null; then
                xdg-open "$report_file"
            elif command -v open &> /dev/null; then
                open "$report_file"
            elif command -v start &> /dev/null; then
                start "$report_file"
            else
                log_info "Откройте файл в браузере: $report_file"
            fi
        else
            log_error "Файл отчета не найден: $report_file"
        fi
    fi

    # Финальная информация
    echo ""
    log_info "Завершение выполнения скрипта: $(date)"
    log_info "Результаты сохранены в: $BUILD_DIR"

    if [[ ! "$SERVE_REPORT" == "true" ]]; then
        if [[ "$SINGLE_FILE_REPORT" == "true" ]]; then
            log_info "Одностраничный отчет: ${REPORTS_DIR}/allure-report.html"
        else
            log_info "HTML отчет: ${ALLURE_REPORT_DIR}/index.
