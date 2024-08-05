package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

import java.util.ArrayList;
import java.util.Date;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.MenstrualDayObserver;

public class CycleInfoPresenter implements CycleInfoContract.Presenter, MenstrualDayObserver {

    private final CycleInfoContract.View view;
    private final CycleService cycleService;

    public CycleInfoPresenter(CycleInfoContract.View view, CycleService cycleService) {
        this.view = view;
        this.cycleService = cycleService;
        this.cycleService.registerObserver(this);
    }

    @Override
    public void loadCycleInfo() {
        int cycleLengthAvg = cycleService.getCycleLengthAvg();
        int menstruationLengthAvg = cycleService.getMenstruationLengthAvg();
        Date nextFlowStartDay;
        ArrayList<Date> predictions = cycleService.getPredictionDays();
        if (predictions.isEmpty()) {
            nextFlowStartDay = null;
        }
        else {
            nextFlowStartDay = predictions.get(0);
        }
        view.showCycleInfo(cycleLengthAvg, menstruationLengthAvg, nextFlowStartDay);
    }

    @Override
    public void onMenstrualDaysUpdated() {
        loadCycleInfo();
    }
}
