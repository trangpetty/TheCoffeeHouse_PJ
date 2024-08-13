package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.user.*;

public interface AuthenticationService {
    UserDto signup(RegisterDto registerDto);

    UserDto signin(LoginDto loginDto);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

//    JwtAuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest);
}
