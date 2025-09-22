package com.android.chatapp.discovery.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chatapp.R;
import com.android.chatapp.discovery.viewmodel.SearchViewModel;
import com.android.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView userRecyclerView;
    private SearchAdapter adapter;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Khởi tạo UI
        searchEditText = findViewById(R.id.searchEditText);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        // Thiết lập RecyclerView
        adapter = new SearchAdapter(this, new ArrayList<>());
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(adapter);

        // Quan sát LiveData từ ViewModel
        viewModel.getSearchResults().observe(this, users -> {
            if (users != null) {
                adapter.updateUsers(users);
            }
        });
        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Lắng nghe thay đổi trong EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                viewModel.searchUsers(query);
            }
        });
    }
}