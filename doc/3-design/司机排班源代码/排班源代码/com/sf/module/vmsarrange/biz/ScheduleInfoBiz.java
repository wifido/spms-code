/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-03     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.context.ApplicationContext;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.cmscommon.biz.DepartmentCacheBiz;
import com.sf.module.department.domain.Department;
import com.sf.module.vmsarrange.dao.IArrDepartmentDao;
import com.sf.module.vmsarrange.dao.IArrVehicleDao;
import com.sf.module.vmsarrange.dao.IPreScheduleDraftDao;
import com.sf.module.vmsarrange.dao.IScheduleArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleInfoArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleInfoDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.log.LogUtils;
import com.sf.module.vmsarrange.util.ArrCommonUtil;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsinfo.util.FileUtil;

/**
 * 
 * 班次信息业务实现类
 * 
 */
public class ScheduleInfoBiz extends BaseBiz implements IScheduleInfoBiz {
	private IScheduleInfoDao scheduleInfoDao;
	private IArrVehicleDao arrVehicleDao;
	private IArrDepartmentDao arrDepartmentDao;
	private IScheduleInfoArrangeDao scheduleInfoArrangeDao;
	private IPreScheduleDraftDao preScheduleDraftDao;
	private IScheduleArrangeDao scheduleArrangeDao; 
	private static int MAX_SIZE = 65530;
	private static String TEMPLATE_NAME = "班次信息模板.xls";
	
	public void setScheduleArrangeDao(IScheduleArrangeDao scheduleArrangeDao) {
		this.scheduleArrangeDao = scheduleArrangeDao;
	}

	public void setPreScheduleDraftDao(IPreScheduleDraftDao preScheduleDraftDao) {
		this.preScheduleDraftDao = preScheduleDraftDao;
	}

	public void setScheduleInfoArrangeDao(
			IScheduleInfoArrangeDao scheduleInfoArrangeDao) {
		this.scheduleInfoArrangeDao = scheduleInfoArrangeDao;
	}

	public void setArrDepartmentDao(IArrDepartmentDao arrDepartmentDao) {
		this.arrDepartmentDao = arrDepartmentDao;
	}

	public void setArrVehicleDao(IArrVehicleDao arrVehicleDao) {
		this.arrVehicleDao = arrVehicleDao;
	}

	public void setScheduleInfoDao(IScheduleInfoDao scheduleInfoDao) {
		this.scheduleInfoDao = scheduleInfoDao;
	}

	// 查询分页数据
	public IPage<ScheduleInfo> listPage(Long deptId, Integer dataSource,
			String vehicleCode, Integer valid,Integer isUsed, int pageSize, int pageIndex) {
		IPage<ScheduleInfo> page = scheduleInfoDao.listPage(deptId, dataSource,
				vehicleCode, valid,isUsed, pageSize, pageIndex, super.getUserId());
		if (null != page && !page.isEmpty()) {
			Collection<ScheduleInfo> data = page.getData();
			if (null != data && !data.isEmpty()) {
				for (ScheduleInfo info : data) {
					if (null == info || null == info.getDept()
							|| null == info.getDept().getAreaCode()) {
						continue;
					}
					Department dept = DepartmentCacheBiz
							.getDepartmentByCode(info.getDept().getAreaCode());
					if (null != dept) {
						info.setAreaName((null == dept.getDeptCode()?"":dept.getDeptCode())+"/"
								+(null == dept.getDeptName()?"":dept.getDeptName()));
					}
				}
			}
		}
		return page;
	}

	// 新增班次信息
	public void saveEntity(ScheduleInfo entity) {
		/** 校验参数非空 **/
		if (null == entity) {
			// 提交的参数为空，请重新提交
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		if (null == entity.getDataSource()) {
			// 数据源不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.10","数据源不能为空"));
		}
		if (null == entity.getDept() || ArrFileUtil.isEmpty(entity.getDept().getDeptCode())) {
			// 归属网点不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.11","归属网点不能为空"));
		}
		if (null == entity.getStartDept()
				|| ArrFileUtil.isEmpty(entity.getStartDept().getDeptCode())) {
			// 始发网点不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.12","始发网点不能为空"));
		}
		if (null == entity.getEndDept() || ArrFileUtil.isEmpty(entity.getEndDept().getDeptCode())) {
			// 目的网点不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.13","目的网点不能为空"));
		}
		if (null == entity.getStartTm()) {
			// 出车时间不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.14","出车时间不能为空"));
		}
		if (null == entity.getEndTm()) {
			// 收车时间不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.15","收车时间不能为空"));
		}
		if (null == entity.getValid()) {
			// 是否有效不能为空
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.18","是否有效不能为空"));
		}
		//过滤手工录入的空格
		entity.getDept().setDeptCode(entity.getDept().getDeptCode().trim());
		entity.getStartDept().setDeptCode(entity.getStartDept().getDeptCode().trim());
		entity.getEndDept().setDeptCode(entity.getEndDept().getDeptCode().trim());
		/** 校验参数格式合法 **/
		// 新增-数据源取值必须为1：手工录入
		if (entity.getDataSource().compareTo(1) != 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.19","数据源取值必须为1"));
		}
		// 出收车时间格式
		if (!entity.getStartTm().matches("^\\d{2}:\\d{2}$")) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.20","出车时间格式有误,正确格式例如:09:23"));
		}
		if (!entity.getEndTm().matches("^\\d{2}:\\d{2}$")) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.21","收车时间格式有误,正确格式例如:09:23"));
		}
		// 校验出车时间格式
		Integer hourPartStart = Integer.parseInt(entity.getStartTm().split(":",
				-1)[0]);
		Integer minutePartStart = Integer.parseInt(entity.getStartTm().split(
				":", -1)[1]);
		if (hourPartStart.compareTo(0) < 0 || hourPartStart.compareTo(23) > 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.22","出车时间:时钟不能小于0大于23"));
		}
		if (minutePartStart.compareTo(0) < 0
				|| minutePartStart.compareTo(59) > 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.23","出车时间:分钟不能小于0大于59"));
		}
		// 校验收车时间格式
		Integer hourPartEnd = Integer
				.parseInt(entity.getEndTm().split(":", -1)[0]);
		Integer minutePartEnd = Integer.parseInt(entity.getEndTm().split(":",
				-1)[1]);
		if (hourPartEnd.compareTo(0) < 0 || hourPartEnd.compareTo(23) > 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.24","收车时间:时钟不能小于0大于23"));
		}
		if (minutePartEnd.compareTo(0) < 0 || minutePartEnd.compareTo(59) > 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.25","收车时间:分钟不能小于0大于59"));
		}
		// 出车时间不能等于收车时间
		if (compareStartEnd(entity.getStartTm(), entity.getEndTm())) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.26","出车时间不能等于收车时间"));
		}
		// 是否有效取值有误，取值范围:0,1
		if (entity.getValid().compareTo(1) != 0
				&& entity.getValid().compareTo(0) != 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.27","是否有效取值有误，取值范围:0,1"));
		}
		/** 校验数据有效 **/
		// 所属网点
		Department dept = null;
		dept = DepartmentCacheBiz.getDepartmentByCode(entity.getDept().getDeptCode());
		if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.28","归属网点无效(请点击文本框右边的搜索图标查询有效网点)"));
		}
		entity.getDept().setId(dept.getId());
		//归属网点权限判断
		if(this.getUserId().compareTo(1L) != 0){
			ArrDepartment d = arrDepartmentDao.listDeptByUserAndDept(dept.getId(), this.getUserId());
			if(null == d || null == d.getId()){
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.29","所录归属网点没有权限(请点击文本框右边的搜索图标查询有权限的网点)"));
			}
		}
		// 始发地
		dept = null;
		dept = DepartmentCacheBiz.getDepartmentByCode(entity.getStartDept()
				.getDeptCode());
		if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.30","始发网点无效(请点击文本框右边的搜索图标查询有效网点)"));
		}
		entity.getStartDept().setId(dept.getId());
		// 目的地
		dept = null;
		dept = DepartmentCacheBiz
				.getDepartmentByCode(entity.getEndDept().getDeptCode());
		if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.31","目的网点无效(请点击文本框右边的搜索图标查询有效网点)"));
		}
		entity.getEndDept().setId(dept.getId());
		/** 校验重复 **/
		// 校验重复-有效数据才校验
		if(entity.getValid().compareTo(1) == 0){
			Integer repeat = scheduleInfoDao.listRepeat(entity.getStartTm(), entity
					.getEndTm(), entity.getStartDept().getId(), entity.getEndDept()
					.getId(),null);
			if (repeat > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.34","与系统已有数据重复(始发地,目的地,出车时间,收车时间都相同)"));
			}
		}
		// 保存数据
		entity.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
		entity.setCreatedTm((new Date()));
		scheduleInfoDao.save(entity);
	}

	// 修改班次信息
	public void updateEntity(ScheduleInfo entity) throws Exception {
		if (null == entity || null == entity.getId()) {
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		ScheduleInfo old = scheduleInfoDao.load(entity.getId());
		if (null == old) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.36","记录已经被删除，请刷新页面"));
		}
		if(null == old.getIsUsed() || old.getIsUsed().compareTo(0) != 0){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.37","记录已被配班使用不允许修改，请刷新页面"));
		}
		if (null == entity.getValid()) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.18","是否有效不能为空"));
		}
		/** 校验参数格式合法 **/
		// 是否有效取值有误，取值范围:0,1
		if (entity.getValid().compareTo(1) != 0
				&& entity.getValid().compareTo(0) != 0) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.27","是否有效取值有误，取值范围:0,1"));
		}
		if (null == entity.getDept() || ArrFileUtil.isEmpty(entity.getDept().getDeptCode())) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.11","归属网点不能为空"));
		}
		//过滤手工录入的空格
		entity.getDept().setDeptCode(entity.getDept().getDeptCode().trim());
		// 所属网点
		Department dept = null;
		dept = DepartmentCacheBiz.getDepartmentByCode(entity.getDept().getDeptCode());
		if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.28","归属网点无效(请点击文本框右边的搜索图标查询有效网点)"));
		}
		entity.getDept().setId(dept.getId());
		//归属网点权限判断
		if(this.getUserId().compareTo(1L) != 0){
			ArrDepartment d = arrDepartmentDao.listDeptByUserAndDept(dept.getId(), this.getUserId());
			if(null == d || null == d.getId()){
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.29","所录归属网点没有权限(请点击文本框右边的搜索图标查询有权限的网点)"));
			}
		}
		/** 手工录入 **/
		if (old.getDataSource().compareTo(1) == 0) {
			/** 校验新参数 **/
			if (null == entity.getStartDept()
					|| ArrFileUtil.isEmpty(entity.getStartDept().getDeptCode())) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.12","始发网点不能为空"));
			}
			if (null == entity.getEndDept() || ArrFileUtil.isEmpty(entity.getEndDept().getDeptCode())) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.13","目的网点不能为空"));
			}
			entity.getStartDept().setDeptCode(entity.getStartDept().getDeptCode().trim());
			entity.getEndDept().setDeptCode(entity.getEndDept().getDeptCode().trim());
			/** 校验数据有效 **/
			// 始发地
			dept = null;
			dept = DepartmentCacheBiz.getDepartmentByCode(entity.getStartDept()
					.getDeptCode());
			if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.30","始发网点无效(请点击文本框右边的搜索图标查询有效网点)"));
			}
			entity.getStartDept().setId(dept.getId());
			// 目的地
			dept = null;
			dept = DepartmentCacheBiz
					.getDepartmentByCode(entity.getEndDept().getDeptCode());
			if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.31","目的网点无效(请点击文本框右边的搜索图标查询有效网点)"));
			}
			entity.getEndDept().setId(dept.getId());
			/** 校验重复 **/
			if(entity.getValid().compareTo(1)==0){
				//只与有效数据比较-不与自己比较
				Integer repeat = scheduleInfoDao.listRepeat(old.getStartTm(),
						old.getEndTm(), entity.getStartDept().getId(), entity
								.getEndDept().getId(),entity.getId());
				if (repeat > 0) {
					throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.34","与系统已有数据重复(始发地,目的地,出车时间,收车时间都相同)"));
				}
			}
			/** 校验时间冲突* **/
			if(null != old.getVehicle() && !ArrFileUtil.isEmpty(old.getVehicle().getVehicleCode())){
				List<ScheduleInfo> list = scheduleInfoDao.listByCode(old.getVehicle().getVehicleCode());
				if (null != list && !list.isEmpty()) {
					for (ScheduleInfo info : list) {
						if (null == info || ArrFileUtil.isEmpty(info.getStartTm())
								|| ArrFileUtil.isEmpty(info.getEndTm())) {
							continue;
						}
						//不与自己比较
						if(info.getId().compareTo(old.getId())==0){
							continue;
						}
						// 时间不能冲突
						if (compareCross(old.getStartTm(), old.getEndTm(),
								info.getStartTm(), info.getEndTm())) {
							throw new BizException(I18nEl.i18n_def_arg2("scheduleinfo.biz.35",
									"线路时间有冲突，该车已经有出车时间={0},收车时间={1}的线路",
									info.getStartTm(), info.getEndTm()));
						}
					}
				}
			}
			//设置修改人修改时间
			entity.setModifiedEmpCode(this.getCurrentUser().getEmployee()
					.getCode());
			entity.setModifiedTm((new Date()));
			//记录日志
			LogUtils.getInstance().updateLog(old, entity);
			// 保存数据
			old.setDept(entity.getDept());
			old.setEndDept(entity.getEndDept());
			old.setStartDept(entity.getStartDept());
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee()
					.getCode());
			old.setModifiedTm(ArrCommonUtil.currentTm());
			old.setValid(entity.getValid());
		}
		/**路径优化***/
		if (old.getDataSource().compareTo(2) == 0){
			/** 校验新参数 **/
			if (ArrFileUtil.isEmpty(entity.getVehicle().getVehicleCode())) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.17","车牌号不能为空"));
			}
			entity.getDept().setDeptCode(entity.getDept().getDeptCode().trim());
			entity.getVehicle().setVehicleCode(entity.getVehicle().getVehicleCode().trim());
			/***校验数据有效性**/
			// 车牌号有效
			ArrVehicle vehicle = arrVehicleDao.listByCode(entity.getVehicle()
					.getVehicleCode());
			if (null == vehicle) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.32","车牌号无效(只取正常停用车辆，请点击文本框右边的搜索图标查询有效车辆)"));
			}
			// 车型匹配
			if (null == vehicle.getModelBase() || !vehicle.getModelBase().equals(old.getModelBase())) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.33","所选车型与车辆车型不匹配"));
			}
			/** 校验重复 **/
			if(entity.getValid().compareTo(1)==0){
				//只与有效数据比较-不与自己比较
				Integer repeat = scheduleInfoDao.listRepeat(old.getStartTm(),
						old.getEndTm(), old.getStartDept().getId(), old
								.getEndDept().getId(),entity.getId());
				if (repeat > 0) {
					throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.34","与系统已有数据重复(始发地,目的地,出车时间,收车时间都相同)"));
				}
			}
			/** 校验时间冲突* **/
			if(entity.getValid().compareTo(1) == 0){
				List<ScheduleInfo> list = scheduleInfoDao.listByCode(vehicle.getVehicleCode());
				if (null != list && !list.isEmpty()) {
					for (ScheduleInfo info : list) {
						if (null == info || ArrFileUtil.isEmpty(info.getStartTm())
								|| ArrFileUtil.isEmpty(info.getEndTm())) {
							continue;
						}
						//不与自己比较
						if(info.getId().compareTo(old.getId())==0){
							continue;
						}
						// 时间不能冲突
						if (compareCross(old.getStartTm(), old.getEndTm(),
								info.getStartTm(), info.getEndTm())) {
							throw new BizException(I18nEl.i18n_def_arg2("scheduleinfo.biz.35",
									"线路时间有冲突，该车已经有出车时间={0},收车时间={1}的线路",
									info.getStartTm(), info.getEndTm()));
						}
					}
				}
			}
			//设置修改人修改时间
			entity.setModifiedEmpCode(this.getCurrentUser().getEmployee()
					.getCode());
			entity.setModifiedTm((new Date()));
			//记录日志
			LogUtils.getInstance().updateLog(old, entity);
			//保存数据
			old.setDept(entity.getDept());
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee()
					.getCode());
			old.setModifiedTm(ArrCommonUtil.currentTm());
			old.setValid(entity.getValid());
			old.setVehicle(vehicle);
		}
	}

	// 查找车型-所有状态车辆
	@SuppressWarnings("rawtypes")
	public List<Map> listModelBase(String modelBase) {
		return arrVehicleDao.listModelBase(modelBase);
	}

	// 分页查找车辆
	public IPage<ArrVehicle> listVehiclePage(String vehicleCode,
			String modelBase, int pageSize, int pageIndex) {
		return arrVehicleDao.listVehiclePage(vehicleCode, modelBase, pageSize,
				pageIndex);
	}

	// 查找全网所有部门-无权限控制
	public IPage<ArrDepartment> listAllDepts(String deptCode, int pageSize,
			int pageIndex) {
		return arrDepartmentDao.listAllDepts(deptCode, pageSize, pageIndex);
	}

	// 查找有权限的部门
	public IPage<ArrDepartment> listAllUserDepts(String deptCode, int pageSize,
			int pageIndex) {
		return arrDepartmentDao.listAllUserDepts(deptCode, super.getUserId(),
				pageSize, pageIndex);
	}
	//批量删除
	public void deleteByIds(List<Long> ids) throws Exception {
		if(null == ids || ids.isEmpty()){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		for(Long id:ids){
			ScheduleInfo old = scheduleInfoDao.load(id);
			if(null == old){
				continue;
			}
			if(old.getValid().compareTo(1) == 0){
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.1","有效数据不能删除(请刷新页面，重新选择)"));
			}
			if(old.getIsUsed().compareTo(0) != 0){
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.39","该线路已被配班引用不可删除"));
			}
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			old.setModifiedTm((new Date()));
			scheduleInfoDao.remove(old);
			LogUtils.getInstance().deleteLog(old);
		}
	}
	//导入路径优化线路
	public void saveFile(File uploadFile, String fileName){
		if(null == uploadFile) {
			throw new BizException(I18nEl.i18n_def("common.biz.nullFile","接收到的上传文件为空，请重新上传"));
		}
		if(uploadFile.exists()){
			HSSFWorkbook workbook = null;
			try {
				workbook = new HSSFWorkbook(uploadFile.toURL().openStream());
			} catch (Exception e) {
				log.error("",e);
				throw new BizException(I18nEl.i18n_def("common.biz.readFailure","读取上传文件失败，请重新上传"));
			}
			HSSFSheet sheet = workbook.getSheetAt(0);
			readSheet(sheet);	
		}else{
			throw new BizException(I18nEl.i18n_def("common.biz.notExist","所选文件不存在，请重新选择文件上传"));
		}
	}
	//读取导入sheet页
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readSheet(HSSFSheet sheet) {	
		String createdEmpCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		final int rightColNum = 9;
		final int startRow = 1;
		//检验表格是否为空
		if( null == sheet ){
			throw new BizException(I18nEl.i18n_def("common.biz.sheetNull","文件填写有误，第一个sheet页为空"));
		}
		//转换sheet数据成list map对象集合
		List<Map<String,String>> datas = new ArrayList<Map<String,String>>();	
		if(sheet.getRow(0)==null){
			throw new BizException(I18nEl.i18n_def("common.biz.titleNull","文件填写有误，第一行(标题行)为空"));
		}
		//定义标题行
		String[] header = new String[]{"0","1","2","3","4","5","6","7","8"};
		//判断列数是否正确
		if(sheet.getRow(0).getLastCellNum() < rightColNum){
			throw new BizException(I18nEl.i18n_def_arg1("common.biz.titleColumnError","文件填写有误,第一行(标题行)列数有误，正确列数为{0}列"
					,rightColNum));
		}
		//获取数据
		try {
			int dataIdx = 0;
			for(int i=startRow;i<=sheet.getLastRowNum();i++){
				HSSFRow row = sheet.getRow(i);
				//过滤空行
				if(null == row){
					continue;
				}
				// 获取对应的数据行,不获取空行数据
				Map dataMap = new HashMap<String,String>(rightColNum);
				boolean hasValue = false; // 标志是否不为空行
				int colIdx = 0;
				String value = null;
				//数据源
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//路径优化线路编号
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.3"
								,"文件填写有误,第{0}行数据路径优化线路编号格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//车型
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.4"
								,"文件填写有误,第{0}行车型格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//出车时间
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//收车时间
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//始发网点
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.5"
								,"文件填写有误,第{0}行始发网点格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//目的网点
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.6"
								,"文件填写有误,第{0}行目的网点格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//归属网点
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.7"
								,"文件填写有误,第{0}行归属网点格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				//指定车牌
				value = getHSSFCellValue(row.getCell(colIdx));
				if(!ArrFileUtil.isEmpty(value)){
					hasValue = true;
					if(null != row.getCell(colIdx) 
							&& row.getCell(colIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
						throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.8"
								,"文件填写有误,第{0}行指定车牌号格式有误(必须为文本格式)",(i+1)));
					}
				}
				dataMap.put(header[colIdx],value);
				colIdx++;
				if(hasValue){
					++dataIdx;
					datas.add(dataMap);
					if(dataIdx > 10000){
						throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.38","数据量过大(超出1万条)，请分批导入"));
					}
				}
			}
		} catch(BizException e){
			throw e;
		}catch (Exception e) {
			log.error("",e);
			throw new BizException(I18nEl.i18n_def("common.biz.parseFailure","导入失败,表格数据转换异常,请检查表格中是否存在引用或特殊格式"));
		}
		
		//检验数据是否为空
		if( null==datas || datas.isEmpty()){
			throw new BizException(I18nEl.i18n_def("common.biz.dataNull","导入失败，表格数据为空"));
		}
		/**
		 * 检验数据的有效性，并将有效记录存入list实体集合
		 * 若全部有效则通过
		 * 若存在无效记录，则返回无效原因
		 */
		int wrongNum = 0;
		StringBuilder wrongMsg = new StringBuilder();
		ScheduleInfo entity;	
		//存储所有校验都同的记录
		List<ScheduleInfo> list = new ArrayList<ScheduleInfo>();
		String dataSource,lineOptimizeNo,modelBase,startTm,endTm,startDeptCode,endDeptCode,deptCode,vehicleCode;
		//加载所有正常停用车辆
		List<ArrVehicle> vehicleList = arrVehicleDao.listAllVehicle();
		//不是admin,加载所有有权限部门
		List<ArrDepartment> userDepts = null;
		if(userId.compareTo(1L) != 0){
			userDepts = arrDepartmentDao.listAllDeptByUser(userId);
		}
		/***分两步校验 1.校验数据本身 2.与系统数据对比**/
		//1.校验数据本身
		for(int rowIndex = 0;rowIndex<datas.size();rowIndex++){
			//错误行数超过100行，退出校验
			if(wrongNum >= 100){
				wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
				break;
			}
			StringBuilder wrongStr = new StringBuilder("第" + (rowIndex+2) + "行：");
			java.util.Map<String,String> map = datas.get(rowIndex);
			dataSource = map.get("0");
			lineOptimizeNo = map.get("1");
			modelBase = map.get("2");
			startTm = map.get("3");
			endTm = map.get("4");
			startDeptCode = map.get("5");
			endDeptCode = map.get("6");
			deptCode = map.get("7");
			vehicleCode = map.get("8");
			
			/**校验非空**/
			if(ArrFileUtil.isEmpty(dataSource)){ 
				wrongStr.append("数据源不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(startTm)){ 
				wrongStr.append("出车时间不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(endTm)){ 
				wrongStr.append("收车时间不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(startDeptCode)){ 
				wrongStr.append("始发网点不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(endDeptCode)){ 
				wrongStr.append("目的网点不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(ArrFileUtil.isEmpty(deptCode)){ 
				wrongStr.append("归属网点不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//过滤空格
			dataSource = dataSource.trim();
			modelBase = modelBase.trim();
			startTm = startTm.trim();
			endTm = endTm.trim();
			startDeptCode = startDeptCode.trim();
			endDeptCode = endDeptCode.trim();
			deptCode = deptCode.trim();
			vehicleCode = vehicleCode.trim();
			/**校验格式**/
			//数据源取值
			if(!dataSource.equals("手工录入") && !dataSource.equals("路径优化")){
				wrongStr.append("数据源必须为:手工录入或路径优化");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//车型和车牌号不能为空
			if(dataSource.equals("路径优化")){
				if(ArrFileUtil.isEmpty(modelBase)){ 
					wrongStr.append("车型不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(ArrFileUtil.isEmpty(vehicleCode)){ 
					wrongStr.append("车牌号不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}else{
				if(!ArrFileUtil.isEmpty(modelBase)){ 
					wrongStr.append("数据源为手工录入不允许指定车型，请清除车型");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(!ArrFileUtil.isEmpty(vehicleCode)){ 
					wrongStr.append("数据源为手工录入不允许指定车牌号，请清除车牌号");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			//路径优化线路编号
			if(dataSource.equals("手工录入") && !ArrFileUtil.isEmpty(lineOptimizeNo)){
				wrongStr.append("数据源为手工录入时不允许填写路径优化线路编号，请清除路径优化线路编号");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(dataSource.equals("路径优化") && ArrFileUtil.isEmpty(lineOptimizeNo)){
				wrongStr.append("数据源为路径优化时,路径优化线路编号不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(dataSource.equals("路径优化") && !ArrFileUtil.isEmpty(lineOptimizeNo)
					&& lineOptimizeNo.getBytes().length > 50){
				wrongStr.append("路径优化编号过长");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			// 出车时间格式
			if (!startTm.matches("^\\d{2}:\\d{2}$")) {
				wrongStr.append("出车时间格式有误,正确格式例如:09:23");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			Integer hourPartStart = Integer.parseInt(startTm.split(":",
					-1)[0]);
			Integer minutePartStart = Integer.parseInt(startTm.split(
					":", -1)[1]);
			if (hourPartStart.compareTo(0) < 0 || hourPartStart.compareTo(23) > 0) {
				wrongStr.append("出车时间:时钟不能小于0或大于23");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if (minutePartStart.compareTo(0) < 0
					|| minutePartStart.compareTo(59) > 0) {
				wrongStr.append("出车时间:分钟不能小于0或大于59");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			// 收车时间格式
			if (!endTm.matches("^\\d{2}:\\d{2}$")) {
				wrongStr.append("收车时间格式有误,正确格式例如:09:23");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			Integer hourPartEnd = Integer.parseInt(endTm.split(
					":", -1)[0]);
			Integer minutePartEnd = Integer.parseInt(endTm
					.split(":", -1)[1]);
			if (hourPartEnd.compareTo(0) < 0
					|| hourPartEnd.compareTo(23) > 0) {
				wrongStr.append("收车时间:时钟不能小于0或大于23");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if (minutePartEnd.compareTo(0) < 0
					|| minutePartEnd.compareTo(59) > 0) {
				wrongStr.append("收车时间:分钟不能小于0或大于59");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/** 校验数据有效 **/
			// 出车时间不能等于收车时间
			if (compareStartEnd(startTm,endTm)) {
				wrongStr.append("出车时间不能等于收车时间");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			// 所属网点
			Department dept = DepartmentCacheBiz.getDepartmentByCode(deptCode);
			if (null == dept || null == dept.getId() || dept.getDeleteFlg()) {
				wrongStr.append("归属网点不存在(请检查网点是否已经失效)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//所属网点权限
			if(userId.compareTo(1L) != 0){
				ArrDepartment d = getArrDepartmentById(userDepts, dept.getId());
				if(null == d || null == d.getId()){
					wrongStr.append("您没有该归属网点的权限(请重新填写)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			// 始发地
			Department startDept = DepartmentCacheBiz.getDepartmentByCode(startDeptCode);
			if (null == startDept || null == startDept.getId() || startDept.getDeleteFlg()) {
				wrongStr.append("始发网点不存在(请检查网点是否已经失效)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			// 目的地
			Department endDept = DepartmentCacheBiz.getDepartmentByCode(endDeptCode);
			if (null == endDept || null == endDept.getId() || endDept.getDeleteFlg()) {
				wrongStr.append("目的网点不存在(请检查网点是否已经失效)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			ArrVehicle vehicle = null;
			if(dataSource.equals("路径优化")){
				// 车牌号有效
				vehicle = getVehicleByCode(vehicleList,vehicleCode);
				if (null == vehicle) {
					wrongStr.append("车牌号不存在(只取正常停用车辆)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				// 车型匹配
				if (null == vehicle.getModelBase() || !vehicle.getModelBase().equals(modelBase)) {
					wrongStr.append("车型与车辆车型不匹配");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			/** 校验重复和交叉 **/
			entity = new ScheduleInfo();
			if(dataSource.equals("手工录入")){
				entity.setDataSource(1);
			}else{
				entity.setDataSource(2);
			}
			ArrDepartment arrDept = new ArrDepartment();
			arrDept.setId(dept.getId());
			ArrDepartment arrStartDept = new ArrDepartment();
			arrStartDept.setId(startDept.getId());
			ArrDepartment arrEndDept = new ArrDepartment();
			arrEndDept.setId(endDept.getId());
			entity.setDept(arrDept);
			entity.setEndDept(arrEndDept);
			entity.setEndTm(endTm);
			entity.setLineOptimizeNo(lineOptimizeNo);
			entity.setStartDept(arrStartDept);
			entity.setStartTm(startTm);
			if(dataSource.equals("路径优化")){
				entity.setModelBase(modelBase);
				entity.setVehicle(vehicle);
			}
			/****a.先与excel自身数据校验****/
			//校验重复和交叉
			if(checkRepeat(list,entity)){
				wrongStr.append("线路重复(请检查您填写的线路信息并修正)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			if(checkCross(list,entity)){
				wrongStr.append("该车线路时间有冲突(请检查您填写的出收车时间并修正)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//校验全部通过-添加到集合
			list.add(entity);
		}
		//有校验不通过的线路，则提示
		if(!ArrFileUtil.isEmpty(wrongMsg.toString())) {
			throw new BizException(wrongMsg.toString());
		}
		//2.与系统数据对比
		Map<String,List<ScheduleInfo>> infoMap = new HashMap<String,List<ScheduleInfo>>();
		for(int rowIndex = 0;rowIndex<list.size();rowIndex++){
			//错误行数超过100行，退出校验
			if(wrongNum >= 100){
				wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
				break;
			}
			StringBuilder wrongStr = new StringBuilder("第" + (rowIndex+2) + "行：");
			ScheduleInfo obj = list.get(rowIndex);
			/****b.与系统已有数据校验****/
			// 校验重复
			Integer repeat = scheduleInfoDao.listRepeat(obj.getStartTm(), obj
					.getEndTm(), obj.getStartDept().getId(), obj.getEndDept()
					.getId(),null);
			if (repeat > 0) {
				wrongStr.append("与系统中已有线路信息重复(请修正)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/***校验时间是否交叉***/
			//标识是否有时间冲突
			boolean flg = false;
			//路径优化、校验时间冲突
			if(obj.getDataSource().compareTo(2) == 0){
				List<ScheduleInfo> l =  null;
				if(infoMap.containsKey(obj.getVehicle().getVehicleCode())){
					//缓存中有该车牌线路，则直接拿
					l = infoMap.get(obj.getVehicle().getVehicleCode());
				}else{
					//缓存中没有该车牌对应线路，则查询数据库并添加到缓存
					l = scheduleInfoDao.listByCode(obj.getVehicle()
							.getVehicleCode());
					//缓存存放已经校验过的车牌对应的线路信息,最多放100个
					if(infoMap.size()>100){
						infoMap.clear();
					}
					infoMap.put(obj.getVehicle().getVehicleCode(), l);
				}
				if (null != l && !l.isEmpty()) {
					for (ScheduleInfo info : l) {
						if (null == info || ArrFileUtil.isEmpty(info.getStartTm())
								|| ArrFileUtil.isEmpty(info.getEndTm())) {
							continue;
						}
						/** 时间不能冲突 **/
						if (compareCross(obj.getStartTm(), obj.getEndTm(),
								info.getStartTm(), info.getEndTm())) {
							flg = true;
							wrongStr.append(String.format(
									"线路时间有冲突，系统中该车已经有出车时间=%s,收车时间=%s的线路",
									info.getStartTm(), info.getEndTm()));
							break;
						}
					}
				}
			}
			/**时间冲突校验未通过，不做后续操作**/
			if(flg){
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
		}
		//有校验不通过的班次，则提示
		if(!ArrFileUtil.isEmpty(wrongMsg.toString())) {
			throw new BizException(wrongMsg.toString());
		}
		//校验全部通过-保存数据
		List<ScheduleInfo> saveListBatch = new ArrayList<ScheduleInfo>();
		if(null != list && !list.isEmpty()){
			for(ScheduleInfo info:list){
				if(null != info){
					info.setCreatedEmpCode(createdEmpCode);
					info.setCreatedTm(ArrCommonUtil.currentTm());
					//设置为有效
					info.setValid(1);
					saveListBatch.add(info);
					if(saveListBatch.size() == 1000){
						scheduleInfoDao.saveBatch(saveListBatch);
						saveListBatch.clear();
					}
				}
			}
			if(!saveListBatch.isEmpty()){
				scheduleInfoDao.saveBatch(saveListBatch);
			}
		}
	}
	/**
	 * 生成报表
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public String listReport(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed) {
		String userCode = this.getCurrentUser().getEmployee().getCode();
		if(null == deptId){
			throw new BizException(I18nEl.i18n_def("common.biz.deptNull","查询条件网点代码不能为空"));
		}
		String fileName = null;
		if(FileUtil.isEmpty(userCode)){
			fileName = "线路信息报表.xls";
		}else{
			fileName = "线路信息报表("+userCode+").xls";
		}
		//创建EXCEL
		java.io.InputStream is = null;
		try {
			is = new FileInputStream(this.getTemplatePath());
		} catch (FileNotFoundException e) {
			log.error("1",e);
			throw new BizException(I18nEl.i18n_def("common.biz.templateNull","生成报表失败，模板文件不存在"));
		} catch (Exception e) {
			log.error("2",e);
			throw new BizException(I18nEl.i18n_def("common.biz.readException","生成报表失败，模板文件读取异常"));
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(is);
		} catch (IOException e) {
			log.error("3",e);
			throw new BizException(I18nEl.i18n_def("common.biz.readException","生成报表失败，模板文件读取异常"));
		}	
		/**获取模板页及各列单元格样式**/
		HSSFSheet templateSheet = workbook.getSheetAt(0);
		if(templateSheet==null){
			throw new BizException(I18nEl.i18n_def("common.biz.firstSheetNull","生成报表失败，模板中第一个sheet页为空"));
		}
		//获取样式
		final int totalColumn = 10;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = templateSheet.getRow(1);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		//查询分页数据
		Long dataSize = scheduleInfoDao.listReportCount(deptId, dataSource, vehicleCode, valid,isUsed, super.getUserId());
		if(dataSize.compareTo(this.MAX_SIZE*5L) > 0){
			throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.9"
					,"数据量超过了{0}条(导出将超时),请分批导出", this.MAX_SIZE*5L));
		}
		int startIdx = 1;
		int endIdx = this.MAX_SIZE;
		//有数据则写数据
		if(dataSize > 0){
			if(dataSize <= this.MAX_SIZE){
				//数据量没有超出最大值，单sheet页
				List<Map> data = scheduleInfoDao.listReport(deptId, dataSource, vehicleCode, valid,isUsed, startIdx, endIdx, this.getUserId());
				this.fillData(data, templateSheet, style);
			}else{
				//数据量超过最大值，分sheet页导出
				while(dataSize >= startIdx){
					HSSFSheet sheet = workbook.cloneSheet(0);
					workbook.setSheetName(workbook.getSheetIndex(sheet), "线路信息"+workbook.getSheetIndex(sheet));
					List<Map> data = scheduleInfoDao.listReport(deptId, dataSource, vehicleCode, valid,isUsed, startIdx, endIdx, this.getUserId());
					this.fillData(data, sheet, style);
					startIdx += this.MAX_SIZE;
					endIdx += this.MAX_SIZE;
				}
				//隐藏模板sheet页
				workbook.setSheetHidden(0, true);
			}
		}
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("ScheduleInfoBiz", "vmsarrange");
		} catch (Exception e) {
			log.error("4",e);
			throw new BizException(I18nEl.i18n_def("common.biz.tmpDir","获取临时存储目录失败"));
		}
		java.io.OutputStream os;
		try {
			os = new FileOutputStream(new File(savePath+File.separator+fileName));
		} catch (FileNotFoundException e) {
			log.error("5",e);
			throw new BizException(I18nEl.i18n_def("common.biz.tmpDirNotExist","找不到临时存储目录"));
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			log.error("6",e);
			throw new BizException(I18nEl.i18n_def("common.biz.writeFailure","写文件失败"));
		}
		if( null!=os ){
			try { os.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		if ( null!=is) {
			try { is.close(); } catch (Exception e2) {log.error("com.sf.module.vmsbase.biz.FuelTargetBiz.saveDownload failure:",e2);}
		}
		return savePath+File.separator+fileName;
	}
	/**
	 * 填充汇总数据
	 */
	@SuppressWarnings("rawtypes")
	private void fillData(List<Map> data,HSSFSheet sheet,HSSFCellStyle[] style){
		if(null == data || data.isEmpty()){
			return ;
		}
		if(null == sheet){
			return ;
		}
		HSSFCell cell;
		HSSFRow row;
		for(int i=0;i<data.size();i++){
			Map m = data.get(i);
			if(null == m){
				continue;
			}
			row = sheet.getRow(i+1);
			if(null == row){
				row = sheet.createRow(i+1);
			}
			//记录列号
			int columnIdx = 0;
			//地区
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("AREA_CODE")
					&& !ArrFileUtil.isEmpty(m.get("AREA_CODE").toString())){
				Department d = DepartmentCacheBiz.getDepartmentByCode(m.get("AREA_CODE").toString());
				if(null != d && !ArrFileUtil.isEmpty(d.getDeptName())){
					cell.setCellValue(m.get("AREA_CODE").toString() + "/" +d.getDeptName());
				}else{
					cell.setCellValue(m.get("AREA_CODE").toString());
				}
			}
			++columnIdx;
			//数据源
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("DATA_SOURCE")
					&& !ArrFileUtil.isEmpty(m.get("DATA_SOURCE").toString())){
				cell.setCellValue(m.get("DATA_SOURCE").toString());
			}
			++columnIdx;
			//路径优化线路编号
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("LINE_OPTIMIZE_NO")
					&& !ArrFileUtil.isEmpty(m.get("LINE_OPTIMIZE_NO").toString())){
				cell.setCellValue(m.get("LINE_OPTIMIZE_NO").toString());
			}
			++columnIdx;
			//车型
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("MODEL_BASE")
					&& !ArrFileUtil.isEmpty(m.get("MODEL_BASE").toString())){
				cell.setCellValue(m.get("MODEL_BASE").toString());
			}
			++columnIdx;
			//出车时间
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("START_TM")
					&& !ArrFileUtil.isEmpty(m.get("START_TM").toString())){
				cell.setCellValue(m.get("START_TM").toString());
			}
			++columnIdx;
			//收车时间
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("END_TM")
					&& !ArrFileUtil.isEmpty(m.get("END_TM").toString())){
				cell.setCellValue(m.get("END_TM").toString());
			}
			++columnIdx;
			//始发网点
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			String deptCodeName = null;
			if(null != m.get("START_DEPT_CODE")
					&& !ArrFileUtil.isEmpty(m.get("START_DEPT_CODE").toString())){
				deptCodeName = m.get("START_DEPT_CODE").toString();
			}
			if(null != m.get("START_DEPT_NAME")
					&& !ArrFileUtil.isEmpty(m.get("START_DEPT_NAME").toString())){
				if(null == deptCodeName){
					deptCodeName = m.get("START_DEPT_NAME").toString();
				}else{
					deptCodeName += "/" + m.get("START_DEPT_NAME").toString();
				}
			}
			if(null != deptCodeName){
				cell.setCellValue(deptCodeName);
			}
			++columnIdx;
			//目的网点
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			deptCodeName = null;
			if(null != m.get("END_DEPT_CODE")
					&& !ArrFileUtil.isEmpty(m.get("END_DEPT_CODE").toString())){
				deptCodeName = m.get("END_DEPT_CODE").toString();
			}
			if(null != m.get("END_DEPT_NAME")
					&& !ArrFileUtil.isEmpty(m.get("END_DEPT_NAME").toString())){
				if(null == deptCodeName){
					deptCodeName = m.get("END_DEPT_NAME").toString();
				}else{
					deptCodeName += "/" + m.get("END_DEPT_NAME").toString();
				}
			}
			if(null != deptCodeName){
				cell.setCellValue(deptCodeName);
			}
			++columnIdx;
			//归属网点
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			deptCodeName = null;
			if(null != m.get("DEPT_CODE")
					&& !ArrFileUtil.isEmpty(m.get("DEPT_CODE").toString())){
				deptCodeName = m.get("DEPT_CODE").toString();
			}
			if(null != m.get("DEPT_NAME")
					&& !ArrFileUtil.isEmpty(m.get("DEPT_NAME").toString())){
				if(null == deptCodeName){
					deptCodeName = m.get("DEPT_NAME").toString();
				}else{
					deptCodeName += "/" + m.get("DEPT_NAME").toString();
				}
			}
			if(null != deptCodeName){
				cell.setCellValue(deptCodeName);
			}
			++columnIdx;
			//指定车牌号
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("VEHICLE_CODE")
					&& !ArrFileUtil.isEmpty(m.get("VEHICLE_CODE").toString())){
				cell.setCellValue(m.get("VEHICLE_CODE").toString());
			}
			++columnIdx;
		}
	}
	/**
	 * 校验数据是否重复-excel自身数据校验
	 * @param list
	 * @param entity
	 * @return
	 */
	private boolean checkRepeat(List<ScheduleInfo> list,ScheduleInfo entity){
		if(null == list || list.isEmpty() || null == entity){
			return false;
		}
		//异常数据不做比较
		if(ArrFileUtil.isEmpty(entity.getStartTm())
					|| ArrFileUtil.isEmpty(entity.getEndTm())
					|| null == entity.getStartDept()
					|| null == entity.getStartDept().getId()
					|| null == entity.getEndDept()
					|| null == entity.getEndDept().getId()){
			return false;
		}
		for(ScheduleInfo info:list){
			//异常数据不做比较
			if(null == info || ArrFileUtil.isEmpty(info.getStartTm())
					|| ArrFileUtil.isEmpty(info.getEndTm())
					|| null == info.getStartDept()
					|| null == info.getStartDept().getId()
					|| null == info.getEndDept()
					|| null == info.getEndDept().getId()){
				continue;
			}
			//出收车时间、始发目的网点相同-重复
			if(entity.getStartTm().equals(info.getStartTm()) 
					&& entity.getEndTm().equals(info.getEndTm())
					&& entity.getStartDept().getId().compareTo(info.getStartDept().getId()) == 0
					&& entity.getEndDept().getId().compareTo(info.getEndDept().getId()) == 0){
				return true;
			}
		}
		return false;
	}
	/**
	 * 校验数据是否重复-excel自身数据校验
	 * @param list
	 * @param entity
	 * @return
	 */
	private boolean checkCross(List<ScheduleInfo> list,ScheduleInfo entity){
		//异常数据不做比较
		if(null == list || list.isEmpty() || null == entity 
				|| null == entity.getVehicle()
				|| ArrFileUtil.isEmpty(entity.getVehicle().getVehicleCode())
				|| ArrFileUtil.isEmpty(entity.getStartTm())
				|| ArrFileUtil.isEmpty(entity.getEndTm())){
			return false;
		}
		//获取该车的班次信息
		List<ScheduleInfo> vehicleList = new ArrayList<ScheduleInfo>();
		for(ScheduleInfo info:list){
			if(null == info 
					|| null == info.getVehicle()
					|| ArrFileUtil.isEmpty(info.getVehicle().getVehicleCode())){
				continue;
			}
			if(info.getVehicle().getVehicleCode().equals(entity.getVehicle().getVehicleCode())){
				vehicleList.add(info);
			}
		}
		if(null == vehicleList || vehicleList.isEmpty()){
			return false;
		}
		//校验冲突
		for(ScheduleInfo si:vehicleList){
			//异常数据不做比较
			if(null == si
					|| ArrFileUtil.isEmpty(si.getStartTm())
					|| ArrFileUtil.isEmpty(si.getEndTm()) ){
				continue;
			}
			//校验班次时间是否冲突
			if(compareCross(entity.getStartTm(),entity.getEndTm(),si.getStartTm(),si.getEndTm())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 比较出收车时间
	 * 
	 * @param newStart
	 * @param newEnd
	 * @param oldStart
	 * @param oldEnd
	 * @return 相等返回true
	 */
	private boolean compareStartEnd(String newStart, String newEnd) {
		Integer newStartPart1 = Integer.parseInt(newStart.split(":", -1)[0]);
		Integer newStartPart2 = Integer.parseInt(newStart.split(":", -1)[1]);
		Integer newEndPart1 = Integer.parseInt(newEnd.split(":", -1)[0]);
		Integer newEndPart2 = Integer.parseInt(newEnd.split(":", -1)[1]);
		// 出车时间不能等于收车时间
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 比较时间冲突
	 * @param newStart
	 * @param newEnd
	 * @param oldStart
	 * @param oldEnd
	 * @return 有冲突返回true
	 */
	private boolean compareCross(String newStart, String newEnd,
			String oldStart, String oldEnd) {
		Integer newStartPart1 = Integer.parseInt(newStart.split(":", -1)[0]);
		Integer newStartPart2 = Integer.parseInt(newStart.split(":", -1)[1]);
		Integer newEndPart1 = Integer.parseInt(newEnd.split(":", -1)[0]);
		Integer newEndPart2 = Integer.parseInt(newEnd.split(":", -1)[1]);
		Integer oldStartPart1 = Integer.parseInt(oldStart.split(":", -1)[0]);
		Integer oldStartPart2 = Integer.parseInt(oldStart.split(":", -1)[1]);
		Integer oldEndPart1 = Integer.parseInt(oldEnd.split(":", -1)[0]);
		Integer oldEndPart2 = Integer.parseInt(oldEnd.split(":", -1)[1]);
		/** 异常数据不做比较 **/
		if (ArrFileUtil.isEmpty(newStart) || ArrFileUtil.isEmpty(newEnd)
				|| ArrFileUtil.isEmpty(oldStart) || ArrFileUtil.isEmpty(oldEnd)) {
			return false;
		}
		if (!newStart.matches("^\\d{2}:\\d{2}$")
				|| !newEnd.matches("^\\d{2}:\\d{2}$")
				|| !oldStart.matches("^\\d{2}:\\d{2}$")
				|| !oldEnd.matches("^\\d{2}:\\d{2}$")) {
			return false;
		}
		/** 都跨天 **/
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) > 0
				&& compareTm(oldStartPart1, oldStartPart2, oldEndPart1,
						oldEndPart2) > 0) {
			return true;
		}
		/** new跨天 old不跨天 **/
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) > 0
				&& compareTm(oldStartPart1, oldStartPart2, oldEndPart1,
						oldEndPart2) <= 0) {
			// 不合法:1.新的出车时间小于旧的收车时间 (或) 2.新的收车时间必须大于旧的出车时间
			if (compareTm(newStartPart1, newStartPart2, oldEndPart1,
					oldEndPart2) < 0) {
				return true;
			}
			if (compareTm(newEndPart1, newEndPart2, oldStartPart1,
					oldStartPart2) > 0) {
				return true;
			}
			return false;
		}
		/** new不跨天 old跨天 **/
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) <= 0
				&& compareTm(oldStartPart1, oldStartPart2, oldEndPart1,
						oldEndPart2) > 0) {
			// 不合法:1.新的出车时间小于旧的收车时间 (或) 2.新的收车时间必须大于旧的出车时间
			if (compareTm(newStartPart1, newStartPart2, oldEndPart1,
					oldEndPart2) < 0) {
				return true;
			}
			if (compareTm(newEndPart1, newEndPart2, oldStartPart1,
					oldStartPart2) > 0) {
				return true;
			}
			return false;
		}
		/** 都不跨天 **/
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) <= 0
				&& compareTm(oldStartPart1, oldStartPart2, oldEndPart1,
						oldEndPart2) <= 0) {
			// 合法:1.新的出车时间大于等于收车时间 (或) 2.新的收车时间小于等于出车时间
			if (compareTm(newStartPart1, newStartPart2, oldEndPart1,
					oldEndPart2) >= 0) {
				return false;
			}
			if (compareTm(newEndPart1, newEndPart2, oldStartPart1,
					oldStartPart2) <= 0) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 比较两个时间大小
	 * 
	 * @param leftHour
	 * @param leftMinute
	 * @param rightHour
	 * @param rightMinute
	 * @return 
	 */
	private int compareTm(Integer leftHour, Integer leftMinute,
			Integer rightHour, Integer rightMinute) {
		// 比较时钟
		if (leftHour.compareTo(rightHour) > 0) {
			return 1;
		}
		if (leftHour.compareTo(rightHour) < 0) {
			return -1;
		}
		// 时钟相等比较分钟
		if (leftMinute.compareTo(rightMinute) > 0) {
			return 1;
		}
		if (leftMinute.compareTo(rightMinute) < 0) {
			return -1;
		}
		// 时钟分钟都相等返回0
		return 0;
	}
	/**
	 * 获取单元格内容
	 * @param cell
	 * @param format
	 * @return
	 */
	private String getHSSFCellValue(HSSFCell cell){
		String val = null;
		DecimalFormat decimalFormat = new DecimalFormat("#");
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm");
		if( null==cell ){
			return "";
		}
		switch ( cell.getCellType() ){
			case HSSFCell.CELL_TYPE_BOOLEAN:
				val = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				val = cell.getRichStringCellValue().getString(); 
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				val = cell.getRichStringCellValue().getString(); 
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				try {
					if( HSSFDateUtil.isCellInternalDateFormatted(cell) || HSSFDateUtil.isCellDateFormatted(cell) ){	
						val = dateFormat.format(cell.getDateCellValue());
						//double dValue = HSSFDateUtil.getExcelDate(sdf.parse(dd));
						//val = dateFormat.format(HSSFDateUtil.getJavaDate(dValue));
					}else{
						double dValue = cell.getNumericCellValue();
						val = String.valueOf(decimalFormat.format(dValue));
					}
				} catch (Exception e) {
					log.error("",e);
					val = "" ;
				}
				break;
			default:
				val="";
		}
		return val;
	}
	/**
	 * 获取报表模板
	 * @param templateFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private String getTemplatePath()throws Exception{
		String packageName = this.getClass().getPackage().getName();
		String moduleName = packageName.replaceAll("^com.sf.module.", "");
		moduleName = moduleName.substring(0, moduleName.indexOf("."));
		String webRoot = ApplicationContext.getContext().getServletContext().getRealPath("/");
		String tmp_TemplateFile = webRoot + "pages" + File.separator + moduleName 
				+ File.separator + "template" + File.separator + this.TEMPLATE_NAME;
		return tmp_TemplateFile;
	}
	/**
	 * 获取权限部门
	 * @param userDepts
	 * @param deptId
	 * @return
	 */
	private ArrDepartment getArrDepartmentById(List<ArrDepartment> userDepts,Long deptId){
		if(null == userDepts || userDepts.isEmpty() || deptId == null){
			return null;
		}
		for(ArrDepartment dept:userDepts){
			if(null == dept || null == dept.getId()){
				continue;
			}
			if(dept.getId().compareTo(deptId) == 0){
				return dept;
			}
		}
		return null;
	}
	/**
	 * 获取车辆
	 * @param vehicleList
	 * @param vehicleCode
	 * @return
	 */
	private ArrVehicle getVehicleByCode(List<ArrVehicle> vehicleList,String vehicleCode){
		if(null == vehicleList || vehicleList.isEmpty() || ArrFileUtil.isEmpty(vehicleCode)){
			return null;
		}
		for(ArrVehicle v:vehicleList){
			if(null == v || null == v.getVehicleCode()){
				continue;
			}
			if(v.getVehicleCode().equals(vehicleCode)){
				return v;
			}
		}
		return null;
	}
	//为配班查找分页数据
	public IPage<ScheduleInfo> listPageForArrange(Long deptId,Long recordId, String startTm,
			int pageSize, int pageIndex) {
		return scheduleInfoDao.listPageForArrange(deptId,recordId, startTm, pageSize, pageIndex, super.getUserId());
	}
	/**
	 *  修改是否有效
	 * @throws Exception 
	 */
	public void updateValid(Long recordId, Integer valid) throws Exception {
		if(null == recordId){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.40","请选择记录"));
		}
		if(null == valid){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.41","请选择有效性"));
		}
		if(valid.compareTo(1)!=0 && valid.compareTo(0)!=0){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.42","有效性字段取值有误，取值范围:0,1"));
		}
		ScheduleInfo old = this.scheduleInfoDao.load(recordId);
		if(null == old){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.36","记录已经被删除，请刷新页面"));
		}
		if(old.getValid().compareTo(valid)==0){
			return ;
		}
		//修改为有效
		if(valid.compareTo(1) == 0){
			//修改无效-有效：校验重复-不与自己比较
			Integer repeat = scheduleInfoDao.listRepeat(old.getStartTm(),
					old.getEndTm(), old.getStartDept().getId(), old
							.getEndDept().getId(),old.getId());
			if (repeat > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.34","与系统已有数据重复(始发地,目的地,出车时间,收车时间都相同)"));
			}
			//校验时间冲突
			if(null != old.getVehicle() && !ArrFileUtil.isEmpty(old.getVehicle().getVehicleCode())){
				List<ScheduleInfo> list = scheduleInfoDao.listByCode(old.getVehicle().getVehicleCode());
				if (null != list && !list.isEmpty()) {
					for (ScheduleInfo info : list) {
						if (null == info || ArrFileUtil.isEmpty(info.getStartTm())
								|| ArrFileUtil.isEmpty(info.getEndTm())) {
							continue;
						}
						//不与自己比较
						if(info.getId().compareTo(old.getId())==0){
							continue;
						}
						// 时间不能冲突
						if (compareCross(old.getStartTm(), old.getEndTm(),
								info.getStartTm(), info.getEndTm())) {
							throw new BizException(I18nEl.i18n_def_arg2("scheduleinfo.biz.35",
									"线路时间有冲突，该车已经有出车时间={0},收车时间={1}的线路",
									info.getStartTm(), info.getEndTm()));
						}
					}
				}
			}
			//记录日志
			ScheduleInfo entity = new ScheduleInfo();
			entity.setId(old.getId());
			entity.setValid(1);
			LogUtils.getInstance().updateLog(old, entity);
			old.setValid(1);
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			old.setModifiedTm(ArrCommonUtil.currentTm());
		}else{
			//修改为无效
			String arrangeNos = this.scheduleArrangeDao.isExistByInfo(recordId);
			if(null != arrangeNos){
				throw new BizException(I18nEl.i18n_def_arg1("scheduleinfo.biz.43",
						"记录已被配班(班次代码{0})使用，请先修改该配班为无效", arrangeNos));
			}
			//记录日志
			ScheduleInfo entity = new ScheduleInfo();
			entity.setId(recordId);
			entity.setValid(valid);
			LogUtils.getInstance().updateLog(old, entity);
			old.setValid(valid);
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			old.setModifiedTm(ArrCommonUtil.currentTm());
		}
	}
}
