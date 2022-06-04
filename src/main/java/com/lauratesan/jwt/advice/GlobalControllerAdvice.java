package com.lauratesan.jwt.advice;

import com.lauratesan.jwt.dto.ErrorResponse;
import com.lauratesan.jwt.exceptions.EmailAlreadyExistException;
import com.lauratesan.jwt.exceptions.EmailConfirmException;
import com.lauratesan.jwt.exceptions.PasswordConfirmException;
import com.lauratesan.jwt.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        return new ErrorResponse("Invalid email or password!", HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailAlreadyExistException(EmailAlreadyExistException emailAlreadyExistException) {
        return new ErrorResponse(emailAlreadyExistException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmailConfirmException(EmailConfirmException emailEmailConfirmException) {
        return new ErrorResponse(emailEmailConfirmException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePasswordConfirmException(PasswordConfirmException passwordConfirmException) {
        return new ErrorResponse(passwordConfirmException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ErrorResponse(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST.name());
    }

}
