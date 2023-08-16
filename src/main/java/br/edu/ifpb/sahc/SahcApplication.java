package br.edu.ifpb.sahc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import br.edu.ifpb.sahc.config.CriarAdmin;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SahcApplication implements CommandLineRunner{
	
	@Autowired
	private CriarAdmin criarAdmin;

	public static void main(String[] args) {
		SpringApplication.run(SahcApplication.class, args);
	}
	

	@Override
	public void run(String... args) throws Exception {
		criarAdmin.run();
	}

}
