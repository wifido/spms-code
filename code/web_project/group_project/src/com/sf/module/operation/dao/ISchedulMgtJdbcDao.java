/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IJdbcDao;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.domain.SchedulingBase;

/**
 * 
 * 排班管理的JdbcDao接口
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public interface ISchedulMgtJdbcDao extends IJdbcDao {

	public SchedulMgt findByCondition(Long deptid, String ym, String empcode);

	public int getMgtCount(Long userId, ScheduleDto dto);

	public IPage<SchedulMgt> findPage(Long userId, ScheduleDto dto,
			int pageSize, int pageIndex);

	public IPage<SchedulMgt> schedulMgtNoConfirmImport(ScheduleDto dto,
			int pageSize, int pageIndex);

	public IPage<SchedulMgt> schedulMgtConfirmImport(ScheduleDto dto,
			int pageSize, int pageIndex);

	public OutEmployee getEmpByCode(Long deptId, String empCode);
	
	public OutEmployee checkExistByEmpByCode(Long deptId, String empCode);

	public List<OutEmployee> getUnFinishedEmp(final String ym);

	public List<SchedulingBase> getScheInfo(Long deptId, String scheCode, String ym);
	
	public List<SchedulingBase> getLastMonthLastDayScheInfo(Long deptId, String empCode, String sheduleDt) ;

	public boolean allEmpIsHasSche(Long deptId, String ym);

	public boolean allScheIsFinished(Long deptId, String ym);

	public IPage<OutEmployee> findEmpPage(ScheduleDto dto, int pageSize,
			int pageIndex);

	public List<MonthConfirmStatus> getConfirmList();

	public boolean getToConfirmThe(Long deptId, String schedulingIds);
	
	public List<OutEmployee> getNotSchedulEmployee(Long deptid,String ym);

	boolean queryModifyPermissions(Long userId);
	
	boolean queryModifyMonthPermissions(Long userId);
}