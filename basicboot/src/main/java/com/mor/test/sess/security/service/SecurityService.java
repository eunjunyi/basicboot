package com.mor.test.sess.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mor.test.sess.vo.LoginLog;
import com.mor.test.session.vo.SessionDto;

public interface SecurityService extends UserDetailsService {
	
	UserDetails loadUserByUsername(String id);
	
	SessionDto getSelectMeberInfo(String id) throws Exception;
    
    int setInsertMember(SessionDto member)throws Exception;

	int setInsertLoginLog(LoginLog loginLog) throws Exception;
	
	int setUpdatePasswordLockCnt(String id) throws Exception;
	
	int setUpdatePasswordLockCntReset(String id) throws Exception;
	
}
