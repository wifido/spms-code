package com.sf.module.driver.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.driver.biz.LineConfigureBiz;
import java.io.File;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

@Scope("prototype")
@Component("lineConfigure")
public class LineConfigureAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private LineConfigureBiz lineConfigureBiz;
	private HashMap<String, Object> resultMap;
	private File uploadFile;
	private boolean success = true;

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
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

	private String classesCode;
	
	private int existClassCode;
	
	public int getExistClassCode() {
		return existClassCode;
	}

	public void setExistClassCode(int existClassCode) {
		this.existClassCode = existClassCode;
	}

	public String getClassesCode() {
		return classesCode;
	}

	public void setClassesCode(String classesCode) {
		this.classesCode = classesCode;
	}

	public String queryClassesCode() {
		String departmentCode = getHttpRequestParameter().get("departmentCode");
		String yearMonth = getHttpRequestParameter().get("yearMonth");
		classesCode = lineConfigureBiz.queryClassesCode(departmentCode, yearMonth);

		return SUCCESS;
	}
	
    public String queryConfigureSchedulingAllLine() {
        resultMap = lineConfigureBiz.queryConfigureSchedulingAllLine(getHttpRequestParameter());
        return SUCCESS;
    }

	public String addConfigureClassesInformation() {
		lineConfigureBiz.addConfigureClasses(getHttpRequestParameter());

		return SUCCESS;
	}
	
	public String addMobileNetwork() {
		lineConfigureBiz.addMobileNetwork(getHttpRequestParameter());
		return SUCCESS;
	}

    public String batchUpdateValidState() {
        lineConfigureBiz.batchUpdateValidState(getHttpRequestParameter());
        return SUCCESS;
    }
    
    public String batchDelete() {
    	lineConfigureBiz.batchDelete(getHttpRequestParameter().get("deleteIds"));
    	return SUCCESS;
    }

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String query() {
		this.resultMap = lineConfigureBiz.queryLineConfigures(getHttpRequestParameter());
		return SUCCESS;
	}

	public String export() {
		this.resultMap = lineConfigureBiz.exportLineConfigure(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String importDynamicLineConfigure() {
		this.resultMap = lineConfigureBiz.importDynamicLineConfigure(uploadFile, getHttpRequestParameter());
		return SUCCESS;
	}

	public String importLineConfigure() {
		this.resultMap = lineConfigureBiz.importLineConfigure(uploadFile, getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String update() {
		lineConfigureBiz.updateLineConfigure(getHttpRequestParameter());
		return SUCCESS;
	}

	public String validClassesCode() {
		String departmentCode = getHttpRequestParameter().get("departmentCode");
		String yearMonth = getHttpRequestParameter().get("yearMonth");
		String code = getHttpRequestParameter().get("code");
		existClassCode = lineConfigureBiz.validClassesCode(departmentCode, yearMonth, code);
		return SUCCESS;
	}
}
