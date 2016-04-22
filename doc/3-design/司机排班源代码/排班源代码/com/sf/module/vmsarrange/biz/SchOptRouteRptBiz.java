/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-16     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.context.ApplicationContext;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.vmsarrange.dao.ISchOptRouteRptDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.SchOptRouteRpt;
import com.sf.module.vmsarrange.util.ArrCommonUtil;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsinfo.util.FileUtil;

/**
 * 
 * 路径优化分析报表业务实现类
 * 
 */

public class SchOptRouteRptBiz extends BaseBiz implements ISchOptRouteRptBiz {
	private ISchOptRouteRptDao schOptRouteRptDao;
	private static int MAX_SIZE = 65530;
	private static String TEMPLATE_NAME = "路径优化分析模板.xls";
	public void setSchOptRouteRptDao(ISchOptRouteRptDao schOptRouteRptDao) {
		this.schOptRouteRptDao = schOptRouteRptDao;
	}
	
	//查询分页数据
	public IPage<SchOptRouteRpt> listPage(Long deptId, Date yearMonth,
			int pageSize, int pageIndex) {
		IPage<SchOptRouteRpt> page = schOptRouteRptDao.listPage(deptId, yearMonth, pageSize, pageIndex, this.getUserId());
		return page;
	}
	/**
	 * 生成报表
	 */
	@SuppressWarnings({ "static-access" })
	public String listReport(Long deptId, Date yearMonth) {
		String userCode = this.getCurrentUser().getEmployee().getCode();
		if(null == deptId){
			throw new BizException("查询条件网点代码不能为空");
		}
		String fileName = null;
		if(FileUtil.isEmpty(userCode)){
			fileName = "路径优化线路分析报表.xls";
		}else{
			fileName = "路径优化线路分析报表("+userCode+").xls";
		}
		//创建EXCEL
		java.io.InputStream is = null;
		try {
			is = new FileInputStream(this.getTemplatePath());
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
		HSSFSheet templateSheet = workbook.getSheetAt(0);
		if(templateSheet==null){
			throw new BizException("生成报表失败，模板中第一个sheet页为空");
		}
		//获取样式
		final int totalColumn = 38;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = templateSheet.getRow(2);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		//查询分页数据
		int dataSize = schOptRouteRptDao.listReportCount(deptId, yearMonth, this.getUserId());
		int pageIdx = 0;
		int pageSize = this.MAX_SIZE;
		if(dataSize > this.MAX_SIZE*5){
			throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.9"
					,"数据量超过了{0}条(导出将超时),请分批导出", this.MAX_SIZE*5));
		}
		//有数据则写数据
		if(dataSize > 0){
			if(dataSize <= pageSize){
				//数据量没有超出最大值，单sheet页
				IPage<SchOptRouteRpt> page = schOptRouteRptDao.listPage(deptId, yearMonth, pageSize, 0, this.getUserId());
				Collection<SchOptRouteRpt> data = null;
				if(null != page){
					data = page.getData();
				}
				this.fillData(data, templateSheet, style,workbook);
			}else{
				//数据量超过最大值，分sheet页导出
				while(dataSize > pageIdx){
					HSSFSheet sheet = workbook.cloneSheet(0);
					workbook.setSheetName(workbook.getSheetIndex(sheet), "路径优化"+workbook.getSheetIndex(sheet));
					IPage<SchOptRouteRpt> page = schOptRouteRptDao.listPage(deptId, yearMonth, pageSize, pageIdx/pageSize, this.getUserId());
					Collection<SchOptRouteRpt> data = null;
					if(null != page){
						data = page.getData();
					}
					this.fillData(data, sheet, style,workbook);
					pageIdx = pageIdx + pageSize;
				}
				//隐藏模板sheet页
				workbook.setSheetHidden(0, true);
			}
		}
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("SchOptRouteRptBiz", "vmsarrange");
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
	 * 填充汇总数据
	 */
	private void fillData(Collection<SchOptRouteRpt> data,HSSFSheet sheet,HSSFCellStyle[] style,HSSFWorkbook workBook){
		if(null == data || data.isEmpty()){
			return ;
		}
		if(null == sheet){
			return ;
		}
		HSSFCell cell;
		HSSFRow row;
		HSSFCellStyle redStyle = null;
		HSSFCellStyle greenStyle = null;
		int startRow = 2;
		for(SchOptRouteRpt orr:data){
			if(null == orr){
				continue;
			}
			row = sheet.getRow(startRow);
			if(null == row){
				row = sheet.createRow(startRow);
			}
			//记录列号
			int columnIdx = 0;
			
			//月份
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			cell.setCellValue(orr.getOptDate());
			++columnIdx;
			
			//区部
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			ArrDepartment dept = orr.getDept();
			if (null != dept ) {
				String areaName = "";
				if(!ArrFileUtil.isEmpty(dept.getDeptCode())){
					areaName = dept.getDeptCode();
				}
				if(!ArrFileUtil.isEmpty(dept.getDeptName())){
					areaName = areaName + "/" + dept.getDeptName();
				}
				cell.setCellValue(areaName.replaceAll("^/", ""));
			}
			++columnIdx;
			
			//线路优化编号
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(!ArrFileUtil.isEmpty(orr.getLineOptimizeNo())){
				cell.setCellValue(orr.getLineOptimizeNo());
			}
			++columnIdx;
			
			//车型
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(!ArrFileUtil.isEmpty(orr.getModelBase())){
				cell.setCellValue(orr.getModelBase());
			}
			++columnIdx;
			
			//优化路径
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(!ArrFileUtil.isEmpty(orr.getOptimizeRoute())){
				cell.setCellValue(orr.getOptimizeRoute());
			}
			++columnIdx;
			//路径匹配天数
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != orr.getMatchDays()){
				cell.setCellValue(orr.getMatchDays());
			}
			++columnIdx;
			/***1号到31号车牌号***/
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
				case 0:value = (ArrFileUtil.isEmpty(orr.getOne()))?"":orr.getOne();break;
				case 1:value = (ArrFileUtil.isEmpty(orr.getTwo()))?"":orr.getTwo();break;
				case 2:value = (ArrFileUtil.isEmpty(orr.getThree()))?"":orr.getThree();break;
				case 3:value = (ArrFileUtil.isEmpty(orr.getFour()))?"":orr.getFour();break;
				case 4:value = (ArrFileUtil.isEmpty(orr.getFive()))?"":orr.getFive();break;
				case 5:value = (ArrFileUtil.isEmpty(orr.getSix()))?"":orr.getSix();break;
				case 6:value = (ArrFileUtil.isEmpty(orr.getSeven()))?"":orr.getSeven();break;
				case 7:value = (ArrFileUtil.isEmpty(orr.getEight()))?"":orr.getEight();break;
				case 8:value = (ArrFileUtil.isEmpty(orr.getNine()))?"":orr.getNine();break;
				case 9:value = (ArrFileUtil.isEmpty(orr.getTen()))?"":orr.getTen();break;
				case 10:value = (ArrFileUtil.isEmpty(orr.getEleven()))?"":orr.getEleven();break;
				case 11:value = (ArrFileUtil.isEmpty(orr.getTwelve()))?"":orr.getTwelve();break;
				case 12:value = (ArrFileUtil.isEmpty(orr.getThirteen()))?"":orr.getThirteen();break;
				case 13:value = (ArrFileUtil.isEmpty(orr.getFourteen()))?"":orr.getFourteen();break;
				case 14:value = (ArrFileUtil.isEmpty(orr.getFifteen()))?"":orr.getFifteen();break;
				case 15:value = (ArrFileUtil.isEmpty(orr.getSixteen()))?"":orr.getSixteen();break;
				case 16:value = (ArrFileUtil.isEmpty(orr.getSeventeen()))?"":orr.getSeventeen();break;
				case 17:value = (ArrFileUtil.isEmpty(orr.getEighteen()))?"":orr.getEighteen();break;
				case 18:value = (ArrFileUtil.isEmpty(orr.getNineteen()))?"":orr.getNineteen();break;
				case 19:value = (ArrFileUtil.isEmpty(orr.getTwenty()))?"":orr.getTwenty();break;
				case 20:value = (ArrFileUtil.isEmpty(orr.getTwentyOne()))?"":orr.getTwentyOne();break;
				case 21:value = (ArrFileUtil.isEmpty(orr.getTwentyTwo()))?"":orr.getTwentyTwo();break;
				case 22:value = (ArrFileUtil.isEmpty(orr.getTwentyThree()))?"":orr.getTwentyThree();break;
				case 23:value = (ArrFileUtil.isEmpty(orr.getTwentyFour()))?"":orr.getTwentyFour();break;
				case 24:value = (ArrFileUtil.isEmpty(orr.getTwentyFive()))?"":orr.getTwentyFive();break;
				case 25:value = (ArrFileUtil.isEmpty(orr.getTwentySix()))?"":orr.getTwentySix();break;
				case 26:value = (ArrFileUtil.isEmpty(orr.getTwentySeven()))?"":orr.getTwentySeven();break;
				case 27:value = (ArrFileUtil.isEmpty(orr.getTwentyEight()))?"":orr.getTwentyEight();break;
				case 28:value = (ArrFileUtil.isEmpty(orr.getTwentyNine()))?"":orr.getTwentyNine();break;
				case 29:value = (ArrFileUtil.isEmpty(orr.getThirty()))?"":orr.getThirty();break;
				case 30:value = (ArrFileUtil.isEmpty(orr.getThirtyOne()))?"":orr.getThirtyOne();break;
				}
				if(!ArrFileUtil.isEmpty(value)){
					cell.setCellValue(value);
				}
				//设置样式-有数据标绿，没有数据标红
				/*if(!ArrFileUtil.isEmpty(value)){
					if(null == greenStyle){
						greenStyle = workBook.createCellStyle();
						if(null != style[columnIdx]){
							greenStyle.cloneStyleFrom(style[columnIdx]);
						}
						greenStyle = fillForegroundColorGreen(greenStyle);
					}
					cell.setCellStyle(greenStyle);
				}else{
					if(null == redStyle){
						redStyle = workBook.createCellStyle();
						if(null != style[columnIdx]){
							redStyle.cloneStyleFrom(style[columnIdx]);
						}
						redStyle = fillForegroundColorRed(redStyle);
					}
					cell.setCellStyle(redStyle);
				}*/
				++columnIdx;
			}
			//取下一行
			++startRow;
		}
	}
	/**
	 * 填充红色
	 * @param cellStyle
	 * @return
	 */
    private  HSSFCellStyle fillForegroundColorRed(HSSFCellStyle cellStyle) {
    	cellStyle.setFillForegroundColor(HSSFColor.DARK_RED.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }
    /**
	 * 填充绿色
	 * @param cellStyle
	 * @return
	 */
    private  HSSFCellStyle fillForegroundColorGreen(HSSFCellStyle cellStyle) {
    	cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }
	/**
	 * 获取报表模板
	 * @param templateFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private String getTemplatePath()throws Exception{
		String packageName = this.getClass().getPackage().getName();
		String moduleName = packageName.replaceAll("^com.sf.module.", "");
		moduleName = moduleName.substring(0, moduleName.indexOf("."));
		String webRoot = ApplicationContext.getContext().getServletContext().getRealPath("/");
		String tmp_TemplateFile = webRoot + "pages" + File.separator + moduleName 
				+ File.separator + "template" + File.separator + this.TEMPLATE_NAME;
		return tmp_TemplateFile;
	}
}
