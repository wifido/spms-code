package com.sf.module.report.dao;


import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;


public class WatchkeeperCountDao extends BaseDao{
	
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
		
	@SuppressWarnings("rawtypes")
	@Transactional
	public List export(String departmentCode, String yearMonth) {
	    StringBuilder sqlString = constructionSQL();
	
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sqlString.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);;
		setQueryParameter(departmentCode, yearMonth, query);
		return query.list();
	}
	
	private StringBuilder constructionSQL() {
		StringBuilder sqlString  = new StringBuilder();
		sqlString.append("select TO_CHAR(T.SHEDULE_DT,'YYYY-MM-DD') SHEDULE_DT, ");
		sqlString.append("D.AREA_CODE,");
		sqlString.append("D.DEPT_CODE, ");
		sqlString.append("E.EMP_CODE, ");
		sqlString.append("E.EMP_NAME, ");
		sqlString.append("E.EMP_DUTY_NAME, ");
		sqlString.append("B.START1_TIME || '-' || CASE WHEN B.START3_TIME IS NOT NULL THEN B.END3_TIME WHEN B.START2_TIME IS NOT NULL THEN B.END2_TIME ELSE B.END1_TIME  END  SHEDULE_TIME, ");
		sqlString.append(" B.SCHEDULE_CODE  from ");
		sqlString.append(" tt_pb_shedule_by_day   T, ");
		sqlString.append(" tm_oss_employee   	   E, ");
		sqlString.append(" tm_pb_schedule_base_info B, ");
		sqlString.append(" tm_department            D ");
		sqlString.append(" where E.emp_code = T.emp_code ");
		sqlString.append(" and T.dept_id = B.dept_id ");
		sqlString.append(" and T.shedule_code = B.schedule_code ");
		sqlString.append(" and D.dept_id = B.dept_id ");
		sqlString.append(" and T.shedule_code like 'ZB%'  ");
		sqlString.append(" AND (E.DIMISSION_DT is null or TO_CHAR(E.DIMISSION_DT, 'YYYY-MM-DD') > TO_CHAR(T.SHEDULE_DT, 'YYYY-MM-DD'))  ");
		sqlString.append(" AND B.ym = TO_CHAR(T.SHEDULE_DT,'YYYY-MM')  ");
		sqlString.append(" AND T.SHEDULE_DT = ?  ");
		sqlString.append(" AND D.DEPT_ID in (SELECT TD.DEPT_ID  ");
		sqlString.append(" FROM TM_DEPARTMENT TD ");
		sqlString.append(" START WITH  TD.DEPT_CODE = ?  ");
		sqlString.append(" CONNECT BY PRIOR TD.DEPT_CODE = TD.PARENT_DEPT_CODE) ");
		return sqlString;
	}
	
	@SuppressWarnings("deprecation")
	public void setQueryParameter(String departmentCode, String yearMonth, Query query) {		
		Date date =new Date(yearMonth);
		query.setParameter(0, date);
		query.setParameter(1, departmentCode);
	}
	
	private void setPagingParameter(int start, int limit, Query query) {
		query.setFirstResult(start);
		query.setMaxResults(limit);
	}
}
