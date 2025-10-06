package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.DataHelper.withIndex;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getLogin;
import static ru.iteco.fmhandroid.ui.steps.AuthorizationSteps.getPassword;

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
import ru.iteco.fmhandroid.ui.steps.AuthorizationSteps;
import ru.iteco.fmhandroid.ui.steps.MainSteps;
import ru.iteco.fmhandroid.ui.steps.ThematicQuoteSteps;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
@Epic("Тест-кейсы функционального тестирования вкладки \"Тематические цитаты\" мобильного приложения \"Мобильный хоспис\"")
public class ThematicQuoteTest {

    private final AuthorizationSteps authSteps = new AuthorizationSteps();
    private final ThematicQuoteSteps thematicSteps = new ThematicQuoteSteps();
    private final MainSteps mainSteps = new MainSteps();

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void setUp() {
        try {
            mainSteps.showTitleNewsOnMain();
        } catch (Exception e) {
            authSteps.fillLoginField(getLogin());
            authSteps.fillPasswordField(getPassword());
            authSteps.clickButtonSignIn();
            mainSteps.showTitleNewsOnMain();
        }
    }

    @Test
    @Story("TC-52")
    @Description("Развернуть и свернуть тематическую цитату на вкладке \"Love is all\" (Позитивный)")
    public void expandCollapseThematicQuote() {
        onView(isRoot())
                .perform(waitDisplayed(thematicSteps.getMissionImageButtonId(), 5000));
        thematicSteps.clickButtonThematicQuote();
        thematicSteps.checkTitleThematicQuote();
        thematicSteps.clickButtonToExpandThematicQuote();
        onView(withIndex(
                withId(R.id.our_mission_item_description_text_view), 0))
                .check(matches(isDisplayed()));
    }
}
