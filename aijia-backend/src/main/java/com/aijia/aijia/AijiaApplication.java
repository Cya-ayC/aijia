package com.aijia.aijia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aijia.aijia.mapper")
public class AijiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AijiaApplication.class, args);
    }

}
