package com.sf.module.dispatch.dao;

import java.util.HashMap;
import com.sf.framework.server.base.dao.BaseEntityDao;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

public class AppBaseDao<E extends com.sf.framework.base.domain.IEntity> extends BaseEntityDao {
    public static final String START = "start";
    public static final String LIMIT = "limit";
    public final static String WORK_TYPE = "WORK_TYPE";
	private static final String IN = "in_";

    protected Session openSession() {
        SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
        return sessionFactory.openSession();
    }

    protected void close(Session session) {
        session.close();
    }
    
    public void setQueryParameterValue(HashMap<String, String> queryParameter,
			Query query) {
		String[] parameters = query.getNamedParameters();
		for (String param : parameters) {
			if (param.startsWith(IN)) {
				query.setParameterList(param, queryParameter.get(param).split(","));
				continue;
			}
			query.setParameter(param, queryParameter.get(param));
		}
	}
}
