package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.ForgotPassword;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.ChangePassword;
import org.prashant.blog.blogapplicationapi.payload.MailBody;
import org.prashant.blog.blogapplicationapi.repository.ForgotPasswordRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forgot_password")
public class ForgotPasswordController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/verify_email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user = this.userRepository.findByUserEmail(email).orElseThrow(()->new UsernameNotFoundException("user with "+email+" not found"));
        this.forgotPasswordRepository.deleteByUser(user);
//        System.out.println("Hello");
        Long otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("Reset Password Mail!!!")
                .text("This is OTP for your Password Reset Request "+otp).build();
        ForgotPassword forgotPassword=new ForgotPassword();
        forgotPassword.setOtp(otp);
        forgotPassword.setUser(user);
        forgotPassword.setExpirationTime(new Date(System.currentTimeMillis()+90*1000));
        emailService.sendSimpleMessage(mailBody);
        ForgotPassword saved_fp=forgotPasswordRepository.save(forgotPassword);
        System.out.println(saved_fp.getFpid());
        return ResponseEntity.ok("email sent successfully");
    }

    @PostMapping("/verify_otp/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Long otp, @PathVariable String email){
        User user = this.userRepository.findByUserEmail(email).orElseThrow(()->new UsernameNotFoundException("user with "+email+" not found"));
        ForgotPassword forgotPassword=this.forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(()-> new ResourceNotFound("User", "Otp", otp.toString()));
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
            return new ResponseEntity<>("OTP Expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP verified!!!");
    }

    @PostMapping("/change_password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
        User user = this.userRepository.findByUserEmail(email).orElseThrow(()->new UsernameNotFoundException("user with "+email+" not found"));
        if(!changePassword.new_password().equals(changePassword.repeat_password())){
            return new ResponseEntity<>("password mismatched!!!", HttpStatus.EXPECTATION_FAILED);
        }
        user.setUserPassword(passwordEncoder.encode(changePassword.new_password()));
        this.userRepository.save(user);
        return ResponseEntity.ok("password updated successfully");
    }

    private  Long otpGenerator(){
        return new Random().nextLong(100000, 999999);
    }
}
