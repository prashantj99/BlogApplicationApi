package org.prashant.blog.blogapplicationapi.payload;

public record VerifyOTPRequest(Long otp, String email) {
}
