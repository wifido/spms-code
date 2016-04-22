/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.Date;
import java.util.List;

import com.sf.framework.server.base.dao.IJdbcDao;
import com.sf.module.operation.domain.Scheduling;

/**
 *
 * 排班的JdbcDao接口
 * @author houjingyu  2014-06-17
 *
 */
public interface ISchedulingJdbcDao extends IJdbcDao {
	public boolean checkScheDtInDateRange(Long deptId, String schecode, Date dt);
	public Integer getGroupDtEmpDtValid(Long deptId,String empCode,Date dt);
	public List<Scheduling> getScheBy(Long deptId, String ym,String empCode);
	public Date[] getValidDateRange(Long deptid,String empcode,String ym);
	public Scheduling findByCondition(Date dt, String empcode);
}