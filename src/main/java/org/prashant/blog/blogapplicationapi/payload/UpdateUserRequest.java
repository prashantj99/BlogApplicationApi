package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Account;

import java.util.List;
import java.util.Optional;

public record UpdateUserRequest(
        Long userId,
        String password,
        String about,
        String name,
        String profileImg,
        List<AccountDT> accounts
) {
}
