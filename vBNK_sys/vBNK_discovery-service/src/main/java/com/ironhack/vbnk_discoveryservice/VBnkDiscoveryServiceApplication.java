package com.ironhack.vbnk_discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class VBnkDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VBnkDiscoveryServiceApplication.class, args);
    }

}
