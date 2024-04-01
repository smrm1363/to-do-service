package com.example.todoservice.infrastructure.in.web;

import com.example.todoservice.application.exception.ItemMissMatchedValuesException;
import com.example.todoservice.application.exception.ItemModificationForbiddenException;
import com.example.todoservice.application.exception.ItemNotFoundException;
import com.example.todoservice.infrastructure.in.web.dto.ToDoItemOutDto;
import com.example.todoservice.infrastructure.in.web.dto.TodoItemInDto;
import com.example.todoservice.infrastructure.in.web.dto.UpdateTodoItemDto;
import com.example.todoservice.model.ItemStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TodoItemIntegrationTest {

  @Autowired
  private TodoItemController controller;

  @Test
  void addItem_ValidInput_ReturnsCreatedResponse() throws ItemMissMatchedValuesException, ItemModificationForbiddenException, ItemNotFoundException {
    TodoItemInDto todoItemInDto = getTodoItemInDto();

    ResponseEntity<ToDoItemOutDto> addResponse = addItemTest(todoItemInDto);
    updateTest(addResponse);
    getAllItemsTest(todoItemInDto);
    getNotDoneItemsTest();
  }

  private void getNotDoneItemsTest() {
    ResponseEntity<List<ToDoItemOutDto>> responseAllItemsNotDone = controller.getNotDoneItems(0,
        10
    );
    assertEquals(1, Objects.requireNonNull(responseAllItemsNotDone.getBody()).size());
  }

  private void getAllItemsTest(TodoItemInDto todoItemInDto) throws ItemMissMatchedValuesException {
    controller.addItem(todoItemInDto);
    ResponseEntity<List<ToDoItemOutDto>> responseAllItems = controller.getAllItems(0, 10);
    assertEquals(2, Objects.requireNonNull(responseAllItems.getBody()).size());
  }

  private void updateTest(
      ResponseEntity<ToDoItemOutDto> addResponse
  ) throws ItemMissMatchedValuesException, ItemModificationForbiddenException, ItemNotFoundException {

    UpdateTodoItemDto updateTodoItemDto = new UpdateTodoItemDto("Updated description",
        ItemStatus.DONE
    );
    ResponseEntity<ToDoItemOutDto> responseUpdate = controller.updateTodoItem(
        Objects.requireNonNull(addResponse.getBody()).id(), updateTodoItemDto);
    assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());
    assertNotNull(responseUpdate.getBody());
    assertEquals(updateTodoItemDto.description(), responseUpdate.getBody().description());
  }

  private ResponseEntity<ToDoItemOutDto> addItemTest(
      TodoItemInDto todoItemInDto
  ) throws ItemMissMatchedValuesException {
    ResponseEntity<ToDoItemOutDto> response = controller.addItem(todoItemInDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    return response;
  }

  private TodoItemInDto getTodoItemInDto() {
    return new TodoItemInDto("test description", ItemStatus.NOT_DONE, LocalDateTime.now(), null);
  }

}