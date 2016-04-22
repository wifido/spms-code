/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-28     jun.wen       创建
 **********************************************/

package com.sf.module.ossinterface.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.ossinterface.domain.HrEmpInfo;

/**
 *
 * HR系统接口获取(人员表)的Dao实现类
 * @author jun.wen  2014-05-28
 *
 */
public class HrEmpInfoDao extends BaseEntityDao<HrEmpInfo> implements IHrEmpInfoDao {
	
	/**
	 * <pre>
	 * 调用初始化emp信息存储过程
	 * </pre>
	 * @author 文俊 (337291)
	 * @date Jun 24, 2014 
	 * @param journalId
	 */
	public void stpInitTmEmployee(String journalId) {
		final String _journalId = journalId;
		super.getHibernateTemplate().execute(new HibernateCallback<String>() {
			public String doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("{call PKG_TCAS_EMPLOYEE.STP_INIT_TM_EMPLOYEE(?)}");
				sqlQuery.setString(0, _journalId);
				sqlQuery.executeUpdate();
				/*session.doWork(new Work() {
					@Override
					public void execute(Connection arg0) throws SQLException {
						
					}
				});*/
				return null;
			}
		});
	}
}