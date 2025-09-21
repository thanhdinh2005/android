package com.android.chatapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        auth.useEmulator("10.0.2.2", 9099);
        db.useEmulator("10.0.2.2", 8080);

        Log.d(TAG, "Current user: " +
                (auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "null"));

        // Test: tạo user mới
        String email = "user" + System.currentTimeMillis() + "@example.com";
        String password = "123456";

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d(TAG, "Đăng ký thành công: " + user.getEmail() + " - UID: " + user.getUid());

                        // Test Firestore: lưu 1 document
                        db.collection("users")
                                .document(user.getUid())
                                .set(Collections.singletonMap("email", email))
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Lưu Firestore OK"))
                                .addOnFailureListener(e -> Log.e(TAG, "Lỗi Firestore", e));

                    } else {
                        Log.e(TAG, "Lỗi đăng ký", task.getException());
                    }
                });
    }
}