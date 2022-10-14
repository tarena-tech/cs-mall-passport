package com.tarena.passport.auto.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.tarena.passport.auto")
public class AutoTestStarter {
    public static void main(String[] args) {
        SpringApplication.run(AutoTestStarter.class,args);
    }
}
