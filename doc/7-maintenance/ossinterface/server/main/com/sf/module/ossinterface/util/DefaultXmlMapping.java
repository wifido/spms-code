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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 文俊 (337291) Jun 23, 2014 
 */

public abstract class DefaultXmlMapping<T> implements IXmlMapping<T> {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    
	protected Map<String, Method> mapping;
	
	
	/**
	 * @author 文俊 (337291)
	 * @date Jun 23, 2014 
	 */
	public DefaultXmlMapping(String journalId) {
		super();
		this.journalId = journalId;
		try {
			Class<?> genericType = getFirstGenericType(getClass());
			BeanInfo beanInfo = Introspector.getBeanInfo(genericType);
			PropertyDescriptor[] pd = beanInfo.getPropertyDescriptors();
			pd (pd, genericType);
			
		} catch (Exception e) {
			throw new RuntimeException(super.getClass().getCanonicalName() + "泛型错误! 继承于:" + DefaultXmlMapping.class.getCanonicalName() + 
	        "的类都必需声明所操作实体的泛型", e);
		}
	}
	
	private void pd(PropertyDescriptor[] pd, Class<?> genericType) {
		if (pd != null) {
        	mapping = new HashMap<String, Method>(pd.length);
            for (int i = 0; i < pd.length; i++) {
            	wm(pd[i], genericType);
            }
        }
	}
	
	private void wm(PropertyDescriptor pd, Class<?> genericType) {
		if (pd != null) {
			Method writeMethod = pd.getWriteMethod();
			if (writeMethod != null) {
				XmlElement xmlElement = writeMethod.getAnnotation(XmlElement.class);
				if (xmlElement != null) {
					mapping.put(xmlElement.name(), writeMethod);
				}
			}
		}
	}
	
	
	private Class<?> getFirstGenericType(Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		if ((type != null) && (ParameterizedType.class.isInstance(type))) {
			ParameterizedType pType = (ParameterizedType) type;
			Type argType = pType.getActualTypeArguments()[0];
			if (argType instanceof Class) {
				return ((Class<?>) argType);
			}
			return null;
		}

		return getFirstGenericType(clazz.getSuperclass());
	}

	protected List<T> data;

	/**
	 * @author 文俊 (337291)
	 * @date Jun 23, 2014 
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}
	
	
	public void mapping(T entity, String tagName, String textValue) {
		try {
			if (tagName != null) {
				Method writeMethod = mapping.get(tagName.toUpperCase());
				if (writeMethod != null) {
					writeMethod.invoke(entity, textValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String msg = String.format(
					"mapping Failure for tagName: %s; textValue: %s",
					tagName, textValue);
			System.out.println(msg);
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
	
	/*
    private Object convertValueToRequiredType(String textValue, Class<?> requiredType) throws Exception {
        if (textValue == null) {
            return null;
        }
        Object result = textValue;
        if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            result = Integer.valueOf(textValue);
        } else if (double.class.equals(requiredType) || Double.class.equals(requiredType)) {
            result = Double.valueOf(textValue);
        } else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            result = Long.valueOf(textValue);
        } else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            result = Float.valueOf(textValue);
        } else if (Date.class.equals(requiredType)) {
            Timestamp timestamp = rs.getTimestamp(i);
            if (timestamp != null) {
                result = new Date(timestamp.getTime());
            }
        }
        return result;
    }*/

	protected String journalId;

}
