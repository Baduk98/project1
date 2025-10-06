package ru.iteco.fmhandroid.ui.elements;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestConstants;

/**
 * Page Object для панели управления новостями
 */
public class NewsControlPanelPage {

    // ViewInteraction элементы
    public final ViewInteraction newsControlPanelTitle;
    public final ViewInteraction addNewsButton;
    public final ViewInteraction editButton;
    public final ViewInteraction deleteButton;
    public final ViewInteraction sortButton;
    public final ViewInteraction filterButton;
    public final ViewInteraction newsItemTitle;
    public final ViewInteraction newsItemDescription;
    public final ViewInteraction newsItemDate;
    public final ViewInteraction newsItemCategory;
    public final ViewInteraction saveButton;
    public final ViewInteraction cancelButton;
    public final ViewInteraction titleField;
    public final ViewInteraction descriptionField;
    public final ViewInteraction categoryDropdown;
    public final ViewInteraction dateField;
    public final ViewInteraction timeField;
    public final ViewInteraction switcherActive;
    public final ViewInteraction confirmDeleteButton;
    public final ViewInteraction newsRecyclerView;
    public final ViewInteraction emptyNewsListText;

    // ID элементы
    public final int newsControlPanelTitleId;
    public final int addNewsButtonId;
    public final int editButtonId;
    public final int deleteButtonId;
    public final int sortButtonId;
    public final int filterButtonId;
    public final int newsItemTitleId;
    public final int newsItemDescriptionId;
    public final int newsItemDateId;
    public final int newsItemCategoryId;
    public final int saveButtonId;
    public final int cancelButtonId;
    public final int titleFieldId;
    public final int descriptionFieldId;
    public final int categoryDropdownId;
    public final int dateFieldId;
    public final int timeFieldId;
    public final int switcherActiveId;
    public final int confirmDeleteButtonId;
    public final int newsRecyclerViewId;
    public final int emptyNewsListTextId;

    // Тестовые данные
    private static final String validNewsTitle = TestConstants.NewsData.VALID_NEWS_TITLE;
    private static final String validNewsDescription = TestConstants.NewsData.VALID_NEWS_DESCRIPTION;
    private static final String validNewsCategory = TestConstants.NewsData.VALID_NEWS_CATEGORY;
    private static final String validNewsDate = TestConstants.NewsData.VALID_NEWS_DATE;
    private static final String validNewsTime = TestConstants.NewsData.VALID_NEWS_TIME;
    private static final String emptyTitle = TestConstants.NewsData.EMPTY_TITLE;
    private static final String emptyDescription = TestConstants.NewsData.EMPTY_DESCRIPTION;
    private static final String longTitle = TestConstants.NewsData.LONG_TITLE;
    private static final String longDescription = TestConstants.NewsData.LONG_DESCRIPTION;
    private static final String specialCharactersTitle = TestConstants.NewsData.SPECIAL_CHARACTERS_TITLE;
    private static final String specialCharactersDescription = TestConstants.NewsData.SPECIAL_CHARACTERS_DESCRIPTION;
    private static final String futureDate = TestConstants.TestDates.FUTURE_DATE;
    private static final String pastDate = TestConstants.NewsData.PAST_DATE;
    private static final String invalidDate = TestConstants.NewsData.INVALID_DATE;
    private static final String invalidTime = TestConstants.NewsData.INVALID_TIME;

    public NewsControlPanelPage() {
        newsControlPanelTitle = onView(allOf(
                withText(TestConstants.CommonTexts.NEWS),
                withParent(withParent(withId(R.id.nav_host_fragment)))
        ));
        addNewsButton = onView(allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button")
        ));
        editButton = onView(allOf(
                withId(R.id.edit_news_item_image_view),
                withContentDescription("Edit news")
        ));
        deleteButton = onView(allOf(
                withId(R.id.delete_news_item_image_view),
                withContentDescription("Delete news")
        ));
        sortButton = onView(allOf(
                withId(R.id.sort_news_material_button),
                withContentDescription("Sort news button")
        ));
        filterButton = onView(allOf(
                withId(R.id.filter_news_material_button),
                withContentDescription("Filter news")
        ));
        newsItemTitle = onView(withId(R.id.news_item_title_text_view));
        newsItemDescription = onView(withId(R.id.news_item_description_text_view));
        newsItemDate = onView(withId(R.id.news_item_date_text_view));
        newsItemCategory = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
        saveButton = onView(allOf(
                withId(R.id.save_button),
                withText(TestConstants.CommonTexts.SAVE)
        ));
        cancelButton = onView(withId(R.id.cancel_button));
        titleField = onView(withId(R.id.news_item_title_text_input_edit_text));
        descriptionField = onView(withId(R.id.news_item_description_text_input_edit_text));
        categoryDropdown = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
        dateField = onView(withId(R.id.news_item_publish_date_text_input_edit_text));
        timeField = onView(withId(R.id.news_item_publish_time_text_input_edit_text));
        switcherActive = onView(withId(R.id.switcher));
        confirmDeleteButton = onView(allOf(
                withId(android.R.id.button1),
                withText(TestConstants.CommonTexts.OK)
        ));
        newsRecyclerView = onView(withId(R.id.news_list_recycler_view));
        emptyNewsListText = onView(withId(R.id.empty_news_list_text_view));

        // ID элементов
        newsControlPanelTitleId = R.id.container_list_news_include_on_fragment_main;
        addNewsButtonId = R.id.add_news_image_view;
        editButtonId = R.id.edit_news_item_image_view;
        deleteButtonId = R.id.delete_news_item_image_view;
        sortButtonId = R.id.sort_news_material_button;
        filterButtonId = R.id.filter_news_material_button;
        newsItemTitleId = R.id.news_item_title_text_view;
        newsItemDescriptionId = R.id.news_item_description_text_view;
        newsItemDateId = R.id.news_item_date_text_view;
        newsItemCategoryId = R.id.news_item_category_text_auto_complete_text_view;
        saveButtonId = R.id.save_button;
        cancelButtonId = R.id.cancel_button;
        titleFieldId = R.id.news_item_title_text_input_edit_text;
        descriptionFieldId = R.id.news_item_description_text_input_edit_text;
        categoryDropdownId = R.id.news_item_category_text_auto_complete_text_view;
        dateFieldId = R.id.news_item_publish_date_text_input_edit_text;
        timeFieldId = R.id.news_item_publish_time_text_input_edit_text;
        switcherActiveId = R.id.switcher;
        confirmDeleteButtonId = android.R.id.button1;
        newsRecyclerViewId = R.id.news_list_recycler_view;
        emptyNewsListTextId = R.id.empty_news_list_text_view;
    }

    // Методы доступа к ID
    public int getAddNewsButtonId() { return addNewsButtonId; }
    public int getEditButtonId() { return editButtonId; }
    public int getDeleteButtonId() { return deleteButtonId; }
    public int getSaveButtonId() { return saveButtonId; }
    public int getNewsRecyclerViewId() { return newsRecyclerViewId; }
    public int getNewsItemTitleId() { return newsItemTitleId; }
    public int getNewsItemDescriptionId() { return newsItemDescriptionId; }
    public int getNewsItemDateId() { return newsItemDateId; }

    // Статические геттеры тестовых данных
    public static String getValidNewsTitle() { return validNewsTitle; }
    public static String getValidNewsDescription() { return validNewsDescription; }
    public static String getValidNewsCategory() { return validNewsCategory; }
    public static String getValidNewsDate() { return validNewsDate; }
    public static String getValidNewsTime() { return validNewsTime; }
    public static String getEmptyTitle() { return emptyTitle; }
    public static String getEmptyDescription() { return emptyDescription; }
    public static String getLongTitle() { return longTitle; }
    public static String getLongDescription() { return longDescription; }
    public static String getSpecialCharactersTitle() { return specialCharactersTitle; }
    public static String getSpecialCharactersDescription() { return specialCharactersDescription; }
    public static String getFutureDate() { return futureDate; }
    public static String getPastDate() { return pastDate; }
    public static String getInvalidDate() { return invalidDate; }
    public static String getInvalidTime() { return invalidTime; }
}
