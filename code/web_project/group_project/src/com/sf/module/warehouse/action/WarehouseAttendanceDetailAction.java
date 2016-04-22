package com.sf.module.warehouse.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.warehouse.biz.WarehouseAttendanceDetailBiz;

@Scope("prototype")
@Controller
public class WarehouseAttendanceDetailAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> dataMap;
	
	@Resource
	private WarehouseAttendanceDetailBiz attendanceDetailBiz;
	
	public String query() {
		dataMap = attendanceDetailBiz.query(ServletActionHelper.getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String export() {
		dataMap = attendanceDetailBiz.exportWarehouseAttendanceDetail(ServletActionHelper.getHttpRequestParameter());
		
		return SUCCESS;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}
