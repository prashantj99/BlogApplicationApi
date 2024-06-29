package org.prashant.blog.blogapplicationapi.exceptions;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ResourceNotFound extends RuntimeException {
    private String resourceName;
    private String resourceFiled;
    private String value;
    public ResourceNotFound(String resourceName, String resourceFiled, String value){
        super("Resource Not Found!!!"+"Resource Name "+resourceName+" Filed Name "+resourceFiled+" Provided Value "+value);
    }
}
