package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.REST_MARK;
import static com.sf.module.common.domain.EmployeeType.DRIVER_PERSONNEL;
import static com.sf.module.common.util.StringUtil.isBlank;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import static com.sf.module.common.util.TemplateHelper.getCellValueAsString;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.joda.time.DateTime;

import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.AbstractImportHandler;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.domain.DriverScheduling;
import com.sf.module.driver.domain.DriverSchedulingImportModel;
import com.sf.module.driver.domain.DriverSchedulingImportModel.Weeks;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.organization.domain.Department;

public class DriverSchedulingImportHandler extends AbstractImportHandler<DriverSchedulingImportModel> {
	private static Log logger = LogFactory
            .getLog(DriverSchedulingImportHandler.class);
	private static final String NAME_ERROR_DATA_DOWNLOAD = "司机排班导入错误数据";
	private String schedulingDateAsString;
	private Date schedulingDate;
	private String schedulingWeek;
	private DriverSchedulingBiz driverSchedulingBiz;

	public DriverSchedulingImportHandler(File uploadFile, DriverSchedulingBiz driverSchedulingBiz) {
		super(uploadFile);
		this.driverSchedulingBiz = driverSchedulingBiz;
	}

	public int hiddenRowIndex() {
		return 1;
	}

	public int startRowIndex() {
		return 3;
	}

	public int titleRowIndex() {
		return 2;
	}

	public String downloadName() {
		return NAME_ERROR_DATA_DOWNLOAD;
	}

	public int lastColumnIndex() {
		return 10;
	}

	public void handleCommonColumn(HSSFSheet sheet) {
		validTemplate();
		schedulingDateAsString = getCellValueAsString(sheet.getRow(0).getCell(1));
		if (isBlank(schedulingDateAsString)) {
			throw new BizException("请填写排班日期！填写需要排班的那周周一的日期  格式：'2015-02-02' !");
		}
		try {
			schedulingDate = DateUtil.parseDate(schedulingDateAsString, DateFormatType.yyyy_MM_dd);
			setSchdulingWeek();
		} catch (ParseException e1) {
			throw new BizException("排班日期不正确! 填写需要排班的那周周一的日期  格式：'2015-02-02' !");
		}

		// 当前周的 周日 23:59
		DateTime sundayOfCurrentWeek = new DateTime().plusWeeks(-2).withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59);
		if (schedulingDate.before(sundayOfCurrentWeek.toDate())) {
			throw new BizException("只能导入上周及之后的排班数据！");
		}
	}

	private void setSchdulingWeek() {
		DateTime importDate = new DateTime(schedulingDate);
		schedulingWeek = importDate.getYear() + "-" + (importDate.getWeekOfWeekyear() < 10 ? "0" + String.valueOf(importDate.getWeekOfWeekyear()) : String.valueOf(importDate.getWeekOfWeekyear()));
	}

	private void validTemplate() {
		String hiddenTitle = headerMap.get(lastColumnIndex());
		if (isBlank(hiddenTitle) || !"sunday".equals(hiddenTitle)) {
			throw new BizException("导入模板有误！请下载最新模板导入!");
		}
	}

	public void validData(List<DriverSchedulingImportModel> datas, HSSFSheet sheet) throws IllegalAccessException, InvocationTargetException,
	        NoSuchMethodException, ParseException {
		DateTime importDate = new DateTime(schedulingDate);

		for (DriverSchedulingImportModel model : datas) {
			model.setMonth(schedulingDateAsString);
			model.setConfigureCodeOfDays();
			validEmployeeExist(model);
			model.validImportDataWhileFailureSetErrorMsg();
			if (!model.isValidPass()) {
				this.isValidPass = false;
				continue;
			}
			validConfiguredCodeExist(model);
			if (!model.isValidPass()) {
				this.isValidPass = false;
				continue;
			}
			
			List<String> updateDays = newArrayList();
			String departmentCode = model.getDepartmentCode();
			String employeeCode = model.getEmployeeCode();
			
			for (Weeks weeks : Weeks.values()) {
				DateTime dateTime = new DateTime()
				        .withYear(importDate.getYear())
				        .withWeekOfWeekyear(importDate.getWeekOfWeekyear())
				        .withDayOfWeek(weeks.ordinal() + 1);
				
				updateDays.add(DateUtil.formatDate(dateTime.toDate(), DateFormatType.yyyyMMdd));
			}

			String errorMessage = driverSchedulingBiz.validSchedulingTimeReturnErrorMessage(updateDays, employeeCode, departmentCode);

			if (StringUtil.isNotBlank(errorMessage)) {
				model.appendErrorMsg(errorMessage);
				this.isValidPass = false;
			}
		}
	}

	private void validEmployeeExist(DriverSchedulingImportModel model) {
		String employeeCode = model.getEmployeeCode();
		if (isNotBlank(employeeCode)) {
			OutEmployee outEmployee = new OutEmployee();
			outEmployee.setEmpCode(employeeCode);
			outEmployee.setEmpPostType(DRIVER_PERSONNEL.getEmployeeType());
			List<OutEmployee> employees = driverSchedulingBiz.getEmployeeDao().findBy(outEmployee);
			if (employees.isEmpty()) {
				model.appendErrorMsg("驾驶员工号不存在或此员工不是驾驶员！");
				return;
			}
			OutEmployee employee = employees.get(0);
			Department department = DepartmentCacheBiz.getDepartmentByID(employee.getDeptId());
			model.setDepartmentCode(department.getDeptCode());
			model.setAreaCode(department.getAreaDeptCode());
		}
	}

	private void validConfiguredCodeExist(DriverSchedulingImportModel model) {
		for (Entry<String, Set<String>> configureCodes : model.configureCodeOfDays.entrySet()) {
			for (String configureCode : configureCodes.getValue()) {
				if (REST_MARK.equals(configureCode)) {
					continue;
				}
				if(!driverSchedulingBiz.getLineConfigureDao().queryConfigureVaild(configureCode.split("-")[1], configureCode.split("-")[0],configureCodes.getKey())){
					model.appendErrorMsg(configureCode + "班别代码无效！");
				}
				if (!driverSchedulingBiz.getLineConfigureDao().queryConfigureExist(configureCode.split("-")[1], configureCode.split("-")[0],configureCodes.getKey())) {
					model.appendErrorMsg(configureCode + "配班代码不存在！");
				}
			}
		}
	}


	public void saveToDb(List<DriverSchedulingImportModel> datas) {
		if (!this.isValidPass)
			return;
		Date now = new Date();
		DateTime importDate = new DateTime(schedulingDate);
		for (DriverSchedulingImportModel model : datas) {
			List<DriverScheduling> schedulings = newArrayList();
			String username = driverSchedulingBiz.getUser().getUsername();
			for (Weeks weeks : Weeks.values()) {
				String configureCode = weeks.getValue(model);
				DateTime dateTime = new DateTime()
				        .withYear(importDate.getYear())
				        .withWeekOfWeekyear(importDate.getWeekOfWeekyear())
				        .withDayOfWeek(weeks.ordinal() + 1);
				DriverScheduling scheduling = new DriverScheduling();
				scheduling.setConfigureCode(configureCode);
				scheduling.setDepartmentCode(model.getDepartmentCode());
				scheduling.setDayOfMonth(DateUtil.formatDate(dateTime.toDate(), DateFormatType.yyyyMMdd));
				String yearMonth = DateUtil.formatDate(dateTime.toDate(), DateFormatType.yyyy_MM);
				logger.info("导入排班，排班年月" + scheduling.getYearMonth());
				
				if (yearMonth.indexOf("-") == -1) {
					throw new BizException("年月格式错误！" + yearMonth);
				}
				scheduling.setYearMonth(yearMonth);
				scheduling.setCreatedTime(now);
				scheduling.setCreator(username);
				scheduling.setModifier(username);
				scheduling.setModifiedTime(now);
				scheduling.setEmployeeCode(model.getEmployeeCode());
				scheduling.setSchedulingType(1L);
				scheduling.setYearWeek(schedulingWeek);
				scheduling.setSyncState(0L);
				scheduling.setConfirmStatus(0L);
				schedulings.add(scheduling);
			}
			driverSchedulingBiz.handImportSuccess(schedulings);
			result.setSuccessCount(result.getSuccessCount() + 1);
		}
	}

}
