package com.example.thecoffeehouse.Utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Getter
    private static String serverAddress;

    @Value("${server.address}")
    public void setServerAddress(String serverAddress) {
        AppConfig.serverAddress = serverAddress;
    }

}