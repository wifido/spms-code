package com.sf.module.vmsarrange.domain;

import com.sf.module.cmscommon.action.DepartmentTreeWithoutCheckbox;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * Description: 组织机构树，增加了类型层次
 * HISTORY
 * *********************************************
 *  ID   DATE           PERSON          REASON
 *  1    2014-05-27	    600675                           创建 
 * *********************************************
 * </pre>
 */
@SuppressWarnings("serial")
public class DepartmentTree extends DepartmentTreeWithoutCheckbox {

	// 类型层次
	private long typeLevel;

	public long getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(long typeLevel) {
		this.typeLevel = typeLevel;
	}
}
