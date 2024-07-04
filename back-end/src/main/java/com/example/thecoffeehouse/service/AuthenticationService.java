package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.user.*;
import com.example.thecoffeehouse.entity.user.User;

public interface AuthenticationService {
    User signup(RegisterDto registerDto);

    UserDto signin(LoginDto loginDto);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
