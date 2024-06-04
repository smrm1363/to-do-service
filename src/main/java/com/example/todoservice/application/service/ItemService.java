package com.example.todoservice.application.service;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.application.exception.ItemMissMatchedValuesException;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.application.exception.ItemNotFoundException;
import com.example.todoservice.application.port.TodoItemRepositoryPort;
import com.example.todoservice.application.usecase.ItemUseCase;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService implements ItemUseCase {

  private final TodoItemRepositoryPort todoItemRepositoryPort;

  @Override
  @Transactional
  public TodoItem add(TodoItem newTodoItem) throws ItemMissMatchedValuesException {
    newTodoItem.setItemStatus(newTodoItem.getItemStatus());
    newTodoItem.setCreationDateTime(LocalDateTime.now());
    validateTodoItem(newTodoItem);
    TodoItem savedItem = todoItemRepositoryPort.persist(newTodoItem);
    log.debug("New todo item added: {}", savedItem);
    return savedItem;
  }

  @Override
  @Transactional
  public TodoItem partiallyUpdate(
      Long id, TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto
  ) throws ItemModificationForbiddenException, ItemMissMatchedValuesException, ItemNotFoundException {
    Optional<TodoItem> foundTodoItemOptional = todoItemRepositoryPort.findById(id);
    if (foundTodoItemOptional.isEmpty()) {
      log.info("Todo item with ID {} not found for partial update.", id);
      throw new ItemNotFoundException(id);
    }
    TodoItem foundTodoItem = foundTodoItemOptional.get();
    validateItemStatus(foundTodoItem.getItemStatus(), id);
    if (todoItemPartiallyUpdateDto.description() != null)
      foundTodoItem.setDescription(todoItemPartiallyUpdateDto.description());
    if (todoItemPartiallyUpdateDto.itemStatus() != null) {
      foundTodoItem.setItemStatus(todoItemPartiallyUpdateDto.itemStatus());
      if (ItemStatus.DONE.equals(todoItemPartiallyUpdateDto.itemStatus()))
        foundTodoItem.setMarkAsDoneDateTime(LocalDateTime.now());
      else if (ItemStatus.NOT_DONE.equals(todoItemPartiallyUpdateDto.itemStatus()))
        foundTodoItem.setMarkAsDoneDateTime(null);
    }
    validateTodoItem(foundTodoItem);
    TodoItem updatedItem = todoItemRepositoryPort.persist(foundTodoItem);
    log.debug("Todo item with ID {} partially updated: {}", id, updatedItem);
    return updatedItem;
  }

  @Override
  public List<TodoItem> getAllItems(int page, int size) {
    return todoItemRepositoryPort.findAll(page, size);
  }

  @Override
  public List<TodoItem> getNotDoneItems(int page, int size) {
    return todoItemRepositoryPort.findAllNotDone(page, size);
  }

  @Override
  public Optional<TodoItem> getItemById(Long id) {
    return todoItemRepositoryPort.findById(id);
  }

  @Scheduled(fixedDelayString = "${spring.application.scheduled.task.update-status.fixed-delay}", initialDelayString = "${spring.application.scheduled.task.update-status.initial-delay}")
  @Async
  @Transactional
  public void updateStatusOfOverdueItems() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    List<TodoItem> overdueItems = todoItemRepositoryPort.findByDueDateTimeBeforeAndItemStatusNot(
        currentDateTime, ItemStatus.PAST_DUE);
    for (TodoItem item : overdueItems) {
      item.setItemStatus(ItemStatus.PAST_DUE);
    }
    todoItemRepositoryPort.saveAll(overdueItems);
    log.info("Updating status of overdue items is finished for {} items", overdueItems.size());
  }

  private void validateTodoItem(TodoItem todoItem) throws ItemMissMatchedValuesException {
    switch (todoItem.getItemStatus()) {
      case PAST_DUE -> pastDueValidation();
      case DONE -> doneValidation(todoItem);
      case NOT_DONE -> notDoneValidation(todoItem);
    }
  }

  private void notDoneValidation(TodoItem todoItem) throws ItemMissMatchedValuesException {
    if (todoItem.getMarkAsDoneDateTime() != null)
      throw new ItemMissMatchedValuesException(
          "The item in not-done status could not have marked as done date-time");
  }

  private void doneValidation(TodoItem todoItem) throws ItemMissMatchedValuesException {
    if (isNull(todoItem.getMarkAsDoneDateTime()))
      throw new ItemMissMatchedValuesException(
          "The item in the done status should have the mark as done date-time");
    if (LocalDateTime.now().isBefore(todoItem.getMarkAsDoneDateTime()))
      throw new ItemMissMatchedValuesException(
          "The item in the done status could not have date-time in the future");
  }

  private void pastDueValidation() throws ItemMissMatchedValuesException {
    throw new ItemMissMatchedValuesException(
        "Inserting or updating an item with past-due state is not possible");
  }

  private void validateItemStatus(
      ItemStatus itemStatus, Long id
  ) throws ItemModificationForbiddenException {
    if (ItemStatus.PAST_DUE.equals(itemStatus))
      throw new ItemModificationForbiddenException(id);
  }
}
