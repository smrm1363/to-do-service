package com.example.todoservice.infrastructure.in.web.dto;

import com.example.todoservice.model.ItemStatus;
import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;

public record TodoItemInDto(
    String description,
    @Nonnull ItemStatus itemStatus,
    @Nonnull LocalDateTime dueDateTime,
    LocalDateTime markAsDoneDateTime) {}
