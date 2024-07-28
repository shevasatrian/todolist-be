package com.sheva.todolist.repository;

import com.sheva.todolist.entity.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ToDoListRepository extends JpaRepository<ToDoList, Integer> {

    Optional<ToDoList> findFirstById(Integer id);

    List<ToDoList> findByUserId(Integer userId);

    List<ToDoList> findByNote(String note);
}
