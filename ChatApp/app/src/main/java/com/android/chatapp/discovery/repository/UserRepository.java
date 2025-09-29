package com.android.chatapp.discovery.repository;

import com.android.chatapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserRepository() {
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
                                        users.addAll(emailTask.getResult().toObjects(User.class));
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
}