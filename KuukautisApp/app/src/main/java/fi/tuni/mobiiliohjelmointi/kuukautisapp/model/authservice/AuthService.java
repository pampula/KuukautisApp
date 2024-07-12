package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Service for handling user registration, login and logout.
 */
public interface AuthService {

    /**
     * Registers a new user with email and password.
     * @param email user's email
     * @param password user's password
     * @param callback registration's success
     */
    void registerUser(String email, String password, AuthServiceCallback<String> callback);

    /**
     * Logs in a user with email and password.
     * @param email user's email
     * @param password user's password
     * @param callback login's success
     */
    void loginUser(String email, String password, AuthServiceCallback<String> callback);

    /**
     * Logs out a user.
     * @param callback logout's success
     */
    void logoutUser(AuthServiceCallback<Boolean> callback);

    /**
     * Gets the current logged in user's id. Returns null if there is no logged in user.
     * @return user id
     */
    String getCurrentUserId();

    /**
     * Callback message of the authorization's success.
     * @param <T>
     */
    interface AuthServiceCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
