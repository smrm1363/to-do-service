package com.example.todoservice.application.usecase;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.model.TodoItem;

import java.util.List;
import java.util.Optional;

public interface ItemUseCase {

   TodoItem add(TodoItem mapToToDoItem) ;

   Optional<TodoItem> partiallyUpdate(Long id, TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto) throws ItemModificationForbiddenException;

   List<TodoItem> getAllItems(int page, int size);

   List<TodoItem> getNotDoneItems(int page, int size);

   Optional<TodoItem> getItemById(Long id);
}
