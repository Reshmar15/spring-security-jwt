package com.reshma.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.reshma.springsecurityjwt.Models.AuthenticationRequest;
import com.reshma.springsecurityjwt.Models.AuthenticationResponse;
import com.reshma.springsecurityjwt.services.MyUserDetailsService;
import com.reshma.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloResource {
	
	@Autowired
	private AuthenticationManager authenticateManager;
	
	private MyUserDetailsService userDetailsService;
	
	private JwtUtil jwtUtil;
	
	@RequestMapping ( value = "/hello",method = RequestMethod.GET)
	public String hello() {
		return "Hello World";
	}
	
	@RequestMapping ( value = "/authenticate",method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception{
		
		try {
		authenticateManager.authenticate(
				new UsernamePasswordAuthenticationToken(
				authRequest.getUserName() , authRequest.getPassword()));
		}catch (BadCredentialsException e) {
			throw new Exception("Incorrect UserName or Password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}

}
