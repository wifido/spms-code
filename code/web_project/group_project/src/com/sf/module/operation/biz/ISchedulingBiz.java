/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.Map;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.operation.action.dto.ScheduleDto;

/**
 *
 * 排班的业务接口
 * @author houjingyu  2014-06-17
 *
 */
public interface ISchedulingBiz extends IBiz {
	//public void saveOrUpdate(ScheduleDto dto);
	public void updateMgtAndDetail(ScheduleDto dto);
	public void saveMgtAndDetail(ScheduleDto dto);
	public Map<String,Object> getSaveValid(ScheduleDto dto);
}