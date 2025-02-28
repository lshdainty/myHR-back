package com.lshdainty.myhr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrApplication.class, args);
    }

}
