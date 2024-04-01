package com.example.manageeducation.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseConfig {
    @PostConstruct
    public void initialize() throws IOException {

        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(token);
    }

    public String generateCustomToken(String uid) throws FirebaseAuthException {
        // Set admin privilege on the user corresponding to uid.
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", true);
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
        // The new custom claims will propagate to the user's ID token the
        // next time a new one is issued.

        // Tạo Custom Token mới với thông tin phân quyền
        return FirebaseAuth.getInstance().createCustomToken(uid, claims);
        //return FirebaseAuth.getInstance().createCustomToken(uid);
    }
}
