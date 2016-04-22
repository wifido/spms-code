package com.sf.module.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtil {

	public static Map<String, Object> transBeanToMap(Object obj) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.equals("class") || null == property.getReadMethod())
				continue;
			Method getter = property.getReadMethod();
			Object value = getter.invoke(obj);
			map.put(key, value);
		}
		return map;
	}

	public static List<Map<String, Object>> transListBeanToMap(List list) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (list.isEmpty()) {
			return result;
		}
		for (Object o : list) {
			result.add(transBeanToMap(o));
		}
		return result;
	}

}
