package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleServiceImpl;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;

/**
 * Runs a calendar app for logging a menstrual cycle.
 * Saves user data when the app is stopped.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog infoDialog;
    private ImageButton infoBtn;
    private CycleService cycleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: retrieve user from data source or create new user
        //user = new UserData("Test user", "Tester", new Settings(), new CycleData());

        this.cycleService = new CycleServiceImpl(new CycleData());

        loadFragment(new CalendarFragment(cycleService), R.id.fragment_container_calendar);
        loadFragment(new CycleInfoFragment(cycleService), R.id.fragment_container_cycle_info);

        infoDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        infoBtn = findViewById(R.id.btn_info);
        infoBtn.setOnClickListener(this);

        deployWelcomeDialog();

    }

    /**
     * Load a fragment to the UI.
     * @param fragment fragment to be initialized
     * @param containerId container the fragment should be in
     */
    private void loadFragment(Fragment fragment, int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Handles UI button clicks.
     * @param view view that has the activated button
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_info) {
            deployInfoDialog();
        }
    }

    /**
     * Opens a welcome pop up window for app information. Is shown to the user only on the first time
     * the app is used.
     */
    private void deployWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_welcome)
                .setPositiveButton(getResources().getString(R.string.close), (dialog, id) -> dialog.dismiss());
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
                .setNeutralButton(getResources().getString(R.string.close), (dialog, id) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Saves user data when app is stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        /*
        DBService.saveUser;
         */
    }
}