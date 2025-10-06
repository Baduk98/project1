package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

/**
 * Класс для создания кастомных действий ViewAction
 */
public class MyViewAction {

    /**
     * Создает ViewAction для клика по дочернему элементу с определенным ID
     *
     * @param id ID дочернего элемента для клика
     * @return ViewAction для выполнения клика
     */
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // Ограничение: родительский view должен быть отображён
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with id: " + id;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View child = view.findViewById(id);
                if (child != null && child.isEnabled()) {
                    child.performClick();
                } else {
                    throw new RuntimeException("Child view with id " + id + " not found or not enabled");
                }
            }
        };
    }

    /**
     * ViewAction для получения текста из TextView
     *
     * @param stringHolder массив для хранения полученного текста (должен быть длиной >=1)
     * @return ViewAction для получения текста
     */
    public static ViewAction getText(final String[] stringHolder) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // Только TextView и наследники
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Get text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    stringHolder[0] = tv.getText().toString();
                } else {
                    throw new RuntimeException("The view is not a TextView");
                }
            }
        };
    }

    /**
     * ViewAction для длительного нажатия на элемент
     *
     * @return ViewAction для длительного нажатия
     */
    public static ViewAction longClick() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // Элемент должен быть включён и отображён
                return isEnabled();
            }

            @Override
            public String getDescription() {
                return "Long click on view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performLongClick();
            }
        };
    }
}
