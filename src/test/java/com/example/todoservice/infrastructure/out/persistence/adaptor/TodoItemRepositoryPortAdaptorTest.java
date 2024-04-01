package com.example.todoservice.infrastructure.out.persistence.adaptor;

import com.example.todoservice.infrastructure.out.persistence.entity.ToDoItemEntity;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.todoservice.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TodoItemRepositoryPortAdaptorTest {

  @Mock
  private TodoItemJPARepository todoItemJPARepository;

  @InjectMocks
  private TodoItemRepositoryPortAdaptor todoItemRepositoryPortAdaptor;

  @Test
  void givenTodoItem_whenPersisted_thenReturnsSavedTodoItem() {
    TodoItem todoItem = testItemNotDoneWithoutId();
    ToDoItemEntity savedEntity = new ToDoItemEntity(todoItem);
    savedEntity.setId(1L);

    when(todoItemJPARepository.save(any(ToDoItemEntity.class))).thenReturn(savedEntity);
    TodoItem savedTodoItem = todoItemRepositoryPortAdaptor.persist(todoItem);

    assertEquals(savedEntity.toModel(), savedTodoItem);
  }

  @Test
  void givenId_whenFindById_thenReturnsTodoItemOptional() {
    Long id = 1L;
    TodoItem expectedTodoItem = testItemNotDone();
    ToDoItemEntity entity = new ToDoItemEntity(expectedTodoItem);

    when(todoItemJPARepository.findById(id)).thenReturn(Optional.of(entity));
    Optional<TodoItem> result = todoItemRepositoryPortAdaptor.findById(id);

    assertEquals(expectedTodoItem, result.orElse(null));
  }

  @Test
  void givenPageAndSize_whenFindAll_thenReturnsListOfTodoItems() {
    int page = 0;
    int size = 10;
    List<ToDoItemEntity> entities = Arrays.asList(
        new ToDoItemEntity(testItemDone()), new ToDoItemEntity(testItemDone()));
    Page<ToDoItemEntity> pageResult = new PageImpl<>(entities);

    when(todoItemJPARepository.findAll(PageRequest.of(page, size))).thenReturn(pageResult);
    List<TodoItem> result = todoItemRepositoryPortAdaptor.findAll(page, size);

    assertEquals(entities.size(), result.size());
  }

  @Test
  void givenPageAndSize_whenFindAllNotDone_thenReturnsListOfNotDoneTodoItems() {
    int page = 0;
    int size = 10;
    List<ToDoItemEntity> entities = Arrays.asList(
        new ToDoItemEntity(testItemNotDone()), new ToDoItemEntity(testItemNotDone()));
    Page<ToDoItemEntity> pageResult = new PageImpl<>(entities);

    when(todoItemJPARepository.findByItemStatus(ItemStatus.NOT_DONE,
        PageRequest.of(page, size)
    )).thenReturn(pageResult.toList());
    List<TodoItem> result = todoItemRepositoryPortAdaptor.findAllNotDone(page, size);

    assertEquals(entities.size(), result.size());
  }

  @Test
  void givenCurrentDateTimeAndItemStatus_whenFindByDueDateTimeBeforeAndItemStatusNot_thenReturnsListOfTodoItems() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    ItemStatus itemStatus = ItemStatus.PAST_DUE;
    List<TodoItem> expectedItems = Arrays.asList(testItemOverDue(), testItemOverDue());

    when(todoItemJPARepository.findByDueDateTimeBeforeAndItemStatusNot(currentDateTime,
        itemStatus
    )).thenReturn(expectedItems);
    List<TodoItem> result = todoItemRepositoryPortAdaptor.findByDueDateTimeBeforeAndItemStatusNot(
        currentDateTime, itemStatus);

    assertEquals(expectedItems.size(), result.size());
  }

  @Test
  void givenListOfTodoItems_whenSaveAll_thenSavesAllItems() {
    List<TodoItem> overdueItems = Arrays.asList(testItemNotDone(), testItemNotDone());

    todoItemRepositoryPortAdaptor.saveAll(overdueItems);

    verify(todoItemJPARepository, times(1)).saveAll(anyList());
  }
}
