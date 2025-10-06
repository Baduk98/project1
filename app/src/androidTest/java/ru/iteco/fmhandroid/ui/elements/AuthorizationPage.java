package ru.iteco.fmhandroid.ui.elements;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestConstants;

/**
 * Страница авторизации
 */
public class AuthorizationPage {

    public final ViewInteraction enterButtonInteraction;
    public final ViewInteraction loginFieldInteraction;
    public final ViewInteraction passwordFieldInteraction;
    public final ViewInteraction exitButtonInteraction;
    public final ViewInteraction logOutButtonInteraction;
    public final ViewInteraction authorizationTextInteraction;

    public final int loginLayout;
    public final int enterButtonId;
    public final int authorizationImageButtonId;

    public static final String rightLogin;
    public static final String rightPassword;
    public static final String unregisteredLogin;
    public static final String loginWithSpecialCharacters;
    public static final String oneLetterLogin;
    public static final String differentRegexLogin;
    public static final String unregisteredPassword;
    public static final String passwordWithSpecialCharacters;
    public static final String oneLetterPassword;
    public static final String differentRegexPassword;

    static {
        rightLogin = TestConstants.LoginData.RIGHT_LOGIN;
        rightPassword = TestConstants.LoginData.RIGHT_PASSWORD;
        unregisteredLogin = TestConstants.LoginData.UNREGISTERED_LOGIN;
        loginWithSpecialCharacters = TestConstants.LoginData.LOGIN_WITH_SPECIAL_CHARACTERS;
        oneLetterLogin = TestConstants.LoginData.ONE_LETTER_LOGIN;
        differentRegexLogin = TestConstants.LoginData.DIFFERENT_REGEX_LOGIN;
        unregisteredPassword = TestConstants.LoginData.UNREGISTERED_PASSWORD;
        passwordWithSpecialCharacters = TestConstants.LoginData.PASSWORD_WITH_SPECIAL_CHARACTERS;
        oneLetterPassword = TestConstants.LoginData.ONE_LETTER_PASSWORD;
        differentRegexPassword = TestConstants.LoginData.DIFFERENT_REGEX_PASSWORD;
    }

    public AuthorizationPage() {
        enterButtonInteraction = onView(withId(R.id.enter_button));
        loginFieldInteraction = onView(allOf(
                withHint("Login"),
                withParent(withParent(withId(R.id.login_text_input_layout)))
        ));
        passwordFieldInteraction = onView(allOf(
                withHint("Password"),
                withParent(withParent(withId(R.id.password_text_input_layout)))
        ));
        exitButtonInteraction = onView(withId(R.id.authorization_image_button));
        logOutButtonInteraction = onView(allOf(
                withId(android.R.id.title),
                withText(TestConstants.CommonTexts.LOG_OUT)
        ));
        authorizationTextInteraction = onView(allOf(
                withText(TestConstants.CommonTexts.AUTHORIZATION),
                withParent(withParent(withId(R.id.nav_host_fragment)))
        ));

        loginLayout = R.id.login_text_input_layout;
        enterButtonId = R.id.enter_button;
        authorizationImageButtonId = R.id.authorization_image_button;
    }
}
