package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class BookShelvesActivityTest {

    @Rule
    public ActivityTestRule<MyBooksShelvesActivity> menuActivityTestRule =
            new ActivityTestRule<>(MyBooksShelvesActivity.class, true, true);

    @Test
    /* Other error occurred in Login */
    public void Test_CheckReadShelfCount() {
        assertEquals("Read  15", MyBooksShelvesActivity.sForTest_Read);
    }

    @Test
    /* Other error occurred in Login */
    public void Test_CheckCurrentlyReadingShelfCount() {
        assertEquals("Currently Reading  49", MyBooksShelvesActivity.sForTest_CurrentlyReading);
    }

    @Test
    /* Other error occurred in Login */
    public void Test_CheckWantTOReadShelfCount() {
        assertEquals("Want to Read  30", MyBooksShelvesActivity.sForTest_WantToRead);
    }

    @Test
    public void Test_ReadButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ReadBooksActivity.class.getName(), null, false);

        onView(withId(R.id.ReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void Test_ReadingButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CurrentlyReadingActivity.class.getName(), null, false);

        onView(withId(R.id.CurrentlyReadingBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void Test_WantButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(WantToReadActivity.class.getName(), null, false);

        onView(withId(R.id.WantToReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}
