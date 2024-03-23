package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.auth.service.AuthService;
import org.prashant.blog.blogapplicationapi.auth.service.JwtService;
import org.prashant.blog.blogapplicationapi.auth.service.RefreshTokenService;
import org.prashant.blog.blogapplicationapi.auth.utils.AuthResponse;
import org.prashant.blog.blogapplicationapi.auth.utils.LoginRequest;
import org.prashant.blog.blogapplicationapi.auth.utils.RefreshTokenRequest;
import org.prashant.blog.blogapplicationapi.auth.utils.RegisterRequest;
import org.prashant.blog.blogapplicationapi.entities.RefreshToken;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = this.refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getRefreshToken()).build());
    }
}
