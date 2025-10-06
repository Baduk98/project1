package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
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
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestConstants;
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;
import ru.iteco.fmhandroid.ui.steps.NewsControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.NewsSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы для функционального тестирования вкладки «Управление новостями»")
public class NewsControlPanelTest {

    private final AuthorizationSteps authSteps = new AuthorizationSteps();
    private final MainSteps mainSteps = new MainSteps();
    private final NewsSteps newsSteps = new NewsSteps();
    private final NewsControlPanelSteps panelSteps = new NewsControlPanelSteps();
    private View decorView;

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity ->
                decorView = activity.getWindow().getDecorView());
        try {
            authSteps.loadAuthorizationPage();
        } catch (Exception e) {
            authSteps.clickButtonExit();
            authSteps.clickButtonLogOut();
            authSteps.loadAuthorizationPage();
        }
        authSteps.waitForLoginLayout(5000);
        authSteps.fillLoginField(AuthorizationSteps.getLogin());
        authSteps.fillPasswordField(AuthorizationSteps.getPassword());
        authSteps.clickButtonSignIn();
        authSteps.waitForAuthorizationImageButton(3000);

        mainSteps.clickAllNews();
        newsSteps.waitForNewsList(5000);
        newsSteps.clickExpandFirstNews();
        panelSteps.waitForAddNewsButton(5000);
    }

    @After
    public void tearDown() {
        try {
            authSteps.clickButtonExit();
            authSteps.clickButtonLogOut();
        } catch (Exception ignored) { }
    }

    @Test
    @Story("TC-37")
    @Description("Создание новости с валидными данными (Позитивный)")
    public void createValidNews() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.verifyAddNewsButtonIsDisplayed();
        panelSteps.createValidNews();
        panelSteps.waitForNewsRecyclerView(3000);
        panelSteps.verifyNewsListIsDisplayed();
    }

    @Test
    @Story("TC-38")
    @Description("Попытка создания новости с пустым заголовком (Негативный)")
    public void createNewsWithEmptyTitle() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithEmptyTitle();
        onView(withText(TestConstants.NewsMessages.FILL_EMPTY_FIELDS))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-39")
    @Description("Попытка создания новости с пустым описанием (Негативный)")
    public void createNewsWithEmptyDescription() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithEmptyDescription();
        onView(withText(TestConstants.NewsMessages.FILL_EMPTY_FIELDS))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-40")
    @Description("Создание новости с будущей датой (Позитивный)")
    public void createNewsWithFutureDate() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithFutureDate();
        panelSteps.waitForNewsRecyclerView(3000);
        panelSteps.verifyNewsListIsDisplayed();
    }

    @Test
    @Story("TC-41")
    @Description("Попытка создания новости со слишком длинным заголовком (Негативный)")
    public void createNewsWithLongTitle() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithLongTitle();
        onView(withText(TestConstants.NewsMessages.SAVING_FAILED))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-42")
    @Description("Попытка создания новости со слишком длинным описанием (Негативный)")
    public void createNewsWithLongDescription() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithLongDescription();
        onView(withText(TestConstants.NewsMessages.SAVING_FAILED))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-43")
    @Description("Создание новости со спецсимволами (Негативный)")
    public void createNewsWithSpecialCharacters() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithSpecialCharacters();
        onView(withText(TestConstants.NewsMessages.SAVING_FAILED))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-44")
    @Description("Попытка создания новости с некорректной датой (Негативный)")
    public void createNewsWithInvalidDate() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithInvalidDate();
        onView(withText(TestConstants.NewsMessages.SAVING_FAILED))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    @Story("TC-45")
    @Description("Попытка создания новости с некорректным временем (Негативный)")
    public void createNewsWithInvalidTime() {
        panelSteps.verifyNewsControlPanelIsDisplayed();
        panelSteps.createNewsWithInvalidTime();
        onView(withText(TestConstants.NewsMessages.SAVING_FAILED))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        pressBack();
    }
}
