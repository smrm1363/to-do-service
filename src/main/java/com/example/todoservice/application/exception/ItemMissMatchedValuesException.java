package com.example.todoservice.application.exception;

public class ItemMissMatchedValuesException extends Exception implements ApplicationException{
  public ItemMissMatchedValuesException(String description){
    super(description);
  }

}
