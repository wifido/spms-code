package io;

import domain.Employee;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FileOperator {

    public static final String TABLE_TM_OSS_EMPLOYEE = "TM_OSS_EMPLOYEE";

    public static List<Employee> readExcelFile(File file) {
        List<Employee> employees = newArrayList();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNumber = sheet.getLastRowNum();

            for (int index = 1; index < lastRowNumber + 1; index++) {
                HSSFRow row = sheet.getRow(index);

                String userCode = "";
                try {
                    userCode = String.valueOf(row.getCell(0)
                            .getStringCellValue());
                } catch (Exception ex) {
                    userCode = String.valueOf(row.getCell(0)
                            .getNumericCellValue());
                }
                String username = row.getCell(1).getStringCellValue();
                String userSex = row.getCell(2).getStringCellValue();
                String workType = row.getCell(3).getStringCellValue();
                String dutyName = row.getCell(11).getStringCellValue();
                String departmentCode = row.getCell(9).getStringCellValue();
                String postType = "";
                try {
                    postType = row.getCell(20).getStringCellValue();
                } catch (Exception ex) {
                }

                Employee employee = new Employee(userCode, username, userSex,
                        workType, dutyName, departmentCode, postType);

                if ("包裹处理序列".equals(postType) || ("仓管员".equals(dutyName) || "仓管组长".equals(dutyName))) {
                    employees.add(employee);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(fileInputStream);
        }

        return employees;
    }

    public static void writeFile(File file, List<Employee> employees) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

//            writeHeader(fileOutputStream, TABLE_TM_OSS_EMPLOYEE);
            int index = 0;
            int time = 1;
            for (Employee employee : employees) {
                fileOutputStream.write(employee.toSql().getBytes());
                fileOutputStream.write("\n".getBytes());
                index++;
                if (index == 500) {
                    fileOutputStream.write("commit;".getBytes());
                    fileOutputStream.write("\n".getBytes());
                    index = 0;
                }
//                fileOutputStream.write(("prompt" + (time * 100) + " size records loaded\n").getBytes());
            }

//            writeFooter(fileOutputStream, TABLE_TM_OSS_EMPLOYEE, employees.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFooter(FileOutputStream fileOutputStream, String tableName, int size) throws IOException {
        String footer = "commit;\n"
                + "prompt " + size + " records loaded\n"
                + "prompt Enabling triggers for " + tableName + "...\n"
                + "alter table " + tableName + " enable all triggers;"
                + "set feedback on\n"
                + "set define on\n"
                + "prompt Done.";
        fileOutputStream.write(footer.getBytes());
    }

    private static void writeHeader(FileOutputStream fileOutputStream, String tableName) throws IOException {
        String header = "prompt PL/SQL Developer import file\n"
                + "prompt Created on 2014-11-05 by 053452\n"
                + "set feedback off\n"
                + "set define off\n"
                + "prompt Disabling triggers for " + tableName + "...\n"
                + "alter table " + tableName + " disable all triggers;\n"
                + " prompt Loading " + tableName + "\n";
        fileOutputStream.write(header.getBytes());
    }

    private static void closeInputStream(FileInputStream fileInputStream) {
        try {
            if (fileInputStream != null)
                fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
