package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.RegistrationContract;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.presenter.RegistrationPresenter;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice.AuthServiceFirebaseImpl;

public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {
    private RegistrationContract.Presenter presenter;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        presenter = new RegistrationPresenter(this, new AuthServiceFirebaseImpl());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: check valid password & email
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                presenter.registerUser(email, password);
            }
        });
    }

    @Override
    public void showRegistrationSuccess(String userId) {
        Toast.makeText(this, "Registration successful! User ID: " + userId, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showRegistrationError(String error) {
        Toast.makeText(this, "Registration failed: " + error, Toast.LENGTH_LONG).show();
    }
}
