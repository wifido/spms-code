/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-16     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.sf.framework.base.IPage;
import com.sf.module.vmsarrange.domain.SchOptRouteRpt;

/**
 * 路径优化分析报表dao实现类
 * 
 * @author 方芳 (350614) 2014-6-16
 */
public class SchOptRouteRptDao extends ArrBaseEntityDao<SchOptRouteRpt> implements
	ISchOptRouteRptDao {
	//查询分页数据
	public IPage<SchOptRouteRpt> listPage(Long deptId, Date yearMonth,
			int pageSize, int pageIndex, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchOptRouteRpt.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.AREA_ID", deptId, userId);
		//如果年月为空，则取下一个月(下一个月没有数据)
		if(null == yearMonth){
			Date d = new Date();
			yearMonth = DateUtils.addMonths(d, 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		dc.add(Restrictions.sqlRestriction("{alias}.opt_month=?",sdf.format(yearMonth),StandardBasicTypes.STRING));
		dc.addOrder(Order.desc("optDate"));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	
	//查询路径优化分析报表数据
	public List<SchOptRouteRpt> listReport(Long deptId, Date yearMonth,
			Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchOptRouteRpt.class);
		// 添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.AREA_ID", deptId, userId);
		// 如果年月为空，则取下一个月(下一个月没有数据)
		if (null == yearMonth) {
			Date d = new Date();
			yearMonth = DateUtils.addMonths(d, 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		dc.add(Restrictions.sqlRestriction("{alias}.opt_month=?",
				sdf.format(yearMonth), StandardBasicTypes.STRING));
		dc.addOrder(Order.desc("deptCode"));
		return super.findBy(dc);
	}

	// 查询路径优化分析报表总数
	public int listReportCount(Long deptId, Date yearMonth, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchOptRouteRpt.class);
		// 添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.AREA_ID", deptId,
				userId);
		// 如果年月为空，则取下一个月(下一个月没有数据)
		if (null == yearMonth) {
			Date d = new Date();
			yearMonth = DateUtils.addMonths(d, 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		dc.add(Restrictions.sqlRestriction("{alias}.opt_month=?",
				sdf.format(yearMonth), StandardBasicTypes.STRING));
		return super.countBy(dc);
	}
}
