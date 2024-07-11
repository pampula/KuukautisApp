package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AuthServiceFirebaseImpl implements AuthService {
    private final FirebaseAuth auth;

    public AuthServiceFirebaseImpl() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void registerUser(String email, String password, AuthServiceCallback<String> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            callback.onSuccess(user != null ? user.getUid() : null);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    @Override
    public void loginUser(String email, String password, AuthServiceCallback<String> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            callback.onSuccess(user != null ? user.getUid() : null);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    @Override
    public void logoutUser(AuthServiceCallback<Boolean> callback) {
        auth.signOut();
        callback.onSuccess(true);
    }
}
