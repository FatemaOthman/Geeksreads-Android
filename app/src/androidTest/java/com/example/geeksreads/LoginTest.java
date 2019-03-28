package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class LoginTest {
    @Rule
    public ActivityTestRule<LoginActivity> menuActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Test
    /* User Didn't write a valid Email */
    public void Test_1() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Please enter a valid Email", LoginActivity.sForTest);
    }

    @Test
    /* User Didn't write a password */
    public void Test_2() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Please enter your Geeksreads Login Password", LoginActivity.sForTest);
    }

    @Test
    /* User Didn't Verify Account Yet */
    public void Test_3() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Your account has not been verified.", LoginActivity.sForTest);
    }

    @Test
    /* User entered a wrong password or email */
    public void Test_4() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Abc1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Invalid email or password.", LoginActivity.sForTest);
    }

    @Test
    /* User logged in successfully */
    public void Test_5() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Login Succeeded", LoginActivity.sForTest);
    }

    @Test
    /* Other error occurred in Login */
    public void Test_6() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("An Error Occurred", LoginActivity.sForTest);
    }

}