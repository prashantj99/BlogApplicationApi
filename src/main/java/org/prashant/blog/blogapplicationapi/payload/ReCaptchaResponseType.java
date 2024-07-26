package org.prashant.blog.blogapplicationapi.payload;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReCaptchaResponseType {
    private boolean success;
    private String challenge_ts;
    private String hostname;
}