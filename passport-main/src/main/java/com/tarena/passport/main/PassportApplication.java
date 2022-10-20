package com.tarena.passport.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tarena.passport")
public class PassportApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassportApplication.class,args);
    }
}
