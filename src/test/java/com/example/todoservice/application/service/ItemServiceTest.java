package com.example.todoservice.application.service;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.application.exception.ItemMissMatchedValuesException;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.application.exception.ItemNotFoundException;
import com.example.todoservice.application.port.TodoItemRepositoryPort;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.todoservice.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {

  @Mock
  private TodoItemRepositoryPort todoItemRepositoryPort;

  @InjectMocks
  private ItemService itemService;

  @Test
  void givenNewTodoItem_add_itemAddedWithID() throws ItemMissMatchedValuesException {
    TodoItem newTodoItem = testItemNotDoneWithoutId();
    TodoItem givenSavedItem = testItemNotDone();

    when(todoItemRepositoryPort.persist(newTodoItem)).thenReturn(givenSavedItem);

    TodoItem result = itemService.add(newTodoItem);

    assertEquals(givenSavedItem.getId(), result.getId());
  }

  @Test
  void givenPastDueNewTodoItem_add_exceptionExpected() {
    String expectedExceptionMessage = "Inserting or updating an item with past-due state is not possible";

    ItemMissMatchedValuesException receivedException = assertThrows(
        ItemMissMatchedValuesException.class, () -> itemService.add(testItemPastDue()));

    assertEquals(expectedExceptionMessage, receivedException.getMessage());
  }

  @Test
  void givenNotDoneWrongNewTodoItem_add_exceptionExpected() {
    String expectedExceptionMessage = "The item in not-done status could not have marked as done date-time";

    ItemMissMatchedValuesException receivedException = assertThrows(
        ItemMissMatchedValuesException.class, () -> itemService.add(testItemNotDoneWrongValue()));

    assertEquals(expectedExceptionMessage, receivedException.getMessage());
  }

  @Test
  void givenDoneWithNullMarkAsDoneDateTimeNewTodoItem_add_exceptionExpected() {
    String expectedExceptionMessage = "The item in the done status should have the mark as done date-time";

    ItemMissMatchedValuesException receivedException = assertThrows(
        ItemMissMatchedValuesException.class,
        () -> itemService.add(testItemDoneWithNullMarkAsDoneDateTimeValue())
    );

    assertEquals(expectedExceptionMessage, receivedException.getMessage());
  }

  @Test
  void givenDoneWithInvalidMarkAsDoneDateTimeNewTodoItem_add_exceptionExpected() {
    String expectedExceptionMessage = "The item in the done status could not have date-time in the future";

    ItemMissMatchedValuesException receivedException = assertThrows(
        ItemMissMatchedValuesException.class,
        () -> itemService.add(testItemDoneWithInvalidMarkAsDoneDateTimeValue())
    );

    assertEquals(expectedExceptionMessage, receivedException.getMessage());
  }

  @Test
  void givenTodoItemPartiallyUpdateDoneDto_partiallyUpdate_itemUpdated() throws ItemMissMatchedValuesException, ItemModificationForbiddenException, ItemNotFoundException {
    TodoItem givenSavedItem = testItemNotDone();
    TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto = todoItemPartiallyUpdateDoneDto();

    when(todoItemRepositoryPort.findById(givenSavedItem.getId())).thenReturn(
        Optional.of(givenSavedItem));
    when(todoItemRepositoryPort.persist(givenSavedItem)).thenReturn(givenSavedItem);

    TodoItem result = itemService.partiallyUpdate(givenSavedItem.getId(),
        todoItemPartiallyUpdateDto
    );

    assertEquals(givenSavedItem.getId(), result.getId());
    assertNotNull(result.getMarkAsDoneDateTime());
  }

  @Test
  void givenTodoItemPartiallyUpdateNotDoneDto_partiallyUpdate_itemUpdated() throws ItemMissMatchedValuesException, ItemModificationForbiddenException, ItemNotFoundException {
    TodoItem givenSavedItem = testItemDone();
    TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto = todoItemPartiallyUpdateNotDoneDto();

    when(todoItemRepositoryPort.findById(givenSavedItem.getId())).thenReturn(
        Optional.of(givenSavedItem));
    when(todoItemRepositoryPort.persist(givenSavedItem)).thenReturn(givenSavedItem);

    TodoItem result = itemService.partiallyUpdate(givenSavedItem.getId(),
        todoItemPartiallyUpdateDto
    );

    assertEquals(givenSavedItem.getId(), result.getId());
    assertNull(result.getMarkAsDoneDateTime());
  }

  @Test
  void givenTodoItemPartiallyUpdateDtoForPastDueItem_partiallyUpdate_exceptionExpected() {
    TodoItem givenSavedItem = testItemPastDue();
    TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDto = todoItemPartiallyUpdateNotDoneDto();

    when(todoItemRepositoryPort.findById(givenSavedItem.getId())).thenReturn(
        Optional.of(givenSavedItem));

    assertThrows(ItemModificationForbiddenException.class,
        () -> itemService.partiallyUpdate(givenSavedItem.getId(), todoItemPartiallyUpdateDto)
    );
  }

  @Test
  void givenSavedTodoItems_getAllItems_itemsFund() {
    int page = 0;
    int size = 10;
    List<TodoItem> expectedItems = Arrays.asList(testItemNotDone(), testItemNotDone());

    when(todoItemRepositoryPort.findAll(page, size)).thenReturn(expectedItems);

    List<TodoItem> actualItems = itemService.getAllItems(page, size);

    verify(todoItemRepositoryPort).findAll(page, size);
    assertEquals(expectedItems, actualItems);
  }

  @Test
  void givenSavedTodoItems_getNotDoneItems_itemsFund() {
    int page = 0;
    int size = 10;
    List<TodoItem> expectedItems = Arrays.asList(testItemNotDone(), testItemNotDone());

    when(todoItemRepositoryPort.findAllNotDone(page, size)).thenReturn(expectedItems);

    List<TodoItem> actualItems = itemService.getNotDoneItems(page, size);

    verify(todoItemRepositoryPort).findAllNotDone(page, size);
    assertEquals(expectedItems, actualItems);
  }

  @Test
  void givenSavedTodoItem_getItemById_itemFund() {
    TodoItem expectedItem = testItemNotDone();

    when(todoItemRepositoryPort.findById(expectedItem.getId())).thenReturn(
        Optional.of(expectedItem));

    Optional<TodoItem> actualItem = itemService.getItemById(expectedItem.getId());

    verify(todoItemRepositoryPort).findById(expectedItem.getId());
    assertEquals(expectedItem, actualItem.get());
  }

  @Test
  void givenItemsWithOverDueDateTime_UpdateStatusOfOverdueItems_ItemsUpdated() {
    List<TodoItem> overdueItems = Arrays.asList(testItemOverDue(), testItemOverDue());

    when(todoItemRepositoryPort.findByDueDateTimeBeforeAndItemStatusNot(any(LocalDateTime.class),
        eq(ItemStatus.PAST_DUE)
    )).thenReturn(overdueItems);

    itemService.updateStatusOfOverdueItems();

    verify(todoItemRepositoryPort).findByDueDateTimeBeforeAndItemStatusNot(
        any(LocalDateTime.class), eq(ItemStatus.PAST_DUE));

    for (TodoItem item : overdueItems) {
      assertEquals(ItemStatus.PAST_DUE, item.getItemStatus());
    }

    verify(todoItemRepositoryPort).saveAll(overdueItems);
  }
}