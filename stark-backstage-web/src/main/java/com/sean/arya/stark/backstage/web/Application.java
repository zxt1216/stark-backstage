package com.sean.arya.stark.backstage.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"com.sean.arya.stark.backstage.dao"})
@SpringBootApplication(scanBasePackages = {"com.sean.arya.stark.backstage"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
