package com.sf.module.warehouse.dao;

import static com.sf.module.common.domain.Constants.*;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.util.HashMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class WarehouseSchedulingAnalysisReportDao extends BaseDao {
	
	private static final String SQL_CALL_PROCEDURE_WAREH_COUNT_SCHE_REPORT = " { Call WAREH_COUNT_SCHE_REPORT(?,?) }";
	
	private static final String SQL_QUERY_ANYLYSIS_REPORT = ""
			+ " SELECT round(DECODE(T.SCHE_A_EMP_NUM, \n "
			+ "                     0, \n "
			+ "                     0, \n "
			+ "                     T.SCHE_A_DAYS / T.SCHE_A_EMP_NUM), \n "
			+ "              2) A_SCHEDULING_DAYS_AVG, \n "
			+ "			  CASE   WHEN T.SCHE_A_EMP_NUM = 0 OR T.SCHE_A_DAYS = 0 THEN  0   else "
			+ "        round(DECODE(T.SCHE_A_EMP_NUM, \n "
			+ "                     0, \n "
			+ "                     0, \n "
			+ "                     (T.SCHE_A_HOURS / T.SCHE_A_EMP_NUM) / \n "
			+ "                     (T.SCHE_A_DAYS / T.SCHE_A_EMP_NUM)), \n "
			+ "              2)  end A_SCHEDULING_HOURS_AVG, \n "
			+ "        round(DECODE(T.SCHE_C_EMP_NUM, \n "
			+ "                     0, \n "
			+ "                     0, \n "
			+ "                     T.SCHE_C_DAYS / T.SCHE_C_EMP_NUM), \n "
			+ "              2) C_SCHEDULING_DAYS_AVG, \n "
			
			+ "         CASE   WHEN T.SCHE_C_EMP_NUM = 0 OR T.SCHE_C_DAYS = 0 THEN  0   else "
			+ "        round(DECODE(T.SCHE_C_EMP_NUM, \n "
			+ "                     0, \n "
			+ "                     0, \n "
			+ "                     (T.SCHE_C_HOURS / T.SCHE_C_EMP_NUM) / \n "
			+ "                     (T.SCHE_C_DAYS / T.SCHE_C_EMP_NUM)), \n "
			+ "              2) end  C_SCHEDULING_HOURS_AVG, \n "
			+ "              D.AREA_CODE, \n "
			+ "        T.* \n "
			+ "   FROM TT_WAREH_SCHEDULED_REPORT T, \n "
			+ "        (SELECT DEPT_CODE ,AREA_CODE  \n "
			+ "           FROM TM_DEPARTMENT \n "
			+ "          WHERE DELETE_FLG = 0 \n "
			+ "          START WITH DEPT_CODE = ? \n "
			+ "         CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) D \n "
			+ "  WHERE T.DEPARTMENT_CODE = D.DEPT_CODE \n "
			+ "    AND T.SCHE_MONTH = ? \n ";
			

	@Transactional
	public HashMap<String, Object> query(String departmentCode, String yearMonth, int start, int limit) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
//		Query query = session.createSQLQuery(SQL_CALL_PROCEDURE_WAREH_COUNT_SCHE_REPORT);
//		query.setParameter(1, departmentCode);
//		query.setParameter(0, yearMonth);
//		query.executeUpdate();
		Query query = session.createSQLQuery(SQL_QUERY_ANYLYSIS_REPORT).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameter(departmentCode, yearMonth, query);
		// 总行数
		int size = query.list().size();
		// 设置分页参数
		setPagingParameter(start, limit, query);
		dataMap.put(TOTAL_SIZE, size);
		dataMap.put(ROOT, query.list());
		return dataMap;
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
