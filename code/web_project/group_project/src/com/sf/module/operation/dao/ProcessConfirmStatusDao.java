/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.ProcessConfirmStatus;

/**
 *
 * 排班提交确认状态的Dao实现类
 * @author houjingyu  2014-07-02
 *
 */
public class ProcessConfirmStatusDao extends BaseEntityDao<ProcessConfirmStatus> implements IProcessConfirmStatusDao {
	public ProcessConfirmStatus findBy(Long deptid,String ym){
		DetachedCriteria dc = DetachedCriteria.forClass(ProcessConfirmStatus.class);
		dc.add(Restrictions.eq("deptId",deptid));
		dc.add(Restrictions.eq("ym",ym));
		List<ProcessConfirmStatus> list = super.findBy(dc);
		if(list!=null&&list.size()>0)
			return list.get(0);
		return null;
	}
	
	public Long save(ProcessConfirmStatus entity){
		Session session=getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			if(findBy(entity.getDeptId(),entity.getYm())==null){
				session.save(entity);
			}
			insertProcessMgtLog(session,entity);
			updateProcessMgtComfirmStatus(session,entity);
			tx.commit();
			return 1L;
		} catch (HibernateException e) {
			tx.rollback();
			return 0L;
		}finally{
			session.close();
		}
	}
	
	private void insertProcessMgtLog(Session session,ProcessConfirmStatus entity){
		Query query=session.createSQLQuery("insert into tt_pb_process_by_month_log \n"
											+"  (id, \n"
											+"   ym, \n"
											+"   dept_id, \n"
											+"   emp_code, \n"
											+"   day1, \n"
											+"   day2, \n"
											+"   day3, \n"
											+"   day4, \n"
											+"   day5, \n"
											+"   day6, \n"
											+"   day7, \n"
											+"   day8, \n"
											+"   day9, \n"
											+"   day10, \n"
											+"   day11, \n"
											+"   day12, \n"
											+"   day13, \n"
											+"   day14, \n"
											+"   day15, \n"
											+"   day16, \n"
											+"   day17, \n"
											+"   day18, \n"
											+"   day19, \n"
											+"   day20, \n"
											+"   day21, \n"
											+"   day22, \n"
											+"   day23, \n"
											+"   day24, \n"
											+"   day25, \n"
											+"   day26, \n"
											+"   day27, \n"
											+"   day28, \n"
											+"   day29, \n"
											+"   day30, \n"
											+"   day31, \n"
											+"   create_tm, \n"
											+"   modified_tm, \n"
											+"   create_emp_code, \n"
											+"   modified_emp_code, \n"
											+"   version, \n"
											+"   commit_status) \n"
											+" \n"
											+"  select SEQ_PROCESS_BY_MONTH_LOG.Nextval, \n"
											+"         ym, \n"
											+"         dept_id, \n"
											+"         emp_code, \n"
											+"         day1, \n"
											+"         day2, \n"
											+"         day3, \n"
											+"         day4, \n"
											+"         day5, \n"
											+"         day6, \n"
											+"         day7, \n"
											+"         day8, \n"
											+"         day9, \n"
											+"         day10, \n"
											+"         day11, \n"
											+"         day12, \n"
											+"         day13, \n"
											+"         day14, \n"
											+"         day15, \n"
											+"         day16, \n"
											+"         day17, \n"
											+"         day18, \n"
											+"         day19, \n"
											+"         day20, \n"
											+"         day21, \n"
											+"         day22, \n"
											+"         day23, \n"
											+"         day24, \n"
											+"         day25, \n"
											+"         day26, \n"
											+"         day27, \n"
											+"         day28, \n"
											+"         day29, \n"
											+"         day30, \n"
											+"         day31, \n"
											+"         sysdate, \n"
											+"         sysdate, \n"
											+"         create_emp_code, \n"
											+"         modified_emp_code, \n"
											+"         version, \n"
											+"         '1' \n"
											+"   \n"
											+"    from tt_pb_process_by_month \n"
											+"   where dept_id = ? and ym = ? and commit_status = ? \n"
											+" \n");
		query.setParameter(0, entity.getDeptId());
		query.setParameter(1, entity.getYm());
		query.setParameter(2, "0");
		query.executeUpdate();
	}
	
	private void updateProcessMgtComfirmStatus(Session session,ProcessConfirmStatus entity){
		Query query=session.createSQLQuery("update TT_PB_PROCESS_BY_MONTH t set t.commit_status='1' " +
				"where  t.dept_id=? and t.ym=? and t.commit_status= ? ");
		query.setParameter(0, entity.getDeptId());
		query.setParameter(1, entity.getYm());
		query.setParameter(2, "0");
		query.executeUpdate();
	}
	
}