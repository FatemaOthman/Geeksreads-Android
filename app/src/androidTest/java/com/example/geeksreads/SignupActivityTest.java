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
    /* Username less than 3 characters */
    public void Test_1() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("Ab"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Username length should be 3 characters minimum and 50 characters maximum",SignupActivity.sForTest);
    }
    @Test
    /* Email is not valid */
    public void Test_2() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Please enter a valid Email",SignupActivity.sForTest);
    }
    @Test
    /* Password is less than 6 characters */
    public void Test_3() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should be 6 characters or more",SignupActivity.sForTest);
    }
    @Test
    /* Password has no numbers */
    public void Test_4() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain numbers",SignupActivity.sForTest);
    }
    @Test
    /* Password has no lower case letters */
    public void Test_5() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("MAH1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("MAH1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain lower case letters",SignupActivity.sForTest);
    }
    @Test
    /* Password has no upper case letters */
    public void Test_6() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain upper case letters",SignupActivity.sForTest);
    }
    @Test
    /* Passwords don't match */
    public void Test_7() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("mah1142010"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("Password should contain upper case letters",SignupActivity.sForTest);
    }
    @Test
    /* Creating account and Waiting Email Verification */
    public void Test_8() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_1@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("A verification email has been sent to mahmoud_1@live.com",SignupActivity.sForTest);
    }
    @Test
    /* Creating account but User is already registered */
    public void Test_9() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_2@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("User already registered",SignupActivity.sForTest);
    }
    @Test
    /* Creating account and an error occurred */
    public void Test_10() {
        onView(withId(R.id.UserNameTxt))
                .perform(typeText("MahmoudMorsy"), closeSoftKeyboard());
        onView(withId(R.id.EmailTxt))
                .perform(typeText("mahmoud_3@live.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmPasswordTxt))
                .perform(typeText("Mah1142019"), closeSoftKeyboard());

        onView(withId(R.id.SignupBtn)).perform(click());

        assertEquals("An Error Occurred",SignupActivity.sForTest);
    }
}