package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;


/**
 * Singleton class for having only one logged in user.
 */
public class UserLoginSingleton {

    private static UserLoginSingleton instance;
    private String userId;
    private boolean isLoggedIn;

    private UserLoginSingleton() {}

    /**
     * Gets the singleton instance.
     * @return user login instance
     */
    public static synchronized UserLoginSingleton getInstance() {
        if (instance == null) {
            instance = new UserLoginSingleton();
        }
        return instance;
    }

    /**
     * Change the status of the user as logged in.
     * @param userId logged in user's id
     */
    public void loginUser(String userId) {
        this.userId = userId;
        this.isLoggedIn = true;
    }

    /**
     * Change the status of the user as logged out.
     */
    public void logoutUser() {
        this.userId = null;
        this.isLoggedIn = false;
    }

    /**
     * Get the currently logged in user's id. Null if there is no used logged in.
     * @return user's id
     */
    public String getUserId() {
        return isLoggedIn ? this.userId : null;
    }

    /**
     * Check if user is logged in.
     * @return true if there is a user logged in
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
