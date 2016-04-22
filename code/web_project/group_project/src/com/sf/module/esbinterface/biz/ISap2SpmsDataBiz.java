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

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.esbinterface.domain.Sap2SpmsData;

/**
 * SAP数据整理BIZ
 * @author 杜志星 (380173) 2014-11-19 
 */
public interface ISap2SpmsDataBiz extends IBiz {

	/**
	 * SAP数据整理
	 * @author 杜志星 (380173)
	 * @date 2014-11-19 
	 * @param model
	 */
	void sap2SpmsDataDone(Sap2SpmsData model);
}
