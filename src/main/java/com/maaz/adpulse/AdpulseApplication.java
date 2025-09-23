package com.maaz.adpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdpulseApplication.class, args);
	}

}
