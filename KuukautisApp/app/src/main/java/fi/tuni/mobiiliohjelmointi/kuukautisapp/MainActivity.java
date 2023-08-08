package fi.tuni.mobiiliohjelmointi.kuukautisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Runs a calendar app for logging a menstrual cycle.
 * Saves user data to internal storage when the app is stopped.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String MAIN_TAG = "MAIN_ACTIVITY";

    private Context context;
    private CalendarView calendarView;
    private Dialog infoDialogue;
    private ImageButton infoBtn;
    private Button toNewEntryBtn;
    private TextView nextFlowText;
    private TextView cycleLengthAvgText;
    private TextView flowLengthAvgText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        infoDialogue = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        infoBtn = findViewById(R.id.btn_info);
        infoBtn.setOnClickListener(this);
        toNewEntryBtn = findViewById(R.id.btn_new_entry);
        toNewEntryBtn.setOnClickListener(this);
        calendarView = findViewById(R.id.calendar_view);
        setOnDayClickListener();
        nextFlowText = findViewById(R.id.next_flow_value);
        cycleLengthAvgText = findViewById(R.id.cycle_length_avg_value);
        flowLengthAvgText = findViewById(R.id.flow_length_avg_value);

        //InternalStorageManager.clearData(context); //FOR TESTING

        if (InternalStorageManager.userExists(context)) {
            Log.i(MAIN_TAG, "Fetching user data");
            user = InternalStorageManager.loadUser(context);
        }
        else {
            Log.i(MAIN_TAG, "Creating a new user");
            user = new User( new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                    null, 0, 0);
            deployWelcomeDialog();
        }

        inflateCalendar();

    }

    /**
     * Adds user's menstrual flow day markings and prediction marking to the calendar view.
     */
    private void inflateCalendar() {
        Log.i(MAIN_TAG, "Updating calendar data");
        List<EventDay> calendarMarkings = new ArrayList<>(user.getCalendarMarkings());
        EventDay nextFlowStartDay = user.getNextFlowStartDay();

        if(!(nextFlowStartDay == null)) {
            calendarMarkings.add(nextFlowStartDay);
            String nextFlowDay = String.valueOf(nextFlowStartDay.getCalendar().get(Calendar.DAY_OF_MONTH));
            String nextFlowMonth = String.valueOf(nextFlowStartDay.getCalendar().get(Calendar.MONTH) + 1);
            String nextFlowYear = String.valueOf(nextFlowStartDay.getCalendar().get(Calendar.YEAR));
            nextFlowText.setText(String.join(".", nextFlowDay, nextFlowMonth, nextFlowYear));

        }
        else {
            nextFlowText.setText(getString(R.string.placeholder_value));
        }

        calendarView.setEvents(calendarMarkings);
        Log.d(MAIN_TAG, "Set " + calendarMarkings.size() + " markings.");
        String cycleText = String.format("%s %s", user.getCycleLengthAvg(), getString(R.string.days_duration));
        String flowText = String.format("%s %s", user.getFlowLengthAvg(), getString(R.string.days_duration));
        cycleLengthAvgText.setText(cycleText);
        flowLengthAvgText.setText(flowText);
    }

    /**
     * Handles UI button clicks.
     * @param view View that has the activated button
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Open information about the app
            case R.id.btn_info:
                deployInfoDialog();
                break;

            // Open activity for adding new entry
            case R.id.btn_new_entry:
                addNewEntry();
                break;
        }
    }

    /**
     * Handles removing calendar markings by clicking them.
     */
    private void setOnDayClickListener() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if(!user.hasMarking(eventDay)) {
                    return;
                }

                Calendar selectedDay = eventDay.getCalendar();
                //TODO: implement removing markings to the past (now only latest marking can be deleted)
                List<EventDay> calendarMarkings = user.getCalendarMarkings();
                if(!calendarMarkings.isEmpty()) {
                    Calendar currentLastMarking = calendarMarkings.get(calendarMarkings.size() - 1).getCalendar();
                    if(!user.isSameDate(currentLastMarking, selectedDay)) {
                        Toast.makeText(context, getString(R.string.delete_from_past_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                @SuppressLint("DefaultLocale") String message = String.format("%s %d.%d.%d?",
                        getResources().getString(R.string.delete_confirmation),
                        selectedDay.get(Calendar.DAY_OF_MONTH),
                        selectedDay.get(Calendar.MONTH) + 1,
                        selectedDay.get(Calendar.YEAR));
                builder.setMessage(message)
                        .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                user.deleteCalendarMarking(eventDay);
                                inflateCalendar();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
    }

    /**
     * Opens a welcome pop up window for app information. Is shown to the user only on the first time
     * the app is used.
     */
    private void deployWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_welcome)
                .setPositiveButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /**
     * Opens a pop up window for app information.
     */
    private void deployInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String infoMessage = String.format("%s\n\n%s\n\n%s",
                getResources().getString(R.string.info_text_markings),
                getResources().getString(R.string.info_text_deleting),
                getResources().getString(R.string.info_text_averages)
                );
        builder.setMessage(infoMessage)
                .setNeutralButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /**
     * Adds new period flow entry to the calendar and saves it to the user.
     */
    private void addNewEntry() {
        OnSelectDateListener listener = new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendars) {
                Calendar newDate = calendars.get(0);
                EventDay newMarking = new EventDay(newDate, R.drawable.circle_flow);

                //TODO: implement adding new markings to the past (now it can be only in the future)
                List<EventDay> calendarMarkings = user.getCalendarMarkings();
                if(!calendarMarkings.isEmpty()) {
                    Calendar currentLastMarking = calendarMarkings.get(calendarMarkings.size() - 1).getCalendar();
                    if(currentLastMarking.after(newDate) || user.isSameDate(currentLastMarking, newDate)) {
                        Toast.makeText(context, getString(R.string.add_to_past_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                user.addCalendarMarking(newMarking);
                inflateCalendar();
            }
        };
        DatePickerBuilder builder = new DatePickerBuilder(this, listener)
                .setPickerType(CalendarView.ONE_DAY_PICKER)
                .setHeaderColor(R.color.brown)
                .setHeaderLabelColor(R.color.white)
                .setAbbreviationsBarColor(R.color.beige)
                .setAbbreviationsLabelsColor(R.color.black)
                .setPagesColor(R.color.light_beige)
                .setDaysLabelsColor(R.color.black)
                .setSelectionColor(R.color.green)
                .setTodayColor(R.color.green)
                .setDialogButtonsColor(R.color.brown);

        DatePicker datePicker = builder.build();
        datePicker.show();
    }

    /**
     * Saves user data to internal storage when app is stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MAIN_TAG, "Saving user data");
        InternalStorageManager.saveUser(context, user);
    }
}