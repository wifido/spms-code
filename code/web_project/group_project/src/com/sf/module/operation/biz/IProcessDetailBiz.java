/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.List;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.domain.Process;

/**
 *
 * 工序每日明细的业务接口
 * @author houjingyu  2014-07-08
 *
 */
public interface IProcessDetailBiz extends IBiz {
	public void updateMgtAndDetail(ProcessDto dto);
	public void saveMgtAndDetail(ProcessDto dto);
	public List<Process> findByDeptId(Long deptid);
}