package com.sf.module.report.action;

import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.report.biz.StatisticsReportBiz;

@Scope("prototype")
@Controller
public class StatisticsReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private StatisticsReportBiz statisticsReportBiz;
	private Map<String, Object> resultMap;
	private boolean success = true;
	private String fileName;

	public String query() {
		resultMap = statisticsReportBiz.queryStatisticsReport(getHttpRequestParameter());
		return SUCCESS;
	}
	
	public String export() {
		fileName = statisticsReportBiz.export(getHttpRequestParameter());
		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

}
