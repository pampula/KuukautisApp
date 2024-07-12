package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Handles the database connection and operations.
 */
public interface DataSource {

    /**
     * Checks that a user exists in the database.
     * @param userId user to check
     * @param callback success message
     */
    void userExists(String userId, DBService.DBServiceCallback<Boolean> callback);

    /**
     * Save user data to database.
     * @param userData user data to save
     * @param callback success message
     */
    void saveUserData(UserData userData, DBService.DBServiceCallback<Boolean> callback);

    /**
     * Gets user data from database.
     * @param userId user to get
     * @param callback success message
     */
    void getUserData(String userId, DBService.DBServiceCallback<UserData> callback);

    /**
     * Delete user from database.
     * @param userId user to delete
     * @param callback success message
     */
    void deleteUser(String userId, DBService.DBServiceCallback<Boolean> callback);

    /**
     * Remove all data from user but not the user itself.
     * @param userId user to truncate
     * @param callback success message
     */
    void truncateUser(String userId, DBService.DBServiceCallback<Boolean> callback);

}
