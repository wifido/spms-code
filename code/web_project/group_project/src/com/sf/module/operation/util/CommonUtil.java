/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2010-10-19      谢年兵        创建
 **********************************************/
package com.sf.module.operation.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
 * 
 * @author 谢年兵 2010-10-19
 * 
 */
public class CommonUtil {
	private static Log logger = LogFactory.getLog(CommonUtil.class);
	private final static String YYYY_MM_DD = "yyyy-MM-dd";
	private final static String YYYY_MM_DD_HH24 = "yyyy-MM-dd HH:mm:ss";
	private final static String YYYY_MM_DD_HH24_mm = "yyyy-MM-dd HH:mm";
	private final static String YYYY_MM_DD_ITALIC = "MM/dd/yyyy";
	public final static String FILE_NAME_SEPARATOR = "_";
	public static final String YYYY_MM_CN = "yyyy年MM月";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String YYYY_MM_CNDD = "yyyy年MM月dd日";
	private final static String path = "/com/sf/module/common/META-INF/config/common.properties";
	private final static String esbPath = "/com/sf/module/ossinterface/META-INF/config/ESBInfo.properties";
	private static String esbResendIp = null;
	private static String esbResendPort = null;
	
	public static String yyyyMMddHHmm;

	public synchronized static String getespResendIp() throws Exception {
		if (esbResendIp == null) {
			Properties p = new Properties();
			try {
				p.load(CommonUtil.class.getResourceAsStream(esbPath));
				String path = p.getProperty("esbResendIp");
				if (path == null || path.length() < 1) {
					throw new Exception(
							"cann't find espResendIp, you must config it first.");
				}
				esbResendIp = path;

			} catch (IOException e) {
				logger.error("read file config.properties failure", e);
				throw new Exception("read file config.properties failure", e);
			}
		}
		return esbResendIp;

	}

	public synchronized static String getesbResendPort() throws Exception {
		if (esbResendPort == null) {
			Properties p = new Properties();
			try {
				p.load(CommonUtil.class.getResourceAsStream(esbPath));
				String path = p.getProperty("esbResendPort");
				if (path == null || path.length() < 1) {
					throw new Exception(
							"cann't find esbResendPort, you must config it first.");
				}
				esbResendPort = path;

			} catch (IOException e) {
				logger.error("read file config.properties failure", e);
				throw new Exception("read file config.properties failure", e);
			}
		}
		return esbResendPort;
	}

	public static void copyProperties(Object dest, Object src) {
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

	public static void copyDiffObjProperties(Object dest, Object src) {
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
	 * 返回日期 不包含时间
	 * 
	 * @return
	 */
	public static Date getYmd(String dt) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sf.parse(dt);
		} catch (ParseException e) {
		}
		return null;
	}

	public static String getYmdStr(Date dt) {
		if (dt != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			return sf.format(dt);
		}
		return "";
	}

	public final static Long ADMIN_ID = Long.valueOf(1);

	public static boolean isAdmin(Long userId) {
		return ADMIN_ID.equals(userId);
	}

	/**
	 * 返回当前日期 不包含时间
	 * 
	 * @return
	 */
	public static Date currentDt() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {
		}
		return now;
	}

	/**
	 * 返回当前年月日小时分钟秒
	 * 
	 * @return
	 */
	public static Date currentTime() {
		SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD_HH24_mm);
		Date now = new Date();
		try {
			now = sf.parse(sf.format(now));
		} catch (ParseException e) {
		}
		return now;
	}

	/**
	 * 日期转换成星期
	 * 
	 * @author 郑萍玲 (330356)
	 * @date 2012-8-13
	 * @param tempDate
	 * @return
	 */
	public static String showWeek(String tempDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(tempDate);
		} catch (ParseException e) {
			logger.error(e);
		}
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int mydate = cd.get(Calendar.DAY_OF_WEEK); // 获取指定日期转换成星期几
		String showDate = "";
		switch (mydate) {
		case 1:
			showDate = "7";
			break;
		case 2:
			showDate = "1";
			break;
		case 3:
			showDate = "2";
			break;
		case 4:
			showDate = "3";
			break;
		case 5:
			showDate = "4";
			break;
		case 6:
			showDate = "5";
			break;
		default:
			showDate = "6";
			break;
		}
		return showDate;
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 根据日期获取到运力班期
	 * 
	 * @author WUQING 2012-8-30
	 */
	public static Integer getScheduledDays(Date flightDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(flightDate);
		int _day = cal.get(Calendar.DAY_OF_WEEK);
		Integer _decodeDay = 0;
		if ((_day - 1) == 0) {
			_decodeDay = 7;
		} else {
			_decodeDay = _day - 1;
		}
		return _decodeDay;
	}

	/**
	 * 对时间做补零操作
	 * 
	 * @param preDepart
	 * @return
	 * @author WUQING
	 * @Date 2012-8-23
	 */
	public static String transitionPreDepart(Integer preDepart) {
		if (null != preDepart) {
			int num = preDepart.intValue();
			String s = num < 0 ? num * -1 + "" : num + "";
			switch (s.length()) {
			case 1:
				s = "000" + s;
				break;
			case 2:
				s = "00" + s;
				break;
			case 3:
				s = "0" + s;
				break;
			}
			return s;
		} else {
			return null;
		}
	}

	public static String addZero(Integer v) {
		String s = "";
		if (null != v) {
			if (v >= 1 && v <= 9) {
				s = "0" + v;
			} else {
				s = v.toString();
			}
		}
		return s;
	}

	/**
	 * 对合同编号中流水号做补零操作
	 * 
	 * @param preDepart
	 * @return
	 * @author WUQING
	 * @Date 2012-10-8
	 */
	public static String transitionDeptCode(Integer preDepart) {
		if (null != preDepart) {
			int num = preDepart.intValue();
			String s = num < 0 ? num * -1 + "" : num + "";
			switch (s.length()) {
			case 1:
				s = "00" + s;
				break;
			case 2:
				s = "0" + s;
				break;
			}
			return s;
		} else {
			return null;
		}
	}

	/**
	 * 返回用户选择的起飞时间与运力资源中补零后的数据
	 * 
	 * @author WUQING
	 * @Date 2012-8-23
	 */
	public static Date transitionValue(Date flightDate, Integer preDepart) {
		String _preDepart = transitionPreDepart(preDepart);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HHmm");
		Date _comDate = null;
		try {
			_comDate = sf2.parse(sf.format(flightDate) + " " + _preDepart);
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error(e);
		}
		return _comDate;
	}

	/**
	 * 返回用户选择的起飞时间与运力资源中补零后的数据
	 * 
	 * @author WUQING
	 * @Date 2012-8-23
	 */
	public static Date transitionValue(Date flightDate, String preDepart) {
		int m = 0;
		int h = 0;
		if (preDepart != null && !"".equals(preDepart)) {
			if (preDepart.length() >= 3) {
				m = Integer
						.parseInt(preDepart.substring(preDepart.length() - 2));
				h = Integer.parseInt(preDepart.substring(0,
						preDepart.length() - 2));
			} else {
				m = Integer.parseInt(preDepart.substring(0));
			}
		}

		Calendar instance = Calendar.getInstance();
		instance.setTime(flightDate);
		instance.set(Calendar.HOUR_OF_DAY, h);
		instance.set(Calendar.MINUTE, m);
		instance.clear(Calendar.SECOND);
		instance.clear(Calendar.MILLISECOND);
		return instance.getTime();
	}

	/**
	 * 获取日期加跨越天数后的日期
	 * 
	 * @param date
	 * @param days
	 * @author 郑萍玲
	 * @return
	 * @throws ParseException
	 */
	public static Date showDate(Date date, Double days) {
		Date d = null;
		try {
			long tempDay = Math.round(days); // 四舍五入取整数
			d = addDate(date, tempDay);
			// System.out.println("增加天数以后的日期：" + format.format(d));
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error(e);
		}
		return d;
	}

	public static Date addDate(Date d, long day) throws ParseException {
		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return new Date(time);
	}

	public static String valueOf(Integer str) {
		String value = " ";
		if (str != null) {
			value = String.valueOf(str);
		}
		return value;
	}

	public static Date strToDate(String strDate) {
		if ("".equals(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date newDate = null;
		try {
			newDate = sdf.parse(strDate);
		} catch (ParseException e) {
			logger.error(e);
		}
		return newDate;
	}

	public static Date getYMD(Date dt) {
		if (dt != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			return getYmd(sf.format(dt));
		}
		return null;
	}

	public static String getYm(Date dt) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		try {
			return sf.format(dt);
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}

	public static String getDayNum(Date dt) {
		SimpleDateFormat sf = new SimpleDateFormat("dd");
		try {
			return String.valueOf(Integer.parseInt(sf.format(dt)));
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}

	public static int getDayNumber(Date dt) {
		SimpleDateFormat sf = new SimpleDateFormat("dd");
		try {
			return Integer.parseInt(sf.format(dt));
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	public static String getNextMonthYm(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.MONTH, 1);
		return sdf.format(calendar.getTime());
	}

	public static int getLastDayOfMonth(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Date getLastDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		return getYMD(cal.getTime());
	}

	public static Date getPreDay(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}

	public static Date getNextDay(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}

	public static Date getDayAfterNum(Date dt, int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DAY_OF_MONTH, n);
		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}

	// 判断数组中是否包含
	public static <T> boolean arrayIsContains(final T[] array, final T v) {
		for (final T e : array)
			if (e == v || v != null && v.equals(e))
				return true;

		return false;
	}

	// 比较时间大小
	public static boolean dt1LessThanDt2(Date dt1, Date dt2) {
		int result = dt1.compareTo(dt2);
		if (result == 0)
			return true;
		else if (result < 0)
			return true;
		else
			return false;
	}

	public static int compareTowDate(Date dt1, Date dt2) {
		return dt1.compareTo(dt2);
	}

	// util.Date 转 sql.Date
	public static java.sql.Date converDate(Date value) {
		if (value == null) {
			return null;
		}
		return new java.sql.Date(value.getTime());
	}

	/**
	 * 获取报表模板文件的路径 模板文件必须放到pages/template目录下
	 * 
	 * @param fileName
	 *            模板文件名放到 pages/template目录下的模板的文件名
	 * @return
	 */
	public static String getReportTemplatePath(String fileName) {
		String webRoot = ApplicationContext.getContext().getServletContext()
				.getRealPath("/");
		String moduleName = ModuleContext.getContext().getModule().getName();
		return webRoot + "pages" + File.separator + moduleName + File.separator
				+ "template" + File.separator + fileName;
	}

	/**
	 * 获取普通报表保存路径
	 * 
	 * @param entityClass
	 * @param moduleName
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String> entityReportSavePaths = new HashMap<String, String>();

	public static String getGeneralReportSaveDir(String entityName,
			String moduleName) throws Exception {
		if (entityReportSavePaths.containsKey(entityName)) {
			return entityReportSavePaths.get(entityName);
		} else {
			String path = getModuleReportSaveDir(moduleName) + File.separator
					+ entityName;
			File dir = new File(path);
			if (dir.exists()) {
				entityReportSavePaths.put(entityName, path);
			} else {
				if (dir.mkdirs()) {
					entityReportSavePaths.put(entityName, path);
				} else {
					throw new Exception("create entity report direction: "
							+ path + "failure!");
				}
			}
			return path;
		}
	}

	// 获取压缩文件后的路径
	public static String getCompresAfterSaveDirPath(String entityName,
			String moduleName) throws Exception {
		String userDay = UserContext.getContext().getCurrentUser().getUsername() + "_" + yyyyMMddHHmm;
		if (entityReportSavePaths.containsKey(userDay)) {
			return entityReportSavePaths.get(userDay);
		} else {
			String path = getModuleReportSaveDir(moduleName) + File.separator
					+ entityName + File.separator + userDay;
			File dir = new File(path);
			if (dir.exists()) {
				entityReportSavePaths.put(userDay, path);
			} else {
				if (dir.mkdirs()) {
					entityReportSavePaths.put(userDay, path);
				} else {
					throw new Exception("create entity report direction: "
							+ path + "failure!");
				}
			}
			return path;
		}
	}

	private static Map<String, String> moduleReportSavePaths = new HashMap<String, String>();

	private static String getModuleReportSaveDir(String moduleName)
			throws Exception {
		if (moduleReportSavePaths.containsKey(moduleName)) {
			return moduleReportSavePaths.get(moduleName);
		} else {
			String path = getReportSavePath() + File.separator + "spms-code"
					+ File.separator + moduleName;
			File dir = new File(path);
			if (dir.exists()) {
				moduleReportSavePaths.put(moduleName, path);
			} else {
				if (dir.mkdirs()) {
					moduleReportSavePaths.put(moduleName, path);
				} else {
					throw new Exception("create module report direction: "
							+ path + "failure!");
				}
			}
			return path;
		}
	}

	/**
	 * 保存Oracle Hr接口文件
	 * 
	 * @return
	 */
	private static String hrXmlSavePath = null;

	public synchronized static String getHrXmlSavePath() throws Exception {
		if (hrXmlSavePath == null) {
			Properties p = new Properties();
			try {
				p.load(CommonUtil.class.getResourceAsStream(path));
				String path = p.getProperty("hrXmlSavePath");
				if (path == null || path.length() < 1) {
					throw new Exception(
							"cann't find hrXmlSavePath, you must config it first.");
				}
				File dir = new File(path);
				if (dir.exists()) {
					hrXmlSavePath = path;
				} else {
					if (dir.mkdirs()) {
						hrXmlSavePath = path;
						if (logger.isInfoEnabled()) {
							logger.info("create hrXmlSavePathdir success! path="
									+ hrXmlSavePath);
						}
					} else {
						logger.error("*********All hrXmlSavePath dir create fail***********");
						throw new RuntimeException(
								"*********All hrXmlSavePath dir create fail***********");
					}
				}
			} catch (IOException e) {
				logger.error("read file config.properties failure", e);
				throw new Exception("read file config.properties failure", e);
			}
		}
		return hrXmlSavePath;

	}

	/**
	 * 获取报表文件的保存根目录
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String reportSavePath = null;

	public synchronized static String getReportSavePath() throws Exception {
		if (reportSavePath == null) {
			Properties p = new Properties();
			try {
				p.load(CommonUtil.class.getResourceAsStream(path));
				String path = p.getProperty("exportSavePath");
				if (path == null || path.length() < 1) {
					throw new Exception(
							"cann't find report save path, you must config it first.");
				}
				File dir = new File(path);
				if (dir.exists()) {
					reportSavePath = path;
				} else {
					if (dir.mkdirs()) {
						reportSavePath = path;
						if (logger.isInfoEnabled()) {
							logger.info("create report save dir success! path="
									+ reportSavePath);
						}
					} else {
						logger.error("*********All month report save root dir create fail***********");
						throw new RuntimeException(
								"*********All month report save root dir create fail***********");
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
	 * 获取文件保存的完整路径
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String uniquePart;
	private static String moduleName;
	private static String entityName;
	private static String downloadFileName;

	public static String getSaveFilePath(Class<?> entityClass) throws Exception {
		moduleName = ModuleContext.getContext().getModule().getName();
		entityName = entityClass.getSimpleName();
		uniquePart = CommonUtil.getUniqueFileNameToken();
		if (uniquePart == null) {
			throw new RuntimeException(
					"downloadFileName is null, you must call setDownloadFileName(name) method set the downloadFileName!");
		}
		return getGeneralReportSaveDir(entityName, moduleName) + File.separator
				+ uniquePart + FILE_NAME_SEPARATOR + downloadFileName;
	}
	
	public static String getSaveCompresFilePath(Class<?> entityClass) throws Exception {
		moduleName = ModuleContext.getContext().getModule().getName();
		entityName = entityClass.getSimpleName();
		uniquePart = CommonUtil.getUniqueFileNameToken();
		if (uniquePart == null) {
			throw new RuntimeException(
					"downloadFileName is null, you must call setDownloadFileName(name) method set the downloadCompresFileName!");
		}
		return getCompresAfterSaveDirPath(entityName, moduleName) + File.separator + downloadFileName;
	}

	/**
	 * 获取一个唯一的文件名字符串
	 * 
	 * @return
	 */
	public static String getUniqueFileNameToken() {
		return Toolkit.getUuidRandomizer().generate()
				+ UserContext.getContext().getCurrentUser().getEmployee()
						.getCode();
	}

	/**
	 * 设置文件下载对话框中显示的文件名 英文、中文都可以
	 * 
	 * @param downloadFileName
	 */
	public static void setDownloadFileName(String downLoadFileName) {
		downloadFileName = downLoadFileName;
	}

	/**
	 * 获取返回至页面的文件字符串
	 * 
	 * @return
	 */
	public static String getReturnPageFileName() {
		return "uniquePart=" + uniquePart + "&fileName=" + downloadFileName
				+ "&moduleName=" + moduleName + "&entityName=" + entityName;
	}

	public static String getLastMonthYm(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.MONTH, -1);
		return sdf.format(calendar.getTime());
	}
}