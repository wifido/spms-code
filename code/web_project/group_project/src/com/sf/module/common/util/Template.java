package com.sf.module.common.util;

import com.sf.module.driver.domain.DriveLine;
import com.sf.module.operation.util.CommonUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public abstract class Template {
    protected String targetFilePath;

    public String getTargetFilePath() {
        return targetFilePath;
    }

    protected abstract void createHeader(HSSFRow row, Workbook workbook);

    public void write(List<Map<String, Object>> results, String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFSheet sheet = workbook.createSheet(getSheetName());
        HSSFRow row = sheet.createRow(0);
        createHeader(row, workbook);

        for (int i = 0; i < results.size(); i++) {
            row = sheet.createRow(i + headerRowCount());
            Map<String, Object> result = results.get(i);
            createSingleCell(row, result, cellStyle);
        }
        writeToFile(workbook, fileName);
    }

    public void writeAsObject(List<?> results, String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFSheet sheet = workbook.createSheet(getSheetName());
        HSSFRow row = sheet.createRow(0);
        createHeader(row, workbook);

        for (int i = 0; i < results.size(); i++) {
            row = sheet.createRow(i + headerRowCount());
            Object result = results.get(i);
            createSingleCell(row, result, cellStyle);
        }
        writeToFile(workbook, fileName);
    }

    private void writeToFile(HSSFWorkbook workbook, String lineInformationExport) {
        try {
            CommonUtil.setDownloadFileName(TemplateHelper.templateName(lineInformationExport));

            File file = new File(CommonUtil.getSaveFilePath(DriveLine.class));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);

            this.targetFilePath = CommonUtil.getReturnPageFileName();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 写入数据到Excel
    public void writeAsObjectToExcel(List<?> results, String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFSheet sheet = workbook.createSheet(getSheetName());
        HSSFRow row = sheet.createRow(0);
        createHeader(row, workbook);

        for (int i = 0; i < results.size(); i++) {
            row = sheet.createRow(i + headerRowCount());
            Object result = results.get(i);
            createSingleCell(row, result, cellStyle);
        }
        writeToCompressFile(workbook, fileName);
    }

    // 写入压缩文件路径
    private void writeToCompressFile(HSSFWorkbook workbook, String lineInformationExport) {
        try {
            CommonUtil.setDownloadFileName(TemplateHelper.templateName(lineInformationExport));

            File file = new File(CommonUtil.getSaveCompresFilePath(DriveLine.class));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);

            this.targetFilePath = file.getPath();

            IOUtil.close(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 导入
    public static String getCellStrValue(HSSFCell cellobj) {
        if (null == cellobj) {
            return "";
        }
        cellobj.setCellType(HSSFCell.CELL_TYPE_STRING);
        return cellobj.toString().trim();
    }

    public static boolean isEmptyRow(HSSFRow row, int num) {
    	int emptyCount = 1;
        for (int i = 1; i < num; i++) {
            HSSFCell c = row.getCell(i);
            if (StringUtil.isBlank(Template.getCellStrValue(c))) {
            	emptyCount ++;
            }
        }
        return emptyCount == num;
    }

    protected abstract int headerRowCount();

    protected abstract void createSingleCell(HSSFRow row, Object result, CellStyle cellStyle);

    protected abstract String getSheetName();
}