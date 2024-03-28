package com.example.todoservice.infrastructure.in.web.dto;

import com.example.todoservice.model.ItemStatus;

import java.time.LocalDateTime;

public record TodoItemInDto(
    String description,
    ItemStatus itemStatus,
    LocalDateTime dueDateTime,
    LocalDateTime markAsDoneDateTime) {}
