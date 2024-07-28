package com.sheva.todolist.service;

import com.sheva.todolist.entity.ToDoList;
import com.sheva.todolist.entity.User;
import com.sheva.todolist.model.CreateToDoListRequest;
import com.sheva.todolist.model.ToDoListResponse;
import com.sheva.todolist.model.UpdateToDoListRequest;
import com.sheva.todolist.repository.ToDoListRepository;
import com.sheva.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ToDoListService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private ValidationService validationService;

//    Create
    @Transactional
    public ToDoListResponse create(User user, CreateToDoListRequest request) {
        validationService.validate(request);

        ToDoList toDoList = new ToDoList();
        toDoList.setNote(request.getNote());
        toDoList.setUser(user);

        toDoListRepository.save(toDoList);
        return toToDoListResponse(toDoList);
    }

    private ToDoListResponse toToDoListResponse(ToDoList toDoList) {
        return ToDoListResponse.builder()
                .id(toDoList.getId())
                .note(toDoList.getNote())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ToDoListResponse> list(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        List<ToDoList> toDoLists = toDoListRepository.findAllByUser(user);
        return toDoLists.stream().map(this::toToDoListResponse).toList();
    }

    @Transactional(readOnly = true)
    private ToDoListResponse get(User user, Integer id) {
        ToDoList toDoList = toDoListRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        return toToDoListResponse(toDoList);
    }

    @Transactional
    public ToDoListResponse update(User user, UpdateToDoListRequest request) {
        validationService.validate(request);

        ToDoList toDoList = toDoListRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        toDoList.setNote(request.getNote());
        toDoListRepository.save(toDoList);

        return toToDoListResponse(toDoList);
    }

    @Transactional
    public void delete(User user, Integer toDoListId) {
        ToDoList toDoList = toDoListRepository.findFirstByUserAndId(user, toDoListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        toDoListRepository.delete(toDoList);
    }
}
