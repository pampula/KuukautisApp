package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * The class handles saving and fetching the data belonging to a user.
 */
public interface DBService {
    /**
     * Checks if the user with a user id exists.
     * @param userId UserData's id
     */
    void userExists(String userId, DBServiceCallback<Boolean> callback);

    /**
     * Adds a user if not already existing.
     */
    void addUser(UserData newUser, DBServiceCallback<Boolean> callback);

    /**
     * Load user data for user with certain id
     * @param userId UserData's id
     */
    void loadUser(String userId, DBServiceCallback<UserData> callback);

    /**
     * Save user data
     */
    void saveUser(DBServiceCallback<Boolean> callback);

    /**
     * Delete user and their data
     * @param userId UserData's id
     */
    void deleteUser(String userId, DBServiceCallback<Boolean> callback);

    /**
     * Remove all data from a user
     * @param userId UserData's id
     */
    void resetUser(String userId, DBServiceCallback<Boolean> callback);

    interface DBServiceCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
