package com.android.chatapp.model;

public class User {
    private String uid;
    private String username;
    private String email;
    private String photoUrl;
    private boolean online;
    private long lastSeen;
    private String fcmToken;

    public User() {}

    public User(String uid, String username, String email, String photoUrl) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.online = false;
        this.lastSeen = System.currentTimeMillis();
    }

    // Getters và Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}