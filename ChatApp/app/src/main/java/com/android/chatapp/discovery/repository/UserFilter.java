package com.android.chatapp.discovery.repository;

import com.android.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserFilter {
    public List<User> filterUsers(List<User> users, String currentUid) {
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (!user.getUid().equals(currentUid)) {
                filtered.add(user);
            }
        }
        return filtered;
    }
}