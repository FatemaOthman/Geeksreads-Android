package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

public class AuthorActivityTest {
    @Rule
    public ActivityTestRule<AuthorActivity> menuActivityTestRule =
            new ActivityTestRule<>(AuthorActivity.class, true, true);


    @Test
    public void TestViews()
    {

        /* Testing getting Name of The Author  right */
        onView(withId(R.id.AuthorName)).check(ViewAssertions.matches(withText("Jane Austen")));

        /* Testing getting Number of Books right */
        onView(withId(R.id.NumberOfBooks)).check(ViewAssertions.matches(withText("Author of 274 books.")));

        /* Testing getting Rating of the Book right */
        onView(withId(R.id.AuthorRating)).check(ViewAssertions.matches(withText("4.6")));
        /* Testing getting Number of Reviews right */
        onView(withId(R.id.NumsOfReviewsAuthor)).check(ViewAssertions.matches(withText("72,562 reviews.")));
        /* Testing getting Number of Ratings right */
        onView(withId(R.id.NumsOfRatingAuthor)).check(ViewAssertions.matches(withText("120,000 ratings.")));

        /* Testing getting PhotoURL of the Author right */
        assertEquals("http://geeksreads.000webhostapp.com/Fatema/janeausten.jpg",AuthorActivity.sForTestAuthorPicURL);




    }


    @Test
    public void authorFollowUnFollow() {
        assertEquals("Following",AuthorActivity.authorFollowUnFollow(false));
        assertEquals("Follow",AuthorActivity.authorFollowUnFollow(true));
    }


}