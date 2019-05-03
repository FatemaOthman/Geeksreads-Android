package CustomFunctions;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.ContextWrapper.*;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static android.content.Context.MODE_PRIVATE;

/*
 * This Class handles all login session information; stores them and provide public apis for other
 * layouts to retrieve needed and cashed information about the user
 */
public class UserSessionManager {
    public enum UserSessionState
    {
        NO_DATA,
        USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN,
        USER_LOGGED_IN
    }

    private static String userEmail;
    private static String hashedPassword;
    private static String userToken;
    private static String userID;
    private static boolean isLoggedIn;

    private static SharedPreferences userDataOnDevice;
    private static Context userContext;

    private static UserSessionState CurrentState;
    private static boolean isInitialized = false;

    public UserSessionManager(String _userToken, boolean _isTest)
    {
        if (_isTest)
        {
            userToken = _userToken;
        }
    }
    public static Context Initialize(Context context)
    {
        if (context == null) return null;
        userEmail = "";
        hashedPassword = "";
        userToken = "";
        userID = "";
        isLoggedIn = false;
        userContext = context;

        userDataOnDevice = userContext.getSharedPreferences("GeeksReads.Auth.Login",MODE_PRIVATE);

        isLoggedIn = userDataOnDevice.getBoolean("isLoggedIn", false);
        userEmail = userDataOnDevice.getString("userEmail", "");
        hashedPassword = userDataOnDevice.getString("hashedPassword", "");
        userToken = userDataOnDevice.getString("userToken", "");
        userID = userDataOnDevice.getString("userID", "");

        if (isLoggedIn && !userToken.isEmpty())
        {
            CurrentState = UserSessionState.USER_LOGGED_IN;
        }
        else if (!userEmail.isEmpty() && !hashedPassword.isEmpty())
        {
            CurrentState = UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN;
        }
        else
        {
            CurrentState = UserSessionState.NO_DATA;
        }
        isInitialized = true;
        return context;
    }

    public static String getHashedPassword()
    {
        return hashedPassword;
    }

    public static String getUserEmail()
    {
        return userEmail;
    }

    public static String getUserToken()
    {
        return userToken;
    }

    public static String getUserID()
    {
        return userID;
    }

    public static UserSessionState getCurrentState()
    {
        return CurrentState;
    }

    private static void Refresh()
    {
        if (!isInitialized) return;
        isLoggedIn = userDataOnDevice.getBoolean("isLoggedIn", false);
        userEmail = userDataOnDevice.getString("userEmail", "");
        hashedPassword = userDataOnDevice.getString("hashedPassword", "");
        userToken = userDataOnDevice.getString("userToken", "");
        userID = userDataOnDevice.getString("userID", "");

        if (isLoggedIn)
        {
            CurrentState = UserSessionState.USER_LOGGED_IN;
        }
        else if (!userEmail.isEmpty() && !userToken.isEmpty() && !hashedPassword.isEmpty())
        {
            CurrentState = UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN;
        }
        else
        {
            CurrentState = UserSessionState.NO_DATA;
        }
    }
    /* This function should be called when used signs In to save his token and user ID and set
     * Logged in parameter to be true
     */
    public static void saveUserData(String _userEmail, String _hashedPassword, String _userToken, String _userID)
    {
        userEmail = _userEmail;
        hashedPassword = _hashedPassword;
        userToken = _userToken;
        userID = _userID;
        isLoggedIn = true;
        CurrentState = UserSessionState.USER_LOGGED_IN;
        if (!isInitialized) return;
        userDataOnDevice.edit().putString("userEmail", userEmail).apply();
        userDataOnDevice.edit().putString("hashedPassword", hashedPassword).apply();
        userDataOnDevice.edit().putString("userToken", userToken).apply();
        userDataOnDevice.edit().putString("userID", userID).apply();
        userDataOnDevice.edit().putBoolean("isLoggedIn", true).apply();
    }
    /* This function resets all registered data about the user */
    public static void resetUserData()
    {
        userEmail = "";
        hashedPassword = "";
        userToken = "";
        isLoggedIn = false;
        CurrentState = UserSessionState.NO_DATA;
        if (!isInitialized) return;
        userDataOnDevice.edit().putString("userEmail", "").apply();
        userDataOnDevice.edit().putString("hashedPassword", "").apply();
        userDataOnDevice.edit().putBoolean("isLoggedIn", false).apply();
        userDataOnDevice.edit().putString("userToken", "").apply();
        userDataOnDevice.edit().putString("userID", "").apply();
    }
    /* This function should be called when used signs out to delete his token and user ID and reset
     * Logged in parameter to be false
     */
    public static void logOutUser()
    {
        userToken = "";
        isLoggedIn = false;
        CurrentState = UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN;
        if (!isInitialized) return;
        userDataOnDevice.edit().putBoolean("isLoggedIn", false).apply();
        userDataOnDevice.edit().putString("userToken", "").apply();
        userDataOnDevice.edit().putString("userID", "").apply();
    }

    public static void stubUserDataForTesting(String _userEmail, String _hashedPassword, String _userToken, String _userID)
    {
        if (APIs.TestingModeEnabled)
        {
            userEmail = _userEmail;
            hashedPassword = _hashedPassword;
            userToken = _userToken;
            userID = _userID;
        }
    }
}
