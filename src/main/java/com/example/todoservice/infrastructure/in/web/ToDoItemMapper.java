package com.example.todoservice.infrastructure.in.web;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.infrastructure.in.web.dto.UpdateTodoItemDto;
import com.example.todoservice.model.TodoItem;
import com.example.todoservice.infrastructure.in.web.dto.TodoItemInDto;
import com.example.todoservice.infrastructure.in.web.dto.ToDoItemOutDto;
import org.springframework.stereotype.Component;

@Component
public class ToDoItemMapper {

  public ToDoItemOutDto todoItemOutDto(TodoItem todoItem) {
    return new ToDoItemOutDto(
        todoItem.getId(),
        todoItem.getDescription(),
        todoItem.getItemStatus(),
        todoItem.getCreationDateTime(),
        todoItem.getDueDateTime(),
        todoItem.getMarkAsDoneDateTime());
  }

  public TodoItem todoitem(TodoItemInDto todoItemInDto) {
    return new TodoItem(
        null,
        todoItemInDto.description(),
        todoItemInDto.itemStatus(),
        null,
        todoItemInDto.dueDateTime(),
        todoItemInDto.markAsDoneDateTime());
  }

  public TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto(UpdateTodoItemDto updateTodoItemDto) {
    return new TodoItemPartiallyUpdateDto(updateTodoItemDto.description(), updateTodoItemDto.itemStatus());
  }
}
