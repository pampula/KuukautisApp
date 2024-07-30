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

/**
 * View for handling the user registration process.
 */
public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

    private RegistrationContract.Presenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button registerButton;
    private ImageButton backButton;
    private Context context;

    /*
        Email validation rules:
        Local Part:
        - Can contain letters (a-z, A-Z), digits (0-9), and certain special characters (., _, %, +, -).
        - Can't start or end with a dot (.).
        - Consecutive dots (..) are not allowed.

        @ Symbol:
        - Must contain exactly one @ symbol separating the local part and the domain part.

        Domain Part:
        - Can contain letters, digits, hyphens (-), and dots (.).
        - Must have at least one dot, separating the domain name and the top-level domain.
        - The portion after the last dot must be at least two letters long (e.g., .com, .org, .fi).
     */
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /*
        Password validation rules:
        - Is at least eight characters long of any characters

     */
    private final String PASSWORD_REGEX = ".{8,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.btn_back);

        presenter = new RegistrationPresenter(this);
        context = this;

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmedPassword = passwordConfirmEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
                Toast.makeText(context, R.string.missing_fields, Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(context, R.string.invalid_password
                        , Toast.LENGTH_LONG).show();
                return;
            }

            if (!password.equals(confirmedPassword)) {
                Toast.makeText(context, R.string.password_not_match
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
