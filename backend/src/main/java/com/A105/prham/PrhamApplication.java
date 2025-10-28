package com.A105.prham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class PrhamApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrhamApplication.class, args);
	}

}
