package com.mor.test.welcomelist;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mor.test.redis.UserRedisRepository;
import com.mor.test.session.SessionDto;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
	
	@Autowired
	UserRedisRepository userRedisRepository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //CustomUserDetails user = userAuthDAO.getUserById(username);
        
    	Optional<SessionDto> sesOption = userRedisRepository.findById(username);
    	
//        if(sesOption.isEmpty()) {
//            throw new UsernameNotFoundException(username);
//        }
        return sesOption.get();
    }

}
