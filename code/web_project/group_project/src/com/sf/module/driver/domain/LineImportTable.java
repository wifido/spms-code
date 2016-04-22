package com.sf.module.driver.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.util.StringUtil.removeColon;
import static com.sf.module.driver.domain.DriveLine.InputType.*;
import static com.sf.module.frameworkimpl.biz.DepartmentCacheBiz.getDepartmentByCode;
import static com.sf.module.operation.util.CommonUtil.isEmpty;
import java.util.List;
import com.google.common.base.Joiner;
import com.sf.module.common.util.StringUtil;
import com.sf.module.organization.domain.Department;

public class LineImportTable {
	private String inputType;
	private String vehicleType;
	private String startTime;
	private String sourceCode;
	private String departmentCode;
	private String destinationCode;
	private String vehicleNumber;
	private LineTableModel lineTableModel;
	private String endTime;
	private List<String> error = newArrayList();
	private String userCode;

	private Long lineId;
	
	private static final String TIME_2400="24:00";
	private static final String TIME_2359="23:59";
	private static final String TIME_0000="00:00";

	public LineImportTable(String userCode) {
		this.userCode = userCode;
		lineTableModel = new LineTableModel();
	}

	public void setValue(String cellValue, int column) {
		lineTableModel.setValue(this, column, cellValue);
	}

	public String getValue(int column) {
		return this.lineTableModel.getValue(this, column);
	}

	public DriveLine transform() {
		DriveLine driveLine = new DriveLine(userCode);

		driveLine.setVehicleNumber(vehicleNumber);
		driveLine.setVehicleType(vehicleType);

		driveLine.setInputType(LABEL_INPUT_BY_HAND.inputType.equals(inputType) ? 0L : 1L);
		driveLine.setStartTime(removeColon(startTime));
		driveLine.setEndTime(removeColon(endTime));
		driveLine.setVehicleNumber(vehicleNumber);
		driveLine.setVehicleType(vehicleType);
		driveLine.setDepartmentCode(departmentCode);
		driveLine.setSourceCode(sourceCode);
		driveLine.setDestinationCode(destinationCode);

		return driveLine;
	}

	private String getErrorCompressed() {
		return Joiner.on(",").join(error);
	}

	public void injectErrorInformation(int sourceIndex, int targetIndex) {
		error.add(formatOverlapInformation(sourceIndex + 2, targetIndex + 2));
	}

	private static class LineTableModel {
		public void setValue(LineImportTable lineImportTable, int column, String value) {
			if (column == 0) {
				lineImportTable.setInputType(value);
			} else if (column == 1) {
				lineImportTable.setDepartmentCode(value);
			} else if (column == 2) {
				lineImportTable.setStartTime(value);
			} else if (column == 3) {
				lineImportTable.setEndTime(value);
			} else if (column == 4) {
				lineImportTable.setSourceCode(value);
			} else if (column == 5) {
				lineImportTable.setDestinationCode(value);
			} else if (column == 6) {
				lineImportTable.setVehicleNumber(value);
			} else if (column == 7) {
				lineImportTable.setVehicleType(value);
			}
		}

		public String getValue(LineImportTable lineImportTable, int column) {
			String value = "";

			if (column == 0) {
				value = lineImportTable.getInputType();
			} else if (column == 1) {
				value = lineImportTable.getDepartmentCode();
			} else if (column == 2) {
				value = lineImportTable.getStartTime();
			} else if (column == 3) {
				value = lineImportTable.getEndTime();
			} else if (column == 4) {
				value = lineImportTable.getSourceCode();
			} else if (column == 5) {
				value = lineImportTable.getDestinationCode();
			} else if (column == 6) {
				value = lineImportTable.getVehicleNumber();
			} else if (column == 7) {
				value = lineImportTable.getVehicleType();
			} else if (column == 8) {
				value = lineImportTable.getErrorCompressed();
			}

			return value;
		}
	}

	private boolean isCorrectTime(String timePattern, String time) {
		return time.matches(timePattern);
	}

	public List<String> validationBasedData() {
		String timePattern = "^(((0\\d{1}|1\\d{1}|2[0-3])[：:]([0-5]\\d{1}))|(24[：:]00))$";

		if (!DriveLine.InputType.LABEL_INPUT_BY_HAND.inputType.equals(inputType) && !DriveLine.InputType.LABEL_INPUT_BY_OPTIMIZE.inputType.equals(inputType)) {
			error.add("数据源不能为空，且只能为手工录入或路径优化");
		}
		if (!isCorrectTime(timePattern, startTime)) {
			error.add("出车时间格式错误");
		}

		if (!isCorrectTime(timePattern, endTime)) {
			error.add("收车时间格式错误");
		}
		
		if(startTime.trim().equals(TIME_2400)){
			startTime=TIME_2359;
		}
		
		if(endTime.trim().equals(TIME_2400)){
			endTime=TIME_2359;
		}
		
		if(endTime.trim().equals(TIME_0000)){
			error.add("收车时间不能是00:00");
		}
		
		if (startTime.trim().equals(endTime.trim())) {
			error.add("出车时间不能等于收车时间");
		}

		Department department = getDepartmentByCode(departmentCode);
		if (department == null) {
			error.add("归属网点不合法");
		} else if (isAreaCode(department)) {
			error.add("归属网点只能是区部以下网点代码");
		}

		if (getDepartmentByCode(sourceCode) == null) {
			error.add("始发网点不合法");
		}

		if (getDepartmentByCode(destinationCode) == null) {
			error.add("目的网点不合法");
		}

		if (StringUtil.isBlank(vehicleNumber)) {
			error.add("车牌号必须填写，不能为空");
		}

		return error;
	}

	private boolean isAreaCode(Department department) {
		return isEmpty(department.getAreaDeptCode()) || department.getAreaDeptCode().equals(department.getDeptCode());
	}

	private String formatOverlapInformation(int sourceRowNumber, int targetRowNumber) {
		return String.format("第 %d 行和第 %d 行,时间出现重复", targetRowNumber, sourceRowNumber);
	}

	public List<String> getError() {
		return error;
	}

	public void setError(List<String> error) {
		this.error = error;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInputType() {
		return inputType;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

}
