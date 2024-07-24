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
    private Button loginButton;
    private ImageButton backButton;
    private Context context;

    private final String EMAIL_REGEX = ".+@.+"; // Contains "@", which has at least one character on each side

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.btn_back);

        context = this;
        presenter = new LoginPresenter(this);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

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
