package com.example.todoservice.application.port;

import com.example.todoservice.model.TodoItem;

import java.util.Optional;

public interface TodoItemRepository {

  TodoItem persist(TodoItem newTodoItem);

  Optional<TodoItem> findById(Long id);
}
