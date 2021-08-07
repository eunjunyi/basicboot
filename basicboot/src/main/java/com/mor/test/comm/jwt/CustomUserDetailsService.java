package com.mor.test.comm.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mor.test.session.JwtSessionManager.USER_ROLE;
import com.mor.test.session.vo.SessionDto;
import com.mor.test.comm.redis.UserRedisRepository;

@Service("userUserDetailsService")
public class CustomUserDetailsService  implements UserDetailsService {
	
	@Autowired
	UserRedisRepository userRedisRepository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //CustomUserDetails user = userAuthDAO.getUserById(username);
        System.out.println("loadUserByUsername=>");
    	//권한은 테이블에 적재.
    	Optional<SessionDto> sesOption = userRedisRepository.findById(username);
    	SessionDto sessionDto = null;
        if(sesOption.isPresent()) {
        	sessionDto = sesOption.get();
        	sessionDto.setAuthorities( SessionDto.convertRoles(sessionDto.getUserRole()));
			
        	/* List<GrantedAuthority> roles= new ArrayList<GrantedAuthority>();
			 * roles.add(USER_ROLE.ADMIN.get()); roles.add(USER_ROLE.USER.get());
			 * sessionDto.setAuthorities(roles);*/
			 
        }
        return sessionDto;
    }

}
