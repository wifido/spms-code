package com.sf.module.driverui.biz;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.biz.DriverSchedulingBiz;
import com.sf.module.driver.dao.DriverSchedulingDao;
import com.sf.module.driver.domain.DriverScheduling;
import com.sf.module.driverui.dao.ApplyDao;
import com.sf.module.driverui.domain.ApplyRecord;
import com.sf.module.pushserver.dao.MessageDao;
import com.sf.module.pushserver.domain.Message;

@Component
public class ApplyBiz extends BaseBiz {

	private static final String KEY_PAGE_INDEX = "pageIndex";
	private static final String KEY_PAGE_SIZE = "pageSize";
	private static final String KEY_EXCHANGE_COUNT = "exchangeCount";
	private static final String KEY_LEAVE_COUNT = "leaveCount";
	private static final String KEY_APPLY_TYPE = "applyType";
	private static final String KEY_EMPLOYEE_CODE = "employeeCode";
	@Resource
	private ApplyDao applyDao;
	@Resource
	private DriverSchedulingDao driverSchedulingDao;
	@Resource
	private DriverSchedulingBiz driverSchedulingBiz;
	@Autowired
	private MessageDao messageDao;
	private static final Long TRANSFER_CLASSES = 2l;
	private static final String THE_APPLYRECORD_DOES_NOT_EXIST = "审批失败，审批记录不存在!";
	private static final String THE_SCHEDULINGRECORD_DOES_NOT_EXIST = "审批失败，排班记录不存在!";
	private static final String APPROVAL_SUCCESS = "审批成功";
	private static final Long CONFIRM_STATUS = 0l;
	private static final Long SYNC_STATUS = 0l;
	private static final Long AGREE_APPROVAL = 2l;

	public HashMap<String, Object> queryApprovalList(HashMap<String, String> params) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		List<ApplyRecord> applyRecords = applyDao.queryApprovalList(params);
		int totalSize = applyDao.queryApprovalCount(params);
		dataMap.put(TOTAL_SIZE, totalSize);
		dataMap.put(ROOT, applyRecords);
		return dataMap;
	}

	@Transactional
	public String updateApply(HashMap<String, String> params) {
		ApplyRecord applyRecord = getApplyRecordByApplyId(Long.parseLong(params.get("applyId")));
		if (null == applyRecord) {
			return THE_APPLYRECORD_DOES_NOT_EXIST;
		}
		List<DriverScheduling> schedulings = queryDriverScheduling(applyRecord);
		if (schedulings.isEmpty())
			return THE_SCHEDULINGRECORD_DOES_NOT_EXIST;
		String msg = validWhetherCanBeModified(applyRecord.getApplyEmployeeCode(), applyRecord.getDayOfMonth(), applyRecord.getDepartmentCode());
		if (Long.parseLong(params.get("status")) == AGREE_APPROVAL && StringUtil.isNotBlank(msg)) {
			return msg;
		}
		if (applyRecord.getApplyType() == TRANSFER_CLASSES
				&& Long.parseLong(params.get("status")) == AGREE_APPROVAL)
			updateSchedulingData(applyRecord, schedulings);
		ApplyRecord apply = buildUpdateApplyBean(params, applyRecord);
		applyDao.update(apply);
		Message messeage = apply.buildAprrovlResultMesseage();
		messageDao.save(messeage);
		return APPROVAL_SUCCESS;
	}

	@Transactional
	public void applyLeave(ApplyRecord applyRecord) {
		applyDao.save(applyRecord);
		messageDao.save(applyRecord.buildPendingAprrovlMesseage());
	}

	@Transactional
	public HashMap<String, Object> queryLeave(String employeeCode, String start, String limit) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(ROOT, applyDao.queryLeave(employeeCode, start, limit));
		resultMap.put(TOTAL_SIZE, applyDao.queryLeaveCount(employeeCode));

		return resultMap;
	}

	@Transactional
	public void revokeLeaveApply(HashMap<String, String> params) {
		reovkeApply(params, 1L);
	}

	@Transactional
	public ApplyRecord revokeExchangeScheduling(HashMap<String, String> params) {
		return reovkeApply(params, 2L);
	}

	private ApplyRecord reovkeApply(HashMap<String, String> params, long applyType) {
		DetachedCriteria dc = DetachedCriteria.forClass(ApplyRecord.class);
		dc.add(Restrictions.eq("applyEmployeeCode", params.get(KEY_EMPLOYEE_CODE)));
		dc.add(Restrictions.eq("dayOfMonth", params.get("dayOfMonth")));
		dc.add(Restrictions.eq(KEY_APPLY_TYPE, applyType));
		dc.addOrder(Order.desc("applyId"));
		List<ApplyRecord> applyRecords = applyDao.findBy(dc);
		ApplyRecord applyRecord = applyRecords.get(0);
		applyRecord.setStatus(4L);
		applyDao.update(applyRecord);
		
		return applyRecord;
	}

	private ApplyRecord buildUpdateApplyBean(HashMap<String, String> params, ApplyRecord apply) {
		try {
			apply.setApproverInfo(java.net.URLDecoder.decode(java.net.URLEncoder.encode(params.get("overruleInfo"), "ISO-8859-1"),"UTF-8"));
			apply.setStatus(Long.parseLong(params.get("status")));
			apply.setApprover(params.get(KEY_EMPLOYEE_CODE));
			apply.setApporveTime(new Date());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return apply;
	}

	private void updateSchedulingData(ApplyRecord apply, List<DriverScheduling> schedulings) {
		DriverScheduling updateDriverScheduling = schedulings.get(0);
		updateDriverScheduling.setConfigureCode(apply.getNewConfigCode());
		updateDriverScheduling.setSyncState(SYNC_STATUS);
		updateDriverScheduling.setModifiedTime(new Date());
		driverSchedulingDao.update(updateDriverScheduling);
		DriverScheduling updateConfirmStatus = new DriverScheduling();
		updateConfirmStatus.setYearWeek(updateDriverScheduling.getYearWeek());
		updateConfirmStatus.setEmployeeCode(apply.getApplyEmployeeCode());
		updateConfirmStatus.setSchedulingType(1L);
		List<DriverScheduling> updateConfirmStatusList = driverSchedulingDao.findBy(updateConfirmStatus);
		for (DriverScheduling driverSchedul : updateConfirmStatusList) {
			driverSchedul.setConfirmStatus(CONFIRM_STATUS);
		}
		driverSchedulingDao.updateBatch(updateConfirmStatusList);
	}

	private List<DriverScheduling> queryDriverScheduling(ApplyRecord apply) {
		DriverScheduling driverScheduling = new DriverScheduling();
		driverScheduling.setEmployeeCode(apply.getApplyEmployeeCode());
		driverScheduling.setDayOfMonth(apply.getDayOfMonth());
		driverScheduling.setDepartmentCode(apply.getDepartmentCode());
		driverScheduling.setSchedulingType(1L);
		List<DriverScheduling> schedulings = driverSchedulingDao.findBy(driverScheduling);
		return schedulings;
	}

	private String validWhetherCanBeModified(String employeeCode, String day, String departmentCode) {
		Map<String, Object> map = driverSchedulingDao.queryEmployeeConvertDate(employeeCode);
		// 当排班日期大于离职日期时
		if (Integer.parseInt(day) >= Integer.parseInt(map.get("DIMISSION_DT").toString())
				&& Integer.parseInt(map.get("DIMISSION_DT").toString()) != 0) {
			return employeeCode + "员工已于"+ map.get("DIMISSION_DT").toString() +"离职，"+ Integer.parseInt(day)+"的申请请驳回！";
		}
		// 当排班日期大于转岗日期时
		if (Integer.parseInt(map.get("TRANSFER_DATE").toString()) != 0) {
			if (!map.get("EMP_POST_TYPE").toString().equals("5")) {
				if (Integer.parseInt(day) >= Integer.parseInt(map.get("TRANSFER_DATE").toString())) {
				return	employeeCode + "员工已于"+ Integer.parseInt(map.get("TRANSFER_DATE").toString()) +"转出司机岗，"+ Integer.parseInt(day)+"的申请请驳回！";
				}
			}
		}
		return "";
	}

	public ApplyRecord getApplyRecordByApplyId(Long applyId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ApplyRecord.class);
		List<ApplyRecord> applyRecord = applyDao.findBy(dc.add(Restrictions.eq("applyId", applyId)));
		return applyRecord.isEmpty() ? null : applyRecord.get(0);
	}

	public HashMap<String, Object> queryMyExchangeScheduling(HashMap<String, String> params) {
		DetachedCriteria dc = DetachedCriteria.forClass(ApplyRecord.class);
		dc.add(Restrictions.eq("applyEmployeeCode", params.get(KEY_EMPLOYEE_CODE)));
		dc.add(Restrictions.eq(KEY_APPLY_TYPE, 2L));
		dc.addOrder(Order.desc("applyId"));
		IPage<ApplyRecord> page = applyDao.findPageBy(dc, Integer.parseInt(params.get(KEY_PAGE_SIZE)), Integer.parseInt(params.get(KEY_PAGE_INDEX)));
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put(TOTAL_SIZE, page.getTotalPage());
		dataMap.put(ROOT, page.getData());
		return dataMap;
	}

	public HashMap<String, Object> queryPendingCount(String employeeCode) {
		HashMap<String, String> params = newHashMap();
		params.put(KEY_EMPLOYEE_CODE, employeeCode);
		params.put(KEY_APPLY_TYPE, "1");
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put(KEY_LEAVE_COUNT, applyDao.queryApprovalCount(params));
		params.put(KEY_APPLY_TYPE, "2");
		dataMap.put(KEY_EXCHANGE_COUNT, applyDao.queryApprovalCount(params));
		return dataMap;
	}

	public HashMap<String, Object> queryTheApprover(HashMap<String, String> params) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		int total = applyDao.queryTheApproverCount(params);
		List list = applyDao.queryTheApproverList(params);
		
		dataMap.put("totalSize", total);
		dataMap.put("root", list);
		
		return dataMap;
	}
}
