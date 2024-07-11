package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

/**
 * Contract interface for separating the login view from the presenter.
 */
public interface LoginContract {
    interface View {
        /**
         * Shows login success in the UI.
         * @param userId user's id
         */
        void showLoginSuccess(String userId);

        /**
         * Shows login error in the UI.
         * @param error error message
         */
        void showLoginError(String error);
    }

    interface Presenter {
        /**
         * Logs in a user.
         * @param email user's email
         * @param password user's password
         */
        void loginUser(String email, String password);
    }
}
