package com.itsurena.usermng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserMngApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMngApplication.class, args);
	}

}
