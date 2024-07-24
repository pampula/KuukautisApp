package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels;


/**
 * Represents a user. Contains their information, app data and settings.
 */
public class UserData {

    private String userId;
    private String userName;
    private Settings settings;
    private CycleData cycleData;

    public UserData() {
    }

    /**
     * Constructor for a user.
     * @param userId user's id
     * @param userName user's name
     * @param settings user's app settings
     * @param cycleData user's cycle data
     */
    public UserData(String userId, String userName, Settings settings, CycleData cycleData) {
        this.userId = userId;
        this.userName = userName;
        this.settings = settings;
        this.cycleData = cycleData;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public CycleData getCycleData() {
        return this.cycleData;
    }

    public void setCycleData(CycleData cycleData) {
        this.cycleData = cycleData;
    }

    public void resetData() {
        this.settings = new Settings();
        this.cycleData = new CycleData();
    }
}
