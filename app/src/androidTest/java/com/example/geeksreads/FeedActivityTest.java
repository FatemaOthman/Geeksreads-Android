package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

public class FeedActivityTest {
    public ActivityTestRule<FeedActivity> menuActivityTestRule =
            new ActivityTestRule<>(FeedActivity.class, true, false);


    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN", "anyid", true);

    @Test
    public void TestView() {

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            menuActivityTestRule.launchActivity(mIntent);
            onView(withId(R.id.FeedRecyclerView));
            assertEquals("2",FeedActivity.sForTestFeeditemsCount);

           // onData(withId(R.id.FeedRecyclerView)).check("2".equals(FeedActivity.sForTestFeeditemsCount));

        }

    }

    @Test
    public void OnClickItem()
    {
        if (APIs.MimicModeEnabled) {
            if(getRVcount()>0)
            {
                onView(withId((R.id.FeedRecyclerView))).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            }
        }

    }
    private int getRVcount()
    {
        onData(withId(R.id.FeedRecyclerView));
        RecyclerView recyclerView = (RecyclerView)menuActivityTestRule.getActivity().findViewById(R.id.FeedRecyclerView);
        return recyclerView.getAdapter().getItemCount();
    }

}