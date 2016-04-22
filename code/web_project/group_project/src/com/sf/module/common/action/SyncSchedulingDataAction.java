package com.sf.module.common.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

import java.util.Date;
import java.util.Map;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.biz.SyncSchedulingDataBiz;
import com.sf.module.common.biz.SysConfigBiz;
import com.sf.module.common.domain.SysConfig;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.esbinterface.fileUpload.SchedulePlanUploader;

public class SyncSchedulingDataAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	
	private SyncSchedulingDataBiz syncSchedulingDataBiz;
	private SchedulePlanUploader schedulePlanUploader;
	private SysConfigBiz sysConfigBiz;
	
	private Map<String, Object> resultMap;
	
	private boolean success;
	
	private String fileName;
	
	public String query() {
		resultMap = syncSchedulingDataBiz.query(getHttpRequestParameter());
		success = true;
		return SUCCESS;
	}

	public String export() {
		fileName = syncSchedulingDataBiz.exportSyncSchedulinData(getHttpRequestParameter());
		success = true;
		return SUCCESS;
	}
	
	public String handlePush() {
		try {
			this.log.info("==========handle push is starting");
			
			SysConfig model = sysConfigBiz.searchByKeyName(StringUtil.SPMS2SAP_SCHEDULE);

			model.setKeyName(StringUtil.SPMS2SAP_SCHEDULE);
			model.setKeyDesc(StringUtil.SPMS2SAP_SCHEDULE_DESC);
			model.setKeyValue(DateUtil.formatDate(new Date(), DateFormatType.FULL_TIME));
			
			sysConfigBiz.saveOrUpdate(model);
			schedulePlanUploader.upload();
			
		} catch (Exception e) {
			this.log.error("handle push is error:"+e.toString());
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public SyncSchedulingDataBiz getSyncSchedulingDataBiz() {
		return syncSchedulingDataBiz;
	}

	public void setSyncSchedulingDataBiz(SyncSchedulingDataBiz syncSchedulingDataBiz) {
		this.syncSchedulingDataBiz = syncSchedulingDataBiz;
	}
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setSchedulePlanUploader(SchedulePlanUploader schedulePlanUploader) {
		this.schedulePlanUploader = schedulePlanUploader;
	}

	public void setSysConfigBiz(SysConfigBiz sysConfigBiz) {
		this.sysConfigBiz = sysConfigBiz;
	}
	
}
