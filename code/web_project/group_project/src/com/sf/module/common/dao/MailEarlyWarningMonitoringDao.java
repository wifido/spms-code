package com.sf.module.common.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class MailEarlyWarningMonitoringDao extends BaseDao {
	@Transactional
	public int queryExistingData(String sql) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return Integer.parseInt(query.list().get(0).toString());
	}
}
