package com.example.be_datn;

import org.hibernate.annotations.Comment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Comment("com.example.be_datn")
public class BeDatnApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeDatnApplication.class, args);
    }

}
