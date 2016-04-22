package com.sf.module.driver.dao;

import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class SendDriverMailDao extends BaseDao {
	@Transactional
	public List generateAccessories(Long userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("   WITH USER_DEPT AS");
		sql.append("    (SELECT distinct D.DEPT_CODE");
		sql.append("       FROM TS_USER_DEPT T, TM_DEPARTMENT D");
		sql.append("      WHERE T.DEPT_ID = D.DEPT_ID");
		sql.append("        AND T.USER_ID = :userId),");
		sql.append("   COMPARED_REPORT AS");
		sql.append("    (SELECT COUNT(*) CON, T.DEPARTMENT_CODE, T.COMPARE_RESULT");
		sql.append("       FROM TT_COMPARED_REPORT T");
		sql.append("       JOIN USER_DEPT");
		sql.append("         ON USER_DEPT.DEPT_CODE = T.DEPARTMENT_CODE");
		sql.append("        AND t.day_month BETWEEN");
		sql.append("            TO_CHAR(TRUNC(SYSDATE - 7, 'D') + 1, 'YYYYMMDD') AND");
		sql.append("            TO_CHAR(TRUNC(SYSDATE - 1, 'D'), 'YYYYMMDD')");
		sql.append("      GROUP BY T.DEPARTMENT_CODE, T.COMPARE_RESULT),");
		sql.append("   SCH_EMP_NUM AS");
		sql.append("    (SELECT T.DEPARTMENT_CODE, COUNT(T.EMP_CODE) COUNT_EMP");
		sql.append("       FROM (SELECT TD.DEPARTMENT_CODE, E.EMP_CODE");
		sql.append("               FROM TT_DRIVER_SCHEDULING TD");
		sql.append("               JOIN TM_OSS_EMPLOYEE E");
		sql.append("                 ON TD.EMPLOYEE_CODE = E.EMP_CODE");
		sql.append("                AND E.SF_DATE <= TRUNC(SYSDATE - 1, 'D') + 1");
		sql.append("                AND (E.DIMISSION_DT IS NULL OR");
		sql.append("                    E.DIMISSION_DT >= TRUNC(SYSDATE - 1, 'D') + 1)");
		sql.append("                AND TD.DAY_OF_MONTH BETWEEN");
		sql.append("                    TO_CHAR(TRUNC(SYSDATE - 7, 'D') + 1, 'YYYYMMDD') AND");
		sql.append("                    TO_CHAR(TRUNC(SYSDATE - 1, 'D'), 'YYYYMMDD')");
		sql.append("                AND TD.SCHEDULING_TYPE = '1'");
		sql.append("                AND TD.YEAR_WEEK = TO_CHAR(SYSDATE - 1, 'YYYY-IW')");
		sql.append("               JOIN USER_DEPT");
		sql.append("                 ON TD.DEPARTMENT_CODE = USER_DEPT.DEPT_CODE");
		sql.append("              GROUP BY TD.DEPARTMENT_CODE, E.EMP_CODE) T");
		sql.append("      GROUP BY T.DEPARTMENT_CODE)");
		sql.append("   SELECT MAX(AR.DEPT_NAME) AREA_NAME,");
		sql.append("          COMPARED_REPORT.DEPARTMENT_CODE DEPARTMENT_CODE,");
		sql.append("          MAX(D.DEPT_NAME) DEPT_NAME,");
		sql.append("          MAX(SCH_EMP_NUM.COUNT_EMP) EMP_SCHE_COUNT,");
		sql.append("          SUM(DECODE(COMPARED_REPORT.COMPARE_RESULT, '1', CON, 0)) AS NON_ATTENDANCE_SCHE,");
		sql.append("          SUM(DECODE(COMPARED_REPORT.COMPARE_RESULT, '2', CON, 0)) AS NON_SCHE_ATTENDANCE,");
		sql.append("          SUM(DECODE(COMPARED_REPORT.COMPARE_RESULT, '3', CON, 0)) AS ATTENDANCE_LESS_SCHE,");
		sql.append("          SUM(DECODE(COMPARED_REPORT.COMPARE_RESULT, '4', CON, 0)) AS ATTENDANCE_MORE_SCHE");
		sql.append("     FROM COMPARED_REPORT");
		sql.append("     LEFT JOIN SCH_EMP_NUM");
		sql.append("       ON COMPARED_REPORT.DEPARTMENT_CODE = SCH_EMP_NUM.DEPARTMENT_CODE");
		sql.append("     JOIN TM_DEPARTMENT D");
		sql.append("       ON COMPARED_REPORT.DEPARTMENT_CODE = D.DEPT_CODE");
		sql.append("     JOIN TM_DEPARTMENT AR");
		sql.append("       ON D.AREA_CODE = AR.DEPT_CODE");
		sql.append("    GROUP BY COMPARED_REPORT.DEPARTMENT_CODE");
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter("userId", userId);
		return query.list();
	}
	@Transactional
	public HashMap queryUsers(Long userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select t.username from ts_user t where t.user_id = :userId ");
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter("userId", userId);
		if (query.list().size() > 0)
			return (HashMap) query.list().get(0);
		return null;
	};
}