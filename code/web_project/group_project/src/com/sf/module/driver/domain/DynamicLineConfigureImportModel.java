package com.sf.module.driver.domain;

import static com.sf.module.common.util.StringUtil.isNotBlank;
import java.text.ParseException;
import java.util.Date;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.domain.ImportModel;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;

public class DynamicLineConfigureImportModel extends ImportModel {
	private static final String REGEX_CONFIGURE_CODE = "^\\w{1,10}-\\d{5}$";
	private static final String REGEX_HOUR_MINUTE = "^(((0\\d{1}|1\\d{1}|2[0-3])[：:]([0-5]\\d{1}))|(24[：:]00))$";
	private static final String DEFAULT_DAY = "20141201";
	private static final String endDate = "00:00";
	private static final String endMaxDate = "24:00";
	private static final String startMaxDate = "24:00";
	private String configureCode;
	private String code;
	private String departmentCode;
	private String month;
	private String belongCode;
	private String startTime;
	private String endTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}


	public String getConfigureCode() {
		return configureCode;
	}

	public void setConfigureCode(String configureCode) {
		this.configureCode = configureCode;
		if (null != configureCode && configureCode.contains("-")) {
			this.code = configureCode.split("-")[1];
			this.departmentCode = configureCode.split("-")[0];
		}
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getBelongCode() {
		return belongCode;
	}

	public void setBelongCode(String belongCode) {
		this.belongCode = belongCode;
	}


	public String getStartTime() {
		if (isNotBlank(startTime)) {
			return startTime.replaceAll("[:：]", "");
		}
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		if (isNotBlank(endTime)) {
			return endTime.replaceAll("[:：]", "");
		}
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public void validData() {

		if (!configureCode.matches(REGEX_CONFIGURE_CODE)) {
			appendErrorMsg("配班代码格式不正确!正确格式为 :'755W-00008' 网点后面需5位数字 ！");
		}

		if (!startTime.matches(REGEX_HOUR_MINUTE)) {
			appendErrorMsg("出车时间格式有误!");
		}

		if (!endTime.matches(REGEX_HOUR_MINUTE)) {
			appendErrorMsg("收车时间格式有误!");
		}
		
		if (endTime.equals(endDate)) {
			appendErrorMsg("收车时间不能为00:00 !");
		}
		
		if (endTime.equals(endMaxDate)) {
			endTime = "23:59";
		}
		if (startTime.equals(startMaxDate)) {
			startTime = "23:59";
		}
		
		if(startTime.equals(endTime)){
			appendErrorMsg("出车时间跟收车时间不能相同！");
		}

		if (DepartmentCacheBiz.getDepartmentByCode(departmentCode) == null) {
			appendErrorMsg("班次代码网点有问题!");
		}

	}

	public boolean startTimeAfterEndTime(String startTime, String endTime) {
		return parseDate(startTime).after(parseDate(endTime)) ? true : false;
	}

	private Date parseDate(String time) {
		try {
			return DateUtil.parseDate(DEFAULT_DAY + time, DateFormatType.yyyyMMddHHmm);
		} catch (ParseException e) {
			throw new BizException("parse date error!");
		}
	}
	
	public LineConfigure bulidLineCofigure(String creator){
		LineConfigure lineConfigure = new LineConfigure();
		lineConfigure.setCode(code);
		lineConfigure.setDepartmentCode(departmentCode);
		lineConfigure.setYearMonth(month);
		lineConfigure.setType(0L);
		lineConfigure.setCreator(creator);
		lineConfigure.setCreateTime(new Date());
		lineConfigure.setValidStatus(1L);
		return lineConfigure;
	}
	
	public DriveLine buildDriverLine(String creator){
		DriveLine driverLine = new DriveLine();
		driverLine.setDepartmentCode(departmentCode);
		driverLine.setStartTime(getStartTime());
		driverLine.setEndTime(getEndTime());
		driverLine.setCreatedTime(new Date());
		driverLine.setCreator(creator);
		driverLine.setMobileNetwork("0");
		return driverLine;
	}
	
	
}
