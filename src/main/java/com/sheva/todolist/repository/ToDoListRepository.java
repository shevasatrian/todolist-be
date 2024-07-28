package com.sheva.todolist.repository;

import com.sheva.todolist.entity.ToDoList;
import com.sheva.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ToDoListRepository extends JpaRepository<ToDoList, Integer> {

    Optional<ToDoList> findFirstById(Integer id);

    Optional<ToDoList> findFirstByUserAndId(User user, Integer id);

    List<ToDoList> findAllByUser(User user);

    List<ToDoList> findByUserId(Integer userId);

    List<ToDoList> findByNote(String note);
}
