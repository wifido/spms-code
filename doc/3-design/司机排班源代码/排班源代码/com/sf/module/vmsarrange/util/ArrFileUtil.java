package com.sf.module.vmsarrange.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.core.presentation.taglib.I18nEl;

/**
 * 文件处理工具类
 * 
 * @author 方芳 (350614) 2012-11-29
 */
public class ArrFileUtil {
	private static Logger log = LoggerFactory.getLogger(ArrFileUtil.class);
	/**数字格式*/
	protected static String NUMERIC_FORMAT="#";	
	/**日期格式**/
	protected static String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";
	public static String writeToTxt(String file, String content) throws BizException {
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			fw.write(content);
			fw.flush();
		} catch (Exception e) {
			log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
			throw new BizException("checkModule.generateZip.error2");
		} finally {
			if ( null!=fw) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new BizException("checkModule.generateZip.error2");
				}
			}
		}
		return file;
	}

	public static void deleteFile(String fileName){
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
	}
	public static void saveFile(String fileName, File file) throws BizException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(file);
			File f = new File(fileName);
			os = new FileOutputStream(f);
			if(!f.exists()){
				f.createNewFile();
			}
			if(is!=null){
				int b;
				while((b=is.read())!=-1){
					os.write(b);
				}
			}
		} catch (Exception e) {
			log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
			throw new BizException(I18nEl.i18n_def("checkModule.generateZip.error2","读取文件失败，文件不存在"));
		} finally {
			try{
				if(is!=null){is.close();}
				if(os!=null){os.close();}
			} catch (IOException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
			}
			
		}
	}
	/**
	 * 递归删除文件夹
	 * 
	 * @param f
	 *            文件夹的完整路径名
	 * @return
	 */
	public static void deleteFiles(File f) {
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] files = f.listFiles();
				if (files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						deleteFiles(files[i]);
					}
				}
				f.delete();
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 打包文件夹
	 * 
	 * @param inputFileName
	 *            文件夹的完整路径名
	 * @return
	 */
	public static void zip(String inputFileName) {
		String zipFileName = inputFileName + ".zip"; // 打包后文件名字
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
		} catch (FileNotFoundException e1) {
			log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e1);
			throw new BizException("checkModule.generateZip.error4");
		}
		File inputFile = new File(inputFileName);
		zip(out, inputFile, inputFile.getName());
		try {
			out.close();
		} catch (IOException e) {
			log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
			throw new BizException("checkModule.generateZip.error5");
		}
	}

	/**
	 * 解压zip
	 * 
	 * @param zipname
	 *            zip文件的完整路径名 outputPath 解压后的输出路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void unZipFile(String zipname, String outputPath) {
		ZipFile file = null;
		try {
			file = new ZipFile(zipname, "UTF-8");
		} catch (IOException e) {
			log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
			throw new BizException("vms.vmsinfo.fileUtil.error6");
		}
		Enumeration<ZipEntry> ze;
		ZipEntry z;
		File f;
		File dir;
		OutputStream out;
		InputStream in;
		// 遍历第一遍,清空解压目标路径原有数据
		ze = (Enumeration<ZipEntry>) file.getEntries();
		while (ze.hasMoreElements()) {
			z = ze.nextElement();
			if (z.isDirectory()) {
				dir = new File(outputPath + z.getName());
				// 如果目标路径存在则删除
				if (dir.exists()) {
					deleteFiles(dir);
				}
			}
		}
		// 遍历第二遍,解压文件到目标路径
		ze = (Enumeration<ZipEntry>) file.getEntries();
		while (ze.hasMoreElements()) {
			z = ze.nextElement();
			if (!z.isDirectory()) {
				f = new File(outputPath + z.getName());
				dir = new File(f.getParent());
				// 目标路径不存在则生成路径
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 目标文件不存在则生成文件
				if (!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
						throw new BizException("vms.vmsinfo.fileUtil.error1");
					}
				}
				try {
					in = file.getInputStream(z);
				} catch (Exception e) {
					log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
					throw new BizException("vms.vmsinfo.fileUtil.error2");
				}
				int b;
				try {
					out = new FileOutputStream(f);
				} catch (FileNotFoundException e) {
					log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
					throw new BizException("vms.vmsinfo.fileUtil.error3");
				}
				try {
					while ((b = in.read()) != -1) {
						out.write(b);
					}
				} catch (IOException e) {
					log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
					throw new BizException("vms.vmsinfo.fileUtil.error4");
				}
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
					throw new BizException("vms.vmsinfo.fileUtil.error5");
				}
			}
		}
	}

	// 递归打包文件夹
	public static void zip(ZipOutputStream out, File f, String base) {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			base += "/";
			try {
				out.putNextEntry(new ZipEntry(base));
			} catch (IOException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
				throw new BizException("checkModule.generateZip.error6");
			} // 添加文件夹到zip
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			try {
				out.putNextEntry(new ZipEntry(base));
			} catch (IOException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
				throw new BizException("checkModule.generateZip.error7");
			}
			FileInputStream in;
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
				throw new BizException("checkModule.generateZip.error8");
			}
			int b;
			try {
				while ((b = in.read()) != -1) {
					out.write(b);
				}
			} catch (IOException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
				throw new BizException("checkModule.generateZip.error9");
			}
			try {
				in.close();
			} catch (IOException e) {
				log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);
				throw new BizException("checkModule.generateZip.error10");
			}
		}
	}
	public static String getShortName(String fullName){
		if(fullName == null){
			return "";
		}
		if(fullName.equals("总部")){
			return fullName;
		}
		fullName = fullName.replace("经营本部", "");
		fullName = fullName.replace("管理区", "");
		fullName = fullName.replace("部", "");
		fullName = fullName.replace("Singapore Office", "");
		fullName = fullName.replace("Malaysia Business Area (", "");
		fullName = fullName.replace(")", "");
		fullName = fullName.replace(" JP country office", "");
		fullName = fullName.replace("香港區", "香港区");
		fullName = fullName.replace("台灣區", "台湾区");
		return fullName;
	}
	/**
	 * 封装HSSFCell值
	 * @param row -- HSSFRow 表格列
	 * @param colIdx -- 列位置 
	 * @param obj    -- 源值对象
	 * @param fieldName -- 源对象属性
	 *  eg:SysVehicleExceptionBiz
	 */
	public static void wrapperCell(HSSFRow row,int colIdx,Object obj,String fieldName,HSSFCellStyle style){
		wrapperCell(row, colIdx, obj, fieldName,style,null);
	}
	
	/**
	 * 封装HSSFCell值
	 * @param row -- HSSFRow
	 * @param colIdx -- 列位置 
	 * @param obj    -- 源对象
	 * @param fieldName -- 源对象中的属性名
	 * @param fmt -- 格式化,eg:数字取整-"#"
	 *  eg:SysVehicleExceptionBiz
	 */
	public static void wrapperCell(HSSFRow row,int colIdx,Object obj,String fieldName,HSSFCellStyle style,String fmt){
		HSSFCell cell = row.getCell(colIdx);
		if( null==cell){
			cell = row.createCell(colIdx);
		}
		if(style!=null){
			cell.setCellStyle(style);
		}
		//根据对象初始化对应的类加载器,以确定类对象是否存在
		Class<?> pt = null;
		try{
			pt = org.apache.commons.beanutils.PropertyUtils.getPropertyType(obj,fieldName);
			
		}catch(Exception e){log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);}
		if( null==pt ){ 
			return; 
		}
		//获取对应的属性值
		Object objVal = null;
		try{
			objVal = org.apache.commons.beanutils.PropertyUtils.getProperty(obj, fieldName);
		}catch(Exception e){log.error("com.sf.module.vmsinfo.util.FileUtil failure:",e);}
		
		if( null==objVal ){
			return ;
		}
		
		if( Date.class.equals(pt) ){
			String format = "yyyy-MM-dd hh:mm:ss";
			if( null!=fmt &&!"".equals(fmt.trim()) ){
				format = fmt;
			}
			cell.setCellValue(new java.text.SimpleDateFormat(format).format((Date)objVal));
			return;
		}
		
		if( Integer.class.equals(pt) || Float.class.equals(pt) || Double.class.equals(pt)
				||double.class.equals(pt) || float.class.equals(pt) ||int.class.equals(pt)){
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			String format = "#";
			if( null!=fmt && !"".equals(fmt.trim()) && !"%".equals(fmt.trim()) ){
				format = fmt;
			}
			//格式为百分比
			if( null!=fmt && "%".equals(fmt.trim()) ){
				double value = Double.parseDouble(new java.text.DecimalFormat("#.00").format(objVal));
				if(value==0){ //为0时特殊处理
					cell.setCellValue(0+"");
				}else{
					System.out.println(value);
					if((value-(int)value)==0){ //小数点后面为0时特殊处理
						cell.setCellValue((int)value+fmt);
					}else{
						cell.setCellValue(value+fmt);
					}
				}
			}else{
				cell.setCellValue(new java.text.DecimalFormat(format).format(objVal));
			}
			return;
		}
		
		cell.setCellValue(objVal.toString());
	}
	/**
	 * 封装HSSFCell值
	 * @param row -- HSSFRow
	 * @param colIdx -- 列位置 
	 * @param cellValue -- 单元格的值
	 *  eg:SysVehicleExceptionBiz
	 */
	public static void wrapperCellStyle(HSSFRow row,int colIdx,String cellValue,HSSFCellStyle style){
		HSSFCell cell = row.getCell(colIdx);
		if( null==cell){
			cell = row.createCell(colIdx);
		}
		if(style!=null){
			cell.setCellStyle(style);
		}
		cell.setCellValue(cellValue);
	}
	/**
	 * 封装HSSFCell值
	 * @param row -- HSSFRow
	 * @param colIdx -- 列位置 
	 * @param cellValue -- 单元格的值
	 *  eg:SysVehicleExceptionBiz
	 */
	public static void wrapperCell(HSSFRow row,int colIdx,String cellValue){
		HSSFCell cell = row.getCell(colIdx);
		if( null==cell){
			cell = row.createCell(colIdx);
		}
		cell.setCellValue(cellValue);
	}
	/**
	 * 设置自动列宽
	 * @param columnCount -- 需要设置自动列宽的列数
	 * @param sheet -- sheet页 
	 *  eg:SysVehicleExceptionBiz
	 */
	public static void columnWidth(int columnCount, HSSFSheet sheet) {
        // 设置sheet每列的宽度,自动,根据需求自行确定
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
	/**
	 * 内部函数
	 *解析导入文件中的数据， 转换excel表格的全部数据为list集合
	 * @param sheet 表格
	 * @param header 标题
	 * @param datas 数据
	 * @throws Exception
	 * @return 返回最短的列数
	 *  eg:TaskMailBiz
	 */
	@SuppressWarnings("unchecked")
	public static int parseSheet(HSSFSheet sheet, String[] header,java.util.List<Map<String,String>> datas,int startRow,String[] formatHeader,String format)throws Exception{
		int columnCount = 0;
		for(int i=startRow-1;i<=sheet.getLastRowNum();i++){
			HSSFRow row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int column = row.getLastCellNum()-row.getFirstCellNum();
			// 获取对应的数据行,不获取空行数据
			@SuppressWarnings("rawtypes")
			Map dataMap = new HashMap<String,String>(row.getPhysicalNumberOfCells());
			boolean check = false; // 标志是否为空行
			for(int j=0;j<header.length;j++){
				String finalFormat = null;
				finalFormat = getFormat(formatHeader,format,header[j]);
				String value = getHSSFCellValue(row.getCell(j),finalFormat);
				if(!"".equals(value)){
					check = true;
				}
				dataMap.put(header[j],value);
			}	
			if(check){
				datas.add(dataMap);
				if(columnCount==0||columnCount>column){
					columnCount = column;
				}
			}
		}
		return columnCount;
	}
	/**
	 * 检测是否有设置格式化
	 * @return 有则返回格式化格式
	 */
	private static String getFormat(String[] header,String format,String checkValue){
		if(header==null||header.length<1){
			return null;
		}
		boolean check = false;
		for(int i=0;i<header.length;i++){
			if(header[i].equals(checkValue)){
				check = true;
				break;
			}
		}
		if(check&&format!=null){
			return format;
		}
		return null;
	}
	/**
	 * 内部函数
	 * 解析导入文件单元格数据,获取excel单元格的值
	 * @author 方芳 (350614)
	 * @date 2012-10-15 
	 * @param cell
	 * @return
	 * eg:TaskMailBiz
	 */
	public static String getHSSFCellValue(HSSFCell cell,String format){
		String val = null;
		DecimalFormat decimalFormat = new DecimalFormat(format==null?NUMERIC_FORMAT:format);
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
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
				val = cell.getRichStringCellValue().getString(); 
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				try {
					if( HSSFDateUtil.isCellInternalDateFormatted(cell) || HSSFDateUtil.isCellDateFormatted(cell) ){					
						double dValue = HSSFDateUtil.getExcelDate(cell.getDateCellValue());
						val = dateFormat.format(HSSFDateUtil.getJavaDate(dValue));
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
		return val;
	}
	public static boolean isEmpty(String s){
		if(s==null || "".equals(s.trim())){
			return true;
		}
		return false;
	}
	/**
	 * 校验是否超长
	 * @param value 被校验的值
	 * @param maxLength 最大字节长度
 	 * @return 如果超长 返回true 没有超长 返回false
	 */
	public static boolean checkOverMaxLength(String value,int maxLength){
		if(isEmpty(value)){
			return false;
		}
		if(maxLength<1){
			return true;
		}
		String replaceValue = value.replaceAll( "[^\\x00-\\xff]" , "***" );
		if(replaceValue.length()>maxLength){
			return true;
		}
		return false;
	}
}
