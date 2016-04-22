package com.sf.module.domain;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;

public class StringUtil {
	public static String urlPath = "http://spms.sf-express.com/spms/driverui";
	
    public static boolean isBlank(String target) {
        return target == null || "".equals(target);
    }

    public static boolean isNotBlank(String target) {
        return !isBlank(target);
    }
    
    public static HashMap<String, String> getHttpRequestParameter() {
        Enumeration<?> params = ServletActionContext.getRequest()
                .getParameterNames();
        HashMap<String, String> hashMap = newHashMap();
        while (params.hasMoreElements()) {
            String queryField = (String) params.nextElement();
            hashMap.put(queryField, ServletActionContext.getRequest()
                    .getParameter(queryField));
        }
        return hashMap;
    }
    
}
