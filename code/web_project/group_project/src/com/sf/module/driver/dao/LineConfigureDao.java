package com.sf.module.driver.dao;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.driver.dao.LineConfigureRepository.*;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.dao.ScheduleBaseDao;
import com.sf.module.common.domain.sql.QueryStatement;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.DriverLineConfigureRelation;
import com.sf.module.driver.domain.LineConfigure;

@Repository
public class LineConfigureDao extends ScheduleBaseDao<LineConfigure> {

	@Transactional(readOnly = true)
	public int countLineConfigure(Map<String, String> params) {
		QueryStatement queryStatement = buildOriginalStatementForAllLineConfigures();
		buildOptionalParameters(params, queryStatement);
		QueryDelegate<Integer> queryDelegate = constructCountQuery(queryStatement, params);
		Integer size = query(queryDelegate);
		return size;
	}

	@Transactional
	public List<Map<String, Object>> queryLineConfigure(Map<String, String> params) {
		QueryStatement queryStatement = buildOriginalStatementForAllLineConfigures();
		buildOptionalParameters(params, queryStatement);
		QueryDelegate<List> listQueryDelegate = constructQueryAllLineConfiguresDelegate(queryStatement, params);
		return queryWithPagination(listQueryDelegate);
	}

	@Transactional
	public List<Map<String, Object>> queryLineConfigureForExport(Map<String, String> params) {
		QueryStatement queryStatement = buildOriginalStatementForExportAllLineConfigures();
		buildOptionalParametersForExportAllLineConfigures(params, queryStatement);
		queryStatement.orderByAscending("CONFIGURE_ID", "SORT");
		QueryDelegate<List> listQueryDelegate = constructQueryAllLineConfiguresDelegate(queryStatement, params);
		return queryWithPagination(listQueryDelegate);
	}

	private QueryDelegate<List> constructQueryAllLineConfiguresDelegate(QueryStatement sqlExpression, Map<String, String> params) {
		return new QueryDelegate<List>(sqlExpression, params) {
			@Override
			public List onQuery(Query query) {
				return query.list();
			}
		};
	}

	private QueryDelegate<Integer> constructCountQuery(QueryStatement sqlExpression, Map<String, String> params) {
		return new QueryDelegate<Integer>(sqlExpression, params) {
			public Integer onQuery(Query query) {
				return query.list().size();
			}
		};
	}

	@Transactional
	public String queryClassesCode(String departmentCode, String yearMonth) {
		QueryStatement queryStatement = buildOriginalStatementForLineConfigureCode();

		List params = newArrayList(departmentCode);
		params.add(yearMonth);

		QueryDelegate<List> listQueryDelegate = constructQueryClassesCode(queryStatement, params);
		List<Map<String, Object>> result = queryWithPagination(listQueryDelegate);

		if (result.get(0).get("DEPARTMENT_CODE") == null) {
			return "0";
		}

		return result.get(0).get("DEPARTMENT_CODE").toString();
	}

	@Transactional
	public int validClassesCode(String departmentCode, String yearMonth, String code) {
		String sql = LineConfigureRepository.SQL_QUERY_CONFIGURE_BY_CONFIGUE_CODE;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, departmentCode);
		query.setParameter(1, yearMonth);
		query.setParameter(2, code);
		return Integer.parseInt(query.list().get(0).toString());
	}

	private QueryDelegate<List> constructQueryClassesCode(QueryStatement sqlExpression, List<?> params) {
		return new QueryDelegate<List>(sqlExpression, params) {
			@Override
			public List onQuery(Query query) {
				return query.list();
			}
		};
	}

	@Transactional
	public List queryLineConfigureByLineIdAndMonth(String month, long lineId) {
		QueryStatement queryStatement = buildOriginalStatementForLineIsConfigured();
		List params = newArrayList(1, month, lineId);
		QueryDelegate<List> listQueryDelegate = constructQueryLineConfigureByLineIdAndMonth(queryStatement, params);
		return queryWithPagination(listQueryDelegate);
	}

	private QueryDelegate<List> constructQueryLineConfigureByLineIdAndMonth(QueryStatement sqlExpression, List<?> params) {
		return new QueryDelegate<List>(sqlExpression, params) {
			@Override
			public List onQuery(Query query) {
				return query.list();
			}
		};
	}

	@Transactional
	public void saveLineConfigureRelation(DriverLineConfigureRelation driverLineConfigureRelation) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(" insert into tt_driver_line_configure_r(configure_id,line_id,sort) values(?,?,?)");
		query.setParameter(0, driverLineConfigureRelation.getConfigureId());
		query.setParameter(1, driverLineConfigureRelation.getLineId());
		query.setParameter(2, driverLineConfigureRelation.getOrder());

		query.executeUpdate();
	}

	@Transactional(readOnly = true)
	public List<Object> queryConfigureSchedulingAllLine(HashMap<String, String> queryParameter) {
		String sql = DriverSchedulingRepository.buildQueryConfigureSchedulingAllLine();

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);

		String[] namedParameters = query.getNamedParameters();
		for (String namedParameter : namedParameters) {
			query.setParameter(namedParameter, queryParameter.get(namedParameter));
		}

		return query.list();
	}

	@Transactional
	public void deleteLineConfigureRelation(long lineConfigureId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(SQL_DELETE_LINE_CONFIGURE_RELATION);
		query.setParameter(0, lineConfigureId);
		query.executeUpdate();
	}

	@Transactional
	public void updateConfigureClass(String id, String validState, String userName, double driveDuration) {
		String sql = "update tt_driver_line_configure conf set conf.valid_status = ?,"
		        + " conf.modified_emp_code = ?, conf.modified_tm = ?, conf.drive_duration = ?, conf.attendance_duration = ? where conf.id = ?";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);

		query.setParameter(0, validState);
		query.setParameter(1, userName);
		query.setParameter(2, new Date());
		query.setParameter(3, driveDuration);
		query.setParameter(4, driveDuration);
		query.setParameter(5, id);

		query.executeUpdate();
	}

	@Transactional
	public void batchDeleteConfigureClass(String[] deleteIds) {
		String sql = "delete tt_driver_line_configure configure where configure.id in (:deleteIds)";

		executeDeleteBySql(sql, deleteIds);
	}

	@Transactional
	public void batchDeleteConfigureRelation(String[] deleteIds) {
		String sql = "delete tt_driver_line_configure_r r where r.configure_id in (:deleteIds)";

		executeDeleteBySql(sql, deleteIds);
	}

	private void executeDeleteBySql(String sql, String[] deleteIds) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);

		query.setParameterList("deleteIds", deleteIds);

		query.executeUpdate();
	}

	@Transactional
	public List<DriveLine> queryLineByConfigCode(String departmentCode, String configCode,String yearMonth) {
		String sql = LineConfigureRepository.SQL_QUERY_LINE_CONFIGURE_BY_CONFIGUE_CODE;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, departmentCode);
		query.setParameter(1, configCode);
		query.setParameter(2, yearMonth);
		List lines = query.list();
		if(lines.isEmpty())return lines;
		ArrayList<DriveLine> list = new ArrayList<DriveLine>();
		for(Map<String,Object> line:(List<Map<String,Object>>)lines){
			DriveLine driverLine = new DriveLine();
			driverLine.setStartTime((String)line.get("START_TIME"));
			driverLine.setEndTime((String)line.get("END_TIME"));
			driverLine.setSourceCode((String)line.get("SOURCE_CODE"));
			driverLine.setDestinationCode((String)line.get("DESTINATION_CODE"));
			list.add(driverLine);
		}
		return lines;
	}
	
	@Transactional
	public String queryConfigureClassByConfigureId(String id) {
		String sql = "select  type from tt_driver_line_configure conf where conf.id = ?";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, id);
		if (query.list().isEmpty())
			return null;
		return query.list().get(0).toString();
	}

	// 查询配班是否存在
	@Transactional
	public boolean queryConfigureExist(String code, String departmentCode,
			String month) {
		String sql = "select  type from tt_driver_line_configure conf where conf.code = ? and conf.department_code = ? and conf.month = ?";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, code);
		query.setParameter(1, departmentCode);
		query.setParameter(2, month);
		if (query.list().isEmpty())
			return false;
		
		return true;
	}
	@Transactional
	public boolean queryConfigureVaild(String code, String departmentCode,
			String month) {
		String sql = "select  type from tt_driver_line_configure conf where conf.code = ? and conf.department_code = ? and conf.month = ? ";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		sql += "and conf.valid_status = 1";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, code);
		query.setParameter(1, departmentCode);
		query.setParameter(2, month);
		if (query.list().isEmpty())
			return false;
		
		return true;
	}
}
