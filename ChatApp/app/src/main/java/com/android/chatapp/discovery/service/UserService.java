package com.android.chatapp.discovery.service;

import android.util.Log;

import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getUserById(String uid, Callback<User> callback) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    User user = snapshot.toObject(User.class);
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void loadCurrentUser(String uid, Callback<User> callback) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User currentUser = doc.toObject(User.class);
                        callback.onSuccess(currentUser);
                    } else {
                        callback.onError("User not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void searchUsers(String query, Callback<List<User>> callback) {
        if (query.isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }

        db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        callback.onError(task.getException() != null
                                ? task.getException().getMessage()
                                : "Username query failed");
                        return;
                    }
                    List<User> users = new ArrayList<>(task.getResult().toObjects(User.class));

                    db.collection("users")
                            .whereGreaterThanOrEqualTo("email", query.toLowerCase())
                            .whereLessThanOrEqualTo("email", query.toLowerCase() + "\uf8ff")
                            .get()
                            .addOnCompleteListener(emailTask -> {
                                List<User> emailUsers = new ArrayList<>();
                                if (emailTask.isSuccessful() && emailTask.getResult() != null) {
                                    emailUsers = emailTask.getResult().toObjects(User.class);
                                } else {
                                    Log.w("UserService", "Email query failed, using only username results: " +
                                            (emailTask.getException() != null ? emailTask.getException().getMessage() : "Unknown"));
                                }

                                Map<String, User> uniqueMap = new HashMap<>();
                                for (User user : users) {
                                    if (user.getUid() != null) {
                                        uniqueMap.put(user.getUid(), user);
                                    }
                                }
                                for (User user : emailUsers) {
                                    if (user.getUid() != null) {
                                        uniqueMap.put(user.getUid(), user);
                                    }
                                }
                                List<User> allUsers = new ArrayList<>(uniqueMap.values());

                                callback.onSuccess(allUsers);
                            });
                });
    }
}