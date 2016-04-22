/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-23     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.context.ApplicationContext;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.cmscommon.biz.DepartmentCacheBiz;
import com.sf.module.department.domain.Department;
import com.sf.module.frameworkimplExtend.util.StringUtil;
import com.sf.module.vmsarrange.dao.IArrDepartmentDao;
import com.sf.module.vmsarrange.dao.IArrDriverDao;
import com.sf.module.vmsarrange.dao.IPreScheduleDao;
import com.sf.module.vmsarrange.dao.IPreScheduleDraftDao;
import com.sf.module.vmsarrange.dao.IScheduleArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleMonthRptDao;
import com.sf.module.vmsarrange.dao.ITransferClassesDFDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.ArrDriver;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;
import com.sf.module.vmsarrange.domain.TransferClassesDF;
import com.sf.module.vmsarrange.domain.TransferClassesRL;
import com.sf.module.vmsarrange.domain.VehicleCertificates;
import com.sf.module.vmsarrange.util.ArrCommonUtil;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsinfo.util.FileUtil;

/**
 *
 * PreScheduleDraft处理类
 *
 */
public class PreScheduleDraftBiz extends BaseBiz implements IPreScheduleDraftBiz {

	private IPreScheduleDraftDao preScheduleDraftDao;
	private ITransferClassesDFDao transferClassesDFDao;
	private IArrDriverDao arrDriverDao;
	private IScheduleArrangeDao scheduleArrangeDao;
	private IScheduleMonthRptDao scheduleMonthRptDao;
	private IPreScheduleDao preScheduleDao;
	private IArrDepartmentDao arrDepartmentDao;
	
	private static int MAX_SIZE = 65530;
	private static String TEMPLATE_NAME = "打印预排班模板.xls";
	private static String TEMPLATE_PRE_NAME = "预排班模板.xls";
	private static String TEMPLATE_REAL_NAME = "实际排班模板.xls";

	public void setTransferClassesDFDao(ITransferClassesDFDao transferClassesDFDao) {
		this.transferClassesDFDao = transferClassesDFDao;
	}

	public void setArrDepartmentDao(IArrDepartmentDao arrDepartmentDao) {
		this.arrDepartmentDao = arrDepartmentDao;
	}

	public void setScheduleArrangeDao(IScheduleArrangeDao scheduleArrangeDao) {
		this.scheduleArrangeDao = scheduleArrangeDao;
	}


	public void setArrDriverDao(IArrDriverDao arrDriverDao) {
		this.arrDriverDao = arrDriverDao;
	}


	public void setPreScheduleDraftDao(IPreScheduleDraftDao preScheduleDraftDao) {
		this.preScheduleDraftDao = preScheduleDraftDao;
	}


	public void setScheduleMonthRptDao(IScheduleMonthRptDao scheduleMonthRptDao) {
		this.scheduleMonthRptDao = scheduleMonthRptDao;
	}

	public void setPreScheduleDao(IPreScheduleDao preScheduleDao) {
		this.preScheduleDao = preScheduleDao;
	}

	/**
	 * 根据id，年月日查询排班明细
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findByCondition(Long id, String yearMonth,
			Integer dayNum) {
		if(FileUtil.isEmpty(yearMonth) || null == dayNum || null == id){
			throw new BizException("获取明细失败，提交的参数为空，请重试");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查找主记录
		PreScheduleDraft psd =  this.preScheduleDraftDao.load(id);
		if(null == psd){
			throw new BizException("获取明细失败，记录不存在，请刷新页面");
		}
		//获取需调班年月日
		String dateStr = yearMonth;
		int day = dayNum.intValue();
		if(day < 10){
			dateStr += "-"+"0"+day;
		}else{
			dateStr += "-"+day;
		}
		TransferClassesDF newTcf = null;
		//遍历获取需调班子记录
		if(null != psd && null != psd.getTransferClassesDFs() && !psd.getTransferClassesDFs().isEmpty()){
			for(TransferClassesDF tc: psd.getTransferClassesDFs()) {
				if(null == tc){
					continue;
				}
				String rDayDt = sdf.format(tc.getDayDt());
				if(rDayDt.equals(dateStr)) {
					newTcf = tc;
				}
			}
		}
		if(null == newTcf){
			return null;
		}
		//封装数据结构给前台方便调用
		Map map = new HashMap();
		map.put("scheduleId", psd.getId());
		map.put("driverCode", psd.getDriver().getEmpCode());
		map.put("driverName", psd.getDriver().getDriverName());
		map.put("classType", psd.getClassType());
		map.put("arrangeNo", newTcf.getArrangeDf()==null ? "" : newTcf.getArrangeDf().getArrangeNo());
		map.put("isValid", newTcf.getArrangeDf()==null ? "" : newTcf.getArrangeDf().getValid());
		map.put("dayDt", newTcf.getDayDt());
		map.put("remark", newTcf.getRemark());
		//封装班次明细数据到map
		List listSi = new ArrayList();
		if(null != newTcf.getArrangeDf() && null !=  newTcf.getArrangeDf().getScheduleArrangeInfos()
				&& ! newTcf.getArrangeDf().getScheduleArrangeInfos().isEmpty()){
			for(ScheduleInfoArrange sia:newTcf.getArrangeDf().getScheduleArrangeInfos()){
				if(null == sia){
					continue;
				}
				ScheduleInfo si = sia.getScheduleInfo();
				Map temp = new HashMap();
				temp.put("startTm", si.getStartTm());
				temp.put("endTm", si.getEndTm());
				if(null != si.getStartDept()){
					temp.put("startDept", si.getStartDept().getDeptCode()+"/"+si.getStartDept().getDeptName());
				}
				if(null != si.getEndDept()){
					temp.put("endDept", si.getEndDept().getDeptCode()+"/"+si.getEndDept().getDeptName());
				}
				temp.put("valid", si.getValid());
				listSi.add(temp);
			}
		}
		//机动班
		if(null != newTcf.getArrangeDf() 
				&& newTcf.getArrangeDf().getArrangeType().compareTo(1)==0
				&& (null ==  newTcf.getArrangeDf().getScheduleArrangeInfos() 
						|| newTcf.getArrangeDf().getScheduleArrangeInfos().isEmpty())){
			Map temp = new HashMap();
			temp.put("startTm", newTcf.getArrangeDf().getStartTm());
			temp.put("endTm", newTcf.getArrangeDf().getEndTm());
			temp.put("valid",  newTcf.getArrangeDf().getValid());
			listSi.add(temp);
		}
		map.put("scheduleInfos", listSi);
		return map;
	}
	/**
	 * 打印预排班
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public String listPreReport(String deptCode, String empCode,String yearMonth,Integer classType){
		String userCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		if(ArrFileUtil.isEmpty(deptCode)){
			throw new BizException("网点不能为空");
		}
		if(ArrFileUtil.isEmpty(yearMonth) || yearMonth.length()<7){
			throw new BizException("月份为空或格式错误");
		}
		String fileName = null;
		if(ArrFileUtil.isEmpty(userCode)){
			fileName = "预排班表.xls";
		}else{
			fileName = "预排班表("+userCode+").xls";
		}
		//创建EXCEL
		java.io.InputStream is = null;
		try {
			is = new FileInputStream(this.getTemplatePath(this.TEMPLATE_PRE_NAME));
		} catch (FileNotFoundException e) {
			log.error("1",e);
			throw new BizException("生成报表失败，模板文件不存在");
		} catch (Exception e) {
			log.error("2",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(is);
		} catch (IOException e) {
			log.error("3",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}	
		/**获取模板页及各列单元格样式**/
		HSSFSheet sheet = workbook.getSheetAt(0);
		if(sheet==null){
			throw new BizException("生成报表失败，模板中第一个sheet页为空");
		}
		/**单sheet页**/
		//获取预排班记录总数
		Long size = preScheduleDraftDao.listPreReportCount(deptCode, empCode, yearMonth, userId,classType);
		if(null != size && size.compareTo(Long.valueOf(MAX_SIZE)) > 0){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//1.获取数据
		List<Map> data = preScheduleDraftDao.listPreReport(deptCode, empCode, yearMonth, userId,classType);
		//2.校验=总数
		if(null != data && data.size() > MAX_SIZE){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//3.填充数据
		fillPreData(data,sheet,deptCode,yearMonth,false);
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("PreScheduleDraftBiz", "vmsarrange");
		} catch (Exception e) {
			log.error("4",e);
			throw new BizException("获取临时存储目录失败");
		}
		java.io.OutputStream os;
		try {
			os = new FileOutputStream(new File(savePath+File.separator+fileName));
		} catch (FileNotFoundException e) {
			log.error("5",e);
			throw new BizException("找不到临时存储目录");
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			log.error("6",e);
			throw new BizException("写文件失败");
		}
		if( null!=os ){
			try { os.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		if ( null!=is) {
			try { is.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		return savePath+File.separator+fileName;
	}
	/**
	 * 导出实际排班
	 * @throws Exception 
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public String listRealReport(String deptCode, String empCode,String yearMonth,Integer classType) throws Exception{
		String userCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		if(ArrFileUtil.isEmpty(deptCode)){
			throw new BizException("网点不能为空");
		}
		if(ArrFileUtil.isEmpty(yearMonth) || yearMonth.length()<7){
			throw new BizException("月份为空或格式错误");
		}
		String fileName = null;
		if(ArrFileUtil.isEmpty(userCode)){
			fileName = "实际排班表.xls";
		}else{
			fileName = "实际排班表("+userCode+").xls";
		}
		//创建EXCEL
		java.io.InputStream is = null;
		try {
			is = new FileInputStream(this.getTemplatePath(this.TEMPLATE_REAL_NAME));
		} catch (FileNotFoundException e) {
			log.error("1",e);
			throw new BizException("生成报表失败，模板文件不存在");
		} catch (Exception e) {
			log.error("2",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(is);
		} catch (IOException e) {
			log.error("3",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}	
		/**获取模板页及各列单元格样式**/
		HSSFSheet sheet = workbook.getSheetAt(0);
		if(sheet==null){
			throw new BizException("生成报表失败，模板中第一个sheet页为空");
		}
		/**单sheet页**/
		//判断日期，当月则计算当月的实际排班，如果是当月之前的日期，则从报表中获取指定月份的报表数据
		Long size;
		List<Map> data = new ArrayList<Map>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//取当月-精确到月
		Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
		//取选中月份-精确到月
		Date chooseMonth = sdf.parse(yearMonth);
		//当前月之后的月份
		if(chooseMonth.after(current)){
			throw new BizException("当前月份之后的月份，请选择预排班导出");
		}
		//当前月之前的月份
		if(chooseMonth.before(current)){
			//获取预排班记录总数
			size = scheduleMonthRptDao.listRealMonthReportCount(deptCode, empCode, yearMonth, userId,classType);
			if(null != size && size.compareTo(Long.valueOf(MAX_SIZE)) > 0){
				throw new BizException("数据过多(超过65530条)，请选择查询条件过滤数据");
			}
			//1.获取数据
			data = scheduleMonthRptDao.listRealMonthReport(deptCode, empCode, yearMonth, userId,classType);
		}
		//当前月
		if(chooseMonth.compareTo(current) == 0){
			//获取预排班记录总数
			size = preScheduleDraftDao.listRealReportCount(deptCode, empCode, yearMonth, userId,classType);
			if(null != size && size.compareTo(Long.valueOf(MAX_SIZE)) > 0){
				throw new BizException("数据过多(超过65530条)，请选择查询条件过滤数据");
			}
			//1.获取数据
			data = preScheduleDraftDao.listRealReport(deptCode, empCode, yearMonth, userId,classType);
		}
		//2.校验=总数
		if(null != data && data.size() > MAX_SIZE){
			throw new BizException("数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//3.填充数据
		fillRealData(data,sheet,deptCode,yearMonth,workbook);
		/**预排班sheet页**/
		//4.填充完成后，释放占用内存
		data = null;
		HSSFSheet secondSheet = workbook.getSheetAt(1);
		if(secondSheet==null){
			throw new BizException("生成报表失败，模板中第二个sheet页为空");
		}
		//获取预排班记录总数
		Long preSize = preScheduleDraftDao.listPreReportCount(deptCode, empCode, yearMonth, userId,classType);
		if(null != preSize && preSize.compareTo(Long.valueOf(MAX_SIZE)) > 0){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		/***1.填充预排班信息***/
		//1.获取数据
		List<Map> preData = preScheduleDraftDao.listPreReport(deptCode, empCode, yearMonth, userId,classType);
		//2.校验=总数
		if(null != preData && preData.size() > MAX_SIZE){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//3.填充数据
		fillPreData(preData,secondSheet,deptCode,yearMonth,true);
		
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("PreScheduleDraftBiz", "vmsarrange");
		} catch (Exception e) {
			log.error("4",e);
			throw new BizException("获取临时存储目录失败");
		}
		java.io.OutputStream os;
		try {
			os = new FileOutputStream(new File(savePath+File.separator+fileName));
		} catch (FileNotFoundException e) {
			log.error("5",e);
			throw new BizException("找不到临时存储目录");
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			log.error("6",e);
			throw new BizException("写文件失败");
		}
		if( null!=os ){
			try { os.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		if ( null!=is) {
			try { is.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		return savePath+File.separator+fileName;
	}
	//导出报表
	@SuppressWarnings({ "rawtypes", "static-access" })
	public String listReport(String deptCode, String empCode,String yearMonth,Integer classType) {
		String userCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		if(ArrFileUtil.isEmpty(deptCode)){
			throw new BizException("网点不能为空");
		}
		if(ArrFileUtil.isEmpty(yearMonth) || yearMonth.length()<7){
			throw new BizException("月份为空或格式错误");
		}
		String fileName = null;
		if(ArrFileUtil.isEmpty(userCode)){
			fileName = "打印预排班信息.xls";
		}else{
			fileName = "打印预排班信息("+userCode+").xls";
		}
		//创建EXCEL
		java.io.InputStream is = null;
		try {
			is = new FileInputStream(this.getTemplatePath(this.TEMPLATE_NAME));
		} catch (FileNotFoundException e) {
			log.error("1",e);
			throw new BizException("生成报表失败，模板文件不存在");
		} catch (Exception e) {
			log.error("2",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(is);
		} catch (IOException e) {
			log.error("3",e);
			throw new BizException("生成报表失败，模板文件读取异常");
		}	
		/**获取模板页及各列单元格样式**/
		HSSFSheet firstSheet = workbook.getSheetAt(0);
		if(firstSheet==null){
			throw new BizException("生成报表失败，模板中第一个sheet页为空");
		}
		HSSFSheet secondSheet = workbook.getSheetAt(1);
		if(secondSheet==null){
			throw new BizException("生成报表失败，模板中第二个sheet页为空");
		}
		/**单sheet页**/
		//获取预排班记录总数
		Long preSize = preScheduleDraftDao.listPreReportCount(deptCode, empCode, yearMonth, userId,classType);
		if(null != preSize && preSize.compareTo(Long.valueOf(MAX_SIZE)) > 0){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//获取配班信息记录总数
		Long arrSize = preScheduleDraftDao.listArrReportCount(deptCode, empCode, yearMonth, userId,classType);
		if(null != arrSize && arrSize.compareTo(Long.valueOf(MAX_SIZE)) > 0){
			throw new BizException("配班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		/***1.填充预排班信息***/
		//1.获取数据
		List<Map> preData = preScheduleDraftDao.listPreReport(deptCode, empCode, yearMonth, userId,classType);
		//2.校验=总数
		if(null != preData && preData.size() > MAX_SIZE){
			throw new BizException("预排班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//3.填充数据
		fillPreData(preData,firstSheet,deptCode,yearMonth,false);
		//4.填充完成后，释放占用内存
		preData = null;
		/***1.填充配班信息***/
		//1.获取数据
		List<Map> arrData = preScheduleDraftDao.listArrReport(deptCode, empCode, yearMonth, userId,classType);
		//2.校验=总数
		if(null != arrData && arrData.size() > MAX_SIZE){
			throw new BizException("配班数据过多(超过65530条)，请选择查询条件过滤数据");
		}
		//3.填充数据
		if(null != arrData && !arrData.isEmpty()){
			fillArrData(arrData,secondSheet);
		}
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("PreScheduleDraftBiz", "vmsarrange");
		} catch (Exception e) {
			log.error("4",e);
			throw new BizException("获取临时存储目录失败");
		}
		java.io.OutputStream os;
		try {
			os = new FileOutputStream(new File(savePath+File.separator+fileName));
		} catch (FileNotFoundException e) {
			log.error("5",e);
			throw new BizException("找不到临时存储目录");
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			log.error("6",e);
			throw new BizException("写文件失败");
		}
		if( null!=os ){
			try { os.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		if ( null!=is) {
			try { is.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		return savePath+File.separator+fileName;
	}
	/**
	 * 填充实际排班数据
	 * @param data
	 * @param sheet
	 * @param deptCode
	 * @param yearMonth
	 */
	@SuppressWarnings("rawtypes")
	private void fillRealData(List<Map> data,HSSFSheet sheet,String deptCode,String yearMonth,HSSFWorkbook workbook){
		if(null == sheet){
			return ;
		}
		//获取样式
		final int totalColumn = 42;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = sheet.getRow(3);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		
		HSSFCell cell;
		HSSFRow row;
		/***填充标题数据***/
		//1.标题
		row = sheet.getRow(0);
		if(null == row){
			row = sheet.createRow(0);
		}
		cell = row.getCell(0);
		if(null == cell){
			cell = row.createCell(0);
		}
		String title = "";
		if(!ArrFileUtil.isEmpty(deptCode)){
			Department d = DepartmentCacheBiz.getDepartmentByCode(deptCode);
			Department ad = null;
			//自己为区部则不取区部
			if(null != d && null != d.getAreaDeptCode() && d.getTypeLevel().compareTo(2L) != 0){
				ad = DepartmentCacheBiz.getDepartmentByCode(d.getAreaDeptCode());
			}
			String areaName = "";
			if(null != ad && null != ad.getDeptName()){
				title+=ad.getDeptName();
				areaName = ad.getDeptName();
			}
			//区部不是自己，则拼接
			if(null != d && null != d.getDeptName() 
					&& !d.getDeptName().equals(areaName)){
				title+=d.getDeptName();
			}
		}
		if(!ArrFileUtil.isEmpty(yearMonth) && yearMonth.length()>=7){
			title+=yearMonth.substring(0,4)+"年"+yearMonth.substring(5,7)+"月";
		}
		title+="排班表";
		cell.setCellValue(title);
		//2.导出网点
		row = sheet.getRow(1);
		if(null == row){
			row = sheet.createRow(1);
		}
		cell = row.getCell(1);
		if(null == cell){
			cell = row.createCell(1);
		}
		cell.setCellValue(deptCode);
		//3.导出日期
		row = sheet.getRow(1);
		if(null == row){
			row = sheet.createRow(1);
		}
		cell = row.getCell(5);
		if(null == cell){
			cell = row.createCell(5);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cell.setCellValue(sdf.format(new Date()));
		//数据填充起始行
		int startRowIdx = 3;
		//序号
		int rownum = 1;
		if(null == data || data.isEmpty()){
			return ;
		}
		if(null == style[6]){
			style[6] = workbook.createCellStyle();
		}
		HSSFCellStyle redStyle = workbook.createCellStyle();
		redStyle.cloneStyleFrom(style[6]);
		redStyle = this.fillForegroundColorRed(redStyle,workbook);
		//填充数据
		for(int i=0;i<data.size();i++){
			Map m = data.get(i);
			if(null == m){
				continue;
			}
			row = sheet.getRow(startRowIdx);
			if(null == row){
				row = sheet.createRow(startRowIdx);
			}
			//记录列号
			int columnIdx = 0;
			//序号列
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			cell.setCellValue(rownum);
			++rownum;
			++columnIdx;
			//地区
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("AREA_CODE")
					&& !ArrFileUtil.isEmpty(m.get("AREA_CODE").toString())){
				String deptCodeName = m.get("AREA_CODE").toString();
				Department d = DepartmentCacheBiz.getDepartmentByCode(deptCodeName);
				if(null != d){
					//deptCodeName = (null == d.getDeptCode()?"":d.getDeptCode())+"/"+(null == d.getDeptName()?"":d.getDeptName());
					deptCodeName = (null == d.getDeptName()?"":d.getDeptName());
				}
				cell.setCellValue(deptCodeName);
			}
			++columnIdx;
			//网点代码
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DEPT_CODE")
					&& !ArrFileUtil.isEmpty(m.get("DEPT_CODE").toString())){
				cell.setCellValue(m.get("DEPT_CODE").toString());
			}
			++columnIdx;
			//驾驶员姓名
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DRIVER_NAME")
					&& !ArrFileUtil.isEmpty(m.get("DRIVER_NAME").toString())){
				cell.setCellValue(m.get("DRIVER_NAME").toString());
			}
			++columnIdx;
			//驾驶员工号
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("EMP_CODE")
					&& !ArrFileUtil.isEmpty(m.get("EMP_CODE").toString())){
				cell.setCellValue(m.get("EMP_CODE").toString());
			}
			++columnIdx;
			//班次类别
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("CLASS_TYPE")
					&& !ArrFileUtil.isEmpty(m.get("CLASS_TYPE").toString())){
				cell.setCellValue(m.get("CLASS_TYPE").toString());
			}
			++columnIdx;
			/**1-31号**/
			//1.设置样式
			for(int k=0;k<31;k++){
				cell = row.getCell(columnIdx);
				if(null == cell){
					cell = row.createCell(columnIdx);
				}
				String value = null;
				switch(k){
				case 0:value = (null != m.get("ONE"))?m.get("ONE").toString():"";break;
				case 1:value = (null != m.get("TWO"))?m.get("TWO").toString():"";break;
				case 2:value = (null != m.get("THREE"))?m.get("THREE").toString():"";break;
				case 3:value = (null != m.get("FOUR"))?m.get("FOUR").toString():"";break;
				case 4:value = (null != m.get("FIVE"))?m.get("FIVE").toString():"";break;
				case 5:value = (null != m.get("SIX"))?m.get("SIX").toString():"";break;
				case 6:value = (null != m.get("SEVEN"))?m.get("SEVEN").toString():"";break;
				case 7:value = (null != m.get("EIGHT"))?m.get("EIGHT").toString():"";break;
				case 8:value = (null != m.get("NINE"))?m.get("NINE").toString():"";break;
				case 9:value = (null != m.get("TEN"))?m.get("TEN").toString():"";break;
				case 10:value = (null != m.get("ELEVEN"))?m.get("ELEVEN").toString():"";break;
				case 11:value = (null != m.get("TWELVE"))?m.get("TWELVE").toString():"";break;
				case 12:value = (null != m.get("THIRTEEN"))?m.get("THIRTEEN").toString():"";break;
				case 13:value = (null != m.get("FOURTEEN"))?m.get("FOURTEEN").toString():"";break;
				case 14:value = (null != m.get("FIFTEEN"))?m.get("FIFTEEN").toString():"";break;
				case 15:value = (null != m.get("SIXTEEN"))?m.get("SIXTEEN").toString():"";break;
				case 16:value = (null != m.get("SEVENTEEN"))?m.get("SEVENTEEN").toString():"";break;
				case 17:value = (null != m.get("EIGHTEEN"))?m.get("EIGHTEEN").toString():"";break;
				case 18:value = (null != m.get("NINETEEN"))?m.get("NINETEEN").toString():"";break;
				case 19:value = (null != m.get("TWENTY"))?m.get("TWENTY").toString():"";break;
				case 20:value = (null != m.get("TWENTY_ONE"))?m.get("TWENTY_ONE").toString():"";break;
				case 21:value = (null != m.get("TWENTY_TWO"))?m.get("TWENTY_TWO").toString():"";break;
				case 22:value = (null != m.get("TWENTY_THREE"))?m.get("TWENTY_THREE").toString():"";break;
				case 23:value = (null != m.get("TWENTY_FOUR"))?m.get("TWENTY_FOUR").toString():"";break;
				case 24:value = (null != m.get("TWENTY_FIVE"))?m.get("TWENTY_FIVE").toString():"";break;
				case 25:value = (null != m.get("TWENTY_SIX"))?m.get("TWENTY_SIX").toString():"";break;
				case 26:value = (null != m.get("TWENTY_SEVEN"))?m.get("TWENTY_SEVEN").toString():"";break;
				case 27:value = (null != m.get("TWENTY_EIGHT"))?m.get("TWENTY_EIGHT").toString():"";break;
				case 28:value = (null != m.get("TWENTY_NINE"))?m.get("TWENTY_NINE").toString():"";break;
				case 29:value = (null != m.get("THIRTY"))?m.get("THIRTY").toString():"";break;
				case 30:value = (null != m.get("THIRTY_ONE"))?m.get("THIRTY_ONE").toString():"";break;
				}
				if(!ArrFileUtil.isEmpty(value)){
					cell.setCellValue(value);
				}
				if(null != style[columnIdx]){
					if(!ArrFileUtil.isEmpty(value) && value.indexOf("旷")!=-1){
						//设置红色
						cell.setCellStyle(redStyle);
					}else{
						cell.setCellStyle(style[columnIdx]);
					}
				}
				++columnIdx;
			}
			//月度平均出勤时长-排班
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("ATTENDANCE_TM")
					&& !ArrFileUtil.isEmpty(m.get("ATTENDANCE_TM").toString())){
				cell.setCellValue(m.get("ATTENDANCE_TM").toString());
			}
			++columnIdx;
			//月度平均驾驶时长--实际
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DRIVING_TM")
					&& !ArrFileUtil.isEmpty(m.get("DRIVING_TM").toString())){
				cell.setCellValue(m.get("DRIVING_TM").toString());
			}
			++columnIdx;
			//月度平均驾驶时长--排班
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(m.containsKey("REAL_DRIVING_TM") && null != m.get("REAL_DRIVING_TM")
					&& !ArrFileUtil.isEmpty(m.get("REAL_DRIVING_TM").toString())){
				cell.setCellValue(m.get("REAL_DRIVING_TM").toString());
			}
			++columnIdx;
			//实际休息天数
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("REAL_DAY")
					&& !ArrFileUtil.isEmpty(m.get("REAL_DAY").toString())){
				cell.setCellValue(m.get("REAL_DAY").toString());
			}
			++columnIdx;
			//排版吻合率
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("RATE")
					&& !ArrFileUtil.isEmpty(m.get("RATE").toString())){
				cell.setCellValue(m.get("RATE").toString());
			}
			++columnIdx;
			++startRowIdx;
		}
	}
	/**
	 * 填充红色
	 * @param cellStyle
	 * @return
	 */
    private  HSSFCellStyle fillForegroundColorRed(HSSFCellStyle cellStyle,HSSFWorkbook workbook) {
    	//cellStyle.setFillForegroundColor(HSSFColor.DARK_RED.index);
		//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	HSSFFont font = workbook.createFont();
    	font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		font.setColor(HSSFFont.COLOR_RED);
		cellStyle.setFont(font);
        return cellStyle;
    }
	/**
	 * 填充预排班数据
	 */
	@SuppressWarnings({ "rawtypes" })
	private void fillPreData(List<Map> data,HSSFSheet sheet,String deptCode,String yearMonth,boolean addFlg){
		if(null == sheet){
			return ;
		}
		//获取样式
		final int totalColumn = 40;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = sheet.getRow(3);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		
		HSSFCell cell;
		HSSFRow row;
		/***填充标题数据***/
		//1.标题
		row = sheet.getRow(0);
		if(null == row){
			row = sheet.createRow(0);
		}
		cell = row.getCell(0);
		if(null == cell){
			cell = row.createCell(0);
		}
		String title = "";
		if(!ArrFileUtil.isEmpty(deptCode)){
			Department d = DepartmentCacheBiz.getDepartmentByCode(deptCode);
			Department ad = null;
			//自己为区部则不取区部
			if(null != d && null != d.getAreaDeptCode() && d.getTypeLevel().compareTo(2L) != 0){
				ad = DepartmentCacheBiz.getDepartmentByCode(d.getAreaDeptCode());
			}
			String areaName = "";
			if(null != ad && null != ad.getDeptName()){
				title+=ad.getDeptName();
				areaName = ad.getDeptName();
			}
			//区部不是自己，则拼接
			if(null != d && null != d.getDeptName() 
					&& !d.getDeptName().equals(areaName)){
				title+=d.getDeptName();
			}
		}
		if(!ArrFileUtil.isEmpty(yearMonth) && yearMonth.length()>=7){
			title+=yearMonth.substring(0,4)+"年"+yearMonth.substring(5,7)+"月";
		}
		title+="预排班表";
		cell.setCellValue(title);
		//2.导出网点
		row = sheet.getRow(1);
		if(null == row){
			row = sheet.createRow(1);
		}
		cell = row.getCell(1);
		if(null == cell){
			cell = row.createCell(1);
		}
		cell.setCellValue(deptCode);
		//3.导出日期
		row = sheet.getRow(1);
		if(null == row){
			row = sheet.createRow(1);
		}
		cell = row.getCell(5);
		if(null == cell){
			cell = row.createCell(5);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		cell.setCellValue(sdf.format(new Date()));
		//数据填充起始行
		int startRowIdx = 3;
		//序号
		int rownum = 1;
		if(null == data || data.isEmpty()){
			return ;
		}
		//填充数据
		for(int i=0;i<data.size();i++){
			Map m = data.get(i);
			if(null == m){
				continue;
			}
			row = sheet.getRow(startRowIdx);
			if(null == row){
				row = sheet.createRow(startRowIdx);
			}
			//记录列号
			int columnIdx = 0;
			//序号列
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			cell.setCellValue(rownum);
			++rownum;
			++columnIdx;
			//地区
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("AREA_CODE")
					&& !ArrFileUtil.isEmpty(m.get("AREA_CODE").toString())){
				String deptCodeName = m.get("AREA_CODE").toString();
				Department d = DepartmentCacheBiz.getDepartmentByCode(deptCodeName);
				if(null != d){
					//deptCodeName = (null == d.getDeptCode()?"":d.getDeptCode())+"/"+(null == d.getDeptName()?"":d.getDeptName());
					deptCodeName = (null == d.getDeptName()?"":d.getDeptName());
				}
				cell.setCellValue(deptCodeName);
			}
			++columnIdx;
			//网点代码
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DEPT_CODE")
					&& !ArrFileUtil.isEmpty(m.get("DEPT_CODE").toString())){
				cell.setCellValue(m.get("DEPT_CODE").toString());
			}
			++columnIdx;
			//驾驶员姓名
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DRIVER_NAME")
					&& !ArrFileUtil.isEmpty(m.get("DRIVER_NAME").toString())){
				cell.setCellValue(m.get("DRIVER_NAME").toString());
			}
			++columnIdx;
			//驾驶员工号
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("EMP_CODE")
					&& !ArrFileUtil.isEmpty(m.get("EMP_CODE").toString())){
				cell.setCellValue(m.get("EMP_CODE").toString());
			}
			++columnIdx;
			//班次类别
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("CLASS_TYPE")
					&& !ArrFileUtil.isEmpty(m.get("CLASS_TYPE").toString())){
				cell.setCellValue(m.get("CLASS_TYPE").toString());
			}
			++columnIdx;
			/**1-31号**/
			//1.设置样式
			for(int k=0;k<31;k++){
				cell = row.getCell(columnIdx);
				if(null == cell){
					cell = row.createCell(columnIdx);
				}
				if(null != style[columnIdx]){
					cell.setCellStyle(style[columnIdx]);
				}
				String value = null;
				switch(k){
				case 0:value = (null != m.get("ONE"))?m.get("ONE").toString():"";break;
				case 1:value = (null != m.get("TWO"))?m.get("TWO").toString():"";break;
				case 2:value = (null != m.get("THREE"))?m.get("THREE").toString():"";break;
				case 3:value = (null != m.get("FOUR"))?m.get("FOUR").toString():"";break;
				case 4:value = (null != m.get("FIVE"))?m.get("FIVE").toString():"";break;
				case 5:value = (null != m.get("SIX"))?m.get("SIX").toString():"";break;
				case 6:value = (null != m.get("SEVEN"))?m.get("SEVEN").toString():"";break;
				case 7:value = (null != m.get("EIGHT"))?m.get("EIGHT").toString():"";break;
				case 8:value = (null != m.get("NINE"))?m.get("NINE").toString():"";break;
				case 9:value = (null != m.get("TEN"))?m.get("TEN").toString():"";break;
				case 10:value = (null != m.get("ELEVEN"))?m.get("ELEVEN").toString():"";break;
				case 11:value = (null != m.get("TWELVE"))?m.get("TWELVE").toString():"";break;
				case 12:value = (null != m.get("THIRTEEN"))?m.get("THIRTEEN").toString():"";break;
				case 13:value = (null != m.get("FOURTEEN"))?m.get("FOURTEEN").toString():"";break;
				case 14:value = (null != m.get("FIFTEEN"))?m.get("FIFTEEN").toString():"";break;
				case 15:value = (null != m.get("SIXTEEN"))?m.get("SIXTEEN").toString():"";break;
				case 16:value = (null != m.get("SEVENTEEN"))?m.get("SEVENTEEN").toString():"";break;
				case 17:value = (null != m.get("EIGHTEEN"))?m.get("EIGHTEEN").toString():"";break;
				case 18:value = (null != m.get("NINETEEN"))?m.get("NINETEEN").toString():"";break;
				case 19:value = (null != m.get("TWENTY"))?m.get("TWENTY").toString():"";break;
				case 20:value = (null != m.get("TWENTY_ONE"))?m.get("TWENTY_ONE").toString():"";break;
				case 21:value = (null != m.get("TWENTY_TWO"))?m.get("TWENTY_TWO").toString():"";break;
				case 22:value = (null != m.get("TWENTY_THREE"))?m.get("TWENTY_THREE").toString():"";break;
				case 23:value = (null != m.get("TWENTY_FOUR"))?m.get("TWENTY_FOUR").toString():"";break;
				case 24:value = (null != m.get("TWENTY_FIVE"))?m.get("TWENTY_FIVE").toString():"";break;
				case 25:value = (null != m.get("TWENTY_SIX"))?m.get("TWENTY_SIX").toString():"";break;
				case 26:value = (null != m.get("TWENTY_SEVEN"))?m.get("TWENTY_SEVEN").toString():"";break;
				case 27:value = (null != m.get("TWENTY_EIGHT"))?m.get("TWENTY_EIGHT").toString():"";break;
				case 28:value = (null != m.get("TWENTY_NINE"))?m.get("TWENTY_NINE").toString():"";break;
				case 29:value = (null != m.get("THIRTY"))?m.get("THIRTY").toString():"";break;
				case 30:value = (null != m.get("THIRTY_ONE"))?m.get("THIRTY_ONE").toString():"";break;
				}
				if(!ArrFileUtil.isEmpty(value)){
					cell.setCellValue(value);
				}
				++columnIdx;
			}
			//计划休息天数
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			//ps:填充数据为文本 ，需根据实际小数位做下列修改：1.修改模板格式为数值并指定小数位 2.转换填充的字符串为Integer或Double
			if(null != m.get("PLAN_DAY")
					&& !ArrFileUtil.isEmpty(m.get("PLAN_DAY").toString())){
				cell.setCellValue(m.get("PLAN_DAY").toString());
			}
			++columnIdx;
			if(addFlg){
				//出勤时长
				cell = row.getCell(columnIdx);
				if(null == cell){
					cell = row.createCell(columnIdx);
				}
				if(null != style[columnIdx]){
					cell.setCellStyle(style[columnIdx]);
				}
				if(null != m.get("ATTENDANCE_TM")
						&& !ArrFileUtil.isEmpty(m.get("ATTENDANCE_TM").toString())){
					cell.setCellValue(m.get("ATTENDANCE_TM").toString());
				}
				++columnIdx;
				//驾驶时长
				cell = row.getCell(columnIdx);
				if(null == cell){
					cell = row.createCell(columnIdx);
				}
				if(null != style[columnIdx]){
					cell.setCellStyle(style[columnIdx]);
				}
				if(null != m.get("DRIVING_TM")
						&& !ArrFileUtil.isEmpty(m.get("DRIVING_TM").toString())){
					cell.setCellValue(m.get("DRIVING_TM").toString());
				}
				++columnIdx;
			}
			++startRowIdx;
		}
	}
	/**
	 * 填充数据
	 */
	@SuppressWarnings({ "rawtypes" })
	private void fillArrData(List<Map> data,HSSFSheet sheet){
		if(null == data || data.isEmpty()){
			return ;
		}
		if(null == sheet){
			return ;
		}
		//获取样式
		final int totalColumn = 10;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = sheet.getRow(1);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		
		HSSFCell cell;
		HSSFRow row;
		//数据填充起始行
		int startRowIdx = 1;
		//总列数
		final int columnTotal = 10;
		//序号
		int rownum = 1;
		//填充数据
		for(int i=0;i<data.size();i++){
			Map m = data.get(i);
			if(null == m){
				continue;
			}
			row = sheet.getRow(startRowIdx);
			if(null == row){
				row = sheet.createRow(startRowIdx);
			}
			//记录列号
			int columnIdx = 0;
			//序号列
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			cell.setCellValue(rownum);
			++rownum;
			++columnIdx;
			//班次代码
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("ARRANGE_NO")
					&& !ArrFileUtil.isEmpty(m.get("ARRANGE_NO").toString())){
				cell.setCellValue(m.get("ARRANGE_NO").toString());
			}
			++columnIdx;
			//是否有效
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("VALID")
					&& !ArrFileUtil.isEmpty(m.get("VALID").toString())){
				cell.setCellValue(m.get("VALID").toString());
			}
			++columnIdx;
			//班次明细
			if(null != m.get("ARRANGE_TYPE") 
					&& m.get("ARRANGE_TYPE").toString().equals("2")){
				/**非机动班填充明细***/
				String arrangeNoCompare = (null ==  m.get("ARRANGE_NO")?"":m.get("ARRANGE_NO").toString());
				List<ScheduleInfo> infos = new ArrayList<ScheduleInfo>();
				//向下遍历数据
				int j=i;
				for(;j<data.size();j++){
					Map mbak = data.get(j);
					if(null == mbak){
						continue;
					}
					String arrangeNoForCompare = "";
					if(null != mbak.get("ARRANGE_NO") 
							&& !ArrFileUtil.isEmpty(mbak.get("ARRANGE_NO").toString())){
						arrangeNoForCompare = mbak.get("ARRANGE_NO").toString();
					}
					//不是同一个配班时退出循环
					if(!arrangeNoCompare.equals(arrangeNoForCompare)){
						break;
					}
					//转换map为班次信息实体
					ScheduleInfo info = new ScheduleInfo();
					if(null != mbak.get("INFO_START_TM")
							&& !ArrFileUtil.isEmpty(mbak.get("INFO_START_TM").toString())){
						info.setStartTm(mbak.get("INFO_START_TM").toString());
					}
					if(null != mbak.get("INFO_END_TM")
							&& !ArrFileUtil.isEmpty(mbak.get("INFO_END_TM").toString())){
						info.setEndTm(mbak.get("INFO_END_TM").toString());
					}
					if(null != mbak.get("START_DEPT")
							&& !ArrFileUtil.isEmpty(mbak.get("START_DEPT").toString())){
						info.setStartDeptCodeName(mbak.get("START_DEPT").toString());
					}
					if(null != mbak.get("END_DEPT")
							&& !ArrFileUtil.isEmpty(mbak.get("END_DEPT").toString())){
						info.setEndDeptCodeName(mbak.get("END_DEPT").toString());
					}
					if(null != mbak.get("INFO_VALID")
							&& !ArrFileUtil.isEmpty(mbak.get("INFO_VALID").toString())){
						info.setValidTxt(mbak.get("INFO_VALID").toString());
					}
					if(null != mbak.get("DEPT")
							&& !ArrFileUtil.isEmpty(mbak.get("DEPT").toString())){
						info.setDeptCodeName(mbak.get("DEPT").toString());
					}
					if(null != mbak.get("VEHICLE_CODE")
							&& !ArrFileUtil.isEmpty(mbak.get("VEHICLE_CODE").toString())){
						info.setVehicleCode(mbak.get("VEHICLE_CODE").toString());
					}
					infos.add(info);
				}
				//退回到当前行
				i = j-1;
				//填充班次明细
				if(null == infos || infos.isEmpty()){
					throw new BizException(String.format("有异常数据(没有班次明细),异常数据:%s",arrangeNoCompare));
				}
				//班次明细排序
				try{
					infos = this.sortList(infos);
				}catch(Exception e){
					log.error("info:sortList failure",e);
				}
				/**合并行**/
				//从当前行开始写
				int rowIdx = startRowIdx;
				//标识是否有取下一行
				boolean flg = false;
				for(ScheduleInfo info:infos){
					if(null == info){
						continue;
					}
					//取行
					row = sheet.getRow(rowIdx);
					if(null == row){
						row = sheet.createRow(rowIdx);
					}
					//设置需要合并行的样式
					for(int cellIdx=0;cellIdx<3;cellIdx++){
						cell = row.getCell(cellIdx);
						if(null == cell){
							cell = row.createCell(cellIdx);
						}
						if(null != style[cellIdx]){
							cell.setCellStyle(style[cellIdx]);
						}
					}
					int colIdx = 3;
					//出车时间
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getStartTm())){
						cell.setCellValue(info.getStartTm());
					}
					++colIdx;
					//收车时间
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getEndTm())){
						cell.setCellValue(info.getEndTm());
					}
					++colIdx;
					//始发网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getStartDeptCodeName())){
						cell.setCellValue(info.getStartDeptCodeName());
					}
					++colIdx;
					//目的网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getEndDeptCodeName())){
						cell.setCellValue(info.getEndDeptCodeName());
					}
					++colIdx;
					//是否有效
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getValidTxt())){
						cell.setCellValue(info.getValidTxt());
					}
					++colIdx;
					//归属网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getDeptCodeName())){
						cell.setCellValue(info.getDeptCodeName());
					}
					++colIdx;
					//车牌号
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getVehicleCode())){
						cell.setCellValue(info.getVehicleCode());
					}
					++colIdx;
					//取下一行
					flg = true;
					++rowIdx;
				}
				//退回到当前行
				if(flg){
					--rowIdx;
				}
				//有多行则合并行
				if(startRowIdx != rowIdx){
					int firstRow = startRowIdx,lastRow = rowIdx;
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 2, 2));
				}
				//设置行为当前行
				startRowIdx = rowIdx;
			}else{
				/**机动班填充出收车时间***/
				int colIdx = 3;
				//出车时间
				cell = row.getCell(colIdx);
				if(null == cell){
					cell = row.createCell(colIdx);
				}
				if(null != style[colIdx]){
					cell.setCellStyle(style[colIdx]);
				}
				if(null != m.get("START_TM")
						&& !ArrFileUtil.isEmpty(m.get("START_TM").toString())){
					cell.setCellValue(m.get("START_TM").toString());
				}
				++colIdx;
				//收车时间
				cell = row.getCell(colIdx);
				if(null == cell){
					cell = row.createCell(colIdx);
				}
				if(null != style[colIdx]){
					cell.setCellStyle(style[colIdx]);
				}
				if(null != m.get("END_TM")
						&& !ArrFileUtil.isEmpty(m.get("END_TM").toString())){
					cell.setCellValue(m.get("END_TM").toString());
				}
				++colIdx;
				//设置剩余单元格样式
				for(;colIdx<columnTotal;colIdx++){
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
				}
			}
			++startRowIdx;
		}
	}
	/**
	 * List排序
	 * @param infos
	 * @return
	 */
	private List<ScheduleInfo> sortList(List<ScheduleInfo> infos){
		Collections.sort(infos, new Comparator<ScheduleInfo>(){
			public int compare(ScheduleInfo o1,ScheduleInfo o2) {
				if(null == o1 || null == o2){
					return 0;
				}
				Integer o1part1=null,o1part2=null,o2part1=null,o2part2=null;
				try{
					o1part1 = Integer.parseInt(o1.getStartTm().split(":", -1)[0]);
					o1part2 = Integer.parseInt(o1.getStartTm().split(":", -1)[1]);
					o2part1 = Integer.parseInt(o2.getStartTm().split(":", -1)[0]);
					o2part2 = Integer.parseInt(o2.getStartTm().split(":", -1)[1]);
				}catch(Exception e){
					log.error("info:sortList",e);
				}
				if(null == o1part1 || null == o1part2 || null == o2part1 || null == o2part2){
					return 0;
				}
				return this.compareTm(o1part1, o1part2, o2part1, o2part2);
			}
			private int compareTm(Integer leftHour, Integer leftMinute,
					Integer rightHour, Integer rightMinute) {
				// 比较时钟
				if (leftHour.compareTo(rightHour) > 0) {
					return 1;
				}
				if (leftHour.compareTo(rightHour) < 0) {
					return -1;
				}
				// 时钟相等比较分钟
				if (leftMinute.compareTo(rightMinute) > 0) {
					return 1;
				}
				if (leftMinute.compareTo(rightMinute) < 0) {
					return -1;
				}
				// 时钟分钟都相等返回0
				return 0;
			}
			
		});
		return infos;
	}
	/**
	 * 获取报表模板
	 * @param templateFile
	 * @return
	 * @throws Exception
	 */
	private String getTemplatePath(String templateName)throws Exception{
		String packageName = this.getClass().getPackage().getName();
		String moduleName = packageName.replaceAll("^com.sf.module.", "");
		moduleName = moduleName.substring(0, moduleName.indexOf("."));
		String webRoot = ApplicationContext.getContext().getServletContext().getRealPath("/");
		String tmp_TemplateFile = webRoot + "pages" + File.separator + moduleName 
				+ File.separator + "template" + File.separator + templateName;
		return tmp_TemplateFile;
	}

	/**
	 * 上传文件
	 * @throws ParseException 
	 */
	public void saveFile(File uploadFile, String fileName) throws ParseException {
		if(null == uploadFile) {
			throw new BizException("接收到的上传文件为空，请重新上传");
		}
		if(uploadFile.exists()){
			HSSFWorkbook workbook = null;
			try {
				workbook = new HSSFWorkbook(uploadFile.toURL().openStream());
			} catch (Exception e) {
				log.error("",e);
				throw new BizException("读取上传文件失败，请重新上传");
			}
			HSSFSheet sheet = workbook.getSheetAt(0);
			readSheet(sheet);	
		}else{
			throw new BizException("所选文件不存在，请重新选择文件上传");
		}
		
	}
	//读取导入sheet页
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readSheet(HSSFSheet sheet) throws ParseException {	
		String createdEmpCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		final int rightColNum = 37;
		final int startRow = 1;
		//检验表格是否为空
		if( null == sheet ){
			throw new BizException("文件填写有误，第一个sheet页为空");
		}
		//转换sheet数据成list map对象集合
		List<Map<String,String>> datas = new ArrayList<Map<String,String>>();	
		if(sheet.getRow(0)==null){
			throw new BizException("文件填写有误，第一行(标题行)为空");
		}
		//判断列数是否正确
		if(sheet.getRow(0).getLastCellNum() < rightColNum){
			throw new BizException(String.format("文件填写有误,第一行(标题行)列数有误，正确列数为%s列"
					,rightColNum));
		}
		//获取数据
		try {
			int dataIdx = 0;
			String yearMonthBak = null;
			for(int i=startRow;i<=sheet.getLastRowNum();i++){
				HSSFRow row = sheet.getRow(i);
				//过滤空行
				if(null == row){
					continue;
				}
				// 获取对应的数据行,不获取空行数据
				Map dataMap = new HashMap<String,String>(rightColNum);
				boolean hasValue = false; // 标志是否不为空行
				int colIdx = 0;
				String value = null;
				//地区
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("areaCode",value);
				colIdx++;
				//网点代码
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("deptCode",value);
				colIdx++;
				//驾驶员名字
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("driverName",value);
				colIdx++;
				//驾驶员工号
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("empCode",value);
				colIdx++;
				//班次类型
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("classType",value);
				colIdx++;
				//年月
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put("yearMonth",value);
				colIdx++;
				for(int j=0;j<31;j++){
					//1-31号
					value = getHSSFCellValue(row.getCell(colIdx));
					if(!ArrFileUtil.isEmpty(value)){
						hasValue = true;
					}
					dataMap.put(""+(j+1),value);
					colIdx++;
				}
				if(hasValue){
					if(null == dataMap.get("yearMonth")
							|| ArrFileUtil.isEmpty(dataMap.get("yearMonth").toString())){
						throw new BizException("月份列不能有空值");
					}
					if(null == yearMonthBak){
						yearMonthBak = dataMap.get("yearMonth").toString();
					}else{
						if(!yearMonthBak.equals(dataMap.get("yearMonth").toString())){
							throw new BizException("不能同时导入不同月份的数据，请根据月份分批导入");
						}
					}
					++dataIdx;
					datas.add(dataMap);
					if(dataIdx > 10000){
						throw new BizException("数据量过大(超出1万条)，请分批导入");
					}
				}
				
			}
		} catch(BizException e){
			throw e;
		}catch (Exception e) {
			log.error("",e);
			throw new BizException("导入失败,表格数据转换异常,请检查表格中是否存在引用或特殊格式");
		}
		
		//检验数据是否为空
		if( null==datas || datas.isEmpty()){
			throw new BizException("导入失败，表格数据为空");
		}
		/**
		 * 检验数据的有效性，并将有效记录存入list实体集合
		 * 若全部有效则通过
		 * 若存在无效记录，则返回无效原因
		 */
		int wrongNum = 0;
		StringBuilder wrongMsg = new StringBuilder();
		//存储所有校验都同的记录
		String empCode = null,deptCode,classType,yearMonth = null;
		String[] days31 = new String[31];
		/***分两步校验 1.校验数据本身 2.与系统数据对比**/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//取当月-精确到月
		Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
		//取次月-精确到月
		Date nextMonth = DateUtils.truncate(DateUtils.addMonths(current, 1),Calendar.MONTH);
		//1.校验数据本身
		//标识是当月还是次月：1当月 2次月
		int currentOrNext = 0;
		//工号集合
		List<String> empCodes = new ArrayList<String>();
		//实际排班集合
		List<PreSchedule> pres = new ArrayList<PreSchedule>();
		//预排班集合
		List<PreScheduleDraft> preDrafts = new ArrayList<PreScheduleDraft>();
		PreScheduleDraft preDraft;
		PreSchedule pre;
		//预排班子记录集合
		List<TransferClassesDF> classDfs = new ArrayList<TransferClassesDF>();
		//实际排班子记录集合
		List<TransferClassesRL> classRls = new ArrayList<TransferClassesRL>();
		TransferClassesDF classDf;
		TransferClassesRL classRl;
		for(int rowIndex = 0;rowIndex<datas.size();rowIndex++){
			days31 = new String[31];
			//错误行数超过100行，退出校验
			if(wrongNum >= 100){
				wrongMsg.append("错误行数过多已中止校验(请修正后再导入)");
				break;
			}
			List<String> arrangeNoList = new ArrayList<String>();
			StringBuilder wrongStr = new StringBuilder("第" + (rowIndex+2) + "行：");
			java.util.Map<String,String> map = datas.get(rowIndex);
			empCode = map.get("empCode");
			classType = map.get("classType");
			yearMonth = map.get("yearMonth");
			deptCode =  map.get("deptCode");
			if(ArrFileUtil.isEmpty(yearMonth)){
				wrongStr.append("月份不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(!yearMonth.matches("^\\d{4}-\\d{2}$")){
				wrongStr.append("月份格式有误，正确格式如:2014-08");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(!sdf.format(current).equals(yearMonth) && !sdf.format(nextMonth).equals(yearMonth)){
				wrongStr.append("月份取值有误，只能取当月或次月");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//初始化月份
			if(0 == currentOrNext){
				if(sdf.format(current).equals(yearMonth)){
					currentOrNext = 1;
				}else{
					currentOrNext = 2;
				}
			}
			if(ArrFileUtil.isEmpty(classType)){ 
				wrongStr.append("班次类型不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/**校验格式**/
			//班次类型
			if(!classType.equals("正常") && !classType.equals("顶班") && !classType.equals("机动")){
				wrongStr.append("班次类型只能为：正常、顶班、机动这三种类型");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/**校验非空**/
			int maxDay = maxDate(yearMonth);
			//标记是否有空值
			boolean flg = false;
			for(int i=0;i<days31.length;i++){
				if((null == map.get(""+(i+1)) || ArrFileUtil.isEmpty(map.get(""+(i+1)).toString()))
						&& i<28){
					flg = true;
					break;
				}
				arrangeNoList.add(map.get(""+(i+1)).toString());
				days31[i] = map.get(""+(i+1));
			}
			if(flg){
				wrongStr.append("1到28号排班有空值");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
		
			if(maxDay==29){
				if(ArrFileUtil.isEmpty(map.get("29"))){
					wrongStr.append("29号排班不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(!ArrFileUtil.isEmpty(map.get("30")) || !ArrFileUtil.isEmpty(map.get("31"))){
					wrongStr.append("所填月份没有30号、31号，请清除30号、31号的排班");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			if(maxDay==30){
				if(ArrFileUtil.isEmpty(map.get("29")) || ArrFileUtil.isEmpty(map.get("30"))){
					wrongStr.append("29号、30号排班不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(!ArrFileUtil.isEmpty(map.get("31"))){
					wrongStr.append("所填月份没有31号，请清除31号的排班");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			if(maxDay==31){
				if(ArrFileUtil.isEmpty(map.get("29")) || ArrFileUtil.isEmpty(map.get("30"))
						|| ArrFileUtil.isEmpty(map.get("31"))){
					wrongStr.append("29号到31号排班不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			if(maxDay==28){
				if(!ArrFileUtil.isEmpty(map.get("29")) || !ArrFileUtil.isEmpty(map.get("30"))
						|| !ArrFileUtil.isEmpty(map.get("31"))){
					wrongStr.append("本月没有29到31号，请清除29到31号的排班");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			//标识是否有机动班
			boolean jdFlg = false;
			for(int i=0;i<days31.length;i++){
				if(null != map.get(""+(i+1)) && !ArrFileUtil.isEmpty(map.get(""+(i+1)).toString())
						&& map.get(""+(i+1)).toString().indexOf("机动")!=-1){
					jdFlg = true;
					break;
				}
			}
			if((classType.equals("正常") || classType.equals("顶班")) && jdFlg){
				wrongStr.append("班次类别为正常或顶班时，排班不能出现机动班次代码");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(empCode)){ 
				wrongStr.append("驾驶员工号不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/**校验数据重复-自身校验**/
			if(empCodes.contains(empCode)){
				wrongStr.append("工号为"+empCode+"的驾驶员有多笔记录，请修正");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}else{
				empCodes.add(empCode);
			}
			/** 校验数据有效 **/
			if(currentOrNext == 1){
				pre = new PreSchedule();
				classRls.clear();
				for(int i=0;i<days31.length;i++){
					if(ArrFileUtil.isEmpty(days31[i])){
						continue;
					}
					switch(i+1){
					case 1:pre.setOne(days31[i]);break;
					case 2:pre.setTwo(days31[i]);break;
					case 3:pre.setThree(days31[i]);break;
					case 4:pre.setFour(days31[i]);break;
					case 5:pre.setFive(days31[i]);break;
					case 6:pre.setSix(days31[i]);break;
					case 7:pre.setSeven(days31[i]);break;
					case 8:pre.setEight(days31[i]);break;
					case 9:pre.setNine(days31[i]);break;
					case 10:pre.setTen(days31[i]);break;
					case 11:pre.setEleven(days31[i]);break;
					case 12:pre.setTwelve(days31[i]);break;
					case 13:pre.setThirteen(days31[i]);break;
					case 14:pre.setFourteen(days31[i]);break;
					case 15:pre.setFifteen(days31[i]);break;
					case 16:pre.setSixteen(days31[i]);break;
					case 17:pre.setSeventeen(days31[i]);break;
					case 18:pre.setEighteen(days31[i]);break;
					case 19:pre.setNineteen(days31[i]);break;
					case 20:pre.setTwenty(days31[i]);break;
					case 21:pre.setTwentyOne(days31[i]);break;
					case 22:pre.setTwentyTwo(days31[i]);break;
					case 23:pre.setTwentyThree(days31[i]);break;
					case 24:pre.setTwentyFour(days31[i]);break;
					case 25:pre.setTwentyFive(days31[i]);break;
					case 26:pre.setTwentySix(days31[i]);break;
					case 27:pre.setTwentySeven(days31[i]);break;
					case 28:pre.setTwentyEight(days31[i]);break;
					case 29:pre.setTwentyNine(days31[i]);break;
					case 30:pre.setThirty(days31[i]);break;
					case 31:pre.setThirtyOne(days31[i]);break;
					}
					classRl = new TransferClassesRL();
					classRl.setArrangeNo(days31[i]);
					String dayDtStr;
					if((i+1)<10){
						dayDtStr = yearMonth+"-0"+(i+1);
					}else{
						dayDtStr = yearMonth+"-"+(i+1);
					}
					classRl.setDayDt(ArrCommonUtil.getYYYY_MM_DDFmt().parse(dayDtStr));
					classRl.setIsTransfer(0);
					classRls.add(classRl);
				}
				Set<TransferClassesRL> rlSet = new HashSet<TransferClassesRL>();
				rlSet.addAll(classRls);
				//设置实际排班明细
				pre.setTransferClassesRLs(rlSet);
				Integer classTypeIdx = classType.equals("正常")?1:(classType.equals("顶班")?2:3);
				//设置班次类型
				pre.setClassType(classTypeIdx);
				//设置为正式
				pre.setDraftFlag(0);
				ArrDriver driver = new ArrDriver();
				driver.setEmpCode(empCode);
				//设置驾驶员
				pre.setDriver(driver);
				//设置月份
				pre.setYearMonth(yearMonth);
				pres.add(pre);
			}else{
				preDraft = new PreScheduleDraft();
				classDfs.clear();
				for(int i=0;i<days31.length;i++){
					if(ArrFileUtil.isEmpty(days31[i])){
						continue;
					}
					switch(i+1){
					case 1:preDraft.setOne(days31[i]);break;
					case 2:preDraft.setTwo(days31[i]);break;
					case 3:preDraft.setThree(days31[i]);break;
					case 4:preDraft.setFour(days31[i]);break;
					case 5:preDraft.setFive(days31[i]);break;
					case 6:preDraft.setSix(days31[i]);break;
					case 7:preDraft.setSeven(days31[i]);break;
					case 8:preDraft.setEight(days31[i]);break;
					case 9:preDraft.setNine(days31[i]);break;
					case 10:preDraft.setTen(days31[i]);break;
					case 11:preDraft.setEleven(days31[i]);break;
					case 12:preDraft.setTwelve(days31[i]);break;
					case 13:preDraft.setThirteen(days31[i]);break;
					case 14:preDraft.setFourteen(days31[i]);break;
					case 15:preDraft.setFifteen(days31[i]);break;
					case 16:preDraft.setSixteen(days31[i]);break;
					case 17:preDraft.setSeventeen(days31[i]);break;
					case 18:preDraft.setEighteen(days31[i]);break;
					case 19:preDraft.setNineteen(days31[i]);break;
					case 20:preDraft.setTwenty(days31[i]);break;
					case 21:preDraft.setTwentyOne(days31[i]);break;
					case 22:preDraft.setTwentyTwo(days31[i]);break;
					case 23:preDraft.setTwentyThree(days31[i]);break;
					case 24:preDraft.setTwentyFour(days31[i]);break;
					case 25:preDraft.setTwentyFive(days31[i]);break;
					case 26:preDraft.setTwentySix(days31[i]);break;
					case 27:preDraft.setTwentySeven(days31[i]);break;
					case 28:preDraft.setTwentyEight(days31[i]);break;
					case 29:preDraft.setTwentyNine(days31[i]);break;
					case 30:preDraft.setThirty(days31[i]);break;
					case 31:preDraft.setThirtyOne(days31[i]);break;
					}
					classDf = new TransferClassesDF();
					classDf.setArrangeNo(days31[i]);
					String dayDtStr;
					if((i+1)<10){
						dayDtStr = yearMonth+"-0"+(i+1);
					}else{
						dayDtStr = yearMonth+"-"+(i+1);
					}
					classDf.setDayDt(ArrCommonUtil.getYYYY_MM_DDFmt().parse(dayDtStr));
					classDfs.add(classDf);
				}
				Set<TransferClassesDF> dfSet = new HashSet<TransferClassesDF>();
				dfSet.addAll(classDfs);
				//设置排班明细
				preDraft.setTransferClassesDFs(dfSet);
				Integer classTypeIdx = classType.equals("正常")?1:(classType.equals("顶班")?2:3);
				//设置班次类型
				preDraft.setClassType(classTypeIdx);
				//设置草稿
				preDraft.setDraftFlag(1);
				ArrDriver driver = new ArrDriver();
				driver.setEmpCode(empCode);
				//设置驾驶员
				preDraft.setDriver(driver);
				//设置月份
				preDraft.setYearMonth(yearMonth);
				preDrafts.add(preDraft);
			}
		}
		//有校验不通过的班次，则提示
		if(!ArrFileUtil.isEmpty(wrongMsg.toString())) {
			throw new BizException(wrongMsg.toString());
		}
		//释放不再使用集合的内存
		datas = null;
		//记录班次代码
		List<ScheduleArrange> arrangeList = new ArrayList<ScheduleArrange>();
		if(currentOrNext == 1){
			//当月
			if(null == pres || pres.isEmpty()){
				throw new BizException("合法排班记录不能为空");
			}
			for(int rowIndex=0;rowIndex<pres.size();rowIndex++){
				//错误行数超过100行，退出校验
				if(wrongNum >= 100){
					wrongMsg.append("错误行数过多已中止校验(请修正后再导入)");
					break;
				}
				PreSchedule obj = pres.get(rowIndex);
				StringBuilder wrongStr = new StringBuilder("第" + (rowIndex+2) + "行：");
				if(null == obj){
					throw new BizException("排班实体不能为空");
				}
				// 驾驶员工号
				ArrDriver arrDriver = this.arrDriverDao.findDriverByCodeAndId(obj.getDriver().getEmpCode(),1L,userId);
				if (null == arrDriver || null == arrDriver.getId()) {
					wrongStr.append("驾驶员工号无效");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null == arrDriver.getDepartment() || null == arrDriver.getDepartment().getId()){
					wrongStr.append("驾驶员所属网点为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				//校验重复
				/*Integer p = preScheduleDao.findDriverById(obj.getYearMonth(), arrDriver.getId());
				if(p > 0){
					wrongStr.append("系统已经有驾驶员"+arrDriver.getEmpCode()+"的排班记录");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}*/
				
				//记录计划休息天数
				int planDay = 0;
				// 1号到31号的班次代码校验
				List<String> arrangeNos = new ArrayList<String>();
				for(TransferClassesRL rll:obj.getTransferClassesRLs()){
					String arrangeNo = rll.getArrangeNo();
					if(ArrFileUtil.isEmpty(arrangeNo)){
						continue;
					}
					if(arrangeNo.equals("休") || arrangeNo.equals("假")){
						continue;
					}
					if(!arrangeNos.contains(arrangeNo)){
						arrangeNos.add(arrangeNo);
					}
				}
				List<ScheduleArrange> arrangeListBak = new ArrayList<ScheduleArrange>();
				if(!arrangeNos.isEmpty()){
					arrangeListBak = this.scheduleArrangeDao.findArrByArrangeNoList(arrangeNos,
							arrDriver.getDepartment().getId(),userId);
				}
				if(null != arrangeListBak && !arrangeListBak.isEmpty()){
					arrangeList.addAll(arrangeListBak);
				}
				for(TransferClassesRL rll:obj.getTransferClassesRLs()){
					String arrangeNo = rll.getArrangeNo();
					if(ArrFileUtil.isEmpty(arrangeNo)){
						continue;
					}
					if(arrangeNo.equals("休") || arrangeNo.equals("假")){
						planDay = planDay+1;
						rll.setCreatedEmpCode(createdEmpCode);
						rll.setCreatedTm(new Date());
						continue;
					}
					ScheduleArrange scheduleArrange = this.findArrange(arrangeList, arrangeNo);
					if(null == scheduleArrange){
						scheduleArrange = this.scheduleArrangeDao.findArrByArrangeNo(arrangeNo,arrDriver.getDepartment().getId(),userId);
						if(null != scheduleArrange){
							if(arrangeList.size() < 1000){
								arrangeList.add(scheduleArrange);
							}else{
								arrangeList.remove(0);
								arrangeList.add(scheduleArrange);
							}
						}
					}
					if(null == scheduleArrange){
						wrongStr.append("班次代码"+arrangeNo+"不存在(只取驾驶员归属网点下的班次)");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					if(scheduleArrange.getValid().compareTo(0) == 0){
						wrongStr.append("班次代码"+arrangeNo+"已失效");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					String checkMsg = this.isQuasiDriving(arrDriver.getQuasiDrivingType(), scheduleArrange);
					if(!ArrFileUtil.isEmpty(checkMsg)){
						wrongStr.append("驾驶员所持证件不满足班次代码"+arrangeNo+"所配车辆的持证要求"+checkMsg);
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					if(scheduleArrange.getIsUsed().compareTo(1) != 0){
						scheduleArrange.setIsUsed(1);
						this.scheduleArrangeDao.update(scheduleArrange);
					}
					scheduleArrange.setModifiedEmpCode(createdEmpCode);
					scheduleArrange.setModifiedTm(ArrCommonUtil.currentTm());
					rll.setArrangeRl(scheduleArrange);
					rll.setCreatedEmpCode(createdEmpCode);
					rll.setCreatedTm(new Date());
				}
				obj.setPlanDay(planDay);
				obj.setRealDay(planDay);
				obj.setRate("100");
				obj.setDriver(arrDriver);
				obj.setDepartment(arrDriver.getDepartment());
			}
			if(wrongMsg.toString().length()>0){
				throw new BizException(wrongMsg.toString());
			}
			//批量保存
			List<PreSchedule> existList = new ArrayList<PreSchedule>();
			if(null != pres && !pres.isEmpty()){
				for(PreSchedule p:pres){
					PreSchedule old = this.preScheduleDao.listByDriver(p.getYearMonth(), p.getDriver().getId());
					if(null != old){
						if(old.getClassType().compareTo(p.getClassType())!=0){
							throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.90"
									,"班次类别不允许修改：系统中已有驾驶员{0}的排班，请核对并修改班次类别与原记录的班次类别一致",
									old.getDriver().getEmpCode()));
						}
						existList.add(p);
						//记录已经存在，查找是否有空值
						for(TransferClassesRL rl:old.getTransferClassesRLs()){
							if(ArrFileUtil.isEmpty(rl.getArrangeNo())){
								//空值则遍历覆盖
								for(TransferClassesRL newRl:p.getTransferClassesRLs()){
									if(rl.getDayDt().compareTo(newRl.getDayDt()) == 0){
										rl.setArrangeNo(newRl.getArrangeNo());
										rl.setArrangeRl(newRl.getArrangeRl());
										rl.setRemark(rl.getRemark()+"-->(导入)"+newRl.getArrangeNo());
										rl.setModifiedEmpCode(createdEmpCode);
										rl.setModifiedTm(ArrCommonUtil.currentTm());
										String day = ArrCommonUtil.getYYYY_MM_DDFmt().format(rl.getDayDt());
										Integer dayIdx = Integer.parseInt(day.substring(8));
										switch(dayIdx.intValue()){
										case 1:old.setOne(newRl.getArrangeNo());break;
										case 2:old.setTwo(newRl.getArrangeNo());break;
										case 3:old.setThree(newRl.getArrangeNo());break;
										case 4:old.setFour(newRl.getArrangeNo());break;
										case 5:old.setFive(newRl.getArrangeNo());break;
										case 6:old.setSix(newRl.getArrangeNo());break;
										case 7:old.setSeven(newRl.getArrangeNo());break;
										case 8:old.setEight(newRl.getArrangeNo());break;
										case 9:old.setNine(newRl.getArrangeNo());break;
										case 10:old.setTen(newRl.getArrangeNo());break;
										case 11:old.setEleven(newRl.getArrangeNo());break;
										case 12:old.setTwelve(newRl.getArrangeNo());break;
										case 13:old.setThirteen(newRl.getArrangeNo());break;
										case 14:old.setFourteen(newRl.getArrangeNo());break;
										case 15:old.setFifteen(newRl.getArrangeNo());break;
										case 16:old.setSixteen(newRl.getArrangeNo());break;
										case 17:old.setSeventeen(newRl.getArrangeNo());break;
										case 18:old.setEighteen(newRl.getArrangeNo());break;
										case 19:old.setNineteen(newRl.getArrangeNo());break;
										case 20:old.setTwenty(newRl.getArrangeNo());break;
										case 21:old.setTwentyOne(newRl.getArrangeNo());break;
										case 22:old.setTwentyTwo(newRl.getArrangeNo());break;
										case 23:old.setTwentyThree(newRl.getArrangeNo());break;
										case 24:old.setTwentyFour(newRl.getArrangeNo());break;
										case 25:old.setTwentyFive(newRl.getArrangeNo());break;
										case 26:old.setTwentySix(newRl.getArrangeNo());break;
										case 27:old.setTwentySeven(newRl.getArrangeNo());break;
										case 28:old.setTwentyEight(newRl.getArrangeNo());break;
										case 29:old.setTwentyNine(newRl.getArrangeNo());break;
										case 30:old.setThirty(newRl.getArrangeNo());break;
										case 31:old.setThirtyOne(newRl.getArrangeNo());break;
										}
										break;
									}
								}
							}
						}
						//重新计算实际休息天数
						int realDay = 0;
						for(TransferClassesRL rl:old.getTransferClassesRLs()){
							if(null != rl.getArrangeNo() 
									&& (rl.getArrangeNo().equals("休")||rl.getArrangeNo().equals("假"))){
								realDay=realDay+1;
							}
						}
						old.setRealDay(realDay);
						old.setModifiedEmpCode(createdEmpCode);
						old.setModifiedTm(ArrCommonUtil.currentTm());
					}
				}
			}
			if(!existList.isEmpty()){
				pres.removeAll(existList);
			}
			//批量保存
			List<PreSchedule> saveList = new ArrayList<PreSchedule>();
			if(!pres.isEmpty()){
				for(PreSchedule p:pres){
					p.setCreatedEmpCode(createdEmpCode);
					p.setCreatedTm(ArrCommonUtil.currentTm());
					saveList.add(p);
					if(saveList.size()==1000){
						this.preScheduleDao.saveBatch(saveList);
						saveList.clear();
					}
				}
				if(!saveList.isEmpty()){
					this.preScheduleDao.saveBatch(saveList);
				}
			}
		}else{
			//次月
			if(null == preDrafts || preDrafts.isEmpty()){
				throw new BizException("合法排班记录不能为空");
			}
			for(int rowIndex=0;rowIndex<preDrafts.size();rowIndex++){
				//错误行数超过100行，退出校验
				if(wrongNum >= 100){
					wrongMsg.append("错误行数过多已中止校验(请修正后再导入)");
					break;
				}
				PreScheduleDraft obj = preDrafts.get(rowIndex);
				StringBuilder wrongStr = new StringBuilder("第" + (rowIndex+2) + "行：");
				if(null == obj){
					throw new BizException("排班实体不能为空");
				}
				// 驾驶员工号
				ArrDriver arrDriver = this.arrDriverDao.findDriverByCodeAndId(obj.getDriver().getEmpCode(),1L,userId);
				if (null == arrDriver || null == arrDriver.getId()) {
					wrongStr.append("驾驶员工号无效");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null == arrDriver.getDepartment() || null == arrDriver.getDepartment().getId()){
					wrongStr.append("驾驶员所属网点为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				//校验重复
				/*Integer p = preScheduleDraftDao.findDriverById(obj.getYearMonth(), arrDriver.getId());
				if(p > 0){
					wrongStr.append("系统已经有驾驶员"+arrDriver.getEmpCode()+"的排班记录");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}*/
				
				//记录计划休息天数
				int planDay = 0;
				// 1号到31号的班次代码校验
				List<String> arrangeNos = new ArrayList<String>();
				for(TransferClassesDF rll:obj.getTransferClassesDFs()){
					String arrangeNo = rll.getArrangeNo();
					if(ArrFileUtil.isEmpty(arrangeNo)){
						continue;
					}
					if(arrangeNo.equals("休") || arrangeNo.equals("假")){
						continue;
					}
					if(!arrangeNos.contains(arrangeNo)){
						arrangeNos.add(arrangeNo);
					}
				}
				List<ScheduleArrange> arrangeListBak = new ArrayList<ScheduleArrange>();
				if(!arrangeNos.isEmpty()){
					arrangeListBak = this.scheduleArrangeDao.findArrByArrangeNoList(arrangeNos,
							arrDriver.getDepartment().getId(),userId);
				}
				if(null != arrangeListBak && !arrangeListBak.isEmpty()){
					arrangeList.addAll(arrangeListBak);
				}
				for(TransferClassesDF rll:obj.getTransferClassesDFs()){
					String arrangeNo = rll.getArrangeNo();
					if(ArrFileUtil.isEmpty(arrangeNo)){
						continue;
					}
					if(arrangeNo.equals("休") || arrangeNo.equals("假")){
						planDay = planDay+1;
						rll.setCreatedEmpCode(createdEmpCode);
						rll.setCreatedTm(new Date());
						continue;
					}
					ScheduleArrange scheduleArrange = this.findArrange(arrangeList, arrangeNo);
					if(null == scheduleArrange){
						scheduleArrange = this.scheduleArrangeDao.findArrByArrangeNo(arrangeNo,arrDriver.getDepartment().getId(),userId);
						if(arrangeList.size() < 1000){
							arrangeList.add(scheduleArrange);
						}else{
							arrangeList.remove(0);
							arrangeList.add(scheduleArrange);
						}
					}
					if(null == scheduleArrange){
						wrongStr.append("班次代码"+arrangeNo+"不存在(只取驾驶员归属网点下的班次)");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					if(scheduleArrange.getValid().compareTo(0) == 0){
						wrongStr.append("班次代码"+arrangeNo+"已失效");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					String checkMsg =  this.isQuasiDriving(arrDriver.getQuasiDrivingType(), scheduleArrange);
					if(!ArrFileUtil.isEmpty(checkMsg)){
						wrongStr.append("驾驶员所持证件不满足班次代码"+arrangeNo+"所配车辆的持证要求"+checkMsg);
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						break;
					}
					if(scheduleArrange.getIsUsed().compareTo(1) != 0){
						scheduleArrange.setIsUsed(1);
						this.scheduleArrangeDao.update(scheduleArrange);
					}
					scheduleArrange.setModifiedEmpCode(createdEmpCode);
					scheduleArrange.setModifiedTm(ArrCommonUtil.currentTm());
					rll.setArrangeDf(scheduleArrange);
					rll.setCreatedEmpCode(createdEmpCode);
					rll.setCreatedTm(new Date());
				}
				for(TransferClassesDF rll:obj.getTransferClassesDFs()){
					//校验时间冲突
					TransferClassesDF lastRll = this.getDfByDate(obj.getTransferClassesDFs(), rll.getDayDt());
					if(null != lastRll && null != lastRll.getArrangeDf() && null != rll.getArrangeDf()){
						if(checkTimeCon(lastRll.getArrangeDf(),rll.getArrangeDf())){
							wrongStr.append(String.format("工号%s的%s与%s排班有时间冲突", 
									arrDriver.getEmpCode(),
									ArrCommonUtil.getYYYY_MM_DDFmt().format(lastRll.getDayDt()),
									ArrCommonUtil.getYYYY_MM_DDFmt().format(rll.getDayDt())));
							String msg = wrongStr.toString();
							wrongNum++;
							wrongMsg.append(msg+";");
							break;
						}
					}
				}
				obj.setPlanDay(planDay);
				obj.setRealDay(planDay);
				obj.setRate("100");
				obj.setDriver(arrDriver);
				obj.setDepartment(arrDriver.getDepartment());
			}
			if(wrongMsg.toString().length()>0){
				throw new BizException(wrongMsg.toString());
			}
			//批量保存
			if(null != preDrafts && !preDrafts.isEmpty()){
				for(PreScheduleDraft p:preDrafts){
					PreScheduleDraft old = this.preScheduleDraftDao.listByDriver(p.getYearMonth(),p.getDriver().getId());
					if(null != old && null != old.getId()){
						if(old.getClassType().compareTo(p.getClassType())!=0){
							throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.90"
									,"班次类别不允许修改：系统中已有驾驶员{0}的排班，请修改班次类别与原记录的班次类别一致",
									old.getDriver().getEmpCode()));
						}
						p.setId(old.getId());
						old.setOne(p.getOne());
						old.setTwo(p.getTwo());
						old.setThree(p.getThree());
						old.setFour(p.getFour());
						old.setFive(p.getFive());
						old.setSix(p.getSix());
						old.setSeven(p.getSeven());
						old.setEight(p.getEight());
						old.setNine(p.getNine());
						old.setTen(p.getTen());
						old.setEleven(p.getEleven());
						old.setTwelve(p.getTwelve());
						old.setThirteen(p.getThirteen());
						old.setFourteen(p.getFourteen());
						old.setFifteen(p.getFifteen());
						old.setSixteen(p.getSixteen());
						old.setSeventeen(p.getSeventeen());
						old.setEighteen(p.getEighteen());
						old.setNineteen(p.getNineteen());
						old.setTwenty(p.getTwenty());
						old.setTwentyOne(p.getTwentyOne());
						old.setTwentyTwo(p.getTwentyTwo());
						old.setTwentyThree(p.getTwentyThree());
						old.setTwentyFour(p.getTwentyFour());
						old.setTwentyFive(p.getTwentyFive());
						old.setTwentySix(p.getTwentySix());
						old.setTwentySeven(p.getTwentySeven());
						old.setTwentyEight(p.getTwentyEight());
						old.setTwentyNine(p.getTwentyNine());
						old.setThirty(p.getThirty());
						old.setThirtyOne(p.getThirtyOne());
						old.setModifiedEmpCode(createdEmpCode);
						old.setModifiedTm(ArrCommonUtil.currentTm());
						old.setPlanDay(p.getPlanDay());
						old.setRate("100");
						old.setRealDay(p.getPlanDay());
						old.getTransferClassesDFs().clear();
						old.getTransferClassesDFs().addAll(p.getTransferClassesDFs());
					}else{
						p.setCreatedEmpCode(createdEmpCode);
						p.setCreatedTm(ArrCommonUtil.currentTm());
						this.preScheduleDraftDao.save(p);
					}
				}
			}
		}
	}
	/**
	 * 获取已经校验过的归属网点
	 * @param depts
	 * @param id
	 * @return
	 */
	private ArrDepartment getDeptById(List<ArrDepartment> depts,Long id){
		if(null == depts || depts.isEmpty() || null == id){
			return null;
		}
		for(ArrDepartment d:depts){
			if(null == d || null == d.getId()){
				continue;
			}
			if(d.getId().compareTo(id)==0){
				return d;
			}
		}
		return null;
	}
	//查找班次
	private ScheduleArrange findArrange(List<ScheduleArrange> arrangeList,String arrangeNo){
		if(null == arrangeList || arrangeList.isEmpty() || ArrFileUtil.isEmpty(arrangeNo)){
			return null;
		}
		for(ScheduleArrange sa:arrangeList){
			if(null == sa || ArrFileUtil.isEmpty(sa.getArrangeNo())){
				continue;
			}
			if(sa.getArrangeNo().equals(arrangeNo)){
				return sa;
			}
		}
		return null;
	}
	//获取前一天的班次代码
	private TransferClassesDF getDfByDate(Set<TransferClassesDF> dfs,Date secondDate){
		if(null == dfs || dfs.isEmpty() || null == secondDate){
			return null;
		}
		Date firstDate = DateUtils.addDays(secondDate, -1);
		for(TransferClassesDF df:dfs){
			if(null == df || null == df.getDayDt()){
				continue;
			}
			if(df.getDayDt().compareTo(firstDate) == 0){
				return df;
			}
		}
		return null;
	}
	/**
	 * 获取指定月最大天数
	 * @param year
	 * @param month
	 * @return
	 */
	private int maxDate(String yearMonth){
		Calendar a= Calendar.getInstance();
		a.set(Calendar.YEAR, Integer.parseInt(yearMonth.substring(0,4)));
		a.set(Calendar.MONTH, Integer.parseInt(yearMonth.substring(5))-1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
	/**
	 * 对象之间转换
	 * @param preSchedule
	 * @return
	 */
	private PreSchedule copyBeanProperties(PreScheduleDraft preScheduleDraft){
		PreSchedule psd = new PreSchedule();
		psd.setId(preScheduleDraft.getId());
		psd.setClassType(preScheduleDraft.getClassType());
		psd.setDepartment(preScheduleDraft.getDepartment());
		psd.setDraftFlag(preScheduleDraft.getDraftFlag());
		psd.setDriver(preScheduleDraft.getDriver());
		psd.setEight(preScheduleDraft.getEight());
		psd.setEighteen(preScheduleDraft.getEighteen());
		psd.setEleven(preScheduleDraft.getEleven());
		psd.setFifteen(preScheduleDraft.getFifteen());
		psd.setFive(preScheduleDraft.getFive());
		psd.setFour(preScheduleDraft.getFour());
		psd.setFourteen(preScheduleDraft.getFourteen());
		psd.setCreatedEmpCode(preScheduleDraft.getCreatedEmpCode());
		psd.setCreatedTm(preScheduleDraft.getCreatedTm());
		psd.setModifiedEmpCode(preScheduleDraft.getModifiedEmpCode());
		psd.setModifiedTm(preScheduleDraft.getModifiedTm());
		psd.setNine(preScheduleDraft.getNine());
		psd.setNineteen(preScheduleDraft.getNineteen());
		psd.setOne(preScheduleDraft.getOne());
		psd.setPlanDay(preScheduleDraft.getPlanDay());
		psd.setRate(preScheduleDraft.getRate());
		psd.setRealDay(preScheduleDraft.getRealDay());
		psd.setSeven(preScheduleDraft.getSeven());
		psd.setSeventeen(preScheduleDraft.getSeventeen());
		psd.setSix(preScheduleDraft.getSix());
		psd.setSixteen(preScheduleDraft.getSixteen());
		psd.setTen(preScheduleDraft.getTen());
		psd.setThirteen(preScheduleDraft.getThirteen());
		psd.setThirty(preScheduleDraft.getThirty());
		psd.setThirtyOne(preScheduleDraft.getThirtyOne());
		psd.setThree(preScheduleDraft.getThree());
		psd.setTwelve(preScheduleDraft.getTwelve());
		psd.setTwenty(preScheduleDraft.getTwenty());
		psd.setTwentyEight(preScheduleDraft.getTwentyEight());
		psd.setTwentyFive(preScheduleDraft.getTwentyFive());
		psd.setTwentyFour(preScheduleDraft.getTwentyFour());
		psd.setTwentyNine(preScheduleDraft.getTwentyNine());
		psd.setTwentyOne(preScheduleDraft.getTwentyOne());
		psd.setTwentySeven(preScheduleDraft.getTwentySeven());
		psd.setTwentySix(preScheduleDraft.getTwentySix());
		psd.setTwentyThree(preScheduleDraft.getTwentyThree());
		psd.setTwentyTwo(preScheduleDraft.getTwentyTwo());
		psd.setTwo(preScheduleDraft.getTwo());
		psd.setYearMonth(preScheduleDraft.getYearMonth());
		return psd;
	}
	/**
	 * 获取单元格内容
	 * @param cell
	 * @param format
	 * @return
	 */
	private String getHSSFCellValue(HSSFCell cell){
		String val = null;
		DecimalFormat decimalFormat = new DecimalFormat("#");
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM");
		if( null==cell ){
			return "";
		}
		switch ( cell.getCellType() ){
			case HSSFCell.CELL_TYPE_BOOLEAN:
				val = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				val = cell.getRichStringCellValue().getString(); 
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
			case HSSFCell.CELL_TYPE_NUMERIC:
				try {
					if( HSSFDateUtil.isCellInternalDateFormatted(cell) || HSSFDateUtil.isCellDateFormatted(cell) ){	
						val = dateFormat.format(cell.getDateCellValue());
						//double dValue = HSSFDateUtil.getExcelDate(sdf.parse(dd));
						//val = dateFormat.format(HSSFDateUtil.getJavaDate(dValue));
					}else{
						double dValue = cell.getNumericCellValue();
						val = String.valueOf(decimalFormat.format(dValue));
					}
				} catch (Exception e) {
					log.error("",e);
					val = "" ;
				}
				break;
			default:
				val="";
		}
		if(null != val){
			val = val.trim();
		}
		return val;
	}
	/**
	 * 判断两个数组中是否有相同的元素
	 * @param strs1
	 * @param strs2
	 * @return
	 */
	public boolean compareArr(String[] need,String[] owner){
		//持证要求和所持证都为空，符合
		if((null == need || need.length < 1) && (null == owner || owner.length < 1)){
			return true;
		}
		//持证要求不为空和所持证为空，不符合
		if((null == owner || owner.length < 1) && (null != need && need.length > 0)){
			return false;
		}
		//持证要求为空和所持证不为空，符合
		if((null != owner && owner.length > 0) && (null == need || need.length < 1)){
			return true;
		}
		//遍历持证要求，查找是否有对应的持证
	    for(int i=0;i<need.length;i++){
	    	for(int j=0;j<owner.length;j++){
	    		if(ArrFileUtil.isEmpty(owner[j])){
	    			continue;
	    		}
	    		if(owner[j].equals(need[i])){
	                 return true;
	            }
	        }
	    }
	    return false;
	}
	
	/**
	 * 判断该网点是否在该用户权限下可见
	 */
	private boolean isDeptCodeForUser(String deptCode,Long userId){
		return this.preScheduleDraftDao.isDeptCodeForUser(deptCode,userId);
	}
	/**
	 * 驾驶员是否是网点代码下的
	 * @return
	 */
	private boolean isDriverForDept(Long driverDeptId,String deptCode){
		return this.preScheduleDraftDao.isDriverForDept(driverDeptId,deptCode);
	}
	/**
	 * 获取1号到31号的数据，集合起来，方便校验
	 * @param preSchedule
	 * @param daysList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> getDaysData(PreSchedule preSchedule) {
		List<Map> daysList = new ArrayList<Map>();
		Map daysMap;
		//1号
		daysMap = new HashMap(); 
		daysMap.put("day", "1");
		daysMap.put("arrangeNo", preSchedule.getOne());
		daysList.add(daysMap);
		//2号
		daysMap = new HashMap();
		daysMap.put("day", "2");
		daysMap.put("arrangeNo", preSchedule.getTwo());
		daysList.add(daysMap);
		//3号
		daysMap = new HashMap();
		daysMap.put("day", "3");
		daysMap.put("arrangeNo", preSchedule.getThree());
		daysList.add(daysMap);
		//4号
		daysMap = new HashMap();
		daysMap.put("day", "4");
		daysMap.put("arrangeNo", preSchedule.getFour());
		daysList.add(daysMap);
		//5号
		daysMap = new HashMap();
		daysMap.put("day", "5");
		daysMap.put("arrangeNo", preSchedule.getFive());
		daysList.add(daysMap);
		//6号
		daysMap = new HashMap();
		daysMap.put("day", "6");
		daysMap.put("arrangeNo", preSchedule.getSix());
		daysList.add(daysMap);
		//7号
		daysMap = new HashMap();
		daysMap.put("day", "7");
		daysMap.put("arrangeNo", preSchedule.getSeven());
		daysList.add(daysMap);
		//8号
		daysMap = new HashMap();
		daysMap.put("day", "8");
		daysMap.put("arrangeNo", preSchedule.getEight());
		daysList.add(daysMap);
		//9号
		daysMap = new HashMap();
		daysMap.put("day", "9");
		daysMap.put("arrangeNo", preSchedule.getNine());
		daysList.add(daysMap);
		//10号
		daysMap = new HashMap();
		daysMap.put("day", "10");
		daysMap.put("arrangeNo", preSchedule.getTen());
		daysList.add(daysMap);
		//11号
		daysMap = new HashMap();
		daysMap.put("day", "11");
		daysMap.put("arrangeNo", preSchedule.getEleven());
		daysList.add(daysMap);
		//12号
		daysMap = new HashMap();
		daysMap.put("day", "12");
		daysMap.put("arrangeNo", preSchedule.getTwelve());
		daysList.add(daysMap);
		//13号
		daysMap = new HashMap();
		daysMap.put("day", "13");
		daysMap.put("arrangeNo", preSchedule.getThirteen());
		daysList.add(daysMap);
		//14号
		daysMap = new HashMap();
		daysMap.put("day", "14");
		daysMap.put("arrangeNo", preSchedule.getFourteen());
		daysList.add(daysMap);
		//15号
		daysMap = new HashMap();
		daysMap.put("day", "15");
		daysMap.put("arrangeNo", preSchedule.getFifteen());
		daysList.add(daysMap);
		//16号
		daysMap = new HashMap();
		daysMap.put("day", "16");
		daysMap.put("arrangeNo", preSchedule.getSixteen());
		daysList.add(daysMap);
		//17号
		daysMap = new HashMap();
		daysMap.put("day", "17");
		daysMap.put("arrangeNo", preSchedule.getSeventeen());
		daysList.add(daysMap);
		//18号
		daysMap = new HashMap();
		daysMap.put("day", "18");
		daysMap.put("arrangeNo", preSchedule.getEighteen());
		daysList.add(daysMap);
		//19号
		daysMap = new HashMap();
		daysMap.put("day", "19");
		daysMap.put("arrangeNo", preSchedule.getNineteen());
		daysList.add(daysMap);
		//20号
		daysMap = new HashMap();
		daysMap.put("day", "20");
		daysMap.put("arrangeNo", preSchedule.getTwenty());
		daysList.add(daysMap);
		//21号
		daysMap = new HashMap();
		daysMap.put("day", "21");
		daysMap.put("arrangeNo", preSchedule.getTwentyOne());
		daysList.add(daysMap);
		//22号
		daysMap = new HashMap();
		daysMap.put("day", "22");
		daysMap.put("arrangeNo", preSchedule.getTwentyTwo());
		daysList.add(daysMap);
		//23号
		daysMap = new HashMap();
		daysMap.put("day", "23");
		daysMap.put("arrangeNo", preSchedule.getTwentyThree());
		daysList.add(daysMap);
		//24号
		daysMap = new HashMap();
		daysMap.put("day", "24");
		daysMap.put("arrangeNo", preSchedule.getTwentyFour());
		daysList.add(daysMap);
		//25号
		daysMap = new HashMap();
		daysMap.put("day", "25");
		daysMap.put("arrangeNo", preSchedule.getTwentyFive());
		daysList.add(daysMap);
		//26号
		daysMap = new HashMap();
		daysMap.put("day", "26");
		daysMap.put("arrangeNo", preSchedule.getTwentySix());
		daysList.add(daysMap);
		//27号
		daysMap = new HashMap();
		daysMap.put("day", "27");
		daysMap.put("arrangeNo", preSchedule.getTwentySeven());
		daysList.add(daysMap);
		//28号
		daysMap = new HashMap();
		daysMap.put("day", "28");
		daysMap.put("arrangeNo", preSchedule.getTwentyEight());
		daysList.add(daysMap);
		//29号
		if(!ArrFileUtil.isEmpty(preSchedule.getTwentyNine())){
			daysMap = new HashMap();
			daysMap.put("day", "29");
			daysMap.put("arrangeNo",preSchedule.getTwentyNine());
			daysList.add(daysMap);
		}
		//30号
		if(!ArrFileUtil.isEmpty(preSchedule.getThirty())){
			daysMap = new HashMap();
			daysMap.put("day", "30");
			daysMap.put("arrangeNo",preSchedule.getThirty());
			daysList.add(daysMap);
		}
		//31号
		if(!ArrFileUtil.isEmpty(preSchedule.getThirtyOne())){
			daysMap = new HashMap();
			daysMap.put("day", "31");
			daysMap.put("arrangeNo",preSchedule.getThirtyOne());
			daysList.add(daysMap);
		}
		return daysList;
	}
	/**
	 * 验是否符合准驾车型
	 * @param quasiDrivingType 驾驶员持证
	 * @param scheduleArrange 配班信息
	 * @return
	 */
	private String isQuasiDriving(String quasiDrivingType,ScheduleArrange scheduleArrange){
		//班次为休或假，默认符合
		if(null == scheduleArrange || null == scheduleArrange.getScheduleArrangeInfos() 
				|| scheduleArrange.getScheduleArrangeInfos().isEmpty()){
			return null;
		}
		//持证为空，不符合
		if(ArrFileUtil.isEmpty(quasiDrivingType)){
			return "(驾驶员持证为空)";
		}
		String[] quasiDrivingTypes = quasiDrivingType.split(",");
		for(ScheduleInfoArrange infoArr:scheduleArrange.getScheduleArrangeInfos()){
			if(null == infoArr || null == infoArr.getScheduleInfo()
					|| null == infoArr.getScheduleInfo().getVehicle() 
					|| null == infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType()
					|| ArrFileUtil.isEmpty(infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType().getHolderCertsNeed())){
				continue;
			}
			String holderCertsNeed = infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType().getHolderCertsNeed();
			String[] holderCertsNeeds = holderCertsNeed.split(",");
			//班次明细任意一笔持证要求不符合，则返回false
			if(!compareArr(holderCertsNeeds,quasiDrivingTypes)){
				return String.format("(车辆持证要求为%s/驾驶员持证为%s)",holderCertsNeed,quasiDrivingType);
			}
		}
		//校验通过，符合
		return null;
	}

	public IPage<ArrDriver> listPageBy(Long deptId, String empCode,int pageSize, int pageIndex) {
		return arrDriverDao.listPageBy(deptId, empCode, this.getNextMonth(), this.getUserId(), pageSize, pageIndex);
	}
	/**
	 * 获取下个月的年月份
	 * @return eg:2014-02
	 */
	private String getNextMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//取当月-精确到月
		Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
		//取次月-精确到月
		Date nextMonth = DateUtils.truncate(DateUtils.addMonths(current, 1),Calendar.MONTH);
		return sdf.format(nextMonth);
	}
	/**
	 * 校验时间是否有冲突
	 * @param arrangeNo
	 * @param day
	 * @param empCode
	 * @param dfs
	 * @param preSchedules
	 * @return
	 */
	private boolean checkTimeCon(ScheduleArrange first,ScheduleArrange second){
		if(null == first || null == first.getScheduleArrangeInfos() || first.getScheduleArrangeInfos().isEmpty()
				|| null == second || null == second.getScheduleArrangeInfos() || second.getScheduleArrangeInfos().isEmpty()){
			return false;
		}
		//1.取班次明细
		List<ScheduleInfo> firstInfos = new ArrayList<ScheduleInfo>();
		for(ScheduleInfoArrange info:first.getScheduleArrangeInfos()){
			if(null == info || null == info.getScheduleInfo()){
				continue;
			}
			firstInfos.add(info.getScheduleInfo());
		}
		if(null == firstInfos || firstInfos.isEmpty()){
			return false;
		}
		List<ScheduleInfo> secondInfos = new ArrayList<ScheduleInfo>();
		for(ScheduleInfoArrange info:second.getScheduleArrangeInfos()){
			if(null == info || null == info.getScheduleInfo()){
				continue;
			}
			secondInfos.add(info.getScheduleInfo());
		}
		if(null == secondInfos || secondInfos.isEmpty()){
			return false;
		}
		//2.班次明细排序取最早和最晚
		firstInfos = this.sortList(firstInfos);
		secondInfos = this.sortList(secondInfos);
		//3.取first最晚、取second最早
		ScheduleInfo lastInfo = firstInfos.get(firstInfos.size()-1);
		ScheduleInfo earlyInfo = secondInfos.get(0);
		//4.判断first是否跨天
		Integer firstStartPart1 = Integer.parseInt(lastInfo.getStartTm().split(":", -1)[0]);
		Integer firstStartPart2 = Integer.parseInt(lastInfo.getStartTm().split(":", -1)[1]);
		Integer firstEndPart1 = Integer.parseInt(lastInfo.getEndTm().split(":", -1)[0]);
		Integer firstEndPart2 = Integer.parseInt(lastInfo.getEndTm().split(":", -1)[1]);
		if (compareTm(firstStartPart1, firstStartPart2, firstEndPart1, firstEndPart2) > 0) {
			//5.first跨天，则比较是否与second交叉
			Integer secondStartPart1 = Integer.parseInt(earlyInfo.getStartTm().split(":", -1)[0]);
			Integer secondStartPart2 = Integer.parseInt(earlyInfo.getStartTm().split(":", -1)[1]);
			//6.first跨天收车时间比second最早出车时间大，则交叉
			if(compareTm(firstEndPart1, firstEndPart2, secondStartPart1, secondStartPart2) > 0){
				return true;
			}
		}
		return false;
	}
	/**
	 * 比较两个时间大小
	 * 
	 * @param leftHour
	 * @param leftMinute
	 * @param rightHour
	 * @param rightMinute
	 * @return 
	 */
	private int compareTm(Integer leftHour, Integer leftMinute,
			Integer rightHour, Integer rightMinute) {
		// 比较时钟
		if (leftHour.compareTo(rightHour) > 0) {
			return 1;
		}
		if (leftHour.compareTo(rightHour) < 0) {
			return -1;
		}
		// 时钟相等比较分钟
		if (leftMinute.compareTo(rightMinute) > 0) {
			return 1;
		}
		if (leftMinute.compareTo(rightMinute) < 0) {
			return -1;
		}
		// 时钟分钟都相等返回0
		return 0;
	}
}
