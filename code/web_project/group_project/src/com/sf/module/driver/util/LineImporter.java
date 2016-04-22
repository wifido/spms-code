package com.sf.module.driver.util;

import com.sf.module.common.util.IOUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.driver.domain.LineImportTable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class LineImporter {
    public static List<LineImportTable> parseDriveLineFromFile(File uploadFile, String userCode) throws IOException {
        FileInputStream inputStream = null;
        List<LineImportTable> driveLines = newArrayList();

        try {
            inputStream = new FileInputStream(uploadFile);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);

            for (int index = 1; index <= sheet.getLastRowNum(); index++) {
                HSSFRow row = sheet.getRow(index);
                if (row == null || isEmptyCell(row))
                    continue;
                driveLines.add(constructOneDriveLine(row, userCode));
            }
        } finally {
            IOUtil.close(inputStream);
        }

        return driveLines;
    }

    private static boolean isEmptyCell(HSSFRow row) {
        int errorTotal = 0;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            HSSFCell cell = row.getCell(i);
            String cellValue = Template.getCellStrValue(cell);
            if (StringUtil.isBlank(cellValue))
                errorTotal++;
        }
        return errorTotal == row.getLastCellNum();
    }

    private static LineImportTable constructOneDriveLine(HSSFRow row, String userCode) {
        LineImportTable lineImportTable = new LineImportTable(userCode);

        for (int index = 0; index < 8; index++) {
            HSSFCell cell = row.getCell(index);
            String cellValue = Template.getCellStrValue(cell);
            lineImportTable.setValue(cellValue, index);
        }

        return lineImportTable;
    }
}
