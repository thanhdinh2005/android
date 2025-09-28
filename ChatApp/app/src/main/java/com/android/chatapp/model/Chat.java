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

    public <T> Chat(String chatId, List<T> list, String s, long l, int i) {
        this.chatId = chatId;
        this.participants = (List<String>) list;
        this.lastMessage = s;
        this.lastTimestamp = l;
        this.unreadCount = i;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}