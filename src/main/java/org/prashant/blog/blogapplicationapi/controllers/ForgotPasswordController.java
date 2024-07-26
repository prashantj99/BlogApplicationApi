package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.ChangePasswordRequest;
import org.prashant.blog.blogapplicationapi.payload.VerifyEmailRequest;
import org.prashant.blog.blogapplicationapi.payload.VerifyOTPRequest;
import org.prashant.blog.blogapplicationapi.service.ForgotPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forgotpassword")
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/verify_email")
    public ResponseEntity<String> handleEmailVerification(@RequestBody VerifyEmailRequest verifyEmailRequest){
        forgotPasswordService.verifyEmail(verifyEmailRequest);
        return ResponseEntity.ok("verified");
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest){
        String token = forgotPasswordService.verifyOTP(verifyOTPRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest changePasswordRequest) {
        forgotPasswordService.changePassword(changePasswordRequest);
        return ResponseEntity.ok("password updated successfully");
    }
}
