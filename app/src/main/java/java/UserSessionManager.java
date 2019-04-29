package java;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.ContextWrapper.*;
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
    private static boolean isLoggedIn;

    private static SharedPreferences userDataOnDevice;
    private static Context userContext;

    private static UserSessionState CurrentState;

    public static void Initialize(Context context)
    {
        userEmail = "";
        hashedPassword = "";
        userToken = "";
        isLoggedIn = false;
        userContext = context;

        userDataOnDevice = userContext.getSharedPreferences("GeeksReads.Auth.Login",MODE_PRIVATE);

        isLoggedIn = userDataOnDevice.getBoolean("isLoggedIn", false);
        userEmail = userDataOnDevice.getString("userEmail", "");
        hashedPassword = userDataOnDevice.getString("hashedPassword", "");
        userToken = userDataOnDevice.getString("userToken", "");

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

    public static UserSessionState getCurrentState()
    {
        return CurrentState;
    }

    private static void Refresh()
    {
        isLoggedIn = userDataOnDevice.getBoolean("isLoggedIn", false);
        userEmail = userDataOnDevice.getString("userEmail", "");
        hashedPassword = userDataOnDevice.getString("hashedPassword", "");
        userToken = userDataOnDevice.getString("userToken", "");

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

    public static void saveUserData(String _userEmail, String _hashedPassword, String _userToken)
    {
        userEmail = _userEmail;
        hashedPassword = _hashedPassword;
        userToken = _userToken;
        isLoggedIn = true;
        CurrentState = UserSessionState.USER_LOGGED_IN;
        userDataOnDevice.edit().putString("userEmail", userEmail).apply();
        userDataOnDevice.edit().putString("hashedPassword", hashedPassword).apply();
        userDataOnDevice.edit().putString("userToken", userToken).apply();
        userDataOnDevice.edit().putBoolean("isLoggedIn", true).apply();
    }

    public static void resetUserData()
    {
        userEmail = "";
        hashedPassword = "";
        userToken = "";
        isLoggedIn = false;
        CurrentState = UserSessionState.NO_DATA;
        userDataOnDevice.edit().putString("userEmail", "").apply();
        userDataOnDevice.edit().putString("hashedPassword", "").apply();
        userDataOnDevice.edit().putBoolean("isLoggedIn", false).apply();
        userDataOnDevice.edit().putString("userToken", "").apply();
    }

    public static void logOutUser()
    {
        userDataOnDevice.edit().putBoolean("isLoggedIn", false).apply();
        userDataOnDevice.edit().putString("userToken", "").apply();
        userToken = "";
        isLoggedIn = false;
        CurrentState = UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN;
    }

}
