package com.ironhack.vbnk_authenticationservice;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@Tag(name = "vBNK Authentication Service")
public class vBNKAuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(vBNKAuthenticationServiceApplication.class, args);
    }

}
