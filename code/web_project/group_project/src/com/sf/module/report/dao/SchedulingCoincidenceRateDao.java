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

public class SchedulingCoincidenceRateDao extends BaseDao {

	@Transactional(readOnly = true)
	public HashMap<String, Object> query(String departmentCode, String yearMonth, String positionType, int start, int limit) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuilder sqlString = constructionSQL();
		Query query = session.createSQLQuery(sqlString.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameter(departmentCode, yearMonth, positionType, query);
		// 总行数
		int size = query.list().size();
		// 设置分页参数
		setPagingParameter(start, limit, query);
		dataMap.put(TOTAL_SIZE, size);
		dataMap.put(ROOT, query.list());
		return dataMap;
	}
	
	@Transactional(readOnly = true)
	public HashMap<String, Object> queryCoincidenceRateForAreaDepartment(String departmentCode, String yearMonth, String positionType, int start, int limit) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sqlString = buildQueryCoincidenceRateForAreaDepartment();
		Query query = session.createSQLQuery(sqlString).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameter(departmentCode, yearMonth, positionType, query);
		List list = query.list();
		dataMap.put(TOTAL_SIZE, list.size());
		dataMap.put(ROOT, list);
		return dataMap;
	}
	
	@Transactional
	public List queryExport(String departmentCode, String yearMonth, String positionType) {
	    StringBuilder sqlString = constructionSQL();
	
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sqlString.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);;
		setQueryParameter(departmentCode, yearMonth, positionType, query);
		return query.list();
	}

	private StringBuilder constructionSQL() {
		StringBuilder sqlString  = new StringBuilder();
		sqlString.append(" SELECT DEPT_ID, ");
		sqlString.append(" DEPT_CODE, ");
		sqlString.append(" YEAR_MONTH, ");
		sqlString.append(" COINCIDENCE_RATE,");
		sqlString.append(" COINCIDENCERATE_COUNT, ");
		sqlString.append(" DEPT_COUNT");
		sqlString.append(" FROM TT_STATISTICAL_COINCIDENCE ");
		sqlString.append(" WHERE DEPT_ID in (SELECT TD.DEPT_ID ");
		sqlString.append(" FROM TM_DEPARTMENT TD ");
		sqlString.append(" where td.delete_flg = 0 ");
		sqlString.append(" START WITH  TD.DEPT_CODE = ? ");
		sqlString.append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)");
		sqlString.append(" AND YEAR_MONTH = ? ");
		sqlString.append(" AND POSITION_TYPE = ? ");
		return sqlString;
	}
	
	private String buildQueryCoincidenceRateForAreaDepartment(){
		String SQL_FOR_QUERY_AREA_DEPARTMENT_COINCIDENCE_RATE = ""
				+ " SELECT sum(COINCIDENCERATE_COUNT) COINCIDENCERATE_COUNT, \n "
				+ "         sum(DEPT_COUNT) DEPT_COUNT, \n "
				+ "         decode(sum(DEPT_COUNT), \n "
				+ "           0, \n "
				+ "           0, \n "
				+ "         round(sum(COINCIDENCERATE_COUNT) / sum(DEPT_COUNT), 4) * 100) as COINCIDENCE_RATE \n "
				+ "    FROM TT_STATISTICAL_COINCIDENCE \n "
				+ "   WHERE DEPT_ID in \n "
				+ "         (SELECT TD.DEPT_ID \n "
				+ "            FROM TM_DEPARTMENT TD \n "
				+ "            where td.delete_flg = 0 \n "
				+ "           START WITH TD.DEPT_CODE = ? \n "
				+ "          CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) \n "
				+ "     AND YEAR_MONTH = ? \n "
				+ "     AND POSITION_TYPE = ? \n ";
		return SQL_FOR_QUERY_AREA_DEPARTMENT_COINCIDENCE_RATE;
	}
	
	public void setQueryParameter(String departmentCode, String yearMonth, String positionType, Query query) {
		query.setParameter(0, departmentCode);
		query.setParameter(1, yearMonth);
		query.setParameter(2, positionType);
	}
	
	private void setPagingParameter(int start, int limit, Query query) {
			query.setFirstResult(start);
			query.setMaxResults(limit);
	}
}
