package com.sf.module.operation.biz;

import static com.sf.module.common.domain.Constants.DATE_FORMAT;
import static com.sf.module.common.domain.Constants.EXPORT_MAX;
import static com.sf.module.common.domain.Constants.IMPORT_SUCCESSFUL;
import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_FILE_NAME;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.NUMERIC_FORMAT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sf.framework.base.IPage;
import com.sf.framework.core.domain.IDepartment;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.IOUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.TemplateHelper;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.dao.GroupBaseInfoDao;
import com.sf.module.operation.dao.IGroupBaseInfoDao;
import com.sf.module.operation.domain.GroupBaseInfo;
import com.sf.module.operation.domain.GroupBaseInfoError;
import com.sf.module.operation.util.CommonUtil;

public class GroupBaseInfoBiz extends BaseBiz implements IGroupBaseInfoBiz {
	public static final String GROUP_EXPORT_XLS = "组别导出.xls";
	public static final String UPLOAD_RECOUNT_TO_MAX = "一次最多只能导入 1000 条记录！";
	public static final String GROUP_EXPORT_EXCEL = "组别导出_";
	public static final String AREA_CODE_IS_NULL = "地区代码不能为空;";
	public static final String AREA_CODE_NOT_EXIST = "地区代码不存在;";
	public static final String DEPT_CODE_IS_NULL = "网点代码不能为空;";
	public static final String DEPT_CODE_NOT_EXIST = "网点代码不存在;";
	public static final String NOT_DEPT_ACCESS = "没有该网点权限;";
	public static final String TRANSIT_FIELD = "中转场";
	public static final String AVIATION_GROUP = "航空组";
	public static final String IMPORT_THE_TRANSITION_OR_AVIATION_GROUP = "导入网点只能是航空操作组和中转场;";
	public static final String AREA_CODE_AND_DEPT_CODE_NOT_MATCH = "地区代码与网点代码不匹配;";
	public static final String DOT_NOT_CONSISTENT = "当前选择网点和excel中的网点不一致;";
	public static final String GROUP_CODE_IS_NULL = "小组代码不能为空;";
	public static final String GROUP_CODE_ALREADY_EXISTS = "小组代码在系统已存在;";
	public static final String GROUP_CODE_LENGTH_TO_MAX = "小组代码最大长度为15;";
	public static final String GROUP_CODE_ERROR = "小组代码设置错误，请仔细查看模版中的规则！";
	public static final String GROUP_NAME_IS_NULL = "小组名称不能为空;";
	public static final String GROUP_NAME_ALREADY_EXISTS = "小组名称在指定网点中已存在;";
	public static final String GROUP_NAME_LENGTH_TO_MAX = "小组名称最大长度为20";
	public static final String CHINESE_CHARACTER_OR_LETTER = "[a-zA-Z\u4e00-\u9faf]+$";
	public static final String GROUP_NAME_IS_CHINESE_CHARACTER_OR_LETTER = "小组名称只能是字母和汉字;";
	public static final String TIME_FORMAT = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String GROUP_ENABLE_DATE_NOT_MORE_THAN = "组别有效日期不能超过历史日期30天以上；";
	public static final String ENABLE_DATE_FORMAT_ERROR = "生效日期格式不正确;";
	public static final String GROUP_DISABLE_DATE_NOT_LESS_ENABLE_DATE = "组别失效日期不能小于生效日期";
	public static final String DISABLE_DATE_ERROR = "失效日期格式不正确;";
	public static final String REMARK_LENGTH_TO_MAX = "备注最大长度为30;";
	public static final String HEADQUARTERS = "001";
	public static final String VERIFY_THE_REASON = "校验未通过原因";
	public static final String EXPORT_DATA = "导出数据";
	public static final String EXPORT_FILE = "导出文件出错:";
	public static final String READ_FILE = "读文件出错:";
	private IGroupBaseInfoDao groupBaseInfoDao;
	private IOssDepartmentDao ossDepartmentDao;
	public static final String TEMPLATE_NAME = "组别导入错误信息_";

	public void setGroupBaseInfoDao(IGroupBaseInfoDao groupBaseInfoDao) {
		this.groupBaseInfoDao = groupBaseInfoDao;
	}

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	public void saveGroupBaseInfo(GroupBaseInfo groupBaseInfo) throws Exception {
		if (groupBaseInfo.getId() == null) {
			assembleAddGroupBaseInfo(groupBaseInfo);
		} else {
			assembleUpdateGroupBaseInfo(groupBaseInfo);
		}
	}

	private void assembleAddGroupBaseInfo(GroupBaseInfo groupBaseInfo) {
		verifyParameters(groupBaseInfo);
		groupBaseInfo.setCreateEmpCode(this.getCurrentUser().getEmployee().getCode());
		groupBaseInfo.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
		groupBaseInfo.setCreateTm(new Date());
		groupBaseInfo.setModifiedTm(new Date());
		groupBaseInfo.setRemark(StringUtil.strReplace(groupBaseInfo.getRemark()));
		groupBaseInfoDao.saveOrUpdate(groupBaseInfo);
	}

	private void assembleUpdateGroupBaseInfo(GroupBaseInfo groupBaseInfo) {
		GroupBaseInfo modifyGroupBaseInfo = groupBaseInfoDao.load(groupBaseInfo.getId());
		modifyGroupBaseInfo.setRemark(StringUtil.strReplace(groupBaseInfo.getRemark()));
		modifyGroupBaseInfo.setDisableDt(groupBaseInfo.getDisableDt());
		modifyGroupBaseInfo.setEnableDt(groupBaseInfo.getEnableDt());
		modifyGroupBaseInfo.setModifiedTm(new Date());
		modifyGroupBaseInfo.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
		groupBaseInfoDao.saveOrUpdate(modifyGroupBaseInfo);
	}

	private void verifyParameters(GroupBaseInfo groupBaseInfo) {
		IDepartment dept = DepartmentCacheBiz.getDepartmentByID(groupBaseInfo.getDeptId());
		if (groupBaseInfo.getGroupCode().indexOf(dept.getDeptCode() + "-") != 0) {
			throw new BizException("groupBaseInfo.groupCode.error");
		}
		// 验证小组代码是否已存在
		if (groupBaseInfoDao.GroupCodeExistInAll(groupBaseInfo.getGroupCode().trim()))
			throw new BizException("groupBaseInfo.groupCode.is.Exist");
		// 验证小组是否已存在
		if (groupBaseInfoDao.GroupNameExistInDept(groupBaseInfo.getGroupName().trim(), groupBaseInfo.getDeptId()))
			throw new BizException("groupBaseInfo.groupName.dept.is.Exist");
	}

	public void remove(Long id) {
		groupBaseInfoDao.remove(id);
	}

	public IPage<GroupBaseInfo> findPageByGroupBaseInfos(GroupBaseInfo baseGroupInfo, int pageSize, int pageIndex) {
		// 用户选择的网点
		IDepartment dept = DepartmentCacheBiz.getDepartmentByID(baseGroupInfo.getDeptId());
		// 是否递归查询
		IPage<GroupBaseInfo> page = groupBaseInfoDao.findPageByPage(baseGroupInfo, false, super.getUserId(), pageSize, pageIndex);
		for (GroupBaseInfo info : page.getData()) {
			dept = DepartmentCacheBiz.getDepartmentByID(info.getDeptId());
			if (null != dept) {
				info.setDeptName(dept.getDeptCode() + "/" + dept.getDeptName());
				info.setAreaCode(dept.getAreaDeptCode());
			}
		}
		return page;
	}

	public Collection<GroupBaseInfo> findPageByGroupBaseInfos() {
		return groupBaseInfoDao.findAll();
	}

	public void delete(String groupBaseInfos) {
		try {
			String[] groupBaseInfoIdArray = groupBaseInfos.replaceAll(",$", "").split(",", -1);
			for (String sgroupBaseInfoId : groupBaseInfoIdArray) {
				Long groupBaseInfoId = Long.parseLong(sgroupBaseInfoId);
				// 需要检查是否被使用-----待实现的内容
				GroupBaseInfo groupBaseInfo = groupBaseInfoDao.load(groupBaseInfoId);
				if (groupBaseInfoDao.queryGroupValidity(groupBaseInfo.getId(), groupBaseInfo.getDeptId())) {
					throw new BizException(getErrorExceptionMsg());
				}
				groupBaseInfoDao.remove(groupBaseInfo);
				// 清空缓存
				((GroupBaseInfoDao) groupBaseInfoDao).flush();
			}
		} catch (org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException e) {
			throw new BizException(getErrorExceptionMsg());
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new BizException(getErrorExceptionMsg());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new BizException(getErrorExceptionMsg());
		}
	}

	private String getErrorExceptionMsg() {
		return "groupBaseInfo.using.not.del";
	}

	/**
	 * 返回当前日期 不包含时间
	 * 
	 * @return
	 */
	public static Date currentDt() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {
		}
		return now;
	}

	private String writeErrExcel(HSSFWorkbook errorWorkBook, FileOutputStream errorFos, List<GroupBaseInfoError> noPassList) {
		HSSFCellStyle cellStyle = errorWorkBook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFSheet sheet = errorWorkBook.getSheetAt(0);
		HSSFRow row1 = sheet.getRow(1);
		row1.getCell(7).setCellValue(VERIFY_THE_REASON);
		if (noPassList != null && noPassList.size() > 0) {
			int rowNum = 1; // 操作行
			for (GroupBaseInfoError m : noPassList) {
				int column = 0;
				HSSFRow row = sheet.createRow(++rowNum);
				// 地区代码
				HSSFCell cell1 = row.createCell(column++);
				cell1.setCellValue(m.getAreaCode() == null ? "" : m.getAreaCode());
				cell1.setCellStyle(cellStyle);
				// 网点代码
				HSSFCell cell2 = row.createCell(column++);
				cell2.setCellValue(m.getDeptCode() == null ? "" : m.getDeptCode());
				cell2.setCellStyle(cellStyle);
				// 小组代码
				HSSFCell cell3 = row.createCell(column++);
				cell3.setCellValue(m.getGroupCode() == null ? "" : m.getGroupCode());
				cell3.setCellStyle(cellStyle);
				// 小组名称
				HSSFCell cell4 = row.createCell(column++);
				cell4.setCellValue(m.getGroupName() == null ? "" : m.getGroupName());
				cell4.setCellStyle(cellStyle);
				// 生效日期
				HSSFCell cell5 = row.createCell(column++);
				cell5.setCellValue(m.getEnableDt() == null ? "" : m.getEnableDt());
				cell5.setCellStyle(cellStyle);
				// 失效日期
				HSSFCell cell6 = row.createCell(column++);
				cell6.setCellValue(m.getDisableDt() == null ? "" : m.getDisableDt());
				cell6.setCellStyle(cellStyle);
				// 备注
				HSSFCell cell7 = row.createCell(column++);
				cell7.setCellValue(m.getRemark() == null ? "" : m.getRemark());
				cell7.setCellStyle(cellStyle);
				// 错误信息描述
				HSSFCell cell8 = row.createCell(column++);
				cell8.setCellValue(m.getErrMsg() == null ? "" : m.getErrMsg());
				cell8.setCellStyle(cellStyle);
			}
			try {
				errorWorkBook.write(errorFos);
			} catch (IOException e) {
				if (errorFos != null) {
					try {
						errorFos.close();
					} catch (IOException e1) {
						log.error("err:", e);
					}
				}
				log.error("err:", e);
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	public static String getHSSFCellValue(HSSFCell cell, String format) {
		String val;
		DecimalFormat decimalFormat = new DecimalFormat(format == null ? NUMERIC_FORMAT : format);
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			val = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_STRING:
			val = cell.getRichStringCellValue().getString();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			val = cell.getRichStringCellValue().getString();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellInternalDateFormatted(cell) || HSSFDateUtil.isCellDateFormatted(cell)) {
				double dValue = HSSFDateUtil.getExcelDate(cell.getDateCellValue());
				val = dateFormat.format(HSSFDateUtil.getJavaDate(dValue));
			} else {
				double dValue = cell.getNumericCellValue();
				val = String.valueOf(decimalFormat.format(dValue));
			}
			break;
		default:
			val = "";
		}
		return val;
	}

	private boolean isNotEmptyRow(HSSFRow row) {
		for (int i = 0; i < 7; i++) {
			HSSFCell c = row.getCell(0);
			String excelColumnValue = getHSSFCellValue(c, "#.00").trim();
			if (StringUtils.isNotBlank(excelColumnValue)) {
				return true;
			}
		}
		return false;
	}

	public String getTheRowInTheColumnDataByIndex(HSSFRow row, int index) {
		HSSFCell c = row.getCell(index);
		return getHSSFCellValue(c, "#.00").trim();
	}

	private void checkDataValid(HSSFRow row, List<GroupBaseInfoError> noPassList, Long deptId) {
		boolean hasExistDept = false;
		StringBuilder errDesc = new StringBuilder();

		String areaCode = getTheRowInTheColumnDataByIndex(row, 0);
		if (StringUtils.isBlank(areaCode)) {
			errDesc.append(AREA_CODE_IS_NULL);
		}
		if (DepartmentCacheBiz.getDepartmentByCode(areaCode) == null) {
			errDesc.append(AREA_CODE_NOT_EXIST);
		}

		String deptCode = getTheRowInTheColumnDataByIndex(row, 1);
		if (StringUtils.isBlank(deptCode)) {
			errDesc.append(DEPT_CODE_IS_NULL);
		} else {
			// 验证网点代码是否已存在
			if (DepartmentCacheBiz.getDepartmentByCode(deptCode) == null) {
				errDesc.append(DEPT_CODE_NOT_EXIST);
			} else {
				hasExistDept = true;
				if (ossDepartmentDao.getDeptByUserId(super.getUserId(), DepartmentCacheBiz.getDepartmentByCode(deptCode).getId()).size() == 0)
					errDesc.append(NOT_DEPT_ACCESS);

				// 验证网点代码和地区代码是否匹配
				if (StringUtils.isNotBlank(areaCode) && !DepartmentCacheBiz.getDepartmentByCode(deptCode).getAreaDeptCode().equals(areaCode)) {
					errDesc.append(AREA_CODE_AND_DEPT_CODE_NOT_MATCH);
				}
				// 判断组织树上点选的名称和deptName是否一致
				if (!deptCode.equals(DepartmentCacheBiz.getDepartmentByID(deptId).getDeptCode())) {
					errDesc.append(DOT_NOT_CONSISTENT);
				}
			}
		}
		String groupCode = getTheRowInTheColumnDataByIndex(row, 2);
		if (StringUtils.isBlank(groupCode)) {
			errDesc.append(GROUP_CODE_IS_NULL);
		} else {
			if (groupBaseInfoDao.GroupCodeExistInAll(groupCode)) {
				errDesc.append(GROUP_CODE_ALREADY_EXISTS);
			}
			if (groupCode.length() > 15)
				errDesc.append(GROUP_CODE_LENGTH_TO_MAX);

			if (groupCode.indexOf(DepartmentCacheBiz.getDepartmentByID(deptId).getDeptCode() + "-") != 0)
				errDesc.append(GROUP_CODE_ERROR);
		}

		String groupBaseName = getTheRowInTheColumnDataByIndex(row, 3);
		if (StringUtils.isBlank(groupBaseName)) {
			errDesc.append(GROUP_NAME_IS_NULL);
		} else {
			if (hasExistDept && groupBaseInfoDao.GroupNameExistInDept(groupBaseName, DepartmentCacheBiz.getDepartmentByCode(deptCode).getId()))
				errDesc.append(GROUP_NAME_ALREADY_EXISTS);
			if (groupBaseName.length() > 20)
				errDesc.append(GROUP_NAME_LENGTH_TO_MAX);
		}
		// 生效效日期
		String enableDt = getTheRowInTheColumnDataByIndex(row, 4);
		Date dEnableDt = null;
		if (StringUtils.isNotBlank(enableDt)) {
			String el = TIME_FORMAT;
			Pattern p = Pattern.compile(el);
			Matcher m = p.matcher(enableDt);
			boolean dateFlag = m.matches();
			if (!dateFlag) {
				errDesc.append(ENABLE_DATE_FORMAT_ERROR);
			} else
				try {
					dEnableDt = (new SimpleDateFormat(YYYY_MM_DD)).parse(enableDt);
					Date enableDate = DateUtil.timeCalculate(new Date(), -31);
					if (enableDateError(dEnableDt, enableDate)) {
						errDesc.append(GROUP_ENABLE_DATE_NOT_MORE_THAN);
					}
				} catch (Exception e) {
					errDesc.append(ENABLE_DATE_FORMAT_ERROR);
				}
		}
		// 失效日期
		String disableDt = getTheRowInTheColumnDataByIndex(row, 5);
		Date dDisable = null;
		if (StringUtils.isNotBlank(disableDt)) {
			String el = TIME_FORMAT;
			Pattern p = Pattern.compile(el);
			Matcher m = p.matcher(disableDt);
			boolean dateFlag = m.matches();
			if (!dateFlag) {
				errDesc.append(DISABLE_DATE_ERROR);
			} else {
				try {
					dDisable = (new SimpleDateFormat(YYYY_MM_DD)).parse(disableDt);
					if (dDisable.getTime() <= dEnableDt.getTime()) {
						errDesc.append(GROUP_DISABLE_DATE_NOT_LESS_ENABLE_DATE);
					}
				} catch (Exception e) {
					errDesc.append(DISABLE_DATE_ERROR);
				}
			}
		}

		// 备注
		String remark = getTheRowInTheColumnDataByIndex(row, 6);
		if (StringUtils.isNotBlank(remark) && remark.length() > 30)
			errDesc.append(REMARK_LENGTH_TO_MAX);

		String msg = errDesc.toString();
		if (StringUtils.isBlank(msg)) {
			// 验证通过正常保存
			GroupBaseInfo groupBaseInfo = new GroupBaseInfo();
			groupBaseInfo.setDeptId(DepartmentCacheBiz.getDepartmentByCode(deptCode).getId());
			groupBaseInfo.setGroupName(groupBaseName);
			groupBaseInfo.setGroupCode(groupCode);
			groupBaseInfo.setDisableDt(dDisable);
			groupBaseInfo.setRemark(StringUtil.strReplace(remark));
			groupBaseInfo.setEnableDt(dEnableDt);
			groupBaseInfo.setModifiedTm(new Date());
			groupBaseInfo.setCreateTm(new Date());
			groupBaseInfoDao.save(groupBaseInfo);
		} else {
			// 记录错误信息
			GroupBaseInfoError groupBaseInfoError = new GroupBaseInfoError();
			groupBaseInfoError.setAreaCode(areaCode);
			groupBaseInfoError.setDeptCode(deptCode);
			groupBaseInfoError.setGroupCode(groupCode);
			groupBaseInfoError.setGroupName(groupBaseName);
			groupBaseInfoError.setRemark(StringUtil.strReplace(remark));
			groupBaseInfoError.setDisableDt(disableDt);
			groupBaseInfoError.setEnableDt(enableDt);
			groupBaseInfoError.setErrMsg(msg);
			noPassList.add(groupBaseInfoError);
		}
	}

	private boolean enableDateError(Date dEnableDt, Date currentDate) {
		return dEnableDt.getTime() < currentDate.getTime();
	}

	private void fillRowData(HSSFWorkbook workbook,HSSFSheet sheet, List<GroupBaseInfo> infos) {
		DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		int rownum = 1;
		for (GroupBaseInfo groupBaseInfo : infos) {
			int column = 0; // 操作列
			HSSFRow row = sheet.createRow(++rownum);
			// 取表格样式
			HSSFCellStyle cellStyle = workbook.createCellStyle(); 
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
			// 地区代码
			HSSFCell cell0 = row.createCell(column++);
			cell0.setCellValue(DepartmentCacheBiz.getDepartmentByID(groupBaseInfo.getDeptId()).getAreaDeptCode());
			cell0.setCellStyle(cellStyle);

			// 网点代码
			HSSFCell cell1 = row.createCell(column++);
			cell1.setCellValue(DepartmentCacheBiz.getDepartmentByID(groupBaseInfo.getDeptId()).getDeptCode());
			cell1.setCellStyle(cellStyle);

			// 小组代码
			HSSFCell cell2 = row.createCell(column++);
			cell2.setCellValue(groupBaseInfo.getGroupCode());
			cell2.setCellStyle(cellStyle);

			// 小组名称
			HSSFCell cell3 = row.createCell(column++);
			cell3.setCellValue(groupBaseInfo.getGroupName());
			cell3.setCellStyle(cellStyle);

			// 失效日期
			HSSFCell cell4 = row.createCell(column++);
			cell4.setCellValue(groupBaseInfo.getEnableDt() == null ? "" : df.format(groupBaseInfo.getEnableDt()));
			cell4.setCellStyle(cellStyle);

			// 失效日期
			HSSFCell cell5 = row.createCell(column++);
			cell5.setCellValue(groupBaseInfo.getDisableDt() == null ? "" : df.format(groupBaseInfo.getDisableDt()));
			cell5.setCellStyle(cellStyle);

			// 备注
			HSSFCell cell6 = row.createCell(column++);
			cell6.setCellValue(groupBaseInfo.getRemark());
			cell6.setCellStyle(cellStyle);
		}
	}

	public String exportGroupBaseInfo(GroupBaseInfo groupBaseInfo) {
		String fileName = "";
		try {
			String downloadFileName = TemplateHelper.templateName(GROUP_EXPORT_EXCEL);
			CommonUtil.setDownloadFileName(downloadFileName);
			String exportFileName = CommonUtil.getSaveFilePath(GroupBaseInfo.class);
			File file = new File(exportFileName);
			if (!file.exists())
				file.createNewFile();
			FileInputStream template = new FileInputStream(CommonUtil.getReportTemplatePath(GROUP_EXPORT_XLS));
			HSSFWorkbook workbook = new HSSFWorkbook(template);			
			List<GroupBaseInfo> exportGroupBaseInfo = groupBaseInfoDao.findAllBaseInfos(groupBaseInfo, true, getUserId());
			HSSFSheet sheet;
			for (int i = 0; i <= exportGroupBaseInfo.size() / EXPORT_MAX; i++) {
				if (i != exportGroupBaseInfo.size() / EXPORT_MAX) {
					sheet = workbook.cloneSheet(0);
					workbook.setSheetName(workbook.getSheetIndex(sheet), EXPORT_DATA + (i + 1));
					List<GroupBaseInfo> exportByPageBaseInfo = exportGroupBaseInfo.subList(i * EXPORT_MAX, (i + 1) * EXPORT_MAX);
					fillRowData(workbook,sheet, exportByPageBaseInfo);
				} else {
					if (exportGroupBaseInfo.size() % EXPORT_MAX > 0) {
						sheet = workbook.cloneSheet(0);
						workbook.setSheetName(workbook.getSheetIndex(sheet), EXPORT_DATA + (i + 1));
						List<GroupBaseInfo> exportByPageBaseInfo = exportGroupBaseInfo.subList(i * EXPORT_MAX, i * EXPORT_MAX + exportGroupBaseInfo.size()
								% EXPORT_MAX);
						fillRowData(workbook,sheet, exportByPageBaseInfo);
					}
				}
			}
			if (exportGroupBaseInfo.size() > 0)
				workbook.removeSheetAt(0);
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			workbook.write(fos);
			fos.close();
			fileName = CommonUtil.getReturnPageFileName();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(KEY_ERROR, e);
		}
		return fileName;
	}

	public boolean noticeShow() {
		return groupBaseInfoDao.noticeHasEmployee(super.getCurrentUser().getEmployee().getDepartment().getId());
	}

	public List<GroupBaseInfo> noticeShowList() {
		return groupBaseInfoDao.noticeShowList(super.getCurrentUser().getEmployee().getDepartment().getId());
	}

	public HashMap<String, String> readUpLoadGroupBaseInfos(File file, String fileName, Long deptId) {
		HashMap<String, String> map = new HashMap<String, String>();
		HSSFWorkbook readWorkBook;
		FileInputStream fileInputStream = null;
		FileInputStream errorInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			readWorkBook = new HSSFWorkbook(fileInputStream);
			HSSFSheet readSheet = readWorkBook.getSheetAt(0);
			int recounts = readSheet.getLastRowNum();
			if (recounts > 1000) {
				map.put(KEY_MESSAGE, UPLOAD_RECOUNT_TO_MAX);
				return map;
			}
			List<GroupBaseInfoError> noPassList = new ArrayList<GroupBaseInfoError>();
			int notEmptyRow = 0;
			for (int rowIndex = 2; rowIndex <= recounts; rowIndex++) {
				HSSFRow row = readSheet.getRow(rowIndex);
				if (isNotEmptyRow(row)) {
					// 校验并收集错误的到noPassList
					checkDataValid(row, noPassList, deptId);
					continue;
				}
				notEmptyRow++;
			}

			String incorrectMessage = extractHandlerResult(recounts, notEmptyRow, noPassList.size());
			map.put(KEY_MESSAGE, incorrectMessage);

			// 生成导入错误的Excel
			errorInputStream = createImportErrorExcel(map, errorInputStream, noPassList);
		} catch (Exception e1) {
			map.put(KEY_MESSAGE, READ_FILE + e1.getMessage());
			return map;
		} finally {
			IOUtil.close(errorInputStream);
			IOUtil.close(fileInputStream);
		}
		return map;
	}

	private FileInputStream createImportErrorExcel(HashMap<String, String> map, FileInputStream errorInputStream, List<GroupBaseInfoError> noPassList) {
		String errorFileName;
		try {
			String downloadFileName = TemplateHelper.templateName(TEMPLATE_NAME);
			CommonUtil.setDownloadFileName(downloadFileName);
			String exportFileName = CommonUtil.getSaveFilePath(GroupBaseInfo.class);

			File outFile = new File(exportFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			errorInputStream = new FileInputStream(CommonUtil.getReportTemplatePath(GROUP_EXPORT_XLS));

			HSSFWorkbook errorWorkBook = new HSSFWorkbook(errorInputStream);
			FileOutputStream fos = new FileOutputStream(outFile.getAbsoluteFile());
			errorFileName = writeErrExcel(errorWorkBook, fos, noPassList);
			if (noPassList.size() > 0) {
				map.put(KEY_FILE_NAME, errorFileName);
			}
		} catch (Exception ee) {
			throw new BizException(EXPORT_FILE + ee.getMessage());
		}
		return errorInputStream;
	}

	private String extractHandlerResult(int totalRow, int notEmptyRow, int incorrectDataSize) {
		int successDataCount = totalRow - incorrectDataSize - 1 - notEmptyRow;
		return String.format(IMPORT_SUCCESSFUL, successDataCount, incorrectDataSize);
	}
}
