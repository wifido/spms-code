package com.sf.module.driver.biz;

import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static com.sf.module.common.util.DateFormatType.yyyyMM;
import static com.sf.module.common.util.DateFormatType.yyyyMMdd;
import static com.sf.module.common.util.DateUtil.parseDate;
import static org.apache.commons.lang.time.DateUtils.addMonths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.util.DateUtil;
import com.sf.module.driver.dao.DriverWarningDao;
import com.sf.module.driver.domain.WarningModel;
import com.sf.module.driver.domain.WarningModel.MultipleWarning;
import com.sf.module.driver.util.DriverWarningContinuousExport;
import com.sf.module.driver.util.DriverWarningExporter;

@Component
public class DriverWarningBiz extends BaseBiz {

    private static final String WARNING_REPORT_EXPORT_NAME = "预警报表";
    @Resource
    private DriverWarningDao driverWarningDao;
    
    // 查询单月预警数据
	public Map<String, Object> query(String departmentCode,
			String warningMonth, int start, int limit) {
		// 查询预警数据、通过分页
		List<WarningModel> list = driverWarningDao.querySingleWarning(
				departmentCode, getCalculateWarningStartTime(warningMonth),
				getCalculateWarningEndTime(warningMonth), start, limit);

		// 查询预警总数
		int totalSize = driverWarningDao.countSingleWarning(departmentCode,
				getCalculateWarningStartTime(warningMonth),
				getCalculateWarningEndTime(warningMonth));

		// 构建数据集返回
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(ROOT, list);
		resultMap.put(TOTAL_SIZE, totalSize);
		
		return resultMap;
	}

	// 查询单月预警需要导出的数据
	public String export(String departmentCode, String warningMonth) {
		// 查询获取导出数据
		List<WarningModel> list = driverWarningDao.exportSingleWarning(
				departmentCode, getCalculateWarningStartTime(warningMonth),
				getCalculateWarningEndTime(warningMonth));
		
		// 导出单月预警数据
		DriverWarningExporter driverWarningExporter = new DriverWarningExporter();
		driverWarningExporter.writeAsObject(list, WARNING_REPORT_EXPORT_NAME);

		// 返回导出成功，生成的临时Excel文件
		return driverWarningExporter.getTargetFilePath();
	}

	// 查询季度预警数据
	public Map<String, Object> queryDriverContinuousWarningReport(
			String departmentCode, String warningMonth, int start, int limit) {
		// 查询数据
		List<MultipleWarning> queryMultipleWarning = driverWarningDao
				.queryMultipleWarning(departmentCode,
						getContinuousWarningStartTime(warningMonth),
						getContinuousWarningEndTime(warningMonth), start, limit);
		
		// 查询季度预警总数
		int totalSize = driverWarningDao.queryCountMultipleWarning(
				departmentCode, getContinuousWarningStartTime(warningMonth),
				getContinuousWarningEndTime(warningMonth));
		
		// 构建数据集返回
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(ROOT, queryMultipleWarning);
        resultMap.put(TOTAL_SIZE, totalSize);
        
		return resultMap;
	}

	// 导出季度预警数据、返回生成临时Excel 路径
    public String exportContinuous(String departmentCode, String warningMonth) {
    	// 获取导出数据
		List<WarningModel.MultipleWarning> multipleWarning = driverWarningDao
				.getExportMultipleWarning(departmentCode,
						getContinuousWarningStartTime(warningMonth),
						getContinuousWarningEndTime(warningMonth));

		//导出数据
        DriverWarningContinuousExport driverWarningContinuousExport = new DriverWarningContinuousExport();
        driverWarningContinuousExport.writeAsObject(multipleWarning, "连续6天统计报表");

        // 返回生成临时Excel 路径
        return driverWarningContinuousExport.getTargetFilePath();
    }

    // 通过传入得年月、获取年月的第一天
    public static String getCalculateWarningStartTime(String warningMonth) {
        try {
            return DateUtil.formatDate(DateUtils.setDays(parseDate(warningMonth, yyyyMM), 1), yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过传入得年月、获取单月预警年月的最后一天
    public static String getCalculateWarningEndTime(String warningMonth) {
        try {
            return DateUtil.formatDate(DateUtils.addDays(addMonths(parseDate(warningMonth, yyyyMM), 1), -1), yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 传入年月、获取传入的年月前两月的第一天
    public static String getContinuousWarningStartTime(String warningMonth) {
        try {
            return DateUtil.formatDate(addMonths(parseDate(warningMonth, yyyyMM), -2), yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过传入得年月、获取季度预警年月的最后一天
    public static String getContinuousWarningEndTime(String warningMonth) {
        try {
            return DateUtil.formatDate((DateUtils.addDays(addMonths(parseDate(warningMonth, yyyyMM), 1), -1)), yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 通过传入得年月、获取月的月份
    public static String getContinuousWarningSecondMonth(String warningMonth) {
        try {
            return DateUtil.formatDate(addMonths(parseDate(warningMonth, yyyyMMdd), 1), yyyyMM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}