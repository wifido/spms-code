package com.sf.module.warehouse.dao;

import static com.sf.module.common.util.StringUtil.isNotBlank;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.warehouse.domain.WarehouseAttendance;

@Repository
public class WarehouseAttendanceDao extends BaseEntityDao<WarehouseAttendance> {
	private static final String IN = "in_";

	@Transactional
	public int queryAttendanceCount(HashMap<String, String> queryParameter) {
		String sql = WarehouseAttendanceRepository.SQL_QUERY_ATTENDANCE_COUNT.replace(
		        WarehouseAttendanceRepository.STRING_DYNAMIC_PARAMETER,
		        buildDynamicParameter(queryParameter));
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		setQueryParameterValue(queryParameter, query);
		return Integer.parseInt(query.list().get(0).toString());
	}

	@Transactional
	public List queryAttendanceList(HashMap<String, String> queryParameter) {
		String sql = WarehouseAttendanceRepository.SQL_QUERY_ATTENDANCE_LIST.replace(
		        WarehouseAttendanceRepository.STRING_DYNAMIC_PARAMETER,
		        buildDynamicParameter(queryParameter));
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}

	private String buildDynamicParameter(HashMap<String, String> queryParameter) {
		StringBuilder stringBuilder = new StringBuilder();
		if (isNotBlank(queryParameter.get("EMP_CODE"))) {
			stringBuilder.append(" and t.EMP_CODE =:EMP_CODE");
		}
		if (isNotBlank(queryParameter.get("EMP_NAME"))) {
			stringBuilder.append(" and t.EMP_NAME =:EMP_NAME");
		}
		if (isNotBlank(queryParameter.get("WORK_TYPE"))) {
			stringBuilder.append(" and t.PERSON_TYPE =:WORK_TYPE");
		}
		return stringBuilder.toString();
	}

	private void setQueryParameterValue(HashMap<String, String> queryParameter, Query query) {
		String[] parameters = query.getNamedParameters();
		for (String param : parameters) {
			if (param.startsWith(IN)) {
				query.setParameterList(param, queryParameter.get(param).split(","));
				continue;
			}
			query.setParameter(param, queryParameter.get(param));
		}
	}
}
