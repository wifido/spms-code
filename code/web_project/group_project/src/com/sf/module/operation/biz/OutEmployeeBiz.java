/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.operation.biz;

import static com.sf.module.common.domain.Constants.COLUMN_DEPT_CODE;
import static com.sf.module.common.domain.Constants.COLUMN_EMP_DUTY_NAME;
import static com.sf.module.common.domain.Constants.COLUMN_EMP_NAME;
import static com.sf.module.common.domain.Constants.COLUMN_GROUP_CODE;
import static com.sf.module.common.domain.Constants.COLUMN_GROUP_ID;
import static com.sf.module.common.domain.Constants.COLUMN_GROUP_NAME;
import static com.sf.module.common.domain.Constants.FIELD_FLAG;
import static com.sf.module.common.domain.Constants.FIELD_USER_ID;
import static com.sf.module.common.domain.Constants.KEY_EMP_CODES;
import static com.sf.module.common.domain.Constants.KEY_EMP_IDS;
import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_FILE;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.util.DateFormatType.yyyy_MM_dd;
import static com.sf.module.common.util.DateUtil.parseDate;
import static com.sf.module.common.util.IOUtil.close;
import static com.sf.module.common.util.StringUtil.extractHandlerResult;
import static java.lang.String.valueOf;
import static net.sf.json.JSONArray.fromObject;
import static net.sf.json.JSONArray.toArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.dao.IOutEmployeeDao;
import com.sf.module.operation.dao.ISchedulMgtDao;
import com.sf.module.operation.domain.EntityExport;
import com.sf.module.operation.domain.GroupOutEmployee;
import com.sf.module.operation.domain.OperationEmployeeAttribute;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.util.CommonUtil;

/**
 * 外包人员实体的业务实现类
 * 
 * @author zhaochangjin 2014-06-20
 */
public class OutEmployeeBiz extends BaseBiz implements IOutEmployeeBiz {
	private static final String CHANGE_GROUP_CODE = "CHANGE_GROUP_CODE";
	private static final String CHANGE_GROUP_NAME = "CHANGE_GROUP_NAME";
	private static final String TOTAL_ROWS = "total_rows";
	private static final String COLUMN_COLOR_GREEN = "GREEN";
	private static final String COLUMN_COLOR_YELLOW = "YELLOW";
	private static final String COLUMN_COLOR = "COLUMN_COLOR";
	private static final String ENABLE_TM = "ENABLE_TM";
	private static final String EMP_CODE = "EMP_CODE";
	private static final String EMPLOYEES = "employees";
	private static final String ERROR_GROUP_ENABLE_DATE = "小组失效时间格式有误！";
	private static final String ENABLE_DATE = "enableDate";
	public static final String FIELD_SITE_CODE = "SITE_CODE";
	public static final String FIELD_OPERATION_EMP_ATTRIBUTE_ID = "OPERATION_EMP_ATTRIBUTE_ID";
	private static final String KEY_IMPORT_DEPT_ID = "importDeptId";
	private static final String DELETE_GROUP = "<label style = 'color:red'>删除小组</label>";
    private static final String KEY_GROUP_ID = "groupId";

    /**
	 * 外包人员实体的Dao接口
	 */
	private IOutEmployeeDao outEmployeeDao;

	/**
	 * 人员同步邮件发送
	 */
	private ISendMailBiz sendMailBiz;

	private static String EMP_POST_TYPE = "1";

	/**
	 * 设置邮件发送Biz接口
	 */
	public void setSendMailBiz(ISendMailBiz sendMailBiz) {
		this.sendMailBiz = sendMailBiz;
	}

	/**
	 * 设置外包人员实体的Dao接口
	 */
	public void setOutEmployeeDao(IOutEmployeeDao outEmployeeDao) {
		this.outEmployeeDao = outEmployeeDao;
	}

	/**
	 * 用户拥有权限网点
	 */
	private IOssDepartmentDao ossDepartmentDao;

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	private ISchedulMgtDao schedulMgtDao;

	public void setSchedulMgtDao(ISchedulMgtDao schedulMgtDao) {
		this.schedulMgtDao = schedulMgtDao;
	}

	public Map findDriversForPage(String empcode, Long deptId, String empname, Long groupid, int pageSize, int pageIndex) {
		return outEmployeeDao.findEmpForPage(empcode, deptId, empname, groupid, pageSize, pageIndex);
	}

	/**
	 * 查询人员信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
	public HashMap queryOutEmployee(HashMap paramsMap) {
		long userId = super.getUserId();
		paramsMap.put(FIELD_USER_ID, userId);
		HashMap result = outEmployeeDao.queryOutEmployee(paramsMap);
		List<Map<String, Object>> employees = (List<Map<String, Object>>) result.get(ROOT);
		if (employees.isEmpty())
			return result;
		for (Map<String, Object> employee : employees) {
			Map<String, Object> record = outEmployeeDao.queryRecordOfModifyEmployeeGroup(employee.get(EMP_CODE).toString());
			if (record == null)
				continue;
			employee.put(CHANGE_GROUP_CODE, record.get(Constants.COLUMN_GROUP_CODE));
			employee.put(CHANGE_GROUP_NAME, record.get(Constants.COLUMN_GROUP_NAME));
			employee.put(ENABLE_TM, record.get(ENABLE_TM));
			Date enableTm = (Date) record.get(ENABLE_TM);
			Date today = new Date();
			if (enableTm.after(today)) {
				employee.put(COLUMN_COLOR, COLUMN_COLOR_YELLOW);
			}
			if (enableTm.before(today) && enableTm.after(DateUtils.addMonths(today, -1))) {
				employee.put(COLUMN_COLOR, COLUMN_COLOR_GREEN);
			}
		}
		return result;
	}

	/**
	 * 获取外包人员自动生成的工号
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
	public HashMap getInsertEmpCode() {
		return outEmployeeDao.getInsertEmpCode();
	}

	/**
	 * 查询小组信息
	 * 
	 * @return dataList
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
	public List getGoupByDeptId(HashMap paramsMap) {
		if (null != paramsMap.get("deptCode")) {
			long departmentId = getDepartmentId(paramsMap);
			paramsMap.put("deptId", departmentId);
		}

		return queryAllGroup(paramsMap);
	}

	public List queryGroupWithEmptyGroup(HashMap paramsMap) {
		List groupLists = getGoupByDeptId(paramsMap);
		groupLists.add(createHashMapWithEmptyGroup());
		return groupLists;
	}

	private List queryAllGroup(HashMap paramsMap) {
		return outEmployeeDao.getGroupByDeptId(paramsMap);
	}

	private Long getDepartmentId(HashMap paramsMap) {
		return DepartmentCacheBiz.getDepartmentByCode(paramsMap.get("deptCode").toString()).getId();
	}

	private HashMap createHashMapWithEmptyGroup() {
		HashMap clearGroup = new HashMap();
		clearGroup.put("GROUP_ID", "delete");
		clearGroup.put("GROUP_NAME", DELETE_GROUP);
		return clearGroup;
	}

	/**
	 * 保存人员基础信息
	 * 
	 * @return true / false
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 */
	public boolean saveEmployee(HashMap paramsMap) {
		OutEmployee outEmployee = new OutEmployee();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 网点ID
		if (!StringUtils.isEmpty(paramsMap.get("deptCode"))) {
			Long deptId = getDepartmentId(paramsMap);
			outEmployee.setDeptId(deptId);
		}
		// 工号
		if (!StringUtils.isEmpty(paramsMap.get("empCode"))) {
			outEmployee.setEmpCode(paramsMap.get("empCode").toString());
			OutEmployee e = outEmployeeDao.loadBy("empCode", paramsMap.get("empCode").toString());
			if (null != e) {
				throw new BizException("工号已存在无法保存！");
			}
		}
		// 姓名
		if (!StringUtils.isEmpty(paramsMap.get("empName"))) {
			outEmployee.setEmpName(paramsMap.get("empName").toString());
		}
		// 小组ID
		if (!StringUtils.isEmpty(paramsMap.get("groupId"))) {
			try {
				outEmployee.setGroupId(Long.parseLong(paramsMap.get("groupId").toString()));
			} catch (Exception e) {
				throw new BizException("小组名称错误，请选择小组名称！");
			}
		}
		// 离职日期
		if (!StringUtils.isEmpty(paramsMap.get("dimissionDt"))) {
			try {
				Date date = sdf.parse(paramsMap.get("dimissionDt").toString());
				outEmployee.setDimissionDt(date);
			} catch (ParseException e) {
				throw new BizException("离职日期填写错误！");
			}
		}
		// 职位名称
		if (!StringUtils.isEmpty(paramsMap.get("empDutyName"))) {
			outEmployee.setEmpDutyName(paramsMap.get("empDutyName").toString());
		}
		// 用工类型 外包人员
		if (StringUtils.isEmpty(paramsMap.get("workType"))) {
			outEmployee.setWorkType(6);
		} else {
			outEmployee.setWorkType(Integer.parseInt((paramsMap.get("workType").toString())));
		}
		// 邮箱
		if (!StringUtils.isEmpty(paramsMap.get("email"))) {
			outEmployee.setEmail(paramsMap.get("email").toString());
		}
		// 所在小组失效日期
		if (!StringUtils.isEmpty(paramsMap.get("disableDt"))) {
			try {
				Date date = sdf.parse(paramsMap.get("disableDt").toString());
				outEmployee.setDisableDt(date);
			} catch (ParseException e) {
				return false;
			}
		}
		// 创建时间
		Date date = new Date();
		outEmployee.setCreateTm(date);
		outEmployee.setModifiedTm(date);
		long userId = super.getUserId();
		// 创建人工号
		outEmployee.setCreateEmpCode(Long.toString(userId));

		outEmployee.setEmpPostType(EMP_POST_TYPE);

		outEmployeeDao.save(outEmployee);

		return true;
	}

	/**
	 * 更新人员基础信息
	 * 
	 * @return true / false
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 */
	public boolean updateEmployee(HashMap paramsMap) {
		OutEmployee outEmployee = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 是否为外包人员
		boolean isOutEmployee = true;
		if (!StringUtils.isEmpty(paramsMap.get("isOutEmployee"))) {
			isOutEmployee = Boolean.parseBoolean(paramsMap.get("isOutEmployee").toString());
		} else {
			return false;
		}
		if (!StringUtils.isEmpty(paramsMap.get("empId"))) {
			outEmployee = outEmployeeDao.load(Long.parseLong(paramsMap.get("empId").toString()));
		} else if (!StringUtils.isEmpty(paramsMap.get("empCode"))) {
			outEmployee = outEmployeeDao.loadBy("empCode", paramsMap.get("empCode").toString());
		} else {
			return false;
		}
		if (isOutEmployee) {// 外包人员处理
			// 离职日期
			if (!StringUtils.isEmpty(paramsMap.get("dimissionDt"))) {
				try {
					Date date = sdf.parse(paramsMap.get("dimissionDt").toString());
					outEmployee.setDimissionDt(date);
				} catch (ParseException e) {
					return false;
				}
			} else {
				outEmployee.setDimissionDt(null);
			}
			if (!StringUtils.isEmpty(paramsMap.get("empDutyName"))) {
				outEmployee.setEmpDutyName(paramsMap.get("empDutyName").toString());
			} else {
				outEmployee.setEmpDutyName(null);
			}
		}

		outEmployee.setGroupId(extractGroupId(paramsMap));

		// 同步人员信息操作
		if (!StringUtils.isEmpty(paramsMap.get("isSynEmployee"))) {
			if (StringUtils.isEmpty(paramsMap.get("deptCode")))
				return false;
			Long deptId = getDepartmentId(paramsMap);

			outEmployee.setDeptId(deptId);
		}

		OperationEmployeeAttribute operationEmployeeAttribute = new OperationEmployeeAttribute();
		operationEmployeeAttribute.setModifiedTm(new Date());
		if (StringUtil.isNotBlank(valueOf(paramsMap.get(FIELD_OPERATION_EMP_ATTRIBUTE_ID)))) {
			operationEmployeeAttribute.setId(Long.valueOf(paramsMap.get(FIELD_OPERATION_EMP_ATTRIBUTE_ID).toString()));
			operationEmployeeAttribute.setSiteCode(valueOf(paramsMap.get(FIELD_SITE_CODE)));
			outEmployeeDao.updateEmployeeSiteCode(operationEmployeeAttribute);
		} else {
			operationEmployeeAttribute.setId(outEmployeeDao.buildOperationEmployeeAttributeId());
			operationEmployeeAttribute.setGroupId(valueOf(paramsMap.get("groupId")));
			operationEmployeeAttribute.setEmpCode(outEmployee.getEmpCode());
			operationEmployeeAttribute.setSiteCode(valueOf(paramsMap.get(FIELD_SITE_CODE)));
			outEmployeeDao.insertOperationEmployeeAttribute(operationEmployeeAttribute);
		}
		// 修改时间
		Date date = new Date();
		outEmployee.setModifiedTm(date);
		// 修改人工号
		long userId = super.getUserId();
		outEmployee.setModifiedEmpCode(Long.toString(userId));
		// 执行更新
		outEmployee.setEmpPostType(EMP_POST_TYPE);
		outEmployeeDao.update(outEmployee);
		return true;
	}

	private Long extractGroupId(HashMap paramsMap) {
		Object groupId = paramsMap.get("groupId");
		Long groupIdAsString = null;

		if (null != groupId && !"删除小组".equals(groupId)) {
			groupIdAsString = Long.valueOf(groupId.toString());
		}

		return groupIdAsString;
	}

	/**
	 * 删除人员基础信息
	 * 
	 * @return true / false
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 */
	public boolean deleteEmployee(HashMap paramsMap) {
		if (StringUtils.isEmpty(paramsMap.get(KEY_EMP_IDS)))
			return false;
		// 执行删除
		String[] ids = paramsMap.get(KEY_EMP_IDS).toString().split("@@");
		String[] empCodes = paramsMap.get(KEY_EMP_CODES).toString().split("@@");
		outEmployeeDao.deleteOperationEmployeeAttributeById(empCodes);

		for (String empId : ids) {
			if (StringUtil.isNotBlank(empId)) {
				outEmployeeDao.remove(Long.parseLong(empId));
			}
		}
		return true;
	}

	/**
	 * 导出人员基础信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 */
	public HashMap exportEmployee(HashMap paramsMap) {
		// 列信息存储容器
		HashMap columnMap = new HashMap();
		// 返回数据容器
		HashMap returnMap = new HashMap();
		HashMap dataMap = new HashMap();
		try {
			paramsMap.put(FIELD_FLAG, "exportEmployee");
			// 获取导出数据
			dataMap = outEmployeeDao.exportOutEmployee(paramsMap);
			// 下载的文件名
			String downloadFileName = String.format("人员信息导出%s.xls", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
			CommonUtil.setDownloadFileName(downloadFileName);
			// 获取下载路径
			String exportFileName = CommonUtil.getSaveFilePath(OutEmployee.class);
			File file = new File(exportFileName);
			// 判断文件是否存在
			if (!file.exists())
				file.createNewFile();
			// 模板文件读取
			FileInputStream template = new FileInputStream(CommonUtil.getReportTemplatePath("人员分组信息导出.xls"));
			// 获取模板文件的WorkBook
			HSSFWorkbook workbook = new HSSFWorkbook(template);
			// 取模板的第一页
			Sheet sheet = workbook.getSheetAt(0);
			// 获取模板列信息
			columnMap = new EntityExport().getColumnMap(workbook, columnMap, 0, 2);
			// 获取导出得到数据容器
			List list = (List) dataMap.get(ROOT);
			// 把数据写入Excel
			workbook = new EntityExport().writeWorkBook(workbook, 0, 4, columnMap, list, true);
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			// 把数据写入文件
			workbook.write(fos);
			// 关闭流
			fos.close();
			template.close();
			// 导出文件路径
			returnMap.put("fileName", CommonUtil.getReturnPageFileName());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(KEY_ERROR, e);
			returnMap.put(KEY_SUCCESS, false);
			returnMap.put(KEY_MESSAGE, "导出错误！");
			return returnMap;
		}
		return returnMap;
	}

	private boolean isEmptyRow(HSSFRow row, HashMap columnMap) {
		int cellDeptCode = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
		String deptCode = Template.getCellStrValue(row.getCell(cellDeptCode));

		int cellEmployeeName = Integer.parseInt(columnMap.get(COLUMN_EMP_NAME).toString());
		String employeeName = Template.getCellStrValue(row.getCell(cellEmployeeName));

		return StringUtil.isBlank(deptCode) && StringUtil.isBlank(employeeName);
	}

	/**
	 * 导入人员基础信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-22
	 */
	public HashMap importEmployee(HashMap paramsMap) {
		// 列数据容器
		HashMap columnMap = new HashMap();
		// 返回数据容器
		HashMap datamap = new HashMap();
		// 执行导入操作的数据容器
		ArrayList insertList = new ArrayList();
		// 验证错误的数据容器
		HashMap errorMap = new HashMap();
		try {
			// 获取上传文件
			File file = (File) paramsMap.get("file");
			// 读取文件流
			FileInputStream is = new FileInputStream(file);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			// 获取文件的WorkBook
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 关闭文件流
			is.close();
			// 获取第一页
			HSSFSheet sheet = wb.getSheetAt(0);
			// 获取总记录数
			int rowNumber = sheet.getLastRowNum();
			// 获取列数据
			columnMap = new EntityExport().getColumnMap(wb, columnMap, 0, 2);
			// 获取实体字段数据
			HashMap EntityMap = new EntityExport().getColumnMap(wb, columnMap, 0, 3);
			// 导入成功数和校验不通过数
			int successCount = 0, errorCount = 0;
			// 验证导入的数据量
			if (rowNumber >= 1004) {
				datamap.put(KEY_SUCCESS, false);
				datamap.put(KEY_MESSAGE, "导入数据超过1000条！");
				return datamap;
			}
			// 遍历数据
			for (int i = 4; i <= rowNumber; i++) {
				HSSFRow row = sheet.getRow(i);
				if (isEmptyRow(row, columnMap)) {
					continue;
				}
				// 校验数据
				Map<String, String> validMap = validRow(row, columnMap, paramsMap.get("importDeptId").toString());
				// 判断校验是否通过
				if (validMap.get(KEY_SUCCESS).equals("false")) {
					errorMap.put(valueOf(row.getRowNum()), validMap.get(KEY_MESSAGE));
					errorCount++;
					continue;
				}
				OutEmployee outEmployee = new OutEmployee();
				// 获取实体类所有的方法
				Method[] methods = outEmployee.getClass().getMethods();
				// 调用set方法赋值
				for (Method m : methods) {
					String methodName = m.getName();
					if (EntityMap.get(methodName) != null) {
						int cellIndex = Integer.parseInt(columnMap.get(methodName).toString());
						m.invoke(outEmployee, row.getCell(cellIndex).getStringCellValue());
					}
				}
				// 获取网点代码
				int deptCodeCellIndex = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
				String departmentCode = row.getCell(deptCodeCellIndex).getStringCellValue();

				if (getExcelGroupIfExist(row, getGroupCodeCellIndex(columnMap))) {
					String groupCode = row.getCell(getGroupCodeCellIndex(columnMap)).getStringCellValue();
					List groupList = outEmployeeDao.getGroupByGroupCode(groupCode);
					if (groupList.size() > 0) {
						HashMap groupMap = (HashMap) groupList.get(0);
						outEmployee.setGroupId(Long.parseLong(groupMap.get(COLUMN_GROUP_ID).toString()));
					}
				}
				// 用工类型 外包人员
				outEmployee.setWorkType(6);
				// 获取自动生成的工号
				HashMap empCodeMap = outEmployeeDao.getInsertEmpCode();
				List list = (List) empCodeMap.get(ROOT);
				HashMap nextMap = (HashMap) list.get(0);
				outEmployee.setEmpCode(nextMap.get("NEXTVAL").toString());
				// 创建时间
				Date date = new Date();
				outEmployee.setCreateTm(date);
				outEmployee.setModifiedTm(date);
				long userId = super.getUserId();
				// 创建人工号
				outEmployee.setCreateEmpCode(Long.toString(userId));
				// 网点ID
				outEmployee.setDeptId(DepartmentCacheBiz.getDepartmentByCode(departmentCode).getId());
				// 保存校验通过的数据
				outEmployee.setEmpPostType(EMP_POST_TYPE);

				outEmployeeDao.save(outEmployee);
				successCount++;
			}
			datamap.put(KEY_SUCCESS, true);
			datamap.put(KEY_MESSAGE, extractHandlerResult(successCount, errorCount));
			if (errorCount > 0) {
				// 定义校验不通过数据返回文件名
				String downloadFileName = String.format("人员信息导入错误信息_%s.xls", new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
				// 获取文件下载路径
				String downLoadUrl = new EntityExport().errorWorkBook(wb, errorMap, 0, 4, columnMap, downloadFileName, "外包人员导入模板.xls", 10);
				datamap.put("downLoadUrl", downLoadUrl);
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			datamap.put(KEY_SUCCESS, false);
			datamap.put(KEY_MESSAGE, "导入出错！");
			return datamap;
		}
		return datamap;
	}

	/**
	 * 校验导入数据
	 * 
	 * @param row
	 * @param columnMap
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-06-25
	 */
	public Map validRow(HSSFRow row, HashMap columnMap, String importDeptId) {
		Map<String, String> validMap = new HashMap<String, String>();
		StringBuffer errorMsg = new StringBuffer();
		try {
			// 校验中转场代码校验
			int cellDeptCode = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
			if (null == row.getCell(cellDeptCode))
				row.createCell(cellDeptCode);
			row.getCell(cellDeptCode).setCellType(HSSFCell.CELL_TYPE_STRING);
			String deptCode = row.getCell(cellDeptCode).toString().trim();
			if (StringUtils.isEmpty(deptCode)) {
				errorMsg.append("网点代码不能为空；");
			} else if (null == DepartmentCacheBiz.getDepartmentByCode(deptCode)) {
				errorMsg.append("网点代码不存在；");
			} else if (ossDepartmentDao.getDeptByUserId(super.getUserId(), DepartmentCacheBiz.getDepartmentByCode(deptCode).getId()).size() == 0) {
				errorMsg.append("没有该网点权限；");
			} else if (!importDeptId.contains(deptCode)) {
				errorMsg.append("网点代码与所选网点不一致；");
			}
			// 姓名校验
			int cellEmployeeName = Integer.parseInt(columnMap.get(COLUMN_EMP_NAME).toString());
			if (null == row.getCell(cellEmployeeName))
				row.createCell(cellEmployeeName);
			row.getCell(cellEmployeeName).setCellType(HSSFCell.CELL_TYPE_STRING);
			String employeeName = row.getCell(cellEmployeeName).toString().trim();
			if (StringUtils.isEmpty(employeeName)) {
				errorMsg.append("姓名不能为空；");
			} else if (employeeName.length() > 20) {
				errorMsg.append("姓名长度超出20个字符；");
			}
			// 职位校验
			int cellEmployeeDutyName = Integer.parseInt(columnMap.get(COLUMN_EMP_DUTY_NAME).toString());
			if (null == row.getCell(cellEmployeeDutyName))
				row.createCell(cellEmployeeDutyName);
			row.getCell(cellEmployeeDutyName).setCellType(HSSFCell.CELL_TYPE_STRING);
			String employeeDutyName = row.getCell(cellEmployeeDutyName).toString().trim();
			if (!StringUtils.isEmpty(employeeDutyName) && employeeDutyName.length() > 20) {
				errorMsg.append("职位长度超出20个字符；");
			}
			if (getExcelGroupIfExist(row, getGroupCodeCellIndex(columnMap))) {
				validGroupByGroupCode(row, errorMsg, getGroupCodeCellIndex(columnMap));
			}
		} catch (Exception e) {
			validMap.put(KEY_SUCCESS, "false");
			validMap.put(KEY_MESSAGE, "校验信息出错！");
			return validMap;
		}
		if (errorMsg.length() > 0) {
			validMap.put(KEY_SUCCESS, "false");
			validMap.put(KEY_MESSAGE, errorMsg.toString());
		} else {
			validMap.put(KEY_SUCCESS, "true");
		}
		return validMap;
	}

	private int getGroupCodeCellIndex(HashMap columnMap) {
		int groupCodeCellIndex = Integer.parseInt(columnMap.get(COLUMN_GROUP_CODE).toString());
		return groupCodeCellIndex;
	}

	private int getGroupNameCellIndex(HashMap columnMap) {
		int groupNameCellIndex = Integer.parseInt(columnMap.get(COLUMN_GROUP_NAME).toString());
		return groupNameCellIndex;
	}

	private boolean getExcelGroupIfExist(HSSFRow row, int groupCodeCellIndex) {
		if (null != row.getCell(groupCodeCellIndex)) {
			return true;
		}
		return false;
	}

	private void validGroupByGroupCode(HSSFRow row, StringBuffer errorMsg, int groupCodeCellIndex) {
		HSSFCell groupCodecell = row.getCell(groupCodeCellIndex);
		groupCodecell.setCellType(HSSFCell.CELL_TYPE_STRING);
		String groupCode = groupCodecell.toString().trim();
		if (StringUtil.isNotBlank(groupCode)) {
			if (outEmployeeDao.getGroupByGroupCode(groupCode).isEmpty()) {
				errorMsg.append("小组代码不存在！");
			}
		}
	}

	/**
	 * 查询需要同步的数据
	 * 
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-10
	 */
	public HashMap queryHrEmp(HashMap paramsMap) {
		String deptCode = (String)paramsMap.get("deptCode");
		if(StringUtil.isNotBlank(deptCode)){
			paramsMap.put("deptCode",deptCode.substring(0, deptCode.indexOf("/")));
		}
		return outEmployeeDao.queryHrEmp(paramsMap);
	}

	/**
	 * 更新Hr同步数据状态
	 */
	public boolean updateSynHrEmp(HashMap paramsMap) {
		return outEmployeeDao.updateSynHrEmp(paramsMap);
	}

	/**
	 * 同步人员邮件发送
	 */
	public boolean synEmployeeEmail() {
		/*List list = outEmployeeDao.queryEmployeeToEmail();
		List synList = outEmployeeDao.querySynEmployee();
		// 检查数据库状态查看现在是否能发送邮件
		if (!sendMailBiz.updateEmailStartStatus())
			return false;
		try {
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HashMap map = (HashMap) list.get(i);
					String email = map.get("EMAIL").toString();
					String userDeptCode = map.get(COLUMN_DEPT_CODE).toString();
					sendMailBiz.sendMail(email, createContent(userDeptCode, synList));
				}
			}
		} catch (Exception e) {
			log.error("OutEmployeeBiz  Email send error!" + e.getMessage());
		} finally {
			// 邮件执行完成修改状态
			sendMailBiz.updateEmailEndStatus();
		}*/
		return true;
	}

	/**
	 * 拼接邮件内容
	 * 
	 * @param userName
	 * @param userCode
	 * @param deptCode
	 * @param list
	 * @return String
	 * @author 632898 李鹏
	 * @date 2014-07-15
	 */
	private String createContent(String deptCode, List list) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<p style='width:780px;line-height:23px;'><b>" + "尊敬的用户：");
		// sb.append("</b><br/><label style='width:2em;'></label>"
		// +"有姓名："+userName+"，工号："+userCode+"入职请添加小组信息，请尽快完成！"+"</p>");
		for (int i = 0; i < list.size(); i++) {
			HashMap hashMap = (HashMap) list.get(i);
			if (null != hashMap && hashMap.get(COLUMN_DEPT_CODE).equals(deptCode)) {
				sb.append("</b><br/><label style='width:2em;'></label>" + "有姓名：" + hashMap.get(COLUMN_EMP_NAME) + "，工号：" + hashMap.get(COLUMN_EMP_NAME)
						+ "入职请添加小组信息，请尽快完成！" + "</p>");
			}
		}
		sb.append("<div style='color:gray;font-size:12px;'>系统发送邮件,请不要回复</div></body></html>");
		return sb.toString();
	}

	/**
	 * 删除用户时判断用户是否有排班
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-18
	 */
	public Map isSchedulMgtbyEmpCode(String[] empCodes) {
		Map map = new HashMap();
		List list = new ArrayList();
		for (String empCode : empCodes) {
			if (!StringUtils.isEmpty(empCode)) {
				if (schedulMgtDao.findByEmpCode(empCode)) {
					list.add(empCode);
				}
			}
		}
		if (list.size() > 0) {
			map.put(KEY_SUCCESS, false);
			map.put(ROOT, list);
		} else {
			map.put(KEY_SUCCESS, true);
		}
		return map;

	}

	public void batchUpdateEmployeeGroup(HashMap paramsMap) {
		try {
            outEmployeeDao.batchUpdateEmployeeGroup(getCurrentUser().getUsername(),
                    parseDate(paramsMap.get(ENABLE_DATE).toString(), yyyy_MM_dd),
                    (Map<String, Object>[]) toArray(fromObject(valueOf(paramsMap.get(EMPLOYEES))), HashMap.class),
                    valueOf(paramsMap.get(KEY_GROUP_ID)), 
                    (String)paramsMap.get("department_id"));
		} catch (ParseException e) {
			log.info(ERROR_GROUP_ENABLE_DATE);
			throw new BizException(ERROR_GROUP_ENABLE_DATE);
		}
	}

	private boolean isEmptyRow(HSSFRow row) {
		boolean deptCode = StringUtil.isBlank(Template.getCellStrValue(row.getCell(2)));
		boolean outEmployeeCode = StringUtil.isBlank(Template.getCellStrValue(row.getCell(3)));
		boolean groupCode = StringUtil.isBlank(Template.getCellStrValue(row.getCell(7)));
		return deptCode && outEmployeeCode && groupCode;
	}

	public HashMap groupImportEmployee(HashMap dataMap) {
		log.info("import employee group infomation begin!------");
		dataMap.put("userName", this.getCurrentUser().getUsername());
		log.info("get import excle workBook!-----");
		File file = (File) dataMap.get("file");
		HSSFWorkbook workBook = getHSSFWorkbook(file);
		HSSFSheet sheet = workBook.getSheetAt(0);

		log.info("valid max row number!-----");
		int rowCount = sheet.getLastRowNum();

		if (rowCount > 1004) {
			dataMap.put(KEY_MESSAGE, "导入数据超过1000条！");
			dataMap.put(KEY_SUCCESS, false);
			return dataMap;
		}
		dataMap.put(TOTAL_ROWS, rowCount);

		log.info("create error cell!-----");
		createErrorCell(sheet.getRow(1), "数据导入失败的原因 ");

		log.info("handle each row begin!-----");
		handleEachRow(sheet, dataMap);
		log.info("handle each row end!");

		if (dataMap.containsKey("isPass")) {
			log.info("have error row ,write error info to excle!");
			writeErrorMsgToExcle(dataMap, workBook);
		}

		return dataMap;
	}

	private void handleEachRow(HSSFSheet sheet, Map<String, Object> dataMap) {
		int rows = (Integer) dataMap.get(TOTAL_ROWS);
		log.info("toal rows:--------" + rows);
		int failCount = 0;
		int sucesssCount = 0;
		for (int rowNumber = 2; rowNumber <= rows; rowNumber++) {
			log.info("handle row beign! rowNumber:-----" + rowNumber);
			HSSFRow row = sheet.getRow(rowNumber);
			if (isEmptyRow(row)) {
				log.info("this row is empty!");
				continue;
			}
			GroupOutEmployee groupOutEmployee = new GroupOutEmployee();
			Set<String> errorMsg = new HashSet<String>();
			String deptCode = Template.getCellStrValue(row.getCell(2));
			String outEmployeeCode = Template.getCellStrValue(row.getCell(3));
			String groupCode = Template.getCellStrValue(row.getCell(7));
			String enableTime = Template.getCellStrValue(row.getCell(9));
			groupOutEmployee.setDeptCode(deptCode);
			groupOutEmployee.setEmpCode(outEmployeeCode);
			groupOutEmployee.setGroupCode(groupCode);
			groupOutEmployee.setGroupEnableTime(enableTime);
			validDataOfImportEmployeeGroup(row, groupOutEmployee, dataMap, errorMsg);
			if (!errorMsg.isEmpty()) {
				failCount++;
				createErrorCell(row, getErrorDesc(errorMsg));
				dataMap.put("isPass", false);
				continue;
			}
			sheet.removeRow(row);
			Date enableDate = getEnableDate(groupCode, enableTime);
			outEmployeeDao.updateEmployeeGroup(groupCode, outEmployeeCode, deptCode, enableDate,dataMap.get("userName").toString());
			sucesssCount++;
		}
		removeEmptyRow(sheet);
		dataMap.put(KEY_SUCCESS, true);
		dataMap.put(KEY_MESSAGE, StringUtil.extractHandlerResult(sucesssCount, failCount));
	}

	private Date getEnableDate(String groupCode, String enableTime) {
		Date enableDate = new Date();
		if (StringUtil.isNotBlank(groupCode) && StringUtil.isNotBlank(enableTime)) {
			try {
				enableDate = parseDate(enableTime, yyyy_MM_dd);
			} catch (ParseException e) {
				log.error("生效时间格式有误！", e);
			}
		}
		return enableDate;
	}

	private void removeEmptyRow(HSSFSheet sheet) {
		int i = sheet.getLastRowNum();
		HSSFRow tempRow;
		while (i > 0) {
			i--;
			tempRow = sheet.getRow(i);
			if (tempRow == null) {
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}
		}
	}

	private void createErrorCell(HSSFRow row, String cellValue) {
		HSSFCell errorCell = row.createCell(11);
		errorCell.setCellValue(cellValue);
	}

	private String getErrorDesc(Set<String> errorMsg) {
		StringBuffer sb = new StringBuffer();
		for (String errorDesc : errorMsg) {
			sb.append(errorDesc);
			sb.append("\n");
		}
		return sb.toString();
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

	private String generateFailExcelFileName() {
		String time = DateUtil.formatDate(new Date(), DateFormatType.yyyyMMdd_HHmmss);
		String fileName = String.format("运作人员分组导入错误数据_%s.xls", time);
		log.info("generateFailExcelFileName :" + fileName);
		return fileName;
	}

	private void writeErrorMsgToExcle(Map<String, Object> resultMap, HSSFWorkbook workBook) {
		CommonUtil.setDownloadFileName(generateFailExcelFileName());
		FileOutputStream fos = null;
		try {
			String errorExcleFileName = CommonUtil.getSaveFilePath(OutEmployee.class);
			File outFile = new File(errorExcleFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			fos = new FileOutputStream(errorExcleFileName);
			workBook.write(fos);
			resultMap.put("downLoadUrl", CommonUtil.getReturnPageFileName());
		} catch (Exception e) {
			log.error("have a exception when create error info to excle! exception:" + e);
			throw new BizException("保存路径不存在！");
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new BizException("写入错误信息时出错！");
				}
			}
		}
	}

	private void validDataOfImportEmployeeGroup(HSSFRow row, GroupOutEmployee groupOutEmployee, Map<String, Object> dataMap, Set<String> errorMsg) {
		if (StringUtils.isEmpty(groupOutEmployee.getDeptCode())) {
			errorMsg.add("网点代码不能为空！");
		}
		if (StringUtils.isEmpty(groupOutEmployee.getDeptCode())) {
			errorMsg.add("工号不能为空！");
		}
		if (!outEmployeeDao.queryEmployeeValidity(groupOutEmployee.getEmpCode(), groupOutEmployee.getDeptCode())) {
			errorMsg.add("员工工号和网点代码不匹配！");
		}
		String departmentCode = dataMap.get("groupimportDeptId").toString();
		if (!departmentCode.contains(groupOutEmployee.getDeptCode())) {
			errorMsg.add("网点代码和所选网点不一致！");
		}
		if (StringUtil.isNotBlank(groupOutEmployee.getGroupCode())) {
			if (!outEmployeeDao.queryGroupValidity(groupOutEmployee.getGroupCode(), groupOutEmployee.getDeptCode())) {
				errorMsg.add("小组代码不存在！");
			}
			if (StringUtils.isEmpty(groupOutEmployee.getGroupEnableTime())) {
				errorMsg.add("生效时间不能为空！");
			} else {
				try {
					Date enbaleDate = parseDate(groupOutEmployee.getGroupEnableTime(), yyyy_MM_dd);
					if (enbaleDate.before(DateUtils.addDays(new Date(), -8))) {
						errorMsg.add("生效日期不能超过历史7天以上！");
					}
				} catch (ParseException e) {
					errorMsg.add("生效时间格式有误！");
				}
			}
		}

	}

	private boolean importEmployeeAttributeIsEmptyRow(HSSFRow row) {
		int totalEmptyRow = 0;
		for (int i = 0; i < row.getLastCellNum(); i++) {
			HSSFCell cell = row.getCell(i);
			if (cell == null || StringUtil.isBlank(Template.getCellStrValue(cell))) {
				totalEmptyRow++;
			}
		}
		return totalEmptyRow == row.getLastCellNum();
	}

	@Override
	public HashMap importEmployeeAttribute(HashMap queryMap) {
		HashMap returnMap = new HashMap();
		HashMap<String, String> validMap = new HashMap<String, String>();
		HashMap<Integer, String> errorMap = new HashMap<Integer, String>();
		List<GroupOutEmployee> errorList = new ArrayList<GroupOutEmployee>();

		try {
			File file = (File) queryMap.get(KEY_FILE);
			FileInputStream fileInputStream = new FileInputStream(file);
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
			HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
			fileInputStream.close();

			HSSFSheet sheet = workbook.getSheetAt(0);
			int totalRowNumber = sheet.getLastRowNum();
			if (totalRowNumber > 1004) {
				returnMap.put(KEY_SUCCESS, false);
				returnMap.put(KEY_MESSAGE, "导入数据超过1000条！");
				return returnMap;
			}

			int errorTotal = 0, successTotal = 0;
			String queryDepartmentCode = (String) queryMap.get(KEY_IMPORT_DEPT_ID);

			for (int i = 4; i <= totalRowNumber; i++) {
				HSSFRow row = sheet.getRow(i);
				if (importEmployeeAttributeIsEmptyRow(row)) {
					continue;
				}
				GroupOutEmployee groupOutEmployee = buildValidationData(row);

				if (validationFails(errorMap, errorList, errorTotal, queryDepartmentCode, groupOutEmployee)) {
					errorTotal++;
					continue;
				}

				OperationEmployeeAttribute operationEmployeeAttribute = new OperationEmployeeAttribute();
				operationEmployeeAttribute.setSiteCode(groupOutEmployee.getSiteCode());
				operationEmployeeAttribute.setEmpCode(groupOutEmployee.getEmpCode());
				operationEmployeeAttribute.setId(outEmployeeDao.buildOperationEmployeeAttributeId());

				outEmployeeDao.deleteOperationEmployeeAttributeById(groupOutEmployee.getEmpCode().split(","));
				outEmployeeDao.insertOperationEmployeeAttribute(operationEmployeeAttribute);
				successTotal++;
			}
			returnMap.put(KEY_SUCCESS, true);
			returnMap.put(KEY_MESSAGE, StringUtil.extractHandlerResult(successTotal, errorTotal));
			if (errorTotal > 0) {
				// 定义校验不通过数据返回文件名
				errorEmployeeAttribute(returnMap, errorMap, errorList);
			}
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			returnMap.put(KEY_SUCCESS, false);
			returnMap.put(KEY_MESSAGE, "导入出错！");
		}
		return returnMap;
	}

	private boolean validationFails(HashMap<Integer, String> errorMap, List<GroupOutEmployee> errorList, int errorTotal, String queryDepartmentCode,
			GroupOutEmployee groupOutEmployee) {
		if (StringUtil.isBlank(groupOutEmployee.getDeptCode())) {
			errorList.add(groupOutEmployee);
			errorMap.put(errorTotal, "网点代码不能为空！");
			return true;
		}

		if (StringUtil.isBlank(groupOutEmployee.getEmpCode())) {
			errorList.add(groupOutEmployee);
			errorMap.put(errorTotal, "员工工号不能为空！");
			return true;
		}

		if (!queryDepartmentCode.equals(groupOutEmployee.getDeptCode())) {
			errorList.add(groupOutEmployee);
			errorMap.put(errorTotal, "网点代码和所选网点不一致！");
			return true;
		}
		return false;
	}

	private GroupOutEmployee buildValidationData(HSSFRow row) {
		String number = Template.getCellStrValue(row.getCell(0));
		String areaCode = Template.getCellStrValue(row.getCell(1));
		String departmentCode = Template.getCellStrValue(row.getCell(2));
		String employeeCode = Template.getCellStrValue(row.getCell(3));
		String employeeName = Template.getCellStrValue(row.getCell(4));
		String postType = Template.getCellStrValue(row.getCell(5));
		String onJobType = Template.getCellStrValue(row.getCell(6));
		String groupCode = Template.getCellStrValue(row.getCell(7));
		String groupName = Template.getCellStrValue(row.getCell(8));
		String siteCode = Template.getCellStrValue(row.getCell(10));
		String workType = Template.getCellStrValue(row.getCell(11));

		GroupOutEmployee groupOutEmployee = new GroupOutEmployee();
		groupOutEmployee.setNumber(number);
		groupOutEmployee.setAreaCode(areaCode);
		groupOutEmployee.setEmpName(employeeName);
		groupOutEmployee.setPostType(postType);
		groupOutEmployee.setOnJobType(onJobType);
		groupOutEmployee.setGroupName(groupName);
		groupOutEmployee.setWorkType(workType);
		groupOutEmployee.setDeptCode(departmentCode);
		groupOutEmployee.setEmpCode(employeeCode);
		groupOutEmployee.setGroupCode(groupCode);
		groupOutEmployee.setSiteCode(siteCode);
		return groupOutEmployee;
	}

	private void errorEmployeeAttribute(HashMap<String, Object> dataMap, HashMap<Integer, String> errorMap, List<GroupOutEmployee> errorList) {
		FileInputStream fileInputStreamForError = null;
		// 生成导入错误的Excel
		try {
			fileInputStreamForError = new FileInputStream(CommonUtil.getReportTemplatePath("场地代码导入模板.xls"));
			HSSFWorkbook errorWorkBook = new HSSFWorkbook(fileInputStreamForError);
			String errorExcelPath = writeErrorEmployeeAttribute(errorWorkBook, errorList, errorMap);
			dataMap.put("downLoadUrl", errorExcelPath);
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("导出文件出错！");
		} finally {
			close(fileInputStreamForError);
		}
	}

	private String writeErrorEmployeeAttribute(HSSFWorkbook errorWorkBook, List<GroupOutEmployee> errorList, HashMap<Integer, String> errorMap) {
		HSSFCellStyle cellStyle = errorWorkBook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		String fileName = "场地代码导入错误数据" + ".xls";
		HSSFSheet sheet = errorWorkBook.getSheetAt(0);
		FileOutputStream errorFos = null;
		HSSFRow row = sheet.getRow(1);
		row.getCell(11).setCellStyle(cellStyle);
		row.getCell(11).setCellValue("校验未通过原因");
		int errorNumber = 1;
		if (errorList != null && errorList.size() > 0) {
			int rowNum = 4; // 操作行
			try {
				for (GroupOutEmployee groupOutEmployee : errorList) {
					HSSFRow errorRow = sheet.createRow(rowNum++);
					buildColumnAndSetData(errorMap, cellStyle, errorNumber, groupOutEmployee, errorRow);
					errorNumber++;
				}
				CommonUtil.setDownloadFileName(fileName);
				errorFos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(OutEmployee.class)));
				errorWorkBook.write(errorFos);
			} catch (Exception e) {
				log.error("error:", e);
				throw new BizException("操作失败！");
			} finally {
				if (errorFos != null) {
					try {
						errorFos.close();
					} catch (Exception e1) {
						log.error("err:", e1);
					}
				}
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	private void buildColumnAndSetData(HashMap<Integer, String> errorMap, HSSFCellStyle cellStyle, int errorNumber, GroupOutEmployee groupOutEmployee,
			HSSFRow errorRow) {
		int column = 0;
		HSSFCell cell0 = errorRow.createCell(column);
		cell0.setCellValue(groupOutEmployee.getNumber());
		cell0.setCellStyle(cellStyle);

		HSSFCell cell1 = errorRow.createCell(++column);
		cell1.setCellValue(groupOutEmployee.getAreaCode());
		cell1.setCellStyle(cellStyle);

		HSSFCell cell2 = errorRow.createCell(++column);
		cell2.setCellValue(groupOutEmployee.getDeptCode());
		cell2.setCellStyle(cellStyle);

		HSSFCell cell3 = errorRow.createCell(++column);
		cell3.setCellValue(groupOutEmployee.getEmpCode());
		cell3.setCellStyle(cellStyle);

		HSSFCell cell4 = errorRow.createCell(++column);
		cell4.setCellValue(groupOutEmployee.getEmpName());
		cell4.setCellStyle(cellStyle);

		HSSFCell cell5 = errorRow.createCell(++column);
		cell5.setCellValue(groupOutEmployee.getPostType());
		cell5.setCellStyle(cellStyle);

		HSSFCell cell6 = errorRow.createCell(++column);
		cell6.setCellValue(groupOutEmployee.getOnJobType());
		cell6.setCellStyle(cellStyle);

		HSSFCell cell7 = errorRow.createCell(++column);
		cell7.setCellValue(groupOutEmployee.getGroupCode());
		cell7.setCellStyle(cellStyle);

		HSSFCell cell8 = errorRow.createCell(++column);
		cell8.setCellValue(groupOutEmployee.getGroupName());
		cell8.setCellStyle(cellStyle);

		HSSFCell cell9 = errorRow.createCell(++column);
		cell9.setCellValue(groupOutEmployee.getSiteCode());
		cell9.setCellStyle(cellStyle);

		HSSFCell cell10 = errorRow.createCell(++column);
		cell10.setCellValue(groupOutEmployee.getWorkType());
		cell10.setCellStyle(cellStyle);

		HSSFCell cell11 = errorRow.createCell(++column);
		cell11.setCellValue(errorMap.get(errorNumber - 1).toString());
		cell11.setCellStyle(cellStyle);
	}
}