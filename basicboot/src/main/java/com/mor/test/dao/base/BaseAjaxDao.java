package com.mor.test.dao.base;

import java.util.List;
import java.util.Map;

public interface BaseAjaxDao {
	public int insertAjaxTest(Map inMap);
	public Map getAjaxTest(Map inMap);
	public List<Map> listAjaxTest(Map inMap);
	public int updateAjaxTest(Map inMap);
	public int deleteAjaxTest(Map inMap);
}
