package com.example.be_datn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeDatnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeDatnApplication.class, args);
    }

}
