package com.example.thecoffeehouse.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@Service
public class FirebaseStorageService {
    private static final String BUCKET_NAME = "thecoffeehouse-197d2.appspot.com";

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        InputStream inputStream = file.getInputStream();
        String fileName = file.getOriginalFilename();

        // Construct the object name with the folder name
        String objectName = folderName + "/" + fileName;

        // Upload the file to Firebase Storage
        StorageClient.getInstance().bucket().create(objectName, inputStream, file.getContentType());

        // Construct and return the URL of the uploaded file
        return "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME +
                "/o/"  + folderName + "%2F" + fileName + "?alt=media";
    }

}
