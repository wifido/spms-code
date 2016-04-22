package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.util.StringUtil.isBlank;
import static com.sf.module.common.util.TemplateHelper.getCellValueAsString;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.joda.time.DateTime;
import com.sf.framework.core.domain.IUser;
import com.sf.framework.core.exception.BizException;
import com.sf.module.common.util.AbstractImportHandler;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.DriverLineConfigureRelation;
import com.sf.module.driver.domain.DynamicLineConfigureImportModel;
import com.sf.module.driver.domain.LineConfigure;

public class DynamicLineConfigureImportHandler extends AbstractImportHandler<DynamicLineConfigureImportModel> {
	private final String DOWNLOAD_NAME = "机动配班导入错误数据";
	private String month;
	private Date monthAsDate;
	private LineConfigureBiz lineConfigureBiz;
	private IUser user;

	public DynamicLineConfigureImportHandler(File uploadFile, LineConfigureBiz lineConfigureBiz, IUser user) {
		super(uploadFile);
		this.lineConfigureBiz = lineConfigureBiz;
		this.user = user;
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
		return DOWNLOAD_NAME;
	}

	public int lastColumnIndex() {
		return 3;
	}

	public void handleCommonColumn(HSSFSheet sheet) {
		validTemplate();
		HSSFRow row = sheet.getRow(0);
		month = getCellValueAsString(row.getCell(1));
		if (isBlank(month)) {
			throw new BizException("导入月份为必填项！");
		}
		if (!month.matches("^2\\d{3}-\\d{2}$")) {
			throw new BizException("导入月份格式不正确! 正确格式如：2014-01！");
		}
		try {
			monthAsDate = DateUtil.parseDate(month, DateFormatType.yyyy_MM);
			if (beforeCurrentMonth()) {
				throw new BizException("不能导入当前月之前的配班数据！");
			}
		} catch (ParseException e) {
			throw new BizException("导入月份格式不正确!");
		}
	}

	private void validTemplate() {
		String hiddenTitle = headerMap.get(lastColumnIndex());
		if (isBlank(hiddenTitle) || !"endTime".equals(hiddenTitle)) {
			throw new BizException("导入模板有误！请下载最新模板导入!");
		}
	}

	private boolean beforeCurrentMonth() throws ParseException {
		// 上个月最后一天 23:59
		DateTime lastDayOfPrevMonth = new DateTime(new Date()).withDayOfMonth(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59);
		return monthAsDate.before(lastDayOfPrevMonth.toDate());
	}

	private boolean afterCurrentMonth() throws ParseException {
		// 当前月最后一天 23:59
		DateTime lastDayOfCurrentMonth = new DateTime(new Date()).withDayOfMonth(1).plusMonths(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59);
		return monthAsDate.after(lastDayOfCurrentMonth.toDate());
	}

	public void validData(List<DynamicLineConfigureImportModel> datas, HSSFSheet sheet) throws ParseException {
		for (DynamicLineConfigureImportModel lineConfigureModel : datas) {
			lineConfigureModel.setMonth(month);
			lineConfigureModel.validData();
			validLineConfigureCode(lineConfigureModel);
			isHaveRepeatLineConfigure(lineConfigureModel, datas);
			if(!lineConfigureModel.isValidPass()){
				this.isValidPass = false;
			}
		}
	}
	
	private void isHaveRepeatLineConfigure(DynamicLineConfigureImportModel model,List<DynamicLineConfigureImportModel> datas){
		int  i=0;
		for (DynamicLineConfigureImportModel lineConfigureModel : datas) {
			if(model.getConfigureCode()!=null&&model.getConfigureCode().equals(lineConfigureModel.getConfigureCode())){
				  i++;
			}
			if(i>1){
				lineConfigureModel.appendErrorMsg("配班代码存在重复多个情况！");
				return ;
			}
		}
	}

	private void validLineConfigureCode(DynamicLineConfigureImportModel lineConfigureModel) {
		boolean isExist = lineConfigureBiz.queryConfigureExist(lineConfigureModel.getCode(), lineConfigureModel.getDepartmentCode(), month);;
		if (isExist) {
			this.isValidPass = false;
			lineConfigureModel.appendErrorMsg("配班代码已经存在！");
		}
	}

	public void saveToDb(List<DynamicLineConfigureImportModel> datas) {
		if (!isValidPass)
			return;
		for (DynamicLineConfigureImportModel model : datas) {
			LineConfigure lineConfigure = model.bulidLineCofigure(user.getUsername());
			DriveLine line = model.buildDriverLine(user.getUsername());
			lineConfigureBiz.getLineManageDao().save(line);
			
			DriverLineConfigureRelation relation = new DriverLineConfigureRelation();
			relation.setLineId(line.getId());
			relation.setOrder(1);
			
			double driveDuration = LineConfigure.getTimeDifference(line.getStartTime(), line.getEndTime());
			
			lineConfigure.setAttendanceDuration(driveDuration);
			lineConfigure.setDriveDuration(driveDuration);
			
			lineConfigureBiz.saveLineconfigure(lineConfigure, newArrayList(relation));
			result.setSuccessCount(result.getSuccessCount() + 1);
		}

	}

}
