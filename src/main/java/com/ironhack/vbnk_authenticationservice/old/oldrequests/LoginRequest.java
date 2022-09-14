package com.ironhack.vbnk_authenticationservice.http.requests;

import lombok.Getter;

@Getter
public class LoginRequest {

    String username;
    String password;
}