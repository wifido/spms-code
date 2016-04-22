package com.sf.module.driver.dao;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.BEAMS;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import static com.sf.module.common.util.StringUtil.removeColon;
import static com.sf.module.driver.dao.LineRepository.QUERY_DRIVING_LOG;
import static com.sf.module.driver.dao.LineRepository.SQL_QUERY_LINE_BY_LINE_CONIGURE_ID;
import static com.sf.module.driver.dao.LineRepository.UPDATE_DRIVER_LINE;
import static com.sf.module.driver.dao.LineRepository.buildOptionalParameters;
import static com.sf.module.driver.dao.LineRepository.buildOriginalStatementForAllLines;
import static com.sf.module.driver.dao.LineRepository.buildOriginalStatementForCountAllLines;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.dao.ScheduleBaseDao;
import com.sf.module.common.domain.sql.QueryStatement;
import com.sf.module.driver.biz.LineManageBiz.InputType;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.LineImportTable;

@Repository
public class LineManageDao extends ScheduleBaseDao<DriveLine> {
	public static final String ID = "id";

	@Transactional(readOnly = true)
	public int countLinesBySpecifyParameter(
	        String departmentId,
	        InputType dataSource,
	        String vehicleNumber,
	        String validStatus,
	        String startTime,
			String configureStatus) {
		List<String> parameters = buildParameters(departmentId, dataSource, vehicleNumber);

		QueryStatement queryStatement = buildOriginalStatementForCountAllLines();
		buildOptionalParameters(queryStatement, vehicleNumber, validStatus, startTime, configureStatus);

		QueryDelegate<Integer> queryDelegate = constructCountQuery(queryStatement, parameters);
		Integer size = query(queryDelegate);

		return size;
	}

	@Transactional
	public List<DriveLine> queryAllById(List<String> idList) {
		String queryString = "select * from tm_driver_line where id in (:id)";		
		return super.find(queryString, idList);
	}
	
	@Transactional
	public List<?> queryAll(
	        String departmentId,
	        InputType dataSource,
	        String vehicleNumber,
	        String validStatus,
	        int start,
	        int limit,
	        String startTime,
	        String configureStatus) {
		List<String> parameters = buildParameters(departmentId, dataSource, vehicleNumber);

		QueryStatement queryStatement = buildOriginalStatementForAllLines();
		buildOptionalParameters(queryStatement, vehicleNumber, validStatus, startTime, configureStatus);

		QueryDelegate<List> listQueryDelegate = constructQueryAllLinesDelegate(queryStatement, parameters, start, limit);

		return queryWithPagination(listQueryDelegate);
	}

	private QueryDelegate<List> constructQueryAllLinesDelegate(QueryStatement sqlExpression, List<String> parameters, int start, int limit) {
		return new QueryDelegate<List>(sqlExpression, parameters, start, limit) {
			@Override
			public List onQuery(Query query) {
				return query.list();
			}
		};
	}

	private QueryDelegate<Integer> constructCountQuery(QueryStatement sqlExpression, List<String> parameters) {
		return new QueryDelegate<Integer>(sqlExpression, parameters) {
			@Override
			public Integer onQuery(Query query) {
				return query.list().size();
			}
		};
	}

	public DriveLine findById(Long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(DriveLine.class);
		dc.add(Restrictions.eq(ID, id));
		List<DriveLine> list = super.findBy(dc);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public DriveLine findLine(DriveLine line) {
		List<DriveLine> list = super.findBy(line);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	private List<String> buildParameters(String departmentId, InputType inputType, String vehicleNumber) {
		List<String> parameters = newArrayList(departmentId, inputType.firstType, inputType.secondType);

		if (isNotBlank(vehicleNumber)) {
			parameters.add("%" + vehicleNumber + "%");
		}
		return parameters;
	}

	@Transactional(readOnly = true)
	public boolean validationOverlapData(LineImportTable targetTable) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(buildQueryConditionSql(targetTable));
		return query.list().size() > 0;
	}

	private String buildQueryConditionSql(LineImportTable lineImportTable) {
		StringBuilder stringBuilder = new StringBuilder(
				"select start_time from tm_driver_line where ");
		stringBuilder.append(" belong_zone_code = '")
				.append(lineImportTable.getDepartmentCode())
				.append("' and source_code = '")
				.append(lineImportTable.getSourceCode())
				.append("' and destination_code = '")
				.append(lineImportTable.getDestinationCode())
				.append("' and vehicle_number = '")
				.append(lineImportTable.getVehicleNumber())
				.append("' and start_time = ")
				.append(removeColon(lineImportTable.getStartTime()))
				.append(" and end_time = ")
				.append(removeColon(lineImportTable.getEndTime()));
		if (null != lineImportTable.getLineId()) {
			stringBuilder.append(" and id != ");
			stringBuilder.append(lineImportTable.getLineId());
		}
		return stringBuilder.toString();
	}

	@Transactional(readOnly = true)
	public List queryLineByLineConfigureId(int lineConfigureId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(SQL_QUERY_LINE_BY_LINE_CONIGURE_ID).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, lineConfigureId);
		return query.list();
	}

	@Transactional
	public void updateLine(String startTime, String endTime, String userName, String configureId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(UPDATE_DRIVER_LINE).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		query.setParameter(0, startTime);
		query.setParameter(1, endTime);
		query.setParameter(2, userName);
		query.setParameter(3, configureId);

		query.executeUpdate();
	}
	
	@Transactional
	public List<Map<String, Object>> findConfigLineByDepartmentCodeAndCodeAndMonth(String departmentCode, String code, String month) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				LineRepository.QUERY_LINE_BY_DEPARTMENT_CODE_MONTH)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		query.setParameter(0, departmentCode);
		query.setParameter(1, code);
		query.setParameter(2, month);
		
		return query.list();
	}
	
	@Transactional
	public List<Map<String, Object>> queryDrivingLogData(String employeeCode, String yearMonth) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(QUERY_DRIVING_LOG).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		
		query.setParameter(0, employeeCode);
		query.setParameter(1, yearMonth);
		
		return query.list();
	}
}