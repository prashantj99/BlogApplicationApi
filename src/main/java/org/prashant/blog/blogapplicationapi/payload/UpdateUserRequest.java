package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record UpdateUserRequest(
        Long userId,
        String password,
        String about,
        String name,
        String profileImg,
        List<AccountDTO> accounts
) {
}
