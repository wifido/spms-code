package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.sf.module.common.util.StringUtil.isBlank;
import static com.sf.module.common.util.TemplateHelper.getCellValueAsString;
import static com.sf.module.driver.biz.LineConfigureValidator.check24Hour;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
import com.sf.module.driver.domain.LineConfigure;
import com.sf.module.driver.domain.LineConfigureImportModel;

public class LineConfigureImportHandler extends AbstractImportHandler<LineConfigureImportModel> {
	private final String DOWNLOAD_NAME = "司机配班导入错误数据";
	private String month;
	private Date monthAsDate;
	private Map<String, List<LineConfigureImportModel>> lineConfigures;
	private LineConfigureBiz lineConfigureBiz;
	private IUser user;

	public LineConfigureImportHandler(File uploadFile, LineConfigureBiz lineConfigureBiz, IUser user) {
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
		return 8;
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
		if (isBlank(hiddenTitle) || !"validFlag".equals(hiddenTitle)) {
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

	public void validData(List<LineConfigureImportModel> datas, HSSFSheet sheet) throws ParseException {

		lineConfigures = getLineConfigures(datas);
		LineConfigureImportModel lineConfigureModel = null;
		List<LineConfigureImportModel> list = newArrayList();
		for (Map.Entry<String, List<LineConfigureImportModel>> entry : lineConfigures.entrySet()) {
			List<String> endTimeList =newArrayList();
			List<String> startTimeList =newArrayList();
			list = entry.getValue();
			for (int i = 0; i < list.size(); i++) {
				lineConfigureModel = list.get(i);
				String endDate =lineConfigureModel.getEndTime();
				endTimeList.add(endDate);
				String startDate = lineConfigureModel.getStartTime();
				startTimeList.add(startDate);
				lineConfigureModel.setMonth(month);
				validBasicInfo(lineConfigureModel);
				if (!lineConfigureModel.isValidPass()) {
					isValidPass = false;
					continue;
				}
				if (i == 0) {
					validLineConfigureCode(lineConfigureModel);
				}
				validLine(lineConfigureModel);
//				validLineIsConfigured(lineConfigureModel, datas);
//				validLineIsCross(lineConfigureModel, list);
				if (i != 0) {
					LineConfigureImportModel prevLineConfigureModel = list.get(i - 1);
					if (existConflictDepartmentBetweenTwoLines(prevLineConfigureModel, lineConfigureModel)) {
						isValidPass = false;
						lineConfigureModel.appendErrorMsg("此线路始发网点跟上一线路的目的网点不一致！");
					}
				}
			}
			if (check24Hour(endTimeList, startTimeList)) {
				isValidPass = false;
				lineConfigureModel.appendErrorMsg("此班次超过24小时！");
			}
		}

	}

	private boolean existConflictDepartmentBetweenTwoLines(LineConfigureImportModel prevModel, LineConfigureImportModel currentModel) {
		return !prevModel.getDestinationCode().equals(currentModel.getSourceCode());
	}

	private void validLineIsCross(LineConfigureImportModel lineConfigureModel, List<LineConfigureImportModel> list) {
		if (!lineConfigureModel.isNeedValid()) {
			return;
		}
		for (LineConfigureImportModel model : list) {
			if (model == lineConfigureModel) {
				continue;
			}
			if (!lineConfigureModel.getSourceCode().equals(model.getSourceCode())
			        || !lineConfigureModel.getDestinationCode().equals(model.getDestinationCode())
			        || !lineConfigureModel.getVehicleNumber().equals(model.getVehicleNumber())) {
				continue;
			}
			String startTime = lineConfigureModel.getEndTime();
			String endTime = model.getStartTime();
			if (lineConfigureModel.startTimeAfterEndTime(startTime, endTime)) {
				isValidPass = false;
				lineConfigureModel.appendErrorMsg("此线路配班时间有交叉！");
			}
		}
	}

	private void validLineIsConfigured(LineConfigureImportModel lineConfigureModel, List<LineConfigureImportModel> datas) throws ParseException {
		if (lineConfigureModel.getLineId() == 0)
			return;
		if (afterCurrentMonth()) {
			validLineIsConfiguredInExcel(lineConfigureModel, datas);
			//validLineIsConfiguredInDatabase(lineConfigureModel);
		}
	}

	private void validLineIsConfiguredInDatabase(LineConfigureImportModel lineConfigureModel) {
		List<?> lineConfigures = lineConfigureBiz.getLineConfigureDao().queryLineConfigureByLineIdAndMonth(month, lineConfigureModel.getLineId());
		if (!lineConfigures.isEmpty()) {
			this.isValidPass = false;
			lineConfigureModel.appendErrorMsg("此线路已经配过班了！");
		}
	}

	private void validLineIsConfiguredInExcel(LineConfigureImportModel lineConfigureModel, List<LineConfigureImportModel> datas) {
		for (LineConfigureImportModel model : datas) {
			if (lineConfigureModel.getRowIndex() == model.getRowIndex()) {
				continue;
			}
			if (lineConfigureModel.getLineId() == model.getLineId()) {
				this.isValidPass = false;
				lineConfigureModel.appendErrorMsg(String.format("此线路已经在第%s行配过班了！", model.getRowIndex() + 1));
				return;
			}
		}
	}

	private void validLineConfigureCode(LineConfigureImportModel lineConfigureModel) {
		// 查看配班是否存在
		boolean isExist = lineConfigureBiz.queryConfigureExist(lineConfigureModel.getCode(), lineConfigureModel.getDepartmentCode(), month);
		if (isExist) {
			this.isValidPass = false;
			lineConfigureModel.appendErrorMsg("配班代码已经存在！");
		}
	}

	private void validLine(LineConfigureImportModel lineConfigureModel) {
		DriveLine queryLine = buildDriverLine(lineConfigureModel);
		DriveLine driveLine = lineConfigureBiz.getLineManageDao().findLine(queryLine);
		if (driveLine == null) {
			lineConfigureModel.appendErrorMsg("此线路不存在！");
			this.isValidPass = false;
			return;
		}
		lineConfigureModel.setLineId(driveLine.getId());
	}

	private void validBasicInfo(LineConfigureImportModel lineConfigureModel) {
		lineConfigureModel.validData();
	}

	public void saveToDb(List<LineConfigureImportModel> datas) {
		if (!isValidPass)
			return;
		for (Map.Entry<String, List<LineConfigureImportModel>> entry : lineConfigures.entrySet()) {
			List<LineConfigureImportModel> lines = entry.getValue();
			LineConfigure lineConfigure = new LineConfigure();
			List<DriverLineConfigureRelation> relations = newArrayList();
			lineConfigure.setYearMonth(month);
			lineConfigure.setCreatedTime(new Date());
			lineConfigure.setCreateTime(new Date());
			lineConfigure.setCreator(user.getUsername());
			lineConfigure.setType(1L);
			lineConfigure.setValidStatus(1L);
			int order = 1;
			
			double driveDuration = 0;
			double attendanceDuration = 0;
			Date endDate = null;
			Date startDate = null;
			// 计算出勤时长、驾驶时长
			for (LineConfigureImportModel line : lines) {
				try {
					if (driveDuration == 0) {
						startDate = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2015-01-01 " + line.getStartTime());
					}
					driveDuration += LineConfigure.getTimeDifference(line.getStartTime(), line.getEndTime());
					endDate = LineConfigure.getEndTime(line.getStartTime(), line.getEndTime(), endDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				DriverLineConfigureRelation relation = new DriverLineConfigureRelation();
				lineConfigure.setCode(line.getCode());
				lineConfigure.setDepartmentCode(line.getDepartmentCode());
				relation.setLineId(line.getLineId());
				relation.setOrder(order);
				order++;
				relations.add(relation);
			}
			
			lineConfigure.setDriveDuration(driveDuration);
			attendanceDuration = ((endDate.getTime() - startDate.getTime()) / 1000) / 60 / 60.0;
			lineConfigure.setAttendanceDuration(attendanceDuration);
			
			lineConfigureBiz.saveLineconfigure(lineConfigure, relations);
			result.setSuccessCount(result.getSuccessCount() + 1);
		}

	}

	private DriveLine buildDriverLine(LineConfigureImportModel line) {
		DriveLine driveLine = new DriveLine();
		driveLine.setStartTime(line.getStartTime());
		driveLine.setEndTime(line.getEndTime());
		driveLine.setDepartmentCode(line.getDepartmentCode());
		driveLine.setSourceCode(line.getSourceCode());
		driveLine.setDestinationCode(line.getDestinationCode());
		driveLine.setVehicleNumber(line.getVehicleNumber());
		return driveLine;
	}

	private Map<String, List<LineConfigureImportModel>> getLineConfigures(List<LineConfigureImportModel> lines) {
		Map<String, List<LineConfigureImportModel>> lineConfigures = newLinkedHashMap();
		for (LineConfigureImportModel line : lines) {
			String lineConfigureCode = line.getConfigureCode();
			if (lineConfigures.containsKey(lineConfigureCode)) {
				lineConfigures.get(lineConfigureCode).add(line);
				continue;
			}
			List<LineConfigureImportModel> list = newArrayList();
			list.add(line);
			lineConfigures.put(lineConfigureCode, list);
		}
		return lineConfigures;
	}

}
