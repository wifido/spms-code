package com.sf.module.warehouse.dao;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.warehouse.dao.WarehouseClassHql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.util.QueryHelper;
import com.sf.module.common.util.StringUtil;

public class WarehouseClassDao extends QueryHelper {

	private static final String SCHEDULING_CODE = "scheduling_code";

	public int queryClassInformationCount(HashMap<String, String> queryParameter) {
		String hql = WarehouseClassHql.QUERY_CLASS_INFORMATION_COUNT
		        + getQueryConditions(queryParameter, getQueryColumn(StringUtil.isNotBlank(queryParameter.get(IS_CHECK_OF_NETWORK))));
		List<Map<String, Object>> list = getQuery(queryParameter, hql);
		return Integer.parseInt(list.get(0).get(TOTALSIZE).toString());
	}

	public List queryClassInformation(HashMap<String, String> queryParameter) {
		return getQuery(queryParameter, getQuerySql(queryParameter));
	}

	private String getQuerySql(HashMap<String, String> queryParameter) {
		return "select * from (select t.*,rownum rn from (" + QUERY_CLASS_INFORMATION
		        + getQueryConditions(queryParameter, getQueryColumn(StringUtil.isNotBlank(queryParameter.get(IS_CHECK_OF_NETWORK))))
		        + " order by bi.schedule_code) t)" + "where rn > :start and rn <= :limit";
	}

	public HashMap<String, String> getQueryColumn(boolean isCheckOfNetwork) {
		HashMap<String, String> queryAttributeColumns = new HashMap<String, String>();
		queryAttributeColumns.put(COLUMN_DEPT_CODE, isCheckOfNetwork ? CONDITION_DEPT_CODE : CONDITION_THIS_DEPT_CODE);
		queryAttributeColumns.put(COLUMN_ENABLE_DT, CONDITION_ENABLE_DT);
		queryAttributeColumns.put(COLUMN_DISABLE_DT, CONDITION_DISABLE_DT);
		return queryAttributeColumns;
	}

	@Transactional
	public boolean addWarehouseClass(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(ADD_CLASS_INFO_SQL);
		setQueryParameterValue(queryCriteria, query);
		query.executeUpdate();
		return true;
	}

	@Transactional
	public boolean updateWarehouseClass(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(UPDATE_CLASS_INFO_SQL);
		setQueryParameterValue(queryCriteria, query);
		query.executeUpdate();
		return true;
	}

	@Transactional
	public boolean validClassNameExist(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(VALID_CLASS_NAME_EXIST_SQL);
		setQueryParameterValue(queryCriteria, query);
		return query.list().isEmpty();
	}

	@Transactional
	public List<Map<String, Object>> queryClassDetailByDeptId(Long deptId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_CLASS_BY_DEPARTMENT_ID_SQL).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("dept_id", deptId);
		return query.list();
	}

	@Transactional
	public boolean delete(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(getDeleteHql(queryCriteria));
		setQueryParameterValue(queryCriteria, query);
		query.executeUpdate();
		return true;
	}

	public boolean queryExistReference(HashMap<String, String> queryCriteria) {
		String hql = EXIST_REFERENCE.replace("${parameter}", queryCriteria.get("CLASSIDS"));
		return getQuery(queryCriteria, hql).size() > 0;
	}

	public String getDeleteHql(HashMap<String, String> queryCriteria) {
		HashMap<String, String> conditionsMap = new HashMap<String, String>();
		conditionsMap.put("CLASSIDS", " AND T.SCHEDULE_ID IN ($$)");
		return DELETE_CLASS_SQL + getQueryConditions(queryCriteria, conditionsMap);
	}

	public List queryClassTimeByScheduleCode(HashMap<String, String> queryParameter) {
		String hql = "select  bi.start1_time, bi.end1_time , bi.IS_CROSS_DAY" + " from tm_pb_schedule_base_info bi, tm_department dept "
		        + " where bi.dept_id = dept.dept_id    AND bi.class_type = '2' "
		        + " and bi.schedule_code IN (:in_scheduleCode)  AND dept.dept_code = :deptCode ";

		return getQuery(queryParameter, hql);
	}
}
