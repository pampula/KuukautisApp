package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.usermanager;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

public class UserManagerSingleton implements UserManager {

    private static UserManager instance;
    private UserData userData;

    private UserManagerSingleton() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManagerSingleton();
        }
        return instance;
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public void resetUserData() {
        this.userData.resetData();
    }

    @Override
    public void updateCycleData(CycleData cycleData) {
        this.userData.setCycleData(cycleData);
    }
}
