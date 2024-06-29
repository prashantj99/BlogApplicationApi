package org.prashant.blog.blogapplicationapi.payload;

public record ChangePassword(String email, String new_password, String repeat_password, String token) {
}
