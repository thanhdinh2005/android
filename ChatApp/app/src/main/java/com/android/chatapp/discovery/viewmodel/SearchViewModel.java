package com.android.chatapp.discovery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.chatapp.discovery.repository.UserDiscoveryRepository;
import com.android.chatapp.model.User;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private UserDiscoveryRepository repository;
    private final MutableLiveData<List<User>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchViewModel() {
        repository = new UserDiscoveryRepository();
    }

    public LiveData<List<User>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchUsers(String query) {
        if (query.isEmpty()) {
            searchResults.setValue(null);
            return;
        }
        repository.searchUsers(query, new UserDiscoveryRepository.SearchCallback() {
            @Override
            public void onSuccess(List<User> users) {
                searchResults.setValue(users);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error: " + e.getMessage());
            }
        });
    }


}
