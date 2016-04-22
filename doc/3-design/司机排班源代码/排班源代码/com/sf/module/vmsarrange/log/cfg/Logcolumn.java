/**  
* @Title Logcolumn.java
* @Package com.sf.commons.config
* @Description TODO(系统日志注解)
* @author 312933   
* @date 2014-6-6 下午01:04:16
* @version V1.0  
*/
package com.sf.module.vmsarrange.log.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** <pre>   
 * 类名称：Logcolumn.java
 * 类描述：TODO(用以实体字段需要记录日志时进行注解声明)
 * 创建人：312933 
 * 创建时间：2014-6-6 下午01:04:16   
 * 修改人：
 * 修改时间：   
 * 修改备注：   
 * </pre>    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Logcolumn {
	/**
	 * @Title describe--字段描述
	 * @Description TODO(当前字段描述)
	 * @return
	 */
	public String describe();
	/**
	 * @Title receive--日志适用类型
	 * @Description TODO([I:新增,U:修改,D:删除])
	 * @return
	 */
	public String receive();
	
	/**
	 * @Title isobject--字段是否bean对象
	 * @Description TODO(true/false,默认为false)
	 * @return
	 */
	public boolean isbean() default false;
	
	/**
	 * @Title refproperty--引用属性
	 * @Description TODO(isbean=true时必须指定引用属性,默认为空 )
	 * @return
	 */
	public String refproperty() default "";
	
	/**
	 * @Title format--字段为日期时格式
	 * @Description TODO(默认"yyyy-MM-dd hh:mm:ss")
	 * @return
	 */
	public String format() default "yyyy-MM-dd HH:mm:ss";
}
