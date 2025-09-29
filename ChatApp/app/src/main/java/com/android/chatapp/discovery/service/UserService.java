package com.android.chatapp.discovery.service;

import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

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
}