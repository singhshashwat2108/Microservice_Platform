package com.queryhub.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.queryhub.view", "com.queryhub.common"})
public class ViewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViewServiceApplication.class, args);
    }
}
