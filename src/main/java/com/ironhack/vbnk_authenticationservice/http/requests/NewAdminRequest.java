package com.ironhack.vbnk_authenticationservice.http.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class NewAdminRequest {
    private String id;
    private String userName,email,firstname,lastname;

    
    public static NewAdminRequest fromRequest(CreateUserRequest request){
        return new NewAdminRequest().setUserName(request.getUsername())
                .setEmail(request.getEmail()).setFirstname(request.getFirstname()).setLastname(request.getLastname());
    }
}
