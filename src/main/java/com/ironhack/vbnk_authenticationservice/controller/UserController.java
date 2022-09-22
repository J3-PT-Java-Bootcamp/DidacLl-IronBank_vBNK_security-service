package com.ironhack.vbnk_authenticationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ironhack.vbnk_authenticationservice.config.KeycloakProvider;
import com.ironhack.vbnk_authenticationservice.http.requests.CreateUserRequest;
import com.ironhack.vbnk_authenticationservice.http.requests.LoginRequest;
import com.ironhack.vbnk_authenticationservice.service.KeycloakAdminClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;

@RestController
@RequestMapping("/v1/security")
public class UserController {
    private final KeycloakAdminClientService kcAdminClient;

    private final KeycloakProvider kcProvider;


    public UserController(KeycloakAdminClientService kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }
    @Tag(name = "ping..")
    @GetMapping("/public/{ping}")
    public String ping(@PathVariable(name = "ping") String ping) {
        return ping.replace('i', 'o');
    }

    @Tag(name = "Create New Admin",description = "Create New Admin User")
    @PostMapping( "/dev/create/admin")
    public String createAdmin(@NotNull @RequestBody CreateUserRequest user) throws JsonProcessingException {
        return kcAdminClient.createKeycloakUser(user,true);
    }
    @Tag(name = "Create New AccountHolder",description = "Create New AccountHolder User")
    @PostMapping( "/auth/create")
    public ResponseEntity<String> createUser(@NotNull @RequestBody CreateUserRequest user) throws JsonProcessingException {

        String createdResponse =  kcAdminClient.createKeycloakUser(user,false);
        return ResponseEntity.ok(createdResponse);//.status(createdResponse.getStatus()).build();

    }
    @Tag(name = "Get Authentication Token",description = "Get Authentication Token from Keycloak server")
    @PostMapping("/public/token")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(), loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }


}
