package com.sf.module.esbinterface.biz;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.esbinterface.dao.DriverLogDao;
import com.sf.module.esbinterface.domain.DriverLog;
import com.sf.module.esbinterface.util.VMUtils;

@Component
public class DriverLogBiz {
	@Autowired
	private DriverLogDao driverLogDao;

	public int countOfDriverLog() {
		DetachedCriteria dc = buidDetachedCriteriaForQueryDriverLog(3L);
		return driverLogDao.countBy(dc);
	}

	public List<DriverLog> listDriverLog(int pageSize, int pageIndex) {
		DetachedCriteria dc = buidDetachedCriteriaForQueryDriverLog(3L);
		return driverLogDao.findBy(dc, pageSize, pageIndex);
	}

	private DetachedCriteria buidDetachedCriteriaForQueryDriverLog(Long status) {
		DetachedCriteria dc = DetachedCriteria.forClass(DriverLog.class);
		dc.add(Restrictions.eq("status", status));
		dc.add(Restrictions.eq("hostName", VMUtils.getCurrentMachineName()));
		dc.add(Restrictions.sqlRestriction(" to_date(ldate,'yyyymmdd') <= trunc(sysdate) "));
		return dc;
	}

	@Transactional
	public void handPushFail(String failDesc) throws Exception {
		driverLogDao.handPushFailLog(failDesc);
	}

	@Transactional
	public void handPushSucess() throws Exception {
		driverLogDao.handPushSuccessLog();
	}

	@Transactional
	public void updateLogStatusToProcessing() {
		driverLogDao.updateLogStatusToProcessing();
	}
	
	@Transactional
	public void excuteProcedureForGetDriveLog(){
		driverLogDao.excuteProcedureForGetDriveLog();
	}

}
