package com.sf.module.warehouse.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.warehouse.biz.WarehouseAttendanceBiz;

@Scope("prototype")
@Controller
public class WarehouseAttendanceAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private WarehouseAttendanceBiz warehouseAttendanceBiz;

	private Map<String, Object> resultMap;
	private boolean success = true;

	public String query() {
		resultMap = warehouseAttendanceBiz.queryAttendance(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String export() {
		resultMap = warehouseAttendanceBiz.exportAttendance(getHttpRequestParameter());
		return SUCCESS;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

}
