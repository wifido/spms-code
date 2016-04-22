package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.*;
import static com.sf.module.common.util.Clock.isOverlap;
import static com.sf.module.common.util.StringUtil.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.Clock;
import com.sf.module.driver.dao.DriverVehicleDao;
import com.sf.module.driver.dao.LineManageDao;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.LineImportTable;
import com.sf.module.driver.domain.Vehicle;
import com.sf.module.driver.util.ErrorLineTemplate;
import com.sf.module.driver.util.LineImporter;
import com.sf.module.driver.util.LineTemplate;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;

@Component
public class LineManageBiz extends BaseBiz {
	private static final String KEY_START_TIME = "START_TIME";
	private static final String KEY_END_TIME = "END_TIME";
	private static final String KEY_DESTINATION_CODE = "DESTINATION_CODE";
	private static final String KEY_SOURCE_CODE = "SOURCE_CODE";
	private static final String KEY_BELONG_ZONE_CODE = "BELONG_ZONE_CODE";
	private static final String LINE_INFORMATION_EXPORT = "线路信息导出";
	private static final String KEY_VEHICLE_NUMBER = "VEHICLE_NUMBER";
	@Resource
	private LineManageDao lineManageDao;
	@Resource
	private DriverVehicleDao driverVehicleDao;

	public HashMap<String, Object> queryLinesBySpecifyParameter(
	        String departmentId,
	        String inputType,
	        String vehicleNumber,
	        String validStatus,
	        String startTime,
	        String configureStatus,
	        int start,
	        int limit) {

		int totalSize = countLineBySpecifyParameter(departmentId, inputType, vehicleNumber, validStatus, startTime, configureStatus);

		List<?> result = lineManageDao.queryAll(
		        departmentId,
		        matchedInputTypeByRequest(inputType),
		        vehicleNumber,
		        validStatus,
		        start,
		        limit,
		        startTime,
		        configureStatus);

		return constructResultForResponse(totalSize, result);
	}

	public boolean updateLine(Long id, String departmentCode, String destinationCode, String sourceCode, Long updateValid, String vehicleNumber) {
		DriveLine driveLine = lineManageDao.findById(id);
		if (null == driveLine)
			return false;
		if (validDepartmentCode(destinationCode, departmentCode, sourceCode)) {
			driveLine.setDepartmentCode(departmentCode);
			driveLine.setDestinationCode(destinationCode);
			driveLine.setSourceCode(sourceCode);
			driveLine.setValidStatus(updateValid);

			if (!vehicleNumber.equals(driveLine.getVehicleNumber())) {
				Vehicle vehicle = driverVehicleDao.queryVehicleByVehicleCode(vehicleNumber);
				if (isBlank(vehicle.getVehicleCode())) {
					throw new BizException("车牌号不存在!");
				}
				driveLine.setVehicleNumber(vehicleNumber);
				driveLine.setVehicleType(vehicle.getBrandModel() + vehicle.getWheelbase());
			}

			if (lineManageDao.validationOverlapData(buildValidObject(driveLine))) {
				throw new BizException("新增线路与数据库中数据出现重叠数据!");
			}

			driveLine.setModifier(getCurrentUser().getUsername());
			driveLine.setModifiedTime(new Date());
			lineManageDao.update(driveLine);
		}
		return true;
	}

	public boolean deleteLine(String lineIdString) {
		List<Long> lineIdList = newArrayList();
		String lineIds[] = lineIdString.split(",");
		for (String lineId : lineIds) {
			lineIdList.add(Long.parseLong(lineId));
		}
		lineManageDao.removeByIds(lineIdList);
		return true;
	}

	public boolean batchUpdteValidStatus(String lineIdString, String validStatus) {
		String lineIds[] = lineIdString.split(",");
		List<DriveLine> batchUpdateDriveLineList = newArrayList();
		for (String lineId : lineIds) {
			DriveLine driveLine = lineManageDao.findById(Long.parseLong(lineId));
			if (null == driveLine)
				return false;
			driveLine.setValidStatus(Long.parseLong(validStatus));
			batchUpdateDriveLineList.add(driveLine);
		}
		lineManageDao.updateBatch(batchUpdateDriveLineList);
		return true;
	}

	private boolean validDepartmentCode(String destinationCode, String departmentCode, String sourceCode) {
		if (null == DepartmentCacheBiz.getDepartmentByCode(departmentCode))
			return false;
		if (null == DepartmentCacheBiz.getDepartmentByCode(destinationCode))
			return false;
		if (null == DepartmentCacheBiz.getDepartmentByCode(sourceCode))
			return false;
		return true;
	}

	private HashMap<String, Object> constructResultForResponse(int totalSize, List<?> result) {
		HashMap<String, Object> resultMap = newHashMap();
		resultMap.put(ROOT, result);
		resultMap.put(TOTAL_SIZE, totalSize);
		return resultMap;
	}

	private int countLineBySpecifyParameter(
	        String departmentId,
	        String dataSourceType,
	        String vehicleNumber,
	        String validStatus,
	        String startTime,
	        String configureStatus) {
		InputType inputTypeForQuery = matchedInputTypeByRequest(dataSourceType);
		return lineManageDao.countLinesBySpecifyParameter(departmentId, inputTypeForQuery, vehicleNumber, validStatus, startTime, configureStatus);
	}

	private InputType matchedInputTypeByRequest(String dataSourceType) {
		int index = Integer.parseInt(dataSourceType);
		return InputType.values()[index];
	}

	public LineManageDao getLineManageDao() {
		return lineManageDao;
	}

	public String exportLine(String departmentId, String inputType, String vehicleNumber, String validStatus, 
								int start, int limit, String configureStatus) {
		List<Map<String, Object>> result = (List<Map<String, Object>>) lineManageDao.queryAll(
		        departmentId,
		        matchedInputTypeByRequest(inputType),
		        vehicleNumber,
		        validStatus,
		        start,
		        limit,
		        null,
				configureStatus);

		LineTemplate lineTemplate = new LineTemplate();
		lineTemplate.write(result, LINE_INFORMATION_EXPORT);

		return lineTemplate.getTargetFilePath();
	}

	@Transactional
	public HashMap<String, Object> importLine(File uploadFile) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			List<LineImportTable> lineImportTables = LineImporter.parseDriveLineFromFile(uploadFile, getCurrentUser().getUsername());

			// 判断是否至少存在一条错误在数据
			if (!allCorrect(lineImportTables)) {
				String targetFilePath = exportToFile(lineImportTables);
				resultMap.put(KEY_FILE_NAME, targetFilePath);
				resultMap.put(KEY_MESSAGE, String.format("导入失败 %d 条", lineImportTables.size()));
				return resultMap;
			}

			Iterable<DriveLine> transform = Iterables.transform(lineImportTables, new Function<LineImportTable, DriveLine>() {
				@Override
				public DriveLine apply(LineImportTable lineImportTable) {
					return lineImportTable.transform();
				}
			});

			lineManageDao.saveBatch(newArrayList(transform));

			resultMap.put(KEY_MESSAGE, String.format("导入成功 %d 条", lineImportTables.size()));
		} catch (FileNotFoundException e) {
			log.error(KEY_ERROR, e);
			resultMap.put(KEY_MESSAGE, "没有找到文件！");
		} catch (IOException e) {
			log.error(KEY_ERROR, e);
			resultMap.put(KEY_MESSAGE, "读取文件失败！");
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			resultMap.put(KEY_MESSAGE, "导入出错，请找相关人员处理。");
		}

		return resultMap;
	}

	private boolean allCorrect(List<LineImportTable> lineImportTables) {

		for (LineImportTable lineImportTable : lineImportTables) {
			lineImportTable.validationBasedData();
			valildVehicle(lineImportTable);
		}

		injectErrorWhenTimeOverlap(lineImportTables);

		Iterable<LineImportTable> incorrectResult = Iterables.filter(lineImportTables, new Predicate<LineImportTable>() {
			@Override
			public boolean apply(LineImportTable lineImportTable) {
				return !lineImportTable.getError().isEmpty();
			}
		});

		return newArrayList(incorrectResult).isEmpty();
	}

	private void valildVehicle(LineImportTable lineImportTable) {
		if (isBlank(lineImportTable.getVehicleNumber()))
			return;
		Vehicle vehicle = driverVehicleDao.queryVehicleByVehicleCode(lineImportTable.getVehicleNumber());
		if (isBlank(vehicle.getVehicleCode())) {
			lineImportTable.getError().add(String.format("车牌号:%s不存在", lineImportTable.getVehicleNumber()));
			return;
		}
		lineImportTable.setVehicleType(vehicle.getBrandModel() + vehicle.getWheelbase());
	}

	public void injectErrorWhenTimeOverlap(List<LineImportTable> lineImportTables) {
		for (LineImportTable targetTable : lineImportTables) {
			for (LineImportTable sourceTable : lineImportTables) {
				if (sourceTable == targetTable) {
					continue;
				}

				Clock.TimeRange sourceRange = constructTimeRange(sourceTable);
				Clock.TimeRange targetRange = constructTimeRange(targetTable);

				if (existSameVehicleNumber(targetTable, sourceTable)) {
					if (isOverlap(sourceRange, targetRange)) {
						targetTable.injectErrorInformation(lineImportTables.indexOf(sourceTable), lineImportTables.indexOf(targetTable));
						break;
					}
				}
			}

			if (lineManageDao.validationOverlapData(targetTable)) {
				targetTable.getError().add("当前行与数据库中数据出现重复数据");
			}
		}
	}

	public boolean existSameVehicleNumber(LineImportTable sourceImportTable, LineImportTable targetImportTable) {
		return sourceImportTable.getVehicleNumber().equals(targetImportTable.getVehicleNumber())
		        && sourceImportTable.getSourceCode().equals(targetImportTable.getSourceCode())
		        && sourceImportTable.getDestinationCode().equals(targetImportTable.getDestinationCode())
		        && sourceImportTable.getDepartmentCode().equals(targetImportTable.getDepartmentCode());
	}

	private Clock.TimeRange constructTimeRange(LineImportTable sourceTable) {
		return new Clock.TimeRange(convertTimeToInteger(sourceTable.getStartTime()), convertTimeToInteger(sourceTable.getEndTime()));
	}

	private int convertTimeToInteger(String time) {
		return Integer.parseInt(removeColon(time));
	}

	private String exportToFile(List<LineImportTable> incorrectLines) {
		ErrorLineTemplate errorLineTemplate = new ErrorLineTemplate();
		errorLineTemplate.writeAsObject(incorrectLines, "线路导入错误");
		return errorLineTemplate.getTargetFilePath();
	}

	public static enum InputType {
		ALL("0", "1"), HAND("0", "0"), OPTIMIZE("1", "1");

		public final String firstType;
		public final String secondType;

		InputType(String firstType, String secondType) {
			this.firstType = firstType;
			this.secondType = secondType;
		}
	}

	@Transactional
	public void addLine(HashMap<String, String> queryParameters) {
		DriveLine queryLine = new DriveLine();
		queryLine.setStartTime(queryParameters.get(KEY_START_TIME));
		queryLine.setEndTime(queryParameters.get(KEY_END_TIME));
		queryLine.setDestinationCode(queryParameters.get(KEY_DESTINATION_CODE));
		queryLine.setSourceCode(queryParameters.get(KEY_SOURCE_CODE));
		queryLine.setDepartmentCode(queryParameters.get(KEY_BELONG_ZONE_CODE));
		queryLine.setVehicleNumber(queryParameters.get(KEY_VEHICLE_NUMBER));
		Vehicle vehicle = driverVehicleDao.queryVehicleByVehicleCode(queryParameters.get(KEY_VEHICLE_NUMBER));
		if (isBlank(vehicle.getVehicleCode())) {
			throw new BizException("车牌号不存在!");
		}
		if (!lineManageDao.findBy(queryLine).isEmpty()) {
			throw new BizException("已存在相同的线路了!");
		}

		/*if (lineManageDao.validationOverlapData(buildValidObject(queryLine))) {
			throw new BizException("新增线路与数据库中数据出现重叠数据!");
		}*/

		DriveLine driveLine = buildDriveLine(queryParameters);
		driveLine.setVehicleType(vehicle.getBrandModel() + vehicle.getWheelbase());

		lineManageDao.save(driveLine);
	}

	private LineImportTable buildValidObject(DriveLine driveLine) {
		LineImportTable lineImportTable = new LineImportTable(getCurrentUser().getUsername());
		lineImportTable.setDepartmentCode(driveLine.getDepartmentCode());
		lineImportTable.setDestinationCode(driveLine.getDestinationCode());
		lineImportTable.setSourceCode(driveLine.getSourceCode());
		lineImportTable.setVehicleNumber(driveLine.getVehicleNumber());
		lineImportTable.setStartTime(driveLine.getStartTime());
		lineImportTable.setEndTime(driveLine.getEndTime());
		lineImportTable.setLineId(driveLine.getId());

		return lineImportTable;
	}

	private DriveLine buildDriveLine(HashMap<String, String> queryParameters) {
		DriveLine driveLine = new DriveLine(getCurrentUser().getUsername());
		driveLine.setStartTime(queryParameters.get(KEY_START_TIME));
		driveLine.setEndTime(queryParameters.get(KEY_END_TIME));
		driveLine.setDestinationCode(queryParameters.get(KEY_DESTINATION_CODE));
		driveLine.setSourceCode(queryParameters.get(KEY_SOURCE_CODE));
		driveLine.setDepartmentCode(queryParameters.get(KEY_BELONG_ZONE_CODE));
		driveLine.setVehicleNumber(queryParameters.get(KEY_VEHICLE_NUMBER));
		driveLine.setInputType(0L);
		return driveLine;
	}

	public HashMap<String, Object> queryLineByLineConfigureId(int lineConfigureId) {
		List list = lineManageDao.queryLineByLineConfigureId(lineConfigureId);
		return constructResultForResponse(list.size(), list);
	}

}