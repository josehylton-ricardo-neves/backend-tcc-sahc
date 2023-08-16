package br.edu.ifpb.sahc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifpb.sahc.model.Usuario;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JWTAutenticacaoFilter extends UsernamePasswordAuthenticationFilter {
	
	//token com duração de 2 horas
	public static final int EXPIRATION_TOKEN_TIME = 7200000;
	//codigo do token
    public static final String TOKEN_CODE = "463408a1-54c9-4307-bb1c-6cced559f5a7";
    
    
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	private final AuthenticationManager authenticationManager;
    
    public JWTAutenticacaoFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		try {
            Usuario user = new ObjectMapper()
                    .readValue(request.getInputStream(), Usuario.class);
            
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            		user.getMatricula(),
            		user.getSenha(),
            		new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException("User authentication failure", e);
        }
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		UsuarioSecurity userDate = (UsuarioSecurity) authResult.getPrincipal();
		
		String token = JWT.create()
				.withSubject(userDate.getUsername())
				.withClaim("role", userDate.getAuthorities().toString().replace("ROLE_", "").replace("[","").replace("]",""))
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN_TIME))
				.sign(Algorithm.HMAC512(TOKEN_CODE));

		response.setHeader(JWTValidacaoFilter.HEADER_ATTRIBUTE, JWTValidacaoFilter.PREFIX_ATTRIBUTE + token);

	}
	
	
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
	}
}
