package com.example.todoservice.infrastructure.out.persistence.adaptor;

import com.example.todoservice.infrastructure.out.persistence.entity.ToDoItemEntity;
import com.example.todoservice.model.ItemStatus;
import com.example.todoservice.model.TodoItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoItemJPARepository extends JpaRepository<ToDoItemEntity, Long> {

  List<ToDoItemEntity> findByItemStatus(ItemStatus itemStatus, Pageable pageable);

  List<TodoItem> findByDueDateTimeBeforeAndItemStatusNot(
      LocalDateTime currentDateTime, ItemStatus itemStatus
  );
}
