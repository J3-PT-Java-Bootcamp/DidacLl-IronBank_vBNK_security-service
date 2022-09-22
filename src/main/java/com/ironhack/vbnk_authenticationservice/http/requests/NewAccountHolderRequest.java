package com.ironhack.vbnk_authenticationservice.http.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class NewAccountHolderRequest {
    private String id;
    private String username,email,firstname,lastname;
    private LocalDate dateOfBirth;

    private String mainStreet, mainCity, mainCountry, mainAdditionalInfo;
    private String mailStreet, mailCity, mailCountry, mailAdditionalInfo;
    private Integer mainStreetNumber, mainZipCode;
    private Integer mailStreetNumber, mailZipCode;
    
    public static NewAccountHolderRequest fromRequest(CreateUserRequest request){
        return new NewAccountHolderRequest().setUsername(request.getUsername())
                .setEmail(request.getEmail()).setFirstname(request.getFirstname()).setLastname(request.getLastname())
                .setDateOfBirth(LocalDate.of(request.getYearOfBirth(),request.getMonthOfBirth(), request.getDayOfBirth()))
                .setMainStreet(request.getMainStreet()).setMainCity(request.getMainCity())
                .setMainCountry(request.getMainCountry()).setMainAdditionalInfo(request.getMainAdditionalInfo())
                .setMainStreetNumber(request.getMainStreetNumber()).setMainZipCode(request.getMainZipCode())
                .setMailStreet(request.getMailStreet()).setMailCity(request.getMailCity())
                .setMailCountry(request.getMailCountry()).setMailAdditionalInfo(request.getMailAdditionalInfo())
                .setMailStreetNumber(request.getMailStreetNumber()).setMailZipCode(request.getMailZipCode());
    }
}
