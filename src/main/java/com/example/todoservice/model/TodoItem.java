package com.example.todoservice.model;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TodoItem {

  private Long id;
  private String description;
  private @Nonnull ItemStatus itemStatus;
  private LocalDateTime creationDateTime;
  private @Nonnull LocalDateTime dueDateTime;
  private LocalDateTime markAsDoneDateTime;
}
