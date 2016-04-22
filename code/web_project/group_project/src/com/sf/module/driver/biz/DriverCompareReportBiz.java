package com.sf.module.driver.biz;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.domain.ExportResult;
import com.sf.module.driver.dao.DriverCompareReportDao;
import com.sf.module.driver.domain.ComparedReportRepository.ComparedReporter;
import com.sf.module.driver.util.CompareReportTemplate;

import org.hibernate.HibernateException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.util.Clock.allDateInRange;
import static com.sf.module.driver.domain.ComparedReportRepository.converDBListToReporterList;

@Service
@Scope("prototype")
public class DriverCompareReportBiz extends BaseBiz {
	@Autowired
	private DriverCompareReportDao driverCompareReportDao;
	private List<String> headerList = newArrayList("序号", "地区", "地区代码", "驾驶员姓名", "驾驶员工号");
	private static final List<String> headerBriefList = newArrayList("导出网点", "导出范围(统计周期):", "导出日期");

	private static final String REPORT_NAME = "排班异常统计报表";

	public ExportResult exportToFile(String departmentCode, Integer[] errorType, String startDate, String endDate) {
		ExportResult exprotResult = new ExportResult();

		try {
			// 通过数据库查询出需要导出在数据;
			List data = driverCompareReportDao.queryComparedDataByMonth(startDate, endDate, departmentCode, errorType,currentUserId());

			// 导出文件;
			String filePath = exportToFileAsExcel(data, departmentCode, startDate, endDate);

			exprotResult.setFilePathAfterSuccessed(filePath);
		} catch (HibernateException exception) {
			exprotResult.buildErrorMessage(String.format("数据库查询失败:%s", exception.getMessage()));
		} catch (Exception exception) {
			exprotResult.buildErrorMessage(String.format("写入excele报表失败:%s", exception.getMessage()));
		}

		return exprotResult;
	}
	
	// 构建特殊表头
	private List<String> injectValuesToBrief(String departmentCode, String startDate, String endDate) {
		DateTime now = DateTime.now();

		List<String> values = Arrays.asList("导出网点", departmentCode, "导出范围(统计周期):",
				String.format("%s%s%s", startDate, "至", endDate), "导出日期", now.toString("yyyy-MM-dd"));

		return values;
	}

	private String currentUserId() {
		return this.getCurrentUser().getId().toString();
	}

	private String exportToFileAsExcel(List<Map<String, Object>> data, String departmentCode, String startDate, String endDate) {
		List<String> extensionHeader = allDateInRange(startDate, endDate);
		
		// extract data;
		List<ComparedReporter> valueFromDb = converDBListToReporterList(data, extensionHeader.size());
		List<String> headerBrief = injectValuesToBrief(departmentCode, startDate, endDate);
		
		return this.writeDataIntoFile(headerBrief, valueFromDb, extensionHeader);
	}

	private String writeDataIntoFile(List<String> headerBrief, List<ComparedReporter> comparedReporterList, List<String> extensionHeader) {
		CompareReportTemplate reportTemplate = new CompareReportTemplate(headerBrief, reportHeader(extensionHeader), extensionHeader);
		
		reportTemplate.writeAsObject(comparedReporterList, REPORT_NAME);
		
		return reportTemplate.getTargetFilePath();
	}

	private List<String> reportHeader(List<String> extensionHeader) {
		headerList.addAll(extensionHeader);
		return headerList;
	}
}