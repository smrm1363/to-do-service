package com.example.todoservice.infrastructure.in.web.dto;

import com.example.todoservice.model.ItemStatus;

import java.time.LocalDateTime;

public record ToDoItemOutDto(Long id,
                             String description,
                             ItemStatus itemStatus,
                             LocalDateTime creationDateTime,
                             LocalDateTime dueDateTime,
                             LocalDateTime markAsDoneDateTime) {}
