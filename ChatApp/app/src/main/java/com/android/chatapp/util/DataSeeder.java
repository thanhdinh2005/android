package com.android.chatapp.util;

import android.util.Log;

import com.android.chatapp.model.Chat;
import com.android.chatapp.model.Message;
import com.android.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DataSeeder {
    private static final String TAG = "DataSeeder";
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void seedAll() {
        createSampleUsers();
    }

    private void createSampleUsers() {
        String[][] users = {
                {"chi@gmail.com", "123456", "Chi"},
                {"bao@gmail.com", "123456", "Bao"},
                {"lan@gmail.com", "123456", "Lan"}
        };

        for (String[] u : users) {
            auth.createUserWithEmailAndPassword(u[0], u[1])
                    .addOnSuccessListener(result -> {
                        FirebaseUser fUser = result.getUser();
                        if (fUser != null) {
                            User user = new User();
                            user.setUid(fUser.getUid());
                            user.setUsername(u[2]);
                            user.setEmail(u[0]);
                            user.setPhotoUrl("");
                            user.setOnline(true);
                            user.setLastSeen(System.currentTimeMillis());
                            user.setFcmToken("");

                            db.collection("users").document(fUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User added: " + u[2]));

                            // Khi đủ 3 user thì tạo chats
                            db.collection("users").get().addOnSuccessListener(snapshot -> {
                                if (snapshot.size() >= 3) {
                                    createSampleChats();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error creating user " + u[0], e));
        }
    }

    private void createSampleChats() {
        db.collection("users").get().addOnSuccessListener(snapshot -> {
            List<String> userIds = new ArrayList<>();
            snapshot.getDocuments().forEach(doc -> userIds.add(doc.getId()));

            if (userIds.size() >= 2) {
                String chatId = userIds.get(0) + "_" + userIds.get(1);
                Chat chat = new Chat(chatId,
                        Arrays.asList(userIds.get(0), userIds.get(1)),
                        "See you soon!",
                        System.currentTimeMillis(),
                        1);

                db.collection("chats").document(chatId).set(chat)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Chat created: " + chatId);
                            seedMessages(chatId, userIds.get(0), userIds.get(1));
                        });
            }

            if (userIds.size() >= 3) {
                String chatId2 = userIds.get(1) + "_" + userIds.get(2);
                Chat chat2 = new Chat(chatId2,
                        Arrays.asList(userIds.get(1), userIds.get(2)),
                        "Bye!",
                        System.currentTimeMillis(),
                        0);

                db.collection("chats").document(chatId2).set(chat2)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Chat created: " + chatId2);
                            seedMessages(chatId2, userIds.get(1), userIds.get(2));
                        });
            }
        });
    }

    private void seedMessages(String chatId, String userA, String userB) {
        List<String> sampleTexts = Arrays.asList(
                "Hi, how are you?",
                "I'm fine, thanks. You?",
                "Great! Are you free tomorrow?",
                "Yes, let's grab coffee ☕",
                "See you soon!"
        );

        long now = System.currentTimeMillis();
        for (int i = 0; i < sampleTexts.size(); i++) {
            String msgId = UUID.randomUUID().toString();
            Message msg = new Message();
            msg.setMessageId(msgId);

            if (i % 2 == 0) {
                msg.setSenderId(userA);
                msg.setReceiverId(userB);
            } else {
                msg.setSenderId(userB);
                msg.setReceiverId(userA);
            }

            msg.setContent(sampleTexts.get(i));
            msg.setTimestamp(now + (i * 60_000));
            msg.setRead(i < sampleTexts.size() - 1);
            msg.setDeletedForSender(false);
            msg.setDeletedForReceiver(false);
            msg.setType("text");

            db.collection("chats").document(chatId)
                    .collection("messages")
                    .document(msgId)
                    .set(msg)
                    .addOnSuccessListener(v ->
                            Log.d(TAG, "Message added to chat " + chatId + " : " + msg.getContent()));
        }
    }
}
