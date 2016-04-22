/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.domain.SchedulMgt;

/**
 * 
 * 排班管理的Dao实现类
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public class SchedulMgtDao extends BaseEntityDao<SchedulMgt> implements
		ISchedulMgtDao {

	private static String SQL_DELETE_SHEDULE_DAY_BY_DEPTIDADNID = " delete TT_PB_SHEDULE_BY_Day tt where tt.dept_id = ?  and tt.SHEDULE_MON_ID = ? ";

	private static String SQL_DELETE_SHEDULE_MONTH_BY_DEPTIDADNID = " delete TT_PB_SHEDULE_BY_month tm where tm.dept_id = ?  and tm.ID = ? ";
	
	public SchedulMgt findByCondition(Long deptid, String ym, String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgt.class);
		dc.add(Restrictions.eq("empCode", empcode));
		dc.add(Restrictions.eq("ym", ym));
		dc.add(Restrictions.eq("deptId", deptid));
		List<SchedulMgt> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<SchedulMgt> queryNoConfirmScheduling(Long deptid, String ym) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgt.class);
		dc.add(Restrictions.eq("ym", ym));
		dc.add(Restrictions.eq("commitStatus", 0));
		dc.add(Restrictions.eq("deptId", deptid));
		List<SchedulMgt> list = this.findBy(dc);
		return list;
	}

	public boolean findByEmpCode(String empcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgt.class);
		dc.add(Restrictions.eq("empCode", empcode));
		List<SchedulMgt> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Transactional
	public void insertConfirmLog(Long deptId, String ym) {
		StringBuilder sql = new StringBuilder();
		// 更新语句主体
		sql.append(" insert into tt_pb_shedule_by_month_log(ID, ");
		sql.append("  YM, ");
		sql.append("  DEPT_ID, ");
		sql.append("  EMP_CODE, ");
		sql.append("  DAY1, ");
		sql.append("  DAY2, ");
		sql.append("  DAY3, ");
		sql.append("  DAY4, ");
		sql.append("  DAY5, ");
		sql.append("  DAY6, ");
		sql.append("  DAY7, ");
		sql.append("  DAY8, ");
		sql.append("  DAY9, ");
		sql.append("  DAY10, ");
		sql.append("  DAY11, ");
		sql.append("  DAY12, ");
		sql.append("  DAY13, ");
		sql.append("  DAY14, ");
		sql.append("  DAY15, ");
		sql.append("  DAY16, ");
		sql.append("  DAY17, ");
		sql.append("  DAY18, ");
		sql.append("  DAY19, ");
		sql.append("  DAY20, ");
		sql.append("  DAY21, ");
		sql.append("  DAY22, ");
		sql.append("  DAY23, ");
		sql.append("  DAY24, ");
		sql.append("  DAY25, ");
		sql.append("  DAY26, ");
		sql.append("  DAY27, ");
		sql.append("  DAY28, ");
		sql.append("  DAY29, ");
		sql.append("  DAY30, ");
		sql.append("  DAY31, ");
		sql.append("  CREATE_TM, ");
		sql.append("  MODIFIED_TM, ");
		sql.append("  CREATE_EMP_CODE, ");
		sql.append("  MODIFIED_EMP_CODE, ");
		sql.append("  VERSION ) ");
		sql.append("  SELECT SEQ_SHEDULE_BY_MONTH_LOG.NEXTVAL,");
		sql.append("  MON.YM, ");
		sql.append("  MON.DEPT_ID,");
		sql.append("  MON.EMP_CODE,");
		sql.append("  MON.DAY1, ");
		sql.append("  MON.DAY2, ");
		sql.append("  MON.DAY3, ");
		sql.append("  MON.DAY4, ");
		sql.append("  MON.DAY5, ");
		sql.append("  MON.DAY6, ");
		sql.append("  MON.DAY7, ");
		sql.append("  MON.DAY8, ");
		sql.append("  MON.DAY9, ");
		sql.append("  MON.DAY10, ");
		sql.append("  MON.DAY11, ");
		sql.append("  MON.DAY12, ");
		sql.append("  MON.DAY13, ");
		sql.append("  MON.DAY14, ");
		sql.append("  MON.DAY15, ");
		sql.append("  MON.DAY16, ");
		sql.append("  MON.DAY17, ");
		sql.append("  MON.DAY18, ");
		sql.append("  MON.DAY19, ");
		sql.append("  MON.DAY20, ");
		sql.append("  MON.DAY21, ");
		sql.append("  MON.DAY22, ");
		sql.append("  MON.DAY23, ");
		sql.append("  MON.DAY24, ");
		sql.append("  MON.DAY25, ");
		sql.append("  MON.DAY26, ");
		sql.append("  MON.DAY27, ");
		sql.append("  MON.DAY28, ");
		sql.append("  MON.DAY29, ");
		sql.append("  MON.DAY30, ");
		sql.append("  MON.DAY31, ");
		sql.append("  sysdate, ");
		sql.append("  sysdate, ");
		sql.append("  MON.CREATE_EMP_CODE, ");
		sql.append("  MON.MODIFIED_EMP_CODE, ");
		sql.append("  VERSION ");
		sql.append("  FROM  TT_PB_SHEDULE_BY_MONTH MON WHERE MON.COMMIT_STATUS = 0  ");
		sql.append("  AND MON.ym = ? ");
		sql.append("  AND MON.DEPT_ID = ?");
		
		// 创建session
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		try {
			Query query = session.createSQLQuery(sql.toString());
			query.setParameter(0, ym);
			query.setParameter(1, deptId);
			query.executeUpdate();

			StringBuilder updateForMonthSql = new StringBuilder();
			updateForMonthSql
					.append(" update TT_PB_SHEDULE_BY_MONTH  set COMMIT_STATUS = 1 where COMMIT_STATUS =0  and ym = ? and dept_id = ? ");
			Query updateMonthQuery = session.createSQLQuery(updateForMonthSql.toString());
			updateMonthQuery.setParameter(0, ym);
			updateMonthQuery.setParameter(1, deptId);
			updateMonthQuery.executeUpdate();

			StringBuilder updateForDaySql = new StringBuilder();
			updateForDaySql
					.append(" update TT_PB_SHEDULE_BY_day  td set COMMIT_STATUS = 1 where td.shedule_mon_id in ( ");
			updateForDaySql.append(" select id from tt_pb_shedule_by_month tm ");
			updateForDaySql.append(" 	where   tm.ym = ? ");
			updateForDaySql.append(" and tm.dept_id = ? ) ");
			Query updateForDayQuery = session.createSQLQuery(updateForDaySql.toString());
			updateForDayQuery.setParameter(0, ym);
			updateForDayQuery.setParameter(1, deptId);
			updateForDayQuery.executeUpdate();
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public void delete(ScheduleDto dto, Long schedulingId) {
		// 创建session
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		try {
			Query dayQuery = session
					.createSQLQuery(SQL_DELETE_SHEDULE_DAY_BY_DEPTIDADNID);
			dayQuery.setParameter(0, dto.getDeptId());
			dayQuery.setParameter(1, schedulingId);
			dayQuery.executeUpdate();
			
			
			Query monthQuery = session
					.createSQLQuery(SQL_DELETE_SHEDULE_MONTH_BY_DEPTIDADNID);
			monthQuery.setParameter(0, dto.getDeptId());
			monthQuery.setParameter(1, schedulingId);
			monthQuery.executeUpdate();
			
		} catch (HibernateException e) {
			throw new BizException(e.getMessage());
		}
	}

	public List<SchedulMgt> findByDeptIdAndId(Long deptId, Long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(SchedulMgt.class);
		dc.add(Restrictions.eq("deptId", deptId));
		dc.add(Restrictions.eq("id", id));
		return this.findBy(dc);
	}

	
	@Transactional
	public void updateSchedulingCommitStatus(Long deptId, String ym) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuilder updateForDaySql = new StringBuilder();
		updateForDaySql
				.append(" update TT_PB_SHEDULE_BY_day  td set COMMIT_STATUS = 1 where td.shedule_mon_id in ( ");
		updateForDaySql.append(" select id from tt_pb_shedule_by_month tm ");
		updateForDaySql.append(" 	where   tm.ym = ? ");
		updateForDaySql.append(" and tm.dept_id = ? ) ");

		Query updateForDayQuery = session.createSQLQuery(updateForDaySql.toString());
		updateForDayQuery.setParameter(0, ym);
		updateForDayQuery.setParameter(1, deptId);
		updateForDayQuery.executeUpdate();
	}

}