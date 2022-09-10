package com.ironhack.nicetomeetyoukeycloak.http.requests;

import lombok.Getter;

@Getter
public class LoginRequest {

    String username;
    String password;
}