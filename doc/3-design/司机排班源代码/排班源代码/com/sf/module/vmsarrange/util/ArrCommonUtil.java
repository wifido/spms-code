/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2010-10-19      谢年兵        创建
 **********************************************/
package com.sf.module.vmsarrange.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.sf.framework.core.Toolkit;
import com.sf.framework.server.core.context.ApplicationContext;
import com.sf.framework.server.core.context.ModuleContext;
import com.sf.framework.server.core.context.UserContext;
/**
 *
 * 公共工具类
 * @author 谢年兵  2010-10-19
 *
 */
public class ArrCommonUtil {
	
	/**
	 * 管理员的userId
	 */
	public final static Long ADMIN_ID = Long.valueOf(1);
	
	private static Log logger = LogFactory.getLog(ArrCommonUtil.class);
	private final static String path = "/com/sf/module/vmsarrange/META-INF/config/config.properties";
	
	/**报表保存根目录*/
	private static String reportSavePath = null;
	
	/**月结报表保存目录*/
	private static String monthReportSavePath = null;
	
	/**月结报表模板保存目录*/
	private static String monthReportTemplateSavePath = null;
	
	private static Map<String, String> moduleReportSavePaths = new HashMap<String, String>();
	
	private static Map<String, String> entityReportSavePaths = new HashMap<String, String>();
	
	public final static String FILE_NAME_SEPARATOR = "_";
	
	public static final String YYYY_MM_CN = "yyyy年MM月";

	public static final String YYYY_MM = "yyyy-MM";
	
	public static final String YYYY_MM_CNDD = "yyyy年MM月dd日";
	
	/**车辆属性  1:行政车辆 */
	public static final int VEHICLE_PROPERTY_XZ = 1;
	
	/**车辆属性  2:营运车辆 */
	public static final int VEHICLE_PROPERTY_YY = 2;
	
	/**车辆属性  3:员工自带车辆 */
	public static final int VEHICLE_PROPERTY_YGSP = 3;
	
	/**车辆属性  4:用者拥有车辆 */
	public static final int VEHICLE_PROPERTY_YZYY = 4;
	
	/** 车辆属性  5:收派车辆 */
	public static final int VEHICLE_PROPERTY_SP = 5;
	
	/**读取用户配置的月结报表模板目录*/
	static {
		Properties p = new Properties();
		try {
			p.load(ArrCommonUtil.class.getResourceAsStream(path));
			String templatePath = p.getProperty("monthReport.template.save.dir");
			if (templatePath != null && templatePath.length() > 0) {
				monthReportTemplateSavePath = new String(templatePath.getBytes("iso-8859-1"), "UTF-8");
			} else {
				monthReportTemplateSavePath = getReportSavePath() + File.separator + "月结报表模板";
			}
		} catch (Exception e) {
			logger.error("init monthReportTemplateSavePath failure", e);
		}
	}
	
	public static Date currentTm() {
		return new Date();
	}
	
	private final static String YYYY_MM_DD = "yyyy-MM-dd";
	private final static String YYYY_MM_DD_HH24 = "yyyy-MM-dd HH:mm:ss";
	private final static String YYYY_MM_DD_HH24_mm = "yyyy-MM-dd HH:mm";
	private final static String YYYY_MM_DD_ITALIC = "MM/dd/yyyy";
	
	/**
	 * 返回yyyy年MM月dd日的日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getYYYY_MM_CNDDFmt(){
		return new SimpleDateFormat(YYYY_MM_CNDD);
	}
	
	/**
	 * 返回yyyy年MM月的日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getYYYY_MM_CNFmt() {
		return new SimpleDateFormat(YYYY_MM_CN);
	}
	
	/**
	 * 返回yyyy-MM日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getYYYY_MMFmt() {
		return new SimpleDateFormat(YYYY_MM);
	}
	
	/**
	 * 返回yyyy-MM-dd日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getYYYY_MM_DDFmt() {
		return new SimpleDateFormat(YYYY_MM_DD);
	}
	
	/**
	 * 返回MM/dd/yyyy日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getDateFmtForITALIC() {
		return new SimpleDateFormat(YYYY_MM_DD_ITALIC);
	}
	
	/**
	 * 返回yyyy-MM-dd HH:mm:ss日期格式化对象
	 * @return
	 */
	public static SimpleDateFormat getYYYY_MM_DD_HH24Fmt() {
		return new SimpleDateFormat(YYYY_MM_DD_HH24);
	}
	
	/**
	 * 返回当前日期 不包含时间
	 * @return
	 */
	public static Date currentDt() {
		SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {
		}
		return now;
	}
	
	/**
	 * 返回当前年月日小时分钟秒
	 * @return
	 */
	public static Date currentTime() {
		SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD_HH24_mm);
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {}
		return now;
	}
	
	/**
	 * 获取报表文件的保存根目录
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getReportSavePath() throws Exception {
		if (reportSavePath == null) {
			Properties p = new Properties();
			try {
				p.load(ArrCommonUtil.class.getResourceAsStream(path));
				String path = p.getProperty("report.save.root.dir");
				if (path == null || path.length() < 1) {
					throw new Exception("cann't find report save path, you must config it first.");
				}
				File dir = new File(path);
				if (dir.exists()) {
					reportSavePath = path;
				} else {
					if (dir.mkdirs()) {
						reportSavePath = path;
						if (logger.isInfoEnabled()) {
							logger.info("create report save dir success! path=" + reportSavePath);
						}
					} else {
						logger.error("*********All month report save root dir create fail***********");
						throw new RuntimeException("*********All month report save root dir create fail***********");
					}
				}
			} catch (IOException e) {
				logger.error("read file config.properties failure", e);
				throw new Exception("read file config.properties failure", e);
			}
		}
		return reportSavePath;
	}
	
	
	/**
	 * 获取生成的附件文件保存路径(/sharefs/attach)
	 * @return -- 文件绝对路径
	 */
	public static String getAttachDocSaveDir() {
		String path = "";
		if (moduleReportSavePaths.containsKey("attach")) {
			path = moduleReportSavePaths.get("attach");
		}
	
		if( null!=path &&!"".equals(path.trim())){
			return path;
		}
			
		try {
			path = getReportSavePath() + File.separator + "attach";
		} catch (Exception e) {
			throw new RuntimeException(
					"create module report direction attach failure!");
		}
		
		// 存储位置(附件保存根目录)
		File dir = new File(path);
		if (dir.exists()) {
			moduleReportSavePaths.put("attach", path);
			
			return path ;
		} else if (dir.mkdirs()) {
			moduleReportSavePaths.put("attach", path);
			// 存储位置(附件保存根目录)
			return path;
		} else {
			throw new RuntimeException("create module report direction "
					+ path + "failure!");
		}
	}
	
	/**
	 * 获取月结报表文件的保存根目录
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getMonthReportSaveDir() throws Exception {
		if (monthReportSavePath == null) {
			monthReportSavePath = getReportSavePath() + File.separator + "月结报表";
		}
		return monthReportSavePath;
	}
	
	private static String getModuleReportSaveDir(String moduleName) throws Exception {
		if (moduleReportSavePaths.containsKey(moduleName)) {
			return moduleReportSavePaths.get(moduleName);
		} else {
			String path = getReportSavePath() + File.separator + "普通报表" + File.separator +  moduleName;
			File dir = new File(path);
			if (dir.exists()) {
				moduleReportSavePaths.put(moduleName, path);
			} else {
				if (dir.mkdirs()) {
					moduleReportSavePaths.put(moduleName, path);
				} else {
					throw new Exception("create module report direction: " + path + "failure!");
				}
			}
			return path;
		}
	}

	/**
	 * 获取普通报表保存路径
	 * @param entityClass
	 * @param moduleName
	 * @return
	 * @throws Exception
	 */
	public static String getGeneralReportSaveDir(String entityName, String moduleName) throws Exception {
		if (entityReportSavePaths.containsKey(entityName)) {
			return entityReportSavePaths.get(entityName);
		} else {
			String path = getModuleReportSaveDir(moduleName) + File.separator + entityName;
			File dir = new File(path);
			if (dir.exists()) {
				entityReportSavePaths.put(entityName, path);
			} else {
				if (dir.mkdirs()) {
					entityReportSavePaths.put(entityName, path);
				} else {
					throw new Exception("create entity report direction: " + path + "failure!");
				}
			}
			return path;
		}
	}
	
	/**
	 *  获取信息化报表保存路径
	 * @param entityName
	 * @param moduleName
	 * @return
	 * @throws Exception
	 */
	public static String getInformationReportSaveDir() throws Exception {
			String path = getReportSavePath() + File.separator + "checkInformation" ;
			File dir = new File(path);
			if (dir.exists()) {
				moduleReportSavePaths.put("checkInformation",path);
			} else {
				if (dir.mkdirs()) {
					moduleReportSavePaths.put("checkInformation", path);
				} else {
					throw new Exception("create module report direction: " + path + "failure!");
				}
			}
			return path;
	}
	
	/**
	 * 获取一个唯一的文件名字符串
	 * @return
	 */
	public static String getUniqueFileNameToken() {
		return Toolkit.getUuidRandomizer().generate() + UserContext.getContext().getCurrentUser().getEmployee().getCode();
	}
	
	/**
	 * 获取报表模板文件的路径  模板文件必须放到pages/template目录下
	 * @param fileName 模板文件名放到 pages/template目录下的模板的文件名
	 * @return
	 */
	public static String getReportTemplatePath(String fileName) {
		String webRoot = ApplicationContext.getContext().getServletContext().getRealPath("/");
		String moduleName = ModuleContext.getContext().getModule().getName();
		return webRoot + "pages" + File.separator + moduleName + File.separator + "template" + File.separator + fileName; 
	}
	
	/**
	  * 获取报表模板文件的路径  模板文件必须放到pages/template目录下
	 * @param fileName 模板文件名放到 pages/template目录下的模板的文件名
	 * @param moduleName 模块名称
	 * @return
	 */
	public static String getReportTemplatePath(String fileName, String moduleName) {
		String webRoot = ApplicationContext.getContext().getServletContext().getRealPath("/");
		return webRoot + "pages" + File.separator + moduleName + File.separator + "template" + File.separator + fileName; 
	}
	
	/**
	 * 获取月结报表模板文件的路径,如果用户配置了报表模板的存放目录,
	 * 并且在该目录下放置了对应的模板文件,则返回用户放置的模板文件,
	 * 否则返回默认的 模板文件
	 * @param templateFileName 模板文件名
	 * @return
	 */
	public static String getMonthReportTemplatePath(String templateFileName) {
		if (monthReportTemplateSavePath != null && monthReportTemplateSavePath.length() > 0) {
			String path = monthReportTemplateSavePath + File.separator + templateFileName;
			File file = new File(path);
			if (file.exists()) {
				return path;
			}
		}
		return getReportTemplatePath(templateFileName); 
	}
	
	/**
	  *求两个日期差
	  *@param beginDate  开始日期
	  *@param endDate   结束日期
	  *@return 两个日期相差天数
	  */
	public static long getDateMargin(Date beginDate,Date endDate){
	    long margin = 0;
	    margin = endDate.getTime() - beginDate.getTime();
	    margin = margin/(1000*60*60*24);
	    return margin;
	}
	
	/**
	  * 求两个日期相差的月份
	  *@param beginDate  开始日期
	  *@param endDate   结束日期
	  *@return 两个日期相差月份
	  */
	public static int getMonthMargin(Date beginDate,Date endDate){
	    Calendar start = Calendar.getInstance();
	    Calendar end = Calendar.getInstance();
	    
	    if (beginDate.before(endDate)) {
	    	start.setTime(beginDate);
	    	end.setTime(endDate);
	    } else {
	    	start.setTime(endDate);
	    	end.setTime(beginDate);
	    }
	    
	    int interval = 0;
	    do {
	    	if ((start.get(Calendar.YEAR) == end.get(Calendar.YEAR))
	    		&& (start.get(Calendar.MONTH) == end.get(Calendar.MONTH))) {
	    		break;
	    	}
	    	interval ++ ;
	    	start.add(Calendar.MONTH, 1);
	    } while(true);
	    return interval;
	}
	
	
	/**
	 * 净值计算
	 * @param buyDt 购置日期
	 * @param originalValue 车辆原值(元)
	 * @param residualRate 残值率 小数，如0.45
	 * @param reportDate 计算的截止日期
	 * @return
	 */
	public static Double calculateNetValue(Date buyDt, Double originalValue, Double residualRate, Date reportDate) {
		Double result = null;
		//总原则 净值=原值-累计折旧
		if (buyDt == null || originalValue == null) {
			return result;
		}
		
        //购置日期到生成报表的月份之间相差的月数大于48个月 净值=原值*残值率
		if (getMonthMargin(buyDt, reportDate) >= 48) {
			result = originalValue * residualRate;
		} else {
			//购置日期到生成报表的月份之间相差的月数小于48个月,并且购置日期大于2007-12-31日
			//净值=原值-原值*(1-残值率)*(报表月份与购置月份之间的间隔月数)/48
			if (buyDt.after(getDate2007End())) {
				result = originalValue-originalValue*(1-residualRate)*getMonthMargin(buyDt, reportDate)/48;
			} else {
				//购置日期到生成报表的月份之间相差的月数小于48个月,并且购置日期小于等于2007-12-31  需分两段来计算
		        //2008年以前的月份(不包括2008年1月)产生的折旧=原值*(1-残值率)*(2007年12月份与购置月份之间的间隔月数)/60
				Double before2008 = originalValue*(1-residualRate)*getMonthMargin(buyDt, getDate2007End())/60;
				
		        //2008年以后的月份(包括2008年1月)产生的折旧=(原值*(1-残值率)-2008年以前的月份(不包括2008年1月)产生的折旧)*报表月份与2008年1月之间的间隔月数/(48-2007年12月份与购置月份之间的间隔月数)
				Double after2008 = (originalValue*(1-residualRate)-before2008)*getMonthMargin(getDate2008Start(), reportDate)/(48 - getMonthMargin(buyDt, getDate2007End()));
		        //净值=原值-2008年以前的月份(不包括2008年1月)产生的折旧-2008年以后的月份(包括2008年1月)产生的折旧
				result = originalValue - before2008 - after2008;
			}
		}
		//四舍五入
		return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 转卖车辆的净值计算
	 * @param buyDt 购置日期
	 * @param originalValue 车辆原值(元)
	 * @param residualRate 残值率 小数，如0.45
	 * @param resellDate 转卖日期
	 * @return
	 */
	public static Double calculateNetValueForResell(Date buyDt, Double originalValue, Double residualRate, Date resellDate) {
		Double result = null;
		//总原则 净值=原值-累计折旧
		if (buyDt == null || originalValue == null || resellDate == null) {
			return result;
		}
		
		if (resellDate.compareTo(getDate2007End()) != 1) {
			int days = getMonthMargin(buyDt, resellDate);
			if (days >= 60) {
				result = originalValue * residualRate;
			} else {
				result = originalValue - originalValue * (1 - residualRate) * days / 60;
			}
		} else {
			if (getDate2007End().before(buyDt)) {
				int days = getMonthMargin(buyDt, resellDate);
				if (days >= 48) {
					result = originalValue * residualRate;
				} else {
					result = originalValue - originalValue * (1 - residualRate) / 48 * days;
				}
			} else {
				int days = getMonthMargin(buyDt, resellDate);
				if (days >= 48) {
					result = originalValue * residualRate;
				} else {
					int d1 = getMonthMargin(buyDt, getDate2007End());
					int d2 = getMonthMargin(resellDate, getDate2007End());
					result =originalValue - (originalValue * (1 - residualRate) / 60 * d1) - (originalValue*(1-residualRate)-originalValue*(1-residualRate)/60*d1) / (48 - d1) * d2;
				}
			}
		}
		
		//四舍五入
		return new BigDecimal(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private static Date date2007;
	
	/**
	 * 返回2007年12月31日
	 * @return
	 */
	private static Date getDate2007End() {
		if (date2007 == null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, 2007);
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			date2007 = c.getTime();
		}
		return date2007;
	}
	
	private static Date date2008;
	
	/**
	 * 返回2008年1月1日
	 * @return
	 */
	
	private static Date getDate2008Start() {
		if (date2008 == null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, 2008);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			date2008 = c.getTime();
		}
		return date2008;
	}
	
	/**业务异常参数识别*/
	public final static Pattern EXCEPTION_PARAMS_PARTTERN = Pattern.compile("(.*)\\{(.*)\\}");
	
	/**计算两个日期相差几年几月，如：0年5月，5年6月，1年0月*/
	public String forMateDate(String beginDate,String endDate){
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date sysDate;
		Date oldDate;
		
		String reStr = "";
		try {
			//格式化成Date类型
			sysDate = s.parse(beginDate);
			oldDate = s1.parse(endDate);
			
			
			//两个日期相差多少个月
			int num = ArrCommonUtil.getMonthMargin(sysDate, oldDate);
			
			if(num < 12){//如果相差的月数小于12个月
				reStr = "0年" + num + "月";				

			}else if(num == 12){//如果相差的月数等于12个月
				reStr = "1年0月";
			}else{//如果相差的月数大于12个月
				int a = num / 12;
				int b = num % 12;
				reStr = a + "年" + b + "月";				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return reStr;
	}
	
	/**
	  *求两个分钟差
	  *@param beginDate  开始日期
	  *@param endDate   结束日期
	  *@return 两个日期相差分钟
	  */
	public static long getDateMin(Date beginDate,Date endDate){
	    long margin = 0;
	    margin = endDate.getTime() - beginDate.getTime();
	    
	    //判断结束事件与开始时间是否同一时间
	    if(margin != 0){
	    	margin = margin/(1000*60);
		    //不足一分钟赋值成一分钟
		    if(margin == 0){
		    	margin = 1;
		    }
	    }
	    return margin;
	}
	
	/**
	 * 获取字符串插入数据库所占的长度,使用UTF-8编码
	 * @param str
	 * @return
	 */
	public static int getLengthForDB(String str) {
		return getLengthForDB(str, "UTF-8");
	}
	
	/**
	 * 获取字符串插入数据库所占的长度
	 * @param str 目标字符串
	 * @param charset 所使用的编码
	 * @return
	 */
	public static int getLengthForDB(String str, String charset) {
		if (str == null) {
			throw new IllegalArgumentException();
		}
		try {
			return str.getBytes(charset).length;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 复制对象属性
	 * @param dest 目标Bean
	 * @param src  实体Bean
	 */
	public static void copyProperties(Object dest,Object src){
		if (!dest.getClass().equals(src.getClass())) {
			throw new ClassCastException();
		}
		BeanWrapper srcWrapper = new BeanWrapperImpl(src);
		BeanWrapper destWrapper = new BeanWrapperImpl(dest);
		PropertyDescriptor pds[] = srcWrapper.getPropertyDescriptors();
		if (null != pds && 0 < pds.length) {
			for (PropertyDescriptor pd : pds) {
				String propName = pd.getName();
				// 读或写方法为空时不处理
				if (null == propName || null == pd.getWriteMethod()
						|| null == pd.getReadMethod()) {
					continue;
				}
				Object propValue = srcWrapper.getPropertyValue(propName);
				destWrapper.setPropertyValue(propName, propValue);
			}
		}
	}
	
	/**
	 * 复制参数值
	 * @param dest
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	public static void populate(Object dest,Map map){
		BeanWrapper destWrapper = new BeanWrapperImpl(dest);
		PropertyDescriptor pds[] = destWrapper.getPropertyDescriptors();
		if (null == pds || 0 == pds.length) {
			return;
		}

		for (PropertyDescriptor pd : pds) {
			String propertyName = pd.getName();
			// 为空或Map未包含该属性时不处理
			if (null == propertyName || !map.containsKey(propertyName)) {
				continue;
			}
			
			// 读或写方法为空时不处理
			if (null == pd.getWriteMethod() || null == pd.getReadMethod()) {
				continue;
			}
			
			// 获取参数值
			Object propertyValue = map.get(propertyName);
			if( null!=propertyValue && propertyValue.toString().trim().matches("^\\d{4}\\-\\d{2}\\-\\d{2}$")){
				try{//日期字段有值并且参数格式为“yyyy-MM-dd”
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
					java.util.Date tmpDt = sdf.parse(propertyValue.toString());
					pd.getWriteMethod().invoke(dest, tmpDt);
				}catch(Exception e){
					throw new java.lang.IllegalArgumentException(e);
				}
			}else if(!java.util.Date.class.equals(pd.getReadMethod().getReturnType()) ){
				destWrapper.setPropertyValue(propertyName, propertyValue);
			}
		}
	}
	/**获取异常统计月度报表存放路径*/
	public static String getExceptionReportPath(String month) {
		String saveDir = null;
		try {
			saveDir = getMonthReportSaveDir() + File.separator + EXCEPTION_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator + month + COMPILE_NAME;
		return savePath;
	}
	
	/**获取油耗目标值存放路径*/
	public static String getFuelTargetPath() {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("FuelTargetBiz", "vmsbase") + File.separator + FUEL_TARGET_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + FUEL_TARGET_NAME;
		return savePath;
	}
	/**获取油耗达标率、差异率存放路径 state:0月度1季度*/
	public static String getFuelMonthDetailPath(int state) {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("FuelStatisticsBiz", "vmsrpt") + File.separator + FUEL_MONTH_DETAIL_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + (state==0?FUEL_MONTH_DETAIL_NAME:FUEL_QUARTER_DETAIL_NAME);
		return savePath;
	}
	
	/**获取油耗达标率、差异率统计存放路径*/
	public static String getFuelPath(String year,String month) {
		String filePath = null;
		try {
			filePath = getMonthReportSaveDir()+File.separator+FUEL_MONTH_DETAIL_DIR_NAME+File.separator+year
			+(Integer.parseInt(month)<10?"0":"")+Integer.parseInt(month);
			File f = new File(filePath);
			if(!f.exists()){
				f.mkdirs();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	/**获取费考评类数据管理(异常分析)-附件存放路径*/
	public static String getAffixPath(String yearMonth){
		String saveDir = null;
		try {//月结报表-vmsreport-itemAnomaly-年
			saveDir = getMonthReportSaveDir() + File.separator + "vmsreport" + File.separator + 
						"itemAnomaly" + File.separator + yearMonth.substring(0,yearMonth.lastIndexOf("-"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		file.mkdirs();
		return saveDir;
	}
	/**获取维修流程监控导出文件存放路径*/
	public static String getMaintanceMonitorPath() {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("MaintanceMonitorBiz", "vmsoperation") + File.separator + MAINTANCE_MONITOR_DIR;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + MAINTANCE_MONITOR_NAME;
		return savePath;
	}
	/**获取车辆三检存放路径*/
	public static String getDrivingCheckPath() {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("DrivingCheckBiz", "vmsoperation") + File.separator + DRIVING_CHECK_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + DRIVING_CHECK_NAME;
		return savePath;
	}
	/**获取支线线路运价存放路径*/
	public static String getAreaBranchPath() {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("AreaBranchBiz", "vmsschedule") + File.separator + AREA_BRANCH_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + AREA_BRANCH_NAME;
		return savePath;
	}
	/**
	 * 获取驾驶员薪酬明细和汇总报表存放路径
	 * @param type 类型:1 明细 2汇总
	 * @return
	 */
	public static String getDriverPayPath(int type) {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("DriverPayDetailBiz", "vmsrpt") + File.separator + DRIVER_PAY_DIR;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = null;
		//明细路径
		if(type == 1){
			savePath = saveDir + File.separator  + DRIVER_PAY_DETAIL;
		}
		//汇总路径
		if(type == 2){
			savePath = saveDir + File.separator  + DRIVER_PAY_SUM;
		}
		return savePath;
	}
	
	/**
	 * 获取外协车辆资料管理导出文件的路径
	 * @return
	 */
	public static String OUTER_VEHICLE_DIR_NAME = "外协车辆数据";
	public static String OUTER_VEHICLE_NAME = "外协车辆资料数据.xls";
	public static String getOuterVehiclePath() {
		String saveDir = null;
		try {
			saveDir = getGeneralReportSaveDir("OuterVehicleBiz", "vmsbase") + File.separator + OUTER_VEHICLE_DIR_NAME;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(saveDir);
		if( !file.exists() ){
			file.mkdirs();
		}
		String savePath = saveDir + File.separator  + OUTER_VEHICLE_NAME;
		return savePath;
	}
	/**
	 * 根据userId判断是否管理员
	 * @param userId
	 * @return 如果是返回true
	 */
	public static boolean isAdmin(Long userId) {
		return ADMIN_ID.equals(userId);
	}	
	
	/**
	 * 校验字符串是否符合时间格式：yyyy-MM-dd HH:mm:ss (如：2014-01-03 16:31:25)
	 * @param text
	 * @return 如果符合返回true
	 */
	public static boolean isYYYY_MM_DD_HH24(String text) {
		if (null == text) {
			return false;
		}
		return text.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
	}
	
	/**行政月结异常统计报表文件夹及文件名字*/
	public static String EXCEPTION_DIR_NAME = "行政异常月度报表";
	public static String COMPILE_NAME = "月份车辆异常统计报表.xls";
	
	public static String FUEL_MONTH_DETAIL_DIR_NAME = "油耗达标率、差异率统计";
	public static String FUEL_MONTH_DETAIL_NAME = "油耗达标率、差异率统计(月度)明细表.xls";
	public static String FUEL_QUARTER_DETAIL_NAME = "油耗达标率、差异率统计(季度)明细表.xls";
	public static String FUEL_MONTH_NAME = "油耗达标率、差异率统计汇总表(月度).xls";
	public static String FUEL_QUARTER_NAME = "油耗达标率、差异率统计汇总表(季度).xls";
	public static String FUEL_TARGET_DIR_NAME = "车辆油耗目标值";
	public static String FUEL_TARGET_NAME = "营运车辆单车油耗目标值明细表.xls";
	public static String MAINTANCE_MONITOR_DIR = "维修流程监控";
	public static String MAINTANCE_MONITOR_NAME = "监控.xls";
	public static String DRIVING_CHECK_DIR_NAME = "车辆三检";
	public static String DRIVING_CHECK_NAME = "车辆三检报表.xls";
	public static String AREA_BRANCH_NAME = "支线线路价码表.xls";
	public static String AREA_BRANCH_DIR_NAME = "支线线路价码报表";
	public static String DRIVER_PAY_DIR = "驾驶员薪酬";
	public static String DRIVER_PAY_DETAIL = "驾驶员月度薪酬明细报表.xls";
	public static String DRIVER_PAY_SUM = "驾驶员月度薪酬汇总报表.xls";
	/** 行政月结数据截止日(25)*/
	public static int POLITICAL_END_DATE = 25;
	/** 行政月结数据生成日(8)*/
	public static int POLITICAL_CHECKOUT_DATE = 8;
}