package com.example.todoservice;

import com.example.todoservice.application.dto.TodoItemPartiallyUpdateDto;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;

import java.time.LocalDateTime;

public class TestDataProvider {
  public static TodoItem testItemOverDue() {
    return new TodoItem(1L, "Test description", ItemStatus.NOT_DONE, LocalDateTime.now(),
        LocalDateTime.now().minusDays(1L), null
    );
  }

  public static TodoItemPartiallyUpdateDto todoItemPartiallyUpdateDoneDto() {
    return new TodoItemPartiallyUpdateDto("Update description", ItemStatus.DONE);
  }

  public static TodoItemPartiallyUpdateDto todoItemPartiallyUpdateNotDoneDto() {
    return new TodoItemPartiallyUpdateDto("Update description", ItemStatus.NOT_DONE);
  }

  public static TodoItem testItemDoneWithInvalidMarkAsDoneDateTimeValue() {
    return new TodoItem(1L, "Test description", ItemStatus.DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), LocalDateTime.now().plusDays(1L)
    );
  }

  public static TodoItem testItemDoneWithNullMarkAsDoneDateTimeValue() {
    return new TodoItem(1L, "Test description", ItemStatus.DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), null
    );
  }

  public static TodoItem testItemNotDone() {
    return new TodoItem(1L, "Test description", ItemStatus.NOT_DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), null
    );
  }

  public static TodoItem testItemDone() {
    return new TodoItem(1L, "Test description", ItemStatus.DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), LocalDateTime.now()
    );
  }

  public static TodoItem testItemPastDue() {
    return new TodoItem(1L, "Test description", ItemStatus.PAST_DUE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), null
    );
  }

  public static TodoItem testItemNotDoneWrongValue() {
    return new TodoItem(1L, "Test description", ItemStatus.NOT_DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), LocalDateTime.now()
    );
  }

  public static TodoItem testItemNotDoneWithoutId() {
    return new TodoItem(null, "Test description", ItemStatus.NOT_DONE, LocalDateTime.now(),
        LocalDateTime.now().plusDays(1L), null
    );
  }

}
