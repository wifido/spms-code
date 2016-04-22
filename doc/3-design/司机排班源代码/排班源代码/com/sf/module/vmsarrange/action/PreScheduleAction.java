/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-27     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.vmsarrange.biz.IArrSingleSateBiz;
import com.sf.module.vmsarrange.biz.IPreScheduleBiz;
import com.sf.module.vmsarrange.biz.IPreScheduleDraftBiz;
import com.sf.module.vmsarrange.biz.IScheduleMonthRptBiz;
import com.sf.module.vmsarrange.dao.IArrSingleStateJdbcDao;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;
import com.sf.module.vmsarrange.dto.ParamsDto;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * PreScheduleAction处理类
 *
 */
public class PreScheduleAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	// Ajax响应标志
	private boolean success = true;  
	//Ajax请求响应提示消息
	private String retMsg; 
	private int start;
	private int limit;
	private long totalSize;
	private Integer dayNum;
	private List<PreSchedule> listSchedule;
	private IArrSingleSateBiz arrSingleSateBiz;
	private IArrSingleStateJdbcDao arrSingleStateJdbcDao;
	//上传、下载的文件名
	private String fileName; 
	//上传的文件流
	private File filePath; 
	// 下载的文件流
	private InputStream inputStream; 
	//年份
	private String scheduleYear;
	//月份
	private String scheduleMonth;
	// ext返回的集合
	private Collection<PreSchedule> root;
	//预排班实体类
	private PreSchedule param;
	//草稿实体类
	private PreScheduleDraft paramDraft;
	//
	//private Map  paramDraftMap;
	//预排班明细
	private Map  paramMap;
	//调班过程信息
	private Collection<Object>  paramList;
	
	//调班页面中的驾驶员信息
	private Collection<Object> optDrivers;
	//获取驾驶员等相关信息
	private List<PreSchedule> preSchedules; 
	//注入的biz
	private IPreScheduleBiz preScheduleBiz;
	
	private IPreScheduleDraftBiz preScheduleDraftBiz;
	private IScheduleMonthRptBiz scheduleMonthRptBiz;
	
	//配班的班次代码
	private String arrangeNo;
	//班次代码集合
	private Collection arrangeNos;
	//选中的网点ID
	private Long deptId;
	//驾驶员工号
	private String empCode;
	//调班页面的驾驶员工号
	private String optEmpCode;
	//调班页面的时间
	private String optDate;
	//驾驶员集合
	private Collection drivers;
	//调班页面中的参数传递对象
	private ParamsDto paramsDto;
	//文件上传
	private File uploadFile;
	//下载文件流
	private InputStream stream;
	//标题数据集合
	private Map htmlMap;
	//是否忽略连续6天排班:1.忽略 2.校验
	private Integer ignore;
	//班次类别
	private Integer arrangeType;
	
	public void setArrangeType(Integer arrangeType) {
		this.arrangeType = arrangeType;
	}

	public void setArrSingleStateJdbcDao(
			IArrSingleStateJdbcDao arrSingleStateJdbcDao) {
		this.arrSingleStateJdbcDao = arrSingleStateJdbcDao;
	}

	public void setArrSingleSateBiz(IArrSingleSateBiz arrSingleSateBiz) {
		this.arrSingleSateBiz = arrSingleSateBiz;
	}

	public void setIgnore(Integer ignore) {
		this.ignore = ignore;
	}

	public Map getHtmlMap() {
		return htmlMap;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setScheduleMonthRptBiz(IScheduleMonthRptBiz scheduleMonthRptBiz) {
		this.scheduleMonthRptBiz = scheduleMonthRptBiz;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public ParamsDto getParamsDto() {
		return paramsDto;
	}

	public void setParamsDto(ParamsDto paramsDto) {
		this.paramsDto = paramsDto;
	}

	public Collection<Object> getParamList() {
		return paramList;
	}

	public Collection<Object> getOptDrivers() {
		return optDrivers;
	}

	public void setOptEmpCode(String optEmpCode) {
		this.optEmpCode = optEmpCode;
	}

	public void setOptDate(String optDate) {
		this.optDate = optDate;
	}

	public String getOptEmpCode() {
		return optEmpCode;
	}

	public String getOptDate() {
		return optDate;
	}

	public Map getParamMap() {
		return paramMap;
	}

	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}

	public void setPreScheduleDraftBiz(IPreScheduleDraftBiz preScheduleDraftBiz) {
		this.preScheduleDraftBiz = preScheduleDraftBiz;
	}

	public void setPreSchedules(List<PreSchedule> preSchedules) {
		this.preSchedules = preSchedules;
	}

	public List<PreSchedule> getPreSchedules() {
		return preSchedules;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Collection getDrivers() {
		return drivers;
	}

	public void setArrangeNo(String arrangeNo) {
		this.arrangeNo = arrangeNo;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpCode() {
		return empCode;
	}

	public Collection getArrangeNos() {
		return arrangeNos;
	}

	public String getArrangeNo() {
		return arrangeNo;
	}

	public String getScheduleYear() {
		return scheduleYear;
	}

	public void setScheduleYear(String scheduleYear) {
		this.scheduleYear = scheduleYear;
	}

	public String getScheduleMonth() {
		return scheduleMonth;
	}

	public void setScheduleMonth(String scheduleMonth) {
		this.scheduleMonth = scheduleMonth;
	}

	public Collection<PreSchedule> getRoot() {
		return root;
	}

	public void setParam(PreSchedule param) {
		this.param = param;
	}

	public PreSchedule getParam() {
		return param;
	}

	public PreScheduleDraft getParamDraft() {
		return paramDraft;
	}

	public void setParamDraft(PreScheduleDraft paramDraft) {
		this.paramDraft = paramDraft;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public List<PreSchedule> getListSchedule() {
		return listSchedule;
	}

	public void setListSchedule(List<PreSchedule> listSchedule) {
		this.listSchedule = listSchedule;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public IPreScheduleBiz getPreScheduleBiz() {
		return preScheduleBiz;
	}

	public void setPreScheduleBiz(IPreScheduleBiz preScheduleBiz) {
		this.preScheduleBiz = preScheduleBiz;
	}
	
	/**
	 * 查询
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String search() {
		try {
			IPage page = this.preScheduleBiz.findByPage(param.getDepartment().getDeptCode(),
					param.getDriver().getEmpCode(), param.getClassType(),param.getYearMonth(), limit,
					start/limit);
			if(null != page){
				this.totalSize = page.getTotalSize();
				this.root = page.getData();
			}
			// 如果root没有数据，则赋值一个空对象，防止页面解析错误
			if (null == root) {
				root = (Collection) new HashMap();
			}
		} catch (Exception e) {
			log.error("",e);
		}
		return SUCCESS;
	}
	/**
	 * 根据网点ID，配班班次代码，查找出该用户权限网点下的所有班次，填充到下拉框中
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String scheduleArrangeCombox(){
		// Map map = (Map) this.preScheduleBiz.findArrangeNos(deptId,arrangeNo);
		// arrangeNos = (Collection) map.get("root");
		IPage page = preScheduleBiz.listArrangePage(deptId,arrangeType, arrangeNo, limit, start/limit);
		if(null != page){
			this.arrangeNos = page.getData();
			this.totalSize = page.getTotalSize();
		}
		return SUCCESS;
	}
	/**
	 * 工号下拉框取数据
	 * @return
	 */
	public String scheduleDriversCombox(){
		 IPage page = this.preScheduleDraftBiz.listPageBy(deptId, empCode, limit, start/limit);
		// Map map = (Map) this.preScheduleBiz.findDrivers(deptId,empCode);
		 //drivers = (Collection) map.get("root");
		 if(null != page){
			 this.drivers = page.getData();
			 this.totalSize = page.getTotalSize();
		 }
		return SUCCESS;
	}

	/**
	 * 检测是否有数据覆盖
	 * @return
	 */
	public String scheduleTestRepeat(){
		this.retMsg = null;
		String message = this.preScheduleBiz.testRepeat(preSchedules);
		if(!ArrFileUtil.isEmpty(message)){
			this.retMsg = message;
		}
		return SUCCESS;
	}
	
	/**
	 * 保存预排班草稿
	 * @return
	 */
	public String savePreSchedule() {
		try {
			this.preScheduleBiz.savePreSchedule(preSchedules);
		}catch (BizException e) {
			log.error("info:",e);
			this.retMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.retMsg = "班次已被其他用户修改，请重新提交保存";
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.savePreSchedule failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("UK_ARRSCHEDULE_DRIVER") != -1) {
				this.retMsg = "不能重复添加驾驶员排班，请核对数据(提示：重新提交保存可获取重复驾驶员信息)";
			}
			if (message != null && message.indexOf("UK_ARRSCHEDULEDRAFT_DRIVER") != -1) {
				this.retMsg = "不能重复添加驾驶员排班，请核对数据(提示：重新提交保存可获取重复驾驶员信息)";
			}
		}
		return SUCCESS;
	}
	
	
	/**
	 * 修改预排班草稿数据
	 */
	public String updateSchedule() {
		try {
			this.preScheduleBiz.updateSchedule(preSchedules);
		}catch (BizException e) {
			log.error("",e);
			this.retMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.retMsg = "班次已被其他用户修改，请重新提交保存";
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.updateSchedule failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
	
	/**
	 * 获取排班明细
	 * @return
	 */
	public String deltailSchedule(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			//取当月-精确到月
			Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
			//取次月-精确到月
			Date nextMonth = DateUtils.truncate(DateUtils.addMonths(current, 1),Calendar.MONTH);
			//取选中月份-精确到月
			Date chooseMonth = sdf.parse( param.getYearMonth());
			
			//次月份-拿预排班数据
			if(chooseMonth.compareTo(nextMonth) == 0){
				paramMap = this.preScheduleDraftBiz.findByCondition(param.getId(),param.getYearMonth(),dayNum);
			}
			//当前月之前的月份
			if(chooseMonth.before(current)){
				//报表那边去拿数据
				paramMap = this.scheduleMonthRptBiz.findByCondition(param.getId(),param.getYearMonth(),dayNum);
			}
			//当前月
			if(chooseMonth.compareTo(current) == 0){
				paramMap = this.preScheduleBiz.findByCondition(param.getId(),param.getYearMonth(),dayNum);;
			}
		}catch (BizException e) {
			log.error("info:",e);
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.updateSchedule failure:",e);
			this.retMsg = "获取明细失败";
		}
		return SUCCESS;
	}
	
	/**
	 * 获取调班过程信息
	 * @return
	 */
	public String optimizeInfo(){
		try {
			
		}catch (BizException e) {
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.updateSchedule failure:",e);
			this.retMsg = "获取明细失败";
		}
		return SUCCESS;
	}
	/**
	 * 调班页面获取驾驶员信息
	 */
	public String findOptDriver(){
		try {
			if(!ArrFileUtil.isEmpty(optDate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				//取当月-精确到月
				Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
				//取选中月份-精确到月
				Date chooseMonth = sdf.parse(optDate);
				//判断月份是否一样
				if(chooseMonth.compareTo(current) != 0){
					retMsg = "只能选择当月的日期来调班";
					return SUCCESS;
				}
			}
			optDrivers = this.preScheduleBiz.findOptDriver(optEmpCode,optDate,deptId);
		}catch (BizException e) {
			log.error("info:",e);
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.updateSchedule failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}		
		return SUCCESS;
	}
	
	/**
	 * 保存调班页面的调班信息
	 */
	public String saveOptInfo(){
		/**控制并发**/
		int count = 0;
		Long typeId = 3L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.retMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try {
			this.preScheduleBiz.saveOptInfo(paramsDto,ignore);
		}catch (BizException e) {
			log.error("info:",e);
			this.retMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.retMsg = "班次已被其他用户修改，请重新提交保存";
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.updateSchedule failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}finally{
			//恢复状态为空闲
			if(0 != count){
				try{
					//不做回滚
					arrSingleStateJdbcDao.updateEnd(typeId);
				}catch(Exception e){
					log.error("info: update tm_arr_single_state set state = 0 where type_id = 3 failure!",e);
					//异常不处理
				}
			}
		}		
		return SUCCESS;
	}
	
	/**
	 * 导出预排班信息
	 * @return
	 */
	public String listReport() {
		try {
			String deptCode = param.getDepartment().getDeptCode();
			String yearMonth = param.getYearMonth();
			String empCode = param.getDriver().getEmpCode();
			Integer classType = param.getClassType();
			this.fileName = preScheduleDraftBiz.listReport(deptCode, empCode, yearMonth,classType);
		}catch (BizException e) {
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
	/**
	 * 导出预排班
	 * @return
	 */
	public String listPreReport() {
		try {
			String deptCode = param.getDepartment().getDeptCode();
			String yearMonth = param.getYearMonth();
			//String empCode = param.getDriver().getEmpCode();
			//Integer classType = param.getClassType();
			this.fileName = preScheduleDraftBiz.listPreReport(deptCode, null, yearMonth,null);
		}catch (BizException e) {
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.savePreSchedule failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
	/**
	 * 导出实际排班
	 * @return
	 */
	public String listRealReport() {
		try {
			String deptCode = param.getDepartment().getDeptCode();
			String yearMonth = param.getYearMonth();
			String empCode = param.getDriver().getEmpCode();
			Integer classType = param.getClassType();
			this.fileName = preScheduleDraftBiz.listRealReport(deptCode, empCode, yearMonth,classType);
		}catch (BizException e) {
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.listRealReport failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
	
	/**
	 * 导入
	 * @return
	 */
	public String saveFile(){
		try{
			preScheduleDraftBiz.saveFile(uploadFile, fileName);
		}catch(BizException e){
			log.error("",e);
			this.retMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.retMsg = "班次已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("UK_ARRSCHEDULE_DRIVER") != -1) {
				this.retMsg = "不能重复添加驾驶员排班，请核对数据(提示：重新提交保存可获取重复驾驶员信息)";
			}
			if (message != null && message.indexOf("UK_ARRSCHEDULEDRAFT_DRIVER") != -1) {
				this.retMsg = "不能重复添加驾驶员排班，请核对数据(提示：重新提交保存可获取重复驾驶员信息)";
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 下载报表文件
	 * @return
	 */
	public String downloadFile() {
		File file = null;
		try {
			this.fileName = URLDecoder.decode(this.fileName,"utf-8");
			file = new File(this.fileName);
			this.stream = new FileInputStream(file);
			fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
			this.fileName = new String(fileName.getBytes("GBK"),"ISO-8859-1");
			return SUCCESS;
		} catch (Exception e) {
			log.error("download excel failure",e);
			return ERROR;
		}finally{
			if(null != file && file.exists()){
				file.delete();
			}
		}
	}
	
	/**
	 * 获取新增页面的标题的区部、分部
	 * @return
	 */
	public String getAreaDeptName(){
		try {
			htmlMap = this.preScheduleBiz.findAreaDeptName(deptId);
		}catch (BizException e) {
			this.retMsg = e.getMessageKey();
		}catch( Exception e){
			log.error("com.sf.module.vmsbase.action.PreScheduleAction.getAreaDeptName failure:",e);
			this.retMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}		
		return SUCCESS;
	}
}
