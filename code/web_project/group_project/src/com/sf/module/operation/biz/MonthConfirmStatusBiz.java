/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.Date;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.DateUtil;
import com.sf.module.operation.dao.IMonthConfirmStatusDao;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 提交确认状态的业务实现类
 * 
 * @author houjingyu 2014-07-02
 * 
 */
public class MonthConfirmStatusBiz extends BaseBiz implements
		IMonthConfirmStatusBiz {
	private final static int FOUR_PER_MONTH = 400;

	private final static int AT_EIGHT_PM = 20;

	/**
	 * 提交确认状态的Dao接口
	 */
	private IMonthConfirmStatusDao monthConfirmStatusDao;

	/**
	 * 设置提交确认状态的Dao接口
	 */
	public void setMonthConfirmStatusDao(
			IMonthConfirmStatusDao monthConfirmStatusDao) {
		this.monthConfirmStatusDao = monthConfirmStatusDao;
	}

	@Override
	public void saveConfirmStatus(Long deptid, String ym) {
		if (DateUtil.validConfirmDate(ym)) {
			if (ym.equals(CommonUtil.getLastMonthYm(new Date())) && DateUtil.isBeOverdue(ym)) {
				throw new BizException("已逾期，不能提交确认该月排班！");
			}
			MonthConfirmStatus monthConfirmStatus = monthConfirmStatusDao.findBy(deptid, ym);
			if (monthConfirmStatus == null) {
				monthConfirmStatus = new MonthConfirmStatus();
				monthConfirmStatus.setDeptId(deptid);
				monthConfirmStatus.setYm(ym);
				monthConfirmStatus.setCommitStatus(1);
				monthConfirmStatusDao.save(monthConfirmStatus);
			}
		} else {
			throw new BizException("提交确认只能选择上个月、当前月或下个月！");
		}
	}
}