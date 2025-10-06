package ru.iteco.fmhandroid.ui.data;

/**
 * Класс для хранения всех константных строк, используемых в тестах
 */
public final class TestConstants {

    private TestConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final class AuthorizationMessages {
        public static final String LOGIN_PASSWORD_EMPTY = "Login and password cannot be empty";
        public static final String SOMETHING_WENT_WRONG = "Something went wrong. Try again later.";
    }

    public static final class NewsMessages {
        public static final String FILL_EMPTY_FIELDS = "Fill empty fields";
        public static final String SAVING_FAILED = "Saving failed. Try again later.";
    }

    public static final class NewsTexts {
        public static final String NEWS_TITLE = "News";
        public static final String ALL_NEWS = "ALL NEWS";
        public static final String NOT_ACTIVE = "NOT ACTIVE";
        public static final String NOT_ACTIVE_SWITCHER = "Not active";

        public static final String DESCRIPTION_POOL_CONSTRUCTION = "Строительство бассейна";
        public static final String DESCRIPTION_SALARY_ADVANCE = "Перечислен аванс";
        public static final String DESCRIPTION_DONATIONS = "За пожертвования";
        public static final String DESCRIPTION_VISIT = "Посещение";
        public static final String DESCRIPTION_ANNIVERSARY = "Юбилей";
    }

    public static final class TestTitles {
        public static final String EDITED_TITLE = "Отредактированный заголовок";
        public static final String EDITED_DESCRIPTION = "Отредактированное описание";
        public static final String TEST_TITLE = "Тестовый заголовок";
        public static final String TEST_DESCRIPTION = "Тестовое описание";
        public static final String TEST_NEWS_TITLE = "Тестовая новость";
        public static final String TEST_NEWS_DESCRIPTION = "Тестовое описание";
    }

    public static final class TestDates {
        public static final String FUTURE_DATE = "15.04.2026";
        public static final String SORT_DATE = "19.03.2025";
        public static final String TEST_DATE = "03.12.2024";
        public static final String TEST_TIME = "14:30";
    }

    public static final class Categories {
        public static final String ANNOUNCEMENT = "Объявление";
    }

    public static final class CommonTexts {
        public static final String AUTHORIZATION = "Authorization";
        public static final String LOG_OUT = "Log out";
        public static final String SAVE = "Save";
        public static final String OK = "OK";
        public static final String NEWS = "News";
        public static final String ALL_NEWS_UPPER = "ALL NEWS";
    }

    public static final class LoginData {
        public static final String RIGHT_LOGIN = "login2";
        public static final String RIGHT_PASSWORD = "password2";
        public static final String UNREGISTERED_LOGIN = "login235";
        public static final String LOGIN_WITH_SPECIAL_CHARACTERS = "№;%:?*(!№";
        public static final String ONE_LETTER_LOGIN = "l";
        public static final String DIFFERENT_REGEX_LOGIN = "LoGin2";
        public static final String UNREGISTERED_PASSWORD = "password123";
        public static final String PASSWORD_WITH_SPECIAL_CHARACTERS = "%:;%№*%:?";
        public static final String ONE_LETTER_PASSWORD = "p";
        public static final String DIFFERENT_REGEX_PASSWORD = "PassWord2";
    }

    public static final class NewsData {
        public static final String VALID_NEWS_TITLE = "Тестовая новость для автотестов";
        public static final String VALID_NEWS_DESCRIPTION = "Описание тестовой новости для проверки функциональности";
        public static final String VALID_NEWS_CATEGORY = Categories.ANNOUNCEMENT;
        public static final String VALID_NEWS_DATE = TestDates.TEST_DATE;
        public static final String VALID_NEWS_TIME = TestDates.TEST_TIME;
        public static final String EMPTY_TITLE = "";
        public static final String EMPTY_DESCRIPTION = "";
        public static final String LONG_TITLE = "Очень длинный заголовок новости который превышает максимально допустимое количество символов для поля заголовка и должен вызвать ошибку валидации при попытке сохранения такой новости в системе управления контентом";
        public static final String LONG_DESCRIPTION = "Очень длинное описание новости которое превышает максимально допустимое количество символов для поля описания и должно вызвать ошибку валидации при попытке сохранения такой новости в системе управления контентом мобильного приложения хоспис";
        public static final String SPECIAL_CHARACTERS_TITLE = "№;%:?*(!№%:;*";
        public static final String SPECIAL_CHARACTERS_DESCRIPTION = "Описание с спецсимволами !@#$%^&*()_+{}|:<>?";
        public static final String PAST_DATE = "01.01.2020";
        public static final String INVALID_DATE = "32.13.2024";
        public static final String INVALID_TIME = "25:70";
    }
}
