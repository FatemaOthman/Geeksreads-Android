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

public class ChangePasswordActivityTest {
    UserSessionManager m = new UserSessionManager("xYzAbCdToKeN", true);

    @Rule
    public ActivityTestRule<ChangePasswordActivity> menuActivityTestRule =
            new ActivityTestRule<>(ChangePasswordActivity.class, true, true);

    @Test
    /* Old Password is Empty */
    public void Test_OldPasswordEmpty() {
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Please enter your old password", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Password is less than 6 characters */
    public void Test_UserEnteredPasswordLessThanSixChars() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mah12"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mah12"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Password should be 6 characters or more", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Password has no numbers */
    public void Test_UserEnteredPasswordWithoutNumbers() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Password should contain numbers", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Password has no lower case letters */
    public void Test_UserEnteredPasswordWithNoLowerCaseLetters() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("123MAHMOUD"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("123MAHMOUD"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Password should contain lower case letters", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Password has no upper case letters */
    public void Test_UserEnteredPasswordWithNoUpperCaseLetters() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("123mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("123mahmoud"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Password should contain upper case letters", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Passwords don't match */
    public void Test_PasswordAndConfirmPasswordDidntMatch() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mah11"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("123Mahmoud"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("123Mahmoud2"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Passwords don't match", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Passwords don't match */
    public void Test_OldAndNewPasswordEqual() {
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("New password cannot be the same old password", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Saving Password with old wrong password */
    public void Test_SavingPasswordWithWrongOldPassword() {
        if (!APIs.MimicModeEnabled) return;
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("AyKalam"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("You entered a wrong old password!", ChangePasswordActivity.sForTest);
    }

    @Test
    /* Saving Password Successfully */
    public void Test_SavingPasswordSuccessCase() {
        if (!APIs.MimicModeEnabled) return;
        onView(withId(R.id.OldPasswordTxt))
                .perform(typeText("Mahmoud123456789"), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());
        onView(withId(R.id.ConfirmNewPasswordTxt))
                .perform(typeText("Mahmoud1234567890"), closeSoftKeyboard());

        onView(withId(R.id.SaveChangesBtn)).perform(click());

        assertEquals("Password changed successfully!", ChangePasswordActivity.sForTest);
    }
}