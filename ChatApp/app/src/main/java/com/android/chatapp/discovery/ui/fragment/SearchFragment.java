package com.android.chatapp.discovery.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chatapp.R;
import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.discovery.repository.UserRepository;
import com.android.chatapp.discovery.ui.adapter.UserAdapter;
import com.android.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView rvSearchResults;
    private UserAdapter userAdapter;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.etSearch);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        userRepository = new UserRepository();

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(new ArrayList<>(), user -> {

            // Open activity chat
            Toast.makeText(getContext(), "Clicked: " + user.getUsername(), Toast.LENGTH_SHORT).show();
        });
        rvSearchResults.setAdapter(userAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchUsers(query);
                } else {
                    userAdapter.setUsers(new ArrayList<>());
                }
            }
        });

        return view;
    }

    private void searchUsers(String query) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRepository.searchUsers(query, currentUid, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                userAdapter.setUsers(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Search error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

