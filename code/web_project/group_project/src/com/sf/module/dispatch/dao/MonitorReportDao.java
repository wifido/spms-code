package com.sf.module.dispatch.dao;

import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class MonitorReportDao extends  BaseDao {
	@Transactional
	public HashMap<String, Object> queryMonitorReport(HashMap<String, String> queryParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		// 构造查询SQL并且设置查询参数
		String sql = buildQuerySqlByqueryParameter();
		try {
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
			
			setQueryParameter(queryParameter, query);
			// 总行数
			int size = query.list().size();
			// 设置分页参数
			setPagingParameter(queryParameter, query);
			// 组装数据格式
			dataMap.put(TOTAL_SIZE, size);
			dataMap.put(ROOT, query.list());
		} catch (Exception e) {
			throw new BizException("查询监控信息失败！");   
		}
		return dataMap;
	}
	
	@Transactional
	public ArrayList<HashMap<String, Object>> queryExportMonitorReport(HashMap<String, String> queryParameter) {
		// 构造查询SQL并且设置查询参数
		String sql = buildQuerySqlByqueryParameter();
		try {
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
			setQueryParameter(queryParameter, query);
			return (ArrayList<HashMap<String, Object>>)query.list();
		} catch (Exception e) {
			throw new BizException("导出监控信息失败！");   
		}
	}

	private void setQueryParameter(HashMap<String, String> queryParameter, Query query) {
		String displayMode = queryParameter.get("displayMode").toString();
		query.setParameter(0, queryParameter.get("deptCode"));
		query.setParameter(1, displayMode);
		query.setParameter(2, displayMode.equals("4") ? 3 : displayMode);
		query.setParameter(3, queryParameter.get("startTime"));
		query.setParameter(4, queryParameter.get("endTime"));
	}

	private void setPagingParameter(HashMap<String, String> paramsMap, Query query) {
		if (paramsMap.get(START) != null)
			query.setFirstResult(Integer.parseInt(paramsMap.get(START).toString()));
		if (paramsMap.get(LIMIT) != null)
			query.setMaxResults(Integer.parseInt(paramsMap.get(LIMIT).toString()));
	}
	
	private String buildQuerySqlByqueryParameter() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TO_CHAR(T.DAY_OF_MONTH,'YYYY-MM-DD') DAY_OF_MONTH,");
		sql.append("       T.HQ_CODE,");
		sql.append("       T.AREA_CODE,");
		sql.append("       T.DEPT_CODE,");
		sql.append("       FULLTIME_EMP_NUM,");
		sql.append("       NOT_FULLTIME_EMP_NUM,");
		sql.append("       FULLTIME_SCHEDULING_NUM,");
		sql.append("       FULLTIME_REST_NUM,");
		sql.append("       NOT_FULLTIME_SCHEDULING_NUM,");
		sql.append("       NOT_FULLTIME_REST_NUM,");
		sql.append("       FULLTIME_NOT_SCHEDULING,");
		sql.append("       NOT_FULLTIME_NOT_SCHEDULING,");
		sql.append("     ROUND((FULLTIME_SCHEDULING_NUM + FULLTIME_REST_NUM) / ");
		sql.append("                   DECODE(FULLTIME_EMP_NUM, 0, 0.1,FULLTIME_EMP_NUM),4) * 100 ");
		sql.append("                    || '%' FULLTIME_SCHEDULING_RATE,");
		sql.append("             ROUND((NOT_FULLTIME_SCHEDULING_NUM + NOT_FULLTIME_REST_NUM) / ");
		sql.append("                   DECODE(NOT_FULLTIME_EMP_NUM, 0, 0.1,NOT_FULLTIME_EMP_NUM),4) * 100 ");
		sql.append("                    || '%' NOT_FULLTIME_SCHEDULING_RATE,");
		sql.append("             ROUND(FULLTIME_SCHEDULING_NUM / DECODE(FULLTIME_EMP_NUM, 0, 0.1,FULLTIME_EMP_NUM),4) * 100 ");
		sql.append("                    || '%' FULLTIME_PLANNING_WORK_RATE,");
		sql.append("             ROUND(NOT_FULLTIME_SCHEDULING_NUM / ");
		sql.append("                   DECODE(NOT_FULLTIME_EMP_NUM, 0, 0.1,NOT_FULLTIME_EMP_NUM),4) * 100 ");
		sql.append("                    || '%' NO_FULLTIME_PLAN_WORK_RATE ");
		sql.append("  FROM TT_DISPATCH_MONITOR_REPORT T");
		sql.append("   WHERE T.DEPT_CODE IN");
		sql.append("       (SELECT DEPT_CODE");
		sql.append("          FROM TM_DEPARTMENT");
		sql.append("         START WITH DEPT_CODE IN ( ? )");
		sql.append("        CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)");
		sql.append("   AND T.TYPE_LEVEL IN (?,?)  ");
		sql.append("   AND T.EMP_POST_TYPE='2' ");
		sql.append("   and t.day_of_month >= to_date(?,'YYYYMMDD') ");
		sql.append("   and t.day_of_month <= to_date(?,'YYYYMMDD') ");
		sql.append("   order by  t.day_of_month , t.dept_code ");
		
		return sql.toString();
	}
	
}