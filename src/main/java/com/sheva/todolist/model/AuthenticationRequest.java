package com.sheva.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// request authenticate
public class AuthenticationRequest {

    private String email;

    private String password;
}
