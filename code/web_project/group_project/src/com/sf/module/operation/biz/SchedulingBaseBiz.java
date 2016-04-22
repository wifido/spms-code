/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-18     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.biz;

import static com.sf.module.common.domain.Constants.COLUMN_DEPT_CODE;
import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.COLUMN_YM;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.dao.ISchedulingBaseDao;
import com.sf.module.operation.dao.ISchedulingBaseJdbcDao;
import com.sf.module.operation.dao.ISchedulingDao;
import com.sf.module.operation.domain.EntityExport;
import com.sf.module.operation.domain.SchedulingBase;
import com.sf.module.operation.util.CommonUtil;

/**
 * 
 * 班次基础信息管理的业务实现类
 * 
 * @author 632898 李鹏 2014-06-18
 * 
 */
public class SchedulingBaseBiz extends BaseBiz implements ISchedulingBaseBiz {

	/**
	 * 班次基础信息管理的Dao接口
	 */
	private ISchedulingBaseDao schedulingBaseDao;

	private ISchedulingDao schedulingDao;

	/**
	 * 设置班次基础信息管理的Dao接口
	 */
	public void setSchedulingBaseDao(ISchedulingBaseDao schedulingBaseDao) {
		this.schedulingBaseDao = schedulingBaseDao;
	}

	/**
	 * 班次基础信息管理的JdbcDao接口
	 */
	@SuppressWarnings("unused")
	private ISchedulingBaseJdbcDao schedulingBaseJdbcDao;

	/**
	 * 设置班次基础信息管理的JdbcDao接口
	 */
	public void setSchedulingBaseJdbcDao(ISchedulingBaseJdbcDao schedulingBaseJdbcDao) {
		this.schedulingBaseJdbcDao = schedulingBaseJdbcDao;
	}

	/**
	 * 用户拥有权限网点
	 */
	private IOssDepartmentDao ossDepartmentDao;

	public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
		this.ossDepartmentDao = ossDepartmentDao;
	}

	private static final String START_TIME = "START_TIME";
	private static final String END_TIME = "END_TIME";
	private static final int ONE_DAY = 2400;

	/**
	 * 保存班次基础信息
	 * 
	 * @date 2014-06-18
	 * @author 632898 李鹏
	 */
	@SuppressWarnings("rawtypes")
	public boolean saveScheduling(HashMap map) {
		SchedulingBase schedulingBase = new SchedulingBase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start1Time = "";
		String end1Time = "";

		String start2Time = "";
		String end2Time = "";

		String end3Time = "";
		String ym  = null != map.get("ym") ? map.get("ym").toString() : "";
		
		if (StringUtil.isNotBlank(ym) && DateUtil.validConfirmDate(ym)) {
			if (DateUtil.isBeOverdue(ym))
				return false;
		} else {
			return false;
		}
		
		// 班别代码
		String scheduleCode = null != map.get("scheduleCode") ? map.get("scheduleCode").toString() : "";
//		if (validateScheduleCode(scheduleCode)) {
			schedulingBase.setScheduleCode(scheduleCode);
//		} else {
//			return false;
//		}
		// 班别名称
		if (null != map.get("scheduleName")) {
			schedulingBase.setScheduleName(map.get("scheduleName").toString());
		} else {
			return false;
		}
		// 开始时间一
		if (null != map.get("start1Time")) {
			start1Time = map.get("start1Time").toString();
			schedulingBase.setStart1Time(start1Time);
		} else {
			return false;
		}
		// 结束时间一
		if (null != map.get("end1Time")) {
			end1Time = map.get("end1Time").toString();
			schedulingBase.setEnd1Time(end1Time);
		} else {
			return false;
		}

		if (StringUtil.isNotBlank(map.get("start2Time").toString()) && StringUtil.isNotBlank(map.get("end2Time").toString())) {
			start2Time = map.get("start2Time").toString();
			end2Time = map.get("end2Time").toString();
			if (sizeComparison(start1Time, end1Time)) {
				if (sizeComparison(end2Time, start1Time)) {
					return false;
				}
			}
		}
		
		if (StringUtil.isNotBlank(map.get("start3Time").toString()) && StringUtil.isNotBlank(map.get("end3Time").toString())) {
			end3Time = map.get("end3Time").toString();
			if ((sizeComparison(start1Time, end1Time) || sizeComparison(start2Time, end2Time))
					&& (sizeComparison(end3Time, start1Time) || sizeComparison(end3Time, start2Time))) {
				return false;
			}
		}
		
		// 开始时间二
		schedulingBase.setStart2Time(map.get("start2Time") == null ? "" : map.get("start2Time").toString());
		// 结束时间二
		schedulingBase.setEnd2Time(map.get("end2Time") == null ? "" : map.get("end2Time").toString());
		// 开始时间三
		schedulingBase.setStart3Time(map.get("start3Time") == null ? "" : map.get("start3Time").toString());
		// 结束时间三
		schedulingBase.setEnd3Time(map.get("end3Time") == null ? "" : map.get("end3Time").toString());

		List<HashMap<String, String>> list = constructionTimeList(schedulingBase.getStart1Time(), schedulingBase.getEnd1Time(), schedulingBase.getStart2Time(),
				schedulingBase.getEnd2Time(), schedulingBase.getStart3Time(), schedulingBase.getEnd3Time());
		if (!validTime(list))
			return false;

		Date date = new Date();
		// 创建时间
		schedulingBase.setCreateTm(date);
		
		schedulingBase.setModifiedTm(date);
		long userId = super.getUserId();
		// 创建人工号
		schedulingBase.setCreateEmpCode(Long.toString(userId));
		// 网点ID
		schedulingBase.setDeptId(Long.parseLong(map.get("deptId").toString()));
		
		// 班别类型
		schedulingBase.setClassType("1");
		
		schedulingBase.setYm(ym);
		// 执行保存
		schedulingBaseDao.save(schedulingBase);
		return true;
	}

	/*private boolean validateScheduleCode(String scheduleCode) {
		return scheduleCode.startsWith("W") || scheduleCode.startsWith("B") || scheduleCode.startsWith("Z");
	}*/

	/**
	 * 查询班次基础信息
	 * 
	 * @date 2014-06-18
	 * @author 632898 李鹏
	 */
	@SuppressWarnings("rawtypes")
	public HashMap querySchedule(HashMap map) {
		long userId = super.getUserId();
		map.put("userId", userId);
		map.put("flag", "querySchedule");
		HashMap returnMap = schedulingBaseDao.querySchedule(map);
		return returnMap;
	}

	/**
	 * 更新班次基础信息
	 * 
	 * @date 2014-06-19
	 * @author 632898 李鹏
	 */
	@SuppressWarnings("rawtypes")
	public boolean updateSchedule(HashMap map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 判断ID是否为空
		if (StringUtils.isEmpty(map.get("scheduleID")))
			return false;
		// 查询需更新的数据
		SchedulingBase schedulingBase = schedulingBaseDao.load(Long.parseLong(map.get("scheduleID").toString()));
		// 修改时间
		Date date = new Date();
		schedulingBase.setModifiedTm(date);
		// 创建人工号
		long userId = super.getUserId();
		schedulingBase.setModifiedEmpCode(Long.toString(userId));

		schedulingBase.setClassType("1");
		// 执行更新
		schedulingBaseDao.update(schedulingBase);
		return true;
	}

	/**
	 * 删除班次基础信息
	 * 
	 * @date 2014-06-19
	 * @author 632898 李鹏
	 */
	@SuppressWarnings("rawtypes")
	public boolean deleteSchedule(HashMap map) {
		// 判断ID是否为空
		if (StringUtils.isEmpty(map.get("ids")))
			return false;
		// 执行删除
		String[] ids = map.get("ids").toString().split("@@");
		for (String id : ids) {
			if (!StringUtils.isEmpty(id))
				schedulingBaseDao.remove(Long.parseLong(id));
		}
		return true;
	}

	/**
	 * 判断班次是否被使用
	 * 
	 * @author 632898 李鹏
	 * @param 班别Id
	 * @date 2014-07-18
	 */
	@SuppressWarnings("all")
	public Map isScheduling(JSONArray jsonList) {
		Map map = new HashMap();
		List list = new ArrayList();
		for (int i = 0; i < jsonList.size(); i++) {
			JSONObject jsonObj = jsonList.getJSONObject(i);
			if (null != jsonObj) {
				Long deptId = jsonObj.getLong("deptId");
				String code = jsonObj.getString("scheduleCode");
				String ym = jsonObj.getString("ym");
				if (schedulingDao.findById(code, deptId, ym)) {
					list.add(deptId + code);
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

	public void setSchedulingDao(ISchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

	public List<SchedulingBase> getScheBaseList(Long deptid, String ym) {
		return this.schedulingBaseJdbcDao.getScheBaseList(deptid, ym);
	}

	/**
	 * 导出班次基础信息
	 * 
	 * @date 2014-06-20
	 * @author 632898 李鹏
	 */
	@SuppressWarnings({ "rawtypes", "unused", "static-access" })
	public HashMap exportSchedule(HashMap map) {
		// Excel信息存储容器
		HashMap columnMap = new HashMap();
		// 返回结果容器
		HashMap returnMap = new HashMap();
		HashMap dataMap = new HashMap();
		try {
			// 查询需要导出的数据
			long userId = super.getUserId();
			map.put("userId", userId);
			map.put("flag", "exportSchedule");
			dataMap = this.schedulingBaseDao.querySchedule(map);
			// 定义导出的文件名
			String downloadFileName = String.format("班别信息导出_%s.xls", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
			// 设置下载的文件名
			CommonUtil.setDownloadFileName(downloadFileName);
			// 获取导出文件写入路径
			String exportFileName = CommonUtil.getSaveFilePath(SchedulingBase.class);
			File file = new File(exportFileName);
			// 判断文件是否存在
			if (!file.exists())
				file.createNewFile();
			// 读取模板文件流
			FileInputStream template = new FileInputStream(CommonUtil.getReportTemplatePath("班别导出.xls"));
			// 获取模板文件的WorkBook
			HSSFWorkbook workbook = new HSSFWorkbook(template);
			// 读取模板文件的第一页
			Sheet sheet = workbook.getSheetAt(0);
			// 获取模板文件的列数据
			columnMap = new EntityExport().getColumnMap(workbook, columnMap, 0, 2);
			// 获取写入Excel的List容器数据
			List list = (List) dataMap.get(ROOT);
			// 把数据写入Excel
			workbook = new EntityExport().writeWorkBook(workbook, 0, 4, columnMap, list, true);
			// 获取导出文件流
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			// 把数据流写入文件
			workbook.write(fos);
			fos.close();
			template.close();
			// 返回下载路径
			returnMap.put("fileName", CommonUtil.getReturnPageFileName());
		} catch (Exception e) {
			log.error(KEY_ERROR, e);
			returnMap.put(KEY_SUCCESS, false);
			returnMap.put(KEY_MESSAGE, "导出失败！");
			return returnMap;
		}
		return returnMap;
	}

	private boolean isEmptyRow(HSSFRow row, HashMap columnMap) {
		// 中转场代码
		int cellDeptCode = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
		boolean deptCode = isBlank(row, cellDeptCode);

		// 班别代码
		int cellScheduleCode = Integer.parseInt(columnMap.get("SCHEDULE_CODE").toString());
		boolean scheduleCode = isBlank(row, cellScheduleCode);

		// 开始时间
		int cellStart1Time = Integer.parseInt(columnMap.get("START1_TIME").toString());
		boolean start1Time = isBlank(row, cellStart1Time);

		// 结束时间
		int cellEnd1Time = Integer.parseInt(columnMap.get("END1_TIME").toString());
		boolean end1Time = isBlank(row, cellEnd1Time);

		return deptCode && scheduleCode && start1Time && end1Time;
	}

	private boolean isBlank(HSSFRow row, int index) {
		if (null == row.getCell(index)) {
			return true;
		}
		row.getCell(index).setCellType(HSSFCell.CELL_TYPE_STRING);
		return StringUtil.isBlank(row.getCell(index).toString().trim());
	}

	/**
	 * 导入班次基础信息
	 * 
	 * @date 2014-06-20
	 * @author 632898 李鹏
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public HashMap importSchedule(HashMap map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 列数据容器
		HashMap columnMap = new HashMap();
		// 返回数据容器
		HashMap datamap = new HashMap();
		// 验证错误的数据容器
		HashMap errorMap = new HashMap();
		try {
			// 获取上传的文件
			File file = (File) map.get("file");
			long deptId = Long.parseLong((String) map.get("deptId"));
			// 读取文件流
			FileInputStream is = new FileInputStream(file);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			// 获取文件流WorkBook
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 关闭文件流
			is.close();
			// 获取数据页
			HSSFSheet sheet = wb.getSheetAt(0);
			// 获取数据总条数
			int rowNumber = sheet.getLastRowNum();
			// 获取Excel列数据
			columnMap = new EntityExport().getColumnMap(wb, columnMap, 0, 2);
			// 获取Excel列数据对应的实体类数据
			HashMap EntityMap = new EntityExport().getColumnMap(wb, columnMap, 0, 3);
			// 导入成功数和失败数
			int successCount = 0, errorCount = 0;
			// 验证导入的数据量
			if (rowNumber >= 1004) {
				datamap.put(KEY_SUCCESS, false);
				datamap.put(KEY_MESSAGE, "导入数据超过1000条！");
				return datamap;
			}
			// 遍历Excel数据
			for (int i = 4; i <= rowNumber; i++) {
				HSSFRow row = sheet.getRow(i);
				if (isEmptyRow(row, columnMap)) {
					continue;
				}
				// 校验数据并返回错误信息
				Map<String, String> validMap = validRow(row, columnMap, deptId);
				// 判断校验是否通过
				if (validMap.get(KEY_SUCCESS).equals("false")) {
					errorMap.put(String.valueOf(row.getRowNum()), validMap.get(KEY_MESSAGE));
					errorCount++;
					continue;
				}
				SchedulingBase schedulingBase = new SchedulingBase();
				// 获取班别信息实体类方法数组
				Method[] methods = schedulingBase.getClass().getMethods();
				// 遍历获取所有的set方法
				for (Method m : methods) {
					String methodName = m.getName();
					// 判断是否需要set数据
					if (EntityMap.get(methodName) != null) {
						int cellIndex = Integer.parseInt(columnMap.get(methodName).toString());
							m.invoke(schedulingBase, row.getCell(cellIndex).getStringCellValue());
					}
				}
				// 获取网点代码
				int deptCodeCellIndex = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
				String departmentCode = row.getCell(deptCodeCellIndex).getStringCellValue();
				// 创建时间
				Date date = new Date();
				schedulingBase.setCreateTm(date);
				schedulingBase.setModifiedTm(date);
				// 创建人工号
				long userId = super.getUserId();
				schedulingBase.setCreateEmpCode(Long.toString(userId));
				// 网点ID
				schedulingBase.setDeptId(DepartmentCacheBiz.getDepartmentByCode(departmentCode).getId());
				schedulingBase.setClassType("1");
				String scheduleName = null;
				String deptCode = DepartmentCacheBiz.getDepartmentByID(schedulingBase.getDeptId()).getDeptCode();
				String scheduleCode = schedulingBase.getScheduleCode();
				String start1Time = schedulingBase.getStart1Time();
				String end3Time = schedulingBase.getEnd3Time();
				String end2Time = schedulingBase.getEnd2Time();
				String end1Time = schedulingBase.getEnd1Time();
				if (!StringUtils.isEmpty(deptCode) && !StringUtils.isEmpty(scheduleCode) && !StringUtils.isEmpty(start1Time)) {
					String time1 = start1Time.replace(":", "");
					scheduleName = deptCode + "-" + scheduleCode + "-" + time1;
					if (!StringUtils.isEmpty(end3Time)) {
						String time3 = end3Time.replace(":", "");
						scheduleName = scheduleName + "-" + time3;
					} else if (!StringUtils.isEmpty(end2Time)) {
						String time2 = end2Time.replace(":", "");
						scheduleName = scheduleName + "-" + time2;
					} else {
						String time = end1Time.replace(":", "");
						scheduleName = scheduleName + "-" + time;
					}
				}
				schedulingBase.setScheduleName(scheduleName);
				// 保存导入的数据
				schedulingBaseDao.save(schedulingBase);
				successCount++;
			}
			// 返回导入信息
			datamap.put(KEY_SUCCESS, true);
			datamap.put(KEY_MESSAGE, StringUtil.extractHandlerResult(successCount, errorCount));
			// 若有导入失败的则把错误数据写入Excel中
			if (errorCount > 0) {
				// 定义导入失败返回的Excel名称
 				String downloadFileName = String.format("班别信息导入错误信息_%s.xls", new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
				// 把错误数据写入Excel中并获取下载路径
				String downLoadUrl = new EntityExport().errorWorkBook(wb, errorMap, 0, 4, columnMap, downloadFileName, "班别导出.xls", 11);
				datamap.put("downLoadUrl", downLoadUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(KEY_ERROR, e);
			datamap.put(KEY_SUCCESS, false);
			datamap.put(KEY_MESSAGE, "导入数据出错！");
			return datamap;
		}
		return datamap;
	}

	/**
	 * 校验导入数据
	 * 
	 * @author 632898 李鹏
	 * @date 2014-06-23
	 * @param row
	 * @param columnMap
	 * @return
	 */
	@SuppressWarnings("unused")
	public Map validRow(HSSFRow row, HashMap columnMap, Long deptId) {
		Map<String, String> validMap = new HashMap<String, String>();
		StringBuffer errorMsg = new StringBuffer();
		try {
			int cellYm = Integer.parseInt(columnMap.get(COLUMN_YM).toString());
			if (null == row.getCell(cellYm))
				row.createCell(cellYm);
			String ym = row.getCell(cellYm).getStringCellValue();
			if (DateUtil.validConfirmDate(ym)) {
				if (DateUtil.isBeOverdue(ym))
					errorMsg.append("已逾期，不能导入该月班别！");
			} else {
				errorMsg.append("最多支持上个月、当前月以及下个月的班别导入！");
			}
			// 校验中转场代码是否正确
			int cellDeptCode = Integer.parseInt(columnMap.get(COLUMN_DEPT_CODE).toString());
			if (null == row.getCell(cellDeptCode))
				row.createCell(cellDeptCode);
			String deptCode = row.getCell(cellDeptCode).getStringCellValue();
			Long getdeptId = DepartmentCacheBiz.getDepartmentByCode(deptCode).getId();
			if (!deptId.equals(getdeptId)) {
				errorMsg.append("当前选择网点和excel中的网点不一致;");
			}
			Long userId = super.getUserId();
			List<OssDepartment> departList = ossDepartmentDao.getDeptByUserId(userId, getdeptId);
			if (StringUtils.isEmpty(deptCode)) {
				errorMsg.append("网点代码不能为空；");
			} else if (DepartmentCacheBiz.getDepartmentByCode(deptCode) == null) {
				errorMsg.append("该网点代码不存在；");
			} else if (departList == null || departList.size() == 0) {
				errorMsg.append("没有该网点权限;");
			}
			// 班别代码
			int cellScheduleCode = Integer.parseInt(columnMap.get("SCHEDULE_CODE").toString());
			if (null == row.getCell(cellScheduleCode))
				row.createCell(cellScheduleCode);
			row.getCell(cellScheduleCode).setCellType(HSSFCell.CELL_TYPE_STRING);
			String scheduleCode = row.getCell(cellScheduleCode).getStringCellValue();
			if (StringUtils.isEmpty(scheduleCode)) {
				errorMsg.append("班别代码不能为空；");
			} else if (DepartmentCacheBiz.getDepartmentByCode(deptCode) != null) {
				getdeptId = DepartmentCacheBiz.getDepartmentByCode(deptCode).getId();
				/*if (!validateScheduleCode(scheduleCode)) {
					errorMsg.append("班别代码设置错误,请以B、W、Z开头；");
				}*/
				if (scheduleCode.length() > 5)
					errorMsg.append("班别代码超出五个字符；");
				Pattern pt = Pattern.compile("^[A-Za-z0-9]+$");
				Matcher m = pt.matcher(scheduleCode);
				if (!m.matches())
					errorMsg.append("班别代码只能由字母和数字组合；");
				Pattern pattern = Pattern.compile("[0-9]*");
				if (pattern.matcher(scheduleCode).matches())
					errorMsg.append("班别代码只能由字母和数字组合；");
				HashMap h = new HashMap();
				h.put("scheduleCode", scheduleCode);
				h.put("deptId", getdeptId);
				h.put("flag", "querySchedule");
				userId = super.getUserId();
				h.put("userId", userId);
				h.put("ym", ym);
				HashMap hashMap = schedulingBaseDao.querySchedule(h);
				int count = Integer.parseInt(hashMap.get("totalSize").toString());
				if (count > 0)
					errorMsg.append("班别代码在该网点内已存在；");
			}
			DateFormat df = new SimpleDateFormat("HH:mm");
			Pattern pt = Pattern.compile("([0-1]?[0-9]|2[0-3]):[0-5][0-9]");
			// 班别开始时间1
			int cellStart1Time = Integer.parseInt(columnMap.get("START1_TIME").toString());
			if (null == row.getCell(cellStart1Time))
				row.createCell(cellStart1Time);
			row.getCell(cellStart1Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String start1Time = row.getCell(cellStart1Time).getStringCellValue();
			if (StringUtils.isEmpty(start1Time)) {
				errorMsg.append("班别开始时间1不能为空；");
			} else {
				try {
					Matcher m = pt.matcher(start1Time);
					if (m.matches())
						df.parse(start1Time);
					else
						errorMsg.append("班别开始时间1填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别开始时间1填写格式不正确，正确格式为15:00；");
				}
			}
			// 班别结束时间1
			int cellEnd1Time = Integer.parseInt(columnMap.get("END1_TIME").toString());
			if (null == row.getCell(cellEnd1Time))
				row.createCell(cellEnd1Time);
			row.getCell(cellEnd1Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String end1Time = row.getCell(cellEnd1Time).getStringCellValue();
			if (StringUtils.isEmpty(end1Time)) {
				errorMsg.append("班别结束时间1不能为空；");
			} else {
				try {
					Matcher m = pt.matcher(end1Time);
					if (m.matches())
						df.parse(end1Time);
					else
						errorMsg.append("班别结束时间1填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别结束时间1填写格式不正确，正确格式为15:00；");
				}
			}
			// 班别开始时间2
			int cellStart2Time = Integer.parseInt(columnMap.get("START2_TIME").toString());
			if (null == row.getCell(cellStart2Time))
				row.createCell(cellStart2Time);
			row.getCell(cellStart2Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String start2Time = row.getCell(cellStart2Time).getStringCellValue();
			if (!StringUtils.isEmpty(start2Time)) {// 不为空
				try {
					Matcher m = pt.matcher(start2Time);
					if (m.matches())
						df.parse(start2Time);
					else
						errorMsg.append("班别开始时间2填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别开始时间2填写格式不正确，正确格式为15:00；");
				}
			}
			// 班别结束时间2
			int cellEnd2Time = Integer.parseInt(columnMap.get("END2_TIME").toString());
			if (null == row.getCell(cellEnd2Time))
				row.createCell(cellEnd2Time);
			row.getCell(cellEnd2Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String end2Time = row.getCell(cellEnd2Time).getStringCellValue();
			if (!StringUtils.isEmpty(end2Time)) {// 不为空
				if (StringUtils.isEmpty(start2Time)) {
					errorMsg.append("班别开始时间2未填写；");
				}
				try {
					Matcher m = pt.matcher(end2Time);
					if (m.matches())
						df.parse(end2Time);
					else
						errorMsg.append("班别结束时间2填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别结束时间2填写格式不正确，正确格式为15:00；");
				}
				if ((sizeComparison(start1Time, end1Time) && (sizeComparison(start2Time, start1Time) || sizeComparison(end2Time, start1Time)))
						|| (sizeComparison(start2Time, end2Time) && (sizeComparison(start1Time, start2Time) || sizeComparison(end1Time, start2Time)))) {
					errorMsg.append("班别时间跨度超过24小时；");
				}

			} else if (!StringUtils.isEmpty(start2Time)) {
				errorMsg.append("班别结束时间2未填写；");
			}
			// 班别开始时间3
			int cellStart3Time = Integer.parseInt(columnMap.get("START3_TIME").toString());
			if (null == row.getCell(cellStart3Time))
				row.createCell(cellStart3Time);
			row.getCell(cellStart3Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String start3Time = row.getCell(cellStart3Time).getStringCellValue();
			if (!StringUtils.isEmpty(start3Time)) {// 不为空
				try {
					Matcher m = pt.matcher(start3Time);
					if (m.matches())
						df.parse(start3Time);
					else
						errorMsg.append("班别开始时间3填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别开始时间3填写格式不正确，正确格式为15:00；");
				}
			}
			// 班别结束时间3
			int cellEnd3Time = Integer.parseInt(columnMap.get("END3_TIME").toString());
			if (null == row.getCell(cellEnd3Time))
				row.createCell(cellEnd3Time);
			row.getCell(cellEnd3Time).setCellType(HSSFCell.CELL_TYPE_STRING);
			String end3Time = row.getCell(cellEnd3Time).getStringCellValue();
			if (!StringUtils.isEmpty(end3Time)) {// 不为空
				if (StringUtils.isEmpty(start3Time)) {
					errorMsg.append("班别开始时间3未填写；");
				}
				try {
					Matcher m = pt.matcher(end3Time);
					if (m.matches())
						df.parse(end3Time);
					else
						errorMsg.append("班别结束时间3填写格式不正确，正确格式为15:00；");
				} catch (Exception e) {
					errorMsg.append("班别结束时间3填写格式不正确，正确格式为15:00；");
				}
				if (timeSpanGreaterthan24(start1Time, end1Time, start2Time, end2Time, start3Time, end3Time)) {
					errorMsg.append("班别时间跨度超过24小时；");
				}

			} else if (!StringUtils.isEmpty(start3Time)) {
				errorMsg.append("班别结束时间3未填写；");
			}
			List<HashMap<String, String>> list = constructionTimeList(start1Time, end1Time, start2Time, end2Time, start3Time, end3Time);
			if (!validTime(list)) {
				errorMsg.append("班别时间冲突！");
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

	private boolean timeSpanGreaterthan24(String start1Time, String end1Time, String start2Time, String end2Time, String start3Time, String end3Time) {
		return (sizeComparison(start1Time, end1Time) && (sizeComparison(start2Time, start1Time) || sizeComparison(end2Time, start1Time)
				|| sizeComparison(start3Time, start1Time) || sizeComparison(end3Time, start1Time)))
				|| (sizeComparison(start2Time, end2Time) && (sizeComparison(start1Time, start2Time) || sizeComparison(end1Time, start2Time)
						|| sizeComparison(start3Time, start2Time) || sizeComparison(end3Time, start2Time)))
				|| (sizeComparison(start3Time, end3Time) && (sizeComparison(start1Time, start3Time) || sizeComparison(end1Time, start3Time)
						|| sizeComparison(start2Time, start3Time) || sizeComparison(end2Time, start3Time)));
	}

	private boolean sizeComparison(String start1Time, String end1Time) {
		return Integer.parseInt(start1Time.replace(":", "")) > Integer.parseInt(end1Time.replace(":", ""));
	}

	private List<HashMap<String, String>> constructionTimeList(String start1Time, String end1Time, String start2Time, String end2Time, String start3Time,
			String end3Time) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(START_TIME, start1Time);
		map.put(END_TIME, end1Time);
		list.add(map);
		if (StringUtil.isNotBlank(start2Time)) {
			map = new HashMap<String, String>();
			map.put(START_TIME, start2Time);
			map.put(END_TIME, end2Time);
			list.add(map);
		}
		if (StringUtil.isNotBlank(start3Time)) {
			map = new HashMap<String, String>();
			map.put(START_TIME, start3Time);
			map.put(END_TIME, end3Time);
			list.add(map);
		}
		return list;
	}

	private boolean enableDateError(Date dEnableDt, Date currentDate) {
		return dEnableDt.getTime() < currentDate.getTime();
	}

	public static boolean validTime(List<HashMap<String, String>> list) {
		HashMap<String, String> classDataMap;
		for (int i = 0; i < list.size(); i++) {
			classDataMap = (HashMap<String, String>) list.get(i);
			Integer currentBeginTime = formatConversion(classDataMap, START_TIME);
			Integer currentEndTime = formatConversion(classDataMap, END_TIME);
			if (aPeriodOfTimeTheSameTime(currentBeginTime, currentEndTime)) {
				return false;
			}
			for (int j = 0; j < list.size(); j++) {
				classDataMap = (HashMap<String, String>) list.get(j);
				Integer contrastBeginTime = formatConversion(classDataMap, START_TIME);
				Integer contrastEndTime = formatConversion(classDataMap, END_TIME);

				if (i != j) {
					if (manyPeriodOfTimeTheSameTime(currentBeginTime, currentEndTime, contrastBeginTime, contrastEndTime)) {
						return false;
					}
					if (contrastBeginTime > contrastEndTime) {
						contrastBeginTime = contrastBeginTime - ONE_DAY;
					}
					if (currentBeginTime > currentEndTime) {
						currentBeginTime = currentBeginTime - ONE_DAY;
					}
					if (!(currentBeginTime > contrastEndTime || currentEndTime < contrastBeginTime)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static boolean aPeriodOfTimeTheSameTime(Integer beginTime, Integer endTime) {
		return beginTime - endTime == 0;
	}

	private static boolean manyPeriodOfTimeTheSameTime(Integer currentBeginTime, Integer currentEndTime, Integer contrastBeginTime, Integer contrastEndTime) {
		return aPeriodOfTimeTheSameTime(currentBeginTime, currentEndTime) || aPeriodOfTimeTheSameTime(currentBeginTime, contrastBeginTime)
				|| aPeriodOfTimeTheSameTime(currentBeginTime, contrastEndTime) || aPeriodOfTimeTheSameTime(currentEndTime, contrastBeginTime)
				|| aPeriodOfTimeTheSameTime(currentEndTime, contrastEndTime) || aPeriodOfTimeTheSameTime(contrastBeginTime, contrastEndTime);
	}

	private static int formatConversion(HashMap<String, String> warehouseClassDataMap, String key) {
		return Integer.parseInt(warehouseClassDataMap.get(key).toString().replace(":", ""));
	}

}