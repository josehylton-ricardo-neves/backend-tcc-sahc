package br.edu.ifpb.sahc.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;

import br.edu.ifpb.sahc.service.UsuarioDetailServiceImpl;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class JWTValidacaoFilter extends OncePerRequestFilter {
	public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String PREFIX_ATTRIBUTE = "Bearer ";
    
    private final UsuarioDetailServiceImpl userDetailServiceImpl;
	
	public JWTValidacaoFilter(UsuarioDetailServiceImpl userDetailServiceImpl) {
		this.userDetailServiceImpl = userDetailServiceImpl;
	}
	
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(HEADER_ATTRIBUTE);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!token.startsWith(PREFIX_ATTRIBUTE)) {
            chain.doFilter(request, response);
            return;
        }

        token = token.replace(PREFIX_ATTRIBUTE, "");
        
        
        
        try {
        	
        	String user = JWT.require(Algorithm.HMAC512(JWTAutenticacaoFilter.TOKEN_CODE))
                    .build()
                    .verify(token)
                    .getSubject();
        	
        	UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(user);
        	
        	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null, userDetails.getAuthorities());
        	
        	//authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        	System.out.println("TESTE TOKEN: " + authenticationToken);
        	
        	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        	
        	chain.doFilter(request, response);
        	
        }catch (TokenExpiredException e) {
        	System.out.println("OCORREU UM ERRO NO TOKEN");
			throw new TokenExpiredException("Token invalido " + e);
		}
        
        //UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
    
    }

	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        String user = JWT.require(Algorithm.HMAC512(JWTAutenticacaoFilter.TOKEN_CODE)).build().verify(token).getSubject();

        if (user == null) {
            return null;
        }
        
        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(user);
        return new UsernamePasswordAuthenticationToken(user,null, userDetails.getAuthorities());
    }
}
