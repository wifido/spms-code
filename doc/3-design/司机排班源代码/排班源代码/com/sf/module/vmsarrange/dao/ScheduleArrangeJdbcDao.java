/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.sf.framework.server.base.dao.BaseJdbcDao;

/**
 * 配班管理dao实现类
 * 
 * @author 方芳 (350614) 2014-6-6
 */
public class ScheduleArrangeJdbcDao extends BaseJdbcDao implements IScheduleArrangeJdbcDao {
	private static String calculteMonthPayDetailSql = "{call PKG_ARR_SCHEDULE.STP_SCHEDULE_ARRANGE_NO(?,?)}";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateInvalid(Long[] recordIds,final String empCode){
		if(null == recordIds || recordIds.length < 1){
			return ;
		}
		String recordIdsStr = "";
		for(int i=0;i<recordIds.length;i++){
			Long id = recordIds[i];
			if(null == id){
				continue;
			}
			recordIdsStr += id+",";
		}
		final String ids = recordIdsStr.replaceAll(",$", "");
		super.getJdbcTemplate().execute(calculteMonthPayDetailSql, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement stmt)
					throws SQLException, DataAccessException {			
				stmt.setString(1, ids);
				stmt.setString(2, empCode);
				return stmt.execute();
			}
		});
	}
}
