package com.ironhack.vbnk_authenticationservice.http.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
@Schema(name = "Authentication Token Request")
public class LoginRequest {

    private String username;
    private String password;

}