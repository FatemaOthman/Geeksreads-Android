//package com.example.geeksreads;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.Intent;
//import android.support.test.espresso.UiController;
//import android.support.test.espresso.ViewAction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import org.hamcrest.Matcher;
//import org.junit.Test;
//
//import CustomFunctions.APIs;
//import CustomFunctions.UserSessionManager;
//
//import static android.support.test.InstrumentationRegistry.getInstrumentation;
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static junit.framework.Assert.assertNotNull;
//import static org.hamcrest.CoreMatchers.anything;
//import static org.junit.Assert.*;
//
//public class FeedActivityTest {
//    public ActivityTestRule<FeedActivity> menuActivityTestRule =
//            new ActivityTestRule<>(FeedActivity.class, true, false);
//    public static class MyViewAction {
//
//        public  static ViewAction clickChildViewWithId(final int id) {
//            return new ViewAction() {
//                @Override
//                public Matcher<View> getConstraints() {
//                    return null;
//                }
//
//                @Override
//                public String getDescription() {
//                    return "Click on a child view with specified id.";
//                }
//
//                @Override
//                public void perform(UiController uiController, View view) {
//                    View v = view.findViewById(id);
//                    v.performClick();
//                }
//            };
//        }
//
//    }
//
//
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
//            onView(withId(R.id.FeedRecyclerView));
//            assertEquals("2",FeedActivity.sForTestFeeditemsCount);
//
//           // onData(withId(R.id.FeedRecyclerView)).check("2".equals(FeedActivity.sForTestFeeditemsCount));
//
//        }
//
//    }
//
//    @Test
//    public void OnClickItemBookCover()
//    {
//        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(BookActivity.class.getName(), null, false);
//
//        onView(withId(R.id.FeedRecyclerView)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.bookCover)));
//        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
//        // next activity is opened and captured.
//        assertNotNull(nextActivity);
//        nextActivity.finish();
//
//
//    }
//
//}