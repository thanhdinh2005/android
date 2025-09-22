package com.android.chatapp.model;

public class Notification {
    private String notificationId;
    private String userId;
    private String senderId;
    private String type;
    private String content;
    private long timestamp;
    private boolean isRead;

    public Notification() {}

    public Notification(String userId, String senderId, String type, String content, long timestamp) {
        this.userId = userId;
        this.senderId = senderId;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}