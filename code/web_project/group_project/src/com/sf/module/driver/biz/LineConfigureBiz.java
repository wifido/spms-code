package com.sf.module.driver.biz;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static java.lang.Long.parseLong;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.driver.dao.LineConfigureDao;
import com.sf.module.driver.dao.LineConfigureExportHandler;
import com.sf.module.driver.dao.LineConfigureRepository;
import com.sf.module.driver.dao.LineManageDao;
import com.sf.module.driver.domain.DriveLine;
import com.sf.module.driver.domain.DriverLineConfigureRelation;
import com.sf.module.driver.domain.LineConfigure;

@Component
public class LineConfigureBiz extends BaseBiz {
	private static final String CONFIGURE_CODE = "CONFIGURE_CODE";
	private static final String YEAR_MONTH = "YEAR_MONTH";
	private static final String CONFIGURE_LINES = "CONFIGURE_LINES";
	private static final String LINE_CONFIGURE_ID = "LINE_CONFIGURE_ID";
	private static final String ENTITY_VALID_STATE = "ENTITY_VALID_STATE";
	private static final String KEY_UPDATE_IDS = "updateIds";
	private static final String KEY_VALID_STATE = "validState";
	private static final String KEY_ID = "id";
	@Resource
	private LineConfigureDao lineConfigureDao;
	@Resource
	private LineManageDao lineManageDao;
	private static final String DOWNLOAD_PATH = "downloadPath";
	private static final String IMPORT_RESULT = "importResult";

	public LineManageDao getLineManageDao() {
		return lineManageDao;
	}

	public LineConfigureDao getLineConfigureDao() {
		return lineConfigureDao;
	}

	public HashMap<String, Object> queryLineConfigures(Map<String, String> params) {
		int totalSize = lineConfigureDao.countLineConfigure(params);
		List<?> result = lineConfigureDao.queryLineConfigure(params);
		return constructResultForResponse(totalSize, result);
	}

	private HashMap<String, Object> constructResultForResponse(int totalSize, List<?> result) {
		HashMap<String, Object> resultMap = newHashMap();
		resultMap.put(ROOT, result);
		resultMap.put(TOTAL_SIZE, totalSize);
		return resultMap;
	}

	public HashMap<String, Object> exportLineConfigure(Map<String, String> params) {
		List<Map<String, Object>> list = lineConfigureDao.queryLineConfigureForExport(params);
		for (Map<String, Object> lineConfigure : list) {
			if(lineConfigure.get("TYPE").toString().equals("0")){
				String configureCode = "机动" + (String) lineConfigure.get(CONFIGURE_CODE);
				lineConfigure.remove(CONFIGURE_CODE);
				lineConfigure.put(CONFIGURE_CODE, configureCode);
			}
		}
		LineConfigureExportHandler handler = new LineConfigureExportHandler();
		try {
			handler.handle(list);
			HashMap<String, Object> result = newHashMap();
			result.put(DOWNLOAD_PATH, handler.getDownloadPath());
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException("exception:" + e);
		}
	}
	
	public HashMap<String, Object> importDynamicLineConfigure(File uploadFile,Map<String, String> params) {
		DynamicLineConfigureImportHandler handler = new DynamicLineConfigureImportHandler(uploadFile, this, getCurrentUser());
		try {
			handler.handleRestTime();
			HashMap<String, Object> result = newHashMap();
			result.put(IMPORT_RESULT, handler.result);
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException(e.getMessage());
		}
	}

	public String queryClassesCode(String departmentCode, String yearMonth) {
		int classesCode = Integer.parseInt(lineConfigureDao.queryClassesCode(departmentCode, yearMonth)) + 1;
		departmentCode += "-" + StringUtil.leftPadding("0", String.valueOf(classesCode), 5);

		return departmentCode;
	}
	
	public int validClassesCode(String departmentCode, String yearMonth, String code) {
		return lineConfigureDao.validClassesCode(departmentCode, yearMonth, code);
	}

	public HashMap<String, Object> queryConfigureSchedulingAllLine(HashMap<String, String> queryParameter) {
		HashMap<String, Object> resultMap = newHashMap();
		resultMap.put(ROOT, lineConfigureDao.queryConfigureSchedulingAllLine(queryParameter));
		return resultMap;
	}

	@Transactional
	public void addConfigureClasses(HashMap<String, String> parameter) {
		// 获取线路ID集合
		String[] lineIds = parameter.get(CONFIGURE_LINES).split(",");
		
		//查询id对应的所有线路;
		List<DriveLine> driveLines = new ArrayList<DriveLine>();
		
		for (int i = 0; i < lineIds.length; i++) {
			DriveLine line = lineManageDao.findById(Long.parseLong(lineIds[i]));
			driveLines.add(line);
		}
		
		if (!LineConfigureValidator.sharedInstance().isLegal(driveLines)) {
			throw new BizException("线路时间间隔不能大于24小时！");
		}
		
		Long configureId = lineConfigureDao.save(buildConfigureInformation(parameter, driveLines));
		
		for (int i = 0; i < lineIds.length; i++) {
			lineConfigureDao.saveLineConfigureRelation(getDriverLineConfigureRelation(configureId, lineIds[i], i));
		}
	}
	
	private boolean afterCurrentMonth(String month) {
		Date monthAsDate;
		try {
			monthAsDate = DateUtil.parseDate(month, DateFormatType.yyyy_MM);
		} catch (ParseException e) {
			throw new BizException("解析日期错误！");
		}
		// 当前月最后一天 23:59
		DateTime lastDayOfCurrentMonth = new DateTime(new Date()).withDayOfMonth(1).plusMonths(1).minusDays(1).withHourOfDay(23).withMinuteOfHour(59);
		return monthAsDate.after(lastDayOfCurrentMonth.toDate());
	}

	private DriverLineConfigureRelation getDriverLineConfigureRelation(Long configureId, String lineId, int i) {
		DriverLineConfigureRelation configureRelation = new DriverLineConfigureRelation();
		configureRelation.setConfigureId(configureId);
		configureRelation.setLineId(Long.parseLong(lineId));
		configureRelation.setOrder(i + 1);
		return configureRelation;
	}

	private LineConfigure buildConfigureInformation(HashMap<String, String> parameter, List<DriveLine> driveLines) {
		LineConfigure lineConfigure = new LineConfigure();
		lineConfigure.setCreator(getCurrentUser().getUsername());
		lineConfigure.setCreateTime(new Date());
		lineConfigure.setModifier(getCurrentUser().getUsername());
		lineConfigure.setModifiedTime(new Date());
		lineConfigure.setValidStatus(Long.valueOf(parameter.get(ENTITY_VALID_STATE)));
		lineConfigure.setCode(parameter.get("ENTITY_ARRANGE_CODE").split("-")[1]);
		lineConfigure.setDepartmentCode(parameter.get("ENTITY_DEPARTMENT_CODE"));
		lineConfigure.setType(Long.parseLong(parameter.get("CONFIGURE_TYPE")));
		lineConfigure.setYearMonth(parameter.get(YEAR_MONTH));

		lineConfigure.setLineConfigureInformation(driveLines);
		
		return lineConfigure;
	}
	
	@Transactional
	public void saveLineconfigure(LineConfigure lineConfigure, List<DriverLineConfigureRelation> lineConfigureRelations) {
		Long configureId = lineConfigureDao.save(lineConfigure);
		for (DriverLineConfigureRelation relation : lineConfigureRelations) {
			relation.setConfigureId(configureId);
			lineConfigureDao.saveLineConfigureRelation(relation);
		}
	}

	public HashMap<String, Object> importLineConfigure(File uploadFile, Map<String, String> params) {
		LineConfigureImportHandler handler = new LineConfigureImportHandler(uploadFile, this, getCurrentUser());
		try {
			handler.handle();
			HashMap<String, Object> result = newHashMap();
			result.put(IMPORT_RESULT, handler.result);
			return result;
		} catch (Exception e) {
			log.debug("export error!" + e);
			throw new BizException(e.getMessage());
		}
	}

	@Transactional
	public void batchUpdateValidState(HashMap<String, String> queryParameter) {
		String[] updateIds = queryParameter.get(KEY_UPDATE_IDS).split(",");

		List<LineConfigure> updateLineConfigureList = new ArrayList<LineConfigure>();
		for (int i = 0; i < updateIds.length; i++) {
			DetachedCriteria dc = DetachedCriteria.forClass(LineConfigure.class);
			dc.add(Restrictions.eq(KEY_ID, Long.valueOf(updateIds[i])));

			LineConfigure lineConfigure = (LineConfigure) lineConfigureDao.findBy(dc).get(0);
			lineConfigure.setValidStatus(Long.valueOf(queryParameter.get(KEY_VALID_STATE)));
			updateLineConfigureList.add(lineConfigure);
		}

		lineConfigureDao.saveOrUpdateBatch(updateLineConfigureList);
	}

	@Transactional
	public void updateLineConfigure(HashMap<String, String> queryParameter) {
		String lineIds = queryParameter.get(CONFIGURE_LINES);
		List<String> list = newArrayList(lineIds.split(","));
		
		long lineConfigureId = Long.parseLong(queryParameter.get(LINE_CONFIGURE_ID));
		long validStatus = Long.parseLong(queryParameter.get(ENTITY_VALID_STATE));

		LineConfigure queriedLineConfigure = new LineConfigure();
		queriedLineConfigure.setId(lineConfigureId);
		LineConfigure lineConfigure = (LineConfigure) lineConfigureDao.load(lineConfigureId);

		lineConfigure.setModifiedTime(new Date());
		lineConfigure.setModifier(getCurrentUser().getUsername());
		lineConfigure.setValidStatus(validStatus);
		
		// 设置线路配班信息，计算出勤时间、驾驶时间
		lineConfigure.setLineConfigureInformation(getLineByIds(list));
		
		lineConfigureDao.update(lineConfigure);

		lineConfigureDao.deleteLineConfigureRelation(lineConfigureId);
		

		for (int i = 0; i < list.size(); i++) {
			String lineID = list.get(i);
			DriverLineConfigureRelation relation = new DriverLineConfigureRelation();
			relation.setLineId(Long.parseLong(lineID));
			relation.setConfigureId(lineConfigureId);
			relation.setOrder(i + 1);
			lineConfigureDao.saveLineConfigureRelation(relation);
		}
	}

	private List<DriveLine> getLineByIds(List<String> list) {
		List<DriveLine> driveLines = new ArrayList<DriveLine>();

		for (int i = 0; i < list.size(); i++) {
			DriveLine line = lineManageDao.findById(Long.parseLong(list.get(i)));
			driveLines.add(line);
		}
		
		if (!LineConfigureValidator.sharedInstance().isLegal(driveLines)) {
			throw new BizException("线路时间间隔不能大于24小时！");
		}
		return driveLines;
	}

	public void addMobileNetwork(HashMap<String, String> httpRequestParameter) {
		saveOrUpdateMobileNetwork(httpRequestParameter);
	}

	@Transactional
	public void saveOrUpdateMobileNetwork(HashMap<String, String> httpRequestParameter) {
		String departmentCode = httpRequestParameter.get("_departmentCode").split("/")[0];
		String startTime = httpRequestParameter.get("_startTime");
		String yearMonth = httpRequestParameter.get("_yearMonth");
		String classCode = httpRequestParameter.get("_classCode");
		String endTime = httpRequestParameter.get("_endTime");
		String validState = httpRequestParameter.get("validState");
		String id = httpRequestParameter.get("configureId");
		// 获取驾驶时长
		double driveDuration = LineConfigure.getTimeDifference(startTime, endTime);

		if (StringUtil.isNotBlank(id)) {
			lineManageDao.updateLine(startTime, endTime, getCurrentUser().getUsername(), id);

			lineConfigureDao.updateConfigureClass(id, validState, getCurrentUser().getUsername(), driveDuration);
			return;
		}

		insertMobileNetwork(departmentCode, startTime, endTime, classCode, validState, yearMonth, driveDuration);
	}

	public void insertMobileNetwork(String departmentCode, String startTime, String endTime, String classCode, String validState, String yearMonth, double driveDuration) {
		LineConfigure lineConfigure = buildLineConfigureObject(classCode, departmentCode, validState, yearMonth, driveDuration);
		
		Long id = lineManageDao.save(buildDriveLineObject(departmentCode, startTime, endTime));
		
		lineConfigureDao
				.saveLineConfigureRelation(buildDriverLineConfigureRelation(
						lineConfigureDao.save(lineConfigure), id));
	}

	public DriveLine buildDriveLineObject(String departmentCode, String startTime, String endTime) {
		DriveLine driverLine = new DriveLine();
		driverLine.setDepartmentCode(departmentCode);
		driverLine.setStartTime(startTime);
		driverLine.setEndTime(endTime);
		driverLine.setCreatedTime(new Date());
		driverLine.setCreator(this.getCurrentUser().getUsername());
		driverLine.setModifier(this.getCurrentUser().getUsername());
		driverLine.setModifiedTime(new Date());
		driverLine.setMobileNetwork("0");

		return driverLine;
	}

	public LineConfigure buildLineConfigureObject(String classCode, String departmentCode, String validState, String yearMonth, double driveDuration) {
		LineConfigure lineConfigure = new LineConfigure();
		lineConfigure.setCode(classCode.split("-")[1]);
		lineConfigure.setDepartmentCode(departmentCode);
		lineConfigure.setValidStatus(Long.valueOf(validState));
		lineConfigure.setType(0L);
		lineConfigure.setCreateTime(new Date());
		lineConfigure.setCreator(this.getCurrentUser().getUsername());
		lineConfigure.setModifiedTime(new Date());
		lineConfigure.setModifier(this.getCurrentUser().getUsername());
		lineConfigure.setYearMonth(yearMonth);
		// 设置出勤时长
		lineConfigure.setAttendanceDuration(driveDuration);
		// 设置驾驶时长
		lineConfigure.setDriveDuration(driveDuration);

		return lineConfigure;
	}

	public DriverLineConfigureRelation buildDriverLineConfigureRelation(Long configureId, Long lineId) {
		DriverLineConfigureRelation configureRelation = new DriverLineConfigureRelation();
		configureRelation.setConfigureId(configureId);
		configureRelation.setLineId(lineId);
		configureRelation.setOrder(1L);
		return configureRelation;
	}

	public void batchDelete(String deleteIds) {
		for (String id : deleteIds.split(",")) {
			if (lineConfigureDao.queryConfigureClassByConfigureId(id).isEmpty()
					|| nonMotorizedMatching(id))
				continue;
			else {
				List<HashMap> list = lineManageDao.queryLineByLineConfigureId(Integer.parseInt(id));
				if (list.isEmpty())
					continue;
				List<Long> lineIdList =  newArrayList();
				lineIdList.add(parseLong(list.get(0).get("ID").toString()));
				lineManageDao.removeByIds(lineIdList);
			}
		}
		
		lineConfigureDao.batchDeleteConfigureRelation(deleteIds.split(","));
		
		lineConfigureDao.batchDeleteConfigureClass(deleteIds.split(","));
		
	}

	private boolean nonMotorizedMatching(String id) {
		return !lineConfigureDao.queryConfigureClassByConfigureId(id).equals("0");
	}
	
	// 查看配班是否存在
	public boolean queryConfigureExist(String code, String departmentCode, String month) {
		return lineConfigureDao.queryConfigureExist(code, departmentCode, month);
	}
}
