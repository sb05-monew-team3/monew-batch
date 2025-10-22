package com.monew.monew_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonewBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonewBatchApplication.class, args);
		System.out.println("localhost:8082");
	}

}
