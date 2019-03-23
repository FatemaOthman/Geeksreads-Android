package com.example.geeksreads;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginTest {
    @Rule
    public ActivityTestRule<LoginActivity> menuActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Test
    public void Test_1() {
        /* User Didn't Verify Account Yet */
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Please enter a valid Email",LoginActivity.forTest);
    }

    @Test
    public void Test_2() {
        /* User Didn't Verify Account Yet */
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Your account has not been verified.",LoginActivity.forTest);
    }

    @Test
    public void Test_3() {
        /* User entered a wrong password or email */
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Abc1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Invalid email or password.",LoginActivity.forTest);
    }

    @Test
    public void Test_4() {
        /* User logged in successfully */
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Login Succeeded",LoginActivity.forTest);
    }

    @Test
    public void Test_5() {
        /* Other error occurred in Login */
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("An Error Occurred",LoginActivity.forTest);
    }

}