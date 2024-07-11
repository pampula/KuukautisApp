package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.LoginContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.LoginPresenter;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthServiceFirebaseImpl;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginContract.Presenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        presenter = new LoginPresenter(this, new AuthServiceFirebaseImpl());

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            presenter.loginUser(email, password);
        });
    }

    @Override
    public void showLoginSuccess(String userId) {
        Toast.makeText(this, "Login successful! User ID: " + userId, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_LONG).show();
    }
}
