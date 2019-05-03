package CustomFunctions;

import android.app.Application;
import android.content.Context;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserSessionManagerTest {
    @Test
    public void initialize() {
        assertEquals(null, UserSessionManager.Initialize(null));
    }

    @Test
    public void getHashedPassword() {
        UserSessionManager.stubUserDataForTesting("", "Abcd", "", "");
        assertEquals("Abcd", UserSessionManager.getHashedPassword());
    }

    @Test
    public void getUserEmail() {
        UserSessionManager.stubUserDataForTesting("mahmoud@live.com", "", "", "");
        assertEquals("mahmoud@live.com", UserSessionManager.getUserEmail());
    }

    @Test
    public void getUserToken() {
        UserSessionManager.stubUserDataForTesting("", "", "ToKeN", "");
        assertEquals("ToKeN", UserSessionManager.getUserToken());
    }

    @Test
    public void getUserID() {
        UserSessionManager.stubUserDataForTesting("", "", "", "MyID");
        assertEquals("MyID", UserSessionManager.getUserID());
    }

    @Test
    public void getCurrentState() {
        assertEquals(UserSessionManager.UserSessionState.NO_DATA, UserSessionManager.getCurrentState());
    }

    @Test
    public void saveUserData() {
        UserSessionManager.saveUserData("mahmoud@live.com", "Abcd", "ToKeN", "UsErId");
        assertEquals("mahmoud@live.com", UserSessionManager.getUserEmail());
        assertEquals("Abcd", UserSessionManager.getHashedPassword());
        assertEquals("ToKeN", UserSessionManager.getUserToken());
        assertEquals("UsErId", UserSessionManager.getUserID());
        assertEquals(UserSessionManager.UserSessionState.USER_LOGGED_IN, UserSessionManager.getCurrentState());
    }

    @Test
    public void resetUserData() {
        UserSessionManager.resetUserData();
        assertEquals("", UserSessionManager.getUserEmail());
        assertEquals("", UserSessionManager.getHashedPassword());
        assertEquals("", UserSessionManager.getUserToken());
        assertEquals("", UserSessionManager.getUserID());
        assertEquals(UserSessionManager.UserSessionState.NO_DATA, UserSessionManager.getCurrentState());
    }

    @Test
    public void logOutUser() {
        UserSessionManager.logOutUser();
        assertEquals("", UserSessionManager.getUserToken());
        assertEquals(UserSessionManager.UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN, UserSessionManager.getCurrentState());
    }

    @Test
    public void stubUserDataForTesting() {
        APIs.TestingModeEnabled = true;
        UserSessionManager.stubUserDataForTesting("mahmoud@live.com", "Abcd", "ToKeN", "UsErId");
        assertEquals("mahmoud@live.com", UserSessionManager.getUserEmail());
        assertEquals("Abcd", UserSessionManager.getHashedPassword());
        assertEquals("ToKeN", UserSessionManager.getUserToken());
        assertEquals("UsErId", UserSessionManager.getUserID());
    }
}