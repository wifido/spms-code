package com.sf.module.esbinterface.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.esbinterface.biz.UploadStatus;
import com.sf.module.esbinterface.util.VMUtils;
import com.sf.module.operation.domain.SchedulingBase;

public class SchedulePlanHandlerDao extends BaseEntityDao<SchedulingBase> implements ISchedulePlanHandlerDao {
	@Override
	public int lockSchedulePlan(final UploadStatus uploadStatus) {
		log.info("lockSchedulePlan started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session
						.createSQLQuery("update TT_SAP_SYNCHRONOUS t "
								+ " set state_flg=1,node_key=?,SYNC_TM=sysdate where state_flg =?    "
								+ "  and ((begin_date < to_char(trunc(sysdate), 'YYYYMMDD')) "
								+ "    or (begin_date = to_char(trunc(sysdate), 'YYYYMMDD') and "
								+ "       tmr_day_flag = 'X') "
								+ "    or EXISTS "
								+ " (select 1 "
								+ "          from tm_oss_employee e, tm_department d "
								+ "         where e.dept_id = d.dept_id "
								+ "           and e.emp_post_type = '1' "
								+ "           and t.emp_code = e.emp_code "
								+ "           AND D.DEPT_CODE IN "
								+ "				(with t1 as    "
								+ "			 (select t.key_value c1    "
								+ "          from tl_spms_sys_config t    "
								+ "     where t.key_name = 'HONGKONG_DEPTCODE')    "
								+ "    select distinct regexp_substr(c1, '[^,]+', 1, level) c1    "
								+ "     from t1    "
								+ "     connect by level <= length(c1) - length(replace(c1, ',', '')) + 1)    "
								+ " )) ");

				sqlQuery.setParameter((int) 0, VMUtils.getCurrentMachineName());
				sqlQuery.setParameter(1, uploadStatus.ordinal());
				return sqlQuery.executeUpdate();
			}
		});
	}

    @Override
	public int updateSchedulePlanToSuccess() {
		log.info("updateSchedulePlanToSuccess started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session
						.createSQLQuery("update TT_SAP_SYNCHRONOUS t"
								+ " set state_flg =2,SYNC_TM=sysdate where state_flg =1 and node_Key=?  and ((begin_date < to_char(trunc(sysdate), 'YYYYMMDD')) "
								+ "    or (begin_date = to_char(trunc(sysdate), 'YYYYMMDD') and "
								+ "       tmr_day_flag = 'X') "
								+ "    or EXISTS "
								+ " (select 1 "
								+ "          from tm_oss_employee e, tm_department d "
								+ "         where e.dept_id = d.dept_id "
								+ "           and e.emp_post_type = '1' "
								+ "           and t.emp_code = e.emp_code "
								+ "           AND D.DEPT_CODE IN "
								+ "				(with t1 as    "
								+ "			 (select t.key_value c1    "
								+ "          from tl_spms_sys_config t    "
								+ "     where t.key_name = 'HONGKONG_DEPTCODE')    "
								+ "    select distinct regexp_substr(c1, '[^,]+', 1, level) c1    "
								+ "     from t1    "
								+ "     connect by level <= length(c1) - length(replace(c1, ',', '')) + 1)    "
								+ " )) ");
				sqlQuery.setParameter((int) 0, VMUtils.getCurrentMachineName());
				return sqlQuery.executeUpdate();
			}
		});
	}

    @Override
	public int updateSchedulePlanToFailure(final String errorInformation) {
		log.info("updateSchedulePlanToFailure started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session
						.createSQLQuery("update TT_SAP_SYNCHRONOUS t"
								+ " set state_flg =3, error_info=?,SYNC_TM=sysdate where state_flg =1 and node_key=? and ((begin_date < to_char(trunc(sysdate), 'YYYYMMDD')) "
								+ "    or (begin_date = to_char(trunc(sysdate), 'YYYYMMDD') and "
								+ "       tmr_day_flag = 'X') "
								+ "    or EXISTS "
								+ " (select 1 "
								+ "          from tm_oss_employee e, tm_department d "
								+ "         where e.dept_id = d.dept_id "
								+ "           and e.emp_post_type = '1' "
								+ "           and t.emp_code = e.emp_code "
								+ "           AND D.DEPT_CODE IN "
								+ "				(with t1 as    "
								+ "			 (select t.key_value c1    "
								+ "          from tl_spms_sys_config t    "
								+ "     where t.key_name = 'HONGKONG_DEPTCODE')    "
								+ "    select distinct regexp_substr(c1, '[^,]+', 1, level) c1    "
								+ "     from t1    "
								+ "     connect by level <= length(c1) - length(replace(c1, ',', '')) + 1)    "
								+ "   )) ");
				sqlQuery.setParameter((int) 0, errorInformation);
				sqlQuery.setParameter((int) 1, VMUtils.getCurrentMachineName());
				return sqlQuery.executeUpdate();
			}
		});
	}

    @Override
	public int resetSchedulePlanToNormal() {
		log.info("resetSchedulePlanToNormal started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session
						.createSQLQuery("update TT_SAP_SYNCHRONOUS t"
								+ " set state_flg =0,node_key=null,SYNC_TM=sysdate where state_flg in (1,3) and node_key=? and ((begin_date < to_char(trunc(sysdate), 'YYYYMMDD')) "
								+ "    or (begin_date = to_char(trunc(sysdate), 'YYYYMMDD') and "
								+ "       tmr_day_flag = 'X') "
								+ "    or EXISTS "
								+ " (select 1 "
								+ "          from tm_oss_employee e, tm_department d "
								+ "         where e.dept_id = d.dept_id "
								+ "           and e.emp_post_type = '1' "
								+ "           and t.emp_code = e.emp_code "
								+ "           AND D.DEPT_CODE IN "
								+ "				(with t1 as    "
								+ "			 (select t.key_value c1    "
								+ "          from tl_spms_sys_config t    "
								+ "     where t.key_name = 'HONGKONG_DEPTCODE')    "
								+ "    select distinct regexp_substr(c1, '[^,]+', 1, level) c1    "
								+ "     from t1    "
								+ "     connect by level <= length(c1) - length(replace(c1, ',', '')) + 1)    "
								+ " )) ");
				sqlQuery.setParameter((int) 0, VMUtils.getCurrentMachineName());
				return sqlQuery.executeUpdate();
			}
		});
	}

    @Override
	public List<HashMap<String,Object>> getSchedulePlansByStatus(final UploadStatus uploading) {
		log.info("getSchedulePlansByStatus started");
		Object result = getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query sqlQuery = session
						.createSQLQuery("SELECT  t.id,t.emp_code,t.begin_date,t.end_date,t.begin_tm,t.end_tm,t.tmr_day_flag,t.off_duty_flag,t.class_system "
								+ "   from TT_SAP_SYNCHRONOUS t WHERE state_flg=? AND node_key=? and  ((begin_date < to_char(trunc(sysdate), 'YYYYMMDD')) "
								+ "    or (begin_date = to_char(trunc(sysdate), 'YYYYMMDD') and "
								+ "       tmr_day_flag = 'X') "
								+ "    or EXISTS "
								+ " (select 1 "
								+ "          from tm_oss_employee e, tm_department d "
								+ "         where e.dept_id = d.dept_id "
								+ "           and e.emp_post_type = '1' "
								+ "           and t.emp_code = e.emp_code "
								+ "           AND D.DEPT_CODE IN "
								+ "				(with t1 as    "
								+ "			 (select t.key_value c1    "
								+ "          from tl_spms_sys_config t    "
								+ "     where t.key_name = 'HONGKONG_DEPTCODE')    "
								+ "    select distinct regexp_substr(c1, '[^,]+', 1, level) c1    "
								+ "     from t1    "
								+ "     connect by level <= length(c1) - length(replace(c1, ',', '')) + 1)    "
								+ " )) ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				sqlQuery.setParameter((int) 0, 1);
				sqlQuery.setParameter(1, VMUtils.getCurrentMachineName());
				return sqlQuery.list();
			}
		});
		return (List<HashMap<String,Object>>) result;
	}
}
