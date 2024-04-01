package com.example.todoservice.application.port;

import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoItemRepositoryPort {

  TodoItem persist(TodoItem newTodoItem);

  Optional<TodoItem> findById(Long id);

  List<TodoItem> findAll(int page, int size);

  List<TodoItem> findAllNotDone(int page, int size);

  List<TodoItem> findByDueDateTimeBeforeAndItemStatusNot(
      LocalDateTime currentDateTime, ItemStatus pastDue
  );

  void saveAll(List<TodoItem> overdueItems);
}
