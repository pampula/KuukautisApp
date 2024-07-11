package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.RegistrationContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.RegistrationPresenter;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthServiceFirebaseImpl;

/**
 * View for handling the user registration process.
 */
public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

    private RegistrationContract.Presenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ImageButton backButton;
    private Context context;

    private final String EMAIL_REGEX = ".+@.+"; // Contains "@", which has at least one character on each side
    private final String PASSWORD_REGEX = ".{8,}"; // Is at least eight characters long of any characters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.btn_back);

        presenter = new RegistrationPresenter(this, new AuthServiceFirebaseImpl());
        context = this;

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(context, R.string.missing_fields, Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_LONG).show();
                return;
            }

            // TODO: define better password requirements
            if (!isValidPassword(password)) {
                Toast.makeText(context, R.string.invalid_password
                        , Toast.LENGTH_LONG).show();
                return;
            }

            presenter.registerUser(email, password);
        });
    }

    @Override
    public void showRegistrationSuccess(String userId) {
        Toast.makeText(context, R.string.registration_successful, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showRegistrationError(String error) {
        Toast.makeText(context, R.string.registeration_unsuccesful + " " + error, Toast.LENGTH_LONG).show();
    }

    /**
     * Checks if the given email is valid and has a "@" sign.
     * @param email user's email
     * @return true if email is valid
     */
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Checks if the given password is valid and has at least eight of any characters.
     * @param password user's password
     * @return true if password is strong enough
     */
    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
