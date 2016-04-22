/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE              PERSON             	REASON
 *  1     2014-11-19         杜志星 (380173)      创建 
 ********************************************************/
package com.sf.module.esbinterface.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.sf.framework.server.base.dao.BaseJdbcDao;

/**
 * SAP数据整理DAO
 * @author 杜志星 (380173) 2014-11-19 
 */
public class Sap2SpmsDataJdbcDao extends BaseJdbcDao implements ISap2SpmsDataJdbcDao {

	
	private final static String STP_SAP_2_SPMS_EMP = "";//"{call PKG_GPS_MAP_GPSEC_THIRD.STP_GPSEX_RPT_THIRD(?,?,?,?,?)}";
	
	public void sapEmpDone() {
		
		//System.out.println("---开始处理数据");
		super.getJdbcTemplate().execute(STP_SAP_2_SPMS_EMP, new CallableStatementCallback<Object>(){

			@Override
			public Object doInCallableStatement(CallableStatement stmt)
					throws SQLException, DataAccessException {
				
				//Done 调用存储过程
				return null;
			}
			
		});
	}
}
