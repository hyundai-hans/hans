package com.handsome.mall.exception;

import io.jsonwebtoken.JwtException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.security.auth.message.AuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  private Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<Map<String, List<String>>> jwtException(
      JwtException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Map<String, List<String>>> authException(
      AuthException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<Map<String, List<String>>> userException(
      UserException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, List<String>>> productException(
      ProductException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<Map<String, List<String>>> postException(
      PostException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

    @ExceptionHandler(HistoryException.class)
    public ResponseEntity<Map<String, List<String>>> historyException(
      HistoryException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, List<String>>> methodArgumentException(
      ProductException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(
      RuntimeException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());
    return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }


}