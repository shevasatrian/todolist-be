package com.sheva.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoListResponse {

    private Integer id;

    private String note;

    public ToDoListResponse(String noteDeletedSuccessfully) {
    }


}
