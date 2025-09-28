package com.android.chatapp.discovery.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chatapp.R;
import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.discovery.repository.ChatRepository;
import com.android.chatapp.discovery.ui.adapter.ChatAdapter;
import com.android.chatapp.model.Chat;
import com.android.chatapp.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private ChatRepository chatRepository;
    private ListenerRegistration chatListener;
    private String currentUid;
    private ImageView ivCurrentUserAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewChats = view.findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
        ivCurrentUserAvatar = view.findViewById(R.id.ivCurrentUserAvatar);
        View onlineIndicator = view.findViewById(R.id.onlineIndicator);
        onlineIndicator.setVisibility(View.VISIBLE);
        loadCurrentUser();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUid = firebaseUser.getUid();
            Log.d("HomeFragment", "Current user: " + currentUid);
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Khởi tạo adapter
        chatAdapter = new ChatAdapter(getContext(), new ArrayList<>(), currentUid);
        recyclerViewChats.setAdapter(chatAdapter);

        // Repo
        chatRepository = new ChatRepository();

        // Nghe dữ liệu chat từ Firestore
        chatListener = chatRepository.listenChats(currentUid, new Callback<List<Chat>>() {
            @Override
            public void onSuccess(List<Chat> result) {
                chatAdapter.updateChats(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (chatListener != null) chatListener.remove();
    }


    private void loadCurrentUser() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User currentUser = doc.toObject(User.class);
                        if (currentUser != null && currentUser.getPhotoUrl() != null) {
                            Glide.with(this)
                                    .load(currentUser.getPhotoUrl())
                                    .placeholder(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(ivCurrentUserAvatar);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeFragment", "Failed to load current user avatar", e);
                });
    }
}

