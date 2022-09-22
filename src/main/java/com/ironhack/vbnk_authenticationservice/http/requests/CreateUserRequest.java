package com.ironhack.vbnk_authenticationservice.http.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Schema(name = "New User Request")
public class CreateUserRequest {
    private String username,password,email,firstname,lastname;
    private int dayOfBirth,monthOfBirth,yearOfBirth;
    private String mainStreet, mainCity, mainCountry, mainAdditionalInfo;
    private String mailStreet, mailCity, mailCountry, mailAdditionalInfo;
    private Integer mainStreetNumber, mainZipCode;
    private Integer mailStreetNumber, mailZipCode;
}
