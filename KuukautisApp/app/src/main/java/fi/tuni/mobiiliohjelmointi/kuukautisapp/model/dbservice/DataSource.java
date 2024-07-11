package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Handles the database connection and operations.
 */
public interface DataSource {
    void userExists(String userId, DBService.DBServiceCallback<Boolean> callback);
    void saveUserData(UserData userData, DBService.DBServiceCallback<Boolean> callback);
    void getUserData(String userId, DBService.DBServiceCallback<UserData> callback);
    void deleteUser(String userId, DBService.DBServiceCallback<Boolean> callback);
    void truncateUser(String userId, DBService.DBServiceCallback<Boolean> callback);

}
