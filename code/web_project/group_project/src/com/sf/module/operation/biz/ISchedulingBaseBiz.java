/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 班次基础信息管理的业务接口
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public interface ISchedulingBaseBiz extends IBiz {

	public boolean saveScheduling(HashMap map);

	public HashMap querySchedule(HashMap map);

	public boolean updateSchedule(HashMap map);

	public List<SchedulingBase> getScheBaseList(Long deptid,String ym);
	
	public Map isScheduling(JSONArray jsonList);
	
	public boolean deleteSchedule(HashMap map);
	
	public HashMap exportSchedule(HashMap map);

	public HashMap importSchedule(HashMap map);
	
}