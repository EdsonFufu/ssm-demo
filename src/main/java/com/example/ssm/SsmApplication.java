package com.example.ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;

@EnableStateMachine
@SpringBootApplication
public class SsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsmApplication.class, args);
	}

}
