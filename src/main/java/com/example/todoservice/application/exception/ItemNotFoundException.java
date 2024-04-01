package com.example.todoservice.application.exception;

public class ItemNotFoundException extends Exception implements ApplicationException {

  public ItemNotFoundException(Long id) {
    super(String.format("Todo item with ID <%s> not found.", id));
  }
}
