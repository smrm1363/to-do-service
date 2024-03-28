package com.example.todoservice.application.dto;

import com.example.todoservice.model.ItemStatus;

public record TodoItemPartiallyUpdateDto(String description,
                                         ItemStatus itemStatus) {
}
