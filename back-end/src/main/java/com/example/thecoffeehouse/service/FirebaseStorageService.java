package com.example.thecoffeehouse.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseStorageService {
    private static final String BUCKET_NAME = "your-bucket-name";
    private static final String OBJECT_NAME_PREFIX = "images/";

    public String uploadFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String objectName = OBJECT_NAME_PREFIX + file.getOriginalFilename();

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();
        Storage storage = storageOptions.getService();

        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        Blob blob = storage.create(blobInfo, inputStream);

        return getPublicUrl(blob.getBlobId().getName());
    }

    private String getPublicUrl(String objectName) {
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + objectName;
    }
}
