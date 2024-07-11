package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels;

/**
 * Observer that creates a notification when the menstrual day data is updated.
 */
public interface MenstrualDayObserver {

    /**
     * Notify observers when a menstual day is added or removed.
     */
    void onMenstrualDaysUpdated();
}
