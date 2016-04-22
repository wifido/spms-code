package com.sf.module.driver.domain;

import static com.sf.module.common.util.StringUtil.*;
import java.text.ParseException;
import java.util.Date;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.domain.ImportModel;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;

public class LineConfigureImportModel extends ImportModel {
	private static final String REGEX_CONFIGURE_CODE = "^\\w{1,10}-\\d{5}$";
	private static final String REGEX_HOUR_MINUTE = "^((1|0)[0-9]|2[0-3])[:：]([0-5][0-9])$";
	private static final String DEFAULT_DAY = "20141201";
	private String sequence;
	private String configureCode;
	private String code;
	private String departmentCode;
	private String month;
	private String belongCode;
	private String sourceCode;
	private String destinationCode;
	private String startTime;
	private String endTime;
	private String vehicleNumber;
	private String validFlag;
	private long lineId;

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

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

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
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

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
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

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
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

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public boolean isNeedValid() {
		if ("否".equals(validFlag)) {
			return false;
		}
		return true;
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

		if (DepartmentCacheBiz.getDepartmentByCode(departmentCode) == null) {
			appendErrorMsg("班次代码网点有问题!");
		}

		if (DepartmentCacheBiz.getDepartmentByCode(sourceCode) == null) {
			appendErrorMsg("始发网点有问题!");
		}

		if (DepartmentCacheBiz.getDepartmentByCode(destinationCode) == null) {
			appendErrorMsg("目的网点有问题!");
		}

		if (isBlank(vehicleNumber)) {
			appendErrorMsg("车牌号不能为空!");
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
}
