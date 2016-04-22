package com.sf.module.pushserver.dao;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.esbinterface.util.VMUtils;
import com.sf.module.pushserver.domain.Message;

@Repository
public class MessageDao extends BaseEntityDao<Message> {

	@Transactional
	public void lockMessage() {
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession
		        .createSQLQuery(" UPDATE TT_DRIVER_MESSAGE T SET T.STATUS = ?,T.HOST_NAME = ? WHERE T.STATUS = 0 AND T.CREATE_TIME > SYSDATE - 2 ");
		query.setParameter(1, VMUtils.getCurrentMachineName());
		query.setParameter(0, 3L);
		query.executeUpdate();
	}

}
