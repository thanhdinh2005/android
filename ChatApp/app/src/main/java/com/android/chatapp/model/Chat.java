package com.android.chatapp.model;

import java.util.List;

public class Chat {
    private String chatId;
    private List<String> participants;
    private String lastMessage;
    private long lastTimestamp;
    private int unreadCount;

    public Chat() {}

    public Chat(String chatId, List<String> participants, String lastMessage, long lastTimestamp) {
        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
        this.unreadCount = 0;
    }
}