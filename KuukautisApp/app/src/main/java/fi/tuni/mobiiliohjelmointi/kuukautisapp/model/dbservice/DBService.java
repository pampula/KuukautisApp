package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * The class handles saving and fetching the data belonging to a user.
 */
public interface DBService {
    /**
     * Checks if the user with a user id exists.
     * @param userId UserData's id
     * @return True if user exists
     */
    void userExists(String userId, DBServiceCallback<Boolean> callback);

    /**
     * Adds a user if not already existing.
     * @return True if adding a new user is successful
     */
    void addUser(UserData newUser, DBServiceCallback<Boolean> callback);

    /**
     * Load user data for user with certain id
     * @param userId UserData's id
     * @return UserData data
     */
    void loadUser(String userId, DBServiceCallback<UserData> callback);

    /**
     * Save user data
     * @param userdata UserData's data
     * @return True if saving is successful
     */
    void saveUser(UserData userdata, DBServiceCallback<Boolean> callback);

    /**
     * Delete user and their data
     * @param userId UserData's id
     * @return True if deletion is successful
     */
    void deleteUser(String userId, DBServiceCallback<Boolean> callback);

    /**
     * Remove all data from a user
     * @param userId UserData's id
     * @return True if resetting is successful
     */
    void resetUser(String userId, DBServiceCallback<Boolean> callback);

    interface DBServiceCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
