package org.prashant.blog.blogapplicationapi.controllers;

import org.prashant.blog.blogapplicationapi.exceptions.InvalidForgotPasswordToken;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidOTPException;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidParameterException;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/invalidParameter")
    public String testInvalidParameter() {
        throw new InvalidParameterException("Invalid parameter passed");
    }

    @GetMapping("/test/invalidForgotPasswordToken")
    public String testInvalidForgotPasswordToken() {
        throw new InvalidForgotPasswordToken("Invalid forgot password token");
    }

    @GetMapping("/test/invalidOTP")
    public String testInvalidOTP() {
        throw new InvalidOTPException("Invalid OTP");
    }
}
