/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE              PERSON             	REASON
 *  1     2014-11-19         杜志星 (380173)      创建 
 ********************************************************/
package com.sf.module.esbinterface.biz;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.esbinterface.dao.ISap2SpmsDataJdbcDao;
import com.sf.module.esbinterface.domain.Sap2SpmsData;
import com.sf.module.esbinterface.util.Sap2SpmsUtil;

/**
 * SAP数据整理BIZ
 * @author 杜志星 (380173) 2014-11-19 
 */
public class Sap2SpmsDataBiz extends BaseBiz implements ISap2SpmsDataBiz {

	private ISap2SpmsDataJdbcDao sap2SpmsDataJdbcDao;
	
	public void sap2SpmsDataDone(Sap2SpmsData model) {
		
		if(model != null) {
			//人员信息处理
			if(Sap2SpmsUtil.HCM_OUT_EMP.equals(model.getDataType())) {
				sap2SpmsDataJdbcDao.sapEmpDone();
			}
		}
	}

	public void setSap2SpmsDataJdbcDao(ISap2SpmsDataJdbcDao sap2SpmsDataJdbcDao) {
		this.sap2SpmsDataJdbcDao = sap2SpmsDataJdbcDao;
	}
}
