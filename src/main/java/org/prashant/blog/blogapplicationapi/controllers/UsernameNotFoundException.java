package org.prashant.blog.blogapplicationapi.controllers;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String message){
        super(message);
    }
}
