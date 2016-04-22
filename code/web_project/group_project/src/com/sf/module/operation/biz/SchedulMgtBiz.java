/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.Clock;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.dispatch.domain.SchedulingForDispatch;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.dao.IMonthConfirmStatusDao;
import com.sf.module.operation.dao.ISchedulMgtDao;
import com.sf.module.operation.dao.ISchedulMgtJdbcDao;
import com.sf.module.operation.dao.ISchedulingDao;
import com.sf.module.operation.dao.ISchedulingJdbcDao;
import com.sf.module.operation.dao.OutEmployeeDao;
import com.sf.module.operation.dao.SchedulMgtHistoryDao;
import com.sf.module.operation.domain.GroupBaseInfo;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.domain.SchedulMgtHistory;
import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.domain.SchedulingBase;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.organization.domain.Department;
import com.sf.module.report.biz.SchedulingModifyBiz;

/**
 * 
 * 排班管理的业务实现类
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public class SchedulMgtBiz extends BaseBiz implements ISchedulMgtBiz {

	/**
	 * 排班管理的Dao接口
	 */
	private ISchedulMgtDao schedulMgtDao;
	private ISchedulingDao schedulingDao;
	private ISchedulMgtJdbcDao schedulMgtJdbcDao;
	private ISchedulingJdbcDao schedulingJdbcDao;
	private IMonthConfirmStatusDao monthConfirmStatusDao;
	private SchedulingModifyBiz schedulingModifyBiz;
	@Resource
	private OutEmployeeDao outEmployeeDao;
	@Resource
	private SchedulMgtHistoryDao schedulMgtHistoryDao;
	private ISendMailBiz sendMailBiz;
	private final int maxPageSize = 60000;
	private static final int commitStatus = 0;
	private static final int synchroStatus = 0;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	private static String titles[] = new String[] { "第1天", "第2天", "第3天", "第4天",
			"第5天", "第6天", "第7天", "第8天", "第9天", "第10天", "第11天", "第12天", "第13天",
			"第14天", "第15天", "第16天", "第17天", "第18天", "第19天", "第20天", "第21天",
			"第22天", "第23天", "第24天", "第25天", "第26天", "第27天", "第28天", "第29天",
			"第30天", "第31天" };
	private static final String START_TIME = "START_TIME";
	private static final String END_TIME = "END_TIME";
	private final static int FOUR_PER_MONTH = 400;
	private final static int AT_EIGHT_PM = 20;
	private static String downloadFileName = String.format("已确认提交信息导出_%s.xls",
			new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

	/**
	 * 设置排班管理的Dao接口
	 */
	public void setSchedulMgtDao(ISchedulMgtDao schedulMgtDao) {
		this.schedulMgtDao = schedulMgtDao;
	}

	public void setSchedulingDao(ISchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

	public void setSchedulMgtJdbcDao(ISchedulMgtJdbcDao schedulMgtJdbcDao) {
		this.schedulMgtJdbcDao = schedulMgtJdbcDao;
	}

	public void setSchedulingJdbcDao(ISchedulingJdbcDao schedulingJdbcDao) {
		this.schedulingJdbcDao = schedulingJdbcDao;
	}

	public void setMonthConfirmStatusDao(
			IMonthConfirmStatusDao monthConfirmStatusDao) {
		this.monthConfirmStatusDao = monthConfirmStatusDao;
	}

	public void setSchedulingModifyBiz(SchedulingModifyBiz schedulingModifyBiz) {
		this.schedulingModifyBiz = schedulingModifyBiz;
	}

	public void setSendMailBiz(ISendMailBiz sendMailBiz) {
		this.sendMailBiz = sendMailBiz;
	}

	// 获取排班是否完成
	public boolean searchNoticesCount() {// TRUE-完成 false-有未完成
		Long deptid = this.getCurrentUser().getEmployee().getDepartment().getId();
		String ym = CommonUtil.getNextMonthYm(new Date());
		return schedulMgtJdbcDao.allEmpIsHasSche(deptid, ym) && schedulMgtJdbcDao.allScheIsFinished(deptid, ym);
	}

	public boolean getCanConfirm(Long deptid, String ym) {// TRUE-完成 false-有未完成
		if (ym.equals(CommonUtil.getNextMonthYm(new Date()))
				|| ym.equals(CommonUtil.getYm(new Date()))
				|| ym.equals(CommonUtil.getLastMonthYm(new Date()))) {
			return schedulMgtJdbcDao.allEmpIsHasSche(deptid, ym)
					&& schedulMgtJdbcDao.allScheIsFinished(deptid, ym);
		} else {
			throw new BizException("提交确认只能选择上个月、当前月或下个月！");
		}
	}

	// 查询
	public IPage<SchedulMgt> findPage(ScheduleDto dto, int pageSize,
			int pageIndex) {
		return schedulMgtJdbcDao.findPage(this.getCurrentUser().getId(), dto,
				pageSize, pageIndex);
	}

	// 导出
	public String getExcel(ScheduleDto dto) {

		// 根据条件获取总记录数
		int ct = schedulMgtJdbcDao.getMgtCount(this.getCurrentUser().getId(),
				dto);
		// if ((Integer) map.get("totalSize") > 60000)
		// 分sheet页导出
		int sheetCt = 0;
		sheetCt = getSheetCount(ct);

		HSSFWorkbook workbook = null;
		String fileName = String.format("排班信息导出_%s.xls", new SimpleDateFormat(
				"yyyyMMdd_HHmmss").format(new Date()));
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(
					CommonUtil.getReportTemplatePath("排班导出.xls"));
			workbook = new HSSFWorkbook(fis);
			HSSFSheet[] sheetArray = new HSSFSheet[sheetCt];
			HSSFSheet sh = workbook.getSheetAt(0);
			HSSFRow r = sh.getRow(0);
			r.getCell(2).setCellValue(
					DepartmentCacheBiz.getDepartmentByID(dto.getDeptId()).getDeptCode());
			r.getCell(4).setCellValue(dto.getYm());
			sheetArray[0] = sh;

			for (int k = 1; k < sheetArray.length; k++) {
				sheetArray[k] = workbook.cloneSheet(0);
			}

			for (int i = 0; i < sheetCt; i++) {
				HSSFSheet sheet = sheetArray[i];
				int rowNum = 1; // 操作行
				/*
				 * IPage<SchedulMgt> confirmPage =
				 * schedulMgtJdbcDao.schedulMgtConfirmImport(dto, maxPageSize,
				 * i); rowNum = writeExcel(cellStyle, sheet, rowNum,
				 * confirmPage, true);
				 */
				IPage<SchedulMgt> page = schedulMgtJdbcDao.findPage(this.getCurrentUser().getId(), dto, maxPageSize, i);
				rowNum = writeExcel(workbook, sheet, rowNum, page, false);
			}
			CommonUtil.setDownloadFileName(fileName);
			fos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(SchedulMgt.class)));
			workbook.write(fos);
		} catch (Exception e) {
			log.error("throw Exception:", e);
			throw new BizException("导出失败!");
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	private int writeExcel(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum,
			IPage<SchedulMgt> page, boolean isConfirm) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Collection<SchedulMgt> data = page.getData();
		HSSFCellStyle cellStyle = getGeneralStyle(workbook);

		HSSFCellStyle greyColorCellStyle = getGeneralStyle(workbook);
		HSSFPalette greyPalette = workbook.getCustomPalette(); 
		greyPalette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 192, (byte) 192, (byte) 192);
		greyColorCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		greyColorCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

		HSSFCellStyle redcolorCellStyle = getGeneralStyle(workbook);
		HSSFPalette redPalette = workbook.getCustomPalette(); 
		redPalette.setColorAtIndex(HSSFColor.RED.index, (byte) 232, (byte) 84, (byte) 84);
		redcolorCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		redcolorCellStyle.setFillForegroundColor(HSSFColor.RED.index);

		HSSFCellStyle bluecolorCellStyle = getGeneralStyle(workbook);
		HSSFPalette bulePalette = workbook.getCustomPalette(); 
		bulePalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 151, (byte) 213, (byte) 255);
		bluecolorCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		bluecolorCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);


		if (data != null && data.size() > 0 && sheet != null) {
			for (SchedulMgt mgt : data) {
				int column = 0;
				HSSFRow row = sheet.createRow(++rowNum);
				// 地区
				HSSFCell cell1 = row.createCell(column++);
				cell1.setCellValue(mgt.getAreaCode() == null ? "" : mgt.getAreaCode());
				cell1.setCellStyle(cellStyle);
				// 网点
				HSSFCell cell2 = row.createCell(column++);
				cell2.setCellValue(mgt.getDeptCode() == null ? "" : mgt.getDeptCode());
				cell2.setCellStyle(cellStyle);
				// 工号
				HSSFCell cell3 = row.createCell(column++);
				cell3.setCellValue(mgt.getEmpCode() == null ? "" : mgt.getEmpCode());
				cell3.setCellStyle(cellStyle);
				// 姓名
				HSSFCell cell4 = row.createCell(column++);
				cell4.setCellValue(mgt.getEmpName() == null ? "" : mgt.getEmpName());
				cell4.setCellStyle(cellStyle);
				
				// 用工类型(1-非全日制工、2-基地见习生、3-劳务派遣、4-全日制员工、5-实习生、6-外包)
				HSSFCell cell5 = row.createCell(column++);
				cell5.setCellValue(mgt.getWorkTypeStr());
				cell5.setCellStyle(cellStyle);
				
				HSSFCell cell8 = row.createCell(column++);
				cell8.setCellValue(mgt.getEmpDutyName() == null ? "" : mgt.getEmpDutyName());
				cell8.setCellStyle(cellStyle);

				Calendar cal = Calendar.getInstance();
				String dimissionDt = mgt.getDimissionDt();
				String dateFrom = mgt.getDateFrom();
				String transferDate = mgt.getTransferDate();
				String LastZno = mgt.getLastZno();
				String empPostType = mgt.getEmpPostType();
				String sfDate = mgt.getSfDate();
				try {
					for (int k = column; k <= 36; k++) {
						HSSFCell cell = row.createCell(column++);
						Object obj = PropertyUtils.getProperty(mgt, "day" + (k - 5));
						cell.setCellStyle(obj == null ? redcolorCellStyle : cellStyle);

						if (null != dimissionDt && dimissionDt.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(dimissionDt, DateFormatType.yyyy_MM_dd));
							if (cal.get(Calendar.DATE) <= (k - 5)) 
								cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
						}
						
						if (null != sfDate && sfDate.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(sfDate, DateFormatType.yyyy_MM_dd));
							if ((k - 5) < cal.get(Calendar.DATE))
								cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
						}

						if (null != transferDate && transferDate.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(transferDate, DateFormatType.yyyy_MM_dd));
							if (empPostType.equals("1")) {
								if (cal.get(Calendar.DATE) > (k - 5))
									cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
							} else {
								if (cal.get(Calendar.DATE) <= (k - 5))
									cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
							}
						}

						if (null != dateFrom && dateFrom.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(dateFrom, DateFormatType.yyyy_MM_dd));
							if (null != LastZno && LastZno.equals(mgt.getDeptCode())) {
								if ((k - 5) >= cal.get(Calendar.DATE))
									cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
							} else {
								if ((k - 5) < cal.get(Calendar.DATE))
									cell.setCellStyle(obj == null ? bluecolorCellStyle : greyColorCellStyle);
							}
						}
						cell.setCellValue(obj == null ? "" : obj.toString());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				HSSFCell cell6 = row.createCell(column++);
				cell6.setCellValue("");
				cell6.setCellStyle(cellStyle);

				HSSFCell cell7 = row.createCell(column++);

				cell7.setCellStyle(cellStyle);

				if (mgt.getCommitStatus() != null && mgt.getCommitStatus() == 1) {
					cell7.setCellValue("是");
				} else {
					cell7.setCellValue("否");
				}

			}

		}
		return rowNum;
	}

	private HSSFCellStyle getGeneralStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	// 导入数据确认
	private List<SchedulMgt> jsonToList(String jsonstr) {
		if ("".equals(jsonstr) || 0 == jsonstr.trim().length()) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(jsonstr);
		if (array == null || array.isEmpty()) {
			return null;
		}
		List<SchedulMgt> list = (List<SchedulMgt>) JSONArray.toCollection(
				array, SchedulMgt.class);// new ArrayList<SchedulMgt>();
		return list;
	}

	public void saveImportConfirmData(String dataStr) {
		List<SchedulMgt> mgtlist = jsonToList(dataStr);
		HashMap<String, List<Scheduling>> schedulMap = convertMgt2Sche(mgtlist);

		for (SchedulMgt sm : mgtlist) {
			sm.setCommitStatus(commitStatus);
			Long monid = null;
			List<Scheduling> detaillist = schedulMap.get(sm.getEmpCode());
			OutEmployee employee = schedulMgtJdbcDao.getEmpByCode(
					sm.getDeptId(), sm.getEmpCode());
			if (sm.getId() != null) {
				monid = sm.getId();
				sm.setModifiedEmpCode(this.getCurrentUser().getUsername());
				sm.setModifiedTm(new Date());
				schedulMgtDao.update(sm);
			} else {
				sm.setCreateEmpCode(this.getCurrentUser().getUsername());
				sm.setCreateTm(new Date());
				sm.setModifiedTm(new Date());
				monid = schedulMgtDao.save(sm);
			}
			for (Scheduling scheduling : detaillist) {
				scheduling.setSheduleMonId(monid);
				if (scheduling.getId() != null) {
					if ((null != employee.getDimissionDt() && scheduling.getSheduleDt().after(employee.getDimissionDt()))
							|| scheduling.getSheduleDt().before(employee.getSfDate())) {
						continue;
					}
					scheduling.setDeptId(sm.getDeptId());
					scheduling.setModifiedEmpCode(this.getCurrentUser().getUsername());
					scheduling.setModifiedTm(new Date());
					scheduling.setSynchroStatus(0);
					if (!scheduling.getSheduleDt().after(new Date())) {
						this.schedulingModifyBiz.addFormScheduling(scheduling);
					}
					schedulingDao.update(scheduling);
				} else {
					scheduling.setCreateEmpCode(this.getCurrentUser().getUsername());
					scheduling.setCreateTm(new Date());
					scheduling.setModifiedTm(new Date());
					scheduling.setSynchroStatus(0);
					schedulingDao.save(scheduling);
				}
			}
		}
	}

	public HashMap<String, Object> saveImport(File uploadFile,
			ScheduleDto scheduleDto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HSSFSheet sheet = getHSSFSheet(uploadFile);

		// 获取导入的总条数
		int rowCount = sheet.getLastRowNum();
		validMaxRowNum(rowCount);

		// 获取导入excel组织名称单元格内容
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.getCell(2);
		String deptCode = Template.getCellStrValue(cell);
		validDepartment(scheduleDto, deptCode);

		// 获取导入excel月份单元格内容
		HSSFCell cell1 = row.getCell(4);
		String month = Template.getCellStrValue(cell1);// 格式：2014-09
		validMonth(month);
		FileOutputStream errorFos = null;
		List<SchedulMgt> passList = new ArrayList<SchedulMgt>();
		List<SchedulMgt> noPassList = new ArrayList<SchedulMgt>();

		int maxDayOfTheMonth = CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(month + "-01"));

		for (int rowIndex = 2; rowIndex <= rowCount; rowIndex++) {
			if (Template.isEmptyRow(sheet.getRow(rowIndex), 5)) {
				continue;
			}

			// 根据dayofMonth年月判断有多少天，循环遍历每月多少天,并收集错误的到noPassList，将验证通过的放入passList
			validEeachRow(sheet.getRow(rowIndex), scheduleDto.getDeptId(),
					maxDayOfTheMonth, month, passList, noPassList);
		}
		HSSFWorkbook errorWorkBook = generateErrorWorkBook();
		// 在passList验证通过基础上，再次校验，将连续三天为不同班次、员工少于4天休
		List<SchedulMgt> confirmList = new ArrayList<SchedulMgt>();
		// 遍历passList，将SchedulMgt转换为Scheduling
		HashMap<String, List<Scheduling>> schedulMap = convertMgt2Sche(passList);
		for (String key : schedulMap.keySet()) {
			// 根据工号获取数据库的记录和导入的合并
			List<Scheduling> dbList = schedulingJdbcDao.getScheBy(
					scheduleDto.getDeptId(), month, key);
			// 每个员工导入数据
			List<Scheduling> list = schedulMap.get(key);
			for (Scheduling entity : list) {
				if (month.equals(CommonUtil.getYm(entity.getSheduleDt()))) {
					if (dbList.contains(entity)) {
						Scheduling old = dbList.get(dbList.indexOf(entity));
						entity.setId(old.getId());
						entity.setSheduleMonId(old.getSheduleMonId());
						dbList.remove(entity);
						dbList.add(entity);
					} else {
						dbList.add(entity);
					}
				}
			}
			SchedulMgt mgtobj = new SchedulMgt();
			mgtobj.setYm(month);
			mgtobj.setEmpCode(key);
			mgtobj.setDeptId(scheduleDto.getDeptId());

			Collections.sort(dbList, new Comparator<Scheduling>() {
				public int compare(Scheduling arg0, Scheduling arg1) {
					return arg0.getSheduleDt().compareTo(arg1.getSheduleDt());
				}
			});
			// 员工少于4天休
			String errstr = this.checkRestCountOfMonthLess4(key, month, dbList);
			if (StringUtil.isNotBlank(errstr)) {
				int index = passList.indexOf(mgtobj);
				if (index >= 0) {
					SchedulMgt tmpobj = passList.get(index);
					tmpobj.setErrorDesc(errstr);
					confirmList.add(tmpobj);
					passList.remove(tmpobj);
				}
			}
		}
		for (SchedulMgt sm : passList) {
			Long monid;
			List<Scheduling> detaillist = schedulMap.get(sm.getEmpCode());
			sm.setCommitStatus(commitStatus);
			OutEmployee employee = schedulMgtJdbcDao.getEmpByCode(
					sm.getDeptId(), sm.getEmpCode());
			if (sm.getId() != null) {
				monid = sm.getId();
				sm.setModifiedEmpCode(this.getCurrentUser().getUsername());
				sm.setModifiedTm(new Date());
				sm.setSynchroStatus(0);
				sm.setCommitStatus(0);
				schedulMgtDao.update(sm);
			} else {
				sm.setCreateEmpCode(this.getCurrentUser().getUsername());
				sm.setCreateTm(new Date());
				sm.setModifiedTm(new Date());
				sm.setSynchroStatus(0);
				sm.setCommitStatus(0);
				monid = schedulMgtDao.save(sm);
			}
			for (Scheduling scheduling : detaillist) {
				scheduling.setSheduleMonId(monid);
				if (scheduling.getId() != null) {
					if ((null != employee.getDimissionDt() && scheduling.getSheduleDt().after(employee.getDimissionDt()))
							|| scheduling.getSheduleDt().before(employee.getSfDate())) {
						continue;
					}
					scheduling.setDeptId(scheduleDto.getDeptId());
					scheduling.setModifiedEmpCode(this.getCurrentUser().getUsername());
					scheduling.setModifiedTm(new Date());
					scheduling.setSynchroStatus(0);
					scheduling.setCommitStatus(0);
					// 运作排班修改记录，导入时插入记录表
					if (!scheduling.getSheduleDt().after(new Date())) {
						this.schedulingModifyBiz.addFormScheduling(scheduling);
					}
					schedulingDao.update(scheduling);

				} else {
					scheduling.setDeptId(scheduleDto.getDeptId());
					scheduling.setCreateEmpCode(this.getCurrentUser().getUsername());
					scheduling.setCreateTm(new Date());
					scheduling.setModifiedTm(new Date());
					scheduling.setSynchroStatus(0);
					scheduling.setCommitStatus(0);
					schedulingDao.save(scheduling);
				}
			}
		}
		// 生成错误记录的excel,包含当月排超过4个班别的数据
		if (noPassList != null && noPassList.size() > 0) {
			if (errorWorkBook != null) {
				try {
					String errorExcelPath = writeErrExcel(errorWorkBook,
							errorFos, noPassList, deptCode, month);
					map.put("errExcelPath", errorExcelPath);
				} catch (Exception e) {
					log.error("error:", e);
					throw new BizException("导出文件出错！");
				} finally {
					if (errorFos != null) {
						try {
							errorFos.close();
						} catch (Exception eee) {
							log.error("error:", eee);
						}
					}
				}
			}
		}

		// 返回需确认的
		map.put("confrimList", confirmList);
		map.put("tips",
				StringUtil.extractHandlerResult(passList.size(),
						noPassList.size()));
		return map;
	}

	private HSSFWorkbook generateErrorWorkBook() {
		FileInputStream errorFis = null;
		HSSFWorkbook errorWorkBook = null;
		// 生成导入错误的Excel
		try {
			String exportFileName = CommonUtil.getSaveFilePath(SchedulMgt.class);
			File outFile = new File(exportFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			errorFis = new FileInputStream(
					CommonUtil.getReportTemplatePath("排班导入模板.xls"));
			errorWorkBook = new HSSFWorkbook(errorFis);
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("导出文件出错！");
		} finally {
			if (errorFis != null) {
				try {
					errorFis.close();
				} catch (Exception eee) {
					log.error("error:", eee);
				}
			}
		}
		return errorWorkBook;
	}

	private void validMonth(String month) {
		if (StringUtil.isBlank(month)) {
			throw new BizException("年月不能为空！");
		}
		try {
			dateFormat.parse(month);
		} catch (ParseException e) {
			throw new BizException("年月格式不正确，正确格式 例如:2014-07！");
		}
		if (DateUtil.validConfirmDate(month)) {
//			if (DateUtil.isBeOverdue(month))
//				throw new BizException("已逾期，不能导入该月排班！");
		} else {
			throw new BizException("最多支持上个月、当前月以及下个月的排班导入！");
		}

	}

	private void validDepartment(ScheduleDto ss, String deptname) {
		if (StringUtil.isBlank(deptname)) {
			throw new BizException("组织代码不能为空！");
		}
		// 判断组织树上点选的名称和deptName是否一致
		if (!deptname.equals(DepartmentCacheBiz.getDepartmentByID(
				ss.getDeptId()).getDeptCode())) {
			throw new BizException("组织代码和选择网点不一致！");
		}
	}

	private void validMaxRowNum(int rowcount) {
		if (rowcount > 1000) {
			throw new BizException("一次最多只能导入 1000 条记录！");
		}
	}

	private HSSFSheet getHSSFSheet(File uploadFile) {
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
		HSSFSheet readSheet = readWorkBook.getSheetAt(0);
		return readSheet;
	}

	private HashMap<String, List<Scheduling>> convertMgt2Sche(
			List<SchedulMgt> passList) {
		HashMap<String, List<Scheduling>> result = new HashMap<String, List<Scheduling>>();
		for (SchedulMgt mgt : passList) {
			List<Scheduling> schedulingList = new ArrayList<Scheduling>();
			Date[] range = schedulingJdbcDao.getValidDateRange(mgt.getDeptId(),
					mgt.getEmpCode(), mgt.getYm());
			int maxDay = CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(mgt.getYm() + "-01"));
			if (range[0] == null || range[1] == null) {
				continue;
			}
			Date start = CommonUtil.getYMD(range[0]);
			Date end = CommonUtil.getYMD(range[1]);
			for (int i = 1; i <= maxDay; i++) {
				Date shedulDt = CommonUtil.getYmd(mgt.getYm() + "-"+ CommonUtil.addZero(i));
				if (CommonUtil.compareTowDate(start, shedulDt) <= 0 && CommonUtil.compareTowDate(shedulDt, end) <= 0) {
					Scheduling sche = this.schedulingJdbcDao.findByCondition(
							shedulDt, mgt.getEmpCode());
					if (sche == null) {
						sche = new Scheduling();
						sche.setDeptId(mgt.getDeptId());
						sche.setEmpCode(mgt.getEmpCode());
						sche.setSheduleDt(shedulDt);
					}
					try {
						Object schecode = PropertyUtils.getProperty(mgt, "day"+ i);
						if (schecode != null && schecode.toString().trim().length() > 0) {
							sche.setScheduleCode(schecode.toString());
							sche.setSheduleDt(shedulDt);
							schedulingList.add(sche);
						}
					} catch (Exception e) {
						log.error("error:", e);
						throw new BizException("操作失败！");
					}

				}
			}
			result.put(mgt.getEmpCode(), schedulingList);
		}
		return result;
	}

	private String writeErrExcel(HSSFWorkbook errorWorkBook,
			FileOutputStream errorFos, List<SchedulMgt> noPassList,
			String deptCode, String ym) {
		HSSFCellStyle cellStyle = getGeneralStyle(errorWorkBook);
		String fileName = String.format("排班导入失败信息_%s.xls",
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		HSSFSheet sheet = errorWorkBook.getSheetAt(0);
		if (noPassList != null && noPassList.size() > 0) {
			HSSFRow row0 = sheet.getRow(0);
			row0.getCell(2).setCellValue(deptCode);
			row0.getCell(4).setCellValue(ym);
			HSSFRow row1 = sheet.getRow(1);
			row1.getCell(35).setCellValue("校验未通过原因");
			int rowNum = 1; // 操作行
			try {
				for (SchedulMgt m : noPassList) {
					int column = 0;
					HSSFRow row = sheet.createRow(++rowNum);
					// 地区
					HSSFCell cell1 = row.createCell(column++);
					cell1.setCellValue(m.getAreaCode() == null ? "" : m.getAreaCode());
					cell1.setCellStyle(cellStyle);
					// 网点
					HSSFCell cell2 = row.createCell(column++);
					cell2.setCellValue(m.getDeptCode() == null ? "" : m.getDeptCode());
					cell2.setCellStyle(cellStyle);
					// 工号
					HSSFCell cell3 = row.createCell(column++);
					cell3.setCellValue(m.getEmpCode() == null ? "" : m.getEmpCode());
					cell3.setCellStyle(cellStyle);

					for (int k = column; k <= 33; k++) {
						HSSFCell cell = row.createCell(column++);
						Object obj = PropertyUtils.getProperty(m, "day"+ (k - 2));
						cell.setCellValue(obj == null ? "" : obj.toString());
						cell.setCellStyle(cellStyle);
					}
					column++;
					HSSFCell cell36 = row.createCell(column++);
					cell36.setCellValue(m.getErrorDesc() == null ? "" : m.getErrorDesc());
					cell36.setCellStyle(cellStyle);
				}
				CommonUtil.setDownloadFileName(fileName);
				errorFos = new FileOutputStream(new File(
						CommonUtil.getSaveFilePath(SchedulMgt.class)));
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
	
	// 总部权限
	private boolean isIncorrectDataWithSpecifyImporter(String targetMonth) {
		int currentDay = Clock.currentDayOfMonth();
		int currentHour = Clock.currentHourOfDay();
		int targetMonthOfYear = new DateTime(targetMonth).getMonthOfYear();		
		// 正确数据;
		if (currentDay < 4 || (currentDay == 4 && currentHour <= 20)) {
			return false;
		}		
		// 错误数据,不能导入以前的历史数据;
		return targetMonthOfYear == Clock.previousMonth();
	}
	
	//地区权限
	private boolean iscorrectDataWithSpecifyImporter(String targetMonth,int day) {
		int targetMonthOfYear = new DateTime(targetMonth).getMonthOfYear();
		int currentMonth = Clock.currentMonthOfYear();
		if(targetMonthOfYear == Clock.previousMonth()){
			if(!iscorrecDatatDay(targetMonth,  day)){
				return false;
			}
		}
	 if (targetMonthOfYear == currentMonth) {
		 if(!iscorrecDatatDay(targetMonth,  day)){
				return false;
			}
	 }			
		return true;
	}
	
	//判断应该导入的天的数据
	private boolean iscorrecDatatDay(String targetMonth,int day) {
		int currentDay = Clock.currentDayOfMonth();
		int currentHour = Clock.currentHourOfDay();			
		if (currentDay > 6) {
			if (DateUtil.timeCalculate(new Date(), -7).after(CommonUtil.getYmd(targetMonth + "-" + (day - 2)))) {
				return false;
			}
		}
		if ((currentDay == 4 && currentHour > 20) || currentDay == 5 || currentDay == 6) {
			if (DateUtil.timeCalculate(new Date(), -currentDay).after(CommonUtil.getYmd(targetMonth + "-" + (day - 2)))) {
				return false;
			}
		}
		if((currentDay == 4 && currentHour <= 20) || currentDay < 4){
			if (DateUtil.timeCalculate(new Date(), -7).after(CommonUtil.getYmd(targetMonth + "-" + (day - 2)))) {
				return false;
			}
		}	
					
		return true;
	}
	
		
	private boolean validEeachRow(HSSFRow row, Long deptId,
			int maxDayOfTheMonth, String month, List<SchedulMgt> passList,
			List<SchedulMgt> noPassList) {
		if (row == null)
			return false;
		HashMap<String, String> daysMap = new HashMap<String, String>();
		HashMap<String, String> errordaysMap = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		SchedulMgt excelObj = new SchedulMgt();// 保存验证不通过的
		SchedulMgt mgtObj = new SchedulMgt();// 保存验证通过的
		boolean flag = true;
		Department deptDept = DepartmentCacheBiz.getDepartmentByID(deptId);
		// 判断工号
		String empCode = Template.getCellStrValue(row.getCell(2));
		OutEmployee emp = null;
		if (StringUtil.isBlank(empCode)) {
			sb.append("工号为空!");
			sb.append("\n");
			flag = false;
		} else {
			emp = schedulMgtJdbcDao.checkExistByEmpByCode(deptId, empCode);
			if (emp == null) {
				sb.append("该工号不存在！");
				sb.append("\n");
				flag = false;
			}
		}
		emp = schedulMgtJdbcDao.getEmpByCode(deptId, empCode);
		Date[] monthDate = { null, null };
		if (emp != null) {
			empCode = emp.getEmpCode();
			// 转网点
			monthDate = schedulingJdbcDao.getValidDateRange(deptId, empCode,month);
			if (monthDate[0] == null || monthDate[1] == null) {
				sb.append("该工号员工已离职或转网点！");
				sb.append("\n");
				flag = false;
			}
		}
		List<String> dayValueList = new ArrayList<String>();
		// day1-day31
		for (int i = 3; i <= 3 + maxDayOfTheMonth - 1; i++) {
			// 当前单元格对应的日期
			HSSFCell c = row.getCell(i);
			String dayValue = Template.getCellStrValue(c);
			dayValueList.add(dayValue);
			String tmrDayTime = "";
			if (StringUtil.isNotBlank(dayValue)) {
				// 有修改整月的权限
				if (this.schedulMgtJdbcDao.queryModifyMonthPermissions(this.getCurrentUser().getId())
						// admin权限
						|| this.getCurrentUser().getId() == 1) {
					// 如果是错误的导入月份数据;对于总部权限的用户来说
					if (isIncorrectDataWithSpecifyImporter(month)) {
						sb.append("已逾期，不能导入该月排班！");
						sb.append("\n");
						flag = false;
					}
					//地区权限
				} else if (this.schedulMgtJdbcDao.queryModifyPermissions(this.getCurrentUser().getId())) {
					if(!iscorrectDataWithSpecifyImporter(month, i)){
							sb.append("有数据已超过历史第7天！");
							sb.append("\n");
							flag = false;
						}
				} else {
					if (DateUtil.timeCalculate(new Date(), 0).after(
							CommonUtil.getYmd(month + "-" + (i - 2)))) {
						sb.append("无修改历史数据权限!");
						sb.append("\n");
						flag = false;
					}
				}
				if (emp != null ){
					if (emp.getSfDate().after(
							CommonUtil.getYmd(month + "-" + (i - 2)))) {
						Date time = emp.getSfDate();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						sb.append(String.format("生效日期从" + sdf.format(time) + "开始",titles[i - 3]));
						sb.append("\n");
						flag = false;
					}
				}
				List<SchedulingBase> lst = this.schedulMgtJdbcDao.getScheInfo(
						deptId, dayValue, month);
				if ((lst == null || lst.size() == 0)) {
					if (!Arrays.asList(Constants.DEFAULT_CLASS).contains(dayValue)) {
						sb.append(String.format("%s排班班别不存在", titles[i - 3]));
						sb.append("\n");
						flag = false;
					}
				} else {
					tmrDayTime = constructionTimeList(lst.get(0));

					if (StringUtil.isNotBlank(tmrDayTime)) {
						if (i == 3 + maxDayOfTheMonth - 1) {
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							List<SchedulingBase> lastLst = this.schedulMgtJdbcDao.getLastMonthLastDayScheInfo(
											deptId,
											empCode,
											df.format(CommonUtil.getYmd(CommonUtil.getNextMonthYm(CommonUtil.getYmd(month+ '-'+ CommonUtil.addZero(1)))+ '-'+ CommonUtil.addZero(1))));
							if (lastLst != null && lastLst.size() != 0) {
								if (formatConversion(tmrDayTime) >= formatConversion(lastLst.get(0).getStart1Time())) {
									sb.append(String.format("%s排班班别与下月第一天排班班别时间冲突",titles[i - 3]));
									sb.append("\n");
									flag = false;
								}
							}
						}
						if (i != 3 + maxDayOfTheMonth - 1) {
							HSSFCell nextCell = row.getCell(i + 1);
							String nextDayValue = Template.getCellStrValue(nextCell);
							List<SchedulingBase> nextDaylist = this.schedulMgtJdbcDao.getScheInfo(deptId, nextDayValue, month);
							if (nextDaylist != null && nextDaylist.size() != 0) {
								if (formatConversion(tmrDayTime) >= formatConversion(nextDaylist.get(0).getStart1Time())) {
									sb.append(String.format(
											"%s排班班别与%s排班班别时间冲突", titles[i - 3],titles[i - 2]));
									sb.append("\n");
									flag = false;
								}
							}
						}
					}
					if (i == 3) {
						List<SchedulingBase> lastLst = this.schedulMgtJdbcDao
								.getLastMonthLastDayScheInfo(
										deptId,
										empCode,
										getFirstday_Lastday_Month(CommonUtil.getYmd(month + '-'+ CommonUtil.addZero(1))));
						if (lastLst != null
								&& lastLst.size() != 0
								&& StringUtil.isNotBlank(constructionTimeList(lastLst.get(0)))) {
							if (formatConversion(constructionTimeList(lastLst.get(0))) >= formatConversion(lst.get(0).getStart1Time())) {
								sb.append(String.format("%s排班班别与上月最后天排班班别时间冲突",titles[i - 3]));
								sb.append("\n");
								flag = false;
							}
						}
					}
				}
				daysMap.put("day" + (i - 2), dayValue);
			}
			errordaysMap.put("day" + (i - 2), dayValue);
		}
		int count = 0;
		for (int i = 0; i < dayValueList.size(); i++) {
			if (CommonUtil.isEmpty(dayValueList.get(i))) {
				count++;
			}
		}
		if (count == dayValueList.size()) {
			sb.append(empCode + "员工，整月排班为空!");
			sb.append("\n");
			flag = false;
		}

		try {
			if (flag) {
				// 将mgtObj放入passList
				if (!StringUtil.isBlank(month) && !StringUtil.isBlank(empCode)) {
					SchedulMgt obj = this.schedulMgtJdbcDao.findByCondition(
							deptId, month, empCode);
					OutEmployee employee = schedulMgtJdbcDao.getEmpByCode(
							deptId, empCode);
					if (obj != null) {
						obj.setAreaCode(deptDept.getAreaDeptCode());
						obj.setDeptCode(deptDept.getDeptCode());
						obj.setEmpCode(employee.getEmpCode());
						obj.setEmpName(employee.getEmpName());
						obj.setWorkType(employee.getWorkType());
						for (String key : daysMap.keySet()) {
							PropertyUtils.setProperty(obj, key,
									daysMap.get(key));
						}
						verificationWorkRepeat(passList, noPassList, empCode,
								obj);
					} else {
						mgtObj.setDeptId(deptDept.getId());
						mgtObj.setAreaCode(deptDept.getAreaDeptCode());
						mgtObj.setDeptCode(deptDept.getDeptCode());
						mgtObj.setEmpCode(employee.getEmpCode());
						mgtObj.setEmpName(employee.getEmpName());
						mgtObj.setWorkType(employee.getWorkType());
						mgtObj.setYm(month);
						for (String key : daysMap.keySet()) {
							PropertyUtils.setProperty(mgtObj, key,daysMap.get(key));
						}
						verificationWorkRepeat(passList, noPassList, empCode,mgtObj);
					}
				}
			} else {
				excelObj.setYm(month);
				excelObj.setAreaCode(deptDept.getAreaDeptCode());
				excelObj.setDeptCode(deptDept.getDeptCode());
				excelObj.setEmpCode(empCode);
				for (String key : errordaysMap.keySet()) {
					PropertyUtils.setProperty(excelObj, key,
							errordaysMap.get(key));
				}
				excelObj.setErrorDesc(sb.toString());
				// 将excelObj放入noPassList
				noPassList.add(excelObj);
			}
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("操作失败！");
		}
		return flag;
	}

	private static String getFirstday_Lastday_Month(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		return day_last;
	}

	private void verificationWorkRepeat(List<SchedulMgt> passList,
			List<SchedulMgt> noPassList, String empCode, SchedulMgt obj) {
		List<SchedulMgt> remove = new ArrayList<SchedulMgt>();
		if (passList.size() > 0) {
			for (SchedulMgt mgts : passList) {
				if (mgts.equals(obj)) {
					obj.setErrorDesc(String.format("%s工号有重复！", empCode));
					noPassList.add(obj);
					mgts.setErrorDesc(String.format("%s工号有重复！", empCode));
					noPassList.add(mgts);
					remove.add(mgts);
				}
			}
		}
		if (remove.size() > 0) {
			passList.removeAll(remove);
		} else {
			passList.add(obj);
		}
	}

	public boolean eqScheduleCode(String srcCode, String destCode) {
		if ("休".equals(srcCode)) {
			srcCode = destCode;
		}
		if ("休".equals(destCode)) {
			destCode = srcCode;
		}

		return !destCode.equals(srcCode);
	}

	// 员工少于4天休校验
	private String checkRestCountOfMonthLess4(String empCode, String ym,
			List<Scheduling> lst) {
		if (CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(ym + "-01")) >= 28) {
			int count = 0;
			if (lst.size() < 28) {
				return "";
			}
			for (Scheduling s : lst) {
				if ("休".equals(s.getScheduleCode())
						|| "SW".equals(s.getScheduleCode())
						|| "OFF".equals(s.getScheduleCode())) {
					count++;
				}
			}
			if (count < 4) {
				return "排休少于4天";
			}
		}
		return "";
	}

	public void loadSendScheMail() {
		/*
		 * String[] ym = CommonUtil.getYm(new Date()).split("-"); List<Date>
		 * dates = getDates(Integer.valueOf(ym[0]), Integer.valueOf(ym[1]));
		 * Date currentDt = CommonUtil.currentDt(); if
		 * (currentDt.compareTo(dates.get(dates.size() - 1)) == 0 ||
		 * currentDt.compareTo(dates.get(dates.size() - 2)) == 0) {
		 * List<OutEmployee> emailList =
		 * schedulMgtJdbcDao.getUnFinishedEmp(CommonUtil.getNextMonthYm(new
		 * Date())); if (sendMailBiz.updateEmailStartStatus()) { if (emailList
		 * != null && emailList.size() > 0) { for (OutEmployee emp : emailList)
		 * { if (null == emp.getEmail() || "".equals(emp.getEmail())) {
		 * continue; } sendMailBiz.sendMail(emp.getEmail(),
		 * createContent("预排班")); } } sendMailBiz.updateEmailEndStatus(); } }
		 */
	}

	private String createContent(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<p style='width:780px;line-height:23px;'><b>" + "尊敬的用户：");
		sb.append("</b><br/><label style='width:2em;'></label>" + "您的"
				+ CommonUtil.getNextMonthYm(new Date()) + "月份" + name
				+ "还未完成，请尽快完成！" + "</p>");
		sb.append("<div style='color:gray;font-size:12px;'>系统发送邮件,请不要回复</div></body></html>");
		return sb.toString();
	}

	private List<Date> getDates(int year, int month) {
		List<Date> dates = new ArrayList<Date>();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);

		while (cal.get(Calendar.YEAR) == year
				&& cal.get(Calendar.MONTH) < month) {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				dates.add((Date) cal.getTime().clone());
			}
			cal.add(Calendar.DATE, 1);
		}
		return dates;

	}

	public IPage<OutEmployee> findEmpPage(ScheduleDto dto, int pageSize,
			int pageIndex) {
		return schedulMgtJdbcDao.findEmpPage(dto, pageSize, pageIndex);
	}

	@Override
	public void insertConfirmLog(Long deptId, String ym) {
		schedulMgtDao.insertConfirmLog(deptId, ym);
	}

	@Override
	public void delete(ScheduleDto dto, String schedulingIds) {
		String[] schedulingIdsArray = schedulingIds.replaceAll(",$", "").split(",", -1);
		try {
			for (String schedulingId : schedulingIdsArray) {
				Long schedulId = Long.parseLong(schedulingId);
				schedulMgtDao.delete(dto, schedulId);
			}
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("删除失败！");
		}
	}

	@Override
	public String getConfirmExport() {
		try {

			CommonUtil.setDownloadFileName(downloadFileName);
			String exportFileName = CommonUtil
					.getSaveFilePath(GroupBaseInfo.class);
			File file = new File(exportFileName);
			if (!file.exists())
				file.createNewFile();
			FileInputStream template = new FileInputStream(
					CommonUtil.getReportTemplatePath("已提交确认信息.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(template);
			HSSFCellStyle cellStyle = getGeneralStyle(workbook);
			List<MonthConfirmStatus> confirmStatusList = schedulMgtJdbcDao.getConfirmList();
			HSSFSheet sheet;
			for (int i = 0; i <= confirmStatusList.size() / maxPageSize; i++) {
				if (i != confirmStatusList.size() / maxPageSize) {
					sheet = workbook.cloneSheet(0);
					workbook.setSheetName(workbook.getSheetIndex(sheet), "导出数据"+ (i + 1));
					List<MonthConfirmStatus> exportByPageBaseInfos = confirmStatusList.subList(i * maxPageSize, (i + 1) * maxPageSize);
					fillRowData(sheet, exportByPageBaseInfos, cellStyle);

				} else {
					if (confirmStatusList.size() % maxPageSize > 0) {
						sheet = workbook.cloneSheet(0);
						workbook.setSheetName(workbook.getSheetIndex(sheet),"导出数据" + (i + 1));
						List<MonthConfirmStatus> exportByPageBaseInfos = confirmStatusList.subList(i * maxPageSize, i * maxPageSize+ confirmStatusList.size()% maxPageSize);
						fillRowData(sheet, exportByPageBaseInfos, cellStyle);
					}

				}
			}
			CommonUtil.setDownloadFileName(downloadFileName);
			if (confirmStatusList.size() > 0)
				workbook.removeSheetAt(0);
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			log.error("throw Exception:", e);
			throw new BizException("导出失败!");
		}

		return CommonUtil.getReturnPageFileName();
	}

	private void fillRowData(HSSFSheet sheet,
			List<MonthConfirmStatus> confirmStatusList, HSSFCellStyle cellStyle) {
		int rowNum = 1; // 操作行
		for (MonthConfirmStatus mcs : confirmStatusList) {
			int column = 0;
			HSSFRow row = sheet.createRow(++rowNum);

			HSSFCell cell1 = row.createCell(column++);
			cell1.setCellValue(mcs.getDeptId() == null ? "": DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getAreaDeptCode());
			cell1.setCellStyle(cellStyle);

			HSSFCell cell2 = row.createCell(column++);
			cell2.setCellValue(mcs.getDeptId() == null ? "": DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getCode());
			cell2.setCellStyle(cellStyle);

			HSSFCell cell3 = row.createCell(column++);
			cell3.setCellValue(mcs.getDeptId() == null ? "": DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getName());
			cell3.setCellStyle(cellStyle);
			// 月份
			HSSFCell cell4 = row.createCell(column++);
			cell4.setCellValue(mcs.getYm() == null ? "" : mcs.getYm());
			cell4.setCellStyle(cellStyle);
			// 网点
			int o = 0;
			o = mcs.getCommitStatus();
			String v = "";
			switch (o) {
			case 1:
				v = "已确认";
				break;
			case 0:
				v = "未确认";
				break;
			}
			HSSFCell cell5 = row.createCell(column++);
			cell5.setCellValue(v);
			cell5.setCellStyle(cellStyle);
		}
	}

	@Override
	public boolean getToConfirmThe(Long deptId, String schedulingIds) {
		return schedulMgtJdbcDao.getToConfirmThe(deptId, schedulingIds);
	}

	@Override
	public String exportNotScheduling(Long deptId, String ym) {
		List<OutEmployee> empList = queryEmployee(deptId, ym);
		int sheetCount = getSheetCount(empList.size());

		HSSFWorkbook workbook = null;
		String fileName = String.format("未排班人员导出_%s.xls", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(
					CommonUtil.getReportTemplatePath("排班导入模板.xls"));
			workbook = new HSSFWorkbook(fis);
			HSSFCellStyle cellStyle = constructionExcelFormat(workbook);

			HSSFSheet[] sheetArray = new HSSFSheet[sheetCount];
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row = sheet.getRow(0);

			Department deptDept = DepartmentCacheBiz.getDepartmentByID(deptId);
			setDeptCode(row, deptDept);
			setYm(ym, row);
			sheetArray[0] = sheet;

			for (int k = 1; k < sheetArray.length; k++) {
				sheetArray[k] = workbook.cloneSheet(0);
			}

			fos = constructionExcelData(empList, sheetCount, workbook,
					fileName, fos, cellStyle, sheetArray);

		} catch (Exception e) {
			log.error("throw Exception:", e);
			throw new BizException("导出失败!");
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				log.error("throw Exception:", e);
			}
		}
		return CommonUtil.getReturnPageFileName();
	}

	private void setYm(String ym, HSSFRow row) {
		row.getCell(4).setCellValue(ym.substring(0, 7));
	}

	private void setDeptCode(HSSFRow row, Department deptDept) {
		row.getCell(2).setCellValue(deptDept.getDeptCode());
	}

	private HSSFCellStyle constructionExcelFormat(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();

		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	private FileOutputStream constructionExcelData(List<OutEmployee> empList,
			int sheetCount, HSSFWorkbook workbook, String fileName,
			FileOutputStream fos, HSSFCellStyle cellStyle,
			HSSFSheet[] sheetArray /*, Department deptDept*/)
			throws FileNotFoundException, Exception, IOException {
		for (int i = 0; i < sheetCount; i++) {
			HSSFSheet sheet = sheetArray[i];
			if (empList != null && empList.size() > 0 && sheet != null) {
				int rowNum = 1; // 操作行
				for (OutEmployee outEmployee : empList) {
					Department deptDept = DepartmentCacheBiz.getDepartmentByID(outEmployee.getDeptId());
					int column = 0;
					HSSFRow row = sheet.createRow(++rowNum);

					// 地区
					HSSFCell cell1 = row.createCell(column++);
					cell1.setCellValue(deptDept.getAreaDeptCode() == null ? "": deptDept.getAreaDeptCode());
					cell1.setCellStyle(cellStyle);

					// 网点
					HSSFCell cell2 = row.createCell(column++);					
					cell2.setCellValue(deptDept.getDeptCode() == null ? "": deptDept.getDeptCode());					
					cell2.setCellStyle(cellStyle);

					// 工号
					HSSFCell cell3 = row.createCell(column++);
					cell3.setCellValue(outEmployee.getEmpCode() == null ? "": outEmployee.getEmpCode());
					cell3.setCellStyle(cellStyle);

					for (int index = column; index <= 33; index++) {
						HSSFCell cell = row.createCell(column++);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
					}
				}
			}
			CommonUtil.setDownloadFileName(fileName);
			fos = new FileOutputStream(new File(
					CommonUtil.getSaveFilePath(SchedulingForDispatch.class)));
			workbook.write(fos);
		}
		return fos;
	}

	private List<OutEmployee> queryEmployee(Long deptId, String ym) {
		List<OutEmployee> empList = schedulMgtJdbcDao.getNotSchedulEmployee(
				deptId, ym.substring(0, 7));
		if (empList.size() == 0) {
			throw new BizException(String.format("该网点在%s月没有未排班人员信息！",
					ym.substring(0, 7)));
		}
		return empList;
	}

	private int getSheetCount(int countEmployee) {
		int sheetCount;
		if (countEmployee <= maxPageSize) {
			sheetCount = 1;
		} else {
			if (countEmployee % maxPageSize == 0) {
				sheetCount = countEmployee / maxPageSize;
			} else {
				sheetCount = countEmployee / maxPageSize + 1;
			}
		}
		return sheetCount;
	}

	private String constructionTimeList(SchedulingBase schedulingBase) {
		if (StringUtil.isNotBlank(schedulingBase.getStart3Time())
				&& (formatConversion(schedulingBase.getStart3Time()) >= formatConversion(schedulingBase.getEnd3Time())
						|| formatConversion(schedulingBase.getStart2Time()) >= formatConversion(schedulingBase.getEnd2Time()) 
						|| formatConversion(schedulingBase.getStart1Time()) >= formatConversion(schedulingBase.getEnd1Time()))) {
			return schedulingBase.getEnd3Time();
		}

		if (StringUtil.isNotBlank(schedulingBase.getStart2Time())
				&& (formatConversion(schedulingBase.getStart2Time()) >= formatConversion(schedulingBase.getEnd2Time()) 
				|| formatConversion(schedulingBase.getStart1Time()) >= formatConversion(schedulingBase.getEnd1Time()))) {
			return schedulingBase.getEnd2Time();
		}

		if (formatConversion(schedulingBase.getStart1Time()) >= formatConversion(schedulingBase.getEnd1Time())) {
			return schedulingBase.getEnd1Time();
		}
		return "";
	}

	private static int formatConversion(String field) {
		return Integer.parseInt(field.replace(":", ""));
	}

	@Transactional
	public void commitConfirmScheduling(Long deptid, String ym)
			throws Exception {
		validBeforeConfirm(ym);
		saveMonthConfirmStatus(deptid, ym);
		List<SchedulMgt> noConfirmSchedulings = schedulMgtDao.queryNoConfirmScheduling(deptid, ym);
		if (noConfirmSchedulings.isEmpty()) {
			return;
		}
		for (SchedulMgt scheduling : noConfirmSchedulings) {
			SchedulMgtHistory prevScheduling = schedulMgtHistoryDao.findByCondition(deptid, ym, scheduling.getEmpCode());
			OutEmployee employee = outEmployeeDao.queryEmployee(
					scheduling.getEmpCode(), deptid);
			if (null == employee)
				continue;
			if (prevScheduling == null) {
				SchedulMgtHistory schedulingHistory = new SchedulMgtHistory();
				BeanUtils.copyProperties(schedulingHistory, scheduling);
				schedulingHistory.setId(null);
				schedulingHistory.setCoincideuNum(0);
				schedulingHistory.countEffectiveNum(employee);
				updateSchedulingAndInsertSchedulingHistory(scheduling,
						schedulingHistory, employee);
				continue;
			}
			SchedulMgtHistory schedulMgtHistory = new SchedulMgtHistory();
			BeanUtils.copyProperties(schedulMgtHistory, scheduling);
			schedulMgtHistory.countCoincideNum(employee, prevScheduling);
			schedulMgtHistory.countEffectiveNum(employee);
			updateSchedulingAndInsertSchedulingHistory(scheduling,
					schedulMgtHistory, employee);
		}
		schedulMgtDao.updateSchedulingCommitStatus(deptid, ym);
	}

	private void updateSchedulingAndInsertSchedulingHistory(
			SchedulMgt scheduling, SchedulMgtHistory schedulingHistory,
			OutEmployee employee) throws IllegalAccessException,
			InvocationTargetException {
		scheduling.setCommitStatus(1);
		schedulMgtDao.update(scheduling);
		schedulingHistory.setModifiedEmpCode(getCurrentUser().getUsername());
		schedulingHistory.setModifiedTm(new Date());
		schedulingHistory.setCreateTm(new Date());
		schedulingHistory.setEmpName(employee.getEmpName());
		schedulingHistory.setWorkType(employee.getWorkType());
		schedulMgtHistoryDao.save(schedulingHistory);
	}

	private void saveMonthConfirmStatus(Long deptid, String ym) {
		MonthConfirmStatus monthConfirmStatus = monthConfirmStatusDao.findBy(
				deptid, ym);
		if (null != monthConfirmStatus)
			return;
		monthConfirmStatus = new MonthConfirmStatus();
		monthConfirmStatus.setDeptId(deptid);
		monthConfirmStatus.setYm(ym);
		monthConfirmStatus.setCommitStatus(1);
		monthConfirmStatusDao.save(monthConfirmStatus);
	}

	private void validBeforeConfirm(String ym) {
		if (!DateUtil.validConfirmDate(ym)) {
			throw new BizException("提交确认只能选择上个月、当前月或下个月！");
		}
		if (ym.equals(CommonUtil.getLastMonthYm(new Date()))
				&& DateUtil.isBeOverdue(ym)) {
			throw new BizException("已逾期，不能提交确认该月排班！");
		}
	}
}