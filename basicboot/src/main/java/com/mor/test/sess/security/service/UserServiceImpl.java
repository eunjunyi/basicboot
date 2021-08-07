package com.mor.test.sess.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mor.test.sess.login.dao.LoginMapper;
import com.mor.test.sess.vo.LoginLog;
import com.mor.test.session.vo.SessionDto;

@Service
public class UserServiceImpl implements SecurityService {
	
	//@Qualifier("com.mor.test.sess.login.dao.LoginMapper") 
	//@Resource (name="loingMapper")
	@Autowired
	LoginMapper loingMapper;
	
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    	SessionDto member = loingMapper.getSelectMeberInfo(id);
         List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         
         if(member != null) {
             authorities.add(new SimpleGrantedAuthority(member.getUserRole()));
             member.setAuthorities(authorities);
         }
         System.out.println("authorities:"+authorities);
         return member;
    }
    
    @Override
	public int setInsertMember(SessionDto member) throws Exception{
		return loingMapper.setInsertMember(member);
	}
  

    @Override
	public SessionDto getSelectMeberInfo(String id) throws Exception{
		return loingMapper.getSelectMeberInfo(id);
	}
	
    
    @Override
	public int setInsertLoginLog(LoginLog loginLog) throws Exception{
		return loingMapper.setInsertLoginLog(loginLog);
	}
	
    @Override
	public int setUpdatePasswordLockCnt(String id) throws Exception{
		return loingMapper.setUpdatePasswordLockCnt(id);
	}
	
    @Override
	public int setUpdatePasswordLockCntReset(String id) throws Exception{
		return loingMapper.setUpdatePasswordLockCntReset(id);
	}

}
