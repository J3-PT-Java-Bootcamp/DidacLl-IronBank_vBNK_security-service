package com.ironhack.vbnk_authenticationservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ironhack.vbnk_authenticationservice.config.KeycloakProvider;
import com.ironhack.vbnk_authenticationservice.http.requests.CreateUserRequest;
import com.ironhack.vbnk_authenticationservice.http.requests.NewAccountHolderRequest;
import com.ironhack.vbnk_authenticationservice.http.requests.NewAdminRequest;
import lombok.extern.java.Log;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
@Log
public class KeycloakAdminClientService {
    private final KeycloakProvider kcProvider;
    @Value("${keycloak.realm}")
    public String realm;
    @Value("${keycloak.resource}")
    public String clientId;

    private static final String TARGET_SERVICE = "vbnk-data-service";
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;
    @Value("${spring.application.name}")
    private String applicationName;
    private WebClient client;

    public KeycloakAdminClientService(KeycloakProvider keycloakProvider) {
        this.kcProvider = keycloakProvider;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public String createKeycloakUser(CreateUserRequest user,boolean isAdmin) throws JsonProcessingException {
        var adminKeycloak = kcProvider.getInstance();
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

//        Change this to change the group logic
        kcUser.setGroups(List.of(isAdmin?"admins":"customers"));


        Response response = usersResource.create(kcUser);

        String userId="";
        if (response.getStatus() == 201) {
            var authentication = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            var tokenString = ((RefreshableKeycloakSecurityContext) authentication).getIdTokenString();
            RealmResource realm1 = adminKeycloak.realm(realm);
            UsersResource users = realm1.users();
            List<UserRepresentation> userList = users.search(kcUser.getUsername()).stream()
                    .filter(userRep -> userRep.getUsername().equals(kcUser.getUsername())).toList();
            var createdUser = userList.get(0);
            userId = createdUser.getId();
            log.info("User with id: " + createdUser.getId() + " created");
            try {
                createClient();
                var val = client.post()
                        .uri(isAdmin?"/v1/data/client/users/new/admin":"/v1/data/client/users/new/account-holder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokenString)
                        .body(isAdmin?
                                (Mono.just(NewAdminRequest.fromRequest(user).setId(createdUser.getId()))):
                                (Mono.just(NewAccountHolderRequest.fromRequest(user).setId(createdUser.getId())))
                                , NewAccountHolderRequest.class)
                        .retrieve().bodyToMono(String.class)
                        .block();

            }catch (Exception e){
                realm1.users().delete(createdUser.getId());
            }
            return createdUser.getId();

        }else{
        }

        return userId;
    }

    void createClient() {
        var serviceInstanceList = discoveryClient.getInstances(TARGET_SERVICE);
        String clientURI = serviceInstanceList.get(0).getUri().toString();
        client = WebClient.create(clientURI);

    }

}