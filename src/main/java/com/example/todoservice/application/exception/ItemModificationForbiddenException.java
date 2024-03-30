package com.example.todoservice.application.exception;

public class ItemModificationForbiddenException extends Exception{
  public ItemModificationForbiddenException(Long id){
    super(String.format("Modification an item which is in past-due state with id <%s> is forbidden.", id));
  }
}
