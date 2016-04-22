package com.sf.module.esbinterface.dao;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.esbinterface.domain.DriverLog;
import com.sf.module.esbinterface.util.VMUtils;

@Repository
public class DriverLogDao extends BaseEntityDao<DriverLog> {
	private static final String SQL_CONDITION_FOR_QUERY_NO_SEND_LOG = " where status=0 and to_date(ldate,'yyyymmdd') <= trunc(sysdate) ";
	private static final String SQL_CONDITION_FOR_QUERY_PROCESSING_LOG = " where status=3 and host_name = ? and to_date(ldate,'yyyymmdd') <= trunc(sysdate) ";
	private static final String SQL_UPDATE_LOG_STATUS_TO_PROCESSING = " update tt_sap_driver_log t set t.status=3 ,t.host_name = ? "
	        + SQL_CONDITION_FOR_QUERY_NO_SEND_LOG;
	private static final String SQL_UPDATE_LOG_STATUS_TO_FAIL = " update tt_sap_driver_log t set t.status=2,t.fail_desc=?  "
	        + SQL_CONDITION_FOR_QUERY_PROCESSING_LOG;
	private static final String SQL_DELETE_LOG = " delete tt_sap_driver_log t " + SQL_CONDITION_FOR_QUERY_PROCESSING_LOG;
	private static final String SQL_REMOVE_LOG_TO_HISTORY_TABLE = " insert into TT_SAP_DRIVER_LOG_HIS(log_id, pernr, zausw, ldate, ltime, ldayt, origf, abwgr, pdc_usrup, zhrxgbz, status, sync_date, fail_desc, create_date, host_name) select log_id, pernr, zausw, ldate, ltime, ldayt, origf, abwgr, "
	        + "pdc_usrup, zhrxgbz, 1, sysdate, fail_desc, create_date, host_name from tt_sap_driver_log " + SQL_CONDITION_FOR_QUERY_PROCESSING_LOG;
	private static final String SQL_CALL_PROCEDURE_HANDLE_DRIVE_CONVER_DATA = " { Call HANDLE_DRIVE_CONVER_DATA() }";

	@Transactional
	public void updateLogStatusToProcessing() {
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession.createSQLQuery(SQL_UPDATE_LOG_STATUS_TO_PROCESSING);
		query.setParameter(0, VMUtils.getCurrentMachineName());
		query.executeUpdate();
	}
	
	@Transactional
	public void excuteProcedureForGetDriveLog(){
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession.createSQLQuery(SQL_CALL_PROCEDURE_HANDLE_DRIVE_CONVER_DATA);
		query.executeUpdate();
	}

	@Transactional
	public void handPushSuccessLog() {
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession.createSQLQuery(SQL_REMOVE_LOG_TO_HISTORY_TABLE);
		query.setParameter(0, VMUtils.getCurrentMachineName());
		query.executeUpdate();
		query = currentSession.createSQLQuery(SQL_DELETE_LOG);
		query.setParameter(0, VMUtils.getCurrentMachineName());
		query.executeUpdate();
	}

	@Transactional
	public void handPushFailLog(String failDesc) {
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession.createSQLQuery(SQL_UPDATE_LOG_STATUS_TO_FAIL);
		query.setParameter(0, failDesc);
		query.setParameter(1, VMUtils.getCurrentMachineName());
		query.executeUpdate();
	}

}
