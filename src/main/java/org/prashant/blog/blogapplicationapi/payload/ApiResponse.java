package org.prashant.blog.blogapplicationapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String content;
    private Boolean status;
}
