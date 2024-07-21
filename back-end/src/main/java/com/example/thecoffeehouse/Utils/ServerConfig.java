package com.example.thecoffeehouse.Utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ServerConfig {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            try {
                String ip = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Server Address: " + ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        };
    }
}
