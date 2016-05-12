package com.restexample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restexample.dao.User;
import com.restexample.dao.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
    
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepo.findByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("user not found");
		detailsChecker.check(user);
		return user;
	}

}
