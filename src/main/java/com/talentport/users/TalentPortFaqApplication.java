package com.talentport.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.talentport.users.controller.FaqController;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({ FaqController.class })
public class TalentPortFaqApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalentPortFaqApplication.class, args);
	}

}
