package ru.iteco.fmhandroid.ui.elements;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;

/**
 * Страница "О приложении"
 */
public class AboutPage {

    public ViewInteraction aboutButton;
    public ViewInteraction privacyPolicyText;
    public ViewInteraction termsOfUseText;
    public ViewInteraction navigateUpButton;

    public AboutPage() {
        aboutButton = onView(allOf(
                withId(android.R.id.title),
                withText("About")
        ));
        privacyPolicyText = onView(withId(R.id.about_privacy_policy_value_text_view));
        termsOfUseText = onView(withId(R.id.about_terms_of_use_value_text_view));
        navigateUpButton = onView(withContentDescription("Navigate up"));
    }
}
