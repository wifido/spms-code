package com.sf.module.common.util;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import static com.sf.module.common.util.TemplateHelper.createCell;
import static com.sf.module.common.util.TemplateHelper.getCellValueAsString;
import static com.sf.module.common.util.TemplateHelper.readExcel;
import static com.sf.module.common.util.TemplateHelper.removeEmptyRow;
import static com.sf.module.common.util.TemplateHelper.templateName;
import static com.sf.module.common.util.TemplateHelper.wirteToExcel;
import static com.sf.module.operation.util.CommonUtil.getReturnPageFileName;
import static com.sf.module.operation.util.CommonUtil.getSaveFilePath;
import static com.sf.module.operation.util.CommonUtil.setDownloadFileName;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sf.module.common.domain.ImportModel;
import com.sf.module.common.domain.ImportResult;

public abstract class AbstractImportHandler<T extends ImportModel> {
	private static final String TITLE_ERROR_COLUMN = "验证错误提示";
	private File uploadFile;
	public final Map<Integer, String> headerMap = newHashMap();
	public final ImportResult result = new ImportResult();
	public boolean isValidPass = true;

	public AbstractImportHandler(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public void handle() throws Exception {
		HSSFWorkbook workbook = readExcel(uploadFile);
		HSSFSheet sheet = workbook.getSheetAt(0);
		setHeader(sheet);
		addErrorColumn(sheet);
		handleCommonColumn(sheet);
		splitMergedRegions(sheet);
		List<T> datas = readData(sheet);
		result.setTotalCount(datas.size());
		validData(datas, sheet);
		saveToDb(datas);
		if (!isValidPass) {
			handleErrorData(datas, sheet);
			removeEmptyRow(sheet);
			setDownloadFileName(templateName(downloadName()));
			wirteToExcel(workbook, getSaveFilePath(AbstractImportHandler.class));
			result.setErrorDataDownloadPath(getReturnPageFileName());
		}
	}
	
	public void handleRestTime() throws Exception {
		HSSFWorkbook workbook = readExcel(uploadFile);
		HSSFSheet sheet = workbook.getSheetAt(0);
		setHeader(sheet);
		addErrorColumn(sheet);
		handleCommonColumn(sheet);
		splitMergedRegions(sheet);
		List<T> datas = readData(sheet);
		result.setTotalCount(datas.size());
		validData(datas, sheet);
		saveToDb(datas);
		if (!isValidPass) {
			handleErrorDataRestTime(datas, sheet);
			removeEmptyRow(sheet);
			setDownloadFileName(templateName(downloadName()));
			wirteToExcel(workbook, getSaveFilePath(AbstractImportHandler.class));
			result.setErrorDataDownloadPath(getReturnPageFileName());
		}
	}


	private void setHeader(HSSFSheet sheet) {
		HSSFRow hiddenRow = sheet.getRow(hiddenRowIndex());
		for (int i = 0; i < hiddenRow.getLastCellNum(); i++) {
			HSSFCell cell = hiddenRow.getCell(i);
			headerMap.put(i, getCellValueAsString(cell));
		}
	}

	private void addErrorColumn(HSSFSheet sheet) {
		HSSFRow row = sheet.getRow(titleRowIndex());
		createCell(row, lastColumnIndex() + 1, TITLE_ERROR_COLUMN);
		sheet.setColumnWidth(row.getCell(lastColumnIndex() + 1).getCellNum(), 10000);
	}

	private void splitMergedRegions(HSSFSheet sheet) {
		int numberMerged = sheet.getNumMergedRegions();
		for (int i = 0; i < numberMerged; i++) {
			CellRangeAddress rangeAddress = sheet.getMergedRegion(i);
			String cellValue = getCellRangeValue(sheet, rangeAddress);
			writeCellRangeValue(sheet, rangeAddress, cellValue);
		}
	}

	private String getCellRangeValue(HSSFSheet sheet, CellRangeAddress rangeAddress) {
		for (int j = rangeAddress.getFirstRow(); j <= rangeAddress.getLastRow(); j++) {
			HSSFRow row = sheet.getRow(j);
			for (int k = rangeAddress.getFirstColumn(); k <= rangeAddress.getLastColumn(); k++) {
				HSSFCell cell = row.getCell(k);
				String cellvalue = getCellValueAsString(cell);
				if (isNotBlank(cellvalue)) {
					return cellvalue;
				}
			}
		}
		return "";
	}

	private void writeCellRangeValue(HSSFSheet sheet, CellRangeAddress rangeAddress, String cellValue) {
		for (int j = rangeAddress.getFirstRow(); j <= rangeAddress.getLastRow(); j++) {
			HSSFRow row = sheet.getRow(j);
			for (int k = rangeAddress.getFirstColumn(); k <= rangeAddress.getLastColumn(); k++) {
				HSSFCell cell = row.getCell(k);
				cell.setCellValue(cellValue);
			}
		}
	}

	private T getT() throws InstantiationException, IllegalAccessException {
		Type sType = getClass().getGenericSuperclass();
		Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
		Class<T> mTClass = (Class<T>) (generics[0]);
		return mTClass.newInstance();
	}

	private List<T> readData(HSSFSheet sheet) throws Exception {
		int totalRow = sheet.getPhysicalNumberOfRows();
		List<T> datats = newArrayList();
		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		for (int i = startRowIndex(); i <= totalRow; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null)
				continue;
			T t = getT();
			for (int j = 0; j <= lastColumnIndex(); j++) {
				String property = headerMap.get(j);
				String cellValue = getCellValueAsString(row.getCell(j));
				beanUtilsBean.setProperty(t, property, cellValue);
			}
			((ImportModel) t).setRowIndex(i);
			datats.add(t);
		}
		return datats;
	}
	
	private void handleErrorData(List<T> datas, HSSFSheet sheet) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (T t : datas) {
			ImportModel modle = ((ImportModel) t);
			int rowIndex = modle.getRowIndex();
			 String errorInfo = modle.getErrorMsg();
			createCell(sheet.getRow(rowIndex), lastColumnIndex() + 1, errorInfo);
		}
	}

	private void handleErrorDataRestTime(List<T> datas, HSSFSheet sheet) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (T t : datas) {
			ImportModel modle = ((ImportModel) t);
			int rowIndex = modle.getRowIndex();
			 HSSFRow row = sheet.getRow(rowIndex);
			 HSSFCell cellEndTime =row.getCell(lastColumnIndex());
			 cellEndTime.setCellValue(formatTime(modle.getEndTime()));
			 HSSFCell cellStartTime =row.getCell(lastColumnIndex()-1);
			 cellStartTime.setCellValue(formatTime(modle.getStartTime()));
			 String errorInfo = modle.getErrorMsg();
			createCell(sheet.getRow(rowIndex), lastColumnIndex() + 1, errorInfo);
		}
	}
	private String formatTime (String time){
		String hh = time.substring(0, 2);
		String ss =time.substring(2, 4);
		String date = hh +':'+ss;
		return date;
	}

	public abstract int hiddenRowIndex();

	public abstract int startRowIndex();

	public abstract int titleRowIndex();

	public abstract String downloadName();

	public abstract int lastColumnIndex();

	public abstract void handleCommonColumn(HSSFSheet sheet) throws Exception;

	public abstract void validData(List<T> datas, HSSFSheet sheet) throws Exception;

	public abstract void saveToDb(List<T> datas) throws Exception;

}
