/**  
 * @Title LoggerUtil.java
 * @Package com.sf.commons
 * @Description TODO(用一句话描述该文件的用途)
 * @author 312933   
 * @date 2014-6-6 下午01:12:18
 * @version V1.0  
 */
package com.sf.module.vmsarrange.log;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sf.framework.server.core.integration.SpringBeanLoader;
import com.sf.module.vmsarrange.log.cfg.Logcolumn;
import com.sf.module.vmsarrange.log.cfg.Logoperemp;
import com.sf.module.vmsarrange.log.cfg.Logopertm;
import com.sf.module.vmsarrange.log.cfg.Logtable;
import com.sf.module.vmsarrange.log.dao.ILogDao;

/**
 * <pre>
 * 类名称：LoggerUtil.java
 * 类描述：TODO(日志记录器)
 * 创建人：312933 
 * 创建时间：2014-6-6 下午01:12:18   
 * 修改人：
 * 修改时间：   
 * 修改备注：
 * </pre>
 */
@SuppressWarnings("all")
public class LogUtils {

	static String LOG_BUFFER = "insert into %s(log_id,"
			+ "entity_code,entity_name,primary_key,unique_desc,index_code,index_name,oper_type,oper_emp_code,oper_tm,log_content)"
			+ " values(seq_arr_base.nextval,'%s','%s','%s','%s','%s','%s','%s','%s',timestamp'%s','%s')";

	static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static LogUtils instance = new LogUtils();
	private ILogDao logDao;
	private LogUtils() {
		logDao = (ILogDao)SpringBeanLoader.getBean("arrLogDao");
	}
	public static LogUtils getInstance() {
		return instance;
	}
	public void insertLog(Object domain) throws Exception {
		this.Log(domain, "I");
	}

	public void updateLog(Object oldDomain, Object newDomain) throws Exception {
		this.Log(oldDomain, newDomain);
	}

	public void deleteLog(Object domain) throws Exception {
		this.Log(domain, "D");
	}

	private void Log(Object domain, String operType) throws Exception {
		this.Log(null, domain, operType);
	}

	private void Log(Object oldDomain, Object domain) throws Exception {
		this.Log(oldDomain, domain, "U");
	}

	private void Log(Object oldDomain, Object domain, String operType)
			throws Exception {
		Class cls = domain.getClass();
		boolean logtableFlag = cls.isAnnotationPresent(Logtable.class);
		if (!logtableFlag) {
			return;
		}
		
		Logtable logtable = (Logtable) cls.getAnnotation(Logtable.class);
		String tablename = logtable.logtable();
		String entityCode = logtable.entitycode();
		String entityName = logtable.entityname();
		Object pKey = logtable.primarykey();// 主键值
		String uniqueDesc = "";// 唯一标识
		Object opTm = null;// 操作时间
		Object opEmp = null;// 操作人
		
		Field[] chlfields = cls.getDeclaredFields();
		Field[] supfields = cls.getSuperclass().getDeclaredFields();
		Field[] fields = new Field[chlfields.length + supfields.length];
		System.arraycopy(chlfields, 0, fields, 0, chlfields.length);
		System.arraycopy(supfields, 0, fields, chlfields.length,supfields.length);
		
		for (Field field : fields) {
			// 唯一标识(说明：如果为对象，则需于该属性添加logColumn说明isBean=true并标明需记录日志的属性名)
			if( null!=logtable.uniquedesc() && 0<logtable.uniquedesc().length){
				for (String fieldname : logtable.uniquedesc()) {
					if (field.getName().equals(fieldname)) {
						Object oValue = getAttr(domain, field);
						uniqueDesc += (null == oValue?"":oValue.toString()) + ";";
						break;
					}
				}
			}
			// 主键值
			if (field.getName().equals(pKey.toString())) {
				pKey = getAttr(domain, field);
				continue;
			}
			
			// 操作时间
			if (field.isAnnotationPresent(Logopertm.class)) {
				Logopertm logtm = (Logopertm) field
						.getAnnotation(Logopertm.class);
				if (-1 == logtm.receive().indexOf(operType)) {
					continue;
				}
				opTm = getAttr(domain, field);
				continue;
			}
			
			// 操作人
			if (field.isAnnotationPresent(Logoperemp.class)) {
				Logoperemp logemp = (Logoperemp) field
						.getAnnotation(Logoperemp.class);
				if (-1 == logemp.receive().indexOf(operType)) {
					continue;
				}
				opEmp = getAttr(domain, field);
				continue;
			}
		}
				
		// 最多30个中文
		uniqueDesc = uniqueDesc.replaceAll(";$", "");
		if (uniqueDesc.getBytes().length > logtable.uniquebytemaxlength()) {
			uniqueDesc = uniqueDesc.substring(0, 30);
		}

		for (Field field : fields) {
			// 是否添加了日志注解
			if (!field.isAnnotationPresent(Logcolumn.class)) {
				continue;
			}
			Logcolumn logcolumn = (Logcolumn) field
					.getAnnotation(Logcolumn.class);
			// 是否符合日志注解接受要求
			String curOperType = logcolumn.receive();
			if (-1 == curOperType.trim().indexOf(operType)) {
				continue;
			}

			String indexCode = field.getName();
			String indexName = logcolumn.describe();
			String format = logcolumn.format();
			
			// 日志内容
			Object logContent = null;
			if ("I".equalsIgnoreCase(operType)) {
				logContent = getAttr(domain, field);
			} else if ("D".equalsIgnoreCase(operType)) {
				logContent = getAttr(domain, field);
			} else {
				Object oldVal = getAttr(oldDomain, field);
				Object newVal = getAttr(domain, field);
				if ((null == oldVal && null==newVal) 
						|| (null != oldVal && null != newVal && oldVal.equals(newVal))) {
					continue;
				}
				logContent = String.format("%s->%s", oldVal, newVal);
			}
			if(null == uniqueDesc || "".equals(uniqueDesc)){
				uniqueDesc = "无";
			}
			if(null == logContent || "".equals(logContent)){
				logContent = "无";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(null == opTm){
				opTm = sdf.format(new Date());
			}
			String sql = String.format(LOG_BUFFER, tablename, entityCode,
					entityName, pKey, uniqueDesc, indexCode, indexName,
					operType, opEmp, opTm, logContent);
			System.out.println(sql);
			logDao.log(sql);
		}
	}

	private Object getAttr(Object domain, Field field) {
		Object obj = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(domain.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			if (null == pds || 0 == pds.length) {
				return null;
			}

			Method method = null;
			for (PropertyDescriptor p : pds) {
				if (p.getName().equals(field.getName())) {
					method = p.getReadMethod();
					break;
				}
			}
			
			if (null == method) {
				return null;
			}

			obj =  method.invoke(domain, new Object[] {});
			if (null == obj) {
				return null;
			}
						
			if ( obj instanceof java.util.Date) {
				// 日期类型
				SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
				if (!field.isAnnotationPresent(Logcolumn.class)) {
					return sdf.format(obj);
				}
				Logcolumn logcolumn = (Logcolumn) field
						.getAnnotation(Logcolumn.class);
				
				if (!logcolumn.format().equals(DEFAULT_DATE_FORMAT)) {
					sdf = new SimpleDateFormat(logcolumn.format());
				}
				return sdf.format(obj);
			} 
			
			// 该字段没有进行日志注解
			if ( !field.isAnnotationPresent(Logcolumn.class) ) {
				return obj;
			}
	
			Logcolumn logcolumn = (Logcolumn) field.getAnnotation(Logcolumn.class);
			if( !logcolumn.isbean() ){
				return obj;
			}
			
			String refproperty = logcolumn.refproperty();
			Field refField = null;
			try {
				refField = obj.getClass().getDeclaredField(refproperty);
			} catch (Exception e) {
				refField = obj.getClass().getSuperclass()
						.getDeclaredField(refproperty);
			}
			obj = getAttr(obj, refField);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
