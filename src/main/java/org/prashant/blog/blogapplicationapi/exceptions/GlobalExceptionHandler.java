package org.prashant.blog.blogapplicationapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidForgotPasswordToken.class)
    public ResponseEntity<String> handleInvalidForgotPasswordToken(InvalidForgotPasswordToken ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidOTPException.class)
    public ProblemDetail handleInvalidTokenException(InvalidOTPException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFound ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> handleInvalidParameterException(InvalidParameterException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>("Invalid Parameter" + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
