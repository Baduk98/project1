package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.Description;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestConstants;
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;
import ru.iteco.fmhandroid.ui.steps.NewsSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы для функционального тестирования вкладки \"Новости\" (News)")
public class NewsTest {

    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final MainSteps mainSteps = new MainSteps();
    private final NewsSteps newsSteps = new NewsSteps();
    private View decorView;

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario()
                .onActivity(activity -> decorView = activity.getWindow().getDecorView());
        try {
            mainSteps.showTitleNewsOnMain();
        } catch (Exception e) {
            authorizationSteps.fillLoginField(AuthorizationSteps.getLogin());
            authorizationSteps.fillPasswordField(AuthorizationSteps.getPassword());
            authorizationSteps.clickButtonSignIn();
            mainSteps.showTitleNewsOnMain();
        }
    }

    // TC - 14: переход во вкладку "Все новости" через меню
    @Test
    @Story("TC-14")
    @Description("Переход во вкладку \"Все новости\" через главное меню (Позитивный)")
    public void transferToAllNewsThroughMainMenu() {
        onView(isRoot())
                .perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        newsSteps.clickButtonNews();
        newsSteps.checkNewsTitle();

        // onView(withText(TestConstants.CommonTexts.ALL_NEWS_UPPER))
               // .check(matches(isDisplayed()));
    }

    // TC - 15: переход во вкладку "Все новости" через главный экран
    @Test
    @Story("TC-15")
    @Description("Переход во вкладку \"Все новости\" через кнопку на главной странице (Позитивный)")
    public void transferToAllNewsThroughMainPage() {
        onView(isRoot())
                .perform(waitDisplayed(mainSteps.getAllNewsButtonId(), 5000));
        mainSteps.showButtonAllNews();
        mainSteps.clickButtonAllNews();
        newsSteps.checkNewsTitle();
    }
}
