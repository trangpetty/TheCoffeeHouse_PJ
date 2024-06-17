package com.example.thecoffeehouse.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String avatar;
    private int gender;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private String roleName;
    private int removable;
    private String refreshToken;
    private Instant refreshTokenExpiry;

    public UserDto(Long id, String name, String phoneNumber, String email, String avatar, Integer gender, LocalDateTime createTime, LocalDateTime modifyTime, String roleName, Integer removable) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
        this.gender = gender;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.roleName = roleName;
        this.removable = removable;
        // Initialize other fields as needed
    }
}
