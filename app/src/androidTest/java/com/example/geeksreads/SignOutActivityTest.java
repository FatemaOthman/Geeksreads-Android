package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import CustomFunctions.UserSessionManager;

import static org.junit.Assert.*;

public class SignOutActivityTest {

    UserSessionManager m = new UserSessionManager("xYzAbCdToKeN", true);

    @Rule
    public ActivityTestRule<SignOutActivity> menuActivityTestRule =
            new ActivityTestRule<>(SignOutActivity.class, true, true);

    @Test
    public void onCreate() {
        long Time=0;
        while (SignOutActivity.sForTest == null && Time < 1000000000){
            Time++;
            continue;
        };
        assertEquals("Signed out Successfully",SignOutActivity.sForTest);
    }

}