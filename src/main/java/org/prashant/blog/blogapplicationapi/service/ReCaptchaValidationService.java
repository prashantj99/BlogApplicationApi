package org.prashant.blog.blogapplicationapi.service;

public interface ReCaptchaValidationService {
    public boolean validateCaptcha(String captchaResponse);
}
