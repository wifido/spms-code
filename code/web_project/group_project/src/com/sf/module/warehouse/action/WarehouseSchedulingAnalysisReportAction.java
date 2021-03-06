package com.sf.module.warehouse.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.warehouse.biz.WarehouseSchedulingAnalysisReportBiz;

@Controller
public class WarehouseSchedulingAnalysisReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Resource
	private WarehouseSchedulingAnalysisReportBiz warehouseSchedulingAnalysisReportBiz;

	private HashMap<String, Object> resultMap = new HashMap<String, Object>();
	
	private boolean success;
	
	public String query() {
		resultMap = warehouseSchedulingAnalysisReportBiz.query(getQueryValueByKey("departmentCode"), 
				getQueryValueByKey("yearMonth").replace("-", ""),
				ServletActionHelper.getStart(getHttpRequestParameter()),
				ServletActionHelper.getLimit(getHttpRequestParameter()));
		success = true;
		return SUCCESS;
	}
	
	public String export() {
		resultMap = warehouseSchedulingAnalysisReportBiz.export(getQueryValueByKey("departmentCode"), 
				getQueryValueByKey("yearMonth").replace("-", ""),
				ServletActionHelper.getStart(getHttpRequestParameter()),
				ServletActionHelper.getLimit(getHttpRequestParameter()));
		success = true;
		return SUCCESS;
	}
	
	private String getQueryValueByKey(String key) {
		return getHttpRequestParameter().get(key);
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
}
