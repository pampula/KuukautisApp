package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

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
        this.dataSource.saveUserData(newUser, callback);
    }

    @Override
    public void loadUser(String userId, DBServiceCallback<UserData> callback) {
        this.dataSource.getUserData(userId, callback);
    }

    @Override
    public void saveUser(UserData userdata, DBServiceCallback<Boolean> callback) {
        this.dataSource.saveUserData(userdata, callback);
    }

    @Override
    public void deleteUser(String userId, DBServiceCallback<Boolean> callback) {
        this.dataSource.deleteUser(userId, callback);
    }

    @Override
    public void resetUser(String userId, DBServiceCallback<Boolean> callback) {
        this.dataSource.truncateUser(userId, callback);
    }
}
