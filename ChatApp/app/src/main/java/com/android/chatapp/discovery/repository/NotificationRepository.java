package com.android.chatapp.discovery.repository;

import com.android.chatapp.model.Notification;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.List;

public class NotificationRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListenerRegistration listenUserNotifications(String userId, Callback<List<Notification>> callback) {
        return db.collection("notifications")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        callback.onError(e.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        List<Notification> notifications = snapshot.toObjects(Notification.class);
                        callback.onSuccess(notifications);
                    }
                });
    }

    public void markAsRead(String notificationId) {
        db.collection("notifications").document(notificationId)
                .update("isRead", true);
    }

    public void addNotification(Notification notification) {
        db.collection("notifications").document(notification.getNotificationId())
                .set(notification);
    }
}

