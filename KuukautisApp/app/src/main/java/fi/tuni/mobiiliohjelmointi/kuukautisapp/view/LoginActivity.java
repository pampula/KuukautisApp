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
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.LoginContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.LoginPresenter;

/**
 * View for handling the user login process.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginContract.Presenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button resetPasswornButton;
    private Button loginButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        resetPasswornButton = findViewById(R.id.forgotPasswordButton);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.btn_back);

        context = this;
        presenter = new LoginPresenter(this);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        resetPasswornButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PasswordResetActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, R.string.missing_fields, Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_LONG).show();
                return;
            }

            presenter.loginUser(email, password);
        });
    }

    @Override
    public void showLoginSuccess(String userId) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(context, R.string.login_unsuccessful + error, Toast.LENGTH_LONG).show();
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
}
