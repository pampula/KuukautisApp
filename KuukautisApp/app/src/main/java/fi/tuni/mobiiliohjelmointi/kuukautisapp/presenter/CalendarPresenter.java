package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;


import java.util.ArrayList;
import java.util.Date;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;

public class CalendarPresenter implements CalendarContract.Presenter {

    private final CalendarContract.View view;
    private final CycleService cycleService;

    public CalendarPresenter(CalendarContract.View view, CycleService cycleService) {
        this.view = view;
        this.cycleService = cycleService;
    }

    @Override
    public void loadCalendarEvents() {
        ArrayList<Date> menstrualDays = cycleService.getMenstrualDays();
        ArrayList<Date> predictionDays = cycleService.getPredictionDays();
        view.showCalendarEvents(menstrualDays, predictionDays);
    }

    @Override
    public boolean menstrualDayExists(Date menstruationDay) {
        return cycleService.menstrualDayExists(menstruationDay);
    }

    @Override
    public void addMenstruationDay(Date menstruationDay) {
        cycleService.addMenstrualDay(menstruationDay);
        loadCalendarEvents();
    }

    @Override
    public void deleteMenstruationDay(Date menstruationDay) {
        cycleService.removeMenstrualDay(menstruationDay);
        loadCalendarEvents();
    }
}
