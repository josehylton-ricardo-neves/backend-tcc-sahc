package br.edu.ifpb.sahc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.edu.ifpb.sahc.security.JWTAutenticacaoFilter;
import br.edu.ifpb.sahc.security.JWTValidacaoFilter;
import br.edu.ifpb.sahc.service.UsuarioDetailServiceImpl;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	    securedEnabled = true,
	    jsr250Enabled = true,
	    prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String[] AUTH_WHITELIST = {
	        "/swagger-resources/**",
	        "/swagger-ui.html",
	        "/v2/api-docs",
	        "/webjars/**"
	};
	
    private final UsuarioDetailServiceImpl userDetailServiceImpl;
	private final PasswordEncoder passwordEncoder;
	private final JWTValidacaoFilter jwtValidateFilter;
	
	public SecurityConfig(UsuarioDetailServiceImpl userDetailServiceImpl, PasswordEncoder passwordEncoder, JWTValidacaoFilter jwtValidateFilter) {
		this.userDetailServiceImpl = userDetailServiceImpl;
		this.passwordEncoder = passwordEncoder;
		this.jwtValidateFilter = jwtValidateFilter;
	}

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceImpl).passwordEncoder(passwordEncoder);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers("/swagger-ui/*").permitAll()
		.antMatchers(HttpMethod.POST, "/cadastrarUsuario").permitAll()
		.antMatchers(HttpMethod.GET, "/coordenacao").permitAll()
		.antMatchers(HttpMethod.POST, "/recuperarSenha").permitAll()
		.antMatchers(HttpMethod.PATCH, "/modificarSenha").hasAnyRole("aluno", "coord")
		.antMatchers(HttpMethod.GET, "/buscarUsuarios").hasAnyRole("admin")
		.antMatchers(HttpMethod.POST, "/cadastrarCoordenacao").hasAnyRole("admin")
		.antMatchers(HttpMethod.PATCH, "/atualizarCoordenacao","/alterarPrivilegiosUsuario").hasAnyRole("admin")
		.antMatchers(HttpMethod.DELETE, "/descadastrarCoordenacao", "/deletarUsuario").hasAnyRole("admin")
		.anyRequest().authenticated()
		.and().addFilter(new JWTAutenticacaoFilter(authenticationManager()))
		.addFilterBefore(jwtValidateFilter, UsernamePasswordAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       
    }
	
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
	    corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
	    corsConfiguration.setExposedHeaders(Arrays.asList("*"));

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers(AUTH_WHITELIST);
	}
	
}
