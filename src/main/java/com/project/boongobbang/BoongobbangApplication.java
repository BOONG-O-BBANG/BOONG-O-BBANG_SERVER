package com.project.boongobbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoongobbangApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoongobbangApplication.class, args);
	}

}
