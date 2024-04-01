package com.example.todoservice.infrastructure.in.web.dto;

import com.example.todoservice.model.ItemStatus;

public record UpdateTodoItemDto(
    String description, ItemStatus itemStatus
) {

}
