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

import com.sf.framework.server.base.dao.IJdbcDao;

/**
 * SAP数据整理DAO接口
 * @author 杜志星 (380173) 2014-11-19 
 */
public interface ISap2SpmsDataJdbcDao extends IJdbcDao {

	/**
	 * sap人员数据处理
	 * @author 杜志星 (380173)
	 * @date 2014-11-19
	 */
	void sapEmpDone();
}
