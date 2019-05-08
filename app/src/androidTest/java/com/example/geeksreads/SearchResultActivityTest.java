package com.example.geeksreads;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static org.junit.Assert.*;

public class SearchResultActivityTest {
    public ActivityTestRule<SearchResultActivity> menuActivityTestRule =
            new ActivityTestRule<>(SearchResultActivity.class, true, false);


    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN", "anyid", true);

    @Test
    public void TestView() {

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            menuActivityTestRule.launchActivity(mIntent);
            assertEquals(2, SearchHandlerActivity.sForTestNumOfResults);

        }

    }


}

