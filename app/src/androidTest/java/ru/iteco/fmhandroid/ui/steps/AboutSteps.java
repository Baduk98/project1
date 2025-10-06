package ru.iteco.fmhandroid.ui.steps;

import static androidx.test.espresso.action.ViewActions.click;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.ui.elements.AboutPage;

/**
 * Steps для взаимодействия со страницей "О приложении"
 */
public class AboutSteps {

    private final AboutPage aboutPage = new AboutPage();

    public void clickButtonAboutMainMenu() {
        Allure.step("Нажать на кнопку About в главном меню");
        aboutPage.aboutButton.perform(click());
    }

    public void clickButtonPrivacyPolicy() {
        Allure.step("Нажать на ссылку Privacy Policy");
        aboutPage.privacyPolicyText.perform(click());
    }

    public void clickButtonTermsOfUse() {
        Allure.step("Нажать на ссылку Terms of Use");
        aboutPage.termsOfUseText.perform(click());
    }

    public void clickButtonNavigateUp() {
        Allure.step("Нажать кнопку Navigate up для возврата назад");
        aboutPage.navigateUpButton.perform(click());
    }
}
