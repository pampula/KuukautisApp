package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.CycleInfoContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.CycleInfoPresenter;

public class CycleInfoFragment extends Fragment implements CycleInfoContract.View {

    private TextView cycleLengthAvgText;
    private TextView menstruationLengthAvgText;
    private TextView nextMenstruationStartDayText;
    private final CycleInfoContract.Presenter presenter;

    public CycleInfoFragment(CycleService cycleService) {
        this.presenter = new CycleInfoPresenter(this, cycleService);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cycle_info, container, false);

        cycleLengthAvgText = view.findViewById(R.id.cycle_length_avg_value);
        menstruationLengthAvgText = view.findViewById(R.id.flow_length_avg_value);
        nextMenstruationStartDayText = view.findViewById(R.id.next_flow_value);

        presenter.loadCycleInfo();

        return view;
    }

    @Override
    public void showCycleInfo(int cycleLengthAvg, int menstruationLengthAvg, Date nextMenstruationStartDay) {
        String cycleText = String.format(Locale.getDefault(), "%d %s", cycleLengthAvg, getString(R.string.days_duration));
        String menstruationText = String.format(Locale.getDefault(), "%d %s",
                menstruationLengthAvg, getString(R.string.days_duration));

        cycleLengthAvgText.setText(cycleText);
        menstruationLengthAvgText.setText(menstruationText);

        if (nextMenstruationStartDay == null) {
            nextMenstruationStartDayText.setText(R.string.placeholder_value);
        }
        else {
            nextMenstruationStartDayText.setText(new SimpleDateFormat("dd.MM.yyyy",
                    Locale.getDefault()).format(nextMenstruationStartDay));
        }
    }
}
