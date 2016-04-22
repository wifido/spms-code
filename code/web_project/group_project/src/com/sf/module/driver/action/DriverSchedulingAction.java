package com.sf.module.driver.action;

import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import static com.sf.module.common.util.StringUtil.isBlank;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.biz.DriverSchedulingBiz;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.organization.domain.Department;

@Scope("prototype")
@Controller
public class DriverSchedulingAction extends BaseAction {
	private static Log logger = LogFactory.getLog(DriverSchedulingAction.class);
	private static final String AREA_DEPARTMENT = "areaDepartment";
	private static final String DEPARTMENT_CODE = "departmentCode";
	private static final long serialVersionUID = 1L;
	@Resource
	private DriverSchedulingBiz driverSchedulingBiz;
	private Map<String, Object> resultMap;
	private boolean success = true;
	private File uploadFile;
	private int totalSize;
	private String downloadPath;

	public String queryDriverScheduling() {
		this.resultMap = driverSchedulingBiz.queryDriverScheduling(getHttpRequestParameter());
		return SUCCESS;
	}

	public String queryDriverScheduledByWeek() {
		this.resultMap = driverSchedulingBiz.queryDriverScheduledByWeek(getHttpRequestParameter());
		return SUCCESS;
	}

	public String importScheduling() {
		this.resultMap = driverSchedulingBiz.importScheduling(uploadFile, getHttpRequestParameter());
		return SUCCESS;
	}

	public String exportDriverScheduling() {
		resultMap = driverSchedulingBiz.exportDriverScheduling(getHttpRequestParameter());
		return SUCCESS;
	}

	public String queryNoSchedulingEmployees() {
		resultMap = driverSchedulingBiz.queryNoSchedulingEmployees(getHttpRequestParameter());
		return SUCCESS;
	}

	public String addScheduling() throws IllegalAccessException, InvocationTargetException {
		resultMap = driverSchedulingBiz.addScheduling(getHttpRequestParameter());
		return SUCCESS;
	}

	public String checkDepartment() {
		Map<String, String> params = getHttpRequestParameter();
		String departmentCode = params.get(DEPARTMENT_CODE);
		success = isAreaCode(departmentCode);
		if (!success) {
			Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
			Department areaDepartment = DepartmentCacheBiz.getDepartmentByCode(department.getAreaDeptCode());
			resultMap = newHashMap();
			resultMap.put(AREA_DEPARTMENT, areaDepartment);
		}

		return SUCCESS;
	}

	// 按周导出
	public String weeklyExport() {
		// 获取导出总条数
		totalSize = driverSchedulingBiz.countAllByWeek(getHttpRequestParameter());

		if (totalSize > 0) {
			// 根据查询条件按周导出，获取下载文件路径
			downloadPath = driverSchedulingBiz.exportWeekReport(totalSize, getHttpRequestParameter());
		}

		return SUCCESS;
	}

	public String isAreaCode() {
		Map<String, String> params = getHttpRequestParameter();
		String departmentCode = params.get(DEPARTMENT_CODE);
		Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);

		if (StringUtil.isNotBlank(department.getAreaDeptCode())) {
			success = false;
			Department areaDepartment = DepartmentCacheBiz.getDepartmentByCode(department.getAreaDeptCode());
			resultMap = newHashMap();
			resultMap.put(AREA_DEPARTMENT, areaDepartment);
		}

		return SUCCESS;
	}

	private boolean isAreaCode(String departmentCode) {
		Department department = DepartmentCacheBiz.getDepartmentByCode(departmentCode);
		if (isBlank(department.getAreaDeptCode()) || department.getDeptCode().equals(department.getAreaDeptCode())) {
			return true;
		}
		return false;
	}

	public String updateDriverScheduling() throws IllegalAccessException, InvocationTargetException {
		HashMap<String, String> queryParameter = getHttpRequestParameter();
		resultMap =driverSchedulingBiz.updateDriverScheduling(queryParameter);
		resultMap.put("success", true);

		return SUCCESS;
	}

	// 导出没有排班人员数据
	public String exportNotSchedulingEmployee() {
		resultMap = driverSchedulingBiz.exportNotSchedulingEmployee(getHttpRequestParameter());

		return SUCCESS;
	}

	public String deleteScheduling() {
		driverSchedulingBiz.deleteScheduling(getHttpRequestParameter());
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

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
