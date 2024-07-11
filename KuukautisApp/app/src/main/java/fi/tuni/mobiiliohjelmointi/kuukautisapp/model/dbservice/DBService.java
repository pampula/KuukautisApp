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
    boolean userExists(String userId);

    /**
     * Adds a user if not already existing.
     * @return True if adding a new user is successful
     */
    boolean addUser(UserData newUser);

    /**
     * Load user data for user with certain id
     * @param userId UserData's id
     * @return UserData data
     */
    UserData loadUser(String userId);

    /**
     * Save user data
     * @param userdata UserData's data
     * @return True if saving is successful
     */
    boolean saveUser(UserData userdata);

    /**
     * Delete user and their data
     * @param userId UserData's id
     * @return True if deletion is successful
     */
    boolean deleteUser(String userId);

    /**
     * Remove all data from a user
     * @param userId UserData's id
     * @return True if resetting is successful
     */
    boolean resetUser(String userId);
}
