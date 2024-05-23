package com.example.manageeducation.trainingclassservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TrainingClassServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingClassServiceApplication.class, args);
	}

}
