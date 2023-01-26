package br.edu.ifpb.sahc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.edu.ifpb.sahc.security.JWTAutenticacaoFilter;
import br.edu.ifpb.sahc.service.UsuarioService;

//@Configuration
public class SecurityConfig {
	
	/*private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	
	public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}*/
	
	//@Bean
	/*
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
		http
		.csrf().disable()
		.authorizeHttpRequests().antMatchers("/").permitAll().and().authorizeHttpRequests().anyRequest().authenticated();//.and()
		//.addFilter(new JWTAuthenticateFilter());
		return http.build();
	}*/
	
	/*@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}*/
	
	/*@Bean
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}*/
	
}
