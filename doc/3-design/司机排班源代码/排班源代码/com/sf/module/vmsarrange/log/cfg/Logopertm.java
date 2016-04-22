/**  
* @Title Logopertm.java
* @Package com.sf.commons.config
* @Description TODO(系统日志注解)
* @author 312933   
* @date 2014-6-9 上午10:29:21
* @version V1.0  
*/
package com.sf.module.vmsarrange.log.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** <pre>   
 * 类名称：Logopertm.java
 * 类描述：TODO(标识日志记录时适用的操作时间)
 * 创建人：312933 
 * 创建时间：2014-6-9 上午10:29:21   
 * 修改人：
 * 修改时间：   
 * 修改备注：   
 * </pre>    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Logopertm {
	/**
	 * @Title describe
	 * @Description TODO(字段描述)
	 * @return
	 */
	public String describe();
	/**
	 * @Title receive
	 * @Description TODO(操作类型[I:新增,U:修改,D:删除])
	 * @return
	 */
	public String receive();
	/**
	 * @Title format
	 * @Description TODO(格式)
	 * @return
	 */
	public String format() default "yyyy-MM-dd HH:mm:ss";
}
