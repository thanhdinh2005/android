package com.android.chatapp.discovery.service;

import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.discovery.repository.UserFilter;
import com.android.chatapp.discovery.repository.UserRepository;
import com.android.chatapp.model.User;

import java.util.List;

public class SearchService {
    private final UserRepository userRepository;
    private final UserFilter userFilter;

    public SearchService(UserRepository userRepository, UserFilter userFilter) {
        this.userRepository = userRepository;
        this.userFilter = userFilter;
    }

    public void searchUsers(String query, String currentUid, Callback<List<User>> callback) {
        userRepository.searchUsers(query, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                List<User> filteredUsers = userFilter.filterUsers(users, currentUid);
                callback.onSuccess(filteredUsers);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
