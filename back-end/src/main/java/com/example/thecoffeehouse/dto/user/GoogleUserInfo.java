package com.example.thecoffeehouse.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleUserInfo {
    public String sub;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String email;
    public boolean email_verified;
    public String hd;
}
