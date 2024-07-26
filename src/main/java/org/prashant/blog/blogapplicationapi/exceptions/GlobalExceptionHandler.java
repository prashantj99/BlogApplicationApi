package org.prashant.blog.blogapplicationapi.exceptions;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<?> buildResponse(HttpStatus status, String error, String description) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "error", error,
                        "error_description", description
                )
        );
    }

    @ExceptionHandler(InvalidForgotPasswordToken.class)
    public ResponseEntity<?> handleInvalidForgotPasswordToken(InvalidForgotPasswordToken ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "invalid_forgot_password_token", ex.getMessage());
    }

    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidOTPException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "invalid_otp", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFound ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "resource_not_found", ex.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "invalid_parameter", ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "null_value", ex.getMessage());
    }

    @ExceptionHandler(UnAuthorizedOperationExcpetion.class)
    public ResponseEntity<?> handleUnAuthorizedOperationException(UnAuthorizedOperationExcpetion ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "unauthorized_operation_performed", ex.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "unauthorized_operation_performed", ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<?> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "invalid_token", ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "file_not_found", ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "server_error", "An internal server error occurred. Please try again later.");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return buildResponse(HttpStatus.PAYLOAD_TOO_LARGE, "file_size_exceeded", "The uploaded file size exceeds the maximum allowed limit.");
    }

    @ExceptionHandler(InvalidRecaptchaException.class)
    public ResponseEntity<?> handleInvalidRecaptchaException(InvalidRecaptchaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "invalid_captcha", ex.getMessage());
    }
}
