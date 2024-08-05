package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice;

import java.util.ArrayList;
import java.util.Date;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.MenstrualDayObserver;

/**
 * Manages the menstrual cycle data.
 */
public interface CycleService {

    /**
     * Register observer that notifies when menstrual days are added or removed.
     * @param observer observer to register
     */
    void registerObserver(MenstrualDayObserver observer);

    /**
     * Gets all menstruation days.
     * @return list of Dates
     */
    ArrayList<Date> getMenstrualDays();

    /**
     * Gets all predictions for the start of following menstruations.
     * @return list of Dates
     */
    ArrayList<Date> getPredictionDays();

    /**
     * Gets the average length of a menstrual cycle.
     * @return length in days
     */
    int getCycleLengthAvg();

    /**
     * Gets the average length of a menstruation.
     * @return length in days
     */
    int getMenstruationLengthAvg();

    /**
     * Adds a new menstruation day.
     * @param menstrualDay date to be added
     */
    void addMenstrualDay(Date menstrualDay);

    /**
     * Removes a menstruation day.
     * @param menstrualDay date to remove
     */
    void removeMenstrualDay(Date menstrualDay);

    /**
     * Checks if the specific menstrual day exists.
     * @param menstrualDay date to check
     * @return true if date exists
     */
    boolean menstrualDayExists(Date menstrualDay);
}
