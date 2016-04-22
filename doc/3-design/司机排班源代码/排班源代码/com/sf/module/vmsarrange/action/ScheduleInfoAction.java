/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-03     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.vmsarrange.biz.IArrSingleSateBiz;
import com.sf.module.vmsarrange.biz.IScheduleArrangeBiz;
import com.sf.module.vmsarrange.biz.IScheduleInfoBiz;
import com.sf.module.vmsarrange.dao.IArrSingleStateJdbcDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.domain.ScheduleInfo;

/**
 *
 * 班次信息Action处理类
 *
 */
public class ScheduleInfoAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private IArrSingleStateJdbcDao arrSingleStateJdbcDao;
	private IScheduleInfoBiz scheduleInfoBiz;
	private IScheduleArrangeBiz scheduleArrangeBiz;
	private IArrSingleSateBiz arrSingleSateBiz;
	private boolean success = true;  // Ajax响应标志
	private String errorMsg; //Ajax请求响应提示消息
	private int start;
	private int limit;
	private long total;
	private Collection<ScheduleInfo> page;
	private Long deptId;
	private Integer dataSource;
	private String vehicleCode;
	private Integer valid;
	private ScheduleInfo entity;
	private Collection<ArrVehicle> vehiclePage;
	@SuppressWarnings("rawtypes")
	private List<Map> brand;
	private String modelBase;
	private Collection<ArrDepartment> deptPage;
	private String deptCode;
	private List<Long> ids;//批量删除id
	private File uploadFile;
	private String fileName;
	private InputStream stream;
	private String startTm;
	private Long recordId;//配班中修改记录的id
	private Integer isUsed;
	
	public void setArrSingleSateBiz(IArrSingleSateBiz arrSingleSateBiz) {
		this.arrSingleSateBiz = arrSingleSateBiz;
	}
	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}
	public void setScheduleArrangeBiz(IScheduleArrangeBiz scheduleArrangeBiz) {
		this.scheduleArrangeBiz = scheduleArrangeBiz;
	}
	public InputStream getStream() {
		return stream;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Collection<ArrDepartment> getDeptPage() {
		return deptPage;
	}
	public List<Map> getBrand() {
		return brand;
	}
	public void setModelBase(String modelBase) {
		this.modelBase = modelBase;
	}
	public Collection<ArrVehicle> getVehiclePage() {
		return vehiclePage;
	}
	public ScheduleInfo getEntity() {
		return entity;
	}
	public void setEntity(ScheduleInfo entity) {
		this.entity = entity;
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
	public Collection<ScheduleInfo> getPage() {
		return page;
	}
	public void setArrSingleStateJdbcDao(
			IArrSingleStateJdbcDao arrSingleStateJdbcDao) {
		this.arrSingleStateJdbcDao = arrSingleStateJdbcDao;
	}
	public void setScheduleInfoBiz(IScheduleInfoBiz scheduleInfoBiz) {
		this.scheduleInfoBiz = scheduleInfoBiz;
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
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	/**
	 * 查找分页数据
	 * @return
	 */
	public String listPage(){
		if(limit > 0){
			IPage<ScheduleInfo> data = scheduleInfoBiz.listPage(deptId, dataSource, vehicleCode, valid,isUsed, limit, start/limit);
			if(null != data){
				this.page = data.getData();
				this.total = data.getTotalSize();
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
			this.scheduleInfoBiz.updateValid(recordId, valid);
		}catch(BizException e){
			log.error("info:"+e.getMessageKey());
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.errorMsg = I18nEl.i18n_def("common.version.changed","记录已被其他用户修改，请重新提交保存");
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
		}
		return SUCCESS;
	}
	/**
	 * 新增班次信息
	 * @return
	 */
	public String saveEntity(){
		/**控制并发-待确认**/
		int count = 0;
		Long typeId = 1L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.errorMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try{
			scheduleInfoBiz.saveEntity(entity);
		}catch(BizException e){
			log.error("info:"+e.getMessageKey());
			this.errorMsg = e.getMessageKey();
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
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
		/**控制并发**/
		int count = 0;
		Long typeId = 1L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.errorMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try{
			scheduleInfoBiz.updateEntity(entity);
		}catch(BizException e){
			log.error("info:"+e.getMessageKey());
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.errorMsg = I18nEl.i18n_def("common.version.changed","记录已被其他用户修改，请重新提交保存");
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
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
	 * 批量删除
	 * @return
	 */
	public String deleteByIds(){
		try{
			scheduleInfoBiz.deleteByIds(ids);
		}catch(BizException e){
			log.error("info:"+e.getMessageKey());
			this.errorMsg = e.getMessageKey();
		}catch(org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException e){
			this.errorMsg = I18nEl.i18n_def("common.version.changed1","记录已被其他用户修改，请重新提交保存");
		}catch(Exception e){
			log.error("info:",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
			String message = e.getMessage();
			if (message != null && message.indexOf("FK_ARR_INFO_ID") != -1) {
				this.errorMsg = I18nEl.i18n_def("scheduleinfo.action.1","已经被配班引用不允许删除(请刷新页面,重新选择)");
			}
			return SUCCESS;
		}
		return SUCCESS;
	}
	/**
	 * 导入
	 * @return
	 */
	public String saveFile(){
		/**控制并发**/
		int count = 0;
		Long typeId = 1L;
		count = arrSingleSateBiz.updateStart(typeId);
		if(0 == count){
			this.errorMsg = I18nEl.i18n_def("common.action.single","其他用户正在操作，请稍后再试");
			return SUCCESS;
		}
		/**执行业务**/
		try{
			scheduleInfoBiz.saveFile(uploadFile, fileName);
		}catch(BizException e){
			log.error("info:"+e.getMessageKey());
			this.errorMsg = e.getMessageKey();
		}catch(Exception e){
			log.error("",e);
			this.errorMsg = I18nEl.i18n_def("common.action.exception","系统异常，请联系管理员");
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
	 * 导出
	 * @return
	 */
	public String listReport(){
		try{
			this.fileName = scheduleInfoBiz.listReport(deptId, dataSource, vehicleCode, valid,isUsed);
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
			this.success = true;
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
	 * 查找车牌号分页数据
	 * @return
	 */
	public String listVehiclePage(){
		if(limit > 0){
			IPage<ArrVehicle> data = scheduleInfoBiz.listVehiclePage(vehicleCode, modelBase, limit, start/limit);
			if(null != data){
				this.vehiclePage = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
	/**
	 * 查找部门分页数据-无权限控制
	 * @return
	 */
	public String listDeptPage(){
		if(limit > 0){
			IPage<ArrDepartment> data = scheduleInfoBiz.listAllDepts(deptCode, limit, start/limit);
			if(null != data){
				this.deptPage = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
	/**
	 * 查找部门分页数据-有权限控制
	 * @return
	 */
	public String listUserDeptPage(){
		if(limit > 0){
			IPage<ArrDepartment> data = scheduleInfoBiz.listAllUserDepts(deptCode, limit, start/limit);
			if(null != data){
				this.deptPage = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
	/**
	 * 查找车型
	 * @return
	 */
	public String listModelBase(){
		this.brand = scheduleInfoBiz.listModelBase(modelBase);
		return SUCCESS;
	}
	/**
	 * 为配班查找分页数据
	 * @return
	 */
	public String listPageForArrange(){
		if(limit > 0){
			IPage<ScheduleInfo> data = scheduleInfoBiz.listPageForArrange(deptId,recordId, startTm, limit, start/limit);
			if(null != data){
				this.page = data.getData();
				this.total = data.getTotalSize();
			}
		}
		return SUCCESS;
	}
}
