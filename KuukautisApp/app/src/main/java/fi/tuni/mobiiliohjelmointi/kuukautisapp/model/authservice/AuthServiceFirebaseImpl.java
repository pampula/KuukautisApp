package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthServiceFirebaseImpl implements AuthService {

    private final FirebaseAuth auth;

    public AuthServiceFirebaseImpl() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void registerUser(String email, String password, AuthServiceCallback<String> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        Log.d("AUTH", "User creation success, current user: " + firebaseUser);

                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            UserLoginSingleton.getInstance().loginUser(userId);
                            Log.d("AUTH", "User status set as logged in!");
                            callback.onSuccess(userId);
                        }
                        else {
                            callback.onFailure(new Exception("User creation failed, user is null"));
                        }

                    }
                    else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void loginUser(String email, String password, AuthServiceCallback<String> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            UserLoginSingleton.getInstance().loginUser(userId);
                            callback.onSuccess(userId);
                        }
                        else {
                            callback.onFailure(new Exception("Login failed, user is null"));
                        }
                    }
                    else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void logoutUser(AuthServiceCallback<Boolean> callback) {
        auth.signOut();
        UserLoginSingleton.getInstance().logoutUser();
        callback.onSuccess(true);
    }

    @Override
    public String getCurrentUserId() {
        return UserLoginSingleton.getInstance().getUserId();
    }
}
