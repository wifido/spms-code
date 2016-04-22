package com.sf.module.common.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.util.CommonUtil;

public class OssDepartmentDao extends BaseEntityDao<OssDepartment> implements IOssDepartmentDao {

	public List<OssDepartment> getDeptByUserId(Long userId,Long deptId) {
		// TODO Auto-generated method stub

		DetachedCriteria dc = DetachedCriteria.forClass(OssDepartment.class);

		// 普通用户仅查询其有权限的区部
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=? and d.DEPT_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition,new Object[]{userId,deptId},
					new Type[]{Hibernate.LONG,Hibernate.LONG}));
		}

		dc.add(Restrictions.eq("deleteFlg", 0));
		return super.findBy(dc);
	
	}

	@Override
	public List<OssDepartment> getParentDeptCode(String deptCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(OssDepartment.class);
		
		String sqlCondition = "{alias}.PARENT_DEPT_CODE = ?";
				
		dc.add(Restrictions.sqlRestriction(sqlCondition,new Object[]{deptCode},
				new Type[]{Hibernate.STRING}));
		return super.findBy(dc);
	}

	@Override
	public OssDepartment getDepartmentByDeptCode(String deptCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(OssDepartment.class);
		dc.add(Restrictions.eq("deptCode",deptCode));
		List<OssDepartment> list = super.findBy(dc);
		if(list!=null&&list.size()>0)
			return  list.get(0);
		return null;
	}

	@Override
	public OssDepartment getById(Long deptId) {
		DetachedCriteria dc = DetachedCriteria.forClass(OssDepartment.class);
		dc.add(Restrictions.eq("id", deptId));
		List<OssDepartment> list = super.findBy(dc);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
