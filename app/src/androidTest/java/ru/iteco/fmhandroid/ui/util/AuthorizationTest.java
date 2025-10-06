package ru.iteco.fmhandroid.ui.util;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import android.os.SystemClock;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;

/**
 * Утильный класс с простыми UI-тестами авторизации.
 */
@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class AuthorizationTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private View decorView;
    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<AppActivity>() {
            @Override
            public void perform(AppActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    private final ViewInteraction textAuthorization = onView(
            allOf(
                    withText("Authorization"),
                    withParent(withParent(withId(R.id.nav_host_fragment)))
            )
    );
    private final ViewInteraction loginField = onView(
            allOf(
                    withHint("Login"),
                    withParent(withParent(withId(R.id.login_text_input_layout)))
            )
    );
    private final ViewInteraction passwordField = onView(
            allOf(
                    withHint("Password"),
                    withParent(withParent(withId(R.id.password_text_input_layout)))
            )
    );
    private final ViewInteraction buttonEnter = onView(withId(R.id.enter_button));
    private final ViewInteraction newsText = onView(
            allOf(
                    withText("News"),
                    withParent(withParent(withId(R.id.container_list_news_include_on_fragment_main)))
            )
    );
    private final ViewInteraction buttonExit = onView(withId(R.id.authorization_image_button));
    private final ViewInteraction buttonLogOut = onView(
            allOf(
                    withId(android.R.id.title),
                    withText("Log out")
            )
    );

    private static final String RIGHT_LOGIN = "login2";
    private static final String RIGHT_PASSWORD = "password2";

    @Test
    public void successfulAuthorization() {
        // Ждём пока откроется экран авторизации
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        // Ввод корректных данных
        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        // Ждём, пока откроется главный экран
        SystemClock.sleep(2000);
        newsText.check(matches(isDisplayed()));

        // Выход
        SystemClock.sleep(2000);
        buttonExit.perform(click());
        SystemClock.sleep(2000);
        buttonLogOut.perform(click());
    }

    @Test
    public void loginFieldEmpty_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(""), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withText("Login and password cannot be empty"))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void unregisteredLogin_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText("login253"), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginSpecialChars_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText("№;%:?*(!^№;"), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginOneLetter_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText("l"), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void loginDifferentCase_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText("LoGin2"), closeSoftKeyboard());
        passwordField.perform(replaceText(RIGHT_PASSWORD), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void passwordEmpty_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText(""), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Login and password cannot be empty"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void unregisteredPassword_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText("password123"), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void passwordSpecialChars_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText("%:;%№*%:?"), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void passwordOneLetter_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText("p"), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void passwordDifferentCase_ShowsError() {
        SystemClock.sleep(2000);
        textAuthorization.check(matches(isDisplayed()));

        loginField.perform(replaceText(RIGHT_LOGIN), closeSoftKeyboard());
        passwordField.perform(replaceText("PassWord2"), closeSoftKeyboard());
        buttonEnter.perform(click());

        SystemClock.sleep(500);
        onView(withContentDescription("Something went wrong. Try again later."))
                .check(matches(isDisplayed()));
    }
}
