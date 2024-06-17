package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.Utils.JwtUtils;
import com.example.thecoffeehouse.dto.user.GoogleUserInfo;
import com.example.thecoffeehouse.dto.user.LoginDto;
import com.example.thecoffeehouse.dto.user.RegisterDto;
import com.example.thecoffeehouse.dto.user.UserDto;
import com.example.thecoffeehouse.entity.User;
import com.example.thecoffeehouse.repository.RoleRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.repository.UserRoleRepository;
import com.example.thecoffeehouse.service.UserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/hello")
    public ResponseEntity<?> create(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(loginDto);
    }


    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody RegisterDto registerDto, BindingResult result) {
        try {
            var checkUser = userRepository.findByEmail(registerDto.getEmail());
            if (checkUser != null) {
                return ResponseEntity.badRequest().body("Email already used");
            }

            UserDto user = userService.createUser(registerDto);

            String jwtToken = createJwtToken(user);
            String refreshToken = createRefreshToken(); // Implement this method
            Instant refreshTokenExpiry = Instant.now().plus(30, ChronoUnit.DAYS); // Example expiry time

            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiry(refreshTokenExpiry);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("refreshToken", refreshToken);
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("There is an Exception: ");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // Thực hiện xác thực người dùng và sinh JWT token
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            var login = userService.login(loginDto);
            String jwtToken = createJwtToken(login);
            String refreshToken = createRefreshToken(); // Implement this method
            Instant refreshTokenExpiry = Instant.now().plus(30, ChronoUnit.DAYS); // Example expiry time

            login.setRefreshToken(refreshToken);
            login.setRefreshTokenExpiry(refreshTokenExpiry);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("refreshToken", refreshToken);
            response.put("user", login);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/login/google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String sub = (String) attributes.get("sub");
        String picture = (String) attributes.get("picture");

        GoogleUserInfo userInfo = new GoogleUserInfo();
        userInfo.setSub(sub);
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setPicture(picture);

        User user = userService.saveOrUpdateGoogleUser(userInfo);
        String token = jwtUtils.generateToken(email);

        Map<String, Object> response = new HashMap<>(attributes);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    private String createJwtToken(UserDto user) {
        Instant now = Instant.now();

        List<String> roles = userRoleRepository.findByUserId(user.getId()).stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleID()).orElseThrow(() -> new RuntimeException("Role not found")).getName())
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24*3600))
                .subject(user.getEmail())
                .claim("role", roles)
                .build();

        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return encoder.encode(params).getTokenValue();
    }

    private String createRefreshToken() {
        return UUID.randomUUID().toString(); // Simple example, consider using a more secure method
    }
}
