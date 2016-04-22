/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 24, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.biz;

import java.io.File;
import java.util.List;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.ossinterface.util.BigFileDataHandlerParament;
import com.sf.module.ossinterface.util.HRSXmlMapping;
import com.sf.module.ossinterface.util.XmlHandler;

/**
 * 
 * @author 文俊 (337291) Jun 24, 2014
 */

public abstract class HRSBigFileDataHandlerBiz<T> extends BaseBiz implements
		IBigFileDataHandlerBiz {

	protected abstract HRSXmlMapping<T> getMapping(String journalId);

	/**
	 * <pre>
	 * 解析HRS XML文档
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param handlerParament
	 */

	public boolean handler(BigFileDataHandlerParament handlerParament) {
		List<File> list = handlerParament.getFileList();
		System.out.println(list);
		try {
			HRSXmlMapping<T> xmlMapping = getMapping(
					handlerParament.getJournalId());
			for (int i = 0; i < list.size(); i++) {
				File file = list.get(i);
				if (file != null && file.exists()) {
					xmlParser(file.listFiles(), xmlMapping);
				}
			}
			return xmlMapping.isComplete();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("handler Failure: ", e);
			throw new RuntimeException(e);
		}
	}
	
	protected String rootElementPattern = "emp";
	
	protected void xmlParser(File[] files, HRSXmlMapping<T> xmlMapping) throws Exception {
		for (int j = 0; j < files.length; j++) {
			XmlHandler.parser(files[j], xmlMapping, rootElementPattern);
		}
	}

}
