package com.sf.module.common.util;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Enumeration;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;

public class ServletActionHelper {
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

    public static int getStart(HashMap<String, String> parameters) {
        return Integer.parseInt(parameters.get("start"));
    }

    public static int getLimit(HashMap<String, String> parameters) {
        return Integer.parseInt(parameters.get("limit"));
    }
    
    public static String getValueByKey(HashMap<String, String> httpRequestParameter,String key) {
        return httpRequestParameter.get(key);
    }
}
