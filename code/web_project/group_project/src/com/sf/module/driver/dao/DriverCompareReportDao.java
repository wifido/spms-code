package com.sf.module.driver.dao;

import com.sf.framework.server.base.dao.BaseDao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.util.Clock.allDateInRange;
import static com.sf.module.common.util.StringUtil.removeHorizontalLine;
import static com.sf.module.driver.domain.ComparedReportRepository.COL_DEPT_CODE;
import static com.sf.module.driver.domain.ComparedReportRepository.COL_ERROR_TYPE;
import static com.sf.module.driver.domain.ComparedReportRepository.COL_USER_ID;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

@Repository
@Scope("prototype")
public class DriverCompareReportDao extends BaseDao {

	private String buildSqlForQuerying(String startDay, String endDay) {
		StringBuilder stringBuilder = new StringBuilder();

		// stringBuilder.append("  SELECT ROWNUM, T.* FROM (SELECT * FROM TT_COMPARED_REPORT WHERE COMPARE_RESULT IN (:ERROR_TYPE) ) ");
		// POVIT行转列，列名错误修改（ID等会不等选出，隐匿GROUP BY）
		stringBuilder.append("  SELECT ROWNUM, T.* FROM (SELECT EMPLOYEE_CODE,EMPLOYEE_NAME,DEPARTMENT_CODE,DEPARTMENT_NAME,AREA_CODE,AREA_NAME,DAY_MONTH,COMPARE_RESULT "
						+ "FROM TT_COMPARED_REPORT WHERE COMPARE_RESULT IN (:ERROR_TYPE)  "
						+ "AND DAY_MONTH BETWEEN '"
						+ removeHorizontalLine(startDay)
						+ "' AND '"
						+ removeHorizontalLine(endDay) + "')");
		stringBuilder.append("  PIVOT ( MAX(COMPARE_RESULT) FOR DAY_MONTH IN  (");

		List<String> allDays = allDateInRange(startDay, endDay);
		List<String> temporarySqlSnippet = newArrayList();

		for (int index = 0; index < allDays.size(); index++) {
			temporarySqlSnippet.add("'" + allDays.get(index) + "' "+ String.format("DAY%d", index + 1));
		}

		stringBuilder.append(on(",").join(temporarySqlSnippet));

		stringBuilder.append(")) T WHERE DEPARTMENT_CODE IN "
						+ "       (SELECT DISTINCT D.DEPT_CODE "
						+ "          FROM (SELECT DEPT_CODE, DEPT_ID "
						+ "                  FROM TM_DEPARTMENT "
						+ "                 WHERE DELETE_FLG = 0 "
						+ "                 START WITH DEPT_CODE = :DEPT_CODE "
						+ "                CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) D, "
						+ "               TS_USER_DEPT UD "
						+ "         WHERE D.DEPT_ID = UD.DEPT_ID "
						+ "           AND (UD.USER_ID = :USER_ID" 
						+" OR :USER_ID IN (SELECT USER_ID FROM TS_USER WHERE STATUS = 'root')))");

		return stringBuilder.toString();
	}

	@Transactional
	public List queryComparedDataByMonth(String startDay, String endDay, String departmentCode, Integer[] errorType, String userId) 
			throws HibernateException {
		Session session = getHibernateTemplate().getSessionFactory().openSession();

		Query query = session.createSQLQuery(buildSqlForQuerying(startDay, endDay)).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameterList(COL_ERROR_TYPE, errorType);
		query.setParameter(COL_DEPT_CODE, departmentCode);
		query.setParameter(COL_USER_ID, userId);
		return query.list();
	}

}
