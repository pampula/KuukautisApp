package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

/**
 * Handles the database connection and operations.
 */
public interface DataSource {
    boolean userExists(String userId);
    UserData getUserData(String userId);
    boolean saveUserData(UserData user);
    boolean deleteUser(String userId);
    boolean truncateUser(String userId);

}
