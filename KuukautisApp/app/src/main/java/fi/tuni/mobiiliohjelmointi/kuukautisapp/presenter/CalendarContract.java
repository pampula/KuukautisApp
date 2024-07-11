package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;


import java.util.ArrayList;
import java.util.Date;

/**
 * Contract interface for separating the Calendar view from the presenter.
 */
public interface CalendarContract {
    interface View {
        /**
         * Shows the markings in the calendar UI.
         * @param menstruationDays menstruation days
         * @param predictions predictions for the next menstruations
         */
        void showCalendarEvents(ArrayList<Date> menstruationDays, ArrayList<Date> predictions);
    }

    interface Presenter {
        /**
         * Loads the calendar events to the view.
         */
        void loadCalendarEvents();

        /**
         * Checks if the specific menstruation day exists already.
         * @param menstruationDay day of menstruation
         * @return true if day exists
         */
        boolean menstrualDayExists(Date menstruationDay);

        /**
         * Adds a new menstruation day.
         * @param menstruationDay day of menstruation to add
         */
        void addMenstruationDay(Date menstruationDay);

        /**
         * Deletes a menstruation day.
         * @param menstruationDay day of menstruation to remove
         */
        void deleteMenstruationDay(Date menstruationDay);
    }
}
