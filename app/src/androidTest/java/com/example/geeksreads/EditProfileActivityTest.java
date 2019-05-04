package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class EditProfileActivityTest {
    UserSessionManager m = new UserSessionManager("xYzAbCdToKeN", true);

    @Rule
    public ActivityTestRule<EditProfileActivity> menuActivityTestRule =
            new ActivityTestRule<>(EditProfileActivity.class, true, true);

    @Test
    /* Username less than 3 characters */
    public void Test_UsernameLessThanThreeChars() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Ab"), closeSoftKeyboard());
       onView(withId(R.id.BirthDate))
                .perform(typeText("1/9/1995"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Username length should be 3 characters minimum and 50 characters maximum!", EditProfileActivity.sForTest);
    }


    @Test
    /* Invalid Day Number in Birth Date */
    public void Test_InvalidDayInBirthDate() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Mahmoud Morsy"), closeSoftKeyboard());
        onView(withId(R.id.BirthDate))
                .perform(typeText("91/9/1995"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Please enter a valid date!", EditProfileActivity.sForTest);
    }

    @Test
    /* Invalid Day Number in Birth Date */
    public void Test_InvalidYearInBirthDate() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Mahmoud Morsy"), closeSoftKeyboard());
       onView(withId(R.id.BirthDate))
                .perform(typeText("1/9/-1995"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Please enter a valid date!", EditProfileActivity.sForTest);
    }

    @Test
    /* Invalid Year Number in Birth Date (Choosing future year) */
    public void Test_FutureYearInBirthDate() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Mahmoud Morsy"), closeSoftKeyboard());
       onView(withId(R.id.BirthDate))
                .perform(typeText("1/9/2020"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Please enter a valid date!", EditProfileActivity.sForTest);
    }

    @Test
    /* Birth Date is less than 5 years ago */
    public void Test_BirthDateLessThan5Years() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Mahmoud Morsy"), closeSoftKeyboard());
       onView(withId(R.id.BirthDate))
                .perform(typeText("1/9/2017"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Please enter a valid date!", EditProfileActivity.sForTest);
    }

    @Test
    /* Valid Case with all inputs */
    public void Test_SuccessCase() {
        onView(withId(R.id.UserNameTxt)).perform(clearText());
        onView(withId(R.id.BirthDate)).perform(clearText());
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Mahmoud Morsy"), closeSoftKeyboard());

       onView(withId(R.id.BirthDate))
                .perform(typeText("1/9/1995"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());
        if (!APIs.MimicModeEnabled) return;
        assertEquals("Your changes are saved successfully!", EditProfileActivity.sForTest);
    }
}