package fi.tuni.mobiiliohjelmointi.kuukautisapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.R;

/**
 * View for resetting the user's password.
 */
public class PasswordResetActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ImageButton backButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        emailEditText = findViewById(R.id.emailEditText);
        backButton = findViewById(R.id.btn_back);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        });

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.missing_fields, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordResetActivity.this, R.string.email_password_reset,
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, StartActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(PasswordResetActivity.this, R.string.email_password_reset_fail,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
