package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class SignOutActivityTest {

    UserSessionManager m = new UserSessionManager("xYzAbCdToKeN", true);

    @Rule
    public ActivityTestRule<SignOutActivity> activityRule
            = new ActivityTestRule<>(
            SignOutActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent

    @Test
    public void onCreate() {
        UserSessionManager.stubUserDataForTesting("", "", "xYzAbCdToKeN", "");
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        long Time=0;
        while (SignOutActivity.sForTest == null && Time < 1000000000){
            Time++;
            continue;
        };
        if (!APIs.MimicModeEnabled) return;
        assertEquals("Signed out Successfully",SignOutActivity.sForTest);
        activityRule.finishActivity();
    }
}