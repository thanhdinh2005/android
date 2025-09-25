package com.android.chatapp.discovery.repository;

import android.util.Log;

import com.android.chatapp.model.User;
import com.android.chatapp.util.EmulatorConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscoveryRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public DiscoveryRepository(){
        // Delete when deploy on server
//        db.useEmulator("10.0.2.2", 8080);
//        EmulatorConnection.connect();
    }



    public void searchUsers(String query, Callback<List<User>> callback) {
        db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>(task.getResult().toObjects(User.class));

                        db.collection("users")
                                .whereGreaterThanOrEqualTo("email", query.toLowerCase())
                                .whereLessThanOrEqualTo("email", query.toLowerCase() + "\uf8ff")
                                .get()
                                .addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful() && emailTask.getResult() != null) {
                                        for (User user : emailTask.getResult().toObjects(User.class)) {
                                            if (!users.contains(user)) {
                                                users.add(user);
                                            }
                                        }
                                        callback.onSuccess(users);
                                    } else {
                                        callback.onError(emailTask.getException() != null
                                                ? emailTask.getException().getMessage()
                                                : "Email query failed");
                                    }
                                });
                    } else {
                        callback.onError(task.getException() != null
                                ? task.getException().getMessage()
                                : "Username query failed");
                    }
                });
    }

    public ListenerRegistration listenOnlineUsers(Callback<List<User>> callback) {
        return db.collection("users")
                .whereEqualTo("online", true)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }
                    if (querySnapshot != null) {
                        List<User> users = querySnapshot.toObjects(User.class);
                        callback.onSuccess(users);
                    } else {
                        callback.onError("No data available");
                    }
                });
    }

    public ListenerRegistration listenAllUsersExcept(String currentUid, Callback<List<User>> callback) {
        return db.collection("users")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }
                    if (querySnapshot != null) {
                        List<User> users = new ArrayList<>();
                        for (User user : querySnapshot.toObjects(User.class)) {
                            if (!user.getUid().equals(currentUid)) {
                                users.add(user);
                            }
                        }
                        callback.onSuccess(users);
                    } else {
                        callback.onError("No data available");
                    }
                });
    }
}
