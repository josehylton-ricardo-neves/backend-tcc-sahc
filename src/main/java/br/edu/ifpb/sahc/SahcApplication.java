package br.edu.ifpb.sahc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SahcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SahcApplication.class, args);
	}

}
