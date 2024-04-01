package com.example.todoservice.infrastructure.out.persistence.adaptor;

import com.example.todoservice.application.port.TodoItemRepositoryPort;
import com.example.todoservice.infrastructure.out.persistence.entity.ToDoItemEntity;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoItemRepositoryPortAdaptor implements TodoItemRepositoryPort {

  private final TodoItemJPARepository todoItemJPARepository;

  @Override
  public TodoItem persist(TodoItem todoItem) {
    ToDoItemEntity toDoItemEntity = new ToDoItemEntity(todoItem);
    return todoItemJPARepository.save(toDoItemEntity).toModel();
  }

  @Override
  public Optional<TodoItem> findById(Long id) {
    return todoItemJPARepository.findById(id).map(ToDoItemEntity::toModel);
  }

  @Override
  public List<TodoItem> findAll(int page, int size) {
    return todoItemJPARepository.findAll(PageRequest.of(page, size)).stream()
        .map(ToDoItemEntity::toModel).toList();
  }

  @Override
  public List<TodoItem> findAllNotDone(int page, int size) {
    return todoItemJPARepository.findByItemStatus(ItemStatus.NOT_DONE, PageRequest.of(page, size))
        .stream().map(ToDoItemEntity::toModel).toList();
  }

  @Override
  public List<TodoItem> findByDueDateTimeBeforeAndItemStatusNot(
      LocalDateTime currentDateTime, ItemStatus itemStatus
  ) {
    return todoItemJPARepository.findByDueDateTimeBeforeAndItemStatusNot(
        currentDateTime, itemStatus);
  }

  @Override
  public void saveAll(List<TodoItem> overdueItems) {
    todoItemJPARepository.saveAll(overdueItems.stream().map(ToDoItemEntity::new).toList());
  }
}
