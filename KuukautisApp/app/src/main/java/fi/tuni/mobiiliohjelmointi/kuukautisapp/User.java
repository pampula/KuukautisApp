package fi.tuni.mobiiliohjelmointi.kuukautisapp;

import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Representation of a single user. Holds information about saved calendar markings.
 */
public class User {

    private final String REMOVE_TAG = "REMOVE";
    private final String USER_TAG = "USER";
    private final String PREDICTION_TAG = "PREDICTION";
    private final String CYCLE_TAG = "CYCLE";
    private final String FLOW_TAG = "FLOW";
    private final String AVERAGES_TAG = "AVERAGES";

    private List<EventDay> calendarMarkings;
    private List<TimePeriod> cycleLengths;
    private List<TimePeriod> flowLengths;
    private EventDay nextFlowStartDay;
    private int cycleLengthAvg;
    private int flowLengthAvg;

    /**
     * Default constructor for a user.
     * @param calendarMarkings List of marked period flow days
     * @param cycleLengths How many days has each period cycle lasted
     * @param flowLengths How many days has each period flow lasted
     * @param nextFlowStartDay Prediction for the next period flow starting day
     * @param cycleLengthAvg Average length of last cycles
     * @param flowLengthAvg Average length of last flows
     */
    public User(List<EventDay> calendarMarkings, List<TimePeriod> cycleLengths,
                List<TimePeriod> flowLengths, EventDay nextFlowStartDay,
                int cycleLengthAvg, int flowLengthAvg) {
        this.calendarMarkings = calendarMarkings;
        this.cycleLengths = cycleLengths;
        this.flowLengths = flowLengths;
        this.nextFlowStartDay = nextFlowStartDay;
        this.cycleLengthAvg = cycleLengthAvg;
        this.flowLengthAvg = flowLengthAvg;
    }

    /**
     * Returns the user's saved calendar markings.
     * @return calendarMarkings List of marked period flow days
     */
    public List<EventDay> getCalendarMarkings() {
        return this.calendarMarkings;
    }

    /**
     * Returns the user's cycle lengths.
     * @return cycleLengths List of calculated cycle lengths
     */
    public List<TimePeriod> getCycleLengths() {
        return this.cycleLengths;
    }

    /**
     * Returns the user's flow lengths.
     * @return flowLengths List of calculated flow lengths
     */
    public List<TimePeriod> getFlowLengths() {
        return this.flowLengths;
    }

    /**
     * Returns the latest prediction for the next period flow starting day.
     * @return nextFlowStartDay Prediction for the next period flow starting day
     */
    public EventDay getNextFlowStartDay() {
        return this.nextFlowStartDay;
    }

    /**
     * Returns the average length of last cycles.
     * @return cycleLengthAvg Average length of last cycles
     */
    public int getCycleLengthAvg() {
        return this.cycleLengthAvg;
    }

    /**
     * Returns the average length of last flows.
     * @return flowLengthAvg Average length of last flows
     */
    public int getFlowLengthAvg() {
        return this.flowLengthAvg;
    }

    /**
     * Sets new markings as calendar markings.
     * @param newMarkings New calendar markings
     */
    public void setCalendarMarkings(List<EventDay> newMarkings) {
        this.calendarMarkings = newMarkings;
    }

    /**
     * Sets new values as cycle lengths.
     * @param newCycleLengths New cycle length values
     */
    public void setCycleLengths(List<TimePeriod> newCycleLengths) {
        this.cycleLengths = newCycleLengths;
    }

    /**
     * Sets new values as flow lengths.
     * @param newFlowLengths New flow length values
     */
    public void setFlowLengths(List<TimePeriod> newFlowLengths) {
        this.flowLengths = newFlowLengths;
    }

    /**
     * Checks if user has selected flow marking.
     * @param eventDay Marking to find
     * @return True if user has the marking
     */
    public boolean hasMarking(EventDay eventDay) {
        return calendarMarkings.contains(eventDay);
    }

    /**
     * Saves a new marking for the user and updates prediction values.
     * @param newMarking New period flow day marking to save
     */
    public void addCalendarMarking(EventDay newMarking) {
        //TODO: make sure markings are sorted. Now it's an assumption that user inputs markings in order.
        this.calendarMarkings.add(newMarking);
        Log.d(USER_TAG, "Added to user: " + newMarking.getCalendar().getTime());
        Calendar date = (Calendar) newMarking.getCalendar().clone();
        updateCycleLength(date);
        updateFlowLength(date);
        calculateAverages();
        calculateNextStartDay();
    }

    /**
     * Removes a selected calendar marking from the user and updates prediction values.
     * @param targetEvent The marking to be deleted
     */
    public void deleteCalendarMarking(EventDay targetEvent) {
        //TODO: implement removing markings to the past (now only latest marking can be deleted)
        this.calendarMarkings.remove(calendarMarkings.size() - 1);
        Calendar targetDate = targetEvent.getCalendar();
        Log.d(REMOVE_TAG, "Removed marking " + targetDate.get(Calendar.DAY_OF_MONTH));
        deleteFromCycleLengths(targetDate);
        deleteFromFlowLengths(targetDate);

        Log.d(REMOVE_TAG, "Now there are cycles:");
        for(TimePeriod tp : this.cycleLengths) {
            Log.d(REMOVE_TAG, tp.getFirstDay().get(Calendar.DATE) + ", " + tp.getLastDay().get(Calendar.DATE) + ", " + tp.getLength());
        }

        calculateAverages();
        calculateNextStartDay();
    }

    /**
     * Removes selected date from cycleLengths
     * @param targetDate Date to be deleted
     */
    private void deleteFromCycleLengths(Calendar targetDate) {
        //TODO: implement removing markings to the past (now only latest marking can be deleted)
        TimePeriod latestCycle = this.cycleLengths.get(cycleLengths.size() - 1);
        Calendar cycleFirstDay = latestCycle.getFirstDay();
        Calendar cycleLastDay = latestCycle.getLastDay();

        //1. removing first marking of the cycle
        if(isSameDate(cycleFirstDay, targetDate)) {
            // Cycle is longer than 1
            if(!isSameDate(cycleFirstDay, cycleLastDay)) {
                Calendar newCycleFirstDay = (Calendar) cycleFirstDay.clone();
                newCycleFirstDay.add(Calendar.DAY_OF_MONTH, 1);
                latestCycle.setFirstDay(newCycleFirstDay);
                latestCycle.addToLength(-1);
                Log.d(REMOVE_TAG, "Cycle moved to start at day " + newCycleFirstDay.get(Calendar.DAY_OF_MONTH));
            }
            // Cycle is only 1 days long
            else {
                this.cycleLengths.remove(cycleLengths.size() - 1);
                Log.d(REMOVE_TAG, "Removed cycle starting " + targetDate.get(Calendar.DAY_OF_MONTH));
            }
            return;
        }

        // 2. Removing latest marking of cycle
        if(isSameDate(cycleLastDay, targetDate)) {
            Calendar newCycleLastDay = (Calendar) cycleLastDay.clone();
            newCycleLastDay.add(Calendar.DAY_OF_MONTH, -1);
            latestCycle.setLastDay(newCycleLastDay);
            latestCycle.addToLength(-1);
            Log.d(REMOVE_TAG, "Cycle shortened to day " + newCycleLastDay.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Removes selected date from flowLengths
     * @param targetDate Date to be deleted
     */
    private void deleteFromFlowLengths(Calendar targetDate) {
        //TODO: implement removing markings to the past (now only latest marking can be deleted)
        TimePeriod latestFlow = this.flowLengths.get(flowLengths.size() - 1);
        Calendar flowFirstDay = latestFlow.getFirstDay();
        Calendar flowLastDay = latestFlow.getLastDay();

        if(isSameDate(flowFirstDay, targetDate)) {

            if(!isSameDate(flowFirstDay, flowLastDay)) {
                Calendar newFlowFirstDay = (Calendar) flowFirstDay.clone();
                newFlowFirstDay.add(Calendar.DAY_OF_MONTH, 1);
                latestFlow.setFirstDay(newFlowFirstDay);
                latestFlow.addToLength(-1);
                Log.d(REMOVE_TAG, "Flow moved to start at day " + newFlowFirstDay.get(Calendar.DAY_OF_MONTH));
            }

            else {
                this.flowLengths.remove(flowLengths.size() - 1);
                Log.d(REMOVE_TAG, "Removed flow starting " + targetDate.get(Calendar.DAY_OF_MONTH));
            }
            return;
        }

        if(isSameDate(flowLastDay, targetDate)) {
            Calendar newFlowLastDay = (Calendar) flowLastDay.clone();
            newFlowLastDay.add(Calendar.DAY_OF_MONTH, -1);
            latestFlow.setLastDay(newFlowLastDay);
            latestFlow.addToLength(-1);
            Log.d(REMOVE_TAG, "Flow shortened to day " + newFlowLastDay.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Compares Calendar objects based on year, month and day.
     * @param cal1 First Calendar to compare
     * @param cal2 Second Calendar to compare
     * @return boolean True if the dates match
     */
    public boolean isSameDate(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }

        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);

        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    /**
     * Calculates the difference of days between two dates.
     * @param day1 Earlier date
     * @param day2 Later date
     * @return int Number of days in between the dates
     */
    private int calculateDaysBetween(Calendar day1, Calendar day2) {
        long millisecondsDiff = Math.abs(day2.getTimeInMillis() - day1.getTimeInMillis());
        return (int) TimeUnit.MILLISECONDS.toDays(millisecondsDiff);
    }

    /**
     * Updates the cycle length if new marking is on the next day than the last saved date of the cycle.
     * @param newDay Date for new flow marking
     */
    private void updateCycleLength(Calendar newDay) {
        if(this.cycleLengths.isEmpty()) {
            cycleLengths.add(new TimePeriod(1, newDay, newDay));
            Log.d(CYCLE_TAG, "Added new cycle starting at " + newDay.getTime());
            return;
        }

        TimePeriod currentCycle = this.cycleLengths.get(cycleLengths.size() - 1);
        Calendar currentLastDay = currentCycle.getLastDay();
        Calendar dayAfterLast = (Calendar) currentLastDay.clone();
        dayAfterLast.add(Calendar.DAY_OF_MONTH, 1);
        //TODO: Check if newDay is before the currentLastDay/currentFirstDay
        if((!isSameDate(dayAfterLast, newDay)) && (!isSameDate(currentLastDay, newDay))) {
            Calendar latestStartDay = currentCycle.getFirstDay();
            int daysDiff = calculateDaysBetween(latestStartDay, newDay);
            currentCycle.setLength(daysDiff);
            cycleLengths.add(new TimePeriod(1, newDay, newDay));
            Log.d(CYCLE_TAG, "Added new cycle starting at " + newDay.getTime());
        }
        else if(isSameDate(newDay, dayAfterLast)) {
            currentCycle.setLastDay(newDay);
            currentCycle.addToLength(1);
            Log.d(CYCLE_TAG, "Extended current cycle by " + newDay.getTime());
        }

        Log.d(CYCLE_TAG, "Now there are cycles:");
        for(TimePeriod tp : this.cycleLengths) {
            Log.d(CYCLE_TAG, tp.getFirstDay().get(Calendar.DATE) + ", " + tp.getLastDay().get(Calendar.DATE) + ", " + tp.getLength());
        }
    }

    /**
     * Updates the flow length if new marking is on the next day than the last saved date of the flow.
     * @param newDay Date for new flow marking
     */
    private void updateFlowLength(Calendar newDay) {
        if(this.flowLengths.isEmpty()) {
            flowLengths.add(new TimePeriod(1, newDay, newDay));
            Log.d(FLOW_TAG, "Added new flow starting at " + newDay.getTime());
            return;
        }

        TimePeriod currentFlow = this.flowLengths.get(flowLengths.size() - 1);
        Calendar currentLastDay = currentFlow.getLastDay();
        Calendar dayAfterLast = (Calendar) currentLastDay.clone();
        dayAfterLast.add(Calendar.DAY_OF_MONTH, 1);
        //TODO: Check if newDay is before the currentLastDay/currentFirstDay
        if(isSameDate(dayAfterLast, newDay)) {
            currentFlow.addToLength(1);
            currentFlow.setLastDay(newDay);
            Log.d(FLOW_TAG, "Extended current flow by " + newDay.getTime());

        }
        else if(!isSameDate(currentLastDay, newDay)) {
            flowLengths.add(new TimePeriod(1, newDay, newDay));
            Log.d(FLOW_TAG, "Added new flow starting at " + newDay.getTime());
        }
    }

    /**
     * Calculates prediction for the next flow start day. If prediction cannot be calculated accurately
     * based on logged averages, it is based on general menstrual cycle length average (28 days).
     */
    private void calculateNextStartDay() {
        int flowCount = flowLengths.size();
        if(flowCount < 4) {
            if(!calendarMarkings.isEmpty()){
                Calendar firstFlowDay = (Calendar) this.flowLengths.get(flowCount - 1).getFirstDay().clone();
                firstFlowDay.add(Calendar.DAY_OF_MONTH, 28);
                this.nextFlowStartDay = new EventDay(firstFlowDay, R.drawable.circle_prediction);
                Log.d(PREDICTION_TAG, "Set default prediction date");
            }
            else {
                this.nextFlowStartDay = null;
            }
            return;
        }

        Calendar latestFlowFirstDay = (Calendar) flowLengths.get(flowCount - 1).getFirstDay().clone();
        latestFlowFirstDay.add(Calendar.DAY_OF_MONTH, cycleLengthAvg);
        this.nextFlowStartDay = new EventDay(latestFlowFirstDay, R.drawable.circle_prediction);
        Log.d(PREDICTION_TAG, "Set new prediction date " + latestFlowFirstDay.getTime());
    }

    /**
     * Calculates the values for cycle length and flow length based on the average of last three (full) cycles.
     */
    public void calculateAverages() {
        int cycleCount = this.cycleLengths.size();
        if(cycleCount < 4) {
            this.cycleLengthAvg = 0;
        }
        else {
            int sum = 0;
            for(int i = 0; i < cycleCount - 1; i++) {
                sum += this.cycleLengths.get(i).getLength();
            }
             this.cycleLengthAvg = sum/(cycleCount - 1);
            Log.d(AVERAGES_TAG, "Calculated new cycleAvg: " + this.cycleLengthAvg);
        }

        int flowCount = this.flowLengths.size();
        if(flowCount < 4) {
            this.flowLengthAvg = 0;
        }
        else {
            int sum = 0;
            for(int i = 0; i < flowCount - 1; i++) {
                sum += this.flowLengths.get(i).getLength();
            }
            this.flowLengthAvg = sum/(flowCount - 1);
            Log.d(AVERAGES_TAG, "Calculated new flowAvg: " + this.flowLengthAvg);
        }
    }
}
