/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 20, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.util;

import java.util.List;

import org.xml.sax.Attributes;

/**
 * 
 * @author 文俊 (337291) Jun 20, 2014 
 */

public interface IXmlMapping<T> {
	int MAX_SIZE = 100;
	List<T> init();
	T newEntity();
	void saveBatch(List<T> entities);
	void mapping(T entity, String tagName, String textValue) ;
	void attributes(String tagName, Attributes attributes);
	
}
