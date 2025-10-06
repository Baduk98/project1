package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getDifferentRegexLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getDifferentRegexPassword;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getLoginWithSpecialCharacters;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getOneLetterLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getOneLetterPassword;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getPassword;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getPasswordWithSpecialCharacters;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getUnregisteredLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getUnregisteredPassword;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestConstants;
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы функционального тестирования вкладки «Авторизация» мобильного приложения «Мобильный хоспис»")
public class AuthorizationTest {

    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final MainSteps mainSteps = new MainSteps();

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private View decorView;

    @Before
    public void setUp() {
        activityRule.getScenario()
                .onActivity(activity -> decorView = activity.getWindow().getDecorView());
        try {
            authorizationSteps.loadAuthorizationPage();
        } catch (Exception e) {
            // Если страница уже залогинена, выйти
            authorizationSteps.clickButtonExit();
            authorizationSteps.clickButtonLogOut();
            authorizationSteps.loadAuthorizationPage();
        }
    }

    @After
    public void tearDown() {
        try {
            authorizationSteps.clickButtonExit();
            authorizationSteps.clickButtonLogOut();
        } catch (Exception ignored) {
        }
    }

    @Test
    @Story("TC-1")
    @Description("Авторизация с корректными учетными данными (Позитивный)")
    public void successfulAuthorization() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        authorizationSteps.waitForAuthorizationImageButton(3000);
        mainSteps.showTitleNewsOnMain();
        // Выход из приложения
        authorizationSteps.clickButtonExit();
        authorizationSteps.clickButtonLogOut();
    }

    @Test
    @Story("TC-2")
    @Description("Пустое поле Логин (Негативный)")
    public void loginFieldIsEmpty() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField("");
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.LOGIN_PASSWORD_EMPTY))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-3")
    @Description("Незарегистрированный логин (Негативный)")
    public void loginFieldUnregisteredUser() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getUnregisteredLogin());
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-4")
    @Description("Логин спецсимволами (Негативный)")
    public void loginFieldWithSpecialCharacters() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLoginWithSpecialCharacters());
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-5")
    @Description("Логин из одного символа (Негативный)")
    public void loginFieldOneLetter() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getOneLetterLogin());
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-6")
    @Description("Логин разного регистра (Негативный)")
    public void loginFieldLettersOfDifferentCase() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getDifferentRegexLogin());
        authorizationSteps.fillPasswordField(getPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-7")
    @Description("Пустое поле Пароль (Негативный)")
    public void passwordFieldIsEmpty() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField("");
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.LOGIN_PASSWORD_EMPTY))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-8")
    @Description("Незарегистрированный пароль (Негативный)")
    public void passwordFieldUnregisteredUser() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField(getUnregisteredPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-9")
    @Description("Пароль спецсимволами (Негативный)")
    public void passwordFieldWithSpecialCharacters() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField(getPasswordWithSpecialCharacters());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-10")
    @Description("Пароль из одного символа (Негативный)")
    public void passwordFieldOneLetter() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField(getOneLetterPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-11")
    @Description("Пароль разного регистра (Негативный)")
    public void passwordFieldLettersOfDifferentCase() {
        authorizationSteps.waitForLoginLayout(5000);
        authorizationSteps.textAuthorization();
        authorizationSteps.fillLoginField(getLogin());
        authorizationSteps.fillPasswordField(getDifferentRegexPassword());
        authorizationSteps.clickButtonSignIn();
        onView(withText(TestConstants.AuthorizationMessages.SOMETHING_WENT_WRONG))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }
}
