package org.prashant.blog.blogapplicationapi.exceptions;

public class InvalidOTPException extends RuntimeException{
    public InvalidOTPException(String message){
        super(message);
    }
}
