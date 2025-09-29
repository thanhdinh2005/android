package com.android.chatapp.discovery.repository;

import com.android.chatapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserRepository(){
    }

    // UC10: Searching user by username or email
    public void searchUsers(String query, String currentUid, Callback<List<User>> callback) {
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

                                        // 🔹 Lọc bỏ bản thân
                                        List<User> filtered = new ArrayList<>();
                                        for (User u : users) {
                                            if (!u.getUid().equals(currentUid)) {
                                                filtered.add(u);
                                            }
                                        }

                                        callback.onSuccess(filtered);
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




    // UC13: Listen to online users
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

    // UC10: Listen to all users except the current user
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
