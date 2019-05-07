package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.anything;

public class ReadBooksActivityTest {

    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN","anyid",true);

    @Rule
    public ActivityTestRule<ReadBooksActivity> menuActivityTestRule =
            new ActivityTestRule<>(ReadBooksActivity.class, true, true);


    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }

    @Test
    public void TestRefreshLayout()
    {
        if (APIs.MimicModeEnabled) {
            onView(withId(R.id.ReadSwipeLayout))
                    .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
        }
    }

    @Test
    public void OnClickItem()
    {
        if (APIs.MimicModeEnabled) {
            Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(BookActivity.class.getName(), null, false);

            onData(anything()).inAdapterView(withId(R.id.ReadBookList)).atPosition(0).perform(click());

            Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
            // next activity is opened and captured.
            assertNotNull(nextActivity);
            nextActivity.finish();
        }

    }

}