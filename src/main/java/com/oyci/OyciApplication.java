package com.oyci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OyciApplication {
    public static void main(String[] args) {
        SpringApplication.run(OyciApplication.class, args);
    }
}
