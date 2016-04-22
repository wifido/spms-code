/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.ProcessMgt;

/**
 *
 * 月度工序实体的Dao实现类
 * @author houjingyu  2014-07-08
 *
 */
public class ProcessMgtDao extends BaseEntityDao<ProcessMgt> implements IProcessMgtDao {

	private static String queryNoConfirmProcessEmpcodeSql = "   select tm.id  \n"
													+"                        from tt_pb_process_by_month tm, \n"
													+"                   tt_pb_process_by_month_log t  \n"
													+"                 where          \n"
													+"               t.ym = tm.ym  \n"
													+"               and t.dept_id = tm.dept_id  \n"
													+"               and t.emp_code = tm.emp_code \n"
													+"                and t.dept_id = :dept_id  \n"
													+"                and tm.id = :processId  \n";

	public ProcessMgt findByCondition(Long deptid,String ym,String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProcessMgt.class);
		dc.add(Restrictions.eq("empCode", empcode));
		dc.add(Restrictions.eq("ym", ym));
		dc.add(Restrictions.eq("deptId", deptid));
		List<ProcessMgt> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public boolean isHaveNoConfirmProcessMgt(Long deptid, String processMgtIds){
		Session session = null;
		try {
			boolean toConfirmThe = false;
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(
					queryNoConfirmProcessEmpcodeSql).setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP);
			String[] processMgtIdsArray = processMgtIds.replaceAll(",$", "").split(
					",", -1);
			for (String processMgtId : processMgtIdsArray) {
				Long processId = Long.parseLong(processMgtId);
				query.setParameter("dept_id", deptid);
				query.setParameter("processId", processId);
				List list = query.list();
				if(list != null && list.size() > 0){
					toConfirmThe = toConfirmThe && false;
				}else{
					toConfirmThe = true;
				}
			}
			return toConfirmThe;
		} finally {
			session.close();
		}
	}
	
	@Transactional
	public void delete(Long deptid, Long processId) {
		Session session=getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(" delete from  Tt_Pb_Process_By_Day t " 
					+ " where t.dept_id =:dept_id  " 
				    + " and  t.PROCESS_MON_ID = :processId ");
			query.setParameter("dept_id", deptid);
			query.setParameter("processId", processId);
			query.executeUpdate();
	}

	@Override
	public List<ProcessMgt> findByDeptIdAndId(Long deptid, Long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProcessMgt.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("deptId", deptid));
		return this.findBy(dc);
	}
}