package com.sf.module.driverapp;

import static com.sf.module.domain.StringUtil.getHttpRequestParameter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.sf.module.common.JsonConverter;
import com.sf.module.domain.OutEmployee;
import com.sf.module.domain.StringUtil;

@Scope("prototype")
@Controller
public class DriverLeaderProxyAction extends ActionSupport {
	private String employeeCode;
	private String yearWeek;
	private OutEmployee employee;
	private Map<String, Object> resultMap;
	private URL url;
	private InputStream inputStream;
	private BufferedReader bufferedReader;
	private String urlPath = StringUtil.urlPath;

	@ResponseBody
	public String toLeaderMyScheduledPage() {
		try {
			urlPath = urlPath + "/toPageIndexApp.action?employeeCode=" + employeeCode;
			String result = searchDataOfUrl(urlPath);
			HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
					new TypeToken<HashMap<String, Object>>() {
					});
			employee = JsonConverter.convertJsonToObject(
					JsonConverter.convertObjectToJson(map.get("employee")),
					new TypeToken<OutEmployee>() {
					});
			yearWeek = map.get("yearWeek").toString();
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	@ResponseBody
	public String queryPending() {
		try {
			urlPath = urlPath + "/queryPending.action";
			HashMap<String, String> params = getHttpRequestParameter();
			String param = "?employeeCode=" + params.get("employeeCode") + "&applyType="
					+ params.get("applyType") + "&start=" + params.get("start") + "&limit="
					+ params.get("limit") + "&searchString=" + params.get("searchString");
			String result = searchDataOfUrl(urlPath + param);
			resultMap = new HashMap<String, Object>();
			resultMap.put("root", result);
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String approval() {
		try {
			urlPath = urlPath + "/approval.action";
			HashMap<String, String> params = getHttpRequestParameter();
			String param = "?applyId=" + params.get("applyId") + "&overruleInfo="
					+ params.get("overruleInfo").replaceAll(" ", "") + "&status=" + params.get("status")
					+ "&employeeCode=" + params.get("employeeCode");
			String result = searchDataOfUrl(urlPath + param);
			HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
					new TypeToken<HashMap<String, Object>>() {
					});
			resultMap = new HashMap<String, Object>();
			resultMap.put("success", map.get("success").toString());
			resultMap.put("msg", map.get("msg").toString());
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String transferForward() {
		return SUCCESS;
	}

	public String leaveForward() {
		return SUCCESS;
	}

	public String revokeLeave() {
		urlPath = urlPath + "/revokeLeave.action";
		HashMap<String, String> params = getHttpRequestParameter();
		String param = "?dayOfMonth=" + params.get("dayOfMonth") + "&employeeCode="
				+ params.get("employeeCode");
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath + param));
		return SUCCESS;
	}

	public String revokeExchangeScheduling() {
		urlPath = urlPath + "/revokeExchangeScheduling.action";
		HashMap<String, String> params = getHttpRequestParameter();
		String param = "?dayOfMonth=" + params.get("dayOfMonth") + "&employeeCode="
				+ params.get("employeeCode");
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath + param));
		return SUCCESS;
	}

	private String searchDataOfUrl(String path) {
		try {
			url = new URL(path);
			inputStream = url.openStream();
			bufferedReader = new BufferedReader(new java.io.InputStreamReader(inputStream, "UTF-8"));
			return bufferedReader.readLine();
		} catch (Exception e) {
		}
		return null;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getYearWeek() {
		return yearWeek;
	}

	public void setYearWeek(String yearWeek) {
		this.yearWeek = yearWeek;
	}

	public OutEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(OutEmployee employee) {
		this.employee = employee;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
}
