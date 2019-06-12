package com.module.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.module.core.domain"})
@EntityScan(basePackages = {"com.module.core.domain"})
public class ModuleApiApplication {
    public static void main(String[] args){
        SpringApplication.run(ModuleApiApplication.class, args);
    }
}
