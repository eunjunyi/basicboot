package com.mor.test.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mor.test.dao.base.BaseAjaxDao;
import com.mor.test.domain.base.TestTable;
import com.mor.test.domain.base.TestTableRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MultiDatasourceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private final TestTableRepository testTableRepository;
	
	@Autowired
	BaseAjaxDao baseDao;
	
	public MultiDatasourceController(TestTableRepository testTableRepository) {
	    this.testTableRepository = testTableRepository;
	}
	
    @RequestMapping("/test/jpa")
	@ResponseBody
	public List<TestTable> jpacheck(@RequestParam Map<String, String> param) {
    	
    	TestTable vv= new TestTable();
    	vv.setC2("dd");
    	vv.setC3("asfsf");
    	vv.setInsertDate(new Date());
    	testTableRepository.save(vv);
    	//if(true) throw new RuntimeException("타타타");
        vv= new TestTable();
    	vv.setC2("dd11");
    	vv.setC3("asfsf22");
    	vv.setInsertDate(new Date());
    	testTableRepository.save(vv);
    	//if(true) throw new RuntimeException("타타타");
    	return testTableRepository.findAll();
	}
	    
    
     @ResponseBody 
     @RequestMapping("/test/mybits")
     public Map insertTEST() {
		 logger.debug("##############insertTEST \n");
		 Map retmaP = new HashMap();
		 Map inMap = new HashMap(); 
		 inMap.put("id","3");
		 inMap.put("title","베이스타이틀1");
		 inMap.put("auth","베이스1"); 
		 inMap.put("content","베이스내용1");
		 baseDao.insertAjaxTest(inMap);
		 inMap.put("id","4");
		 inMap.put("title","프라이머리타이틀1"); 
		 inMap.put("auth","프라이1");
		 inMap.put("content","프라이머리내용이다1"); 
		 baseDao.insertAjaxTest(inMap);
		 
		 
		 TestTable vv= new TestTable();
	    	vv.setC2("dda");
	    	vv.setC3("asfsfa");
	    	vv.setInsertDate(new Date());
	    	testTableRepository.save(vv);
	    	
	    	if(true) throw new RuntimeException("타타타");	
		 //if(true) throw new RuntimeException("dhfb"); Map retmaP = new HashMap();
		 retmaP.put("이름","헬로"); 
		 return retmaP; 
		}
    
}
