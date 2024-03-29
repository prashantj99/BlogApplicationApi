package org.prashant.blog.blogapplicationapi.auth.service;

import org.prashant.blog.blogapplicationapi.entities.RefreshToken;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.RefreshTokenRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username){
        User user = this.userRepository.findByUserEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found with email : "+username));
        RefreshToken refreshToken = user.getRefreshToken();
        if(refreshToken == null){
            refreshToken = RefreshToken.builder().refreshToken(UUID.randomUUID().toString()).
                    expirationTime(Instant.now().plusMillis(30*60*1000))
                    .user(user).
                    build();
            this.refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken db_refreshToken = this.refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new ResourceNotFound("RefreshToken", "refreshToken", refreshToken));
        if(db_refreshToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(db_refreshToken);
            throw new RuntimeException("Refresh Token is expired!!!");
        }
        return db_refreshToken;
    }
}
