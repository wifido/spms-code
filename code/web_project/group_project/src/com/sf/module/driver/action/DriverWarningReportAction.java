package com.sf.module.driver.action;

import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.driver.biz.DriverWarningBiz;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.util.ServletActionHelper.getHttpRequestParameter;
import static java.lang.Integer.parseInt;

@Component
public class DriverWarningReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	@Resource
    private DriverWarningBiz driverWarningBiz;

    private static final String KEY_DEPARTMENT_CODE = "DEPARTMENT_CODE";
    private static final String KEY_WARNING_MONTH = "WARNING_MONTH";

    private Map<String, Object> result;
    private String filePath;

    public String query() {
    	HashMap<String, String> requestParameter = getHttpRequestParameter();
		result = driverWarningBiz.query(requestParameter.get(KEY_DEPARTMENT_CODE),
                requestParameter.get(KEY_WARNING_MONTH),
                parseInt(requestParameter.get(START)),
                parseInt(requestParameter.get(LIMIT)));

        return SUCCESS;
    }

    public String export() {
    	HashMap<String, String> requestParameter = getHttpRequestParameter();
		filePath = driverWarningBiz.export(
				requestParameter.get(KEY_DEPARTMENT_CODE),
				requestParameter.get(KEY_WARNING_MONTH));

        return SUCCESS;
    }

    public String exportContinuousWarning() {
		filePath = driverWarningBiz.exportContinuous(getHttpRequestParameter()
				.get(KEY_DEPARTMENT_CODE),
				getHttpRequestParameter().get(KEY_WARNING_MONTH));
        return SUCCESS;
    }

	public String queryDriverContinuousWarningReport() {
		HashMap<String, String> requestParameter = getHttpRequestParameter();
		result = driverWarningBiz
				.queryDriverContinuousWarningReport(
						requestParameter.get(KEY_DEPARTMENT_CODE),
						requestParameter.get(KEY_WARNING_MONTH),
						parseInt(requestParameter.get(START)),
						parseInt(requestParameter.get(LIMIT)));
        return SUCCESS;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
