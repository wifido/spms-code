package com.sf.module.report.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.report.biz.SchedulingInputStatisticalBiz;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;

@Controller
public class SchedulingInputStatisticalAction extends BaseAction{
	@Resource
	private SchedulingInputStatisticalBiz schedulingInputStatisticalBiz;

    private HashMap<String, Object> resultMap;
    private String downloadPath;
    private boolean success;

    public String query() {
        resultMap = schedulingInputStatisticalBiz.query(getQueryValueByKey("departmentCode"),
                getQueryValueByKey("yearMonth"), getQueryValueByKey("start"), getQueryValueByKey("limit"));
        success = true;
        return SUCCESS;
    }

    public String export() {
        downloadPath = schedulingInputStatisticalBiz.export(getQueryValueByKey("departmentCode"), getQueryValueByKey("yearMonth"));
        success = true;
        return SUCCESS;
    }

    private String getQueryValueByKey(String key) {
        return getHttpRequestParameter().get(key);
    }

    public HashMap<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(HashMap<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
