package com.itsurena.usermng.api.handler;


import com.itsurena.usermng.api.web.ErrorResDto;
import com.itsurena.usermng.exception.UserAlreadyExistsException;
import com.itsurena.usermng.exception.UserDataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResDto> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorResDto errorResDto = ErrorResDto.builder().message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value()).build();
        return new ResponseEntity<>(errorResDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public final ResponseEntity<ErrorResDto> handleInvalidParameterException(InvalidParameterException ex) {
        log.warn("invalid param error", ex);
        return new ResponseEntity<>(new ErrorResDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDataNotFoundException.class)
    public ResponseEntity<ErrorResDto> DataNotFoundException(UserDataNotFoundException e) {
        ErrorResDto errorResDto = ErrorResDto.builder().message(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value()).build();
        return new ResponseEntity<>(errorResDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResDto> handleGeneralException(Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
