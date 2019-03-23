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

public class SignupActivityTest {
    @Rule
    public ActivityTestRule<SignupActivity> menuActivityTestRule =
            new ActivityTestRule<>(SignupActivity.class, true, true);

    @Test
    public void Test_1() {
        /* Username less than 3 characters */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Ab"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Username length should be 3 characters minimum and 50 characters maximum",SignupActivity.forTest);
    }
    @Test
    public void Test_2() {
        /* Email is not valid */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Please enter a valid Email",SignupActivity.forTest);
    }
    @Test
    public void Test_3() {
        /* Password is less than 6 characters */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should be 6 characters or more",SignupActivity.forTest);
    }
    @Test
    public void Test_4() {
        /* Password has no numbers */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain numbers",SignupActivity.forTest);
    }
    @Test
    public void Test_5() {
        /* Password has no lower case letters */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("MAH1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("MAH1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain lower case letters",SignupActivity.forTest);
    }
    @Test
    public void Test_6() {
        /* Password has no upper case letters */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain upper case letters",SignupActivity.forTest);
    }
    @Test
    public void Test_7() {
        /* Passwords don't match */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("mah1142010"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain upper case letters",SignupActivity.forTest);
    }
    @Test
    public void Test_8() {
        /* Creating account and Waiting Email Verification */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("A verification email has been sent to mahmoud_1@live.com",SignupActivity.forTest);
    }
    @Test
    public void Test_9() {
        /* Creating account but User is already registered */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("User already registered",SignupActivity.forTest);
    }
    @Test
    public void Test_10() {
        /* Creating account and an error occurred */
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("An Error Occurred",SignupActivity.forTest);
    }
}