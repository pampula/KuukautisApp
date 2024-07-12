package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.authservice;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.Settings;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBService;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice.DBServiceImpl;

public class AuthServiceFirebaseImpl implements AuthService {

    private final FirebaseAuth auth;
    private final DBService dbService;

    public AuthServiceFirebaseImpl() {
        this.auth = FirebaseAuth.getInstance();
        this.dbService = new DBServiceImpl();
    }

    @Override
    public void registerUser(String email, String password, AuthServiceCallback<String> callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            UserLogin.getInstance().loginUser(userId);
                            UserData newUser = new UserData(userId, email, new Settings(), new CycleData());
                            dbService.addUser(newUser, new DBService.DBServiceCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    callback.onSuccess(userId);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    callback.onFailure(e);
                                }
                            });
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
                            UserLogin.getInstance().loginUser(userId);
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
        UserLogin.getInstance().logoutUser();
        callback.onSuccess(true);
    }

    @Override
    public String getCurrentUserId() {
        return UserLogin.getInstance().getUserId();
    }
}
