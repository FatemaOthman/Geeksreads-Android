package CustomFunctions;
import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

/**
 * @author Mahmoud MORSY,
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

    /* Private Variables */
    private static String userEmail;
    private static String hashedPassword;
    private static String userToken;
    private static String userID;
    private static boolean isLoggedIn;
    private static SharedPreferences userDataOnDevice;
    private static Context userContext;
    private static UserSessionState CurrentState;
    private static boolean isInitialized = false;

    /**
     * Constructor function for UserSession Manager Class
     * @param _userToken Input String for UserToken
     * @param _isTest Input Boolean if it's used for Testing
     */
    public UserSessionManager(String _userToken, boolean _isTest)
    {
        if (_isTest)
        {
            userToken = _userToken;
        }
    }

    /**
     * Function to Initialize the UserSessionManager Class & Load Saved Data in Device
     * @param context Input Context of the Calling Activity
     * @return The passed Context if function worked correctly
     */
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

    /**
     * Function to get the Saved Hashed User Password
     * @return Saved Hashed Password of Current User
     */
    public static String getHashedPassword()
    {
        return hashedPassword;
    }

    /**
     * Function to get the User Saved Email Address
     * @return Saved User Email of Current User
     */
    public static String getUserEmail()
    {
        return userEmail;
    }

    /**
     * Function to get the Saved User Token
     * @return Saved User Token of Current User
     */
    public static String getUserToken()
    {
        return userToken;
    }
    /**
     * Function to get the user ID of the Current User
     * @return Saved User ID of Current User
     */
    public static String getUserID()
    {
        return userID;
    }
    /**
     * Function to get the Current Status of the User Session
     * @return Current Status of User Session: NO_DATA, LOGGED_IN, etc.
     */
    public static UserSessionState getCurrentState()
    {
        return CurrentState;
    }

    /**
     * This function should be called when user signs In to save his token and user ID and set
     * logged in parameter to be true
     * @param _userEmail : Current User Email
     * @param _hashedPassword : Current User Hashed Password
     * @param _userToken : Current User Token
     * @param _userID : Current User ID
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
    /**
     * This function resets all registered data about the user
     */
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
    /** This function should be called when used signs out to delete his token and user ID and reset
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

    /**
     * This function should be called when used while TESTING to save stubbed token and user ID and set
     * logged in parameter to be true
     * @param _userEmail : Current User Email
     * @param _hashedPassword : Current User Hashed Password
     * @param _userToken : Current User Token
     * @param _userID : Current User ID
     */
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
