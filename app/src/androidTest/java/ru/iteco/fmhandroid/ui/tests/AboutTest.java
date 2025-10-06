package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getPassword;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.steps.AboutSteps;
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы для функционального тестирования вкладки \"О приложении\" мобильного приложения \"Мобильный хоспис\"")
public class AboutTest {

    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final AboutSteps aboutSteps = new AboutSteps();
    private final MainSteps mainSteps = new MainSteps();

    @Rule
    public IntentsTestRule<AppActivity> intentsTestRule =
            new IntentsTestRule<>(AppActivity.class);

    @Before
    public void setUp() {
        try {
            mainSteps.showTitleNewsOnMain();
        } catch (Exception e) {
            authorizationSteps.fillLoginField(getLogin());
            authorizationSteps.fillPasswordField(getPassword());
            authorizationSteps.clickButtonSignIn();
            mainSteps.showTitleNewsOnMain();
        }
    }

    @After
    public void tearDown() {
        pressBack();
    }

    @Test
    @Story("TC-53")
    @Description("Просмотр ссылки \"Политика конфиденциальности\" (Privacy policy) во вкладке \"О приложении\" (About)")
    public void watchingPrivacyPolicy() {
        onView(isRoot())
                .perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        aboutSteps.clickButtonAboutMainMenu();
        aboutSteps.clickButtonPrivacyPolicy();
        intended(hasData("https://vhospice.org/#/privacy-policy/"));
        intended(hasAction(Intent.ACTION_VIEW));
    }

    @Test
    @Story("TC-54")
    @Description("Просмотр ссылки \"Пользовательское соглашение\" (Terms of use) во вкладке \"О приложении\" (About)")
    public void watchingTermsOfUse() {
        onView(isRoot())
                .perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        aboutSteps.clickButtonAboutMainMenu();
        aboutSteps.clickButtonTermsOfUse();
        intended(hasData("https://vhospice.org/#/terms-of-use"));
        intended(hasAction(Intent.ACTION_VIEW));
    }
}
