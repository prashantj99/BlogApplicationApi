package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.ForgotPassword;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidForgotPasswordToken;
import org.prashant.blog.blogapplicationapi.exceptions.InvalidOTPException;
import org.prashant.blog.blogapplicationapi.payload.ChangePassword;
import org.prashant.blog.blogapplicationapi.payload.MailBody;
import org.prashant.blog.blogapplicationapi.payload.VerifyEmailRequest;
import org.prashant.blog.blogapplicationapi.payload.VerifyOTPRequest;
import org.prashant.blog.blogapplicationapi.repository.ForgotPasswordRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.EmailService;
import org.prashant.blog.blogapplicationapi.service.ReCaptchaValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forgotpassword")
public class ForgotPasswordController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReCaptchaValidationService reCaptchaValidationService;

    @PostMapping("/verify_email")
    public ResponseEntity<String> handleEmailVerification(@RequestBody VerifyEmailRequest verifyEmailRequest){
        //verifly captcha
        if(!reCaptchaValidationService.validateCaptcha(verifyEmailRequest.captcha())){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Invalid Captcha");
        }
        User user = this.userRepository.findByUserEmail(verifyEmailRequest.email()).orElseThrow(()->new UsernameNotFoundException("user with "+verifyEmailRequest.email()+" not found"));
        this.forgotPasswordRepository.deleteByUser(user);
        Long otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(verifyEmailRequest.email())
                .subject("Reset Password Mail!!!")
                .text("This is OTP for your Password Reset Request "+otp).build();
        ForgotPassword forgotPassword=new ForgotPassword();
        forgotPassword.setOtp(otp);
        forgotPassword.setUser(user);
        forgotPassword.setToken(UUID.randomUUID().toString());
        forgotPassword.setExpirationTime(new Date(System.currentTimeMillis()+15*60*1000));
        emailService.sendSimpleMessage(mailBody);
        ForgotPassword saved_fp=forgotPasswordRepository.save(forgotPassword);
        System.out.println(saved_fp.getFpid());
        return ResponseEntity.status(HttpStatus.OK).body("Email verification successful");
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest){
        User user = this.userRepository.findByUserEmail(verifyOTPRequest.email()).orElseThrow(()->new UsernameNotFoundException("user with "+verifyOTPRequest.email()+" not found"));
        ForgotPassword forgotPassword=this.forgotPasswordRepository.findByOtpAndUser(verifyOTPRequest.otp(), user).orElseThrow(()-> new InvalidOTPException("invalid otp "+ verifyOTPRequest.otp().toString()));
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
            return new ResponseEntity<>("OTP Expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(forgotPassword.getToken());
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword){
        if(!changePassword.new_password().equals(changePassword.repeat_password())){
            return new ResponseEntity<>("password mismatched!!!", HttpStatus.EXPECTATION_FAILED);
        }
        ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(changePassword.token()).orElseThrow(()-> new InvalidForgotPasswordToken("Invalid Token "+changePassword.token()));
        User user = forgotPassword.getUser();
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
            throw new InvalidForgotPasswordToken("Token expired!!!");
        }
        user.setUserPassword(passwordEncoder.encode(changePassword.new_password()));
        this.userRepository.save(user);
        forgotPasswordRepository.deleteById(forgotPassword.getFpid());
        return ResponseEntity.ok("password updated successfully");
    }

    private  Long otpGenerator(){
        return new Random().nextLong(100000, 999999);
    }
}
