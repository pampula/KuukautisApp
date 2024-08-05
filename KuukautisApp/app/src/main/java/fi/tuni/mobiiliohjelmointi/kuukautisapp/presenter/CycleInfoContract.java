package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;


import java.util.Date;

/**
 * Contract interface for separating the CycleInfo view from the presenter.
 */
public interface CycleInfoContract {
    interface View {
        /**
         * Shows the info in the UI.
         * @param cycleLengthAvg average length in days of a menstrual cycle
         * @param menstruationLengthAvg average length in days of a menstruation
         * @param nextMenstruationStartDay predicted day for the start of the next menstruation
         */
        void showCycleInfo(int cycleLengthAvg, int menstruationLengthAvg, Date nextMenstruationStartDay);
    }

    interface Presenter {
        /**
         * Loads cycle information to the view.
         */
        void loadCycleInfo();
    }
}
