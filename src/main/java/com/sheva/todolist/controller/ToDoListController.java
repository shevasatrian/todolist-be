package com.sheva.todolist.controller;

import com.sheva.todolist.entity.User;
import com.sheva.todolist.model.CreateToDoListRequest;
import com.sheva.todolist.model.ToDoListResponse;
import com.sheva.todolist.model.UpdateToDoListRequest;
import com.sheva.todolist.repository.UserRepository;
import com.sheva.todolist.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;

    @Autowired
    private UserRepository userRepository;

    // Mendapatkan user dari konteks keamanan
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // username dari UserDetails
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

//    create
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ToDoListResponse> create(
            @RequestBody CreateToDoListRequest request
    ) {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(toDoListService.create(user, request));
    }

// read
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ToDoListResponse>> list() {
        User user = getAuthenticatedUser();
        List<ToDoListResponse> toDoListResponses = toDoListService.list(user.getEmail());
        return ResponseEntity.ok(toDoListResponses);
    }

//    Update
    @PutMapping(
        path = "/{toDoListId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ToDoListResponse> update(
            @RequestBody UpdateToDoListRequest request,
            @PathVariable("toDoListId") Integer toDoListId
    ) {
        request.setId(toDoListId);
        User user = getAuthenticatedUser();
        ToDoListResponse toDoListResponse = toDoListService.update(user, request);
        return ResponseEntity.ok(toDoListResponse);
    }

//    Delete
    @DeleteMapping(
            path = "/{toDoListId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ToDoListResponse> delete(
        @PathVariable("toDoListId") Integer toDoListId
    ) {
        User user = getAuthenticatedUser();
        toDoListService.delete(user, toDoListId);
        ToDoListResponse toDoListResponse = new ToDoListResponse("Note deleted successfully");
        return new ResponseEntity<>(toDoListResponse, HttpStatus.OK);
    }
}
