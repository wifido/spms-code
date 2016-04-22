package com.sf.module.esbinterface.util;

import com.sf.framework.server.core.integration.SpringBeanLoader;
import com.sf.module.esbinterface.biz.IBigFileDataHandlerBiz;

public class BeanUtil {
    public static IBigFileDataHandlerBiz getHandlerBean(String beanId) {
        return (IBigFileDataHandlerBiz) getBean(beanId);
    }

    private static Object getBean(String beanId) {
        return SpringBeanLoader.getBean(beanId);
    }
}
