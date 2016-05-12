package com.restexample.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.restexample.security.StatelessAuthenticationFilter;
import com.restexample.security.StatelessLoginFilter;
import com.restexample.security.UserService;
import com.restexample.security.jwt.TokenAuthenticationService;

@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;
	private TokenAuthenticationService tokenAuthenticationService;
	
	@Autowired
	@Value("${secretkey}")
	private String key;
	
	public SecurityConfig()
	{
		super(true);
		this.userService = new UserService();
	}
	
	@PostConstruct
	public void init()
	{
		tokenAuthenticationService = new TokenAuthenticationService(key, userService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling()
				.and()
			.anonymous()
				.and()
			.servletApi()
				.and()
			.authorizeRequests()
			
			.antMatchers("/")
				.permitAll()
				
			//allow anonymous POSTs to login
			.antMatchers(HttpMethod.POST, "/api/login")
				.permitAll()
				
			//allow anonymous GETs to API
			.antMatchers(HttpMethod.GET, "/api/**")
				.permitAll()
			
			//defined Admin only API area
			.antMatchers("/admin/**")
				.hasRole("ADMIN")
			
			//all other request need to be authenticated
			.anyRequest()
				.hasRole("USER")
				.and()
			
			// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
			.addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, userService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

			// custom Token based authentication based on the header previously given to the client
			.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);				
			
//			.csrf().disable()
//			.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//			.formLogin()
//				.and()
//			.httpBasic();
	}
	
	@Bean
    @Override
    protected UserService userDetailsService() {
        return userService;
    }
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth
		.userDetailsService(userService)
		.passwordEncoder(new BCryptPasswordEncoder());
//		.inMemoryAuthentication()
//			.withUser("user").password("password").roles("USER");
	}
	
	@Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return tokenAuthenticationService;
    }
}
