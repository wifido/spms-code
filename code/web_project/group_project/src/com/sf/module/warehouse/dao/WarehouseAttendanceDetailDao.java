package com.sf.module.warehouse.dao;

import static com.sf.module.common.util.StringUtil.isNotBlank;
import static com.sf.module.warehouse.dao.WarehouseAttendanceDetailHql.buildQueryTableHead;
import static com.sf.module.warehouse.dao.WarehouseAttendanceDetailHql.getDepartmentSql;
import static com.sf.module.warehouse.dao.WarehouseAttendanceDetailHql.getQuerySql;
import static com.sf.module.warehouse.dao.WarehouseAttendanceDetailHql.getQueryTotalSizeSql;
import static java.lang.Integer.parseInt;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class WarehouseAttendanceDetailDao extends BaseDao {

	@Transactional
	public int queryTotalSize(HashMap<String, String> requestParameter) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(getQueryTotalSizeSql()
				+ buildQueryConditionByRequestParamenter(requestParameter));

		return Integer.parseInt(query.list().get(0).toString());
	}

	@Transactional
	public List query(HashMap<String, String> requestParameter) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();

		Query query = session.createSQLQuery(buildPagingSql(requestParameter))
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);

		return query.list();
	}

	private String buildPagingSql(HashMap<String, String> requestParameter) {
		String sql = getQuerySql(buildQueryConditionByRequestParamenter(requestParameter));

		if (isNotBlank(requestParameter.get("paging"))) {
			return buildQueryTableHead(sql);
		}
		return buildQueryTableHead(sql)
				+ " where rn > "
				+ requestParameter.get("start")
				+ " and  rn <= "
				+ (parseInt(requestParameter.get("start")) 
				+ parseInt(requestParameter.get("limit")));
	}

	private String buildQueryConditionByRequestParamenter(
			HashMap<String, String> requestParameter) {
		StringBuilder sb = new StringBuilder();

		if (!requestParameter.get("departmentCode").isEmpty()
				&& !requestParameter.get("departmentCode").equals("''")) {
			sb.append(" AND spms.dept_code in ("
					+ getDepartmentSql().replace(":departmentCode",
							requestParameter.get("departmentCode")) + ")");
		}

		if (!requestParameter.get("startTime").isEmpty()) {
			sb.append(" AND SCH.WORK_DATE >= to_date('"
					+ requestParameter.get("startTime") + "','yyyy/mm/dd')");
		}

		if (!requestParameter.get("endTime").isEmpty()) {
			sb.append(" AND SCH.WORK_DATE <= to_date('"
					+ requestParameter.get("endTime") + "','yyyy/mm/dd')");
		}

		if (!requestParameter.get("emp_code").isEmpty()) {
			sb.append(" AND SCH.EMP_CODE = '"
					+ requestParameter.get("emp_code") + "'");
		}

		if (!requestParameter.get("emp_name").isEmpty()) {
			sb.append(" AND SCH.EMP_NAME = '"
					+ requestParameter.get("emp_name") + "'");
		}

		return sb.toString();
	}
}
