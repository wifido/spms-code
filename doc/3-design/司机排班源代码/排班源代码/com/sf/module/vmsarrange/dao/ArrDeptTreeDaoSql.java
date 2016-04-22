package com.sf.module.vmsarrange.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

public class ArrDeptTreeDaoSql {

	/**
	 * 根据网点编号设置查询条件（递归网点）
	 * @param criteria  -- 查询准则
	 * @param deptField --网点字段(条件匹配对应网点字段名称)
	 * @param deptId    --部门编号
	 * @param userId    --用户编号(1:所有网点,否则当前用户拥有权限的网点及下级网点)
	 */
	public static void addDeptIdCriteria(DetachedCriteria criteria, String deptField, long deptId, long userId) {
		if (1 == userId) {
			String sql = deptField + " in (" + DEPT_ID_TREE_SQL + ")";
			criteria.add(Restrictions.sqlRestriction(sql, deptId,
					new LongType()));
			return;
		}

		String sql = deptField + " in (" + USER_DEPT_ID_TREE_SQL + ")";
		Object[] params = new Object[] { deptId, userId };
		Type[] types = new Type[] { new LongType(), new LongType() };
		criteria.add(Restrictions.sqlRestriction(sql, params, types));
	}
	
	/**
	 * 根据网点代码设置查询条件（递归网点）
	 * @param criteria  -- 查询准则
	 * @param deptField --网点字段(条件匹配对应网点字段名称)
	 * @param deptCode  --网点代码
	 * @param userId    --用户编号(1:所有网点,否则当前用户拥有权限的网点及下级网点)
	 */
	public static void addDeptCodeCriteria(DetachedCriteria criteria, String deptField, String deptCode, long userId) {
		if (1 == userId) {
			String sql = deptField + " in (" + DEPT_CODE_TREE_SQL_1 + ")";
			criteria.add(Restrictions.sqlRestriction(sql, deptCode,
					new StringType()));
			return;
		}

		String sql = deptField + " in (" + USER_DEPT_CODE_TREE_SQL_1 + ")";
		Object[] params = new Object[] { deptCode, userId };
		Type[] types = new Type[] { new StringType(), new LongType() };
		criteria.add(Restrictions.sqlRestriction(sql, params, types));
	}
	
	/**
	 * 根据网点编号及传入的参数生成标准SQL查询语句
	 * @param deptField --网点字段名称
	 * @param deptId    --网点ID
	 * @param userId    --用户ID
	 * @param sql       --查询语句
	 * @param params    --(查询条件参数)
	 */
	public static void addDeptIdSql(String deptField, long deptId, long userId, StringBuilder sql, List<Object> params) {
		if (1 == userId) {
			sql.append(deptField);
			sql.append(" in (");
			sql.append(DEPT_ID_TREE_SQL);
			sql.append(")");
			params.add(deptId);
			return;
		}

		sql.append(deptField);
		sql.append(" in (");
		sql.append(USER_DEPT_ID_TREE_SQL);
		sql.append(")");
		params.add(deptId);
		params.add(userId);
	}
	
	/**
	 * 根据网点代码及传入的参数生成标准SQL查询语句
	 * @param deptField --网点字段名称
	 * @param deptCode  --网点代码
	 * @param userId    --用户ID
	 * @param sql       --查询语句
	 * @param params    --(查询条件参数)
	 */
	public static void addDeptCodeSql(String deptField, String deptCode, long userId, StringBuilder sql, List<Object> params) {
		if (1 == userId) {
			sql.append(deptField);
			sql.append(" in (");
			sql.append(DEPT_CODE_TREE_SQL_2);
			sql.append(")");
			params.add(deptCode);
			return;
		}

		sql.append(deptField);
		sql.append(" in (");
		sql.append(USER_DEPT_CODE_TREE_SQL_2);
		sql.append(")");
		params.add(deptCode);
		params.add(userId);
	}
	
	/**
	 * 递归网点查询条件,针对deptId所加的条件,传入的参数为deptCode
	 * @param criteria
	 * @param deptField
	 * @param deptCode
	 * @param userId
	 */
	public static void addDeptIdCriteriaByDeptCode(DetachedCriteria criteria, String deptField, String deptCode, long userId) {
		
		if (1 == userId) {
			String sql = deptField + " in (" + DEPT_ID_TREE_SQL_1 + ")";
			criteria.add(Restrictions.sqlRestriction(sql, deptCode,
					new StringType()));
			return;
		}

		String sql = deptField + " in (" + USER_DEPT_ID_TREE_SQL_1 + ")";
		Object[] params = new Object[] { deptCode, userId };
		Type[] types = new Type[] { new StringType(), new LongType() };
		criteria.add(Restrictions.sqlRestriction(sql, params, types));
	}
	
	// 网点权限递归树SQL--[DEPT_ID]
	private static String DEPT_ID_TREE_SQL =
		"select dept_id\n" +
		"  from tm_department\n" +
		" where delete_flg = 0\n" +
		" start with dept_id = ?\n" +
		"connect by prior dept_code = parent_dept_code";

	// 用户网点权限递归树SQL--[DEPT_ID && USER_ID]
	private static String USER_DEPT_ID_TREE_SQL =
		"select a.dept_id\n " +
		"  from ts_user_dept a,\n " +
		"       (select t.dept_id\n " +
		"          from tm_department t\n " +
		"         where t.delete_flg = 0\n " +
		"         start with t.dept_id = ?\n " +
		"        connect by prior t.dept_code = t.parent_dept_code) b\n " +
		" where a.user_id = ?\n " +
		"   and b.dept_id = a.dept_id";
	
	// 网点权限递归树SQL--[DEPT_CODE]
	private static String DEPT_CODE_TREE_SQL_1 =
		"select dept_code\n" +
		"  from tm_department\n" +
		" where delete_flg = 0\n" +
		" start with dept_code = ?\n" +
		"connect by prior dept_code = parent_dept_code";
	
	// 网点权限递归树SQL--[DEPT_CODE]
	private static String DEPT_CODE_TREE_SQL_2 =
		"select dept_code\n" +
		"  from tm_department\n" +
		" where delete_flg = 0\n" +
		" start with dept_code = ?\n" +
		"connect by prior dept_code = parent_dept_code";

	// 用户网点权限递归树SQL--[DEPT_CODE && USER_ID]
	private static String USER_DEPT_CODE_TREE_SQL_1 =
		"select b.dept_code\n " +
		"  from ts_user_dept a,\n " +
		"       (select t.dept_id,t.dept_code\n " +
		"          from tm_department t\n " +
		"         where t.delete_flg = 0\n " +
		"         start with t.dept_code = ?\n " +
		"        connect by prior t.dept_code = t.parent_dept_code) b\n " +
		" where a.user_id = ?\n " +
		"   and b.dept_id = a.dept_id";
	
	// 用户网点权限递归树SQL--[DEPT_CODE && USER_ID]
	private static String USER_DEPT_CODE_TREE_SQL_2 =
		"select b.dept_code\n" +
		"  from ts_user_dept a,\n" + 
		"       (select t.dept_id, t.dept_code\n" + 
		"          from tm_department t\n" + 
		"         where t.delete_flg = 0\n" + 
		"         start with t.dept_code = ?\n" + 
		"        connect by prior t.dept_code = t.parent_dept_code) b\n" + 
		" where a.user_id = ?\n" + 
		"   and b.dept_id = a.dept_id";

	// 网点权限递归树SQL--[DEPT_CODE]
	private static String DEPT_ID_TREE_SQL_1 =
		"select dept_id\n" +
		"  from tm_department\n" +
		" where delete_flg = 0\n" +
		" start with dept_code = ?\n" +
		"connect by prior dept_code = parent_dept_code";
	
	// 用户网点权限递归树SQL--[DEPT_CODE&USER_ID]
	private static String USER_DEPT_ID_TREE_SQL_1 =
		"select b.dept_id\n " +
		"  from ts_user_dept a,\n " +
		"       (select t.dept_id,t.dept_code\n " +
		"          from tm_department t\n " +
		"         where t.delete_flg = 0\n " +
		"         start with t.dept_code = ?\n " +
		"        connect by prior t.dept_code = t.parent_dept_code) b\n " +
		" where a.user_id = ?\n " +
		"   and b.dept_id = a.dept_id";
}
