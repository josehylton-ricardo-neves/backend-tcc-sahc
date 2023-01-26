package br.edu.ifpb.sahc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	    securedEnabled = true,
	    jsr250Enabled = true,
	    prePostEnabled = true)
public class SecurityConfigV2 extends WebSecurityConfigurerAdapter{
	
	//@Autowired
    private final UsuarioDetailServiceImpl userDetailServiceImpl;
	//@Autowired
	private final PasswordEncoder passwordEncoder;
	private final JWTValidacaoFilter jwtValidateFilter;
	
	public SecurityConfigV2(UsuarioDetailServiceImpl userDetailServiceImpl, PasswordEncoder passwordEncoder, JWTValidacaoFilter jwtValidateFilter) {
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
        http.csrf().disable().authorizeRequests()
        		.antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/cadastrarUsuario").permitAll()
                .antMatchers(HttpMethod.GET, "/listarUsuario").permitAll()
                .antMatchers("/swagger-ui/index.html").permitAll()
                .antMatchers(HttpMethod.POST, "/recuperarSenha").hasRole("ADMIN")
                //.antMatchers(HttpMethod.POST, "/inserirRole").permitAll()
                .anyRequest().authenticated()
                //.anyRequest().permitAll()
                .and().addFilter(new JWTAutenticacaoFilter(authenticationManager()))
                .addFilterBefore(jwtValidateFilter, UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new JWTValidateFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        //http.addFilterBefore(jwtValidateFilter, UsernamePasswordAuthenticationFilter.class);
    }
	
	
	
	//@Bean
	//public AuthenticationManager authenticationManagerBean() throws Exception {
	//    return super.authenticationManagerBean();
	//}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
	
}
