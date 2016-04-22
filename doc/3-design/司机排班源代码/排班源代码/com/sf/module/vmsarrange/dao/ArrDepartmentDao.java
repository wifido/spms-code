/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-27      600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmscommon.util.CommonUtil;

/**
 * 简单部门Dao实现类
 * @author 600675 2014-05-27
 */
@SuppressWarnings("rawtypes")
public class ArrDepartmentDao extends BaseEntityDao<ArrDepartment> implements
		IArrDepartmentDao {

	/** 特殊区部网点 */ // modify by 312933 2013-04-27
	private static String[] SPECIAL_DEPTS = null; 
		/* new String[] { "SSM0101" }; */
	/**
	 * 是否特殊的地区部门
	 * @param deptCode-部门代码
	 * @return
	 */
	public static boolean isSpecialAreaCode(String deptCode){
		if( null==SPECIAL_DEPTS ){
			return false;
		}
		boolean bSpecialAreaCode = false;
		for( String areaCode: SPECIAL_DEPTS){
			if( areaCode.equals(deptCode) ){
				bSpecialAreaCode = true;
				break;
			}
		}
		return bSpecialAreaCode;
	}
	/**
	 * 封装特殊的部门为地区
	 * @param dc
	 */
	private void wrapperSpecialDeptForArea(DetachedCriteria dc) {
		if (null == SPECIAL_DEPTS || 0 == SPECIAL_DEPTS.length) {
			dc.add(Restrictions.eq("typeLevel", Long.valueOf(2)));
		} else {
			dc.add(Restrictions.or(
					Restrictions.eq("typeLevel", Long.valueOf(2)),
					Restrictions.in("deptCode", SPECIAL_DEPTS)));
		}
	}
	
	/**
	 * 获取用户拥有权限的区部对象(月度消耗查询数据专用)
	 * @param userId
	 * @return
	 */
	public List<ArrDepartment> getAreaByUserId(Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);

		// Modify by 312933 2012-12-14 begin
		wrapperSpecialDeptForArea(dc);// 封装特殊的部门为地区
		// Modify by 312933 2012-12-14 end

		// 普通用户仅查询其有权限的区部
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition, userId,
					StandardBasicTypes.LONG));
		}

		dc.add(Restrictions.eq("deleteFlg", 0));
		
		
		dc.addOrder(Order.asc("typeLevel"))
			.addOrder(Order.asc("parentDeptCode"))
				.addOrder(Order.asc("deptCode"));
		return super.findBy(dc);
	}
		
	/**
	 * 获取所有的区部对象
	 * @return
	 */
	public List<ArrDepartment> getAllAreaDepts() {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		// Modify by 312933 2012-12-14 begin
		wrapperSpecialDeptForArea(dc);// 封装特殊的部门为地区
		// Modify by 312933 2012-12-14 end
		dc.addOrder(Order.asc("typeLevel"))
			.addOrder(Order.asc("parentDeptCode"))
				.addOrder(Order.asc("deptCode"));
		return super.findBy(dc);
	}
	
	/**
	 * 获取所有区部及区部以上的部门
	 * @return
	 */
	public List<ArrDepartment> getAllAreaAndUpwardDept(){
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		if (null == SPECIAL_DEPTS || 0 == SPECIAL_DEPTS.length) {
			dc.add(Restrictions.le("typeLevel", Long.valueOf(2)));
		} else {
			dc.add(Restrictions.or(
					Restrictions.le("typeLevel", Long.valueOf(2)),
					Restrictions.in("deptCode", SPECIAL_DEPTS)));
		}
		dc.addOrder(Order.asc("typeLevel"))
			.addOrder(Order.asc("parentDeptCode"))
				.addOrder(Order.asc("deptCode"));
		return super.findBy(dc);
	}
	
	/**
	 * 根据用户权限获取区部及区部以上的部门
	 * @param userId
	 * @return
	 */
	public List<ArrDepartment> getAllAreaAndUpwardDeptByUserId(Long userId){
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		if (null == SPECIAL_DEPTS || 0 == SPECIAL_DEPTS.length) {
			dc.add(Restrictions.le("typeLevel", Long.valueOf(2)));
		} else {
			dc.add(Restrictions.or(
					Restrictions.le("typeLevel", Long.valueOf(2)),
					Restrictions.in("deptCode", SPECIAL_DEPTS)));
		}
		// 普通用户仅查询其有权限的区部
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition, userId,
					StandardBasicTypes.LONG));
		}
		
		dc.addOrder(Order.asc("typeLevel"))
			.addOrder(Order.asc("parentDeptCode"))
				.addOrder(Order.asc("deptCode"));
		return super.findBy(dc);
	}
	
	/**
	 * 获取行政岗位信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List listPoliticalPostInfoForCombox() {
		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					String hql = "select dic_id job_code,dic_name job_name"
						+" from ts_vms_dictionary d"
						+ " where d.dic_valid=1"
						+" and d.dic_type=101";
					public Object doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						org.hibernate.Query query = arg0.createSQLQuery(hql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}
	
	/**
	 * @author 350613
	 * 根据用户权限获取所有经营本部
	 * @return
	 */
	public List<ArrDepartment> getAllODept(Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		
		// 普通用户仅查询其有权限的区部
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition, userId,
					StandardBasicTypes.LONG));
		}

		dc.add(Restrictions.eq("deleteFlg", 0));
		
		dc.add(Restrictions.eq("typeLevel", Long.valueOf(1)));
		
		dc.addOrder(Order.asc("typeLevel"))
			.addOrder(Order.asc("parentDeptCode"))
				.addOrder(Order.asc("deptCode"));
		return super.findBy(dc);
	}
	/**
	 * @author 350614
	 * 查找全网所有部门-无权限控制
	 */
	public IPage<ArrDepartment> listAllDepts(String deptCode,int pageSize,int pageIndex) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		if(!ArrFileUtil.isEmpty(deptCode)){
			dc.add(Restrictions.like("deptCode", deptCode,MatchMode.ANYWHERE));
		}
		//已删除网点不取
		dc.add(Restrictions.eq("deleteFlg", 0));
		dc.addOrder(Order.asc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	/**
	 * @author 350614
	 * 查找有权限的部门
	 */
	public IPage<ArrDepartment> listAllUserDepts(String deptCode, Long userId,
			int pageSize, int pageIndex) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		if(!ArrFileUtil.isEmpty(deptCode)){
			dc.add(Restrictions.like("deptCode", deptCode,MatchMode.ANYWHERE));
		}
		//已删除网点不取
		dc.add(Restrictions.eq("deleteFlg", 0));
		// 普通用户仅查询其有权限的部门
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition, userId,
					StandardBasicTypes.LONG));
		}
		dc.addOrder(Order.asc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	/**
	 * @author 350614
	 * 查找有权限的所有部门
	 */
	public List<ArrDepartment> listAllDeptByUser(Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		//已删除网点不取
		dc.add(Restrictions.eq("deleteFlg", 0));
		// 普通用户仅查询其有权限的部门
		if (!CommonUtil.isAdmin(userId)) {
			String sqlCondition = "{alias}.DEPT_ID IN ("
					+ "SELECT d.DEPT_ID FROM TS_USER_DEPT d WHERE d.USER_ID=?)";
			dc.add(Restrictions.sqlRestriction(sqlCondition, userId,
					StandardBasicTypes.LONG));
		}
		return super.findBy(dc);
	}
	/**
	 * @author 350614
	 * 校验用户是否有指定部门的权限
	 */
	public ArrDepartment listDeptByUserAndDept(Long deptId, Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrDepartment.class);
		//已删除网点不取
		dc.add(Restrictions.eq("deleteFlg", 0));
		String sql = " {alias}.DEPT_ID = ? " +
					 " AND EXISTS(SELECT DEPT_ID FROM TS_USER_DEPT WHERE USER_ID = ? AND DEPT_ID = {alias}.DEPT_ID) ";
		dc.add(Restrictions.sqlRestriction(sql, new Object[]{deptId,userId},
				(new LongType[]{StandardBasicTypes.LONG,StandardBasicTypes.LONG})));
		List<ArrDepartment> list = super.findBy(dc);
		if(null == list || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}
