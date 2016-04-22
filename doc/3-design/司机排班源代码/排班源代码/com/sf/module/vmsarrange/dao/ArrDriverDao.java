/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-19     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.frameworkimplExtend.util.StringUtil;
import com.sf.module.vmsarrange.domain.ArrDriver;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * ArrDriverDao处理类
 *
 */
public class ArrDriverDao extends BaseEntityDao<ArrDriver> implements IArrDriverDao {

	public ArrDriver findDriverByCode(String empCode) {
		ArrDriver driver = null;
		if(StringUtil.isEmpty(empCode)){
			return null;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDriver.class);
		dc.add(Restrictions.eq("empCode", empCode));
		dc.add(Restrictions.eq("managerFlg", 0));
		dc.add(Restrictions.eq("valid", 1));
		List list = super.findBy(dc);
		if(null != list && list.size()>0){
			driver = (ArrDriver) list.get(0);
		}
		return driver;
	}
	
	public ArrDriver findDriverByCodeAndId(String empCode,Long deptId,Long userId) {
		ArrDriver driver = null;
		if(StringUtil.isEmpty(empCode)){
			return null;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDriver.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		dc.add(Restrictions.eq("empCode", empCode));
		dc.add(Restrictions.eq("managerFlg", 0));
		dc.add(Restrictions.eq("valid", 1));
		List<ArrDriver> list = super.findBy(dc);
		if(null != list && list.size()>0){
			driver = list.get(0);
		}
		return driver;
	}
	
	public IPage<ArrDriver> listPageBy(Long deptId, String empCode,String yearMonth, Long userId,int pageSize,int pageIndex){
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDriver.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteria(dc, "{alias}.DEPT_ID",deptId, userId);
		dc.add(Restrictions.eq("managerFlg", 0));
		dc.add(Restrictions.eq("valid", 1));
		if(!ArrFileUtil.isEmpty(empCode)){
			dc.add(Restrictions.like("empCode", empCode,MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.sqlRestriction(" NOT EXISTS(SELECT DF.SCHEDULE_DF_ID " +
				" FROM TT_ARR_SCHEDULE_DRAFT DF WHERE DF.YEAR_MONTH=? AND DF.DRIVER_ID = {alias}.DRIVER_ID) ",
				yearMonth,StandardBasicTypes.STRING));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
}
