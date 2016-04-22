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

import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.ProcessMgt;

/**
 *
 * 月度工序实体的Dao接口
 * @author houjingyu  2014-07-08
 *
 */
public interface IProcessMgtDao extends IEntityDao<ProcessMgt> {
	public ProcessMgt findByCondition(Long deptid,String ym,String empcode);
	
	public void delete(Long deptid,Long processMgtIds);

	public boolean isHaveNoConfirmProcessMgt(Long deptid, String ym);

	public List<ProcessMgt> findByDeptIdAndId(Long deptid, Long id);
}