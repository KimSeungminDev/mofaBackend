package com.springboot.mofabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.springboot") // <- 이거 추가!
public class MofaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MofaBackendApplication.class, args);
    }

}
