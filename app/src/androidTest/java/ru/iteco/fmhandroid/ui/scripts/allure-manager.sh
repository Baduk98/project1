#!/bin/bash

# Загружаем конфигурацию
source "$(dirname "${BASH_SOURCE[0]}")/test-config.sh"

# =================================
# УПРАВЛЕНИЕ ALLURE ОТЧЕТАМИ
# =================================

# Функция генерации Allure отчета
generate_allure_report() {
    local results_dir="${1:-$ALLURE_RESULTS_DIR}"
    local report_dir="${2:-$ALLURE_REPORT_DIR}"

    log_step "Генерация Allure отчета..."

    # Проверяем наличие результатов
    if [[ ! -d "$results_dir" ]] || [[ -z "$(ls -A "$results_dir" 2>/dev/null)" ]]; then
        log_error "Директория с результатами пуста или не существует: $results_dir"
        return 1
    fi

    log_info "Результаты найдены в: $results_dir"
    log_info "Файлы результатов:"
    ls -la "$results_dir"

    # Создаем директорию для отчета
    mkdir -p "$report_dir"

    # Копируем историю из предыдущего отчета (если есть)
    if [[ -d "${report_dir}/history" ]]; then
        log_info "Копирование истории предыдущих запусков..."
        cp -r "${report_dir}/history" "${results_dir}/" 2>/dev/null || true
    fi

    # Генерируем отчет
    log_info "Генерация HTML отчета..."
    allure generate "$results_dir" --clean --output "$report_dir"

    local generate_status=$?

    if [[ $generate_status -eq 0 ]]; then
        log_success "Отчет сгенерирован в: $report_dir"

        # Выводим информацию о отчете
        local report_size
        report_size=$(du -sh "$report_dir" 2>/dev/null | cut -f1)
        log_info "Размер отчета: $report_size"

        # Проверяем основные файлы отчета
        if [[ -f "${report_dir}/index.html" ]]; then
            log_info "Главный файл отчета: ${report_dir}/index.html"
        fi

        return 0
    else
        log_error "Ошибка генерации отчета (код: $generate_status)"
        return 1
    fi
}

# Функция запуска Allure сервера
serve_allure_report() {
    local results_dir="${1:-$ALLURE_RESULTS_DIR}"
    local port="${2:-}"

    log_step "Запуск Allure сервера..."

    # Проверяем наличие результатов
    if [[ ! -d "$results_dir" ]] || [[ -z "$(ls -A "$results_dir" 2>/dev/null)" ]]; then
        log_error "Директория с результатами пуста или не существует: $results_dir"
        return 1
    fi

    # Формируем команду запуска
    local serve_command="allure serve \"$results_dir\""

    if [[ -n "$port" ]]; then
        serve_command="$serve_command --port $port"
    fi

    log_info "Команда запуска сервера: $serve_command"
    log_info "Сервер будет доступен в браузере (откроется автоматически)"
    log_info "Для остановки сервера нажмите Ctrl+C"

    # Запускаем сервер
    eval "$serve_command"
}

# Функция создания одностраничного отчета
generate_single_file_report() {
    local results_dir="${1:-$ALLURE_RESULTS_DIR}"
    local output_file="${2:-${REPORTS_DIR}/allure-report.html}"

    log_step "Генерация одностраничного отчета..."

    # Проверяем наличие результатов
    if [[ ! -d "$results_dir" ]] || [[ -z "$(ls -A "$results_dir" 2>/dev/null)" ]]; then
        log_error "Директория с результатами пуста или не существует: $results_dir"
        return 1
    fi

    # Создаем директорию для файла
    mkdir -p "$(dirname "$output_file")"

    # Генерируем одностраничный отчет
    allure generate "$results_dir" --single-file --clean --output "$(dirname "$output_file")"

    local generate_status=$?

    if [[ $generate_status -eq 0 ]]; then
        # Переименовываем файл
        if [[ -f "$(dirname "$output_file")/index.html" ]]; then
            mv "$(dirname "$output_file")/index.html" "$output_file"
            log_success "Одностраничный отчет создан: $output_file"

            local file_size
            file_size=$(du -sh "$output_file" 2>/dev/null | cut -f1)
            log_info "Размер файла: $file_size"

            return 0
        else
            log_error "Не найден сгенерированный файл index.html"
            return 1
        fi
    else
        log_error "Ошибка генерации одностраничного отчета (код: $generate_status)"
        return 1
    fi
}

# Функция добавления вложений к отчету
add_attachments_to_results() {
    local results_dir="${1:-$ALLURE_RESULTS_DIR}"
    local attachments_dir="${2:-${REPORTS_DIR}/attachments}"

    log_step "Добавление вложений к результатам..."

    if [[ ! -d "$attachments_dir" ]]; then
        log_info "Директория вложений не найдена: $attachments_dir"
        return 0
    fi

    # Копируем файлы вложений
    local attachment_count=0

    # Копируем скриншоты
    if [[ -d "${attachments_dir}/screenshots" ]]; then
        find "${attachments_dir}/screenshots" -type f \( -name "*.png" -o -name "*.jpg" -o -name "*.jpeg" \) -exec cp {} "$results_dir/" \; 2>/dev/null || true
        attachment_count=$((attachment_count + $(find "${attachments_dir}/screenshots" -type f \( -name "*.png" -o -name "*.jpg" -o -name "*.jpeg" \) | wc -l)))
    fi

    # Копируем логи
    if [[ -d "${attachments_dir}/logs" ]]; then
        find "${attachments_dir}/logs" -type f -name "*.txt" -exec cp {} "$results_dir/" \; 2>/dev/null || true
        attachment_count=$((attachment_count + $(find "${attachments_dir}/logs" -type f -name "*.txt" | wc -l)))
    fi

    # Копируем видео (если есть)
    if [[ -d "${attachments_dir}/videos" ]]; then
        find "${attachments_dir}/videos" -type f \( -name "*.mp4" -o -name "*.avi" -o -name "*.mov" \) -exec cp {} "$results_dir/" \; 2>/dev/null || true
        attachment_count=$((attachment_count + $(find "${attachments_dir}/videos" -type f \( -name "*.mp4" -o -name "*.avi" -o -name "*.mov" \) | wc -l)))
    fi

    if [[ $attachment_count -gt 0 ]]; then
        log_success "Добавлено $attachment_count вложений к результатам"
    else
        log_info "Вложения не найдены или не добавлены"
    fi
}

# Функция архивирования отчетов
archive_reports() {
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    local archive_dir="${REPORTS_DIR}/archive"
    local archive_name="test-results-${timestamp}.tar.gz"

    log_step "Архивирование отчетов..."

    mkdir -p "$archive_dir"

    # Создаем архив с результатами и отчетами
    tar -czf "${archive_dir}/${archive_name}" \
        -C "$BUILD_DIR" \
        "$(basename "$ALLURE_RESULTS_DIR")" \
        "$(basename "$ALLURE_REPORT_DIR")" \
        "$(basename "$REPORTS_DIR")" \
        2>/dev/null || true

    local archive_size
    archive_size=$(du -sh "${archive_dir}/${archive_name}" 2>/dev/null | cut -f1)

    log_success "Архив создан: ${archive_dir}/${archive_name} ($archive_size)"
}

# Функция очистки старых отчетов
cleanup_old_reports() {
    local days_to_keep="${1:-7}"

    log_step "Очистка отчетов старше $days_to_keep дней..."

    local archive_dir="${REPORTS_DIR}/archive"

    if [[ -d "$archive_dir" ]]; then
        local deleted_count
        deleted_count=$(find "$archive_dir" -name "test-results-*.tar.gz" -mtime +$days_to_keep -delete -print | wc -l)

        if [[ $deleted_count -gt 0 ]]; then
            log_success "Удалено $deleted_count старых архивов"
        else
            log_info "Старые архивы не найдены"
        fi
    fi
}

# Функция получения статистики отчета
get_report_statistics() {
    local results_dir="${1:-$ALLURE_RESULTS_DIR}"

    log_step "Получение статистики тестирования..."

    if [[ ! -d "$results_dir" ]]; then
        log_error "Директория результатов не найдена: $results_dir"
        return 1
    fi

    # Подсчитываем файлы результатов
    local result_files
    result_files=$(find "$results_dir" -name "*-result.json" 2>/dev/null | wc -l)

    # Подсчитываем контейнеры
    local container_files
    container_files=$(find "$results_dir" -name "*-container.json" 2>/dev/null | wc -l)

    # Подсчитываем вложения
    local attachment_files
    attachment_files=$(find "$results_dir" -name "*-attachment.*" 2>/dev/null | wc -l)

    log_info "Статистика результатов:"
    echo "  Файлов результатов: $result_files"
    echo "  Файлов контейнеров: $container_files"
    echo "  Файлов вложений: $attachment_files"

    # Пытаемся получить более детальную статистику из JSON файлов
    if command -v jq &> /dev/null && [[ $result_files -gt 0 ]]; then
        log_info "Детальная статистика тестов:"

        local passed_count=0
        local failed_count=0
        local broken_count=0
        local skipped_count=0

        for result_file in "$results_dir"/*-result.json; do
            if [[ -f "$result_file" ]]; then
                local status
                status=$(jq -r '.status // "unknown"' "$result_file" 2>/dev/null)
                case "$status" in
                    "passed") passed_count=$((passed_count + 1)) ;;
                    "failed") failed_count=$((failed_count + 1)) ;;
                    "broken") broken_count=$((broken_count + 1)) ;;
                    "skipped") skipped_count=$((skipped_count + 1)) ;;
                esac
            fi
        done

        local total_tests=$((passed_count + failed_count + broken_count + skipped_count))

        echo "  Всего тестов: $total_tests"
        echo "  Прошло: $passed_count"
        echo "  Провалилось: $failed_count"
        echo "  Сломано: $broken_count"
        echo "  Пропущено: $skipped_count"

        if [[ $total_tests -gt 0 ]]; then
            local success_rate=$((passed_count * 100 / total_tests))
            echo "  Процент успешных: ${success_rate}%"
        fi
    fi
}

# Экспорт функций
export -f generate_allure_report serve_allure_report generate_single_file_report
export -f add_attachments_to_results archive_reports cleanup_old_reports get_report_statistics
