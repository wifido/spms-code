package com.sf.module.driver.action;

import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.common.util.ServletActionHelper.*;
import static com.sf.module.operation.util.CommonUtil.isNotEmpty;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.ServletActionHelper;
import com.sf.module.driver.biz.LineManageBiz;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;

@Scope("prototype")
@Component("lineManage")
public class LineManageAction extends BaseAction {
	private static final String LINE_CONFIGURE_ID = "lineConfigureId";
	private static final long serialVersionUID = 1L;
	private static final String KEY_OPTIMIZE_LINE_CODE = "optimizeLineCode";
	private static final String KEY_VALID_STATUS = "validStatus";
	private static final String KEY_VEHICLE_NUMBER = "vehicleNumber";
	private static final String KEY_DEPARTMENT_ID = "departmentId";
	private static final String KEY_INPUT_TYPE = "inputType";
	private static final String UPDATE_ID = "updateId";
	private static final String UPDATE_BELONG_ZONE_CODE = "updateBelongZoneCode";
	private static final String UPDATE_DESTINATION_CODE = "updateDestinationCode";
	private static final String UPDATE_SOURCE_CODE = "updateSourceCode";
	private static final String UPDATE_VALID = "updateValid";
	private static final String ID = "ID";
	private static final String KEY_TRUE = "true";
	private static final String KEY_FALSE = "false";
	private static final String PLEASE_INPUT_CORRECT_DEPARTMENT_INFORMATION_TIP = "请输入正确的网点信息！";
	private static final String KEY_DEPARTMENT_CODE = "departmentCode";
	private static final int EXPORT_TOTAL_COUNT = 60000;
	private static final String VALID_STATUS = "valid_status";
	private static final String KEY_START_TIME = "startTime";
	private static final String KEY_CONFIGURE_STATUS = "configureStatus";
	@Resource
	private LineManageBiz lineManageBiz;
	private HashMap<String, Object> resultMap;
	private boolean success = true;
	private String fileName;
	private File uploadFile;
	private String msg;
	private Collection lineSchedulingMgts;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String queryLine() {
		HashMap<String, String> httpRequestParameter = getHttpRequestParameter();
		convertDepartmentCodeToID(httpRequestParameter);
		this.resultMap = lineManageBiz.queryLinesBySpecifyParameter(
		        getValueByKey(httpRequestParameter, KEY_DEPARTMENT_ID),
		        getValueByKey(httpRequestParameter, KEY_INPUT_TYPE),
		        getValueByKey(httpRequestParameter, KEY_VEHICLE_NUMBER),
		        getValueByKey(httpRequestParameter, KEY_VALID_STATUS),
		        getValueByKey(httpRequestParameter, KEY_START_TIME),
		        getValueByKey(httpRequestParameter, KEY_CONFIGURE_STATUS),
		        getStart(httpRequestParameter),
		        getLimit(httpRequestParameter));

		return SUCCESS;
	}

	private void convertDepartmentCodeToID(HashMap<String, String> httpRequestParameter) {
		String departmentCode = httpRequestParameter.get(KEY_DEPARTMENT_CODE);
		if (isNotEmpty(departmentCode)) {
			Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
			httpRequestParameter.put(KEY_DEPARTMENT_ID, department.getId().toString());
		}
	}

	public String queryLineByLineConfigureId() {
		this.resultMap = lineManageBiz.queryLineByLineConfigureId(Integer.parseInt(getHttpRequestParameter().get(LINE_CONFIGURE_ID)));
		return SUCCESS;
	}

	public String exportLine() {
		HashMap<String, String> httpRequestParameter = getHttpRequestParameter();
		fileName = lineManageBiz.exportLine(
		        getValueByKey(httpRequestParameter, KEY_DEPARTMENT_ID),
		        getValueByKey(httpRequestParameter, KEY_INPUT_TYPE),
		        getValueByKey(httpRequestParameter, KEY_VEHICLE_NUMBER),
		        getValueByKey(httpRequestParameter, KEY_VALID_STATUS),
		        0,
		        EXPORT_TOTAL_COUNT,
		        getValueByKey(httpRequestParameter, KEY_CONFIGURE_STATUS));
		return SUCCESS;
	}

	public String addLine() {
		lineManageBiz.addLine(getHttpRequestParameter());
		return SUCCESS;
	}

	public String searchDepartmentValid() {
		resultMap = new HashMap<String, Object>();
		resultMap.put(KEY_SUCCESS, KEY_TRUE);

		String departmentCode = getHttpRequestParameter().get(KEY_DEPARTMENT_CODE);
		Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);

		if (department == null) {
			resultMap.put(KEY_SUCCESS, KEY_FALSE);
			resultMap.put(KEY_MESSAGE, PLEASE_INPUT_CORRECT_DEPARTMENT_INFORMATION_TIP);
		}
		return SUCCESS;
	}

	public String updateLineById() {
		HashMap<String, String> httpRequestParameter = ServletActionHelper.getHttpRequestParameter();
		lineManageBiz.updateLine(
		        Long.parseLong(getValueByKey(httpRequestParameter, UPDATE_ID)),
		        getValueByKey(httpRequestParameter, UPDATE_BELONG_ZONE_CODE),
		        getValueByKey(httpRequestParameter, UPDATE_DESTINATION_CODE),
		        getValueByKey(httpRequestParameter, UPDATE_SOURCE_CODE),
		        Long.parseLong(getValueByKey(httpRequestParameter, UPDATE_VALID)),
		        getValueByKey(httpRequestParameter, KEY_VEHICLE_NUMBER));
		
		success = true;

		return SUCCESS;
	}

	public String deleteLine() {
		HashMap<String, String> httpRequestParameter = ServletActionHelper.getHttpRequestParameter();
		success = lineManageBiz.deleteLine(getValueByKey(httpRequestParameter, ID));
		return SUCCESS;
	}

	public String importLine() {
		resultMap = lineManageBiz.importLine(uploadFile);
		success = true;
		return SUCCESS;
	}

	public String updateValidStatus() {
		HashMap<String, String> httpRequestParameter = ServletActionHelper.getHttpRequestParameter();
		success = lineManageBiz.batchUpdteValidStatus(getValueByKey(httpRequestParameter, ID), getValueByKey(httpRequestParameter, VALID_STATUS));
		return SUCCESS;
	}

	public LineManageBiz getLineManageBiz() {
		return lineManageBiz;
	}

	public HashMap<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public Collection getLineSchedulingMgts() {
		return lineSchedulingMgts;
	}

	public void setLineSchedulingMgts(Collection lineSchedulingMgts) {
		this.lineSchedulingMgts = lineSchedulingMgts;
	}
}
