package org.prashant.blog.blogapplicationapi.payload;

public record VerifyEmailRequest(String email, String captcha) {
}
