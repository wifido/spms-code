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

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.operation.domain.Scheduling;

/**
 * 
 * 排班的Dao实现类
 * 
 * @author houjingyu 2014-06-17
 * 
 */
public class SchedulingDao extends BaseEntityDao<Scheduling> implements
		ISchedulingDao {

	public Scheduling findByCondition(final Date dt,
			final String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(Scheduling.class);
		dc.add(Restrictions.eq("empCode", empcode));
		dc.add(Restrictions.eq("sheduleDt", dt));
		//dc.add(Restrictions.eq("deptId", deptid));
		List<Scheduling> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	public boolean findById( String scheduleCode,Long deptid, String ym) {
		DetachedCriteria dc = DetachedCriteria.forClass(Scheduling.class);
		dc.add(Restrictions.eq("scheduleCode", scheduleCode));
		dc.add(Restrictions.eq("deptId", deptid));
		
		try {
			Date date1 = DateUtil.parseDate(ym +"-01",DateFormatType.yyyy_MM_dd);
			DateTime date2=new DateTime(date1).plusMonths(1).minusDays(1);
			dc.add(Restrictions.between("sheduleDt", date1, date2.toDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Scheduling> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
	@Override
	public boolean checkChangSchedule(Long id, String newScheduleCodd) {
		Session session = this.getSessionFactory().openSession();
		Scheduling scheduling = (Scheduling) session.get(Scheduling.class, id);
		session.close();
		return scheduling.getScheduleCode().equals(newScheduleCodd) ? false : true;
	}

}