package com.sf.module.dispatch.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.sf.framework.core.domain.UserStatus;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.framework.server.core.context.UserContext;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.dispatch.biz.SchedulingForDispatchBiz;
import com.sf.module.dispatch.domain.SchedulingForDispatch;

public class SchedulingAction extends BaseAction {
	private static final String DEPARTMENT_CODES = "departmentCodes";
	private HashMap<String, Object> dataMap;
	private SchedulingForDispatchBiz schedulingForDispatchBiz;
	private File importExcelFile;
	private String fileName;
	private boolean success;
	private String msg;
	private Collection schedulMgts;
	private String bizMsg;
	private String dataStr;

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public String getBizMsg() {
		return bizMsg;
	}

	public void setBizMsg(String bizMsg) {
		this.bizMsg = bizMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public File getImportExcelFile() {
		return importExcelFile;
	}

	public void setImportExcelFile(File importExcelFile) {
		this.importExcelFile = importExcelFile;
	}

	public String querySingleShedulingInfo() {
		dataMap = new HashMap();
		List list = schedulingForDispatchBiz.queryOneDayScheduling(parseParameter());
		dataMap.put("root", list);
		success = true;
		return SUCCESS;
	}

	public String saveScheduling() throws JSONException {
		List<SchedulingForDispatch> insetList = new ArrayList<SchedulingForDispatch>();
		SchedulingForDispatch dispatch;

		HashMap<String, String> map = parseParameter();
		JSONObject jsonObject = JSONObject.fromObject(map);
		List<Map<String, Object>> insetDataList = (List<Map<String, Object>>) JSONUtil.deserialize(map.get("data"));

		for (int i = 0; i < insetDataList.size(); i++) {
			Map<String, Object> tmp = insetDataList.get(i);
			dispatch = new SchedulingForDispatch();
			dispatch.setEmployeeCode((String) tmp.get("EMPLOYEE_CODE"));
			dispatch.setDepartmentCode((String) tmp.get("DEPARTMENT_CODE"));
			dispatch.setDayOfMonth(tmp.get("YEARS").toString() + tmp.get("MONTH_ID").toString() + tmp.get("DAY_OF_MONTH"));
			dispatch.setMonthId(tmp.get("YEARS").toString() + tmp.get("MONTH_ID").toString());
			dispatch.setBeginTime(tmp.get("BEGIN_TIME").toString() + "00");
			dispatch.setEndTime(tmp.get("END_TIME").toString() + "00");
			dispatch.setCreatedEmployeeCode((String) tmp.get("CREATED_EMPLOYEE_CODE"));
			dispatch.setModifiedEmployeeCode(getCurrentUser().getUsername());
			dispatch.setEmpPostType("2");
			if (tmp.get("ACROSS_NAME").equals("是")) {
				dispatch.setCrossDayType("X");
			}
			dispatch.setModifiedTime(new Date());
			insetList.add(dispatch);
		}
		SchedulingForDispatch delteObj = new SchedulingForDispatch();
		delteObj.setEmployeeCode((String) map.get("EMPLOYEE_CODE"));
		delteObj.setMonthId((String) map.get("MONTH_ID"));
		delteObj.setDayOfMonth((String) map.get("DAY_OF_MONTH"));
		
		if (insetList.isEmpty()) {
			dispatch = new SchedulingForDispatch();
			dispatch.setEmployeeCode(map.get("EMPLOYEE_CODE"));
			dispatch.setDepartmentCode(map.get("DEPARTMENT_CODE"));
			dispatch.setDayOfMonth(map.get("DAY_OF_MONTH"));
			dispatch.setMonthId(map.get("MONTH_ID"));
			dispatch.setEmpPostType("2");
			dispatch.setModifiedEmployeeCode(getCurrentUser().getUsername());
			dispatch.setCreatedEmployeeCode(getCurrentUser().getUsername());
			dispatch.setCreateTime(new Date());
			dispatch.setModifiedTime(new Date());
			
			insetList.add(dispatch);
		}

		schedulingForDispatchBiz.saveOrDeleScheduling(insetList, delteObj);
		return SUCCESS;
	}

	public String querySchedule() {
		dataMap = new HashMap();

		try {
			dataMap = (HashMap) schedulingForDispatchBiz.queryAll(parseParameter());
			success = true;
			dataMap.put("success", true);
		} catch (Exception e) {
			log.error("error", e);
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage());
		}

		return SUCCESS;
	}

	public String importScheduling() {
		try {
			String departmentCodes = parseParameter().get(DEPARTMENT_CODES);

			dataMap = schedulingForDispatchBiz.importScheduling(importExcelFile, departmentCodes);
			msg = (String) dataMap.get("msg");
			fileName = (String) dataMap.get("errorExcelPath");
			schedulMgts = (Collection) dataMap.get("root");
			success = true;
		} catch (BizException e) {
			bizMsg = e.getMessageKey();
		} catch (Exception e) {
			success = false;
			bizMsg = e.getMessage();
			log.debug("exception", e);
		}
		return SUCCESS;
	}

	public String export() {
		try {
			fileName = schedulingForDispatchBiz.getExportExcel(parseParameter());
			success = true;
		} catch (BizException e) {
			success = false;
			msg = e.getMessageKey();
		}
		return SUCCESS;
	}

	public String exportSchedulingDeatail() {
		try {
			fileName = schedulingForDispatchBiz.exportDispatchSchedulingDetail(parseParameter());
			success = true;
		} catch (Exception e) {
			success = false;
			msg = e.getMessage();
		}
		return SUCCESS;
	}

	public String deleteScheulDaily() {
		try {
			schedulingForDispatchBiz.deleteScheulDaily(parseParameter());
			success = true;
		} catch (Exception e) {
			success = false;
			this.msg = e.getMessage();
		}
		return SUCCESS;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String queryPermissions (){
		dataMap = new HashMap();
		Long userId = (Long) UserContext.getContext().getUserId();
		String status = UserContext.getContext().getCurrentUser().getStatus().toString();
		if(!status.equalsIgnoreCase("ROOT")){
			HashMap<String, String> map = ServletActionHelper.getHttpRequestParameter();
			dataMap = (HashMap) schedulingForDispatchBiz.queryPermissions(map,userId.toString());			
		}
		return SUCCESS;
	}

	public String queryIsBeOverdue() {
		success = schedulingForDispatchBiz.queryIsBeOverdue(parseParameter());
		return SUCCESS;
	}

	private HashMap<String, String> parseParameter() {
		return ServletActionHelper.getHttpRequestParameter();
	}

	public HashMap<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setSchedulingForDispatchBiz(SchedulingForDispatchBiz schedulingForDispatchBiz) {
		this.schedulingForDispatchBiz = schedulingForDispatchBiz;
	}

	public Collection getSchedulMgts() {
		return schedulMgts;
	}

	public void setSchedulMgts(Collection schedulMgts) {
		this.schedulMgts = schedulMgts;
	}

}
