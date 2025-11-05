package com.A105.prham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PrhamApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrhamApplication.class, args);
	}


}