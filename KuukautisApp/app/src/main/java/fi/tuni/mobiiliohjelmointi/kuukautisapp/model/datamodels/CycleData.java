package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Represents the menstrual cycles and aggregated data.
 */
public class CycleData {
    private ArrayList<Date> menstrualDays;
    private HashSet<Date> menstrualDaysSet;
    private ArrayList<Date> predictionDays;
    private int cycleLengthAvg;
    private int menstruationLengthAvg;
    private final int GENERAL_CYCLE_LENGTH = 28;

    public CycleData(ArrayList<Date> menstrualDays, ArrayList<Date> predictionDays,
                     int cycleLengthAvg, int menstruationLengthAvg) {
        this.menstrualDays = menstrualDays;
        this.menstrualDaysSet = new HashSet<>(menstrualDays);
        this.predictionDays = predictionDays;
        this.cycleLengthAvg = cycleLengthAvg;
        this.menstruationLengthAvg = menstruationLengthAvg;
        calculateAverages();
        calculatePredictions();
    }

    public CycleData() {
        this.menstrualDays = new ArrayList<>();
        this.menstrualDaysSet = new HashSet<>();
        this.predictionDays = new ArrayList<>();
        this.cycleLengthAvg = 0;
        this.menstruationLengthAvg = 0;
    }

    public ArrayList<Date> getMenstrualDays() {
        return this.menstrualDays;
    }

    public void setMenstrualDays(ArrayList<Date> menstrualDays) {
        this.menstrualDays = menstrualDays;
        this.menstrualDaysSet = new HashSet<>(menstrualDays);
        calculateAverages();
        calculatePredictions();
    }

    /**
     * Adds a new menstruation day. Makes sure that the Dates are in order and that the specific
     * Date does not already exist.
     * @param menstrualDay date to add
     */
    public void addMenstrualDay(Date menstrualDay) {
        if (!menstrualDayExists(menstrualDay)) {
            // Keep Dates in order
            int index = Collections.binarySearch(this.menstrualDays, menstrualDay);
            if (index < 0) {
                index = -index - 1;
            }
            this.menstrualDays.add(index, menstrualDay);
            this.menstrualDaysSet.add(menstrualDay);
            calculateAverages();
            calculatePredictions();
        }
    }

    /**
     * Removes a menstruation day, if it exists.
     * @param menstrualDay date to remove
     */
    public void removeMenstrualDay(Date menstrualDay) {
        if (menstrualDayExists(menstrualDay)) {
            this.menstrualDays.remove(menstrualDay);
            this.menstrualDaysSet.remove(menstrualDay);
            calculateAverages();
            calculatePredictions();
        }
    }

    public boolean menstrualDayExists(Date menstrualDay) {
        return this.menstrualDaysSet.contains(menstrualDay);
    }

    public ArrayList<Date> getPredictionDays() {
        return this.predictionDays;
    }

    public void setPredictionDays(ArrayList<Date> predictionDays) {
        this.predictionDays = predictionDays;
    }

    public int getCycleLengthAvg() {
        return this.cycleLengthAvg;
    }

    public void setCycleLengthAvg(int cycleLengthAvg) {
        this.cycleLengthAvg = cycleLengthAvg;
    }

    public int getMenstruationLengthAvg() {
        return this.menstruationLengthAvg;
    }

    public void setMenstruationLengthAvg(int menstruationLengthAvg) {
        this.menstruationLengthAvg = menstruationLengthAvg;
    }

    /**
     * Calculates predictions for the next three menstruations' start dates.
     */
    private void calculatePredictions() {
        this.predictionDays.clear();
        if (this.menstrualDays.isEmpty()) {
            return;
        }

        int cycleLength = (cycleLengthAvg == 0) ? GENERAL_CYCLE_LENGTH : cycleLengthAvg;
        Date lastCycleStart = menstrualDays.get(0);

        // Identify the start of the latest cycle
        for (int i = menstrualDays.size() - 1; i > 0; i--) {
            long diff = getDateDiff(menstrualDays.get(i - 1), menstrualDays.get(i), TimeUnit.DAYS);
            if (diff > 1) {
                lastCycleStart = menstrualDays.get(i);
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            Date predictionDate =
                    new Date(lastCycleStart.getTime() + TimeUnit.DAYS.toMillis((long) cycleLength * i));
            predictionDays.add(predictionDate);
        }
    }

    /**
     * Calculates the averages for cycle length and menstruation length.
     */
    private void calculateAverages() {
        this.cycleLengthAvg = calculateCycleLengthAvg();
        this.menstruationLengthAvg = calculateMenstruationLengthAvg();
    }

    /**
     * Calculates the average cycle length based on the last three cycles.
     *
     * @return the average cycle length in days
     */
    private int calculateCycleLengthAvg() {
        if (menstrualDays == null || menstrualDays.size() < 4) {
            return 0;
        }

        ArrayList<Date> cycleStartDays = new ArrayList<>();
        // Identify the start of the last cycles
        for (int i = menstrualDays.size() - 1; i > 0; i--) {
            long diff = getDateDiff(menstrualDays.get(i - 1), menstrualDays.get(i), TimeUnit.DAYS);
            if (diff > 1) {
                cycleStartDays.add(menstrualDays.get(i));
                if (cycleStartDays.size() == 3) break;
            }
        }

        // Ensure to add the very first menstrual day to cover edge cases
        if (cycleStartDays.size() < 3 && !cycleStartDays.contains(menstrualDays.get(0))) {
            cycleStartDays.add(menstrualDays.get(0));
        }

        if (cycleStartDays.size() < 3) {
            return 0;
        }

        Collections.sort(cycleStartDays);

        ArrayList<Long> cycleLengths = new ArrayList<>();

        for (int i = 1; i < cycleStartDays.size(); i++) {
            long diff = getDateDiff(cycleStartDays.get(i - 1), cycleStartDays.get(i), TimeUnit.DAYS);
            cycleLengths.add(diff);
        }

        return (int) cycleLengths.stream().mapToLong(Long::longValue).average().orElse(0);
    }

    /**
     * Calculates the average menstruation length based on the last three menstruation periods.
     *
     * @return the average menstruation length in days
     */
    private int calculateMenstruationLengthAvg() {
        if (menstrualDays == null || menstrualDays.size() < 4) {
            return 0;
        }

        int size = menstrualDays.size();
        ArrayList<Long> menstruationLengths = new ArrayList<>();
        long length = 1;

        for (int i = size - 1; i > 0; i--) {
            long diff = getDateDiff(menstrualDays.get(i - 1), menstrualDays.get(i), TimeUnit.DAYS);
            if (diff == 1) {
                length++;
            } else {
                menstruationLengths.add(length);
                if (menstruationLengths.size() == 3) break;
                length = 1;
            }
        }
        if (menstruationLengths.size() < 3) {
            menstruationLengths.add(length);
        }

        return (int) menstruationLengths.stream().mapToLong(Long::longValue).average().orElse(0);
    }

    /**
     * Helper method to calculate the difference between two dates in the specified time unit.
     *
     * @param date1 the first date
     * @param date2 the second date
     * @param timeUnit the unit of time to return the difference in
     * @return the difference in the specified time unit
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
