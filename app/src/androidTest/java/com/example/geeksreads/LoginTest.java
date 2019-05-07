package com.example.geeksreads;
import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;
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
    public void Test_UserDidntWriteValidEmail() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Please enter a valid Email", LoginActivity.sForTest);
    }

    @Test
    /* User Didn't write a password */
    public void Test_UserDidntWritePassword() {

        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Please enter your Geeksreads Login Password", LoginActivity.sForTest);
    }

    @Test
    /* User Didn't Verify Account Yet */
    public void Test_UserDidntVerifyAccount() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("geeksreads.2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Geeks123"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Your account has not been verified.", LoginActivity.sForTest);
    }

    @Test
    /* User entered a wrong password or email */
    public void Test_UserEnteredWrongEmailOrPassword() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_morsy@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Abc1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Invalid email or password.", LoginActivity.sForTest);
    }

    @Test
    /* User logged in successfully */
    public void Test_UserEnteredCorrectEmailAndPassword() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_morsy@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());

        assertEquals("Login Successful", LoginActivity.sForTest);
    }

    @Test
    /* Other error occurred in Login */
    public void Test_UnknownErrorOccurred() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());
        if (!APIs.MimicModeEnabled) return;
        assertEquals("An error occurred!", LoginActivity.sForTest);
    }
    @Test
    /* Other error occurred in Login */
    public void Test_A1() {
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.LoginBtn)).perform(click());
        if (!APIs.MimicModeEnabled) return;
        assertEquals("An error occurred!", LoginActivity.sForTest);
    }
}