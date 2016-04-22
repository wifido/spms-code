/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;

/**
 * 
 * 配班关系dao接口
 * @author 方芳 (350614) 2014-6-6
 */
public interface IScheduleInfoArrangeDao extends IEntityDao<ScheduleInfoArrange> {
	/**
	 * 根据班次信息id查找配班关系
	 * @return
	 */
	public List<ScheduleInfoArrange> listByInfoId(Long infoId);
	/**
	 * 根据配班ids查找配班关系
	 * @return
	 */
	public List<ScheduleInfoArrange> listByArrangeIds(List<Long> arrangeIds);
	
	/**
	 * 根据配班ids查找配班关系
	 * @return
	 */
	public List<ScheduleInfoArrange> listByArrangeId(Long arrangeId);
	/**
	 * 批量更新
	 * @param ids
	 */
	public void updateIsUsedByIds(List<Long> ids);
	/**
	 * 批量更新
	 * @param ids
	 */
	public void updateIsNotUsedByIds(List<Long> ids);
	/**
	 * 查询线路是否被其他班次使用
	 * @return
	 */
	public Integer listByArrangeAndInfo(Long arrangeId,Long infoId);
}
