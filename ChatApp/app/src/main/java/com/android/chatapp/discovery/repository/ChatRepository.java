package com.android.chatapp.discovery.repository;

import com.android.chatapp.discovery.service.ChatService;
import com.android.chatapp.model.Chat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.List;

import com.google.firebase.firestore.ListenerRegistration;

public class ChatRepository {
    private final ChatService chatService;

    public ChatRepository(ChatService chatService) {
        this.chatService = chatService;
    }

    public ListenerRegistration listenUserChats(String uid, Callback<List<Chat>> callback) {
        return chatService.listenUserChats(uid, callback);
    }
}
