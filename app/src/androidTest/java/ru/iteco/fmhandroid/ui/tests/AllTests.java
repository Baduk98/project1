package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getPassword;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.intent.rule.IntentsTestRule;
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
import ru.iteco.fmhandroid.ui.steps.AboutSteps;
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;
import ru.iteco.fmhandroid.ui.steps.NewsControlPanelSteps;
import ru.iteco.fmhandroid.ui.steps.NewsSteps;
import ru.iteco.fmhandroid.ui.steps.ThematicQuoteSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Полное сквозное тестирование приложения \"Мобильный хоспис\"")
public class AllTests {

    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final MainSteps mainSteps = new MainSteps();
    private final NewsSteps newsSteps = new NewsSteps();
    private final NewsControlPanelSteps newsControlPanelSteps = new NewsControlPanelSteps();
    private final ThematicQuoteSteps thematicQuoteSteps = new ThematicQuoteSteps();
    private final AboutSteps aboutSteps = new AboutSteps();
    private View decorView;

    @Rule
    public IntentsTestRule<AppActivity> intentsRule =
            new IntentsTestRule<>(AppActivity.class);

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity ->
                decorView = activity.getWindow().getDecorView());
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
    @Story("TC-12")
    @Description("Переход на вкладку Главная")
    public void mainNavigation() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        newsSteps.clickButtonNews();
        onView(withText(TestConstants.CommonTexts.NEWS)).check(matches(isDisplayed()));
        pressBack();
        onView(withText(TestConstants.CommonTexts.NEWS)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-13")
    @Description("Свернуть/развернуть новости на Главной")
    public void expandCollapseNews() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getExpandNewsButtonId(), 5000));
        mainSteps.clickExpandNewsButton();
        mainSteps.clickExpandNewsButton();
        onView(withId(R.id.all_news_text_view))
                .check(matches(withText(TestConstants.NewsTexts.ALL_NEWS)));
    }

    @Test
    @Story("TC-14")
    @Description("Переход во Все новости через меню")
    public void toAllNewsViaMenu() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        newsSteps.clickButtonNews();
        onView(allOf(withText(TestConstants.NewsTexts.ALL_NEWS),
                withId(R.id.all_news_text_view)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-15")
    @Description("Переход во Все новости через вкладку Все новости на Главной")
    public void toAllNewsViaMain() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getAllNewsButtonId(), 5000));
        mainSteps.showButtonAllNews();
        mainSteps.clickButtonAllNews();
        onView(allOf(withText(TestConstants.NewsTexts.ALL_NEWS),
                withId(R.id.all_news_text_view)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-19")
    @Description("Создание новости категории Объявление")
    public void createNewsAdvertisement() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        newsSteps.clickButtonNews();
        newsControlPanelSteps.loadNewsControlPanelPage();
        newsControlPanelSteps.clickAddNewsButton();
        newsControlPanelSteps.fillTitleField(TestConstants.NewsData.VALID_NEWS_TITLE);
        newsControlPanelSteps.fillDescriptionField(TestConstants.NewsData.VALID_NEWS_DESCRIPTION);
        newsControlPanelSteps.selectCategory(TestConstants.NewsData.VALID_NEWS_CATEGORY);
        newsControlPanelSteps.fillDateField(TestConstants.NewsData.VALID_NEWS_DATE);
        newsControlPanelSteps.fillTimeField(TestConstants.NewsData.VALID_NEWS_TIME);
        newsControlPanelSteps.clickSaveButton();
        newsControlPanelSteps.scrollToAndClickNewsItem(0);
        onView(withId(R.id.news_item_description_text_view))
                .check(matches(withText(TestConstants.NewsTexts.DESCRIPTION_POOL_CONSTRUCTION)));
        newsControlPanelSteps.clickDeleteButtonOnNewsItem(0);
        newsControlPanelSteps.clickConfirmDeleteButton();
    }

    @Test
    @Story("TC-52")
    @Description("Развернуть/свернуть тематическую цитату")
    public void expandThematicQuote() {
        onView(isRoot()).perform(waitDisplayed(thematicQuoteSteps.getMissionImageButtonId(), 5000));
        thematicQuoteSteps.clickButtonThematicQuote();
        thematicQuoteSteps.checkTitleThematicQuote();
        thematicQuoteSteps.clickButtonToExpandThematicQuote();
        onView(allOf(withText("Love is all"), withId(R.id.our_mission_item_description_text_view)))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("TC-53")
    @Description("Просмотр Privacy Policy")
    public void watchingPrivacyPolicy() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        aboutSteps.clickButtonAboutMainMenu();
        aboutSteps.clickButtonPrivacyPolicy();
        intended(hasData("https://vhospice.org/#/privacy-policy/"));
        intended(hasAction(Intent.ACTION_VIEW));
    }

    @Test
    @Story("TC-54")
    @Description("Просмотр Terms of Use")
    public void watchingTermsOfUse() {
        onView(isRoot()).perform(waitDisplayed(mainSteps.getMainMenuButtonId(), 5000));
        mainSteps.clickMainMenuButton();
        aboutSteps.clickButtonAboutMainMenu();
        aboutSteps.clickButtonTermsOfUse();
        intended(hasData("https://vhospice.org/#/terms-of-use"));
        intended(hasAction(Intent.ACTION_VIEW));
    }
}
