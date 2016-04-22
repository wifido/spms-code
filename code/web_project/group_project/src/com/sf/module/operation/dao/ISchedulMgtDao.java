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
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.domain.SchedulMgt;

/**
 * 
 * 排班管理的Dao接口
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public interface ISchedulMgtDao extends IEntityDao<SchedulMgt> {

	public SchedulMgt findByCondition(Long deptid, String ym, String empcode);

	public boolean findByEmpCode(String empcode);

	public void insertConfirmLog(Long deptId, String ym);

	public void delete(ScheduleDto dto, Long schedulingId);

	public List<SchedulMgt> findByDeptIdAndId(Long deptId, Long id);

	public List<SchedulMgt> queryNoConfirmScheduling(Long deptid, String ym);
	
	public void updateSchedulingCommitStatus(Long deptId, String ym);
}