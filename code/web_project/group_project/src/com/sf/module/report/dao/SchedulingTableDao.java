package com.sf.module.report.dao;

import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

public class SchedulingTableDao extends BaseDao{
	
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
	
	@Transactional
	public List export(String departmentCode, String yearMonth) {
	    StringBuilder sqlString = constructionSQL();
	
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sqlString.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);;
		setQueryParameter(departmentCode, yearMonth, query);
		return query.list();
	}
	
	private StringBuilder constructionSQL() {
		StringBuilder sqlString  = new StringBuilder();
		sqlString.append(" SELECT DEPT_ID, ");
		sqlString.append(" MONTH_ID,");
		sqlString.append(" AREA_CODE,");
		sqlString.append(" DEPT_CODE,");
		sqlString.append(" EMP_CODE,");
		sqlString.append(" EMP_NAME,");
		sqlString.append(" PERSK_TXT,");
		sqlString.append(" to_char(SF_DATE,'YYYY-MM-DD') SF_DATE,");
		sqlString.append(" EMP_STATUS,");
		sqlString.append(" SHEDULE_NUM,");
		sqlString.append(" GROUP_NUM,");
		sqlString.append(" PROCESS_NUM,");
		sqlString.append(" LENGTH_TIME_OF_DAY,");
		sqlString.append(" REST_DAYS,");
		sqlString.append(" TOTAL_ATTENDANCE");
		sqlString.append(" FROM OPERATION_COUNT_SHEDULING ");
		sqlString.append(" WHERE MONTH_ID = ? ");
		sqlString.append(" AND DEPT_ID in (SELECT TD.DEPT_ID ");
		sqlString.append(" FROM TM_DEPARTMENT TD ");
		sqlString.append(" START WITH  TD.DEPT_CODE = ? ");
		sqlString.append(" CONNECT BY PRIOR TD.DEPT_CODE = TD.PARENT_DEPT_CODE)");
		return sqlString;
	}
	
	public void setQueryParameter(String departmentCode, String yearMonth, Query query) {
		query.setParameter(0, yearMonth);
		query.setParameter(1, departmentCode);
	}
	
	private void setPagingParameter(int start, int limit, Query query) {
		query.setFirstResult(start);
		query.setMaxResults(limit);
	}
	
}
