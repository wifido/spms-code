package com.sf.module.operation.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.SchedulMgtHistory;

@Repository
public class SchedulMgtHistoryDao extends BaseEntityDao<SchedulMgtHistory> {

	private static final String MODIFIED_TM = "modifiedTm";
	private static final String YM = "ym";
	private static final String DEPT_ID = "deptId";
	private static final String EMP_CODE = "empCode";

	public SchedulMgtHistory findByCondition(Long deptid, String ym, String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgtHistory.class);
		dc.add(Restrictions.eq(EMP_CODE, empcode));
		dc.add(Restrictions.eq(YM, ym));
		dc.add(Restrictions.eq(DEPT_ID, deptid));
		dc.addOrder(Order.desc(MODIFIED_TM));
		List<SchedulMgtHistory> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<SchedulMgtHistory> findSchedulMgtHistoryList(Long deptid, String ym, String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgtHistory.class);
		dc.add(Restrictions.eq(EMP_CODE, empcode));
		dc.add(Restrictions.eq(YM, ym));
		dc.add(Restrictions.eq(DEPT_ID, deptid));
		dc.addOrder(Order.asc(MODIFIED_TM));
		List<SchedulMgtHistory> list = this.findBy(dc);
		return list;
	}

	public List<SchedulMgtHistory> findSchedulMgtHistoryByYm(String ym) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgtHistory.class);
		dc.add(Restrictions.eq(YM, ym));
		dc.addOrder(Order.desc(MODIFIED_TM));
		List<SchedulMgtHistory> list = this.findBy(dc);
		return list;
	}

}