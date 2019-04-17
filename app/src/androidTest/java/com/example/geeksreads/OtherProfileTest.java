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

public class OtherProfileTest {
    @Rule
    public ActivityTestRule<Profile> menuActivityTestRule =
            new ActivityTestRule<>(Profile.class, true, true);


    @Test
    public void Test_Other_Profile() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Profile.class.getName(), null, false);

        onView(withId(R.id.ActualFollowersCount)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        Instrumentation.ActivityMonitor activityMonitor2 = getInstrumentation().addMonitor(FollowActivity.class.getName(), null, false);

        onView(withId(R.id.FollowersList)).perform(click());

        Activity nextActivity2 = getInstrumentation().waitForMonitorWithTimeout(activityMonitor2, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);

        nextActivity.finish();

    }

}
