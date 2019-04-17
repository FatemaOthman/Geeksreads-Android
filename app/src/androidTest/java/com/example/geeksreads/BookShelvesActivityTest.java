package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

public class BookShelvesActivityTest {

    @Rule
    public ActivityTestRule<MyBooksShelvesActivity> menuActivityTestRule =
            new ActivityTestRule<>(MyBooksShelvesActivity.class, true, true);

    @Test
    public void TestReadButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ReadBooksActivity.class.getName(), null, false);

        onView(withId(R.id.ReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void TestReadingButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CurrentlyReadingActivity.class.getName(), null, false);

        onView(withId(R.id.CurrentlyReadingBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Test
    public void TestWantButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(WantToReadActivity.class.getName(), null, false);

        onView(withId(R.id.WantToReadBtn)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}
