package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class ForgetPasswordActivityTest {
    UserSessionManager m = new UserSessionManager("xYzAbCdToKeN", true);

    @Rule
    public ActivityTestRule<ForgetPasswordActivity> menuActivityTestRule =
            new ActivityTestRule<>(ForgetPasswordActivity.class, true, true);

    @Test
    /* Verification Code is Empty */
    public void Test_VerificationCodeEmpty() {
        onView(withId(R.id.otpTxt))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Please enter the verification code that has been sent to your email", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Password is less than 6 characters */
    public void Test_UserEnteredPasswordLessThanSixChars() {
        onView(withId(R.id.otpTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("Mah12"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("Mah12"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Password should be 6 characters or more", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Password has no numbers */
    public void Test_UserEnteredPasswordWithoutNumbers() {
        onView(withId(R.id.otpTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Password should contain numbers", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Password has no lower case letters */
    public void Test_UserEnteredPasswordWithNoLowerCaseLetters() {
        onView(withId(R.id.otpTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("123MAHMOUD"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("123MAHMOUD"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Password should contain lower case letters", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Password has no upper case letters */
    public void Test_UserEnteredPasswordWithNoUpperCaseLetters() {
        onView(withId(R.id.otpTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("123mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("123mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Password should contain upper case letters", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Passwords don't match */
    public void Test_PasswordAndConfirmPasswordDidntMatch() {
        onView(withId(R.id.otpTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("123Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("123Mahmoud2"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Passwords don't match", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Saving Password with wrong verification code */
    public void Test_SavingPasswordWithWrongVerificationCode() {
        if (!APIs.MimicModeEnabled) return;
        onView(withId(R.id.otpTxt))
                .perform(typeText("AyKalam"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Invalid verification code.", ForgetPasswordActivity.sForTest);
    }

    @Test
    /* Saving Password Successfully */
    public void Test_SavingPasswordSuccessCase() {
        if (!APIs.MimicModeEnabled) return;
        onView(withId(R.id.otpTxt))
                .perform(typeText("xYzAbCdToKeN"), closeSoftKeyboard());
        onView(withId(R.id.passwordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());
        onView(withId(R.id.confPasswordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());

        onView(withId(R.id.submitChangedPassword)).perform(click());

        assertEquals("Your password is changed successfully", ForgetPasswordActivity.sForTest);
    }
}