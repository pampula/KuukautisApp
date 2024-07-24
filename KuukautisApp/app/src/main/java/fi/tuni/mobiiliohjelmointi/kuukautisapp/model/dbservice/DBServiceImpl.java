package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.usermanager.UserManagerSingleton;

public class DBServiceImpl implements DBService {

    private final DataSource dataSource;

    public DBServiceImpl() {
        this.dataSource = new DataSourceFirestoreImpl();
    }

    @Override
    public void userExists(String userId, DBServiceCallback<Boolean> callback) {
        this.dataSource.userExists(userId, callback);
    }

    @Override
    public void addUser(UserData newUser, DBServiceCallback<Boolean> callback) {
        UserManagerSingleton.getInstance().setUserData(newUser);
        this.dataSource.saveUserData(newUser, callback);
    }

    @Override
    public void loadUser(String userId, DBServiceCallback<UserData> callback) {
        this.dataSource.getUserData(userId, new DBServiceCallback<UserData>() {
            @Override
            public void onSuccess(UserData userData) {
                UserManagerSingleton.getInstance().setUserData(userData);
                callback.onSuccess(userData);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public void saveUser(DBServiceCallback<Boolean> callback) {
        UserData userData = UserManagerSingleton.getInstance().getUserData();
        if (userData != null) {
            this.dataSource.saveUserData(userData, callback);
        }
        else {
            callback.onFailure(new Exception("No user data available to save"));
        }
    }

    @Override
    public void deleteUser(String userId, DBServiceCallback<Boolean> callback) {
        this.dataSource.deleteUser(userId, callback);
    }

    @Override
    public void resetUser(String userId, DBServiceCallback<Boolean> callback) {
        this.dataSource.truncateUser(userId, callback);
        UserManagerSingleton.getInstance().resetUserData();
        //TODO: handle resetting user in local memory
    }
}
