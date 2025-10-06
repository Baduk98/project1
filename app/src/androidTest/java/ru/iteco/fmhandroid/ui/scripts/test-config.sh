#!/bin/bash

# =================================
# КОНФИГУРАЦИЯ ТЕСТОВОГО ОКРУЖЕНИЯ
# =================================

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Пути и директории
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_MODULE="app"
BUILD_DIR="${PROJECT_ROOT}/build"
REPORTS_DIR="${BUILD_DIR}/reports"
ALLURE_RESULTS_DIR="${BUILD_DIR}/allure-results"
ALLURE_REPORT_DIR="${BUILD_DIR}/allure-report"
SCRIPTS_DIR="${PROJECT_ROOT}/scripts"

# Настройки Android
ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
ADB="${ANDROID_HOME}/platform-tools/adb"
EMULATOR="${ANDROID_HOME}/emulator/emulator"

# Настройки Gradle
GRADLE_WRAPPER="${PROJECT_ROOT}/gradlew"
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    GRADLE_WRAPPER="${PROJECT_ROOT}/gradlew.bat"
fi

# Настройки тестирования
TEST_RUNNER="io.qameta.allure.android.runners.AllureAndroidJUnit4"
TEST_PACKAGE="ru.iteco.fmhandroid.test"
APP_PACKAGE="ru.iteco.fmhandroid"

# Настройки эмулятора
EMULATOR_NAME="test_emulator_api_29"
EMULATOR_DEVICE="Nexus_5X"
EMULATOR_API_LEVEL="29"
EMULATOR_TARGET="android-${EMULATOR_API_LEVEL}"

# Таймауты (в секундах)
EMULATOR_BOOT_TIMEOUT=300
DEVICE_READY_TIMEOUT=60
TEST_EXECUTION_TIMEOUT=1800

# Функции для логирования
log_info() {
    echo -e "${BLUE}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_step() {
    echo -e "${CYAN}[STEP]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# Функция проверки зависимостей
check_dependencies() {
    log_step "Проверка зависимостей..."

    # Проверка Java
    if ! command -v java &> /dev/null; then
        log_error "Java не найдена. Установите Java 8 или выше."
        exit 1
    fi

    # Проверка Android SDK
    if [[ ! -d "$ANDROID_HOME" ]]; then
        log_error "Android SDK не найден в $ANDROID_HOME"
        exit 1
    fi

    # Проверка ADB
    if [[ ! -f "$ADB" ]]; then
        log_error "ADB не найден в $ADB"
        exit 1
    fi

    # Проверка Allure
    if ! command -v allure &> /dev/null; then
        log_error "Allure CLI не найден. Установите Allure."
        exit 1
    fi

    # Проверка Gradle Wrapper
    if [[ ! -f "$GRADLE_WRAPPER" ]]; then
        log_error "Gradle Wrapper не найден в $GRADLE_WRAPPER"
        exit 1
    fi

    log_success "Все зависимости найдены"
}

# Функция создания директорий
create_directories() {
    log_step "Создание необходимых директорий..."
    mkdir -p "$BUILD_DIR"
    mkdir -p "$REPORTS_DIR"
    mkdir -p "$ALLURE_RESULTS_DIR"
    mkdir -p "$ALLURE_REPORT_DIR"
    log_success "Директории созданы"
}

# Функция очистки предыдущих результатов
cleanup_previous_results() {
    log_step "Очистка предыдущих результатов..."
    rm -rf "$ALLURE_RESULTS_DIR"/*
    rm -rf "$ALLURE_REPORT_DIR"/*
    "$ADB" shell rm -rf /sdcard/allure-results/* 2>/dev/null || true
    log_success "Предыдущие результаты очищены"
}

# Экспорт переменных
export PROJECT_ROOT BUILD_DIR REPORTS_DIR ALLURE_RESULTS_DIR ALLURE_REPORT_DIR
export ANDROID_HOME ADB EMULATOR GRADLE_WRAPPER
export TEST_RUNNER TEST_PACKAGE APP_PACKAGE
export EMULATOR_NAME EMULATOR_DEVICE EMULATOR_API_LEVEL EMULATOR_TARGET
export EMULATOR_BOOT_TIMEOUT DEVICE_READY_TIMEOUT TEST_EXECUTION_TIMEOUT
