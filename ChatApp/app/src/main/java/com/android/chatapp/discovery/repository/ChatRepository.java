package com.android.chatapp.discovery.repository;

import com.android.chatapp.model.Chat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ChatRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UC11: Get all chats of a user
    public ListenerRegistration listenUserChats(String uid, Callback<List<Chat>> callback) {
        return db.collection("chats")
                .whereArrayContains("participants", uid)
                .orderBy("lastTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        List<Chat> chats = snapshot.toObjects(Chat.class);
                        callback.onSuccess(chats);
                    }
                });
    }

    // UC11: Create a new chat
    public void createChat(Chat chat, Callback<Void> callback) {
        db.collection("chats").document(chat.getChatId())
                .set(chat)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public ListenerRegistration listenChats(String currentUid, Callback<List<Chat>> callback) {
        return db.collection("chats")
                .whereArrayContains("participants", currentUid)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }
                    if (querySnapshot != null) {
                        List<Chat> chats = querySnapshot.toObjects(Chat.class);
                        callback.onSuccess(chats);
                    } else {
                        callback.onError("No data available");
                    }
                });
    }
}

