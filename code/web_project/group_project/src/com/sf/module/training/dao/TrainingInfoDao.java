package com.sf.module.training.dao;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static com.sf.module.warehouse.dao.WarehouseSchedulingHql.DELETE_EMPLOYEE_SCHEDULING_SQL;
import static com.sf.module.warehouse.dao.WarehouseSchedulingHql.GET_EMPLOYEE_SQL;
import static com.sf.module.warehouse.dao.WarehouseSchedulingHql.QUERY_EMPLOYEE_DYNAMIC_DEPT_SQL;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.common.util.StringUtil;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.training.domain.Training;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.dao.BaseDao;

public class TrainingInfoDao extends BaseDao {

	@Transactional(readOnly = true)
	public HashMap<String, Object> queryTrainingInfo(HashMap<String, String> paramsMap) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		// 构造查询SQL并且设置查询参数
		String sql = getQueryParameter(paramsMap, params);
		try {
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
			setQueryParameter(params, query);
			// 总行数
			int size = query.list().size();
			// 设置分页参数
			setPagingParameter(paramsMap, query);
			// 组装数据格式
			dataMap.put(TOTAL_SIZE, size);
			dataMap.put(ROOT, query.list());
		} catch (Exception e) {
			throw new BizException("查询培训信息失败！");
		}
		return dataMap;
	}
	
	@Transactional
	public boolean remove(long parseLong) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		String sql = " delete tt_pb_training_info where id = ? ";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, parseLong);
		query.executeUpdate();
		return true;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> getEmployeeByEmpCodeAndDeptId(Long deptId, String empCode) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT T.EMP_CODE," );
		sql.append(" T.EMP_NAME," );
		sql.append(" T.DEPT_ID," );
		sql.append(" T.GROUP_ID," );
		sql.append(" T.DIMISSION_DT,");
		sql.append(" T.WORK_TYPE," );
		sql.append(" G.DISABLE_DT" );
		sql.append(" FROM TM_OSS_EMPLOYEE T, TM_PB_GROUP_INFO G" );
		sql.append(" WHERE T.DEPT_ID = G.DEPT_ID(+)");
		sql.append(" AND T.GROUP_ID = G.GROUP_ID(+)" );
		sql.append(" AND T.EMP_CODE = ?" );
		sql.append(" AND T.DEPT_ID = ?");
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, empCode);
		query.setParameter(1, deptId);
		query.executeUpdate();
		List result = query.list();
		if (!result.isEmpty()) {
			return (Map<String, Object>) result.get(0);
		}
		return null;
	}
	
	@Transactional
	public List queryExportTraining(HashMap<String, String> queryCriteria) {
		List<Object> params = new ArrayList<Object>();
		String sql = getQueryParameter(queryCriteria, params);
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);;
		setQueryParameter(params, query);
		return query.list();
	}
	
	private String getQueryParameter(HashMap<String, String> queryCriteria, List<Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("select  t.ID, ");
		sql.append("t.DEPARTMENT_CODE, ");
		sql.append("t.TRAINING_CODE, ");
		sql.append("t.EMPLOYEE_CODE, ");
		sql.append("t.YEARS_MONTH, ");
		sql.append("t.DAY_OF_MONTH, ");
		sql.append("t.POST_TYPE, ");
		sql.append(" case when (e.EMP_DUTY_NAME is null and t.POST_TYPE = '1')  then '运作员' else e.EMP_DUTY_NAME end EMP_DUTY_NAME,");
		sql.append("t.CREATE_TM, ");
		sql.append(" e.emp_name EMPLOYEE_NAME,");
		sql.append(" case when e.work_type = 6 then '外包' else persk_txt end  WORK_TYPE,");
	    sql.append(" CASE WHEN E.DIMISSION_DT IS NULL THEN 1  ");
	    sql.append(" WHEN E.DIMISSION_DT > SYSDATE THEN 1  ");
		sql.append(" WHEN E.DIMISSION_DT <= SYSDATE THEN 0 END DIMISSION_DT,");
		sql.append("t.MODIFIED_TM, ");
		sql.append("t.MODIFIED_EMP_CODE ");
		sql.append("from tt_pb_training_info t,tm_oss_employee e, ");
		sql.append(" tm_department d  ");
		sql.append(" where t.employee_code = e.emp_code  ");
		sql.append(" and t.department_code = d.dept_code ");
		sql.append(" and d.dept_id = e.dept_id ");
		if (StringUtil.isNotBlank(queryCriteria.get("DEPT_CODE"))) {
			sql.append(" AND D.DEPT_ID  IN ( SELECT  T.DEPT_ID  FROM TM_DEPARTMENT  T  WHERE DELETE_FLG=0  START WITH DEPT_CODE = ? CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE )  ");
			params.add(queryCriteria.get("DEPT_CODE"));
		}

		if (StringUtil.isNotBlank(queryCriteria.get("EMP_CODE"))) {
			sql.append(" AND t.employee_code  = ? ");
			params.add(queryCriteria.get("EMP_CODE"));
		}

		if (StringUtil.isNotBlank(queryCriteria.get("EMP_NAME"))) {
			sql.append(" AND e.emp_name  = ? ");
			params.add(queryCriteria.get("EMP_NAME"));
		}

		if (StringUtil.isNotBlank(queryCriteria.get("MONTH_ID"))) {
			sql.append(" AND t.years_month  = ? ");
			params.add(queryCriteria.get("MONTH_ID"));
		}
		
		if (StringUtil.isNotBlank(queryCriteria.get("POST_TYPE"))) {
			sql.append(" AND t.POST_TYPE  = ? ");
			params.add(queryCriteria.get("POST_TYPE"));
		}

		sql.append(" ORDER BY t.EMPLOYEE_CODE,t.DAY_OF_MONTH ");

		return sql.toString();
	}

	private void setQueryParameter(List<Object> params, Query query) {
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
	}

	private void setPagingParameter(HashMap<String, String> paramsMap, Query query) {
		if (paramsMap.get(START) != null)
			query.setFirstResult(Integer.parseInt(paramsMap.get(START).toString()));
		if (paramsMap.get(LIMIT) != null)
			query.setMaxResults(Integer.parseInt(paramsMap.get(LIMIT).toString()));
	}

	public boolean saveTraining(List<Training> trainingList) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			for (Training training : trainingList) {
				String sql = " delete  from tt_pb_training_info t " + " where t.employee_code  = ?   " + " and t.day_of_month  = ? " + " and t.post_type  = ? ";
				Query query = session.createSQLQuery(sql);
				query.setParameter(0, training.getEmployeeCode());
				query.setParameter(1, training.getDayOfMonth());
				query.setParameter(2, training.getPostType());
				query.executeUpdate();
				session.save(training);
			}
			tx.commit();
			return true;
		} catch (HibernateException e) {
			tx.rollback();
			throw new BizException(e.getMessage());
		} finally {
			session.close();
		}
	}

}
