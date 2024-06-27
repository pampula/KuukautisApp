package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;

public class DBServiceImpl implements DBService{
    private final DataSource dataSource;

    public DBServiceImpl() {
        this.dataSource = new DataSourceImpl();
    }

    @Override
    public boolean userExists(String userId) {
        return this.dataSource.userExists(userId);
    }

    @Override
    public boolean addUser(UserData newUser) {
        return this.dataSource.saveUserData(newUser);
    }

    @Override
    public UserData loadUser(String userId) {
        return this.dataSource.getUserData(userId);
    }

    @Override
    public boolean saveUser(UserData userdata) {
        return this.dataSource.saveUserData(userdata);
    }

    @Override
    public boolean deleteUser(String userId) {
        return this.dataSource.deleteUser(userId);
    }

    @Override
    public boolean resetUser(String userId) {
        return this.dataSource.truncateUser(userId);
    }
}
