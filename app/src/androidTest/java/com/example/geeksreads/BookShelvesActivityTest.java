package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

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
    /* Checking retrieved number in  */
    public void Test_CheckReadShelfCount() {
        if (APIs.MimicModeEnabled)
        {
            UserSessionManager.stubUserDataForTesting("mahmoud_morsy@live.com","Non", "xYzAbCdToKeN", "iiiidddd1142019");
            onView(withId(R.id.ReadBtn)).perform();
            assertEquals("5", MyBooksShelvesActivity.sForTest_Read);
        }
        else
        {
            assertEquals(true, MyBooksShelvesActivity.sForTest_Read != null);
        }
    }

    @Test
    /* Other error occurred in Login */
    public void Test_CheckCurrentlyReadingShelfCount() {
        if (APIs.MimicModeEnabled)
        {
            UserSessionManager.stubUserDataForTesting("mahmoud_morsy@live.com","Non", "xYzAbCdToKeN", "iiiidddd1142019");
            //assertEquals("Currently Reading  15", MyBooksShelvesActivity.sForTest_Reading);
        }
        else
        {
            assertEquals(true, MyBooksShelvesActivity.sForTest_Reading != null);
        }
    }

    @Test
    /* Other error occurred in Login */
    public void Test_CheckWantTOReadShelfCount() {
        if (APIs.MimicModeEnabled)
        {
            UserSessionManager.stubUserDataForTesting("mahmoud_morsy@live.com","Non", "xYzAbCdToKeN", "iiiidddd1142019");
            onView(withId(R.id.WantToReadBtn)).perform();
            assertEquals("10", MyBooksShelvesActivity.sForTest_WantToRead);
        }
        else
        {
            assertEquals(true, MyBooksShelvesActivity.sForTest_WantToRead != null);
        }
    }

    @Test
    public void Test_zReadButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ReadBooksActivity.class.getName(), null, false);

        onView(withId(R.id.ReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void Test_zReadingButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CurrentlyReadingActivity.class.getName(), null, false);

        onView(withId(R.id.CurrentlyReadingBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void Test_zWantButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(WantToReadActivity.class.getName(), null, false);

        onView(withId(R.id.WantToReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}
