package org.prashant.blog.blogapplicationapi.exceptions;

public class InvalidRecaptchaException extends RuntimeException{
    public InvalidRecaptchaException(String msg){
        super(msg);
    }
}
