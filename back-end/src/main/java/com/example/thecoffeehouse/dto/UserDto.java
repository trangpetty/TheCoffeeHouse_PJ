package com.example.thecoffeehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
