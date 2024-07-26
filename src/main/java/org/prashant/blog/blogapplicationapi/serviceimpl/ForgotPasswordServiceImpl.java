package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.ForgotPassword;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.*;
import org.prashant.blog.blogapplicationapi.payload.ChangePasswordRequest;
import org.prashant.blog.blogapplicationapi.payload.MailBody;
import org.prashant.blog.blogapplicationapi.payload.VerifyEmailRequest;
import org.prashant.blog.blogapplicationapi.payload.VerifyOTPRequest;
import org.prashant.blog.blogapplicationapi.repository.ForgotPasswordRepository;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.EmailService;
import org.prashant.blog.blogapplicationapi.service.ForgotPasswordService;
import org.prashant.blog.blogapplicationapi.service.ReCaptchaValidationService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final ReCaptchaValidationService reCaptchaValidationService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        if(!reCaptchaValidationService.validateCaptcha(verifyEmailRequest.captcha())){
            throw new InvalidRecaptchaException("Unable to verify recaptcha!!");
        }

        //find user from database
        User user = this.userRepository.findByEmail(verifyEmailRequest.email())
                .orElseThrow(()-> {
                    return new ResourceNotFound("User", "userEmail", verifyEmailRequest.email());
                });
        this.forgotPasswordRepository.deleteByUser(user);

        //generate otp
        Long otp = ForgotPasswordService.otpGenerator();

        //build mail body
        MailBody mailBody = MailBody.builder()
                .to(verifyEmailRequest.email())
                .subject("Reset Password Mail!!!")
                .text("This is OTP for your Password Reset Request "+otp).build();

        //build forgot password entity
        ForgotPassword forgotPassword=new ForgotPassword();
        forgotPassword.setOtp(otp);
        forgotPassword.setUser(user);
        forgotPassword.setToken(UUID.randomUUID().toString());
        forgotPassword.setExpirationTime(new Date(System.currentTimeMillis()+ AppConstant.REFRESH_ACCESS_TOKEN_EXP_TIME));

        //send email to user
        emailService.sendSimpleMessage(mailBody);

        //if mail send successfully then save forgot password entity into db
        forgotPasswordRepository.save(forgotPassword);
    }

    @Override
    public String verifyOTP(VerifyOTPRequest verifyOTPRequest) {
        //fetch user from db
        User user = this.userRepository.findByEmail(verifyOTPRequest.email())
                .orElseThrow(()-> {
                    return new ResourceNotFound("User", "userEmail", verifyOTPRequest.email());
                });

        //fetch forgot password request by user
        ForgotPassword forgotPassword=this.forgotPasswordRepository.findByOtpAndUser(verifyOTPRequest.otp(), user)
                .orElseThrow(()-> {
                    return new InvalidOTPException("invalid otp " + verifyOTPRequest.otp().toString());
                });

        //if forgot password request is invalid
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            throw new InvalidOTPException("OTP Expired");
        }

        //send token
        return forgotPassword.getToken();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        //check for password confirmation
        if (!request.new_password().equals(request.repeat_password())) {
            throw new InvalidParameterException("password mismatched!!!");
        }

        //get forgot password reqest from db
        ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(request.token())
                .orElseThrow(() -> {
                    return new InvalidForgotPasswordToken("Invalid Token " + request.token());
                });

        //verify forgot password token
        User user = forgotPassword.getUser();
        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            throw new InvalidForgotPasswordToken("Token expired!!!");
        }

        //save new password in encoded form
        user.setPassword(passwordEncoder.encode(request.new_password()));
        this.userRepository.save(user);

        //delete the forgot password request
        forgotPasswordRepository.deleteById(forgotPassword.getId());
    }
}
