package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice;

import java.util.ArrayList;
import java.util.Date;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.MenstrualDayObserver;

public class CycleServiceImpl implements CycleService{

    private CycleData cycleData;
    private ArrayList<MenstrualDayObserver> observers;

    public CycleServiceImpl(CycleData cycleData) {
        this.cycleData = cycleData;
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(MenstrualDayObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyObservers() {
        for (MenstrualDayObserver observer : observers) {
            observer.onMenstrualDaysUpdated();
        }
    }

    @Override
    public ArrayList<Date> getMenstrualDays() {
        return this.cycleData.getMenstrualDays();
    }

    @Override
    public ArrayList<Date> getPredictionDays() {
        return this.cycleData.getPredictionDays();
    }

    @Override
    public int getCycleLengthAvg() {
        return this.cycleData.getCycleLengthAvg();
    }

    @Override
    public int getMenstruationLengthAvg() {
        return this.cycleData.getMenstruationLengthAvg();
    }

    @Override
    public void addMenstrualDay(Date menstrualDay) {
        this.cycleData.addMenstrualDay(menstrualDay);
        notifyObservers();
    }

    @Override
    public void removeMenstrualDay(Date menstrualDay) {
        this.cycleData.removeMenstrualDay(menstrualDay);
        notifyObservers();
    }

    @Override
    public boolean menstrualDayExists(Date menstrualDay) {
        return this.cycleData.menstrualDayExists(menstrualDay);
    }

}
