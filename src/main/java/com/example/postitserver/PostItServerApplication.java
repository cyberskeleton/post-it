package com.example.postitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sofia Novikova on 2021-12-21
 */
@SpringBootApplication
@EnableScheduling
public class PostItServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostItServerApplication.class, args);
    }

}
