package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels;

/**
 * Represents the user settings of the application.
 */
public class Settings {
    //TODO: application settings
    private String dummySetting;

    public Settings() {
        this.dummySetting = "placeholder";
    }

    public String getDummySetting() {
        return dummySetting;
    }

    public void setDummySetting(String dummySetting) {
        this.dummySetting = dummySetting;
    }
}
