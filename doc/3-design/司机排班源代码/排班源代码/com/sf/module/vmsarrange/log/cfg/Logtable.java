/**  
* @Title Logtable.java
* @Package com.sf.commons.config
* @Description TODO(系统日志注解)
* @author 312933   
* @date 2014-6-9 上午10:30:21
* @version V1.0  
*/
package com.sf.module.vmsarrange.log.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** <pre>   
 * 类名称：Logtable.java
 * 类描述：TODO(标识实体类型需要记录操作日志)
 * 创建人：312933 
 * 创建时间：2014-6-9 上午10:30:21   
 * 修改人：
 * 修改时间：   
 * 修改备注：   
 * </pre>    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Logtable {
	/**
	 * @Title logtable
	 * @Description TODO(日志表名称)
	 * @return
	 */
	public String logtable() default "TL_ARR_OPER_LOG";
	/**
	 * @Title entitycode
	 * @Description TODO(实体表代码)
	 * @return
	 */
	public String entitycode();
	/**
	 * @Title entityname
	 * @Description TODO(实体表名称)
	 * @return
	 */
	public String entityname();
	/**
	 * @Title primarykey
	 * @Description TODO(实体表主键)
	 * @return
	 */
	public String primarykey() default "id";
	/**
	 * @Title uniquedesc
	 * @Description TODO(实体唯一描述)
	 * @return
	 */
	public String[] uniquedesc();
	/**
	 * @Title uniquedesc
	 * @Description TODO(唯一描述长度)
	 * @return
	 */
	public int uniquebytemaxlength() default 100;
}
