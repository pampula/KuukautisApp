package fi.tuni.mobiiliohjelmointi.kuukautisapp;

import java.util.Calendar;

/**
 * Represents a period of time, which is either a menstrual cycle or a menstrual flow.
 */
public class TimePeriod {

    private int length;
    private Calendar firstDay;
    private Calendar lastDay;

    /**
     * Default constructor for a time period.
     * @param length Length in days
     * @param lastDay Last day of time period
     */
    public TimePeriod(int length, Calendar firstDay, Calendar lastDay) {
        this.length = length;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    /**
     * Returns time period length in days.
     * @return length Length in days
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Returns time period's first day.
     * @return Time period's first day
     */
    public Calendar getFirstDay() {
        return this.firstDay;
    }

    /**
     * Returns time period's last day.
     * @return Time period's last day
     */
    public Calendar getLastDay() {
        return this.lastDay;
    }

    /**
     * Adds to the time period's length
     */
    public void addToLength(int amount) {
        this.length += amount;
    }

    /**
     * Sets the time period's last day
     * @param lastDay Time period's last day
     */
    public void setLastDay(Calendar lastDay) {
        this.lastDay = lastDay;
    }

    /**
     * Sets the time period's first day
     * @param firstDay Time period's first day
     */
    public void setFirstDay(Calendar firstDay) {
        this.firstDay = firstDay;
    }

    /**
     * Sets the time period's length
     * @param amount Amount to set as length
     */
    public void setLength(int amount) {
        this.length = amount;
    }
}
