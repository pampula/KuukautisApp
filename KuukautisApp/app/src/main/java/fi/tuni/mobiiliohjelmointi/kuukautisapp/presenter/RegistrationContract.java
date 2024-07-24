package fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter;

/**
 * Contract interface for separating the registration view from the presenter.
 */
public interface RegistrationContract {
    interface View {
        /**
         * Shows registration success in the UI.
         * @param userId user's id
         */
        void showRegistrationSuccess(String userId);

        /**
         * Shows registration error in the UI.
         * @param error error message
         */
        void showRegistrationError(String error);
    }

    interface Presenter {
        /**
         * Register's a new user.
         * @param email user's email
         * @param password user's password
         */
        void registerUser(String email, String password);
    }
}