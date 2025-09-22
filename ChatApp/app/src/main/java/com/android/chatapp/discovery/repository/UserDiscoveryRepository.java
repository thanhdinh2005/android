package com.android.chatapp.discovery.repository;

import android.util.Log;

import com.android.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDiscoveryRepository {
    private FirebaseFirestore db;

    public UserDiscoveryRepository(){
        db = FirebaseFirestore.getInstance();

        // Delete when deploy on server
        db.useEmulator("10.0.2.2", 8080);
        connectToEmulator("chi@gmail.com", "123456");
    }

    public interface SearchCallback {
        void onSuccess(List<User> users);
        void onFailure(Exception e);
    }

    public void searchUsers(String query, SearchCallback callback) {
        db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        List<User> users = new ArrayList<>(snapshot.toObjects(User.class));

                        db.collection("users")
                                .whereGreaterThanOrEqualTo("email", query.toLowerCase())
                                .whereLessThanOrEqualTo("email", query.toLowerCase() + "\uf8ff")
                                .get()
                                .addOnCompleteListener(emailTask -> {
                                    if (emailTask.isSuccessful()) {
                                        for (User user : emailTask.getResult().toObjects(User.class)) {
                                            if (!users.contains(user)) {
                                                users.add(user);
                                            }
                                        }
                                        callback.onSuccess(users);
                                    } else {
                                        callback.onFailure(emailTask.getException());
                                    }
                                });
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    private void connectToEmulator(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("UserDiscoveryRepository", "Login success");
                } else {
                    Log.e("UserDiscoveryRepository", "Login failed: " + Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        } else {
            currentUser.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("UserDiscoveryRepository", "Token refresh success");
                } else {
                    Log.e("UserDiscoveryRepository", "Token refresh failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    mAuth.signOut();
                }
            });
        }
    }
}
