package com.restexample.security.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import com.restexample.dao.User;
import com.restexample.security.UserAuthentication;
import com.restexample.security.UserService;

public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	private static final long TEN_MINUTES = 1000 * 60 * 10;

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService(String secret, UserService userService) {
        tokenHandler = new TokenHandler(secret, userService);
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final User user = authentication.getDetails();
        user.setExpires(new Date().getTime() + TEN_MINUTES);
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
}
