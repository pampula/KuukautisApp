package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthService.AuthServiceCallback;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final AuthService authService;

    public LoginPresenter(LoginContract.View view, AuthService authService) {
        this.view = view;
        this.authService = authService;
    }

    @Override
    public void loginUser(String email, String password) {
        authService.loginUser(email, password, new AuthServiceCallback<String>() {
            @Override
            public void onSuccess(String userId) {
                view.showLoginSuccess(userId);
            }

            @Override
            public void onFailure(Exception e) {
                view.showLoginError(e.getMessage());
            }
        });
    }
}
