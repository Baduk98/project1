package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;

public class DataHelper {

    /**
     * Возвращает матчер, который выбирает вью по порядковому индексу среди всех подходящих.
     */
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ")
                        .appendValue(index)
                        .appendText(" ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (matcher.matches(view)) {
                    if (currentIndex == index) {
                        return true;
                    }
                    currentIndex++;
                }
                return false;
            }
        };
    }

    /**
     * Ждёт появления и отображения вью с указанным ID в течение millis миллисекунд.
     */
    public static ViewAction waitDisplayed(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait up to " + millis + " ms for view with id <" + viewId + "> to be displayed.";
            }

            @Override
            public void perform(UiController uiController, View rootView) {
                uiController.loopMainThreadUntilIdle();
                long startTime = System.currentTimeMillis();
                long endTime = startTime + millis;

                Matcher<View> idMatcher = withId(viewId);
                Matcher<View> displayMatcher = isDisplayed();

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(rootView)) {
                        if (idMatcher.matches(child) && displayMatcher.matches(child)) {
                            return; // найден и отображён — выходим
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                // Если таймаут, выбрасываем исключение с понятным описанием
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(rootView))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
