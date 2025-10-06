package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

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
import ru.iteco.fmhandroid.ui.steps.NewsSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы для проведения функционального тестирования вкладки \"Главная страница\" (Main) мобильного приложения \"Мобильный хоспис\".")
public class MainTest {

    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final MainSteps mainSteps = new MainSteps();
    private final NewsSteps newsSteps = new NewsSteps();

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void setUp() {
        try {
            mainSteps.showTitleNewsOnMain();
        } catch (Exception e) {
            authorizationSteps.fillLoginField(AuthorizationSteps.getLogin());
            authorizationSteps.fillPasswordField(AuthorizationSteps.getPassword());
            authorizationSteps.clickButtonSignIn();
            mainSteps.showTitleNewsOnMain();
        }
    }

    @Test
    @Story("TC - 12")
    @Description("Переход на вкладку \"Главная страница\" (Main) через главное меню мобильного приложения \"Мобильный хоспис\" (Позитивный).")
    public void mainPageTransition() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        newsSteps.clickButtonNews();
        onView(withText(TestConstants.CommonTexts.NEWS))
                .check(matches(isDisplayed()));

        mainSteps.clickMainMenuButton();
        mainSteps.clickAllNews();
        onView(withText(TestConstants.CommonTexts.NEWS))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC - 13")
    @Description("Свернуть/развернуть вкладку \"Новости\" (News) на вкладке \"Главная страница\" (Main) мобильного приложения \"Мобильный хоспис\" (Позитивный).")
    public void expandCollapseNews() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getExpandNewsButtonId(), 5000));
        mainSteps.clickExpandNewsButton();
        mainSteps.clickExpandNewsButton();
        onView(withId(R.id.all_news_text_view))
                .check(matches(withText(TestConstants.CommonTexts.ALL_NEWS_UPPER)));
    }
}
