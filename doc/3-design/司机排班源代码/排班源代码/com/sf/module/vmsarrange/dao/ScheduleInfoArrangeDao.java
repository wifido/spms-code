/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;

/**
 * 配班关系dao实现类
 * 
 * @author 方芳 (350614) 2014-6-6
 */
public class ScheduleInfoArrangeDao extends ArrBaseEntityDao<ScheduleInfoArrange> implements
	IScheduleInfoArrangeDao {
	//根据班次信息id查找配班关系
	public List<ScheduleInfoArrange> listByInfoId(Long infoId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfoArrange.class);
		dc.createAlias("scheduleInfo", "info").add(Restrictions.eq("info.id", infoId));
		return super.findBy(dc);
	}
	//根据配班ids查找配班关系
	public List<ScheduleInfoArrange> listByArrangeIds(List<Long> arrangeIds) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfoArrange.class);
		dc.add(Restrictions.in("arrangeId", arrangeIds));
		return super.findBy(dc);
	}
	//根据配班id查找配班关系
	public List<ScheduleInfoArrange> listByArrangeId(Long arrangeId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfoArrange.class);
		dc.add(Restrictions.eq("arrangeId", arrangeId));
		return super.findBy(dc);
	}
	//批量更新
	public void updateIsUsedByIds(List<Long> ids) {
		Map<String, Object> args = new HashMap<String, Object>(1);
		args.put("id", ids);
		super.execute(
				"UPDATE com.sf.module.vmsarrange.domain.ScheduleArrange SET IS_USED = 1 WHERE id IN (:id)",
				args);
	}
	//批量更新
	public void updateIsNotUsedByIds(List<Long> ids) {
		Map<String, Object> args = new HashMap<String, Object>(1);
		args.put("id", ids);
		super.execute(
				"UPDATE com.sf.module.vmsarrange.domain.ScheduleArrange SET IS_USED = 0 WHERE id IN (:id)",
				args);
	}
	//查询线路是否被其他班次使用
	public Integer listByArrangeAndInfo(Long arrangeId,Long infoId){
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleInfoArrange.class);
		dc.createAlias("scheduleInfo", "info").add(Restrictions.eq("info.id", infoId));
		dc.add(Restrictions.ne("arrangeId", arrangeId));
		return super.countBy(dc);
	}
}
