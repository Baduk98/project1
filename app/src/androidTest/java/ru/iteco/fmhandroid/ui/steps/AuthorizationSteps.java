package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.ui.elements.AuthorizationPage;

/**
 * Steps для страницы авторизации
 */
public class AuthorizationSteps {

    private final AuthorizationPage authorizationPage = new AuthorizationPage();

    /**
     * Ожидание загрузки экрана авторизации (поля логина)
     */
    public void waitForLoginLayout(int timeout) {
        Allure.step("Ожидание отображения поля логина");
        onView(isRoot())
                .perform(waitDisplayed(authorizationPage.loginLayout, timeout));
    }

    /**
     * Ожидание кнопки выхода (Authorization image button)
     */
    public void waitForAuthorizationImageButton(int timeout) {
        Allure.step("Ожидание кнопки Authorization image button");
        onView(isRoot())
                .perform(waitDisplayed(authorizationPage.authorizationImageButtonId, timeout));
    }

    /**
     * Загрузка страницы авторизации
     */
    public void loadAuthorizationPage() {
        Allure.step("Загрузка страницы авторизации");
        waitForLoginLayout(5000);
    }

    /**
     * Нажать кнопку "Войти"
     */
    public void clickButtonSignIn() {
        Allure.step("Нажать кнопку Войти");
        authorizationPage.enterButtonInteraction.perform(click());
    }

    /**
     * Нажать кнопку "Exit" (иконка авторизации)
     */
    public void clickButtonExit() {
        Allure.step("Нажать кнопку Exit");
        authorizationPage.exitButtonInteraction.perform(click());
    }

    /**
     * Нажать кнопку "Log out"
     */
    public void clickButtonLogOut() {
        Allure.step("Нажать кнопку Log out");
        authorizationPage.logOutButtonInteraction.perform(click());
    }

    /**
     * Проверить отображение текста "Authorization"
     */
    public void textAuthorization() {
        Allure.step("Проверить текст Authorization");
        authorizationPage.authorizationTextInteraction.check(matches(isDisplayed()));
    }

    /**
     * Ввести текст в поле логина
     */
    public void fillLoginField(String text) {
        Allure.step("Ввод в поле Логин: " + text);
        onView(isRoot())
                .perform(waitDisplayed(authorizationPage.loginLayout, 5000));
        authorizationPage.loginFieldInteraction.perform(replaceText(text));

    }

    /**
     * Ввести текст в поле пароля
     */
    public void fillPasswordField(String text) {
        Allure.step("Ввод в поле Пароль: " + text);
        authorizationPage.passwordFieldInteraction.perform(replaceText(text));
    }

    // Статические геттеры констант
    public static String getLogin() {
        return AuthorizationPage.rightLogin;
    }

    public static String getPassword() {
        return AuthorizationPage.rightPassword;
    }

    public static String getUnregisteredLogin() {
        return AuthorizationPage.unregisteredLogin;
    }

    public static String getUnregisteredPassword() {
        return AuthorizationPage.unregisteredPassword;
    }

    public static String getLoginWithSpecialCharacters() {
        return AuthorizationPage.loginWithSpecialCharacters;
    }

    public static String getPasswordWithSpecialCharacters() {
        return AuthorizationPage.passwordWithSpecialCharacters;
    }

    public static String getOneLetterLogin() {
        return AuthorizationPage.oneLetterLogin;
    }

    public static String getOneLetterPassword() {
        return AuthorizationPage.oneLetterPassword;
    }

    public static String getDifferentRegexLogin() {
        return AuthorizationPage.differentRegexLogin;
    }

    public static String getDifferentRegexPassword() {
        return AuthorizationPage.differentRegexPassword;
    }
}
