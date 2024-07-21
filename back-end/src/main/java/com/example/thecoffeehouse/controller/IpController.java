package com.example.thecoffeehouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api")
public class IpController {

    @GetMapping("/server-ip")
    public ResponseEntity<String> getServerIp() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ResponseEntity.ok(ip);
        } catch (UnknownHostException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get IP");
        }
    }
}
