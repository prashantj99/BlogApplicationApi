package org.prashant.blog.blogapplicationapi.auth.utils;

import org.prashant.blog.blogapplicationapi.entities.Account;
import org.prashant.blog.blogapplicationapi.entities.Role;
import org.prashant.blog.blogapplicationapi.entities.User;

import java.util.List;
import java.util.Set;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Long id,
        String name,
        String email,
        String about,
        String profileImg,
        List<Account> accounts,
        List<Long> roles
) {
    public AuthResponse(String accessToken, String refreshToken, User user) {
        this(accessToken,
                refreshToken,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getAbout(),
                user.getProfileImg(),
                user.getAccounts(),
                user.getRoles().stream().map(Role::getId).toList()
        );
    }
}
