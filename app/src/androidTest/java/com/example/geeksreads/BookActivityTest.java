package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class BookActivityTest {
    @Rule
    public ActivityTestRule<BookActivity> menuActivityTestRule =
            new ActivityTestRule<>(BookActivity.class, true, false);


    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN","anyid",true);

    @Test
    public void TestView() {

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            mIntent.putExtra("BookID", "111");

            menuActivityTestRule.launchActivity(mIntent);
            /* Testing getting Author of the Book right */
            onView(withId(R.id.AuthorNameTxt)).check(ViewAssertions.matches(withText("By: Jane Austen")));

            /* Testing getting Title of the Book right */
            onView(withId(R.id.BookTitleTxt)).check(ViewAssertions.matches(withText("Pride and Prejudice")));

            /* Testing getting Rating of the Book right */
            onView(withId(R.id.RatingBar)).check(ViewAssertions.matches(withText("4.25")));
            onView(withId(R.id.ReviewsNumberTxt)).check(ViewAssertions.matches(withText("56204 Reviews")));
            onView(withId(R.id.RatingsNumberTxt)).check(ViewAssertions.matches(withText("2533019 Ratings")));


            /* Testing getting Date of the Book right */
            onView(withId(R.id.PublishedOnTxt)).check(ViewAssertions.matches(withText("Published On  28-1-1813, By: Modern Library")));

            /* Testing getting ISBN of the Book right */
            onView(withId(R.id.ISBN)).check(ViewAssertions.matches(withText("ISBN: 9780679783268")));

            /* Testing getting pages Number of the Book right */
            onView(withId(R.id.pages)).check(ViewAssertions.matches(withText("279 pages")));

            /* Testing The finishing of all Async Tasks */
            assertEquals("Done", BookActivity.sForTestBookActivity);
        }
    }

    @Test
    public void TestButton(){

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            mIntent.putExtra("BookID", "111");

            menuActivityTestRule.launchActivity(mIntent);

            Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ChooseShelfActivity.class.getName(), null, false);

            onView(withId(R.id.OptionsDropList)).perform(click());

            Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
            // next activity is opened and captured.
            assertNotNull(nextActivity);
            nextActivity.finish();
        }
    }

    private static ViewAction setRating(final float rating) {
        if (rating % 0.5 != 0) {
            throw new IllegalArgumentException("Rating must be multiple of 0.5f");
        }

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar in 0.5f increments";
            }

            @Override
            public void perform(UiController uiController, View view) {
                GeneralClickAction viewAction = new GeneralClickAction(
                        Tap.SINGLE,
                        new CoordinatesProvider() {
                            @Override
                            public float[] calculateCoordinates(View view) {
                                int[] locationOnScreen = new int[2];
                                view.getLocationOnScreen(locationOnScreen);
                                int screenX = locationOnScreen[0];
                                int screenY = locationOnScreen[1];
                                int numStars = ((RatingBar) view).getNumStars();
                                float widthPerStar = 1f * view.getWidth() / numStars;
                                float percent = rating / numStars;
                                float x = screenX + view.getWidth() * percent;
                                float y = screenY + view.getHeight() * 0.5f;
                                return new float[]{x - widthPerStar * 0.5f, y};
                            }
                        },
                        Press.FINGER,
                        InputDevice.SOURCE_UNKNOWN,
                        MotionEvent.BUTTON_PRIMARY
                );
                viewAction.perform(uiController, view);
            }
        };
    }

    @Test
    public void TestAddReviewSuccess(){

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            mIntent.putExtra("BookID", "111");

            menuActivityTestRule.launchActivity(mIntent);

            onView(withId(R.id.ratingBook)).perform(scrollTo(), setRating(4));

            onView(withId(R.id.Review)).perform(scrollTo(), typeText("Add Review to Test"), closeSoftKeyboard());

            onView(withId(R.id.AddReview)).perform(scrollTo(), click());

            assertEquals("Review Added", BookActivity.sForTestAddingReview);
        }
    }

    @Test
    public void TestAddReviewNoBody(){

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            mIntent.putExtra("BookID", "111");

            menuActivityTestRule.launchActivity(mIntent);

            onView(withId(R.id.ratingBook)).perform(scrollTo(), setRating(4));

            onView(withId(R.id.AddReview)).perform(scrollTo(), click());

            assertEquals("Your review must be more than 6 char", BookActivity.sForTestAddingReview);
        }
    }

    @Test
    public void TestAddReviewNoRate(){

        if (APIs.MimicModeEnabled) {
            Intent mIntent = new Intent();
            mIntent.putExtra("BookID", "111");

            menuActivityTestRule.launchActivity(mIntent);

            onView(withId(R.id.Review)).perform(scrollTo(), replaceText("Add Review to Test"), closeSoftKeyboard());

            onView(withId(R.id.AddReview)).perform(scrollTo(), click());

            assertEquals("You have to rate before Adding a review.", BookActivity.sForTestAddingReview);
        }
     }


}