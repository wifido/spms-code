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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.module.vmsarrange.domain.ScheduleArrangeNo;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 * 班次代码dao实现类
 * 
 * @author 方芳 (350614) 2014-6-6
 */
public class ScheduleArrangeNoDao extends ArrBaseEntityDao<ScheduleArrangeNo> implements
	IScheduleArrangeNoDao {
	//获取最大的序号
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long listMaxNo(String deptCode, int type) {
		String sqlbak = "select nvl(max(no),0) from tm_arr_schedule_arrange_no where dept_code = '"+deptCode+"' and type = "+type;
		final String sql = sqlbak;
		return (Long) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				Object object = query.uniqueResult();
				if (object != null) {
					return ((BigDecimal)object).longValue();
				}
				return null;
			}
		});
	}
	// 获取指定区部的记录
	public List<ScheduleArrangeNo> listByCodeAndType(String deptCode, int type) {
		DetachedCriteria dc = DetachedCriteria.forClass(ScheduleArrangeNo.class);
		if(ArrFileUtil.isEmpty(deptCode)){
			deptCode = "无";
		}
		dc.add(Restrictions.eq("deptCode", deptCode));
		dc.add(Restrictions.eq("type", type));
		return super.findBy(dc);
	}
}
