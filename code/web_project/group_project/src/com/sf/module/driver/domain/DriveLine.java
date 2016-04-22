package com.sf.module.driver.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.BEAMS;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.sf.framework.base.domain.BaseEntity;

public class DriveLine extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String sourceCode;
	private String departmentCode;
	private String startTime;
	private String destinationCode;
	private String endTime;
	private String modifier;
	private Date modifiedTime;
	private Date createdTime;
	private String creator;
	private Long validStatus = 1L;
	private Long inputType;
	private String vehicleNumber;
	private String vehicleType;
	private String optimizeLineCode;
	private String areaCode;
	private String mobileNetwork;
	private String jointSchedulingDepartment;
	private int sort;

	public DriveLine(String creatorCode) {
		setCreator(creatorCode);
		setModifier(creatorCode);
		setCreatedTime(new Date());
		setModifiedTime(new Date());
	}
	
	public DriveLine(String departmentCode, String startTime, String endTime, String sourceCode, String destinationCode, int sort) {
		this.departmentCode = departmentCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sourceCode = sourceCode;
		this.destinationCode = destinationCode;
		this.jointSchedulingDepartment = sourceCode + BEAMS + destinationCode;
		this.sort = sort;
	}

	public DriveLine() {

	}

	public String getMobileNetwork() {
		return mobileNetwork;
	}

	public void setMobileNetwork(String mobileNetwork) {
		this.mobileNetwork = mobileNetwork;
	}

	public Long getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(Long validStatus) {
		this.validStatus = validStatus;
	}

	public Long getInputType() {
		return inputType;
	}

	public void setInputType(Long inputType) {
		this.inputType = inputType;
	}

	public String getOptimizeLineCode() {
		return optimizeLineCode;
	}

	public void setOptimizeLineCode(String optimizeLineCode) {
		this.optimizeLineCode = optimizeLineCode;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getJointSchedulingDepartment() {
		return jointSchedulingDepartment;
	}

	public void setJointSchedulingDepartment(String jointSchedulingDepartment) {
		this.jointSchedulingDepartment = jointSchedulingDepartment;
	}

	public static enum InputType {
		LABEL_INPUT_BY_HAND("手工录入"), LABEL_INPUT_BY_OPTIMIZE("路径优化");

		public final String inputType;

		InputType(String inputType) {
			this.inputType = inputType;
		}

		public static String getInputTypeDescription(int inputTypeValue) {
			for (InputType temporary : InputType.values()) {
				if (temporary.ordinal() == inputTypeValue) {
					return temporary.inputType;
				}
			}
			return "";
		}
	}

	public static List<LineImporter> templateForLine = newArrayList();
	public static List<LineImporter> templateForErrorLine = newArrayList();

	static {
		Collections.addAll(templateForLine, LineImporter.values());
		templateForLine.remove(templateForLine.size() - 1);

		Collections.addAll(templateForErrorLine, LineImporter.values());
	}

	public static enum LineImporter {
		INPUT_TYPE("数据源") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setInputType(cellValue.trim().equals("手工录入") ? 0L : 1L);
			}
		},
		OPTIMIZE_CODE("路径优化线路编号") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setOptimizeLineCode(cellValue);
			}
		},
		VEHICLE_TYPE("车型") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setVehicleType(cellValue);
			}
		},
		START_TIME("出车时间") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setStartTime(cellValue);
			}
		},
		END_TIME("收车时间") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setEndTime(cellValue);
			}
		},
		ORIGINAL_CODE("始发网点(代码)") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setSourceCode(cellValue);
			}
		},
		DESTINATION_CODE("目的网点(代码)") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setDestinationCode(cellValue);
			}
		},
		BELONG_CODE("归属网点") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setDepartmentCode(cellValue);
			}
		},
		VEHICLE_NUMBER("指定车牌号") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
				driveLine.setVehicleNumber(cellValue);
			}
		},
		ERROR_INFORMATION("错误数据") {
			@Override
			public void setValue(DriveLine driveLine, String cellValue) {
			}
		};

		public final String title;

		LineImporter(String title) {
			this.title = title;
		}

		public abstract void setValue(DriveLine driveLine, String cellValue);
	}
}
