package org.prashant.blog.blogapplicationapi.serviceimpl;

import org.prashant.blog.blogapplicationapi.payload.ReCaptchaResponseType;
import org.prashant.blog.blogapplicationapi.service.ReCaptchaValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaValidationServiceImpl implements ReCaptchaValidationService {
    @Value("${GOOGLE_RECAPTCHA_ENDPOINT}")
    private String GOOGLE_RECAPTCHA_ENDPOINT;
    @Value("${RECAPTCHA_SECRET}")
    private String RECAPTCHA_SECRET;
    @Override
    public boolean validateCaptcha(String captchaResponse){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", RECAPTCHA_SECRET);
        requestMap.add("response", captchaResponse);

        ReCaptchaResponseType apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, ReCaptchaResponseType.class);
        if(apiResponse == null){
            return false;
        }

        return Boolean.TRUE.equals(apiResponse.isSuccess());
    }
}
