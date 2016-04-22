/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE              PERSON             	REASON
 *  1     2015-5-28         杜志星 (380173)      创建 
 ********************************************************/
package com.sf.module.common.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.biz.ISysConfigBiz;
import com.sf.module.common.util.StringUtil;

/**
 * 
 * @author 杜志星 (380173) 2015-5-28 
 */
public class SpmsBaseAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	public String filterDeptCodeType;
	public ISysConfigBiz sysConfigBiz;
	
	public String forwardOperationIndex() {
		
		filterDeptCodeType = sysConfigBiz.searchByKeyName(StringUtil.OPERATION_DEPTCODE_FILTER).getKeyValue()+",";
		return SUCCESS;
	}

	public String getFilterDeptCodeType() {
		return filterDeptCodeType;
	}

	public void setFilterDeptCodeType(String filterDeptCodeType) {
		this.filterDeptCodeType = filterDeptCodeType;
	}

	public void setSysConfigBiz(ISysConfigBiz sysConfigBiz) {
		this.sysConfigBiz = sysConfigBiz;
	}
}
