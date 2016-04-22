package com.sf.module.common.dao;

import com.sf.framework.server.base.dao.BaseJdbcDao;

public class SendMailDao extends BaseJdbcDao implements ISendMailDao {

	public boolean updateEmailStartStatus() {
		int flag = this
				.getJdbcTemplate()
				.update("UPDATE TS_OSS_DATACONTROL SET STARTTM=SYSDATE,STATE=0 WHERE TASK=1 AND  TRUNC(SYSDATE) > TRUNC(STARTTM)");
		if (flag > 0)
			return true;
		else
			return false;
	}

	public boolean updateEmailEndStatus() {
		int flag = this
				.getJdbcTemplate()
				.update("UPDATE TS_OSS_DATACONTROL SET ENDTM = SYSDATE,STATE=1 WHERE TASK=1");
		if (flag > 0)
			return true;
		else
			return false;
	}

}
