package com.example.thecoffeehouse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class ThecoffeehouseApplication {

	public static void main(String[] args) throws IOException {

		ClassLoader classLoader = ThecoffeehouseApplication.class.getClassLoader();
		File file = new File(classLoader.getResource("serviceAccountKey.json").getFile());
		FileInputStream serviceAccountKey = new FileInputStream(file.getAbsolutePath());

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
				.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(ThecoffeehouseApplication.class, args);
	}

}
