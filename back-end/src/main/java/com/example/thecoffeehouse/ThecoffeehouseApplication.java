package com.example.thecoffeehouse;

import com.example.thecoffeehouse.entity.user.Role;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class ThecoffeehouseApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ThecoffeehouseApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner createAdminUser() {
//		return args -> {
//			User user = new User();
//			user.setEmail("admin@gmail.com");
//			user.setFirstName("admin");
//			user.setLastName("admin");
//			user.setRole(Role.ADMIN);
//			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
//			userRepository.save(user);
//
//		};
//	}
}
