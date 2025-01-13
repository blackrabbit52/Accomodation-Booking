package com.jaynie.accombooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.jaynie.accombooking")
@EnableMongoRepositories
@AutoConfiguration
@EnableCaching
public class AccomBookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccomBookingApplication.class, args);
	}
}

