package org.prashant.blog.blogapplicationapi.payload;

public record AccountDTO(
        Long accountId,
        String link,
        String platform
) {
}
