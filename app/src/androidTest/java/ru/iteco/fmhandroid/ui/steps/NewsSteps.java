package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.DataHelper.withIndex;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestConstants;
import ru.iteco.fmhandroid.ui.elements.NewsPage;

/**
 * Steps для взаимодействия со списком новостей
 */
public class NewsSteps {

    private NewsPage newsPage = new NewsPage();

    /**
     * Нажать на пункт "Новости" в главном меню
     */
    public void clickButtonNews() {
        Allure.step("Нажать на пункт 'News' в главном меню");
        onView(withText(TestConstants.CommonTexts.NEWS)).perform(click());
    }

    /**
     * Проверить, что заголовок "Новости" отображается на экране
     */
    public void checkNewsTitle() {
        Allure.step("Проверить отображение заголовка 'News'");
        onView(isRoot())
                .perform(waitDisplayed(newsPage.newsTitleId, 5000))
                .check(matches(isDisplayed()));
        // onView(withId(newsPage.newsTitleId)).check(matches(isDisplayed()));
    }

    /**
     * Нажать на кнопку "ALL NEWS"
     */
    public void clickAllNewsButton() {
        Allure.step("Нажать на кнопку 'ALL NEWS'");
        onView(withId(R.id.all_news_text_view)).perform(click());
    }

    /**
     * Ожидание загрузки списка новостей
     * @param timeout миллисекунды ожидания
     */
    public void waitForNewsList(int timeout) {
        Allure.step("Ожидание загрузки списка новостей");
        onView(withId(R.id.container_list_news_include_on_fragment_main))
                .perform(waitDisplayed(R.id.container_list_news_include_on_fragment_main, timeout));
    }

    /**
     * Нажать на первую кнопку "Развернуть" в списке новостей
     */
    public void clickExpandFirstNews() {
        Allure.step("Нажать на кнопку 'Развернуть' первой новости");
        onView(withId(R.id.expand_material_button)).perform(click());
    }

    /**
     * Проверить описание первой новости
     * @param expectedText ожидаемый текст описания
     */
    public void checkFirstNewsDescription(String expectedText) {
        Allure.step("Проверить описание первой новости");
        onView(withIndex(withId(R.id.news_item_description_text_view), 0))
                .check(matches(withText(expectedText)))
                .check(matches(isDisplayed()));
    }
}
