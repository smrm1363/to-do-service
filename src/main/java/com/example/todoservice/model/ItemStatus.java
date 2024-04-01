package com.example.todoservice.model;

public enum ItemStatus {
  NOT_DONE("not done"), DONE("done"), PAST_DUE("past due");

  private final String value;

  ItemStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
