package com.devspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevSpaceApplication.class, args);
    }
}
