package com.example.geeksreads;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ChangePasswordUnitTest {

    @Test
    public void ChangePassword_isInvalid_EmptyOldPassword() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.OLD_PASSWORD_EMPTY,
                ChangePasswordActivity.verifyChangePassword("","Mahmoud123", "Mahmoud123"));
    }
    @Test
    public void ChangePassword_isInvalid_EmptyNewPassword() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy","", ""));
    }
    @Test
    public void ChangePassword_isInvalid_LessThan6Chars() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "Mah12", "Mah12"));
    }
    @Test
    public void ChangePassword_isInvalid_NoNumbers() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_HAS_NO_NUMBERS,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void ChangePassword_isInvalid_NoLowerCaseLetters() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_HAS_NO_LOWERCASE,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "MAHMOUD123", "MAHMOUD123"));
    }
    @Test
    public void ChangePassword_isInvalid_NoUpperCaseLetters() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_HAS_NO_UPPERCASE,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "mahmoud123", "mahmoud123"));
    }
    @Test
    public void ChangePassword_isInvalid_PasswordsDontMatch() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_DONT_MATCH,
                ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "Mahmoud123", "Mahmoud124"));
    }
    @Test
    public void ChangePassword_isInvalid_NewPasswordEqualOldPassword() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NEW_PASSWORD_EQUAL_OLD,
                ChangePasswordActivity.verifyChangePassword("Mahmoud123", "Mahmoud123", "Mahmoud123"));
    }
    @Test
    public void ChangePasswordData_areValid() {
        assertEquals(ChangePasswordActivity.changePasswordErrors.NO_ERRORS, ChangePasswordActivity.verifyChangePassword("MahmoudMorsy", "Mahmoud123", "Mahmoud123"));
    }
}