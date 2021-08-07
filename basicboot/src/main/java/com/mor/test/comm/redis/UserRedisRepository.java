package com.mor.test.comm.redis;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mor.test.session.vo.SessionDto;

@Repository
public interface UserRedisRepository extends CrudRepository<SessionDto, String> {
	
	  SessionDto findByUserIdIgnoreCase(String userId);
	  void delectByUserIdIgnoreCase(String userId);
}
