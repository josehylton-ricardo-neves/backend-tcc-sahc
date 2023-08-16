package br.edu.ifpb.sahc.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.*;

import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

	//http://localhost:8080/swagger-ui/#/

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
	
	
	@SuppressWarnings("rawtypes")
	List <VendorExtension> vendorExtensions = new ArrayList <>() ;
	
	private ApiInfo apiInfo() {
		return new ApiInfo(
                "Spring Boot Blog REST APIs",
                "Spring Boot Blog REST API Documentation",
                "1",
                "Terms of service",
                new Contact("Josehylton Neves e Italo Neves", null, "ricardo.neves@academico.ifpb.edu.br; neves.italo@academico.ifpb.edu.br"),
                "License of API",
                "API license URL",
                vendorExtensions
				);
	  }
	
	private ApiKey apiKey() { 
	    return new ApiKey("JWT", "Authorization", "header"); 
	}
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	}
	
	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
}
