package org.prashant.blog.blogapplicationapi.payload;

public record ChangePasswordRequest(String email, String new_password, String repeat_password, String token) {
}
