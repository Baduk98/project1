package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.elements.ThematicQuotePage;

/**
 * Steps для вкладки "Тематические цитаты"
 */
public class ThematicQuoteSteps {

    private final ThematicQuotePage page = new ThematicQuotePage();

    public void clickButtonThematicQuote() {
        Allure.step("Нажать на кнопку Тематические цитаты на главной странице");
        page.missionButton.perform(click());
    }

    public void checkTitleThematicQuote() {
        Allure.step("Проверить заголовок 'Love is all' на вкладке Тематические цитаты");
        page.missionTitle.check(matches(allOf(withText("Love is all"), isDisplayed())));
    }

    public void clickButtonToExpandThematicQuote() {
        Allure.step("Нажать на кнопку развернуть тематическую цитату");
        page.missionItemClick();
    }

    public int getMissionImageButtonId() {
        return page.missionImageButtonId;
    }
}
