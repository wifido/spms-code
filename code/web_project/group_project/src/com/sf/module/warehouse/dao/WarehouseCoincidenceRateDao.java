package com.sf.module.warehouse.dao;

import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class WarehouseCoincidenceRateDao extends BaseDao {

	@Transactional(readOnly = true)
	public HashMap<String, Object> query(String departmentCode, String yearMonth, int start, int limit) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuilder sqlString = constructionSQL();
		Query query = session.createSQLQuery(sqlString.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameter(departmentCode, yearMonth, query);
		// 总行数
		int size = query.list().size();
		// 设置分页参数
		setPagingParameter(start, limit, query);
		dataMap.put(TOTAL_SIZE, size);
		dataMap.put(ROOT, query.list());
		return dataMap;
	}

	private StringBuilder constructionSQL() {
		StringBuilder sqlString  = new StringBuilder();
		sqlString.append(" select r.department_code dept_code, ");
		sqlString.append(" r.year_month,");
		sqlString.append(" r.sched_agree_rate * 100 sched_agree_rate, ");
		sqlString.append(" r.sched_agree_count, ");
		sqlString.append(" r.sched_total,");
		sqlString.append(" r.working_emp_count");
		sqlString.append(" from spms.tt_wareh_scheduled_agree_rate r, tm_department dept ");
		sqlString.append(" WHERE r.department_code = dept.dept_code ");
		sqlString.append(" AND DEPT_ID in (SELECT TD.DEPT_ID ");
		sqlString.append(" FROM TM_DEPARTMENT TD ");
		sqlString.append(" START WITH  TD.DEPT_CODE = ? ");
		sqlString.append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)");
		sqlString.append(" AND YEAR_MONTH = ? ");
		sqlString.append(" order by r.department_code ");
		return sqlString;
	}
	
	public void setQueryParameter(String departmentCode, String yearMonth, Query query) {
		query.setParameter(0, departmentCode);
		query.setParameter(1, yearMonth);
	}
	
	private void setPagingParameter(int start, int limit, Query query) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
	}
}
