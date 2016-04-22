/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 28, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 
 * @author 文俊 (337291) Jun 28, 2014 
 */

public class BasicDomain extends BaseEntity {
	/**
	 * @author 文俊 (337291)
	 * @date Jun 28, 2014 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected String substring(int len, String str) {
		if (str != null && str.length() > len) {
			str = str.substring(0, len);
		}
		return str;
	}
}
