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
import com.sf.module.domain.ApplyRecord;
import com.sf.module.domain.OutEmployee;
import com.sf.module.domain.StringUtil;

@Scope("prototype")
@Controller
public class DriverProxyAction extends ActionSupport {
	private String employeeCode;
	private String yearWeek;
	private OutEmployee employee;
	private ApplyRecord applyRecord;
	private Map<String, Object> resultMap;
	private static final String DRIVERS_LEADER = "司机组长";
	private static final String TOINDEXPAGE = "toIndexPage";
	private static final String TOLEADERPAGE = "toLeaderPage";
	private URL url;
	private InputStream inputStream;
	private BufferedReader bufferedReader;
	private String urlPath;

	public String toPageIndex() {
		try {
			urlPath = StringUtil.urlPath  + "/toPageIndexApp.action?employeeCode="
					+ employeeCode;
			String result = searchDataOfUrl(urlPath);

			HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
					new TypeToken<HashMap<String, Object>>() {
					});
			employee = JsonConverter.convertJsonToObject(
					JsonConverter.convertObjectToJson(map.get("employee")),
					new TypeToken<OutEmployee>() {
					});
			if (employee.getEmpDutyName().indexOf(DRIVERS_LEADER) != -1) {
				urlPath = StringUtil.urlPath  + "/queryPendingCount.action?employeeCode="
						+ employeeCode;
				String countResult = searchDataOfUrl(urlPath);
				HashMap<String, Object> countMap = JsonConverter.convertJsonToObject(countResult,
						new TypeToken<HashMap<String, Object>>() {
						});
				HashMap map1 = JsonConverter.convertJsonToObject(
						JsonConverter.convertObjectToJson(countMap.get("resultMap")),
						new TypeToken<HashMap<String, Object>>() {
						});
				resultMap = new HashMap<String, Object>();
				resultMap.put("leaveCount", map1.get("leaveCount").toString().replace(".0", ""));
				resultMap.put("exchangeCount", map1.get("exchangeCount").toString().replace(".0", ""));
				return TOLEADERPAGE;
			}
			yearWeek = map.get("yearWeek").toString();
			return TOINDEXPAGE;
		} catch (Exception e) {
			return "error";
		}
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

	@ResponseBody
	public String myScheduling() {
		try {
			urlPath = StringUtil.urlPath + "/myScheduling.action?employeeCode="
					+ employeeCode + "&yearWeek=" + yearWeek;
			resultMap = new HashMap<String, Object>();
			resultMap.put("root", searchDataOfUrl(urlPath));
			return "success";
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String toMyLeave() {
		urlPath = StringUtil.urlPath + "/toPageIndexApp.action?employeeCode="
				+ employeeCode;
		String result = searchDataOfUrl(urlPath);

		HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
				new TypeToken<HashMap<String, Object>>() {
				});
		employee = JsonConverter.convertJsonToObject(
				JsonConverter.convertObjectToJson(map.get("employee")),
				new TypeToken<OutEmployee>() {
				});
		return SUCCESS;
	}

	public String queryLeave() {
		HashMap<String, String> params = getHttpRequestParameter();
		urlPath = StringUtil.urlPath + "/queryLeave.action?employeeCode="
				+ employeeCode
				+ "&start=" + params.get("start") + "&limit=" + params.get("limit");
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath));
		return SUCCESS;
	}

	public String toMyExchangeScheduling() {
		urlPath = StringUtil.urlPath + "/toPageIndexApp.action?employeeCode="
				+ employeeCode;
		String result = searchDataOfUrl(urlPath);

		HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
				new TypeToken<HashMap<String, Object>>() {
				});
		employee = JsonConverter.convertJsonToObject(
				JsonConverter.convertObjectToJson(map.get("employee")),
				new TypeToken<OutEmployee>() {
				});
		return SUCCESS;
	}

	public String myExchangScheduling() {
		HashMap<String, String> params = getHttpRequestParameter();
		urlPath = StringUtil.urlPath + "/myExchangScheduling.action?employeeCode="
				+ employeeCode
				+ "&pageIndex=" + params.get("pageIndex") + "&pageSize="
				+ params.get("pageSize");
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath));
		return SUCCESS;
	}

	public String toLeave() {
		urlPath = StringUtil.urlPath + "/toLeave.action?applyRecord.dayOfMonth="
				+ applyRecord.getDayOfMonth() + "&applyRecord.applyEmployeeCode="
				+ applyRecord.getApplyEmployeeCode() + "&applyRecord.departmentCode="
				+ applyRecord.getDepartmentCode() + "&applyRecord.applyType="
				+ applyRecord.getApplyType() + "&applyRecord.status=" + applyRecord.getStatus()
				+ "&applyRecord.oldConfigCode=" + applyRecord.getOldConfigCode()
				+ "&applyRecord.applyInfo=" + applyRecord.getApplyInfo().replaceAll(" ", "")
				+ "&applyRecord.approver=" + applyRecord.getApprover();
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath));
		return SUCCESS;
	}

	public String toDriverLeaderPage() {
		urlPath = StringUtil.urlPath + "/queryPendingCount.action?employeeCode="
				+ employeeCode;
		String countResult = searchDataOfUrl(urlPath);
		HashMap<String, Object> countMap = JsonConverter.convertJsonToObject(countResult,
				new TypeToken<HashMap<String, Object>>() {
				});
		HashMap map1 = JsonConverter.convertJsonToObject(
				JsonConverter.convertObjectToJson(countMap.get("resultMap")),
				new TypeToken<HashMap<String, Object>>() {
				});
		resultMap = new HashMap<String, Object>();
		resultMap.put("leaveCount", map1.get("leaveCount").toString().replace(".0", ""));
		resultMap.put("exchangeCount", map1.get("exchangeCount").toString().replace(".0", ""));
		urlPath = StringUtil.urlPath + "/toPageIndexApp.action?employeeCode="
				+ employeeCode;
		String result = searchDataOfUrl(urlPath);
		HashMap<String, Object> map = JsonConverter.convertJsonToObject(result,
				new TypeToken<HashMap<String, Object>>() {
				});
		employee = JsonConverter.convertJsonToObject(
				JsonConverter.convertObjectToJson(map.get("employee")),
				new TypeToken<OutEmployee>() {
				});
		return TOLEADERPAGE;
	}

	public String queryLineConfigueForExchangeScheduling() {
		HashMap<String, String> params = getHttpRequestParameter();
		urlPath = StringUtil.urlPath + "/queryLineConfigueForExchangeScheduling.action";
		String param = "?CODE=" + params.get("CODE") + "&start=" + params.get("start") + "&limit="
				+ params.get("limit") + "&dayOfMonth=" + params.get("dayOfMonth")
				+ "&employeeCode=" + params.get("employeeCode")
				+ "&currentCode=" + params.get("currentCode");
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath + param));
		return SUCCESS;
	}

	public String exchangeScheduling() {
		urlPath = StringUtil.urlPath + "/exchangeScheduling.action";
		String param = "?applyRecord.oldConfigCode=" + applyRecord.getOldConfigCode()
				+ "&applyRecord.applyEmployeeCode=" + applyRecord.getApplyEmployeeCode()
				+ "&applyRecord.newConfigCode=" + applyRecord.getNewConfigCode()
				+ "&applyRecord.applyInfo=" + applyRecord.getApplyInfo().replaceAll(" ", "")
				+ "&applyRecord.dayOfMonth=" + applyRecord.getDayOfMonth()
				+ "&applyRecord.approver=" + applyRecord.getApprover();
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath + param));
		return SUCCESS;
	}

	public String confirmScheduling() {
		urlPath = StringUtil.urlPath + "/confirmScheduling.action";
		String param = "?yearWeek=" + yearWeek + "&employeeCode=" + employeeCode;
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath + param));
		return SUCCESS;
	}
	
	public String queryTheApprover() {
		HashMap<String, String> params = getHttpRequestParameter();
		
		urlPath = StringUtil.urlPath + "/queryTheApprover.action?departmentCode="
				+ params.get("departmentCode") + "&start=" + params.get("start") + "&limit=" + params.get("limit")+"&searchString=" + params.get("searchString");
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("root", searchDataOfUrl(urlPath));
		return SUCCESS;
	}

	public ApplyRecord getApplyRecord() {
		return applyRecord;
	}

	public void setApplyRecord(ApplyRecord applyRecord) {
		this.applyRecord = applyRecord;
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
