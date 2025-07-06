package com.springboot.mofabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})   // 확실하게 h2 제한
@ComponentScan(basePackages = "com.springboot")
public class MofaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MofaBackendApplication.class, args);
    }

}
