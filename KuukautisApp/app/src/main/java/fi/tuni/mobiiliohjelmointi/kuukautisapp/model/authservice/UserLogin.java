package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Singleton class for having only one logged in user.
 */
public class UserLogin {

    private static UserLogin instance;
    private String userId;
    private boolean isLoggedIn;

    private UserLogin() {}

    /**
     * Gets the singleton instance.
     * @return user login instance
     */
    public static synchronized UserLogin getInstance() {
        if (instance == null) {
            instance = new UserLogin();
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
