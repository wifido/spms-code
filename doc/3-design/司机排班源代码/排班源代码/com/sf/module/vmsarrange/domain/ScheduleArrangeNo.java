/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6      350614        创建
 **********************************************/
package com.sf.module.vmsarrange.domain;

import java.util.Date;

import com.sf.framework.base.domain.BaseEntity;
import com.sf.module.vmsarrange.log.cfg.Logcolumn;
import com.sf.module.vmsarrange.log.cfg.Logoperemp;
import com.sf.module.vmsarrange.log.cfg.Logopertm;
import com.sf.module.vmsarrange.log.cfg.Logtable;

/**
 *
 * 班次代码生成表
 *
 */
@Logtable(entitycode = "TM_VMS_SCHEDULE_ARRANGE_NO", 
		entityname = "班次代码生成", 
		logtable = "TL_ARR_OPER_LOG_BAK",
		uniquedesc = { "deptCode" }
)
public class ScheduleArrangeNo extends BaseEntity {
	/**
	 * @author 方芳 (350614)
	 * @date 2014-6-6 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//网点代码
	private String deptCode;
	//序号
	@Logcolumn(describe = "序号", receive = "I")
	private Long no;
	//类型 1.机动 2.普通
	private Integer type;
	//获取人
	@Logoperemp(describe = "获取人", receive = "I")
	private String operEmpCode;
	//获取时间
	@Logopertm(describe = "获取时间", receive = "I")
	private Date operTm;
	
	public String getOperEmpCode() {
		return operEmpCode;
	}
	public void setOperEmpCode(String operEmpCode) {
		this.operEmpCode = operEmpCode;
	}
	public Date getOperTm() {
		return operTm;
	}
	public void setOperTm(Date operTm) {
		this.operTm = operTm;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
