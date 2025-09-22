package com.android.chatapp.model;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp;
    private boolean isRead;
    private boolean deletedForSender;
    private boolean deletedForReceiver;
    private String type;

    public Message() {}

    public Message(String senderId, String receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = false;
        this.deletedForSender = false;
        this.deletedForReceiver = false;
        this.type = "text";
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public boolean isDeletedForSender() {
        return deletedForSender;
    }

    public void setDeletedForSender(boolean deletedForSender) {
        this.deletedForSender = deletedForSender;
    }

    public boolean isDeletedForReceiver() {
        return deletedForReceiver;
    }

    public void setDeletedForReceiver(boolean deletedForReceiver) {
        this.deletedForReceiver = deletedForReceiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}