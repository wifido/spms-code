/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-9     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.vmsarrange.biz.IArrSingleSateBiz;
import com.sf.module.vmsarrange.biz.IScheduleArrangeBiz;
import com.sf.module.vmsarrange.dao.IArrSingleStateJdbcDao;
import com.sf.module.vmsarrange.domain.ScheduleArrange;

/**
 *
 * 配班管理Action处理类
 *
 */
public class ScheduleArrangeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private IArrSingleStateJdbcDao arrSingleStateJdbcDao;
	private IScheduleArrangeBiz scheduleArrangeBiz;
	private IArrSingleSateBiz arrSingleSateBiz;
	private boolean success = true;  // Ajax响应标志
	private String errorMsg; //Ajax请求响应提示消息
	private int start;
	private int limit;
	private long total;
	private Collection<ScheduleArrange> page;
	private Long deptId;
	private String arrangeNo;
	private Integer valid;
	private ScheduleArrange entity;
	private File uploadFile;
	private String fileName;
	private InputStream stream;
	private int arrangeType;
	private String deptCode;
	private Integer isUsed;
	private Long recordId;
	private Long[] recordIds;
	private Long oldArrangId;
	private Integer isNew;
	
	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	public void setOldArrangId(Long oldArrangId) {
		this.oldArrangId = oldArrangId;
	}
	public void setRecordIds(Long[] recordIds) {
		this.recordIds = recordIds;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public void setArrSingleSateBiz(IArrSingleSateBiz arrSingleSateBiz) {
		this.arrSingleSateBiz = arrSingleSateBiz;
	}
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}
	public String getArrangeNo() {
		return arrangeNo;
	}
	public void setArrangeType(int arrangeType) {
		this.arrangeType = arrangeType;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public ScheduleArrange getEntity() {
		return entity;
	}
	public void setEntity(ScheduleArrange entity) {
		this.entity = entity;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public long getTotal() {
		return total;
	}
	public Collection<ScheduleArrange> getPage() {
		return page;
	}
	public InputStream getStream() {
		return stream;
	}
	public void setArrSingleStateJdbcDao(
			IArrSingleStateJdbcDao arrSingleStateJdbcDao) {
		this.arrSingleStateJdbcDao = arrSingleStateJdbcDao;
	}
	public void setScheduleArrangeBiz(IScheduleArrangeBiz scheduleArrangeBiz) {
		this.scheduleArrangeBiz = scheduleArrangeBiz;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public void setArrangeNo(String arrangeNo) {
		this.arrangeNo = arrangeNo;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	/**
	 * 查找分页数据
	 * @return
	 */
	public String listPage(){
		if(limit > 0){
			IPage<ScheduleArrange> data = scheduleArrangeBiz.listPage(deptId, arrangeNo, valid,isUsed, limit, start/limit);
			if(null != data){
				this.page = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
	/**
	 * 校验是否超过16个小时
	 * @param entity
	 * @return
	 */
	public String listCheckEntity(){
		this.errorMsg = this.scheduleArrangeBiz.listCheckEntity(entity);
		return SUCCESS;
	}
	/**
	 * 新增配班信息
	 * @return
	 */
	public String saveEntity(){
		/**执行业务**/
		try{
			scheduleArrangeBiz.saveEntity(entity);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			log.error("",e);
			this.errorMsg = "线路已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.1","存在选择的线路已经被删除(请刷新线路信息,重新选择)");
			}
			if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.2","存在选择的线路已经被其他配班使用(请刷新线路信息,重新选择)");
			}
			if (message != null && message.indexOf("UK_ARR_ARRANGE_NO") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.3","生成的班次代码被其他配班优先使用(请点击重新获取班次代码)");
			}
		}
		return SUCCESS;
	}
	/**
	 * 新增配班信息-机动班
	 * @return
	 */
	public String saveEntityArr(){
		/**控制并发**/
		int count = 0;
		Long typeId = 2L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.errorMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try{
			scheduleArrangeBiz.saveEntity(entity);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			try{
				String message = e.getMessage();
				if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
					this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.1","存在选择的线路已经被删除(请刷新线路信息,重新选择)");
				}
				if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
					this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.2","存在选择的线路已经被其他配班使用(请刷新线路信息,重新选择)");
				}
				if (message != null && message.indexOf("UK_ARR_ARRANGE_NO") != -1) {
					this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.3","生成的班次代码被其他配班优先使用(请点击重新获取班次代码)");
				}
			}catch(Exception ee){
				log.error("info:",ee);
				//转换提示信息异常不处理
			}
		}finally{
			//恢复状态为空闲
			if(0 != count){
				try{
					//不做回滚
					arrSingleStateJdbcDao.updateEnd(typeId);
				}catch(Exception e){
					log.error("info: update tm_arr_single_state set state = 0 where type_id = 1 failure!",e);
					//异常不处理
				}
			}
		}
		return SUCCESS;
	}
	/**
	 * 修改班次信息
	 * @return
	 */
	public String updateEntity(){
		/**执行业务**/
		try{
			//修改记录-班次代码有变化则认为是新增班次
			scheduleArrangeBiz.updateEntity(entity);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			log.error("",e);
			this.errorMsg = "线路或班次已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.1","存在选择的线路已经被删除(请刷新线路信息,重新选择)");
			}
			if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.2","存在选择的线路已经被其他配班使用(请刷新线路信息,重新选择)");
			}
		}
		return SUCCESS;
	
	}
	/**
	 * 修改班次信息-机动班
	 * @return
	 */
	public String updateEntityArr(){
		/**控制并发**/
		int count = 0;
		Long typeId = 2L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.errorMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try{
			scheduleArrangeBiz.updateEntity(entity);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			log.error("",e);
			this.errorMsg = "记录已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			try{
				String message = e.getMessage();
				if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
					this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.1","存在选择的线路已经被删除(请刷新线路信息,重新选择)");
				}
				if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
					this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.2","存在选择的线路已经被其他配班使用(请刷新线路信息,重新选择)");
				}
			}catch(Exception ee){
				log.error("info:",ee);
				//转换提示信息异常不处理
			}
		}finally{
			//恢复状态为空闲
			if(0 != count){
				try{
					//不做回滚
					arrSingleStateJdbcDao.updateEnd(typeId);
				}catch(Exception e){
					log.error("info: update tm_arr_single_state set state = 0 where type_id = 1 failure!",e);
					//异常不处理
				}
			}
		}
		return SUCCESS;
	
	}
	/**
	 * 修改是否有效
	 * @return
	 */
	public String updateValid(){
		try{
			this.scheduleArrangeBiz.updateInvalid(recordIds,valid);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			log.error("",e);
			this.errorMsg = "记录已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.1","存在选择的线路已经被删除(请刷新线路信息,重新选择)");
			}
			if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.2","存在选择的线路已经被其他配班使用(请刷新线路信息,重新选择)");
			}
		}
		return SUCCESS;
	}
	/**
	 * 导入
	 * @return
	 */
	public String saveFile(){
		/**执行业务**/
		try{
			scheduleArrangeBiz.saveFile(uploadFile, fileName);
		}catch(BizException e){
			log.error("info:",e);
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			log.error("",e);
			this.errorMsg = "线路已被其他用户修改，请重新提交保存";
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.3",
						"存在选择的班次已经被删除,请核对后再提交(提示:重新点击导入可获取最新被删除的班次所在行)");
			}
			if (message != null && message.indexOf("UK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.4",
						"存在选择的班次已经被其他配班使用,请核对后再提交(提示:重新点击导入可获取最新被使用的班次所在行)");
			}
			if (message != null && message.indexOf("UK_ARR_ARRANGE_NO") != -1) {
				this.errorMsg = I18nEl.i18n_def("ScheduleArrangeAction.action.5",
						"存在班次代码与系统已有班次代码重复，请核对后再提交(提示:重新点击导入可获取最新被占用的班次代码所在行)");
			}
		}
		return SUCCESS;
	
	}
	/**
	 * 导出
	 * @return
	 */
	public String listReport(){
		try{
			this.fileName = scheduleArrangeBiz.listReport(deptId, arrangeNo, valid,isUsed);
		}catch(BizException e){
			this.errorMsg = e.getMessage();
		}
		return SUCCESS;
	}
	/**
	 * 下载报表
	 * @return
	 */
	public String downloadFile() {
		File file = null;
		try {
			this.fileName = URLDecoder.decode(this.fileName,"UTF-8");
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
	 * 获取班次代码
	 * @return
	 */
	public String listArrangeNo(){
		try {
			this.arrangeNo = scheduleArrangeBiz.saveArrangeNo(deptCode, arrangeType);
		} catch (Exception e) {
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
}
