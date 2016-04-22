package com.sf.module.dispatch.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.dispatch.domain.MaintenanceMail;
import com.sf.module.esbinterface.util.VMUtils;

@Repository
public class MaintenanceMailDao extends  BaseEntityDao<MaintenanceMail> {
	
	public int lockMailPlan() {
		log.info("lockMailPlan started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("update TT_DISPATCH_MAINTENANCE_MAIL"
						+ " set node_key=?  where node_key is null ");
				sqlQuery.setParameter((int) 0, VMUtils.getCurrentMachineName());
				return sqlQuery.executeUpdate();
			}
		});
	}
	
	public int clearLockMailPlan() {
		log.info("clearLockMailPlan started");
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("update TT_DISPATCH_MAINTENANCE_MAIL"
						+ " set node_key= ''  where node_key is not null ");
				return sqlQuery.executeUpdate();
			}
		});
	}
}