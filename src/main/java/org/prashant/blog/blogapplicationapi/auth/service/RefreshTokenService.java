package org.prashant.blog.blogapplicationapi.auth.service;

import org.prashant.blog.blogapplicationapi.entities.RefreshToken;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.RefreshTokenExpiredException;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.repository.RefreshTokenRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.Date;
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
        User user = this.userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFound("User", "userEmail", username));
        RefreshToken token = user.getRefreshToken();
        if(token == null){
            //create a new refresh token
            token = new RefreshToken();
            token.setRefreshToken(UUID.randomUUID().toString());
            token.setUser(user);
            token.setExpirationTime(Instant.now().plusMillis(AppConstant.REFRESH_ACCESS_TOKEN_EXP_TIME));

            //save token to database
            this.refreshTokenRepository.save(token);
        }
        return token;
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken db_refreshToken = this.refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new ResourceNotFound("RefreshToken", "refreshToken", refreshToken));
        if(db_refreshToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(db_refreshToken);
            throw new RefreshTokenExpiredException("Refresh Token is expired!!!");
        }
        return db_refreshToken;
    }
}
