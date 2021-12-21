package com.example.postitserver;

import com.example.postitserver.controllers.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration
@SpringBootTest
class PostItServerApplicationTests {

    @Autowired
    private AdminController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
