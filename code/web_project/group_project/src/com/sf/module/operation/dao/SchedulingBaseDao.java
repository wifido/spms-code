/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.SchedulingBase;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 班次基础信息管理的Dao实现类
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public class SchedulingBaseDao extends BaseEntityDao<SchedulingBase> implements
		ISchedulingBaseDao {

	/**
	 * 查询班次基本信息
	 * 
	 * @author 632898 李鹏
	 * @param map
	 * @return list
	 */
	public HashMap querySchedule(HashMap map) {
		HashMap returnMap = new HashMap();
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		Long userId = Long.parseLong(map.get("userId").toString());
		String flag = map.get("flag").toString();
		// 查询语句主体
		sql.append(" SELECT S.*, D.AREA_CODE ,D.DEPT_NAME ,D.DEPT_CODE   FROM TM_PB_SCHEDULE_BASE_INFO S,TM_DEPARTMENT D WHERE S.DEPT_ID = D.DEPT_ID  ");
		// wangjian 查询动作所有信息
		sql.append("AND s.class_type = 1 ");
		// 网点ID
		if (!StringUtils.isEmpty(map.get("deptId"))) {
			// 查询有权限的子网点
			if (CommonUtil.isAdmin(userId)) {
				sql.append(" AND   S.DEPT_ID  IN ( SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0  START WITH DEPT_ID=?  CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) ");
				params.add(map.get("deptId"));

			} else {
				sql.append(" AND   S.DEPT_ID  IN ( SELECT A.DEPT_ID FROM  TS_USER_DEPT A,(SELECT  T.DEPT_ID  FROM TM_DEPARTMENT  T  WHERE DELETE_FLG=0  START WITH DEPT_ID=? CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) B ");
				sql.append("  WHERE A.USER_ID = ?  AND B.DEPT_ID = A.DEPT_ID)  ");
				params.add(map.get("deptId"));
				params.add(userId);
			}
		}
		// 班别名称
		if (!StringUtils.isEmpty(map.get("scheduleName"))) {
			sql.append("AND  S.SCHEDULE_NAME  LIKE ? ");
			params.add("%" + map.get("scheduleName").toString() + "%");
		}
		// 班别代码
		if (!StringUtils.isEmpty(map.get("scheduleCode"))) {
			sql.append(" AND  S.SCHEDULE_CODE = ? ");
			params.add(map.get("scheduleCode").toString());
		}
		
		if (!StringUtils.isEmpty(map.get("ym"))) {
			sql.append(" AND  S.ym = ? ");
			params.add(map.get("ym").toString());
		}

		// 默认按照Id排序
		sql.append("ORDER BY  SCHEDULE_ID  DESC ");

		// 创建新的session
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		// 查询结果以MAP形式存入LIST返回
		Query query = session.createSQLQuery(sql.toString())
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		// 写入参数
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		// 总行数
		int size = query.list().size();
		if (map.get("start") != null)
			query.setFirstResult(Integer.parseInt(map.get("start").toString()));
		if (map.get("limit") != null)
			query.setMaxResults(Integer.parseInt(map.get("limit").toString()));
		// 获取结果集
		List list = query.list();
		returnMap.put("totalSize", size);
		returnMap.put("root", list);

		// 关闭session
		session.close();

		return returnMap;
	}

	public SchedulingBase getScheeduleBaseByCode(final String code,
			final Long deptId) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<SchedulingBase>() {
					public SchedulingBase doInHibernate(Session session)
							throws HibernateException, SQLException {
						String sql = "SELECT * FROM TM_PB_SCHEDULE_BASE_INFO T WHERE T.SCHEDULE_CODE = '"
								+ code
								+ "' and CLASS_TYPE = '1' AND T.DEPT_ID = "
								+ deptId;
						SQLQuery query = session.createSQLQuery(sql).addEntity(
								SchedulingBase.class);
						List lst = query.list();
						if (lst != null && lst.size() > 0) {
							return (SchedulingBase) lst.get(0);
						}
						return null;
					}
				});
	}

}