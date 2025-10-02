package com.android.chatapp.discovery.service;

import com.android.chatapp.discovery.repository.Callback;
import com.android.chatapp.model.Chat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListenerRegistration listenUserChats(String uid, Callback<List<Chat>> callback) {
        return db.collection("chats")
                .whereArrayContains("participants", uid)
                .orderBy("lastTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    List<Chat> chats = new ArrayList<>();
                    if (snapshot != null) {
                        chats = snapshot.toObjects(Chat.class);
                    }
                    // Manipulation: Ensure empty list if no data
                    callback.onSuccess(chats);
                });
    }
}
