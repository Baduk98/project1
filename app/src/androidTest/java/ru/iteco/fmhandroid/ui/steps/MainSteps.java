// app/src/androidTest/java/ru/iteco/fmhandroid/ui/steps/MainSteps.java
package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.iteco.fmhandroid.ui.data.DataHelper.waitDisplayed;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.elements.MainPage;

/**
 * Steps для взаимодействия с главной страницей
 */
public class MainSteps {
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final MainPage mainPage = new MainPage();

    public void clickAllNews() {
        Allure.step("Нажать на кнопку 'Все новости'");
        mainPage.allNewsButtonInteraction.perform(click());
    }

    public void clickMainMenuButton() {
        Allure.step("Нажать на кнопку главного меню");
        mainPage.mainMenuButtonInteraction.perform(click());
    }

    public void showTitleNewsOnMain() {
        Allure.step("Отобразился заголовок новостей на главной странице");
        onView(isRoot())
                .perform(waitDisplayed(mainPage.newsTitleId, 10000));
        // mainPage.newsTitleInteraction.perform(waitDisplayed());
        mainPage.newsTitleInteraction.check(matches(isDisplayed()));

    }

    public void clickExpandNewsButton() {
        Allure.step("Нажать на кнопку разворачивания новостей");
        mainPage.expandNewsButtonInteraction.perform(click());
    }

    public void waitForContainerListNews(int timeout) {
        Allure.step("Ожидание контейнера списка новостей");
        onView(isRoot())
                .perform(waitDisplayed(mainPage.getContainerListNewsId(), timeout));
    }

    public void waitForAllNewsButton(int timeout) {
        Allure.step("Ожидание кнопки 'Все новости'");
        onView(isRoot())
                .perform(waitDisplayed(mainPage.getAllNewsButtonId(), timeout));
    }

    public void showButtonAllNews() {
        Allure.step("Проверить отображение кнопки 'Все новости'");
        mainPage.allNewsButtonInteraction.check(matches(isDisplayed()));
    }

    public void clickButtonAllNews() {
        Allure.step("Нажать на кнопку 'Все новости' (alias)");
        clickAllNews();
    }

    public void clickButtonMain() {
        Allure.step("Возврат на главную страницу через Back");
        Espresso.pressBack();
    }

    public int getAllNewsButtonId() {
        return mainPage.getAllNewsButtonId();
    }

    public int getMainMenuButtonId() {
        return mainPage.getMainMenuButtonId();
    }

    public int getExpandNewsButtonId() {
        return mainPage.getExpandNewsButtonId();
    }
}
