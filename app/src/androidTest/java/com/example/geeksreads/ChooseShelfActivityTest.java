package com.example.geeksreads;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class ChooseShelfActivityTest {

    @Rule
    public ActivityTestRule<ChooseShelfActivity> menuActivityTestRule =
            new ActivityTestRule<>(ChooseShelfActivity.class, true, false);

    UserSessionManager userSessionManager = new UserSessionManager("xYzAbCdToKeN","anyid",true);

    @Test
    public void TestChooseRead(){
        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioRead)).perform(click());

            onView(withId(R.id.AddShelfBtn)).perform(click());

            assertEquals("Read", ChooseShelfActivity.sForTestRadioChooseShelf);
        }
    }

    @Test
    public void TestChooseReading(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioReading)).perform(click());

            onView(withId(R.id.AddShelfBtn)).perform(click());

            assertEquals("Reading", ChooseShelfActivity.sForTestRadioChooseShelf);
        }
    }

    @Test
    public void TestChooseWantToRead(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioWantRead)).perform(click());

            onView(withId(R.id.AddShelfBtn)).perform(click());

            assertEquals("WantToRead", ChooseShelfActivity.sForTestRadioChooseShelf);
        }
    }

    @Test
    public void TestNoShelfChosen(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.AddShelfBtn)).perform(click());

            assertEquals("You need to choose Shelf", ChooseShelfActivity.sForTestChooseShelf);
        }
    }

    @Test
    public void TestAddToShelf(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioWantRead)).perform(click());

            onView(withId(R.id.AddShelfBtn)).perform(click());

            assertEquals("Added Successfully to shelf", ChooseShelfActivity.sForTestChooseShelf);
        }
    }

    @Test
    public void TestMoveToReading(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "Want To Read");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioRead)).check(matches(not(isClickable())));
            onView(withId(R.id.radioWantRead)).check(matches((isChecked())));
        }
    }

    @Test
    public void TestMoveToRead(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "Currently Reading");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioWantRead)).check(matches(not(isClickable())));
            onView(withId(R.id.radioReading)).check(matches((isChecked())));
        }
    }

    @Test
    public void TestIfRead(){

        if (APIs.MimicModeEnabled) {
            Intent myIntent = new Intent();
            myIntent.putExtra("Author", "Jane Austen");
            myIntent.putExtra("Title", "Pride and Prejudice");
            myIntent.putExtra("Rating", "4.25");
            myIntent.putExtra("RatingNumber", "128768");
            myIntent.putExtra("Pages", "240");
            myIntent.putExtra("published", "1-2-1999");
            myIntent.putExtra("bookID", "111");
            myIntent.putExtra("cover", "http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg");
            myIntent.putExtra("BookStatus", "Read");

            menuActivityTestRule.launchActivity(myIntent);

            onView(withId(R.id.radioWantRead)).check(matches(not(isClickable())));
            onView(withId(R.id.radioReading)).check(matches(not(isClickable())));
            onView(withId(R.id.radioRead)).check(matches((isChecked())));
        }
    }
}