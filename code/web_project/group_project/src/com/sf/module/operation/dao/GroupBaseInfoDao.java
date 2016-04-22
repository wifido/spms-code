package com.sf.module.operation.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.dispatch.domain.SchedulingRepository;
import com.sf.module.operation.domain.GroupBaseInfo;

public class GroupBaseInfoDao extends BaseEntityDao<GroupBaseInfo> implements IGroupBaseInfoDao {
	private final static String YYYY_MM_DD = "yyyy-MM-dd";

	// 网点权限递归树SQL--[DEPT_ID]
	private static String DEPT_ID_TREE_SQL = "select dept_id\n" + "  from tm_department\n" + " where delete_flg = 0\n" + " start with dept_id = ?\n"
			+ "connect by prior dept_code = parent_dept_code";

	// 用户网点权限递归树SQL--[DEPT_ID && USER_ID]
	private static String USER_DEPT_ID_TREE_SQL = "select a.dept_id\n " + "  from ts_user_dept a,\n " + "       (select t.dept_id\n "
			+ "          from tm_department t\n " + "         where t.delete_flg = 0\n " + "         start with t.dept_id = ?\n "
			+ "        connect by prior t.dept_code = t.parent_dept_code) b\n " + " where a.user_id = ?\n " + "   and b.dept_id = a.dept_id";

	public IPage<GroupBaseInfo> findPageByPage(GroupBaseInfo groupBaseInfo, Boolean isRecursion, Long userId, int pageSize, int pageIndex) {
		DetachedCriteria criteria = generateCriteria(groupBaseInfo, isRecursion, userId);
		return super.findPageBy(criteria, pageSize, pageIndex);
	}

	/**
	 * 根据网点编号设置查询条件（递归网点）
	 * 
	 * @param criteria
	 *            -- 查询准则
	 * @param deptField
	 *            --网点字段(条件匹配对应网点字段名称)
	 * @param deptId
	 *            --部门编号
	 * @param userId
	 *            --用户编号(1:所有网点,否则当前用户拥有权限的网点及下级网点)
	 */
	public static void addDeptIdCriteria(DetachedCriteria criteria, String deptField, long deptId, long userId) {
		if (1 == userId) {
			String sql = deptField + " in (" + DEPT_ID_TREE_SQL + ")";
			criteria.add(Restrictions.sqlRestriction(sql, deptId, new LongType()));
			return;
		}

		String sql = deptField + " in (" + USER_DEPT_ID_TREE_SQL + ")";
		Object[] params = new Object[] { deptId, userId };
		Type[] types = new Type[] { new LongType(), new LongType() };
		criteria.add(Restrictions.sqlRestriction(sql, params, types));
	}

	public List<GroupBaseInfo> findAllBaseInfos(GroupBaseInfo groupBaseInfo, Boolean isRecursion, Long userId) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = generateCriteria(groupBaseInfo, isRecursion, userId);
		return super.findBy(criteria);
	}

	/**
	 * 返回当前日期 不包含时间
	 * 
	 * @return
	 */
	public static Date currentDt() {
		SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {
		}
		return now;
	}

	private DetachedCriteria generateCriteria(GroupBaseInfo groupBaseInfo, Boolean isRecursion, Long userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(super.entityClass);
		try {
			String queryValue = null;
			queryValue = groupBaseInfo.getGroupName();
			if (StringUtils.isNotEmpty(queryValue)) {
				criteria.add(Restrictions.eq("groupName", queryValue));
			}
			queryValue = groupBaseInfo.getGroupCode();
			if (StringUtils.isNotEmpty(queryValue)) {
				criteria.add(Restrictions.eq("groupCode", queryValue));
			}

			Long deptId = new Long(groupBaseInfo.getDeptId());
			if (isRecursion) {
				addDeptIdCriteria(criteria, "{alias}.DEPT_ID", deptId, userId);
			} else {
				criteria.add(Restrictions.eq("deptId", deptId));
			}
			Integer status = groupBaseInfo.getStatus();
			if (status == 1) {
				// 有效 失效日期必须大于等于当前日期
				criteria.add(Restrictions.or(Restrictions.ge("disableDt", currentDt()), Restrictions.isNull("disableDt")));

			}
			if (status == 2) {
				criteria.add(Restrictions.lt("disableDt", currentDt()));
				// 失效 当前日期大于失效日期
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return criteria;
	}

	public boolean GroupNameExistInDept(String groupName, long deptId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupBaseInfo.class);
		criteria.add(Expression.eq("groupName", groupName));
		criteria.add(Expression.eq("deptId", new Long(deptId)));
		// TODO Auto-generated method stub
		return super.findBy(criteria).size() > 0;
	}

	public boolean GroupCodeExistInAll(String groupCode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupBaseInfo.class);
		criteria.add(Expression.eq("groupCode", groupCode));
		return super.findBy(criteria).size() > 0;
	}

	public boolean noticeHasEmployee(Long deptId) {
		// 当前网点下存在失效日期在7天内则需要进行提醒
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupBaseInfo.class);
		criteria.add(Expression.eq("deptId", new Long(deptId)));
		Date dt = currentDt();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 7);
		Date lastDt = calendar.getTime();
		criteria.add(Expression.gt("disableDt", dt));
		criteria.add(Expression.le("disableDt", lastDt));
		// TODO Auto-generated method stub
		return super.findBy(criteria).size() > 0;
	}

	public List<GroupBaseInfo> noticeShowList(Long deptId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GroupBaseInfo.class);
		criteria.add(Expression.eq("deptId", new Long(deptId)));
		Date dt = currentDt();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 7);
		Date lastDt = calendar.getTime();
		criteria.add(Expression.gt("disableDt", dt));
		criteria.add(Expression.le("disableDt", lastDt));
		// TODO Auto-generated method stub
		return super.findBy(criteria);
	}

	@Transactional
	public boolean queryGroupValidity(Long groupId, Long departmentId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery("SELECT  COUNT(*) TOTALSIZE FROM  tm_oss_employee  t  where t.group_id = ? and t.dept_id = ?  ")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, groupId);
		query.setParameter(1, departmentId);
		List<Map<String, Object>> searchSchedulingCountPage = query.list();
		return ((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue() > 0;
	}

}
