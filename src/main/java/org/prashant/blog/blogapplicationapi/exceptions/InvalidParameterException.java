package org.prashant.blog.blogapplicationapi.exceptions;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message){
        super(message);
    }
}
