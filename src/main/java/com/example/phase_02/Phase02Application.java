package com.example.phase_02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Controller
public class  Phase02Application {

    public static void main(String[] args) {
        SpringApplication.run(Phase02Application.class, args);
    }

}
