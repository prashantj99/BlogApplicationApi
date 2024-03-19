package org.prashant.blog.blogapplicationapi.exceptions;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFound extends RuntimeException {
    private String resourceName;
    private String resourceFiled;
    private String value;
}
