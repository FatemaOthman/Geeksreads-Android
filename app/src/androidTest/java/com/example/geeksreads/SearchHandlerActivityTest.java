//package com.example.geeksreads;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.Intent;
//import android.support.test.rule.ActivityTestRule;
//import android.view.KeyEvent;
//import android.widget.SearchView;
//
//import org.junit.Test;
//
//import CustomFunctions.APIs;
//import CustomFunctions.UserSessionManager;
//
//import static android.support.test.InstrumentationRegistry.getInstrumentation;
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static junit.framework.Assert.assertNotNull;
//import static org.hamcrest.CoreMatchers.anything;
//import static org.junit.Assert.*;
//
//public class SearchHandlerActivityTest {
//    public ActivityTestRule<SearchHandlerActivity> menuActivityTestRule =
//            new ActivityTestRule<>(SearchHandlerActivity.class, true, false);
//
//
//    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN", "anyid", true);
//
//    @Test
//    public void TestView() {
//
//        if (APIs.MimicModeEnabled) {
//            Intent mIntent = new Intent();
//            menuActivityTestRule.launchActivity(mIntent);
//            assertEquals(2, SearchHandlerActivity.sForTestNumOfResults);
//
//        }
//
//    }
//
//
//}