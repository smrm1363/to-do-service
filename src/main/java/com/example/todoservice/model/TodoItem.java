package com.example.todoservice.model;

import java.time.LocalDateTime;

public record TodoItem(
    Long id,
    String description,
    ItemStatus itemStatus,
    LocalDateTime creationDateTime,
    LocalDateTime dueDateTime,
    LocalDateTime markAsDoneDateTime) {}
