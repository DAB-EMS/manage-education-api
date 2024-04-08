package com.example.manageeducation.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FirebaseService {

    private Storage storage;
    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).
                    setProjectId("manage-education").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(token);
    }

    public String pushImage(MultipartFile file) throws IOException {
        String imageName = generateFileName(file.getOriginalFilename());
        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of("manage-education.appspot.com", imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getInputStream());
        String fileUrl = "https://firebasestorage.googleapis.com/v0/b/manage-education.appspot.com/o/"+ URLEncoder.encode(imageName, "UTF-8") + "?alt=media&token=" + imageName;
        System.out.println();
        return fileUrl;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
