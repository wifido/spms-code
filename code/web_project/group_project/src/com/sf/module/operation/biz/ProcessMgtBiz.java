/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import static com.sf.module.common.domain.Constants.KEY_ERROR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.biz.ISendMailBiz;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.dao.IMonthConfirmStatusDao;
import com.sf.module.operation.dao.IProcessConfirmStatusDao;
import com.sf.module.operation.dao.IProcessDao;
import com.sf.module.operation.dao.IProcessDetailDao;
import com.sf.module.operation.dao.IProcessMgtDao;
import com.sf.module.operation.dao.IProcessMgtJdbcDao;
import com.sf.module.operation.dao.ISchedulMgtJdbcDao;
import com.sf.module.operation.dao.ISchedulingDao;
import com.sf.module.operation.dao.ISchedulingJdbcDao;
import com.sf.module.operation.domain.GroupBaseInfo;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.Process;
import com.sf.module.operation.domain.ProcessConfirmStatus;
import com.sf.module.operation.domain.ProcessDetail;
import com.sf.module.operation.domain.ProcessMgt;
import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.organization.domain.Department;

/**
 * 
 * 月度工序实体的业务实现类
 * 
 * @author houjingyu 2014-07-08
 * 
 */
public class ProcessMgtBiz extends BaseBiz implements IProcessMgtBiz {
	private IProcessMgtDao processMgtDao;
	private IProcessMgtJdbcDao processMgtJdbcDao;
	private IMonthConfirmStatusDao monthConfirmStatusDao;
	private ISchedulMgtJdbcDao schedulMgtJdbcDao;
	private IProcessDao processDao;
	private ISchedulingJdbcDao schedulingJdbcDao;
	private IProcessDetailDao processDetailDao;
	private IProcessConfirmStatusDao processConfirmStatusDao;
	private ISendMailBiz sendMailBiz;
	private ISchedulingDao schedulingDao;
	private static Integer COMMITSTATUS = 0;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private final static String titles[] = new String[] { "第1天", "第2天", "第3天", "第4天", "第5天", "第6天", "第7天", "第8天", "第9天", "第10天", "第11天", "第12天", "第13天",
			"第14天", "第15天", "第16天", "第17天", "第18天", "第19天", "第20天", "第21天", "第22天", "第23天", "第24天", "第25天", "第26天", "第27天", "第28天", "第29天", "第30天", "第31天" };

	public void setProcessMgtDao(IProcessMgtDao processMgtDao) {
		this.processMgtDao = processMgtDao;
	}

	public void setProcessMgtJdbcDao(IProcessMgtJdbcDao processMgtJdbcDao) {
		this.processMgtJdbcDao = processMgtJdbcDao;
	}

	public IPage<ProcessMgt> findPage(ProcessDto dto, int pageSize, int pageIndex) {
		return processMgtJdbcDao.findPage(this.getCurrentUser().getId(), dto, pageSize, pageIndex);
	}

	public void setMonthConfirmStatusDao(IMonthConfirmStatusDao monthConfirmStatusDao) {
		this.monthConfirmStatusDao = monthConfirmStatusDao;
	}

	public void setSchedulMgtJdbcDao(ISchedulMgtJdbcDao schedulMgtJdbcDao) {
		this.schedulMgtJdbcDao = schedulMgtJdbcDao;
	}

	public void setProcessDao(IProcessDao processDao) {
		this.processDao = processDao;
	}

	public void setSchedulingJdbcDao(ISchedulingJdbcDao schedulingJdbcDao) {
		this.schedulingJdbcDao = schedulingJdbcDao;
	}

	public void setProcessDetailDao(IProcessDetailDao processDetailDao) {
		this.processDetailDao = processDetailDao;
	}

	public void setProcessConfirmStatusDao(IProcessConfirmStatusDao processConfirmStatusDao) {
		this.processConfirmStatusDao = processConfirmStatusDao;
	}

	public void setSendMailBiz(ISendMailBiz sendMailBiz) {
		this.sendMailBiz = sendMailBiz;
	}

	public void setSchedulingDao(ISchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

	private final int maxPageSize = 60000;

	public String getExcel(ProcessDto dto) {
		int totalSize = processMgtJdbcDao.getExportProcessMgtCount(dto);
		if (totalSize < 1) {
			throw new BizException("暂无确认的工序安排数据！");
		}
		int sheetCount = getSheetCount(totalSize);
		HSSFWorkbook workbook = null;
		String fileName = String.format("工序安排信息导出_%s.xls", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(CommonUtil.getReportTemplatePath("工序安排导出.xls"));
			workbook = new HSSFWorkbook(fis);
		
			HSSFSheet[] sheetArray = new HSSFSheet[sheetCount];
			HSSFSheet sh = workbook.getSheetAt(0);
			HSSFRow r = sh.getRow(0);
			r.getCell(2).setCellValue(DepartmentCacheBiz.getDepartmentByID(dto.getDeptId()).getDeptCode());
			r.getCell(4).setCellValue(dto.getYm());
			sheetArray[0] = sh;

			for (int k = 1; k < sheetArray.length; k++) {
				sheetArray[k] = workbook.cloneSheet(0);
			}

			for (int i = 0; i < sheetCount; i++) {
				HSSFSheet sheet = sheetArray[i];
				int rowNum = 1; // 操作行
//				IPage<ProcessMgt> page = processMgtJdbcDao.importConfirmProcessMgt(dto, maxPageSize, i);
//				rowNum = writeExcel(cellStyle, sheet, rowNum, page, true);

				IPage<ProcessMgt> noConfirmPage = processMgtJdbcDao.findPage(this.getCurrentUser().getId(), dto, maxPageSize, i);
				rowNum = writeExcel(workbook, sheet, rowNum, noConfirmPage, false);

			}
			CommonUtil.setDownloadFileName(fileName);
			fos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(ProcessMgt.class)));
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
	
	private HSSFCellStyle getGeneralStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	private int writeExcel(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum, IPage<ProcessMgt> page, boolean isConfirm) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Collection<ProcessMgt> data = page.getData();
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
			for (ProcessMgt mgt : data) {
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
				
				Calendar cal = Calendar.getInstance();
				String dimissionDt = mgt.getDimissionDt();
				String dateFrom = mgt.getDateFrom();
				String transferDate = mgt.getTransferDate();
				String LastZno = mgt.getLastZno();
				String empPostType = mgt.getEmpPostType();
				String sfDate = mgt.getSfDate();
				try {
					for (int k = column; k <= 35; k++) {
						HSSFCell cell = row.createCell(column++);
						Object obj = PropertyUtils.getProperty(mgt, "day" + (k - 4));
						cell.setCellStyle(obj == null ? redcolorCellStyle : cellStyle);

						if (null != dimissionDt && dimissionDt.indexOf(mgt.getYm()) == 0) {

							cal.setTime(DateUtil.parseDate(dimissionDt, DateFormatType.yyyy_MM_dd));

							if (cal.get(Calendar.DATE) <= (k - 4))
								cell.setCellStyle(obj == null ? bluecolorCellStyle
										: greyColorCellStyle);
						}

						if (null != sfDate && sfDate.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(sfDate, DateFormatType.yyyy_MM_dd));
							if ((k - 4) < cal.get(Calendar.DATE))
								cell.setCellStyle(obj == null ? bluecolorCellStyle
										: greyColorCellStyle);
						}

						if (null != transferDate && transferDate.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(transferDate, DateFormatType.yyyy_MM_dd));
							if (empPostType.equals("1")) {
								if (cal.get(Calendar.DATE) > (k - 4))
									cell.setCellStyle(obj == null ? bluecolorCellStyle
											: greyColorCellStyle);
							} else {
								if (cal.get(Calendar.DATE) <= (k - 4))
									cell.setCellStyle(obj == null ? bluecolorCellStyle
											: greyColorCellStyle);
							}
						}

						if (null != dateFrom && dateFrom.indexOf(mgt.getYm()) == 0) {
							cal.setTime(DateUtil.parseDate(dateFrom, DateFormatType.yyyy_MM_dd));
							if (null != LastZno && LastZno.equals(mgt.getDeptCode())) {
								if ((k - 4) >= cal.get(Calendar.DATE))
									cell.setCellStyle(obj == null ? bluecolorCellStyle
											: greyColorCellStyle);
							} else {
								if ((k - 4) < cal.get(Calendar.DATE))
									cell.setCellStyle(obj == null ? bluecolorCellStyle
											: greyColorCellStyle);
							}
						}
						cell.setCellValue(obj == null ? "" : obj.toString());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				HSSFCell cell37 = row.createCell(column++);
				cell37.setCellValue("");
				cell37.setCellStyle(cellStyle);
				HSSFCell cell38 = row.createCell(37);
				cell38.setCellStyle(cellStyle);
				if (mgt.getCommitStatus() == 1) {
					cell38.setCellValue("是");
				} else {
					cell38.setCellValue("否");
					
				}
			}
		}
		return rowNum;
	}

	private boolean thisSystemWorkType(int workType) {
		return workType == 0 || workType == 6 || workType == 8 || workType == 9;
	}

	private HSSFCellStyle setCelllStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	private int getSheetCount(int totalSize) {
		if (totalSize <= maxPageSize) {
			return 1;
		}
		return totalSize % maxPageSize == 0 ? totalSize / maxPageSize : totalSize / maxPageSize + 1;
	}

	private String getCellStrValue(HSSFCell cellobj) {
		if (null == cellobj) {
			return "";
		}
		cellobj.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cellobj.toString().trim();
	}

	public HashMap<String, Object> saveImport(File uploadFile, ProcessDto ss) {
		if (uploadFile != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			FileInputStream fin = null;
			HSSFWorkbook readWorkBook = null;

			try {
				fin = new FileInputStream(uploadFile);
				readWorkBook = new HSSFWorkbook(fin);
			} catch (Exception e1) {
				log.error("throw Exception:", e1);
				throw new BizException("读文件出错！");
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (Exception e) {
						log.error(KEY_ERROR, e);
					}
				}

			}
			HSSFSheet readSheet = readWorkBook.getSheetAt(0);
			int dayofMonth = 0;
			int rowcount = readSheet.getLastRowNum();
			if (rowcount > 1000) {
				throw new BizException("一次最多只能导入 1000 条记录！");
			}
			// 获取导入excel组织名称单元格内容
			HSSFRow r = readSheet.getRow(0);
			HSSFCell c = r.getCell(2);
			String deptCode = getCellStrValue(c);
			if (StringUtils.isBlank(deptCode)) {
				throw new BizException("组织代码不能为空！");
			}
			// 判断组织树上点选的名称和deptName是否一致
			if (!deptCode.equals(DepartmentCacheBiz.getDepartmentByID(ss.getDeptId()).getDeptCode())) {
				throw new BizException("Excel中的组织代码和所选的网点不一致!");
			}
			// 获取导入excel月份单元格内容
			c = r.getCell(4);
			String ym = getCellStrValue(c);
			dayofMonth = validYm(dayofMonth, ym);
			// 排班数据未确认的不能导入
			queryConfirmStatus(ss, ym);
			// 模板表头
			FileInputStream errorFis = null;
			FileOutputStream errorFos = null;
			Long impDeptId = ss.getDeptId();
			List<ProcessMgt> passList = new ArrayList<ProcessMgt>();
			List<ProcessMgt> noPassList = new ArrayList<ProcessMgt>();
			for (int rowIndex = 2; rowIndex <= rowcount; rowIndex++) {
				HSSFRow row = readSheet.getRow(rowIndex);
				if (Template.isEmptyRow(row, 3)) {
					continue;
				}
				// 根据dayofMonth年月判断有多少天，循环遍历每月多少天,并收集错误的到noPassList，将验证通过的放入passList
				checkRowDataValid(row, impDeptId, titles, dayofMonth, ym, passList, noPassList);
			}
			// 生成导入错误的Excel
			HSSFWorkbook errorWorkBook = createExcel(errorFis);

			// 遍历passList，将SchedulMgt转换为Scheduling
			HashMap<String, List<ProcessDetail>> schedulMap = dataForEveryDay(map, deptCode, ym, errorFos, passList, noPassList, errorWorkBook);
			// 保存passList的数据,并保存明细
			saveData(passList, schedulMap);

			map.put("tips", StringUtil.extractHandlerResult(passList.size(), noPassList.size()));
			return map;
		}
		return null;

	}

	private void saveData(List<ProcessMgt> passList, HashMap<String, List<ProcessDetail>> schedulMap) {
		for (ProcessMgt sm : passList) {
			Long monid;

			if (sm.getId() != null) {
				monid = sm.getId();
				sm.setModifiedEmpCode(this.getCurrentUser().getUsername());
				sm.setModifiedTm(new Date());
				sm.setCommitStatus(0);
				processMgtDao.update(sm);
			} else {
				sm.setCreateEmpCode(this.getCurrentUser().getUsername());
				sm.setCreateTm(new Date());
				sm.setModifiedTm(new Date());
				sm.setCommitStatus(0);
				monid = processMgtDao.save(sm);
			}
			List<ProcessDetail> detaillist = schedulMap.get(sm.getEmpCode());
			for (ProcessDetail detail : detaillist) {
				detail.setProcessMonId(monid);
				if (detail.getId() != null) {
					detail.setModifiedEmpCode(this.getCurrentUser().getUsername());
					detail.setModifiedTm(new Date());
					processDetailDao.update(detail);

				} else {
					detail.setCreateEmpCode(this.getCurrentUser().getUsername());
					detail.setCreateTm(new Date());
					detail.setModifiedTm(new Date());
					processDetailDao.save(detail);
				}
			}
		}
	}

	private HashMap<String, List<ProcessDetail>> dataForEveryDay(HashMap<String, Object> map, String deptCode, String ym, FileOutputStream errorFos,
			List<ProcessMgt> passList, List<ProcessMgt> noPassList, HSSFWorkbook errorWorkBook) {
		HashMap<String, List<ProcessDetail>> schedulMap = convertMgt2Detail(passList);
		// 生成错误记录的excel,包含当月排超过4个班别的数据
		if (noPassList != null && noPassList.size() > 0) {
			if (errorWorkBook != null) {
				try {
					String errorExcelPath = writeErrExcel(errorWorkBook, errorFos, noPassList, deptCode, ym);
					map.put("errExcelPath", errorExcelPath);
				} catch (Exception e) {
					log.error("throw Exception:", e);
					throw new BizException("导出文件出错!");
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
		return schedulMap;
	}

	private HSSFWorkbook createExcel(FileInputStream errorFis) {
		HSSFWorkbook errorWorkBook = null;
		try {
			String exportFileName = CommonUtil.getSaveFilePath(ProcessMgt.class);
			File outFile = new File(exportFileName);
			if (!outFile.exists())
				outFile.createNewFile();
			errorFis = new FileInputStream(CommonUtil.getReportTemplatePath("工序安排导入模板.xls"));
			errorWorkBook = new HSSFWorkbook(errorFis);
		} catch (Exception ee) {
			log.error("throw Exception:", ee);
			throw new BizException("导出文件出错!");
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

	private void queryConfirmStatus(ProcessDto ss, String ym) {
		MonthConfirmStatus mcs = monthConfirmStatusDao.findBy(ss.getDeptId(), ym);
		if (mcs == null) {
			
			throw new BizException(String.format("%s月份排班未确认，不能进行排工序！", ym));
		}
	}

	private int validYm(int dayofMonth, String ym) {
		if (StringUtils.isBlank(ym)) {
			throw new BizException("年月不能为空！");
		} else {
			// 年月的格式y-m
			String el = "[0-9]{4}-[0-9]{2}";
			Pattern p = Pattern.compile(el);
			Matcher m = p.matcher(ym);
			boolean dateFlag = m.matches();
			if (!dateFlag) {
				throw new BizException("年月格式不正确，正确格式 例如:2014-07！");
			} else {
				dayofMonth = CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(ym + "-01"));
			}

			if (!(ym.equals(CommonUtil.getNextMonthYm(new Date())) || ym.equals(CommonUtil.getYm(new Date())) || ym.equals(CommonUtil
					.getLastMonthYm(new Date())))) {
				throw new BizException("最多支持上个月、当前月以及下个月的排班导入！");
			}
		}
		return dayofMonth;
	}

	private boolean checkRowDataValid(HSSFRow row, Long deptId, String[] titles, int dayofMonth, String ym, List<ProcessMgt> passList,
			List<ProcessMgt> noPassList) {
		if (row != null) {
			HashMap<String, String> daysMap = new HashMap<String, String>();
			HashMap<String, String> ErrdaysMap = new HashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			ProcessMgt excelObj = new ProcessMgt();// 保存验证不通过的
			ProcessMgt mgtObj = new ProcessMgt();// 保存验证通过的
			boolean flag = true;
			Department deptDept = DepartmentCacheBiz.getDepartmentByID(deptId);

			// 判断工号
			String empCode = getCellStrValue(row.getCell(2));
			OutEmployee emp = null;
			if (StringUtils.isBlank(empCode)) {
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
			Date startdt = null;
			Date enddt = null;
			List<Scheduling> detailList = null;
			if (emp != null) {
				empCode = emp.getEmpCode();
				detailList = schedulingJdbcDao.getScheBy(deptId, ym, empCode);
				Collections.sort(detailList, new Comparator<Scheduling>() {
					public int compare(Scheduling arg0, Scheduling arg1) {
						return arg0.getSheduleDt().compareTo(arg1.getSheduleDt());
					}
				});
				if (detailList.size() > 0) {
					startdt = detailList.get(0).getSheduleDt();
					enddt = detailList.get(detailList.size() - 1).getSheduleDt();
				} else {
					sb.append(String.format("该员工在%s月无确认排班记录,请确认排班记录", ym));
					sb.append("\n");
					flag = false;
				}

			}
			for (int i = 3; i <= 3 + dayofMonth - 1; i++) {
				// 当前单元格对应的日期
				Date d2 = CommonUtil.getYmd(ym + '-' + CommonUtil.addZero(i - 2));

				HSSFCell c = row.getCell(i);
				if (c == null || StringUtil.isBlank(getCellStrValue(c))) {
					sb.append(String.format("工序安排不全，请补充完整！%s未排工序" ,titles[i - 3]));
					sb.append("\n");
					flag = false;
					continue;
				}
				String dayValue = getCellStrValue(c);
				if (startdt != null && enddt != null && CommonUtil.compareTowDate(startdt, d2) <= 0 && CommonUtil.compareTowDate(d2, enddt) <= 0) {
					Scheduling s1 = new Scheduling();
					s1.setDeptId(deptId);
					s1.setEmpCode(empCode);
					s1.setSheduleDt(d2);
					if (detailList != null && detailList.contains(s1)) {
						s1 = detailList.get(detailList.indexOf(s1));
						if (s1 != null) {
							if ("休".equals(s1.getScheduleCode()) || "OFF".equals(s1.getScheduleCode()) || "SW".equals(s1.getScheduleCode())) {
								daysMap.put("day" + (i - 2), "休");
							} else {
								if (StringUtils.isNotBlank(dayValue) && !"休".equals(dayValue.trim())) {
									Process proobj = processDao.findBy(deptId, dayValue);
									if (proobj == null ) {
										sb.append(String.format("%s所排工序不存在或无效" ,titles[i - 3]));
										sb.append("\n");
										flag = false;
									}
									daysMap.put("day" + (i - 2), dayValue);
								}else{
									sb.append(String.format("%s所排工序无效" ,titles[i - 3]));
									sb.append("\n");
									flag = false;
								}
							}
						}
					}
					Date empEndDt = null;
					if (emp != null) {
						empEndDt = getDimissionDate(emp);
					}
					// 该员工离职日期
					if (empEndDt != null) {
						if (CommonUtil.compareTowDate(d2, empEndDt) >= 0) {// 员工当天及之前已离职
							sb.append(String.format("该员工%s已离职" , ym + '-' + CommonUtil.addZero(i - 2)));
							sb.append("\n");
							flag = false;
						}
					}
				}
				// 将一月的所有班别放入set
				ErrdaysMap.put("day" + (i - 2), dayValue);
			}

			try {
				if (flag) {
					// 将mgtObj放入passList
					if (deptDept != null && deptDept.getId() != null && !StringUtils.isBlank(ym) && !StringUtils.isBlank(empCode)) {
						ProcessMgt obj = this.processMgtJdbcDao.findByCondition(deptId, ym, empCode);
						if (obj != null) {
							for (String key : daysMap.keySet()) {
								PropertyUtils.setProperty(obj, key, daysMap.get(key));
							}
							validWorkRepeat(passList, noPassList, empCode, obj, deptDept.getAreaDeptCode(), deptDept.getDeptCode());
						} else {
							mgtObj.setDeptId(deptDept.getId());
							mgtObj.setAreaCode(deptDept.getAreaDeptCode());
							mgtObj.setDeptCode(deptDept.getDeptCode());
							mgtObj.setEmpCode(empCode);
							mgtObj.setWorkType(emp.getWorkType());
							mgtObj.setYm(ym);
							for (String key : daysMap.keySet()) {
								PropertyUtils.setProperty(mgtObj, key, daysMap.get(key));
							}
							validWorkRepeat(passList, noPassList, empCode, mgtObj, deptDept.getAreaDeptCode(), deptDept.getDeptCode());
						}
					}
				} else {
					excelObj.setYm(ym);
					excelObj.setAreaCode(deptDept.getAreaDeptCode());
					excelObj.setDeptCode(deptDept.getDeptCode());
					excelObj.setEmpCode(empCode);
					for (String key : ErrdaysMap.keySet()) {
						PropertyUtils.setProperty(excelObj, key, ErrdaysMap.get(key));
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
		return false;

	}

	private Date getDimissionDate(OutEmployee emp) {
		return CommonUtil.getYMD(emp.getDimissionDt()) == null ? CommonUtil.getYmd("2114-01-01") : CommonUtil.getYMD(emp.getDimissionDt());
	}

	@SuppressWarnings("unused")
	private void validWorkRepeat(List<ProcessMgt> passList, List<ProcessMgt> noPassList, String empCode, ProcessMgt obj, String areaCode, String deptCode) {
		List<ProcessMgt> remove = new ArrayList<ProcessMgt>();
		if (passList.size() > 0) {
			for (ProcessMgt mgts : passList) {
				if (mgts.equals(obj)) {
					obj.setAreaCode(areaCode);
					obj.setDeptCode(deptCode);
					obj.setErrorDesc(String.format("%s工号有重复！", empCode));
					noPassList.add(obj);
					remove.add(mgts);
					mgts.setDeptCode(deptCode);
					mgts.setAreaCode(areaCode);
					mgts.setErrorDesc(String.format("%s工号有重复！", empCode));
					noPassList.add(mgts);
				}
			}
		}
		if (remove.size() > 0) {
			passList.removeAll(remove);
		} else {
			passList.add(obj);
		}
	}

	private HashMap<String, List<ProcessDetail>> convertMgt2Detail(List<ProcessMgt> passList) {
		HashMap<String, List<ProcessDetail>> m = new HashMap<String, List<ProcessDetail>>();
		for (ProcessMgt mgt : passList) {
			List<Scheduling> detailList = schedulingJdbcDao.getScheBy(mgt.getDeptId(), mgt.getYm(), mgt.getEmpCode());
			Collections.sort(detailList, new Comparator<Scheduling>() {
				public int compare(Scheduling arg0, Scheduling arg1) {
					return arg0.getSheduleDt().compareTo(arg1.getSheduleDt());
				}
			});
			int start = 0;
			int end = 0;
			if (detailList.size() > 0) {
				start = CommonUtil.getDayNumber(detailList.get(0).getSheduleDt());
				end = CommonUtil.getDayNumber(detailList.get(detailList.size() - 1).getSheduleDt());
			}

			List<ProcessDetail> scheduleList = new ArrayList<ProcessDetail>();
			for (int i = start; i <= end; i++) {
				Date processDt = CommonUtil.getYmd(mgt.getYm() + "-" + CommonUtil.addZero(i));
				ProcessDetail process = this.processMgtJdbcDao.findBy(mgt.getDeptId(), processDt, mgt.getEmpCode());
				if (process == null) {
					process = new ProcessDetail();
					process.setDeptId(mgt.getDeptId());
					process.setEmpCode(mgt.getEmpCode());
					process.setProcessDt(processDt);
				}
				try {
					Object proCode = PropertyUtils.getProperty(mgt, "day" + i);
					if (proCode != null && proCode.toString().trim().length() > 0) {
						process.setProcessCode(proCode.toString());
						process.setProcessDt(processDt);
						scheduleList.add(process);
					}

				} catch (Exception e) {
					log.error("error:", e);
					throw new BizException("操作失败！");
				}
			}
			m.put(mgt.getEmpCode(), scheduleList);
		}
		return m;
	}

	private String writeErrExcel(HSSFWorkbook errorWorkBook, FileOutputStream errorFos, List<ProcessMgt> noPassList, String deptCode, String ym) {
		HSSFCellStyle cellStyle = setCelllStyle(errorWorkBook);
		String fileName = String.format("工序安排导入错误信息_%s.xls", new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())) ;
		HSSFSheet sheet = errorWorkBook.getSheetAt(0);
		if (noPassList != null && noPassList.size() > 0) {
			HSSFRow row0 = sheet.getRow(0);
			row0.getCell(2).setCellValue(deptCode);
			row0.getCell(4).setCellValue(ym);
			HSSFRow row1 = sheet.getRow(1);
			row1.getCell(35).setCellValue("校验未通过原因");
			int rowNum = 1; // 操作行sdasda
			try {
				for (ProcessMgt m : noPassList) {
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
						Object obj = PropertyUtils.getProperty(m, "day" + (k - 2));
						cell.setCellValue(obj == null ? "" : obj.toString());
						cell.setCellStyle(cellStyle);
					}
					column++;
					HSSFCell cell36 = row.createCell(column++);
					cell36.setCellValue(m.getErrorDesc() == null ? "" : m.getErrorDesc());
					cell36.setCellStyle(cellStyle);
				}
				CommonUtil.setDownloadFileName(fileName);
				errorFos = new FileOutputStream(new File(CommonUtil.getSaveFilePath(ProcessMgt.class)));
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

	public boolean searchNoticesCount() {
		Long deptid = this.getCurrentUser().getEmployee().getDepartment().getId();
		String ym = CommonUtil.getNextMonthYm(new Date());
		return processMgtJdbcDao.allScheIsHasProcess(deptid, ym);
	}

	public boolean getCanConfirm(Long deptid, String ym) {
		if (ym.equals(CommonUtil.getNextMonthYm(new Date())) || ym.equals(CommonUtil.getYm(new Date())) || ym.equals(CommonUtil.getLastMonthYm(new Date()))) {
			return processMgtJdbcDao.allScheIsHasProcess(deptid, ym);
		} else {
			throw new BizException("提交确认只能选择上个月、当前月或下个月！");
		}
	}

	public boolean processMgtIsConfirm(Long deptid, String processMgtIds) {
		return processMgtDao.isHaveNoConfirmProcessMgt(deptid, processMgtIds);
	}

	public void deleteProcessMgt(Long deptid, String processMgtIds) {
		String [] processMgtIdsArray = processMgtIds.replaceAll(",$", "").split(",", -1);
		try {
			for (String id : processMgtIdsArray) {
				processMgtDao.remove(Long.parseLong(id));
				processMgtDao.delete(deptid, Long.parseLong(id));
			}
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("删除失败！");
		}
	}

	public void saveConfirmStatus(Long deptid, String ym) {
		if (ym.equals(CommonUtil.getLastMonthYm(new Date())) && fourPerMonthAtEightTwenty()) {
			throw new BizException("已逾期，不能提交确认该月排班！");
		}
		ProcessConfirmStatus mcs = new ProcessConfirmStatus();
		mcs.setDeptId(deptid);
		mcs.setYm(ym);
		mcs.setCommitStatus(1);
		processConfirmStatusDao.save(mcs);
	}

	private boolean fourPerMonthAtEightTwenty() {
		return Integer.parseInt(DateUtil.formatDate(new Date(), DateFormatType.DDHH)) - 420 > 0;
	}

	public void loadSendProcessMail() {
		/*String[] ym = CommonUtil.getYm(new Date()).split("-");
		List<Date> dates = getDates(Integer.valueOf(ym[0]), Integer.valueOf(ym[1]));
		Date currentDt = CommonUtil.currentDt();
		// 只在月底前2个工作日执行
		if (currentDt.compareTo(dates.get(dates.size() - 1)) == 0 || currentDt.compareTo(dates.get(dates.size() - 2)) == 0) {
			if (sendMailBiz.updateEmailStartStatus()) {
				List<OutEmployee> emailList = processMgtJdbcDao.getUnFinishedEmp(this.getCurrentUser().getEmployee().getDepartment().getId(),
						CommonUtil.getNextMonthYm(new Date()));
				if (emailList != null && emailList.size() > 0) {
					for (OutEmployee emp : emailList) {
						if (null == emp.getEmail() || "".equals(emp.getEmail())) {
							continue;
						}
						sendMailBiz.sendMail(emp.getEmail(), createContent("排工序"));
					}
				}
				sendMailBiz.updateEmailEndStatus();
			}
		}
	*/	
	}

	private String createContent(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head><body>");
		sb.append("<p style='width:780px;line-height:23px;'><b>" + "尊敬的用户：");
		sb.append("</b><br/><label style='width:2em;'></label>" + "您的" + CommonUtil.getNextMonthYm(new Date()) + "月份" + name + "还未完成，请尽快完成！" + "</p>");
		sb.append("<div style='color:gray;font-size:12px;'>系统发送邮件,请不要回复</div></body></html>");
		return sb.toString();
	}

	// 获取当月的工作日
	private List<Date> getDates(int year, int month) {
		List<Date> dates = new ArrayList<Date>();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);

		while (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month) {
			int day = cal.get(Calendar.DAY_OF_WEEK);

			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				dates.add((Date) cal.getTime().clone());
			}
			cal.add(Calendar.DATE, 1);
		}
		return dates;

	}

	@Override
	public String getConfirmExport() {
		try {
			String downloadFileName = String.format("已确认提交信息导出_%s.xls", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			CommonUtil.setDownloadFileName(downloadFileName);
			String exportFileName = CommonUtil.getSaveFilePath(GroupBaseInfo.class);
			File file = new File(exportFileName);
			if (!file.exists())
				file.createNewFile();
			FileInputStream template = new FileInputStream(CommonUtil.getReportTemplatePath("已提交确认信息.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(template);
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			List<ProcessConfirmStatus> confirmStatusList = processMgtJdbcDao.getConfirmList();
			HSSFSheet sheet;
			for (int i = 0; i <= confirmStatusList.size() / maxPageSize; i++) {
				if (i != confirmStatusList.size() / maxPageSize) {
					sheet = workbook.cloneSheet(0);
					workbook.setSheetName(workbook.getSheetIndex(sheet), "导出数据" + (i + 1));
					List<ProcessConfirmStatus> exportByPageBaseInfos = confirmStatusList.subList(i * maxPageSize, (i + 1) * maxPageSize);
					fillRowData(sheet, exportByPageBaseInfos, cellStyle);

				} else {
					if (confirmStatusList.size() % maxPageSize > 0) {
						sheet = workbook.cloneSheet(0);
						workbook.setSheetName(workbook.getSheetIndex(sheet), "导出数据" + (i + 1));
						List<ProcessConfirmStatus> exportByPageBaseInfos = confirmStatusList.subList(i * maxPageSize,
								i * maxPageSize + confirmStatusList.size() % maxPageSize);
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

	private void fillRowData(HSSFSheet sheet, List<ProcessConfirmStatus> confirmStatusList, HSSFCellStyle cellStyle) {
		int rowNum = 1; // 操作行
		for (ProcessConfirmStatus mcs : confirmStatusList) {
			int column = 0;
			HSSFRow row = sheet.createRow(++rowNum);

			HSSFCell cell1 = row.createCell(column++);
			cell1.setCellValue(mcs.getDeptId() == null ? "" : DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getAreaDeptCode());
			cell1.setCellStyle(cellStyle);

			HSSFCell cell2 = row.createCell(column++);
			cell2.setCellValue(mcs.getDeptId() == null ? "" : DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getCode());
			cell2.setCellStyle(cellStyle);

			HSSFCell cell3 = row.createCell(column++);
			cell3.setCellValue(mcs.getDeptId() == null ? "" : DepartmentCacheBiz.getDepartmentByID(mcs.getDeptId()).getName());
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
}