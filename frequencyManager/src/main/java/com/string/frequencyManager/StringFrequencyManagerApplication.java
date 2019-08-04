package com.string.frequencyManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application to implement a small SaaS called String Frequency Manager
 *
 * @author Kamna
 * @see com.spring.FrequencyManagerApplication
 */

@SpringBootApplication
@ComponentScan
@EnableScheduling
public class StringFrequencyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StringFrequencyManagerApplication.class, args);
	}

}
