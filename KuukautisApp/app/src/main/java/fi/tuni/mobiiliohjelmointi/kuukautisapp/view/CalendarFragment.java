package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.CalendarContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.CalendarPresenter;

/**
 * Fragment view for showing an interactable calendar UI.
 * Uses the Applandeo Material-Calendar-View library.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener, CalendarContract.View {

    private Context context;
    private CalendarView calendarView;
    private Button addEntryBtn;
    private final CalendarContract.Presenter presenter;

    public CalendarFragment(CycleService cycleService) {
        this.presenter = new CalendarPresenter(this, cycleService);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,
                container, false);

        context = getActivity();
        calendarView = view.findViewById(R.id.calendar_view);
        addEntryBtn = view.findViewById(R.id.btn_new_entry);
        addEntryBtn.setOnClickListener(this);
        setOnDayClickListener();
        presenter.loadCalendarEvents();

        return view;
    }

    @Override
    public void showCalendarEvents(ArrayList<Date> menstruationDays, ArrayList<Date> predictions) {
        List<CalendarDay> calendarDays = new ArrayList<>();

        for (Date date : menstruationDays) {
            Calendar day = convertDateToCalendar(date);
            CalendarDay newMarking = new CalendarDay(day);
            newMarking.setImageResource(R.drawable.circle_flow);
            calendarDays.add(newMarking);
        }

        for (Date date : predictions) {
            Calendar day = convertDateToCalendar(date);
            CalendarDay newMarking = new CalendarDay(day);
            newMarking.setImageResource(R.drawable.circle_prediction);
            calendarDays.add(newMarking);
        }

        calendarView.setCalendarDays(calendarDays);
    }

    private Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void setOnDayClickListener() {
        calendarView.setOnCalendarDayClickListener(calendarDay -> {
            Date day = calendarDay.getCalendar().getTime();

            if(!presenter.menstrualDayExists(day)) {
                return;
            }

            Calendar selectedDay = calendarDay.getCalendar();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @SuppressLint("DefaultLocale") String message = String.format("%s %d.%d.%d?",
                    getResources().getString(R.string.delete_confirmation),
                    selectedDay.get(Calendar.DAY_OF_MONTH),
                    selectedDay.get(Calendar.MONTH) + 1,
                    selectedDay.get(Calendar.YEAR));
            builder.setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.delete), (dialog, id) -> {
                        presenter.deleteMenstruationDay(day);
                        presenter.loadCalendarEvents();
                        dialog.dismiss();
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, id) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_entry) {
            addNewEntry();
        }
    }

    private void addNewEntry() {
        OnSelectDateListener listener = calendars -> {
            Calendar newDate = calendars.get(0);
            presenter.addMenstruationDay(newDate.getTime());
        };

        new DatePickerBuilder(getActivity(), listener)
                .pickerType(CalendarView.ONE_DAY_PICKER)
                .headerColor(R.color.brown)
                .headerLabelColor(R.color.white)
                .abbreviationsBarColor(R.color.beige)
                .abbreviationsLabelsColor(R.color.black)
                .pagesColor(R.color.light_beige)
                .daysLabelsColor(R.color.black)
                .selectionColor(R.color.green)
                .todayColor(R.color.green)
                .dialogButtonsColor(R.color.brown)
                .build()
                .show();
    }
}
