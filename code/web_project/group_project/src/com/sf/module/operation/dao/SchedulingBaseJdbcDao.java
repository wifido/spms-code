/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import com.sf.module.operation.dao.OssJdbcDao;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 班次基础信息管理的JdbcDao实现类
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public class SchedulingBaseJdbcDao extends OssJdbcDao implements
		ISchedulingBaseJdbcDao {

	public List<SchedulingBase> getScheBaseList(Long deptid,String ym) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT SCHEDULE_CODE, SCHEDULE_NAME, DEPT_ID, ENABLE_DT, DISABLE_DT ")
				.append("FROM TM_PB_SCHEDULE_BASE_INFO ")
				.append("WHERE YM = ? ")
				.append("AND DEPT_ID = ?  and CLASS_TYPE = '1' ");
		return this.queryForList(sb.toString(), new Object[] {ym, deptid},
				SchedulingBase.class);
	}
	
}