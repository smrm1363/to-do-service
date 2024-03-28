package com.example.todoservice.infrastructure.in.web;

import com.example.todoservice.application.usecase.ItemUseCase;
import com.example.todoservice.infrastructure.in.web.dto.ToDoItemOutDto;
import com.example.todoservice.infrastructure.in.web.dto.TodoItemInDto;
import com.example.todoservice.infrastructure.in.web.dto.UpdateTodoItemDto;
import com.example.todoservice.model.TodoItem;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
public class TodoItemController {
  private final ItemUseCase itemUseCase;
  private final ToDoItemMapper toDoItemMapper;

  @PostMapping
  public ResponseEntity<ToDoItemOutDto> addItem(@RequestBody TodoItemInDto todoItemInDto) {
    TodoItem savedItem = itemUseCase.add(toDoItemMapper.todoitem(todoItemInDto));
    return new ResponseEntity<>(toDoItemMapper.todoItemOutDto(savedItem), HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ToDoItemOutDto> updateTodoItem(
      @PathVariable Long id, @RequestBody UpdateTodoItemDto updateTodoItemDto) {
    Optional<TodoItem> updatedItemOptional =
        itemUseCase.partiallyUpdate(
            id, toDoItemMapper.todoItemPartiallyUpdateDto(updateTodoItemDto));
    if (updatedItemOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return ResponseEntity.ok(toDoItemMapper.todoItemOutDto(updatedItemOptional.get()));
    }
  }

  @GetMapping
  public ResponseEntity<List<ToDoItemOutDto>> getAllItems(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    List<TodoItem> allItems = itemUseCase.getAllItems(page, size);
    return new ResponseEntity<>(mapToTodoItemOutDtos(allItems), HttpStatus.OK);
  }

  @GetMapping("/not-done")
  public ResponseEntity<List<ToDoItemOutDto>> getNotDoneItems(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    List<TodoItem> notDoneItems = itemUseCase.getNotDoneItems(page, size);
    return new ResponseEntity<>(mapToTodoItemOutDtos(notDoneItems), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ToDoItemOutDto> getItemDetails(@PathVariable Long id) {
    TodoItem item = itemUseCase.getItemById(id);
    if (item == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(toDoItemMapper.todoItemOutDto(item), HttpStatus.OK);
  }

  private List<ToDoItemOutDto> mapToTodoItemOutDtos(List<TodoItem> allItems) {
    return allItems.stream().map(toDoItemMapper::todoItemOutDto).toList();
  }
}
