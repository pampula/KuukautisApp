package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.usermanager;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;

/**
 * Singleton service for managing and updating user data.
 */
public interface UserManager {

    /**
     * Gets user data.
     * @return UserData
     */
    UserData getUserData();

    /**
     * Set new data for user.
     * @param userData user's data
     */
    void setUserData(UserData userData);

    /**
     * Resets the user's data exept from name and id.
     */
    void resetUserData();

    /**
     * Updates the cycle data for the user.
     * @param cycleData updated cycle data
     */
    void updateCycleData(CycleData cycleData);

}
