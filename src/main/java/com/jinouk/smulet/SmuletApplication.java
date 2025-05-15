package com.jinouk.smulet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SmuletApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmuletApplication.class, args);
    }
}
