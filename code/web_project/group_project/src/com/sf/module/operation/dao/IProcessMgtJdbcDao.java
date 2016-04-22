/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.Date;
import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IJdbcDao;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.ProcessConfirmStatus;
import com.sf.module.operation.domain.ProcessDetail;
import com.sf.module.operation.domain.ProcessMgt;
import com.sf.module.operation.domain.OutEmployee;

/**
 *
 * 月度工序实体的JdbcDao接口
 * @author houjingyu  2014-07-08
 *
 */
public interface IProcessMgtJdbcDao extends IJdbcDao {
	public IPage<ProcessMgt> findPage(Long userId,ProcessDto dto, int pageSize,int pageIndex);
	public int getMgtCount(Long userId,ProcessDto dto);
	public boolean allScheIsHasProcess(Long deptId,String ym);
	public List<OutEmployee> getUnFinishedEmp(final Long deptid,final String ym);
	public ProcessMgt findByCondition(Long deptid,String ym,String empcode);
	public ProcessDetail findBy(Long deptid, Date dt, String empcode);
	
	public int getExportProcessMgtCount(ProcessDto dto);
	public IPage<ProcessMgt> importConfirmProcessMgt(ProcessDto dto,int pageSize, int pageIndex);
	public IPage<ProcessMgt> importNoConfirmProcessMgt(ProcessDto dto,int pageSize, int pageIndex);
	public List<ProcessConfirmStatus>  getConfirmList();
}