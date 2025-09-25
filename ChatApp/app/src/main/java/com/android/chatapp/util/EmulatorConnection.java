package com.android.chatapp.util;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class EmulatorConnection {
    private static final String HOST = "10.0.2.2";
    private static final int AUTH_PORT = 9099;
    private static final int FIRESTORE_PORT = 8080;
    private static final int STORAGE_PORT = 9199;

    public static boolean connect() {
        try {
            FirebaseAuth.getInstance().useEmulator(HOST, AUTH_PORT);
            FirebaseFirestore.getInstance().useEmulator(HOST, FIRESTORE_PORT);
            // FirebaseStorage.getInstance().useEmulator(HOST, STORAGE_PORT);
            Log.d("EmulatorConnection", "Connected to Firebase Emulator Suite");
        } catch (Exception e){
            Log.e("EmulatorConnection", "Failed to connect to Firebase Emulator Suite", e);
            return false;
        }
        return true;
    }

    public static boolean signInWithTestAccount(String email, String password){
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("EmulatorConnection", "Sign in with test account successful");
                            Log.d("EmulatorConnection", "Current user: " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                        } else {
                            Log.e("EmulatorConnection", "Sign in with test account failed", task.getException());
                        }
                    });
        } catch (Exception e){
            Log.e("EmulatorConnection", "Failed to sign in with test account", e);
            return false;
        }
        return true;
    }
}