package com.ironhack.vbnk_authenticationservice.http.requests;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateUserRequest {
    private String username,password,email,firstname,lastname;
    private String dateOfBirth;
    private String mainStreet, mainCity, mainCountry, mainAdditionalInfo;
    private String mailStreet, mailCity, mailCountry, mailAdditionalInfo;
    private Integer mainStreetNumber, mainZipCode;
    private Integer mailStreetNumber, mailZipCode;
}
