package com.sf.module.common.dao;

import com.sf.framework.server.base.dao.IJdbcDao;

public interface ISendMailDao extends IJdbcDao {
	public boolean updateEmailStartStatus();
	public boolean updateEmailEndStatus();
}
