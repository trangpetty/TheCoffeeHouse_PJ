package com.example.thecoffeehouse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class ThecoffeehouseApplication {

	public static void main(String[] args) throws IOException {
		initializeFirebaseApp();
		SpringApplication.run(ThecoffeehouseApplication.class, args);
	}

	private static void initializeFirebaseApp() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setStorageBucket("thecoffeehouse-197d2.appspot.com")
				.build();

		FirebaseApp.initializeApp(options);
	}

}
