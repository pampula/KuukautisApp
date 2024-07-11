package fi.tuni.mobiiliohjelmointi.kuukautisapp.model.dbservice;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.CycleData;
import fi.tuni.mobiiliohjelmointi.kuukautisapp.model.datamodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

public class DataSourceFirestoreImpl implements DataSource {
    private final FirebaseFirestore db;
    private final String USERS_COLLECTION = "users";

    public DataSourceFirestoreImpl() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void userExists(String userId, DBService.DBServiceCallback<Boolean> callback) {
        DocumentReference docRef = db.collection(USERS_COLLECTION).document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess(task.getResult().exists());
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    @Override
    public void saveUserData(UserData userData, DBService.DBServiceCallback<Boolean> callback) {
        DocumentReference docRef = db.collection(USERS_COLLECTION).document(userData.getUserId());
        docRef.set(userData)
                .addOnSuccessListener(aVoid -> callback.onSuccess(true))
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void getUserData(String userId, DBService.DBServiceCallback<UserData> callback) {
        DocumentReference docRef = db.collection(USERS_COLLECTION).document(userId);
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserData userData = documentSnapshot.toObject(UserData.class);
                    callback.onSuccess(userData);
                })
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void deleteUser(String userId, DBService.DBServiceCallback<Boolean> callback) {
        DocumentReference docRef = db.collection(USERS_COLLECTION).document(userId);
        docRef.delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(true))
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void truncateUser(String userId, DBService.DBServiceCallback<Boolean> callback) {
        getUserData(userId, new DBService.DBServiceCallback<UserData>() {
            @Override
            public void onSuccess(UserData userData) {
                userData.setCycleData(new CycleData()); // Reset the cycle data
                saveUserData(userData, callback);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}
