/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.ProcessConfirmStatus;

/**
 *
 * 排班提交确认状态的Dao接口
 * @author houjingyu  2014-07-02
 *
 */
public interface IProcessConfirmStatusDao extends IEntityDao<ProcessConfirmStatus> {
	public ProcessConfirmStatus findBy(Long deptid,String ym);
}