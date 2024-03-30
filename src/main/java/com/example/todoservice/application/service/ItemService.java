package com.example.todoservice.application.service;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.application.port.TodoItemRepository;
import com.example.todoservice.application.usecase.ItemUseCase;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemUseCase {
  private final TodoItemRepository todoItemRepository;
  @Override
  @Transactional
  public TodoItem add(TodoItem newTodoItem) {
    newTodoItem.setItemStatus(newTodoItem.getItemStatus());
    newTodoItem.setCreationDateTime(LocalDateTime.now());
    validateTodoItem(newTodoItem);
    return todoItemRepository.persist(newTodoItem);
  }

  @Override
  @Transactional
  public Optional<TodoItem> partiallyUpdate(
      Long id, TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto
  ) throws ItemModificationForbiddenException {
    Optional<TodoItem> foundTodoItemOptional = todoItemRepository.findById(id);
    if(foundTodoItemOptional.isEmpty())
      return Optional.empty();
    TodoItem foundTodoItem = foundTodoItemOptional.get();
    if(todoItemPartiallyUpdateDto.description() != null)
      foundTodoItem.setDescription(todoItemPartiallyUpdateDto.description());
    if(todoItemPartiallyUpdateDto.itemStatus() != null)
      foundTodoItem.setItemStatus(todoItemPartiallyUpdateDto.itemStatus());
    validateItemStatus(foundTodoItem.getItemStatus(),id);
    validateTodoItem(foundTodoItem);

    return Optional.of(todoItemRepository.persist(foundTodoItem));
  }

  @Override
  public List<TodoItem> getAllItems(int page, int size) {
    return Collections.emptyList();
  }

  @Override
  public List<TodoItem> getNotDoneItems(int page, int size) {
    return Collections.emptyList();
  }

  @Override
  public Optional<TodoItem> getItemById(Long id) {
    return Optional.empty();
  }
  private void validateTodoItem(TodoItem todoItem) {
    switch (todoItem.getItemStatus())
    {
      case PAST_DUE -> pastDueValidation(todoItem);
      case DONE -> doneValidation(todoItem);
      case NOT_DONE -> notDoneValidation(todoItem);
    }
  }

  private void notDoneValidation(TodoItem todoItem) {
    if(todoItem.getMarkAsDoneDateTime()!=null)
      throw new IllegalArgumentException("The item in not-done status could not have marked as done date-time");
  }

  private void doneValidation(TodoItem todoItem) {
    if(isNull(todoItem.getMarkAsDoneDateTime()))
      throw new IllegalArgumentException("The item in the done status should have the mark as done date-time");
    if(LocalDateTime.now().isBefore(todoItem.getMarkAsDoneDateTime()))
      throw new IllegalArgumentException("The item in the done status could not have date-time in the future");
  }

  private void pastDueValidation(TodoItem todoItem) {
    if(LocalDateTime.now().isBefore(todoItem.getDueDateTime()))
      throw new IllegalArgumentException("In the past-due items, the due date-time could not be in the future");
  }

  private void validateItemStatus(ItemStatus itemStatus, Long id) throws ItemModificationForbiddenException {
    if(ItemStatus.PAST_DUE.equals(itemStatus))
      throw new ItemModificationForbiddenException(id);
  }
}
