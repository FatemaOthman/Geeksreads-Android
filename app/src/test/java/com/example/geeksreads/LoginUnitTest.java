package com.example.geeksreads;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {
    @Test
    public void LoginEmail_isInvalid_MissingDotCom() {
        assertEquals(LoginActivity.verificationErrorType.INVALID_EMAIL, LoginActivity.verifyStaticCredentials("mahmoud_morsy@live", "Mahmoud"));
    }
    @Test
    public void LoginEmail_isInvalid_MissingAt() {
        assertEquals(LoginActivity.verificationErrorType.INVALID_EMAIL, LoginActivity.verifyStaticCredentials("mahmoud_morsy#live.com", "Mahmoud"));
    }
    @Test
    public void LoginEmail_isInvalid_MissingEmailName() {
        assertEquals(LoginActivity.verificationErrorType.INVALID_EMAIL, LoginActivity.verifyStaticCredentials("@live.com", "Mahmoud"));
    }
    @Test
    public void LoginEmail_isInvalid_MissingHostName() {
        assertEquals(LoginActivity.verificationErrorType.INVALID_EMAIL, LoginActivity.verifyStaticCredentials("mahmoud_morsy@.com", "Mahmoud"));
    }
    @Test
    public void LoginPassword_isInvalid_EmptyPassword() {
        assertEquals(LoginActivity.verificationErrorType.INVALID_PASSWORD, LoginActivity.verifyStaticCredentials("mahmoud_morsy@live.com", ""));
    }
    @Test
    public void LoginEmailAndPassword_areValid() {
        assertEquals(LoginActivity.verificationErrorType.NO_ERRORS, LoginActivity.verifyStaticCredentials("mahmoud_morsy@live.com", "Mahmoud"));
    }
}