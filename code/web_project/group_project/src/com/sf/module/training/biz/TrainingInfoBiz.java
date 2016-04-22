package com.sf.module.training.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.google.common.base.Joiner;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.TemplateHelper;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.organization.domain.Department;
import com.sf.module.report.biz.SchedulingInputStatisticalExportHandler;
import com.sf.module.training.dao.TrainingInfoDao;
import com.sf.module.training.domain.Training;
import com.sf.module.warehouse.biz.WarehouseSchedulingBiz;

public class TrainingInfoBiz extends BaseBiz {
	private TrainingInfoDao trainingInfoDao;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	
	public TrainingInfoDao getTrainingInfoDao() {
		return trainingInfoDao;
	}

	public void setTrainingInfoDao(TrainingInfoDao trainingInfoDao) {
		this.trainingInfoDao = trainingInfoDao;
	}

	public HashMap<String, Object> query(HashMap<String, String> httpRequestParameter) {
		return trainingInfoDao.queryTrainingInfo(httpRequestParameter);
	}

	public boolean delete(HashMap<String, String> httpRequestParameter) {
		if (StringUtil.isBlank(httpRequestParameter.get("ids")))
			return false;
		String[] ids = httpRequestParameter.get("ids").toString().split(",");
		for (String id : ids) {
			if (!StringUtil.isBlank(id))
				trainingInfoDao.remove(Long.parseLong(id));
		}
		return true;
	}

	public HashMap<String, Object> importTraining(File uploadFile, HashMap<String, String> httpRequestParameter) {
		HSSFWorkbook workBook = getHSSFWorkbook(uploadFile);
		HSSFSheet sheet = workBook.getSheetAt(0);
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.putAll(httpRequestParameter);
		validMaxRowNum(sheet, dataMap);
		validMonth(sheet, dataMap);
		validDepartment(sheet, dataMap);
		createErrorCell(sheet.getRow(1), "失败原因");
		handEachRow(sheet, dataMap);
		if (dataMap.containsKey("sucess")) {
			TemplateHelper.removeEmptyRow(sheet);
			writeErrorMsgToExcle(dataMap, workBook);
		}
		return dataMap;
	}
	
	public String exporTraining(HashMap<String, String> httpRequestParameter) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		List list = trainingInfoDao.queryExportTraining(httpRequestParameter);
		if (list.size() == 0) {
			throw new BizException("该网点暂无培训信息！");
		}
		TrainingExportHandler trainingExportHandler = new TrainingExportHandler();
		trainingExportHandler.write(list, "培训信息导出");
	    return trainingExportHandler.getTargetFilePath();
	}

	private HSSFWorkbook getHSSFWorkbook(File uploadFile) {
		FileInputStream fin = null;
		HSSFWorkbook readWorkBook = null;
		try {
			fin = new FileInputStream(uploadFile);
			readWorkBook = new HSSFWorkbook(fin);
		} catch (Exception e1) {
			log.error("error", e1);
			throw new BizException("读文件出错！");
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (Exception e) {
					log.error("error", e);
				}
			}
		}
		return readWorkBook;
	}

	private void validMaxRowNum(HSSFSheet sheet, Map<String, Object> dataMap) {
		int rowCount = sheet.getLastRowNum();
		if (rowCount > 1000) {
			throw new BizException("一次最多只能导入 1000 条记录！");
		}
		dataMap.put("total_rows", rowCount);
	}

	private void validMonth(HSSFSheet sheet, Map<String, Object> dataMap) {
		HSSFRow row = sheet.getRow(0);
		if (null == row) {
			throw new BizException("导入的模板有问题！请检查！");
		}
		HSSFCell cell1 = row.getCell(4);
		if (null == cell1) {
			throw new BizException("导入的模板有问题！请检查！");
		}
		String month = getCellStrValue(cell1);
		
		if (StringUtil.isBlank(month)) {
			throw new BizException("年月不能为空！");
		}
		try {
			dateFormat.parse(month);
		} catch (ParseException e) {
			throw new BizException("年月格式不正确，正确格式 例如:2014-07！");
		}
		if (DateUtil.validConfirmDate(month)) {
			if (DateUtil.isBeOverdue(month))
				throw new BizException("已逾期，不能导入该月排班！");
		} else {
			throw new BizException("最多支持上个月、当前月以及下个月的排班导入！");
		}
		String year = month.split("-")[0];
		String m = month.split("-")[1];

		int days = DateUtil.getDaysOfMonth(Integer.parseInt(year), Integer.parseInt(m));
		dataMap.put("DAYS_OF_MONTH", days);
		dataMap.put("MONTH", month);
	}

	private String getCellStrValue(HSSFCell cellobj) {
		if (cellobj == null)
			return null;
		cellobj.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cellobj.toString().trim();
	}

	private void validDepartment(HSSFSheet sheet, Map<String, Object> dataMap) {
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.getCell(2);
		String deptCode = getCellStrValue(cell);
		String checkDeptCode = dataMap.get("upload_deptCode").toString();

		if (!checkDeptCode.equals(deptCode)) {
			throw new BizException("文件中的网点代码和当前网点代码不一致!");
		}
		if (StringUtils.isBlank(deptCode)) {
			throw new BizException("网点代码不能为空!");
		}
		Department dept = DepartmentCacheBiz.getDepartmentByCode(deptCode);
		if (dept == null) {
			throw new BizException("网点代码不存在!");
		}
		dataMap.put("DEPT_CODE", deptCode);
		dataMap.put("DEPT_Id", dept.getId());
	}

	private void createErrorCell(HSSFRow row, String cellValue) {
		HSSFCell errorCell = row.createCell(36);
		errorCell.setCellValue(cellValue);
		HSSFCell preCell = row.getCell(36 - 4);
		if (preCell != null) {
			errorCell.setCellStyle(preCell.getCellStyle());
		}
	}

	private void handEachRow(HSSFSheet sheet, Map<String, Object> dataMap) {
		int rows = (Integer) dataMap.get("total_rows");
		List<Training> insertList = new ArrayList<Training>();
		Set<String> schedulingEmployees = new HashSet<String>();
		int failNum = 0;
		for (int rowBum = 2; rowBum <= rows; rowBum++) {
			HSSFRow row = sheet.getRow(rowBum);
			if (row == null) {
				continue;
			}
			Set<String> errorMsg = new HashSet<String>();
			checkEmpCode(row, errorMsg, schedulingEmployees, dataMap);
			List<Training> tempList = new ArrayList<Training>();
			handEveryDayTraining(dataMap, row, errorMsg, tempList);
			if (!errorMsg.isEmpty()) {
				failNum++;
				dataMap.put("sucess", false);
				createErrorCell(row, getErrorDesc(errorMsg));
				continue;
			}
			sheet.removeRow(row);
			insertList.addAll(tempList);
		}
		if (!insertList.isEmpty()) {
			saveDataToDb(insertList);
		}
		schedulingEmployees.clear();
		dataMap.put("tips", "成功导入：" + (rows - failNum - 1) + "条   失败导入: " + failNum + "条!");
	}

	public boolean saveDataToDb(List<Training> trainingList) {
		return trainingInfoDao.saveTraining(trainingList);
	}

	private void checkEmpCode(HSSFRow row, Set<String> errorMsg, Set<String> schedulingEmployees, Map<String, Object> dataMap) {
		HSSFCell cell = row.getCell(2);
		String empCode = getCellStrValue(cell);
		dataMap.put("EMP_CODE", empCode);
		if (StringUtils.isEmpty(empCode)) {
			errorMsg.add("员工工号不能为空!\n");
			return;
		}
		if (schedulingEmployees.contains(empCode)) {
			errorMsg.add("此员工存在多条记录!\n");
			return;
		}
		Long deptId = (Long) dataMap.get("DEPT_Id");
		Map<String, Object> emp = trainingInfoDao.getEmployeeByEmpCodeAndDeptId(deptId, empCode);
		if (null == emp) {
			errorMsg.add("员工工号不存在!\n");
			return;
		}
		Date dimission_dt = (Date) emp.get("DIMISSION_DT");
		if (null != dimission_dt && dimission_dt.before(new Date())) {
			errorMsg.add("此员工已经离职!\n");
			return;
		}
		schedulingEmployees.add(empCode);
	}

	private String getErrorDesc(Set<String> errorMsg) {
		StringBuffer sb = new StringBuffer();
		for (String errorDesc : errorMsg) {
			sb.append(errorDesc);
			sb.append("\n");
		}
		return sb.toString();
	}

	private void handEveryDayTraining(Map<String, Object> dataMap, HSSFRow row, Set<String> errorMsg, List<Training> tempList) {
		int daysOfMonth = (Integer) dataMap.get("DAYS_OF_MONTH");
		int days = 0;
		for (int i = 3; i < 3 + daysOfMonth; i++) {
			days++;
			HSSFCell cell = row.getCell(i);
			String schedulingCode = getCellStrValue(cell);
			if (CommonUtil.isEmpty(schedulingCode)) {
				continue;
			}
			if (!schedulingCode.equals("PX")) {
				errorMsg.add((days < 10 ? "0" + days : days) +" 号培训代码有误,培训代码必须为PX!\n");
				continue;
			}
			tempList.add(generateEntity(dataMap, days, schedulingCode));
		}
	}

	private Training generateEntity(Map<String, Object> dataMap, int i, String schedulingCode) {
		Training training = new Training();
		training.setDepartmentCode(dataMap.get("DEPT_CODE").toString());
		training.setCreatedEmployeeCode(this.getCurrentUser().getUsername());
		training.setCreateTime(new Date());
		training.setModifiedEmployeeCode(this.getCurrentUser().getUsername());
		training.setModifiedTime(new Date());
		training.setDayOfMonth(dataMap.get("MONTH").toString() + "-" + (i < 10 ? "0" + i : i));
		training.setEmployeeCode(dataMap.get("EMP_CODE").toString());
		training.setPostType(Long.parseLong(dataMap.get("upload_postType").toString()));
		training.setTrainingCode(schedulingCode);
		training.setYearsMonth(dataMap.get("MONTH").toString());
		return training;
	}

	private void writeErrorMsgToExcle(Map<String, Object> resultMap, HSSFWorkbook workBook) {
		CommonUtil.setDownloadFileName(generateFailExcelFileName());
		FileOutputStream fos = null;
		try {
			String errorExcleFileName = CommonUtil.getSaveFilePath(Training.class);
			File outFile = new File(errorExcleFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			fos = new FileOutputStream(errorExcleFileName);
			workBook.write(fos);
			resultMap.put("errorFileName", CommonUtil.getReturnPageFileName());
		} catch (Exception e) {
			throw new BizException("保存路径不存在！");
		}
	}

	private String generateFailExcelFileName() {
		String time = DateUtil.formatDate(new Date(), DateFormatType.yyyyMMdd_HHmmss);
		String fileName = "培训信息导入错误数据_" + time + ".xls";
		return fileName;
	}
}
