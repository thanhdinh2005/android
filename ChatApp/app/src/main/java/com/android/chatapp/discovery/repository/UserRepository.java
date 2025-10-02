package com.android.chatapp.discovery.repository;

import com.android.chatapp.discovery.service.UserService;
import com.android.chatapp.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final UserService userService;

    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public void searchUsers(String query, String currentUid, Callback<List<User>> callback) {
        userService.searchUsers(query, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                List<User> filteredUsers = filterUsers(users, currentUid);
                callback.onSuccess(filteredUsers);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private List<User> filterUsers(List<User> users, String currentUid) {
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (user != null && !user.getUid().equals(currentUid)) {
                filtered.add(user);
            }
        }
        return filtered;
    }
}