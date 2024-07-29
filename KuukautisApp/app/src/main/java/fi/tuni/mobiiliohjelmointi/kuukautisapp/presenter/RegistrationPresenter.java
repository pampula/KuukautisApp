package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

import android.util.Log;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService.AuthServiceCallback;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthServiceFirebaseImpl;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.Settings;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBServiceImpl;

/**
 * Presenter that handles user registration logic.
 */
public class RegistrationPresenter implements RegistrationContract.Presenter {

    private final RegistrationContract.View view;
    private final AuthService authService;
    private final DBService dbService;

    public RegistrationPresenter(RegistrationContract.View view) {
        this.view = view;
        this.authService = new AuthServiceFirebaseImpl();
        this.dbService = new DBServiceImpl();
    }

    @Override
    public void registerUser(String email, String password) {
        authService.registerUser(email, password, new AuthServiceCallback<String>() {
            @Override
            public void onSuccess(String userId) {
                UserData newUser = new UserData(userId, email, new Settings(), new CycleData());
                dbService.addUser(newUser, new DBService.DBServiceCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        view.showRegistrationError(e.getMessage());
                    }
                });
                view.showRegistrationSuccess(userId);
            }

            @Override
            public void onFailure(Exception e) {
                view.showRegistrationError(e.getMessage());
            }
        });
    }
}
