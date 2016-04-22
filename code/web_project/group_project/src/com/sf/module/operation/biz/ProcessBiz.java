/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.biz;

import static com.sf.module.common.domain.Constants.KEY_ERROR;
import static com.sf.module.common.domain.Constants.KEY_MESSAGE;
import static com.sf.module.common.domain.Constants.KEY_SUCCESS;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import static com.sf.module.common.util.TemplateHelper.templateName;
import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.authorization.domain.User;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.util.StringUtil;
import com.sf.module.common.util.Template;
import com.sf.module.common.util.TemplateHelper;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;
import com.sf.module.operation.dao.IProcessDao;
import com.sf.module.operation.domain.EntityExport;
import com.sf.module.operation.domain.Process;
import com.sf.module.operation.util.CommonUtil;

/**
 * 工序管理的业务实现类
 * 
 * @author 632898 李鹏 2014-06-30
 */
public class ProcessBiz extends BaseBiz implements IProcessBiz {
	public static final String PROCESS_INFO_EXPORT = "工序信息导出_";
    public static final String PROCESS_CODE = "PROCESS_CODE";
    public static final String ESTIMATE_VALUE = "ESTIMATE_VALUE";
    public static final String INTENSITY_VALUE = "INTENSITY_VALUE";
    public static final String SKILL_VALUE = "SKILL_VALUE";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    public static final String DATA = "data";
    public static final String DATA_LIST = "dataList";
    public static final String USER_ID = "userId";
    public static final String IS_ONLY = "isOnly";
    public static final String HQ_CODE = "001";
    public static final String DIFFICULTY_VALUE = "DIFFICULTY_VALUE";
    public static final String FILE = "file";
    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_DOUBLE = "java.lang.Double";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
	public static final String PROCESS_IMPORT_ERROR_INFORMATION = "工序信息导入错误信息_";
	public static final String IMPORT_ERROR_INFORMATION = "导入出错！";
    public static final String DOWNLOAD_URL = "downLoadUrl";
    public static final String FILE_NAME = "fileName";
    public static final String FIELD_DIFFICULTY_MODIFY_VALUE = "difficultyModifyValue";
    public static final String FIELD_SKILL_VALUE = "skillValue";
    public static final String FIELD_ESTIMATE_VALUE = "estimateValue";
    public static final String FIELD_INTENSITY_VALUE = "intensityValue";
    public static final String FIELD_PROCESS_TOOL = "processTool";
    public static final String FIELD_PROCESS_AREA = "processArea";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_DIFFICULTY_VALUE = "difficultyValue";
    public static final String FIELD_PROCESS_NAME = "processName";
    public static final String FIELD_PROCESS_CODE = "processCode";
    public static final String DEPT_CODE = "deptCode";
    public static final String FIELD_DEPT_ID = "deptId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_FLAG = "flag";
    public static final String FILED_QUERY_PROCESS = "queryProcess";
    public static final String FIELD_EXPORT_PROCESS = "exportProcess";

    /**
	 * 工序管理的Dao接口
	 */
    private IProcessDao processDao;

    /**
	 * 设置工序管理的Dao接口
	 */
    public void setProcessDao(IProcessDao processDao) {
        this.processDao = processDao;
    }

    /**
	 * 用户拥有权限网点
	 */
    private IOssDepartmentDao ossDepartmentDao;

    public void setOssDepartmentDao(IOssDepartmentDao ossDepartmentDao) {
        this.ossDepartmentDao = ossDepartmentDao;
    }

    /**
	 * 查询工序信息
	 * 
	 * @return true or false
	 * @author 632898 李鹏
	 * @date 2014-06-30
	 */
    public HashMap queryProcess(HashMap paramsMap) {
        long userId = super.getUserId();
        paramsMap.put(USER_ID, userId);
        paramsMap.put(FIELD_FLAG, FILED_QUERY_PROCESS);
        return processDao.queryProcess(paramsMap);
    }

    /**
	 * 查询总部用来同步的工序信息
	 * 
	 * @return true or false
	 * @author 664525 莫航
	 * @date 2014-07-23
	 */
    public HashMap tbQueryProcess(HashMap paramsMap) {
        long userId = super.getUserId();
        paramsMap.put(USER_ID, userId);
        return processDao.tbQueryProcess(paramsMap);
    }

    /**
	 * 保存工序信息
	 * 
	 * @return true or false
	 * @author 632898 李鹏
	 * @date 2014-06-30
	 */
    public boolean saveProcess(HashMap paramsMap) {
        Process process = new Process();
        if (!super.getCurrentUser().getEmployee().getDepartment().getDeptCode().equals(HQ_CODE)) {
            return false;
        }
		// 网点ID
        if (paramsMap.get(DEPT_CODE) != null) {
            Long deptId = DepartmentCacheBiz.getDepartmentByCode(paramsMap.get(DEPT_CODE).toString()).getId();
            process.setDeptId(deptId);
        }
		// 设置新增的参数
        setParameter(paramsMap, process, true);
		// 保存数据
        processDao.save(process);
        return true;
    }

    /**
	 * 更新工序信息
	 * 
	 * @return true or false
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 */
    public boolean updateProcess(HashMap paramsMap) {
        Process process;
        //ID
        if (StringUtils.isEmpty(paramsMap.get(FIELD_PROCESS_ID))) {
            return false;
        } else {
            process = processDao.load(Long.parseLong(paramsMap.get(FIELD_PROCESS_ID).toString()));
        }
		// 设置修改的参数
        setParameter(paramsMap, process, false);

		// 执行更新
        processDao.update(process);
        return true;
    }

    private void setParameter(HashMap paramsMap, Process process, boolean operationType) {
		// 当 operationType 等于 true 时，为新增工序、false 为修改
        if (operationType) {
			// 工序代码
            if (isNotBlank(valueOf(paramsMap.get(FIELD_PROCESS_CODE))) && paramsMap.get(FIELD_PROCESS_CODE) != null) {
                process.setProcessCode(valueOf(paramsMap.get(FIELD_PROCESS_CODE)));
            }
			// 工序名称
            if (isNotBlank(valueOf(paramsMap.get(FIELD_PROCESS_NAME))) && paramsMap.get(FIELD_PROCESS_NAME) != null) {
                process.setProcessName(StringUtil.strReplace(valueOf(paramsMap.get(FIELD_PROCESS_NAME))));
            }
			// 强度需求
            if (isNotBlank(valueOf(paramsMap.get(FIELD_INTENSITY_VALUE))) && paramsMap.get(FIELD_INTENSITY_VALUE) != null) {
                process.setIntensityValue(parseDouble(valueOf(paramsMap.get(FIELD_INTENSITY_VALUE))));
            }
			// 判断需求
            if (isNotBlank(valueOf(paramsMap.get(FIELD_ESTIMATE_VALUE))) && paramsMap.get(FIELD_ESTIMATE_VALUE) != null) {
                process.setEstimateValue(parseDouble(valueOf(paramsMap.get(FIELD_ESTIMATE_VALUE))));
            }
			// 难度系数
            if (isNotBlank(valueOf(paramsMap.get(FIELD_DIFFICULTY_VALUE))) && paramsMap.get(FIELD_DIFFICULTY_VALUE) != null) {
                process.setDifficultyValue(parseDouble(valueOf(paramsMap.get(FIELD_DIFFICULTY_VALUE))));
            }
			// 技能需求
            if (isNotBlank(valueOf(paramsMap.get(FIELD_SKILL_VALUE))) && paramsMap.get(FIELD_SKILL_VALUE) != null) {
                process.setSkillValue(parseDouble(valueOf(paramsMap.get(FIELD_SKILL_VALUE))));
            }
			// 工序状态 1为有效 0 为无效
            process.setStatus(1);
			// 创建时间
            Date date = new Date();
            process.setCreateTm(date);
            process.setModifiedTm(date);
			// 创建人工号
            long userId = super.getUserId();
            process.setCreateEmpCode(Long.toString(userId));
        } else {
			// 强度需求
            process.setIntensityValue(isNotBlank(valueOf(paramsMap.get(FIELD_INTENSITY_VALUE))) ? parseDouble(valueOf(paramsMap.get(FIELD_INTENSITY_VALUE))) : null);
			// 判断需求
            process.setEstimateValue(isNotBlank(valueOf(paramsMap.get(FIELD_ESTIMATE_VALUE))) ? parseDouble(valueOf(paramsMap.get(FIELD_ESTIMATE_VALUE))) : null);
			// 技能需求
            process.setSkillValue(isNotBlank(valueOf((paramsMap.get(FIELD_SKILL_VALUE)))) ? parseDouble(valueOf(paramsMap.get(FIELD_SKILL_VALUE))) : null);
			// 难度系数调整
            process.setDifficultyModifyValue(isNotBlank(valueOf((paramsMap.get(FIELD_DIFFICULTY_MODIFY_VALUE))))
                    ? parseDouble(valueOf(paramsMap.get(FIELD_DIFFICULTY_MODIFY_VALUE))) : null);
			// 修改时间
            Date date = new Date();
            process.setModifiedTm(date);
			// 创建人工号
            long userId = super.getUserId();
            process.setModifiedEmpCode(Long.toString(userId));
        }
		// 工具使用
        process.setProcessTool(valueOf(paramsMap.get(FIELD_PROCESS_TOOL)));
		// 区域
        process.setProcessArea(valueOf(paramsMap.get(FIELD_PROCESS_AREA)));
    }

    /**
	 * 导出工序代码信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 */
    public HashMap exportProcess(HashMap paramsMap) {
		// 列数据容器
        HashMap columnMap = new HashMap();
		// 返回数据容器
        HashMap returnMap = new HashMap();
        try {
        	paramsMap.put(FIELD_FLAG, FIELD_EXPORT_PROCESS);
			// 获取导出的数据
            returnMap = processDao.queryProcess(paramsMap);
			// 定义导出文件名
            String downloadFileName = templateName(PROCESS_INFO_EXPORT, YYYYMMDDHHMMSS);
            CommonUtil.setDownloadFileName(downloadFileName);
			// 获取文件路径
            String exportFileName = CommonUtil.getSaveFilePath(Process.class);
            File file = new File(exportFileName);
			// 判断文件是否存在
            if (!file.exists())
                file.createNewFile();
			// 读取模板文件
			FileInputStream template = new FileInputStream(CommonUtil.getReportTemplatePath("工序导入模板.xls"));
			// 获取模板文件WorkBook
            HSSFWorkbook workbook = new HSSFWorkbook(template);
			// 获取第一页
            Sheet sheet = workbook.getSheetAt(0);
			// 获取模板列数据
            columnMap = new EntityExport().getColumnMap(workbook, columnMap, 0, 2);
			// 获取导出数据
            List list = (List) returnMap.get(ROOT);
			// 把数据写入Excel
            workbook = new EntityExport().writeWorkBook(workbook, 0, 4, columnMap, list, true);
			// 读取导出文件流
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
			// 把数据写入Excel文件
            workbook.write(fos);
			// 关闭流
            fos.close();
            template.close();
			// 文件路劲
            returnMap.put(FILE_NAME, CommonUtil.getReturnPageFileName());
        } catch (Exception e) {
            log.error(KEY_ERROR, e);
            returnMap.put(KEY_SUCCESS, false);
            returnMap.put(KEY_MESSAGE, IMPORT_ERROR_INFORMATION);
            return returnMap;
        }
        return returnMap;
    }

    private boolean isEmptyRow(HSSFRow row, HashMap columnMap) {
		// 工序代码校验
        int cellProcessCode = Integer.parseInt(columnMap.get(PROCESS_CODE)
                .toString());
		if (null == row)
			return true;
		return StringUtil.isBlank(Template.getCellStrValue(row.getCell(cellProcessCode)));
    }

    /**
	 * 导入工序代码信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 */
    public HashMap importProcess(HashMap paramsMap) {
		// 列数据容器
        HashMap columnMap = new HashMap();
		// 返回数据容器
        HashMap dateMap = new HashMap();
		// 验证错误的数据容器
        HashMap errorMap = new HashMap();
        try {
			// 获取导入的文件
            File file = (File) paramsMap.get(FILE);
			// 读取上传的Excel数据
            HSSFWorkbook workbook = getUploadExcel(file);
			// 得到第一页签数据
            HSSFSheet sheet = workbook.getSheetAt(0);
			// 获取数据总条数
            int rowTotality = sheet.getLastRowNum();
			// 读取Excel的列数据
            columnMap = new EntityExport().getColumnMap(workbook, columnMap, 0, 2);
			// 读取实体类的列信息
            HashMap EntityMap = new EntityExport().getColumnMap(workbook, columnMap, 0, 3);
			// 上传数据成功和失败的记录数
            int successTotal = 0, errorTotal = 0;
			// 当超出规定数据时
            if (validationImportTotalFaild(dateMap, rowTotality)) return dateMap;
			// 遍历数据
            for (int i = 4; i <= rowTotality; i++) {
                HSSFRow row = sheet.getRow(i);
                if (isEmptyRow(row, columnMap)) {
                    continue;
                }
				// 获取数据校验结果
                Map<String, String> validationMap = validRow(row, columnMap);
                if (validationFailed(errorMap, row, validationMap)) {
                    errorTotal++;
                    continue;
                }
                Process process = new Process();
				// 设置保存数据
                setProcessParameter(columnMap, EntityMap, row, process);
				// 保存数据
                processDao.save(process);
                successTotal++;
            }
            dateMap.put(KEY_SUCCESS, true);
            dateMap.put(KEY_MESSAGE, StringUtil.extractHandlerResult(successTotal, errorTotal));
            if (errorTotal > 0) {
				// 定义校验未通过返回数据的文件名
                String downloadFileName = TemplateHelper.templateName(PROCESS_IMPORT_ERROR_INFORMATION, YYYYMMDDHHMMSS);
				// 获取文件下载路径
				String downLoadUrl = new EntityExport().errorWorkBook(workbook, errorMap, 0, 4, columnMap, downloadFileName, "工序导入模板.xls", 9);
                dateMap.put(DOWNLOAD_URL, downLoadUrl);
            }
        } catch (Exception e) {
            log.error(KEY_ERROR, e);
            dateMap.put(KEY_SUCCESS, false);
            dateMap.put(KEY_MESSAGE, IMPORT_ERROR_INFORMATION);
            return dateMap;
        }
        return dateMap;
    }

    private boolean validationFailed(HashMap errorMap, HSSFRow row, Map<String, String> validMap) {
		// 判断校验是否通过
        if (validMap.get(KEY_SUCCESS).equals(FALSE)) {
            errorMap.put(valueOf(row.getRowNum()), validMap.get(KEY_MESSAGE));
            return true;
        }
        return false;
    }

    private boolean validationImportTotalFaild(HashMap hashMap, int rowTotality) {
		// 验证导入的数据量
        if (rowTotality >= 1004) {
            hashMap.put(KEY_SUCCESS, false);
			hashMap.put(KEY_MESSAGE, "导入数据超过1000条！");
            return true;
        }
        return false;
    }

    private HSSFWorkbook getUploadExcel(File file) throws IOException {
		// 读取文件流
        FileInputStream fileInputStream = new FileInputStream(file);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
		// 读取上传的Excel
        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
		// 关闭文件流
        fileInputStream.close();
        return workbook;
    }

    private void setProcessParameter(HashMap columnMap, HashMap entityMap, HSSFRow row, Process process) throws IllegalAccessException, InvocationTargetException {
		// 读取实体类的方法
        Method[] methods = process.getClass().getMethods();
        for (Method m : methods) {
            String methodName = m.getName();
            if (entityMap.get(methodName) != null) {
                Class[] parameterTypes = m.getParameterTypes();
				// 获取set方法的参数类型
                Class type = parameterTypes[0];
                int cellIndex = Integer.parseInt(columnMap.get(methodName).toString());
                if (null == row.getCell(cellIndex))
                    continue;
                row.getCell(cellIndex).setCellType(HSSFCell.CELL_TYPE_STRING);
				// 调用set方法赋值
                if (type.getName().equals(JAVA_LANG_STRING))
                    m.invoke(process, row.getCell(cellIndex).getStringCellValue());
                if (type.getName().equals(JAVA_LANG_DOUBLE)) {
                    if (!StringUtils.isEmpty(row.getCell(cellIndex).getStringCellValue()))
                        m.invoke(process, parseDouble(row.getCell(cellIndex).getStringCellValue()));
                }
            }
        }
		// 获取总部Id
        long deptId = DepartmentCacheBiz.getDepartmentByCode(HQ_CODE).getId();
        process.setDeptId(deptId);
		// 不导入修正难度系数
        process.setDifficultyModifyValue(null);
		// 创建时间
        Date date = new Date();
        process.setCreateTm(date);
        process.setModifiedTm(date);
        long userId = super.getUserId();
		// 工序状态 1为有效 0 为无效
        process.setStatus(1);
		// 创建人工号
        process.setCreateEmpCode(Long.toString(userId));
    }

    /**
	 * 校验导入数据
	 * 
	 * @param row
	 * @param columnMap
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-01
	 */
    private Map<String, String> validRow(HSSFRow row, HashMap columnMap) {
        Map<String, String> resultMap = new HashMap<String, String>();
        StringBuilder errorMsg = new StringBuilder();
        try {
			// 判断是否有权限
            if (ossDepartmentDao.getDeptByUserId(super.getUserId(), DepartmentCacheBiz.getDepartmentByCode(HQ_CODE).getId()).size() == 0) {
				errorMsg.append("没有总部网点权限;");
            }
			// 校验工序代码
            validationProcessCodeValue(row, columnMap, errorMsg);
			// 校验判断需求
            validationEstimateValue(row, columnMap, errorMsg);
			// 校验强度需求
            validationIntensityValue(row, columnMap, errorMsg);
			// 校验技能需求
            validationSkillValue(row, columnMap, errorMsg);
			// 校验难度系数
            validationDifficultyValue(row, columnMap);

            setValidationResultsByErrorMsg(resultMap, errorMsg);
        } catch (Exception e) {
            resultMap.put(KEY_SUCCESS, FALSE);
			resultMap.put(KEY_MESSAGE, "校验信息出错！" + e.getMessage());
            return resultMap;
        }
        return resultMap;
    }

    private void setValidationResultsByErrorMsg(Map<String, String> validMap, StringBuilder errorMsg) {
        if (errorMsg.length() > 0) {
            validMap.put(KEY_SUCCESS, FALSE);
            validMap.put(KEY_MESSAGE, errorMsg.toString());
        } else {
            validMap.put(KEY_SUCCESS, TRUE);
        }
    }

    private void validationDifficultyValue(HSSFRow row, HashMap columnMap) {
        int cellDifficultyValue = Integer.parseInt(columnMap.get(DIFFICULTY_VALUE).toString());
        if (null == row.getCell(cellDifficultyValue))
            row.createCell(cellDifficultyValue);
        row.getCell(cellDifficultyValue).setCellType(HSSFCell.CELL_TYPE_STRING);
    }

    private void validationSkillValue(HSSFRow row, HashMap columnMap, StringBuilder errorMsg) {
        int cellSkillValue = Integer.parseInt(columnMap.get(SKILL_VALUE).toString());
        if (null == row.getCell(cellSkillValue))
            row.createCell(cellSkillValue);
        row.getCell(cellSkillValue).setCellType(HSSFCell.CELL_TYPE_STRING);
        String skillValue = row.getCell(cellSkillValue).getStringCellValue();
        if (!StringUtils.isEmpty(skillValue)) {
            try {
                double d = parseDouble(skillValue);
                if (d < 0.01 || d > 10) {
					errorMsg.append("技能需求的值超出了0.01~10的范围。");
                }
            } catch (Exception e) {
				errorMsg.append("技能需求填写格式不正确，为0.01~10的数字含小数。");
            }
        }
    }

    private void validationIntensityValue(HSSFRow row, HashMap columnMap, StringBuilder errorMsg) {
        int cellIntensityValue = Integer.parseInt(columnMap.get(INTENSITY_VALUE).toString());
        if (null == row.getCell(cellIntensityValue))
            row.createCell(cellIntensityValue);
        row.getCell(cellIntensityValue).setCellType(HSSFCell.CELL_TYPE_STRING);
        String intensityValue = row.getCell(cellIntensityValue).getStringCellValue();
        if (!StringUtils.isEmpty(intensityValue)) {
            try {
                double d = parseDouble(intensityValue);
                if (d < 0.01 || d > 10) {
					errorMsg.append("强度需求的值超出了0.01~10的范围。");
                }
            } catch (Exception e) {
				errorMsg.append("强度需求填写格式不正确，为0.01~10的数字含小数。");
            }
        }
    }

    private void validationEstimateValue(HSSFRow row, HashMap columnMap, StringBuilder errorMsg) {
        int cellEstimateValue = Integer.parseInt(columnMap.get(ESTIMATE_VALUE).toString());
        if (null == row.getCell(cellEstimateValue))
            row.createCell(cellEstimateValue);
        row.getCell(cellEstimateValue).setCellType(HSSFCell.CELL_TYPE_STRING);
        String estimateValue = row.getCell(cellEstimateValue).getStringCellValue();
        if (!StringUtils.isEmpty(estimateValue)) {
            try {
                double d = parseDouble(estimateValue);
                if (d < 0 || d > 10) {
					errorMsg.append("判断需求的值超出了0~10的范围。");
                }
            } catch (Exception e) {
				errorMsg.append("判断需求填写格式不正确，为0~10的数字含小数。");
            }
        }
    }

    private void validationProcessCodeValue(HSSFRow row, HashMap columnMap, StringBuilder errorMsg) {
        int cellProcessCode = Integer.parseInt(columnMap.get(PROCESS_CODE).toString());
        if (null == row.getCell(cellProcessCode))
            row.createCell(cellProcessCode);
        row.getCell(cellProcessCode).setCellType(HSSFCell.CELL_TYPE_STRING);
        String processCode = row.getCell(cellProcessCode).getStringCellValue();
        if (StringUtils.isEmpty(processCode)) {
			errorMsg.append("工序代码不能为空；");
        } else if (processCode.length() > 6) {
			errorMsg.append("工序代码长度超过6个字符；");
        } else if (null != processDao.loadBy(FIELD_PROCESS_CODE, processCode)) {
			errorMsg.append("工序代码已存在；");
        }
    }

    /**
	 * 查询用户所在网点工序信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-07-02
	 */
    public HashMap queryUserProcess(HashMap paramsMap) {
        HashMap map = new HashMap();
		// 获取当前用户
        User user = (User) super.getCurrentUser();
		// 获取用户所在的网点信息
        Long deptId = user.getDept().getId();
        List<Process> list = processDao.findByDeptId(deptId);
        map.put(DATA, list);
        return map;
    }

    /**
	 * 工序确认
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-07-02
	 */
    @Transactional
    public HashMap confirmProcess(HashMap paramsMap) {
        HashMap map = new HashMap();
        if (StringUtils.isEmpty(paramsMap.get(DATA_LIST))) {
            map.put(KEY_SUCCESS, false);
			map.put(KEY_MESSAGE, "工序数据提交出错！");
            return map;
        }
		// 获取工序数据
        String processData = paramsMap.get(DATA_LIST).toString();
		// 转换为JsonArray容器数据
        JSONArray json = JSONArray.fromObject(processData);
		// 转换为实体类数据
        List<Process> confirmList = (List<Process>) JSONArray.toCollection(json, Process.class);
        long departmentId = confirmList.get(0).getDeptId();
        List<Process> loadList = processDao.findByDeptId(departmentId);

        List updateList = new ArrayList();
        List removeList = new ArrayList();
        List addList = new ArrayList();

		// 设置参数
        setConfirmProcessParameter(confirmList, departmentId, loadList, updateList, removeList, addList);

		// 删除当前用户所在网点的工序数据
        processDao.removeBatch(removeList);
		// 保存当前用户提交的工序数据
        processDao.saveBatch(addList);
		// 更新当前用户提交的工序
        processDao.updateBatch(updateList);
        map.put(KEY_SUCCESS, true);
		map.put(KEY_MESSAGE, "确认工序保存成功！");
        return map;
    }

    private void setConfirmProcessParameter(List<Process> confirmList, long departmentId, List<Process> loadList, List updateList, List removeList, List addList) {
		// 获取当前用户的网点信息
        User user = (User) super.getCurrentUser();
		// 遍历提交的工序数据
        for (int i = 0; i < confirmList.size(); i++) {
            Process process = confirmList.get(i);
			// 查询该网点下是否已存在该工序若存在则把状态该为启用 status =1 没有查询到网点下存在此工序则执行保存
            List<Process> list = processDao.loadProcessByCode(departmentId, process.getProcessCode());
            if (list.size() > 0) {
                Process p = list.get(0);
                p.setStatus(1);
                loadList.remove(p);
                p.setDifficultyModifyValue(process.getDifficultyModifyValue());
                updateList.add(p);
                continue;
            }
			// 网点
            process.setDeptId(departmentId);
			// 创建时间
            Date date = new Date();
			// 创建时间
            process.setCreateTm(date);
            process.setModifiedTm(date);
			// 工序状态
            process.setStatus(1);
			// 创建人工号
            process.setCreateEmpCode(Long.toString(user.getId()));
            addList.add(process);
        }
		// 遍历需要删除的数据中是否有被使用的
        for (Process p : loadList) {
			// 查询该工序是否有被使用，若是有则把状态该为停用 status =0
            if (processDao.isUsedProcess(p.getDeptId(), p.getProcessCode())) {
                p.setStatus(0);
                updateList.add(p);
            } else {
                removeList.add(p);
            }
        }
    }


    /**
	 * 判断工序的唯一性
	 * 
	 * @param map
	 *            (key 属性名 ， value 值);
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-03
	 */
    public HashMap isOnlyProcess(HashMap paramsMap) {
        paramsMap.remove(USER_ID);
        Iterator iterator = paramsMap.keySet().iterator();
        String key = null, value = null;
        HashMap dataMap = new HashMap();
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            value = (String) paramsMap.get(key);
        }
        boolean opera = false;
        if (null == processDao.loadBy(key, value))
            opera = true;
        dataMap.put(KEY_SUCCESS, true);
        dataMap.put(IS_ONLY, opera);
        return dataMap;
    }

    /**
	 * 读取总部工序更新信息
	 * 
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-09
	 */
    public HashMap pushMsg() {
        HashMap map = new HashMap();
        long deptId = DepartmentCacheBiz.getDepartmentByCode(HQ_CODE).getId();
        List list = processDao.pushMsg(deptId);
        map.put(TOTAL_SIZE, list.size());
        map.put(ROOT, list);
        return map;
    }

    /**
	 * 根据网点ID查询工序
	 * 
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-23
	 */
    public HashMap findByDeptId(Long departmentId) {
        HashMap map = new HashMap();
        List list = processDao.findByDeptId(departmentId);
        map.put(TOTAL_SIZE, list.size());
        map.put(ROOT, list);
        return map;
    }
}