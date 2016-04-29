package com.sf.module.report.dao;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseDao;
import com.sf.module.dispatch.domain.SchedulingRepository;

public class SchedulingDetailDao extends BaseDao {
	private static final String DEPT_CODE = "DEPT_CODE";
	private static final String DEPT_NAME = "DEPT_NAME";
	private static final String START_TIME = "START_TIME";
	private static final String END_TIME = "END_TIME";
	private static final String EMP_CODE = "EMP_CODE";
	private static final String EMP_NAME = "EMP_NAME";
	private static final String GROUP_CODE = "GROUP_CODE";

	private static final String SQL_QUERY_TABLE = "select sch.work_date shedule_dt,"
			+ " sch.dept_code,"
			+ " sch.group_code,"
			+ " sch.emp_code,"
			+ " sch.emp_name,"
			+ " sch.job_seq_code process_code,"
			+ " sch.job_seq difficulty_modify_value,"
			+ " sch.arbst timelength,"
			+ " sch.kq_xss attendance_hours,"
			+ " sch.stdaz overtime_hours,"
			+ " sch.class_code,"
			+ " sch.leave_type,"
			+ " sch.work_time"
			+ " from ti_tcas_spms_schedule sch" 
			+ " where sch.position_type = '1' ";

	private static String SQL_QUERY_TOTAL_SIZE = "select count(*) as totalSize from ti_tcas_spms_schedule sch where sch.position_type = '1' ";

	private static final String SQL_QUERY_USER_PERMISSION_REPORT = "select * from op_user_report ";

	private void setQueryParameterValue(HashMap<String, String> queryCriteria,
			Query query) {
		String[] parmeters = query.getNamedParameters();
		for (String param : parmeters) {
			query.setParameter(param, queryCriteria.get(param));
		}
	}

	private String addQueryParameters(HashMap<String, String> queryCriteria) {
		StringBuffer sb = new StringBuffer();
		
		for (Map.Entry<String, String> entry : queryCriteria.entrySet()) {
			String hqlFiled = ":" + entry.getKey();						
			if (exist(DEPT_CODE, entry)) {
				sb.append(" and sch.dept_code = " + hqlFiled);
			}
			
			if (exist(START_TIME, entry)) {
				sb.append(" and  sch.work_date > = to_date("+ hqlFiled+",'yyyy/MM/dd') ");
			}
			if (exist(END_TIME, entry)) {
				sb.append(" and  sch.work_date < = to_date("+ hqlFiled+",'yyyy/MM/dd') ");
			}
			if (exist(GROUP_CODE, entry)) {
				sb.append(" and sch.group_code = " + hqlFiled);
			}
			if (exist(EMP_CODE, entry)) {
				sb.append(" and sch.emp_code = " + hqlFiled);
			}
			if (exist(EMP_NAME, entry)) {
				sb.append(" and sch.emp_name = " + hqlFiled);
			}
		}
		sb.append(" order by sch.work_date,sch.emp_code");
		return sb.toString();
	}
	
	private boolean exist(String paramCode, Map.Entry<String, String> entry) {
		return paramCode.equals(entry.getKey()) && entry.getValue() != null
				&& !"".equals(entry.getValue());
	}

	@Transactional(readOnly = true)
	public List queryDetailReport(HashMap<String, String> queryCriteria) {
		String sql = "select * from (select t.*,rownum rn from  ( "
				+ SQL_QUERY_TABLE + addQueryParameters(queryCriteria)
				+ " )t ) where :start < rn and rn <= :limit ";
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, query);
		return query.list();
	}

	@Transactional(readOnly = true)
	public int queryDetailReportCount(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(
				SQL_QUERY_TOTAL_SIZE + addQueryParameters(queryCriteria))
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryCriteria, query);
		List<Map<String, Object>> searchSchedulingCountPage = query.list();
		return ((BigDecimal) searchSchedulingCountPage.get(0).get(
				SchedulingRepository.COL_TOTAL_SIZE)).intValue();
	}

	@Transactional(readOnly = true)
	public List queryUserPermissionReport(HashMap<String, String> queryCriteria) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(SQL_QUERY_USER_PERMISSION_REPORT)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@Transactional
	public int queryDeptPermissions(String deptId,String userId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String sql = "select count(*) totalSize from TS_USER_DEPT t where t.user_id = '"+userId+"' and t.dept_id = (select t.dept_id from tm_department t where t.dept_code='"+deptId+"')";
		Query queryCount = session.createSQLQuery(sql);
		List<Map<String, Object>> searchSchedulingCountPage = queryCount.list();
		return  ((BigDecimal) searchSchedulingCountPage.get(0)).intValue();
	}
	

}
