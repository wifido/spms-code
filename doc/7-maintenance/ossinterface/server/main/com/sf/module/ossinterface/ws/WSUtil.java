/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 4, 2013           文俊 (337291)       创建
 ********************************************************/
package com.sf.module.ossinterface.ws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.sf.module.common.util.CommonUtil;

/**
 *
 * @author 文俊 (337291) Jun 4, 2013
 */

public class WSUtil {

	private WSUtil() {
	}

	public static <T> T getService(Class<T> serviceClass, String uriKey,
			String addrKey) throws Exception {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		String wsdlString = "http://10.0.44.31:12041"
			//+ CommonUtil.getespResendIp() + ":"
			//	+ CommonUtil.getesbResendPort()
				+ "/esb/ws/BigFileResendData?wsdl";
		factory.setAddress(wsdlString);
		return factory.create(serviceClass);
	}

	public static <T> T getServiceEsb(Class<T> serviceClass, String addrKey)
			throws Exception {
		return getService(serviceClass, null, addrKey);
	}

}
