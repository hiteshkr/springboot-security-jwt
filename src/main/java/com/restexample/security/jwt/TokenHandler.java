package com.restexample.security.jwt;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.restexample.dao.User;
import com.restexample.security.UserService;

public final class TokenHandler {
	
	private final String secret;
	private final UserService userService;
	
	public TokenHandler(String secret, UserService userService)
	{
		this.secret = secret;
		this.userService = userService;
	}
	
	public User parseUserFromToken(String token)
	{
		System.out.println("SECRET:" + secret);
		String username = Jwts.parser()
							.setSigningKey(secret)
							.parseClaimsJws(token)
							.getBody()
							.getSubject();
		return userService.loadUserByUsername(username);
	}
	
	public String createTokenForUser(User user)
	{
		System.out.println("secret:" + secret);
		return Jwts.builder()
				.setSubject(user.getUsername())
				.setExpiration(new Date(user.getExpires()))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
}
