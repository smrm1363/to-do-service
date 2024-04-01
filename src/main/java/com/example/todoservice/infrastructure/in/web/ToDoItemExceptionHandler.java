package com.example.todoservice.infrastructure.in.web;

import com.example.todoservice.application.exception.ApplicationException;
import com.example.todoservice.application.exception.ItemMissMatchedValuesException;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.application.exception.ItemNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ToDoItemExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(
      {ItemMissMatchedValuesException.class, ItemModificationForbiddenException.class}
  )
  public ResponseEntity<Object> handleValidationException(ApplicationException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", now());
    body.put("message", ex.getMessage());
    return new ResponseEntity<>(body, NOT_ACCEPTABLE);
  }

  @ExceptionHandler({ItemNotFoundException.class})
  public ResponseEntity<Object> handleNoDataFoundException(ApplicationException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", now());
    body.put("message", ex.getMessage());
    return new ResponseEntity<>(body, NOT_FOUND);
  }
}
