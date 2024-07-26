package org.prashant.blog.blogapplicationapi.exceptions;

public class UnAuthorizedOperationExcpetion extends RuntimeException{
    public UnAuthorizedOperationExcpetion(String msg){
        super(msg);
    }
}
