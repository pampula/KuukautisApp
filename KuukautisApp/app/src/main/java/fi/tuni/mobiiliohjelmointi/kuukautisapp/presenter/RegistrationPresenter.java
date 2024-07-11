package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService.AuthServiceCallback;

public class RegistrationPresenter implements RegistrationContract.Presenter {
    private final RegistrationContract.View view;
    private final AuthService authService;

    public RegistrationPresenter(RegistrationContract.View view, AuthService authService) {
        this.view = view;
        this.authService = authService;
    }

    @Override
    public void registerUser(String email, String password) {
        authService.registerUser(email, password, new AuthServiceCallback<String>() {
            @Override
            public void onSuccess(String userId) {
                // TODO: add new user to db
                view.showRegistrationSuccess(userId);
            }

            @Override
            public void onFailure(Exception e) {
                view.showRegistrationError(e.getMessage());
            }
        });
    }
}
