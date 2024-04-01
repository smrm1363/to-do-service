package com.example.todoservice.infrastructure.out.persistence.entity;

import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String description;
  private @Nonnull ItemStatus itemStatus;
  private @Nonnull LocalDateTime creationDateTime;
  private @Nonnull LocalDateTime dueDateTime;
  private LocalDateTime markAsDoneDateTime;

  public ToDoItemEntity(TodoItem todoItem) {
    setId(todoItem.getId());
    setDescription(todoItem.getDescription());
    setItemStatus(todoItem.getItemStatus());
    setCreationDateTime(todoItem.getCreationDateTime());
    setDueDateTime(todoItem.getDueDateTime());
    setMarkAsDoneDateTime(todoItem.getMarkAsDoneDateTime());
  }

  public TodoItem toModel() {
    return new TodoItem(id, description, itemStatus, creationDateTime, dueDateTime,
        markAsDoneDateTime
    );
  }
}
