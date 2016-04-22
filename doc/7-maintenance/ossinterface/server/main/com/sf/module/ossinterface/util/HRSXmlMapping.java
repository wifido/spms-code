/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 23, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.util;

import org.xml.sax.Attributes;


/**
 * 
 * @author 文俊 (337291) Jun 23, 2014 
 */

public abstract class HRSXmlMapping<T> extends DefaultXmlMapping<T> {

	/**
	 * @author 文俊 (337291)
	 * @date Jun 24, 2014 
	 * @param journalId
	 */
	public HRSXmlMapping(String journalId) {
		super(journalId);
	}
	/**total size*/
	protected long size = -1;
	protected long savedSize = 0L;
	
	public boolean isComplete() {
		return size == -1 || (size != -1 && size == savedSize);
	}

	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014 
	 * @return the errmsg
	 */
	public String getErrmsg() {
		return errmsg;
	}


	protected String batchNumber;
	protected String errmsg;

	
	public void attributes(String tagName, Attributes attributes) {
		//<emps size="9999" batch_number="20140619144107" errmsg="">
		if ("EMPS".equals(tagName)) {
			String value = attributes.getValue("size");
			if (value != null && value.matches("^\\d*$")) {
				if (this.size == -1) {
					this.size = 0;
				}
				this.size += Long.parseLong(value);
			}
			this.batchNumber = attributes.getValue("batch_number");
			this.errmsg = attributes.getValue("errmsg");
		}
	}
}
