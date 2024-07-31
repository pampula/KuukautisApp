package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthServiceFirebaseImpl;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.cycleservice.CycleServiceImpl;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBServiceImpl;

/**
 * Runs a calendar app for logging a menstrual cycle.
 * Saves user data when the app is stopped.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog infoDialog;
    private ImageButton infoBtn;
    private ImageButton logoutBtn;

    private CycleService cycleService;
    private DBService dbService;
    private AuthService authService;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbService = new DBServiceImpl();
        authService = new AuthServiceFirebaseImpl();
        cycleService = new CycleServiceImpl(new CycleData());

        userId = authService.getCurrentUserId();
        if (userId != null && !userId.isEmpty()) {
            Log.d("MAIN", "Loading current user, id: " + userId);
            loadUserData(userId);
        }
        else {
            Log.d("MAIN", "userId is null or empty, redirecting to start");
            redirectToStartScreen();
        }

        infoDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        infoBtn = findViewById(R.id.btn_info);
        infoBtn.setOnClickListener(this);
        logoutBtn = findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(this);
    }

    /**
     * Fetches and loads the user data from DBService.
     * @param userId user to load
     */
    private void loadUserData(String userId) {
        dbService.loadUser(userId, new DBService.DBServiceCallback<UserData>() {
            @Override
            public void onSuccess(UserData userData) {
                cycleService = new CycleServiceImpl(userData.getCycleData());
                loadFragment(new CalendarFragment(cycleService), R.id.fragment_container_calendar);
                loadFragment(new CycleInfoFragment(cycleService), R.id.fragment_container_cycle_info);

                if (cycleService.getMenstrualDays().isEmpty()) {
                    deployWelcomeDialog();
                }
            }

            @Override
            public void onFailure(Exception e) {
                showError(getString(R.string.data_load_error));
                cycleService = new CycleServiceImpl(new CycleData());
                loadFragment(new CalendarFragment(cycleService), R.id.fragment_container_calendar);
                loadFragment(new CycleInfoFragment(cycleService), R.id.fragment_container_cycle_info);
            }
        });
    }

    /**
     * Starts the app's start screen.
     */
    private void redirectToStartScreen() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    /**
     * Shows error message Toast with given text.
     * @param message error message
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
     * @param view activated button
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_info) {
            deployInfoDialog();
        }
        else if (view.getId() == R.id.btn_logout) {
            saveAndLogout();
            redirectToStartScreen();
        }
    }

    /**
     * Opens a welcome pop up window for app information.
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
     * Saves the user data and logs the user out.
     */
    private void saveAndLogout() {
        dbService.saveUser(new DBService.DBServiceCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.i("SAVING", "User saved");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("SAVING", String.valueOf(e));
            }
        });

        authService.logoutUser(new AuthService.AuthServiceCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.i("LOGOUT", "User logout");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("LOGOUT", String.valueOf(e));
            }
        });
    }

    /**
     * Saves user data when app is stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        saveAndLogout();
    }
}