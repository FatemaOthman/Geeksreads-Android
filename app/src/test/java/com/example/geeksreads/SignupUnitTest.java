package com.example.geeksreads;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SignupUnitTest {
    @Test
    public void SignupUsername_isInvalid_LessThan3Chars() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_USERNAME_LENGTH, SignupActivity.validateSignUpData("Ma", "mahmoud_morsy@live", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupUsername_isInvalid_MoreThan50Chars() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_USERNAME_LENGTH, SignupActivity.validateSignUpData("AbcdefghijklmnopqrstuvwxyzAbcdefghijklmnopqrstuvwxyz", "mahmoud_morsy@live", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupEmail_isInvalid_MissingDotCom() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_EMAIL, SignupActivity.validateSignUpData("MahmoudMorsy", "mahmoud_morsy@live", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupEmail_isInvalid_MissingAt() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_EMAIL, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy#live.com", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupEmail_isInvalid_MissingEmailName() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_EMAIL, SignupActivity.validateSignUpData("MahmoudMorsy","@live.com", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupEmail_isInvalid_MissingHostName() {
        assertEquals(SignupActivity.signUpValidationErrors.INVALID_EMAIL, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@.com", "Mahmoud", "Mahmoud"));
    }

    @Test
    public void SignupPassword_isInvalid_EmptyPassword() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "", ""));
    }
    @Test
    public void SignupPassword_isInvalid_LessThan6Chars() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "Mah12", "Mah12"));
    }
    @Test
    public void SignupPassword_isInvalid_NoNumbers() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_HAS_NO_NUMBERS, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "Mahmoud", "Mahmoud"));
    }
    @Test
    public void SignupPassword_isInvalid_NoLowerCaseLetters() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_HAS_NO_LOWERCASE, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "MAHMOUD123", "MAHMOUD123"));
    }
    @Test
    public void SignupPassword_isInvalid_NoUpperCaseLetters() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_HAS_NO_UPPERCASE, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "mahmoud123", "mahmoud123"));
    }
    @Test
    public void SignupPassword_isInvalid_PasswordsDontMatch() {
        assertEquals(SignupActivity.signUpValidationErrors.NEW_PASSWORD_DONT_MATCH, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "Mahmoud123", "Mahmoud124"));
    }
    @Test
    public void SignupPassword_areValid() {
        assertEquals(SignupActivity.signUpValidationErrors.NO_ERRORS, SignupActivity.validateSignUpData("MahmoudMorsy","mahmoud_morsy@live.com", "Mahmoud123", "Mahmoud123"));
    }
}