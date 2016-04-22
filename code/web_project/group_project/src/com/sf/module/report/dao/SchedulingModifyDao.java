package com.sf.module.report.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.report.domain.SchedulingModify;

public class SchedulingModifyDao extends BaseEntityDao<SchedulingModify> {

	private static final String authoritySql = "dept_code in (select distinct d.dept_code from "
			+ "(select dept_code, dept_id from tm_department where delete_flg = 0 " 
			+ "start with dept_code = ? connect by prior dept_code = parent_dept_code) d, "
			+ "ts_user_dept ud " 
			+ "where d.dept_id = ud.dept_id "
			+ "and (ud.user_id = ? or ? in (select user_id from ts_user where status = 'root')))";

	public List<SchedulingModify> query(String parentDeptCode, Long userId, String month, String empCode) {
		DetachedCriteria detachedCriteria = buildQuery(parentDeptCode, userId, month, empCode);
		detachedCriteria.addOrder(Order.desc("modifiedTm"));
		List<SchedulingModify> list = this.findBy(detachedCriteria);
		return list;
	}

	public IPage<SchedulingModify> queryByPage(String parentDeptCode, Long userId, String month, String empCode, int start, int limit) {
		DetachedCriteria detachedCriteria = buildQuery(parentDeptCode, userId, month, empCode);
		IPage<SchedulingModify> page = this.findPageBy(detachedCriteria, limit, start / limit, "modifiedTm", false);
		return page;
	}

	private DetachedCriteria buildQuery(String parentDeptCode, Long userId, String month, String empCode) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SchedulingModify.class);

		SchedulingModify schedulingModify = new SchedulingModify();
		schedulingModify.setYearMonth(month);
		if (CommonUtil.isNotEmpty(empCode)) {
			schedulingModify.setEmpCode(empCode);
		}
		detachedCriteria.add(Example.create(schedulingModify)).add(
				Restrictions.sqlRestriction(authoritySql, new Object[] { parentDeptCode, userId, userId }, new Type[] {
						org.hibernate.type.StringType.INSTANCE, org.hibernate.type.LongType.INSTANCE, org.hibernate.type.LongType.INSTANCE }));
		return detachedCriteria;
	}
}
