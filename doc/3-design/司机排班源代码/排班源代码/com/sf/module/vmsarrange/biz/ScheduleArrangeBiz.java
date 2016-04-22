/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-6     方芳                        创建
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;

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
import com.sf.module.vmsarrange.dao.IScheduleArrangeJdbcDao;
import com.sf.module.vmsarrange.dao.IScheduleArrangeNoDao;
import com.sf.module.vmsarrange.dao.IScheduleInfoArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleInfoDao;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.domain.ScheduleArrangeNo;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;
import com.sf.module.vmsarrange.log.LogUtils;
import com.sf.module.vmsarrange.util.ArrCommonUtil;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsinfo.util.FileUtil;

/**
 * 
 * 班次信息业务实现类
 * 
 */

public class ScheduleArrangeBiz extends BaseBiz implements IScheduleArrangeBiz {
	private IScheduleArrangeDao scheduleArrangeDao;
	private IScheduleInfoArrangeDao scheduleInfoArrangeDao;
	private IScheduleArrangeNoDao scheduleArrangeNoDao;
	private IScheduleInfoDao scheduleInfoDao;
	private IArrVehicleDao arrVehicleDao;
	private IArrDepartmentDao arrDepartmentDao;
	private IPreScheduleDraftDao preScheduleDraftDao;
	private IScheduleArrangeJdbcDao scheduleArrangeJdbcDao;
	private static int MAX_SIZE = 65530;
	private static String TEMPLATE_NAME = "配班信息模板.xls";
	
	public void setScheduleArrangeJdbcDao(
			IScheduleArrangeJdbcDao scheduleArrangeJdbcDao) {
		this.scheduleArrangeJdbcDao = scheduleArrangeJdbcDao;
	}
	public void setPreScheduleDraftDao(IPreScheduleDraftDao preScheduleDraftDao) {
		this.preScheduleDraftDao = preScheduleDraftDao;
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
	public void setScheduleArrangeNoDao(IScheduleArrangeNoDao scheduleArrangeNoDao) {
		this.scheduleArrangeNoDao = scheduleArrangeNoDao;
	}
	public void setScheduleArrangeDao(IScheduleArrangeDao scheduleArrangeDao) {
		this.scheduleArrangeDao = scheduleArrangeDao;
	}
	public void setScheduleInfoArrangeDao(
			IScheduleInfoArrangeDao scheduleInfoArrangeDao) {
		this.scheduleInfoArrangeDao = scheduleInfoArrangeDao;
	}
	//查询分页数据
	public IPage<ScheduleArrange> listPage(Long deptId, String arrangeNo,
			Integer valid,Integer isUsed, int pageSize, int pageIndex) {
		IPage<ScheduleArrange> data = scheduleArrangeDao.listPage(deptId, arrangeNo, valid,isUsed, pageSize, pageIndex, super.getUserId());
		if(null != data && null != data.getData() && !data.getData().isEmpty()){
			Collection<ScheduleArrange> page = data.getData();
			for(ScheduleArrange arr:page){
				if(null == arr.getScheduleArrangeInfos() || arr.getScheduleArrangeInfos().isEmpty()
						|| null == arr.getArrangeType()){
					continue;
				}
				/**非机动班如果尚未设置最早出车时间、出车网点、收车时间、收车网点，则设置**/
				if(arr.getArrangeType().compareTo(2) == 0 && ArrFileUtil.isEmpty(arr.getStartTm())){
					//1.取班次明细
					List<ScheduleInfo> infos = new ArrayList<ScheduleInfo>();
					for(ScheduleInfoArrange info:arr.getScheduleArrangeInfos()){
						if(null == info || null == info.getScheduleInfo()){
							continue;
						}
						infos.add(info.getScheduleInfo());
					}
					if(null == infos || infos.isEmpty()){
						continue;
					}
					//2.班次明细排序取最早和最晚
					infos = this.sortList(infos);
					int size = infos.size();
					ScheduleInfo first = infos.get(0);
					ScheduleInfo last = infos.get(size-1);
					//3.设置最早最晚出收车时间和网点
					arr.setStartTm(first.getStartTm());
					arr.setStartDept(first.getStartDept());
					arr.setEndTm(last.getEndTm());
					arr.setEndDept(last.getEndDept());
				}
			}
		}
		return data;
	}
	//校验是否超过16个小时
	public String listCheckEntity(ScheduleArrange entity) {
		if(null == entity || null == entity.getInfoArrangeList() || entity.getInfoArrangeList().isEmpty()){
			return "ok";
		}
		//重新获取线路信息
		List<ScheduleInfo> infos = new ArrayList<ScheduleInfo>();
		for(ScheduleInfo info:entity.getInfoArrangeList()){
			if(null == info || null == info.getId()){
				continue;
			}
			ScheduleInfo old = this.scheduleInfoDao.load(info.getId());
			if(null == old || null == old.getStartTm() || null == old.getEndTm()){
				continue;
			}
			infos.add(old);
		}
		//校验是否超过16个小时
		String firstTm;
		String lastTm;
		if(null != infos && !infos.isEmpty()){
			infos = this.sortList(infos);
			int size = infos.size();
			firstTm = infos.get(0).getStartTm();
			lastTm = infos.get(size-1).getEndTm();
			Integer firstPart1 = Integer.parseInt(firstTm.split(":",-1)[0]);
			Integer firstPart2 = Integer.parseInt(firstTm.split(":",-1)[1]);
			Integer lastPart1 = Integer.parseInt(lastTm.split(":",-1)[0]);
			Integer lastPart2 = Integer.parseInt(lastTm.split(":",-1)[1]);
			//出收车时间相等，则为24小时，大于16小时
			if(this.compareTm(lastPart1, lastPart2, firstPart1, firstPart2)==0){
				return "morethan16hours";
			}
			//收车小于出车时间，则跨天
			if(this.compareTm(lastPart1, lastPart2, firstPart1, firstPart2)<0){
				lastPart1 = lastPart1+24;
			}
			//计算时长
			Integer workingTm = (lastPart1-firstPart1)*60+(lastPart2-firstPart2);
			if(workingTm.compareTo(16*60)>0){
				return "morethan16hours";
			}
		}
		return "ok";
	}
	//新增配班数据
	public void saveEntity(ScheduleArrange entity) {
		/**1.校验参数非空**/
		if(null == entity){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		if(null == entity.getDept() || null == entity.getDept().getId() 
				|| ArrFileUtil.isEmpty(entity.getDept().getDeptCode())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.1","网点不能为空"));
		}
		if(null == entity.getValid()){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.18","是否有效不能为空"));
		}
		if(null == entity.getArrangeType()){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.2","班次类型不能为空(是否机动班)"));
		}
		if(null == entity.getArrangeNo()){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.3","班次代码不能为空(请点击重新获取班次代码)"));
		}
		/**2.校验格式正确**/
		if(entity.getValid().compareTo(1) != 0 && entity.getValid().compareTo(0) != 0){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.27","是否有效取值有误，取值范围:0,1"));
		}
		if(entity.getArrangeType().compareTo(1) != 0 && entity.getArrangeType().compareTo(2) != 0){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.4","班次类型取值有误，取值范围:1,2"));
		}
		//1.非机动班班次明细不能为空
		if(entity.getArrangeType().compareTo(2) == 0 
				&& (null == entity.getInfoArrangeList() || entity.getInfoArrangeList().isEmpty())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.5","班次明细数据不能为空"));
		}
		//2.机动班上下班时间不能为空
		if(entity.getArrangeType().compareTo(1) == 0 && ArrFileUtil.isEmpty(entity.getStartTm())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.6","上班时间不能为空"));
		}
		if(entity.getArrangeType().compareTo(1) == 0 && ArrFileUtil.isEmpty(entity.getEndTm())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.7","下班时间不能为空"));
		}
		/*********3.校验数据有效*******/
		/**1.非机动班校验**/
		ScheduleInfoArrange infoArrange ;
		if(entity.getArrangeType().compareTo(2) == 0){
			//1.重新获取并校验班次明细
			List<ScheduleInfo> oldList = new ArrayList<ScheduleInfo>();
			for(ScheduleInfo info:entity.getInfoArrangeList()){
				if(null == info || null == info.getId()){
					throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.8","線路明细有空線路，请重新选择線路"));
				}
				ScheduleInfo old = scheduleInfoDao.load(info.getId());
				if(null == old){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.9"
							,"線路:出车时间={0},收车时间={1}的線路已经被删除，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				if(old.getValid().compareTo(0) == 0){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.10"
							,"線路:出车时间={0},收车时间={1}的線路已被修改为无效線路，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				//手工录入，选择车牌号
				if(old.getDataSource().compareTo(1)==0){
					if(null == info.getVehicle() || ArrFileUtil.isEmpty(info.getVehicle().getVehicleCode())){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.33","手工录入线路，车牌号不能为空，请指定车牌号"));
					}
					ArrVehicle vehicle = this.arrVehicleDao.listByCode(info.getVehicle().getVehicleCode());
					if(null == vehicle || null == vehicle.getId()){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.34","车牌号无效(只取正常、停用车辆)"));
					}
					//1.原记录配车与新配车不一致-则提示
					if(null != old.getVehicle() && null != old.getVehicle().getId()
							&& old.getVehicle().getId().compareTo(vehicle.getId())!=0){
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.35",
								"出车时间为{0}的线路已经配车，不允许重新指定车牌(原车牌号:{1})",
								old.getStartTm(),old.getVehicle().getVehicleCode()));
					}
					if(null == old.getVehicle() || null == old.getVehicle().getId()
							|| old.getVehicle().getId().compareTo(vehicle.getId())!=0){
						//取该车牌号所有有效的线路信息
						List<ScheduleInfo> list = scheduleInfoDao.listByCode(vehicle.getVehicleCode());
						if (null != list && !list.isEmpty()) {
							for (ScheduleInfo i : list) {
								if (null == i || null==i.getId()
										||ArrFileUtil.isEmpty(i.getStartTm())
										|| ArrFileUtil.isEmpty(i.getEndTm())) {
									continue;
								}
								//不与自己比较
								if(old.getId().compareTo(i.getId())==0){
									continue;
								}
								// 校验时间是否冲突
								if (compareCross(old, i)) {
									throw new BizException(I18nEl.i18n_def_arg3("scheduleArrange.biz.36",
													"線路时间有冲突，车辆{0}已经有出车时间={1},收车时间={2}的班次，请重新选择车牌号",
													vehicle.getVehicleCode(),
													i.getStartTm(), 
													i.getEndTm()));
								}
							}
						}
						old.setVehicle(vehicle);
					}
				}
				//当前记录有效：1.校验线路未使用2.设置状态为已使用(1) 当前记录无效:1.设置状态为未使用-不允许修改(2)
				if(entity.getValid().compareTo(1) == 0){
					//有效则校验是否已经被使用
					if(old.getIsUsed().compareTo(1) == 0){
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.11"
								,"線路:出车时间={0},收车时间={1}的線路已经被其他配班记录使用，请重新选择",info.getStartTm(),info.getEndTm()));
					}
					//有效-已配班
					old.setIsUsed(1);
					old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					old.setModifiedTm(ArrCommonUtil.currentTm());
				}else{
					//无效-未配班-不允许修改
					if(old.getIsUsed().compareTo(0) == 0){
						old.setIsUsed(2);
						old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
						old.setModifiedTm(ArrCommonUtil.currentTm());
					}
				}
				oldList.add(old);
			}
			//2.校验班次时间是否冲突
			for(ScheduleInfo info:oldList){
				for(ScheduleInfo other:oldList){
					//不与自己比较
					if(info.getId().compareTo(other.getId()) == 0){
						continue;
					}
					if (compareCross(info, other)) {
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.12"
								,"出车时间={0},收车时间={1}的線路与其他線路时间有冲突",info.getStartTm(), info.getEndTm()));
					}
				}
				infoArrange = new ScheduleInfoArrange();
				infoArrange.setScheduleInfo(info);
				//有效-则添加唯一约束
				if(entity.getValid().compareTo(1) == 0){
					infoArrange.setInfoIdBak(info.getId());
				}
				entity.getScheduleArrangeInfos().add(infoArrange);
			}
		}
		/**2.机动班校验**/
		if(entity.getArrangeType().compareTo(1) == 0){
			//1.校验上班时间格式
			Integer hourPartStart = Integer.parseInt(entity.getStartTm().split(":",
					-1)[0]);
			Integer minutePartStart = Integer.parseInt(entity.getStartTm().split(
					":", -1)[1]);
			if (hourPartStart.compareTo(0) < 0 || hourPartStart.compareTo(23) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.13","上班时间:时钟不能小于0大于23"));
			}
			if (minutePartStart.compareTo(0) < 0
					|| minutePartStart.compareTo(59) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.14","上班时间:分钟不能小于0大于59"));
			}
			//2.校验下班时间格式
			Integer hourPartEnd = Integer
					.parseInt(entity.getEndTm().split(":", -1)[0]);
			Integer minutePartEnd = Integer.parseInt(entity.getEndTm().split(":",
					-1)[1]);
			if (hourPartEnd.compareTo(0) < 0 || hourPartEnd.compareTo(23) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.15","下班时间:时钟不能小于0大于23"));
			}
			if (minutePartEnd.compareTo(0) < 0 || minutePartEnd.compareTo(59) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.16","下班时间:分钟不能小于0大于59"));
			}
			//3.上班时间不能大于等于下班时间
			if (compareStartEnd(entity.getStartTm(), entity.getEndTm())) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.17","上班时间不能等于下班时间"));
			}
			//4.数据不能重复
			if(entity.getValid().compareTo(1) == 0){
				Integer repeat = scheduleArrangeDao.listRepeat(entity.getStartTm(), entity.getEndTm(), entity.getDept().getId(),null);
				if(repeat > 0){
					throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.32","与系统已有数据重复(上下班时间、网点均相同)"));
				}
			}
		}
		/****4.保存数据**/
		//2.保存配班数据
		entity.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
		entity.setCreatedTm((new Date()));
		scheduleArrangeDao.save(entity);
	}
	//新增配班数据
	private void saveEntity(ScheduleArrange entity,ScheduleArrange oldArr) {
		/**1.校验参数非空**/
		if(null == entity){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		if(null == entity.getValid()){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.18","是否有效不能为空"));
		}
		if(null == entity.getArrangeNo()){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.37","正在获取班次代码，请稍后再试"));
		}
		entity.setDept(oldArr.getDept());
		entity.setArrangeType(2);
		/**2.校验格式正确**/
		if(entity.getValid().compareTo(1) != 0 && entity.getValid().compareTo(0) != 0){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.27","是否有效取值有误，取值范围:0,1"));
		}
		//1.非机动班班次明细不能为空
		if(null == entity.getInfoArrangeList() || entity.getInfoArrangeList().isEmpty()){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.5","班次明细数据不能为空"));
		}
		/*********3.校验数据有效*******/
		/**1.非机动班校验**/
		ScheduleInfoArrange infoArrange ;
		if(entity.getArrangeType().compareTo(2) == 0){
			//1.重新获取并校验班次明细
			List<ScheduleInfo> oldList = new ArrayList<ScheduleInfo>();
			for(ScheduleInfo info:entity.getInfoArrangeList()){
				if(null == info || null == info.getId()){
					throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.8","班次明细有空班次，请重新选择班次"));
				}
				ScheduleInfo old = scheduleInfoDao.load(info.getId());
				if(null == old){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.9"
							,"班次:出车时间={0},收车时间={1}的班次已经被删除，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				if(old.getValid().compareTo(0) == 0){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.10"
							,"班次:出车时间={0},收车时间={1}的班次已被修改为无效班次，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				//手工录入，选择车牌号
				if(old.getDataSource().compareTo(1)==0){
					if(null == info.getVehicle() || ArrFileUtil.isEmpty(info.getVehicle().getVehicleCode())){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.33","手工录入线路，车牌号不能为空，请指定车牌号"));
					}
					ArrVehicle vehicle = this.arrVehicleDao.listByCode(info.getVehicle().getVehicleCode());
					if(null == vehicle || null == vehicle.getId()){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.34","车牌号无效(只取正常、停用车辆)"));
					}
					//1.原记录配车与新配车不一致-则提示
					if(null != old.getVehicle() && null != old.getVehicle().getId()
							&& old.getVehicle().getId().compareTo(vehicle.getId())!=0){
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.35",
								"出车时间为{0}的线路已经配车，不允许重新指定车牌(原车牌号:{1})",
								old.getStartTm(),old.getVehicle().getVehicleCode()));
					}
					//1.原记录没有配车 2.原记录配车与新配车不一致-则校验时间冲突并保存新车牌号
					if(null == old.getVehicle() || null == old.getVehicle().getId()
							|| old.getVehicle().getId().compareTo(vehicle.getId())!=0){
						//取该车牌号所有有效的线路信息
						List<ScheduleInfo> list = scheduleInfoDao.listByCode(vehicle.getVehicleCode());
						if (null != list && !list.isEmpty()) {
							for (ScheduleInfo i : list) {
								if (null == i || null==i.getId()
										||ArrFileUtil.isEmpty(i.getStartTm())
										|| ArrFileUtil.isEmpty(i.getEndTm())) {
									continue;
								}
								//不与自己比较
								if(old.getId().compareTo(i.getId())==0){
									continue;
								}
								// 校验时间是否冲突
								if (compareCross(old, i)) {
									throw new BizException(I18nEl.i18n_def_arg3("scheduleArrange.biz.36",
											"时间有冲突，车辆{0}已经有出车时间={1},收车时间={2}的线路，请重新选择车牌号",
											vehicle.getVehicleCode(),
											i.getStartTm(),
											i.getEndTm()));
								}
							}
						}
						old.setVehicle(vehicle);
					}
				}
				//当前记录有效或手工录入：1.校验线路未使用2.设置状态为已使用(1) 当前记录无效:1.设置状态为未使用-不允许修改(2)
				if(entity.getValid().compareTo(1) == 0){
					//有效则校验是否已经被使用
					if(old.getIsUsed().compareTo(1) == 0){
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.11"
								,"线路:出车时间={0},收车时间={1}的线路已经被其他配班记录使用，请重新选择",info.getStartTm(),info.getEndTm()));
					}
					//有效-已配班
					old.setIsUsed(1);
					old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					old.setModifiedTm(ArrCommonUtil.currentTm());
				}else{
					//无效-未配班-不允许修改
					if(old.getIsUsed().compareTo(0) == 0){
						old.setIsUsed(2);
						old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
						old.setModifiedTm(ArrCommonUtil.currentTm());
					}
				}
				oldList.add(old);
			}
			//2.校验班次时间是否冲突
			for(ScheduleInfo info:oldList){
				for(ScheduleInfo other:oldList){
					//不与自己比较
					if(info.getId().compareTo(other.getId()) == 0){
						continue;
					}
					if (compareCross(info, other)) {
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.12"
								,"出车时间={0},收车时间={1}的线路与其他线路时间有冲突",info.getStartTm(), info.getEndTm()));
					}
				}
				infoArrange = new ScheduleInfoArrange();
				infoArrange.setScheduleInfo(info);
				//有效-则添加唯一约束
				if(entity.getValid().compareTo(1) == 0){
					infoArrange.setInfoIdBak(info.getId());
				}
				entity.getScheduleArrangeInfos().add(infoArrange);
			}
		}
		/****4.保存数据**/
		//2.保存配班数据
		entity.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
		entity.setCreatedTm((new Date()));
		scheduleArrangeDao.save(entity);
	}
	//修改配班数据
	public void updateEntity(ScheduleArrange entity) throws Exception {
		/**1.校验参数非空**/
		if(null == entity || null == entity.getId()){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		ScheduleArrange old = scheduleArrangeDao.load(entity.getId()) ;
		if(null == old){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.36","记录已经被删除，请刷新页面"));
		}
		if(ArrFileUtil.isEmpty(entity.getArrangeNo())){
			entity.setArrangeNo(old.getArrangeNo());
		}
		/**2.如果班次代码有改变，则认为新增记录***/
		if(!entity.getArrangeNo().equals(old.getArrangeNo())){
			//保存新的配班
			entity.setId(null);
			this.saveEntity(entity,old);
			return ;
		}
		if(old.getIsUsed().compareTo(1) == 0){
			throw new BizException("状态为已排班的记录不允许修改(请刷新页面，重新选择)");
		}
		if(null == entity.getValid()){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.18","是否有效不能为空"));
		}
		/**2.校验格式正确**/
		if(entity.getValid().compareTo(1) != 0 && entity.getValid().compareTo(0) != 0){
			throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.27","是否有效取值有误，取值范围:0,1"));
		}
		//1.非机动班班次明细不能为空
		if(old.getArrangeType().compareTo(2) == 0 
				&& (null == entity.getInfoArrangeList() || entity.getInfoArrangeList().isEmpty())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.5","班次明细数据不能为空"));
		}
		//2.机动班出收车时间不能为空
		if(old.getArrangeType().compareTo(1) == 0 && ArrFileUtil.isEmpty(entity.getStartTm())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.6","上班时间不能为空"));
		}
		if(old.getArrangeType().compareTo(1) == 0 && ArrFileUtil.isEmpty(entity.getEndTm())){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.7","下班时间不能为空"));
		}
		/***********3.校验数据有效**********/
		/**1.非机动班校验**/
		ScheduleInfoArrange infoArrange;
		String removeContent = "remove:";
		String addContent = "add:";
		if(old.getArrangeType().compareTo(2) == 0){
			//1.原数据有异常、不处理
			if(null == old.getScheduleArrangeInfos() || old.getScheduleArrangeInfos().isEmpty()){
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.30","记录异常(没有班次明细数据)"));
			}
			//2.校验班次时间是否冲突
			for(ScheduleInfo info:entity.getInfoArrangeList()){
				for(ScheduleInfo other:entity.getInfoArrangeList()){
					//不与自己比较
					if(info.getId().compareTo(other.getId()) == 0){
						continue;
					}
					if (compareCross(info, other)) {
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.12"
								,"出车时间={0},收车时间={1}的班次与其他班次时间有冲突",info.getStartTm(), info.getEndTm()));
					}
				}
			}
			//3.校验班次存在、有效
			List<ScheduleInfo> allInfoNew = new ArrayList<ScheduleInfo>();
			for(ScheduleInfo info:entity.getInfoArrangeList()){
				ScheduleInfo oldInfo = scheduleInfoDao.load(info.getId());
				if(null == oldInfo){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.9"
							,"班次:出车时间={0},收车时间={1}的班次已经被删除，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				if(oldInfo.getValid().compareTo(0) == 0){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.10"
							,"班次:出车时间={0},收车时间={1}的班次已被修改为无效班次，请重新选择",info.getStartTm(),info.getEndTm()));
				}
				//手工录入，选择车牌号
				if(oldInfo.getDataSource().compareTo(1)==0){
					if(null == info.getVehicle() || ArrFileUtil.isEmpty(info.getVehicle().getVehicleCode())){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.33","手工录入线路，车牌号不能为空，请指定车牌号"));
					}
					ArrVehicle vehicle = this.arrVehicleDao.listByCode(info.getVehicle().getVehicleCode());
					if(null == vehicle){
						throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.34","车牌号无效(只取正常、停用车辆)"));
					}
					//1.原记录配车与新配车不一致-则提示
					if(null != oldInfo.getVehicle() && null != oldInfo.getVehicle().getId()
							&& oldInfo.getVehicle().getId().compareTo(vehicle.getId())!=0){
						//判断是否只有本班次
						Integer count = this.scheduleInfoArrangeDao.listByArrangeAndInfo(old.getId(),oldInfo.getId());
						if(count > 0){
							throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.35",
									"出车时间为{0}的线路已经配车，不允许重新指定车牌(原车牌号:{1})",
									oldInfo.getStartTm(),
									oldInfo.getVehicle().getVehicleCode()));
						}
					}
					//1.原记录没有配车 2.原记录配车与新配车不一致-则校验时间冲突并保存新车牌号
					if(null == oldInfo.getVehicle() || null == oldInfo.getVehicle().getId()
							|| oldInfo.getVehicle().getId().compareTo(vehicle.getId())!=0){
						//取该车牌号所有有效的线路信息
						List<ScheduleInfo> list = scheduleInfoDao.listByCode(vehicle.getVehicleCode());
						if (null != list && !list.isEmpty()) {
							for (ScheduleInfo i : list) {
								if (null == i || null == i.getId() 
										|| ArrFileUtil.isEmpty(i.getStartTm())
										|| ArrFileUtil.isEmpty(i.getEndTm())) {
									continue;
								}
								//不与自己比较
								if(oldInfo.getId().compareTo(i.getId()) == 0){
									continue;
								}
								// 校验时间不能冲突
								if (compareCross(oldInfo, i)) {
									throw new BizException(I18nEl.i18n_def_arg3("scheduleArrange.biz.36",
											"时间有冲突，车辆{0}已经有出车时间={1},收车时间={2}的线路，请重新选择车牌号",
											vehicle.getVehicleCode(),
											i.getStartTm(), i.getEndTm()));
								}
							}
						}
						oldInfo.setVehicle(vehicle);
					}
				}
				//无效->有效，校验班次是否已经被使用
				if(old.getValid().compareTo(0) == 0 && entity.getValid().compareTo(1)==0){
					if(oldInfo.getIsUsed().compareTo(1) == 0){
						throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.11"
								,"线路:出车时间={0},收车时间={1}的线路已经被其他配班记录使用，请重新选择",info.getStartTm(),info.getEndTm()));
					}
				}
				allInfoNew.add(oldInfo);
			}
			//4.遍历获取已释放班次
			List<ScheduleInfoArrange> removedOld = new ArrayList<ScheduleInfoArrange>();
			for(ScheduleInfoArrange o:old.getScheduleArrangeInfos()){
				if(null == o){
					continue;
				}
				//标识是否释放班次
				boolean flg = true;
				for(ScheduleInfo info:allInfoNew){
					if(null == info){
						continue;
					}
					if(o.getScheduleInfo().getId().compareTo(info.getId()) == 0){
						flg = false;
						break;
					}
				}
				//添加释放班次到集合
				if(flg){
					ScheduleInfo oldInfo = scheduleInfoDao.load(o.getScheduleInfo().getId());
					if(null != oldInfo){
						//查询是否被其他班次使用
						String arrangeNos = this.scheduleArrangeDao.isExistByInfoId(o.getScheduleInfo().getId(),old.getId());
						//设置未使用-原来被有效记录使用
						if(old.getValid().compareTo(1)==0){
							if(oldInfo.getIsUsed().compareTo(1)==0){
								if(ArrFileUtil.isEmpty(arrangeNos)){
									oldInfo.setIsUsed(0);
									oldInfo.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
									oldInfo.setModifiedTm(ArrCommonUtil.currentTm());
								}else{
									oldInfo.setIsUsed(2);
									oldInfo.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
									oldInfo.setModifiedTm(ArrCommonUtil.currentTm());
								}
							}
						}else{
							//原来为无效-没有被其他班次使用则置为未配班
							if(oldInfo.getIsUsed().compareTo(2)==0){
								if(ArrFileUtil.isEmpty(arrangeNos)){
									oldInfo.setIsUsed(0);
									oldInfo.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
									oldInfo.setModifiedTm(ArrCommonUtil.currentTm());
								}
							}
						}
						//没有被其他班次使用，则置空车牌号
						if(ArrFileUtil.isEmpty(arrangeNos) && oldInfo.getDataSource().compareTo(1)==0){
							oldInfo.setVehicle(null);
						}
						//记录日志
						removeContent += o.getScheduleInfo().getId()+";";
					}
					removedOld.add(o);
				}
			}
			//5.移除释放的班次
			if(null != removedOld && !removedOld.isEmpty()){
				old.getScheduleArrangeInfos().removeAll(removedOld);
			}
			//6.遍历获取新增的班次
			List<ScheduleInfoArrange> addNew = new ArrayList<ScheduleInfoArrange>();
			for(ScheduleInfo info:allInfoNew){
				if(null == info || null == info.getId()){
					throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.8","班次明细有空班次，请重新选择班次"));
				}
				//标识是否为新增的班次
				boolean flg = true;
				for(ScheduleInfoArrange o:old.getScheduleArrangeInfos()){
					if(null == o){
						continue;
					}
					if(o.getScheduleInfo().getId().compareTo(info.getId()) == 0){
						flg = false;
						break;
					}
				}
				//校验新增的班次
				if(flg){
					//记录日志
					addContent += info.getId()+";";
					//转换新增班次为：班次配班关系实体
					infoArrange = new ScheduleInfoArrange();
					infoArrange.setScheduleInfo(info);
					addNew.add(infoArrange);
				}
			}
			//6.添加新增的班次
			if(null != addNew && !addNew.isEmpty()){
				old.getScheduleArrangeInfos().addAll(addNew);
			}
			//7.遍历所有班次，设置使用状态和唯一约束
			boolean validFlg = (entity.getValid().compareTo(1)==0);
			for(ScheduleInfoArrange o:old.getScheduleArrangeInfos()){
				if(null == o || null == o.getScheduleInfo()){
					continue;
				}
				//有效添加唯一约束
				if(validFlg){
					o.setInfoIdBak(o.getScheduleInfo().getId());
				}else{
					o.setInfoIdBak(null);
				}
				//当前记录有效：设置已使用 、否则设置未使用-不允许修改
				if(null != o.getScheduleInfo()){
					//无效-有效、有效-有效
					if(validFlg){
						o.getScheduleInfo().setIsUsed(1);
						o.getScheduleInfo().setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
						o.getScheduleInfo().setModifiedTm(ArrCommonUtil.currentTm());
					}else{
						//无效-无效
						if(old.getValid().compareTo(0)==0 && o.getScheduleInfo().getIsUsed().compareTo(0)==0){
							o.getScheduleInfo().setIsUsed(2);
							o.getScheduleInfo().setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
							o.getScheduleInfo().setModifiedTm(ArrCommonUtil.currentTm());
						}
						//有效-无效
						if(old.getValid().compareTo(1)==0 
								&&(o.getScheduleInfo().getIsUsed().compareTo(0)==0 
										|| o.getScheduleInfo().getIsUsed().compareTo(1)==0)){
							o.getScheduleInfo().setIsUsed(2);
							o.getScheduleInfo().setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
							o.getScheduleInfo().setModifiedTm(ArrCommonUtil.currentTm());
						}
					}
				}
			}
			//9.记录日志
			entity.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			entity.setModifiedTm((new Date()));
			entity.setArrangeNo(old.getArrangeNo());
			String modifiedInfo = removeContent+addContent;
			if(modifiedInfo.getBytes().length > 3000){
				modifiedInfo = modifiedInfo.substring(0,1000);
			}
			entity.setModifiedInfo(modifiedInfo);
			LogUtils.getInstance().updateLog(old, entity);
			//9.保存数据-班次明细已与上面保存
			old.setValid(entity.getValid());
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			old.setModifiedTm((new Date()));
		}
		/**2.机动班校验**/
		if(old.getArrangeType().compareTo(1) == 0){
			//1.校验上班时间格式
			Integer hourPartStart = Integer.parseInt(entity.getStartTm().split(":",
					-1)[0]);
			Integer minutePartStart = Integer.parseInt(entity.getStartTm().split(
					":", -1)[1]);
			if (hourPartStart.compareTo(0) < 0 || hourPartStart.compareTo(23) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.13","上班时间:时钟不能小于0大于23"));
			}
			if (minutePartStart.compareTo(0) < 0
					|| minutePartStart.compareTo(59) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.14","上班时间:分钟不能小于0大于59"));
			}
			//2.校验下班时间格式
			Integer hourPartEnd = Integer
					.parseInt(entity.getEndTm().split(":", -1)[0]);
			Integer minutePartEnd = Integer.parseInt(entity.getEndTm().split(":",
					-1)[1]);
			if (hourPartEnd.compareTo(0) < 0 || hourPartEnd.compareTo(23) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.15","下班时间:时钟不能小于0大于23"));
			}
			if (minutePartEnd.compareTo(0) < 0 || minutePartEnd.compareTo(59) > 0) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.16","下班时间:分钟不能小于0大于59"));
			}
			//3.上班时间不能大于等于下班时间
			if (compareStartEnd(entity.getStartTm(), entity.getEndTm())) {
				throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.17","上班时间不能等于下班时间"));
			}
			//4.数据不能重复
			if(entity.getValid().compareTo(1) == 0){
				Integer repeat = scheduleArrangeDao.listRepeat(entity.getStartTm(), entity.getEndTm(), old.getDept().getId(),old.getId());
				if(repeat > 0){
					throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.32","与系统已有数据重复(上下班时间、网点均相同)"));
				}
			}
			//5.记录日志
			entity.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			entity.setModifiedTm((new Date()));
			entity.setArrangeNo(old.getArrangeNo());
			LogUtils.getInstance().updateLog(old, entity);
			//6.保存数据
			old.setStartTm(entity.getStartTm());
			old.setEndTm(entity.getEndTm());
			old.setValid(entity.getValid());
			old.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			old.setModifiedTm((new Date()));
		}
	}
	//导入配班数据
	public void saveFile(File uploadFile, String fileName) {
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
	
	//导出报表
	@SuppressWarnings("rawtypes")
	public String listReport(Long deptId, String arrangeNo, Integer valid,Integer isUsed) {
		String userCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		if(null == deptId){
			throw new BizException(I18nEl.i18n_def("common.biz.deptNull","查询条件网点代码不能为空"));
		}
		String fileName = null;
		if(FileUtil.isEmpty(userCode)){
			fileName = "配班信息.xls";
		}else{
			fileName = "配班信息("+userCode+").xls";
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
		HSSFSheet sheet = workbook.getSheetAt(0);
		if(sheet==null){
			throw new BizException(I18nEl.i18n_def("common.biz.firstSheetNull","生成报表失败，模板中第一个sheet页为空"));
		}
		/**单sheet页**/
		//获取记录总数
		Long size = scheduleArrangeDao.listReportCount(deptId, arrangeNo, valid,isUsed,userId);
		if(null != size && size.compareTo(Long.valueOf(MAX_SIZE)) > 0){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.31","数据过多(超过65530条)，请选择查询条件过滤数据"));
		}
		//获取记录
		List<Map> data = scheduleArrangeDao.listReport(deptId, arrangeNo, valid,isUsed, userId);
		if(null != data && !data.isEmpty()){
			//填充数据
			fillData(data,sheet);
		}
		String savePath = null;
		try {
			//获取普通报表的保存路径
			savePath = ArrCommonUtil.getGeneralReportSaveDir("ScheduleArrangeBiz", "vmsarrange");
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
	//生成班次代码
	public String saveArrangeNo(String deptCode,int arrangeType) throws Exception{
		long maxNo = scheduleArrangeNoDao.listMaxNo(deptCode, arrangeType);
		String arrangeNo = this.generateArrangeNo(maxNo, deptCode, arrangeType);
		List<ScheduleArrangeNo> list = scheduleArrangeNoDao.listByCodeAndType(deptCode, arrangeType);
		if(null == list || list.isEmpty()){
			//没有记录则新增一条
			ScheduleArrangeNo scheduleNo = new ScheduleArrangeNo();
			scheduleNo.setDeptCode(deptCode);
			scheduleNo.setNo(maxNo+1);
			scheduleNo.setType(arrangeType);
			scheduleArrangeNoDao.save(scheduleNo);
			scheduleNo.setOperEmpCode(this.getCurrentUser().getEmployee().getCode());
			scheduleNo.setOperTm((new Date()));
			LogUtils.getInstance().insertLog(scheduleNo);
		}else{
			//有记录则全部更新
			boolean flg = true;
			for(ScheduleArrangeNo n:list){
				if(null == n){
					continue;
				}
				n.setNo(maxNo+1);
				//只记一条日志
				if(flg){
					flg = false;
					n.setOperEmpCode(this.getCurrentUser().getEmployee().getCode());
					n.setOperTm((new Date()));
					LogUtils.getInstance().insertLog(n);
				}
			}
		}
		return arrangeNo;
	}
	//读取导入sheet页
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readSheet(HSSFSheet sheet) {	
		String createdEmpCode = this.getCurrentUser().getEmployee().getCode();
		Long userId = this.getUserId();
		final int rightColNum = 8;
		final int startRow = 1;
		//检验表格是否为空
		if( null == sheet ){
			throw new BizException(I18nEl.i18n_def("common.biz.sheetNull","文件填写有误，第一个sheet页为空"));
		}
		//转换sheet数据成list map对象集合
		List<Map<String,Map>> datas = new ArrayList<Map<String,Map>>();	
		if(sheet.getRow(0)==null){
			throw new BizException(I18nEl.i18n_def("common.biz.titleNull","文件填写有误，第一行(标题行)为空"));
		}
		//判断列数是否正确
		if(sheet.getRow(0).getLastCellNum() < rightColNum){
			throw new BizException(I18nEl.i18n_def_arg1("common.biz.titleColumnError","文件填写有误,第一行(标题行)列数有误，正确列数为{0}列"
					,rightColNum));
		}
		int mergedIdx = -1;
		/*****1.转换数据为list*****/
		try {
			// 获取对应的数据行,不去除空行数据
			Map headerMap = new HashMap<String,Object>();
			List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
			// 标志是否不为空行
			int dataIdx = 0;
			for(int i=startRow;i<=sheet.getLastRowNum();i++){
				HSSFRow row = sheet.getRow(i);
				//过滤空行
				if(null == row){
					continue;
				}
				int colIdx = 0;
				String value = null;
				//序号(组合列)
				mergedIdx = isMergedRegion(sheet,i,colIdx);
				int lineNoRange = getMergedRangeRow(sheet,i,colIdx);
				if(mergedIdx != -1){
					//合并单元格
					value = getMergedCellValue(sheet,i,colIdx);
				}else{
					//非合并单元格
					value = getHSSFCellValue(row.getCell(colIdx));
				}
				if(ArrFileUtil.isEmpty(value)){
					throw new BizException(I18nEl.i18n_def_arg2("scheduleArrange.biz.19"
							,"序号列不能为空,行号(此处指excel自带行号):{0},提示:本sheet页共有{1}行(如尾部有空行，请自行删除)",(i+1),
							sheet.getLastRowNum()+1));
				}
				headerMap.put("lineNo", value);
				colIdx++;
				//班次代码(组合列)
				int arrangeNoRange = getMergedRangeRow(sheet,i,colIdx);
				if(arrangeNoRange != lineNoRange){
					throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.20"
							,"序号和班次代码合并的行数不相同,行号(此处指excel自带行号):{0}",(i+1)));
				}
				if(-1 != isMergedRegion(sheet,i,colIdx)){
					value = getMergedCellValue(sheet,i,colIdx);
				}else{
					value = getHSSFCellValue(row.getCell(colIdx));
				}
				headerMap.put("arrangeNo", value);
				colIdx++;
				//归属网点(组合列)
				int deptRange = getMergedRangeRow(sheet,i,colIdx);
				if(deptRange != arrangeNoRange){
					throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.21"
							,"归属网点和班次代码合并的行数不相同,行号(此处指excel自带行号):{0}",(i+1)));
				}
				if(-1 != isMergedRegion(sheet,i,colIdx)){
					value = getMergedCellValue(sheet,i,colIdx);
				}else{
					value = getHSSFCellValue(row.getCell(colIdx));
				}
				headerMap.put("deptCode", value);
				colIdx++;
				if(mergedIdx != -1){
					//合并单元格:一个配班有多笔线路
					while(i<=sheet.getLastRowNum()){
						int nextMergedIdx = isMergedRegion(sheet,i,0);
						int startColumnIdx = 3;
						HSSFRow mergeRow = sheet.getRow(i);
						if(null == mergeRow){
							mergeRow = sheet.createRow(i);
						}
						//下一个单元格与本单元格不是同一个合并单元格，则为下一个配班
						if(nextMergedIdx != mergedIdx){
							--i;
							break;
						}
						//下一个单元格与本单元格是同一个合并单元格，则取其线路信息
						if(nextMergedIdx == mergedIdx){
							Map<String,String> m = new HashMap<String,String>();
							//出车时间
							value = getHSSFCellValue(mergeRow.getCell(startColumnIdx));
							m.put("startTm",value);
							startColumnIdx++;
							//收车时间
							value = getHSSFCellValue(mergeRow.getCell(startColumnIdx));
							m.put("endTm",value);
							startColumnIdx++;
							//始发网点
							value = getHSSFCellValue(mergeRow.getCell(startColumnIdx));
							if(!ArrFileUtil.isEmpty(value)){
								if(null != mergeRow.getCell(startColumnIdx) 
										&& mergeRow.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
									throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.22"
											,"文件填写有误,第{0}行始发网点格式有误(必须为文本格式)",(i+1)));
								}
							}
							m.put("startDeptCode",value);
							startColumnIdx++;
							//目的网点
							value = getHSSFCellValue(mergeRow.getCell(startColumnIdx));
							if(!ArrFileUtil.isEmpty(value)){
								if(null != mergeRow.getCell(startColumnIdx) 
										&& mergeRow.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
									throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.23"
											,"文件填写有误,第{0}行目的网点格式有误(必须为文本格式)",(i+1)));
								}
							}
							m.put("endDeptCode",value);
							startColumnIdx++;
							//车牌号
							value = getHSSFCellValue(mergeRow.getCell(startColumnIdx));
							if(!ArrFileUtil.isEmpty(value)){
								if(null != mergeRow.getCell(startColumnIdx) 
										&& mergeRow.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
									throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.24"
											,"文件填写有误,第{0}行指定车牌号格式有误(必须为文本格式)",(i+1)));
								}
							}
							m.put("vehicleCode",value);
							startColumnIdx++;
							++dataIdx;
							dataList.add(m);
							if(dataIdx > 10000){
								throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.38","数据量过大(超出1万条)，请分批导入"));
							}
						}
						++i;
					}
					//线路记录中放入线路明细数据
					headerMap.put("data", dataList);
					//添加线路记录到线路集合
					datas.add(headerMap);
					headerMap = new HashMap<String,Object>();
					dataList = new ArrayList<Map<String,String>>();
				}else{
					int startColumnIdx = 3;
					Map<String,String> m = new HashMap<String,String>();
					//出车时间
					value = getHSSFCellValue(row.getCell(startColumnIdx));
					m.put("startTm",value);
					startColumnIdx++;
					//收车时间
					value = getHSSFCellValue(row.getCell(startColumnIdx));
					m.put("endTm",value);
					startColumnIdx++;
					//始发网点
					value = getHSSFCellValue(row.getCell(startColumnIdx));
					if(!ArrFileUtil.isEmpty(value)){
						if(null != row.getCell(startColumnIdx) 
								&& row.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
							throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.22"
									,"文件填写有误,第{0}行始发网点格式有误(必须为文本格式)",(i+1)));
						}
					}
					m.put("startDeptCode",value);
					startColumnIdx++;
					//目的网点
					value = getHSSFCellValue(row.getCell(startColumnIdx));
					if(!ArrFileUtil.isEmpty(value)){
						if(null != row.getCell(startColumnIdx) 
								&& row.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
							throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.23"
									,"文件填写有误,第{0}行目的网点格式有误(必须为文本格式)",(i+1)));
						}
					}
					m.put("endDeptCode",value);
					startColumnIdx++;
					//车牌号
					value = getHSSFCellValue(row.getCell(startColumnIdx));
					if(!ArrFileUtil.isEmpty(value)){
						if(null != row.getCell(startColumnIdx) 
								&& row.getCell(startColumnIdx).getCellType() != HSSFCell.CELL_TYPE_STRING){
							throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.24"
									,"文件填写有误,第{0}行指定车牌号格式有误(必须为文本格式)",(i+1)));
						}
					}
					m.put("vehicleCode",value);
					startColumnIdx++;
					++dataIdx;
					dataList.add(m);
					if(dataIdx > 10000){
						throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.38","数据量过大(超出1万条)，请分批导入"));
					}
					headerMap.put("data", dataList);
					datas.add(headerMap);
					headerMap = new HashMap<String,Object>();
					dataList = new ArrayList<Map<String,String>>();
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
		/*****2.校验数据*****/
		int wrongNum = 0;
		//记录提示信息
		StringBuilder wrongMsg = new StringBuilder();
		//线路明细各属性
		String lineNo = null,arrangeNo = null,deptCode = null,startTm,endTm,startDeptCode,endDeptCode,vehicleCode;
		
		//配班记录集合-存放需要保存的记录
		List<ScheduleArrange> saveList = new ArrayList<ScheduleArrange>();
		
		//所有配班的班次代码集合-用于校验班次代码是否重复
		List<String> arrangeNoList = new ArrayList<String>();
		//所有配班的线路明细集合-用于校验线路明细是否重复
		List<ScheduleInfo> usedInfoList = new ArrayList<ScheduleInfo>();
		//单个配班的线路明细集合-用于校验时间交叉
		List<ScheduleInfo> myInfoList;
		
		//配班实体
		ScheduleArrange arrange;
		//配班对应的线路明细关系集合-用于线路明细实体与配班实体的转换
		List<ScheduleInfoArrange> infoArrangeList;
		//线路明细实体-用与转换map为实体
		ScheduleInfo infoEntity = null;
		
		//缓存有权限部门:admin不缓存
		List<ArrDepartment> userDepts = null;
		if(userId.compareTo(1L) != 0){
			userDepts = arrDepartmentDao.listAllDeptByUser(userId);
		}
		
		/**1.与自身比较**/
		for(int rowIndex = 0;rowIndex<datas.size();rowIndex++){
			/**错误行数超过100行，退出校验**/
			if(wrongNum >= 100){
				wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
				break;
			}
			java.util.Map<String,Map> map = datas.get(rowIndex);
			if(null == map){
				continue;
			}
			/**获取数据**/
			//获取序号
			Object ln = map.get("lineNo");
			if(null != ln){
				lineNo = String.format("%s", ln);
			}
			//获取班次代码
			Object an = map.get("arrangeNo");
			if(null != an){
				arrangeNo = String.format("%s", an);
			}
			//获取归属网点
			Object dc = map.get("deptCode");
			if(null != dc){
				deptCode = String.format("%s", dc);
			}
			//获取线路明细
			List<Map<String,String>> infoList = null;
			if(null != map.get("data")){
				infoList = (List<Map<String,String>>) map.get("data");
			}
			/**校验序号**/
			//序号不能为空
			if(ArrFileUtil.isEmpty(lineNo)){ 
				String msg = "序号不能有空值";
				wrongMsg.append(msg+";");
				wrongNum++;
				continue;
			}
			StringBuilder wrongStr = new StringBuilder("序号为" + lineNo + "的记录：");
			/**校验班次代码**/
			//1.非空
			if(ArrFileUtil.isEmpty(arrangeNo)){ 
				wrongStr.append("班次代码不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//2.超长
			if(arrangeNo.getBytes().length > 50){ 
				wrongStr.append("班次代码过长");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//3.唯一
			if(checkArrangeNoRepeat(arrangeNoList,arrangeNo)){
				wrongStr.append("班次代码不能重复");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			arrangeNoList.add(arrangeNo);
			/**校验归属网点**/
			//1.非空
			if(ArrFileUtil.isEmpty(deptCode)){ 
				wrongStr.append("归属网点不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//2.有效
			Department department = DepartmentCacheBiz.getDepartmentByCode(deptCode);
			if (null == department || null == department.getId() || department.getDeleteFlg()) {
				wrongStr.append("归属网点不存在(请检查网点是否已经失效)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//3.有权限:admin不校验权限
			ArrDepartment dept = new ArrDepartment();
			dept.setId(department.getId());
			dept.setDeptCode(department.getDeptCode());
			if(userId.compareTo(1L) != 0){
				ArrDepartment d = this.getDeptById(userDepts,department.getId());
				if(null == d || null == d.getId()){
					wrongStr.append("您没有该归属网点的权限(请重新填写)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
			}
			//4.归属网点正确时，校验班次代码格式
			if(!arrangeNo.matches("^"+deptCode+"-[0-9]{1,7}$")){
				wrongStr.append("班次代码格式错误(正确格式:归属网点代码-数字(最少1位、最多7位),例如:755Y-001)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/**校验线路明细**/
			//线路明细不能为空
			if(null == infoList || infoList.isEmpty()){
				wrongStr.append("线路明细不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//记录同一个配班的线路明细数据--用于校验时间是否交叉
			myInfoList = new ArrayList<ScheduleInfo>();
			for(int i=0;i<infoList.size();i++){
				/**错误行数超过100行，退出校验**/
				if(wrongNum >= 100){
					wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
					break;
				}
				Map m = infoList.get(i);
				if(null == m){
					continue;
				}
				wrongStr = new StringBuilder("序号为" + lineNo + "的记录(第"+(i+1)+"笔明细):");
				/**1.校验线路明细字段非空***/
				startTm = (m.get("startTm")==null?"":m.get("startTm").toString());
				endTm = (m.get("endTm")==null?"":m.get("endTm").toString());
				startDeptCode = (m.get("startDeptCode")==null?"":m.get("startDeptCode").toString());
				endDeptCode = (m.get("endDeptCode")==null?"":m.get("endDeptCode").toString());
				deptCode = (m.get("deptCode")==null?"":m.get("deptCode").toString());
				vehicleCode = (m.get("vehicleCode")==null?"":m.get("vehicleCode").toString());
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
				if(startDeptCode.getBytes().length > 30){ 
					wrongStr.append("始发网点过长");
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
				if(endDeptCode.getBytes().length > 30){ 
					wrongStr.append("目的网点过长");
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
				if(vehicleCode.getBytes().length > 16){ 
					wrongStr.append("车牌号过长");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				/**2.校验线路明细数据格式**/
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
					wrongStr.append("出车时间:时钟不能小于0大于23");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if (minutePartStart.compareTo(0) < 0
						|| minutePartStart.compareTo(59) > 0) {
					wrongStr.append("出车时间:分钟不能小于0大于59");
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
					wrongStr.append("收车时间:时钟不能小于0大于23");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if (minutePartEnd.compareTo(0) < 0
						|| minutePartEnd.compareTo(59) > 0) {
					wrongStr.append("收车时间:分钟不能小于0大于59");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				/** 3.校验线路明细重复和交叉 **/
				//创建线路信息实体
				infoEntity = new ScheduleInfo();
				//设置出车网点
				ArrDepartment arrStartDept = new ArrDepartment();
				arrStartDept.setDeptCode(startDeptCode);
				//设置收车网点
				ArrDepartment arrEndDept = new ArrDepartment();
				arrEndDept.setDeptCode(endDeptCode);
				//设置出车网点、收车网点、出车时间、收车时间、车辆
				infoEntity.setStartDept(arrStartDept);
				infoEntity.setEndDept(arrEndDept);
				infoEntity.setStartTm(startTm);
				infoEntity.setEndTm(endTm);
				ArrVehicle vehicle = new ArrVehicle();
				vehicle.setVehicleCode(vehicleCode);
				infoEntity.setVehicle(vehicle);
				/****a.先与excel自身数据校验****/
				//校验重复和交叉
				if(checkRepeat(usedInfoList,infoEntity)){
					wrongStr.append("线路明细重复(请检查您填写的线路明细并修正)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				usedInfoList.add(infoEntity);
				if(checkCross(myInfoList,infoEntity)){
					wrongStr.append("该配班线路明细时间有冲突(请修正)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				myInfoList.add(infoEntity);
			}
			//线路明细校验全部通过，则添加配班记录
			if(ArrFileUtil.isEmpty(wrongMsg.toString())){
				if(null == myInfoList || myInfoList.isEmpty()){
					wrongStr.append("合法线路明细不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				arrange = new ScheduleArrange();
				//设置序号
				arrange.setLineNo(lineNo);
				//设置班次代码
				arrange.setArrangeNo(arrangeNo);
				//设置配班线路关系集合
				arrange.setInfoArrangeList(myInfoList);
				//设置归属网点
				arrange.setDept(dept);
				//添加该班次到待保存列表
				saveList.add(arrange);
			}
		}
		/**校验完成后，释放不再使用集合占用的内存**/
		datas = null;
		userDepts = null;
		arrangeNoList = null;
		usedInfoList = null;
		//1-1.自身校验不通过，则提示
		if(!ArrFileUtil.isEmpty(wrongMsg.toString())) {
			throw new BizException(wrongMsg.toString());
		}
		/**2.与系统已有数据比较**/
		for(int rowIndex = 0;rowIndex<saveList.size();rowIndex++){
			/**错误行数超过100行，退出校验**/
			if(wrongNum >= 100){
				wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
				break;
			}
			ScheduleArrange arr = saveList.get(rowIndex);
			StringBuilder wrongStr = new StringBuilder("序号为" + arr.getLineNo() + "的记录：");
			/**校验班次代码**/
			//与系统已有班次代码比较
			int repeat = scheduleArrangeDao.listArrange(arr.getArrangeNo());
			if(repeat > 0){
				wrongStr.append("班次代码已被其他配班占用(请重新填写)");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			/**校验线路明细**/
			//线路明细不能为空
			if(null == arr.getInfoArrangeList() || arr.getInfoArrangeList().isEmpty()){
				wrongStr.append("合法线路明细不能为空");
				String msg = wrongStr.toString();
				wrongNum++;
				wrongMsg.append(msg+";");
				continue;
			}
			//校验线路
			for(int i=0;i<arr.getInfoArrangeList().size();i++){
				/**错误行数超过100行，退出校验**/
				if(wrongNum >= 100){
					wrongMsg.append("错误行数过多已中止校验(请修正后再导入);");
					break;
				}
				ScheduleInfo si = arr.getInfoArrangeList().get(i);
				wrongStr = new StringBuilder("序号为" + arr.getLineNo() + "的记录(第"+(i+1)+"笔明细):");
				if(null == si){
					wrongStr.append("线路不能为空");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				/****b.与系统已有数据校验****/
				//校验始发网点
				Long startDeptId = -1L;
				Long endDeptId = -1L;
				Department d = DepartmentCacheBiz.getDepartmentByCode(si.getStartDept().getDeptCode());
				if(null == d || null == d.getId()){
					wrongStr.append("始发网点无效(请重新填写)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null != d && null != d.getId()){
					startDeptId = d.getId();
				}
				d = null;
				//校验目的网点
				d = DepartmentCacheBiz.getDepartmentByCode(si.getEndDept().getDeptCode());
				if(null == d || null == d.getId()){
					wrongStr.append("目的网点无效(请重新填写)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null != d && null != d.getId()){
					endDeptId = d.getId();
				}
				//校验是否存在、是否失效、是否已使用
				ScheduleInfo scheduleInfo = scheduleInfoDao.listInfoByStartEndDept(si.getStartTm(), si.getEndTm(), 
						startDeptId, endDeptId,arr.getDept().getId(),userId);
				if (null == scheduleInfo || null == scheduleInfo.getId()) {
					wrongStr.append("线路不存在(请重新填写)");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null != scheduleInfo.getValid() && scheduleInfo.getValid().compareTo(0)==0){
					wrongStr.append("线路已经失效(请重新填写),");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				if(null != scheduleInfo.getIsUsed() && scheduleInfo.getIsUsed().compareTo(1)==0){
					wrongStr.append("线路已被其他配班使用(请重新填写),");
					String msg = wrongStr.toString();
					wrongNum++;
					wrongMsg.append(msg+";");
					continue;
				}
				//手工录入
				if(scheduleInfo.getDataSource().compareTo(1)==0){
					ArrVehicle vehicle = this.arrVehicleDao.listByCode(si.getVehicle().getVehicleCode());
					if(null == vehicle){
						wrongStr.append("车牌号不存在(请重新填写),");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						continue;
					}
					if(null != scheduleInfo 
							&& null != scheduleInfo.getVehicle() 
							&& null != scheduleInfo.getVehicle().getId()
							&& scheduleInfo.getVehicle().getId().compareTo(vehicle.getId())!=0){
						wrongStr.append(String.format("出车时间为%s的线路已经配车，不允许重新指定车牌(原车牌号:%s)",
								scheduleInfo.getStartTm(),
								scheduleInfo.getVehicle().getVehicleCode()));
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						continue;
					}
					//1.原记录没有配车 2.原记录配车与新配车不一致-则校验时间冲突并保存新车牌号
					if(null == scheduleInfo.getVehicle() || null == scheduleInfo.getVehicle().getId()
							|| scheduleInfo.getVehicle().getId().compareTo(vehicle.getId())!=0){
						//取该车牌号所有有效的线路信息
						List<ScheduleInfo> list = scheduleInfoDao.listByCode(vehicle.getVehicleCode());
						boolean flg = false;
						String flgMsg = null;
						if (null != list && !list.isEmpty()) {
							for (ScheduleInfo ii : list) {
								if (null == ii || null == ii.getId() 
										|| ArrFileUtil.isEmpty(ii.getStartTm())
										|| ArrFileUtil.isEmpty(ii.getEndTm())) {
									continue;
								}
								//不与自己比较
								if(scheduleInfo.getId().compareTo(ii.getId()) == 0){
									continue;
								}
								// 校验时间不能冲突
								if (compareCross(scheduleInfo, ii)) {
									flg = true;
									flgMsg = String.format("线路时间有冲突，车辆%s已经有出车时间=%s,收车时间=%s的线路，" +
											"请重新填写车牌号",vehicle.getVehicleCode(),
											ii.getStartTm(), ii.getEndTm());
									break;
								}
							}
						}
						if(flg){
							wrongStr.append(flgMsg);
							String msg = wrongStr.toString();
							wrongNum++;
							wrongMsg.append(msg+";");
							continue;
						}
						scheduleInfo.setVehicle(vehicle);
					}
				}else{
					if(null != scheduleInfo.getVehicle() && null != scheduleInfo.getVehicle().getId()
							&& null != scheduleInfo.getVehicle().getVehicleCode()
							&& !scheduleInfo.getVehicle().getVehicleCode().equals(si.getVehicle().getVehicleCode())){
						wrongStr.append("所填车牌号与线路指定车牌号不一致(请修正)，");
						String msg = wrongStr.toString();
						wrongNum++;
						wrongMsg.append(msg+";");
						continue;
					}
				}
				/**校验全部通过-添加到需要保存的线路明细中**/
				scheduleInfo.setIsUsed(1);
				scheduleInfo.setModifiedEmpCode(createdEmpCode);
				scheduleInfo.setModifiedTm(ArrCommonUtil.currentTm());
				//设置线路信息id
				arr.getInfoArrangeList().set(i, scheduleInfo);
			}
			/**线路明细校验通过，则添加配班记录**/
			if(ArrFileUtil.isEmpty(wrongMsg.toString())){
				infoArrangeList = new ArrayList<ScheduleInfoArrange>();
				ScheduleInfoArrange infoArrange = null;
				//转换线路信息为线路明细关系实体
				for(ScheduleInfo info:arr.getInfoArrangeList()){
					infoArrange = new ScheduleInfoArrange();
					infoArrange.setScheduleInfo(info);
					infoArrange.setInfoIdBak(info.getId());
					infoArrangeList.add(infoArrange);
				}
				//List转换成Set，对象id都为空或有一个为空时，会认为是不同的对象(只有id都不为空时才比较id)
				Set<ScheduleInfoArrange> infoArrangeSet = new HashSet<ScheduleInfoArrange>(infoArrangeList);
				//设置线路明细
				arr.setScheduleArrangeInfos(infoArrangeSet);
			}
		}
		//2-1.与系统数据校验不通过，则提示
		if(!ArrFileUtil.isEmpty(wrongMsg.toString())) {
			throw new BizException(wrongMsg.toString());
		}
		/**3.保存数据**/
		//2.保存配班数据
		List<ScheduleArrange> saveListBatch = new ArrayList<ScheduleArrange>();
		if(null != saveList && !saveList.isEmpty()){
			for(ScheduleArrange arr:saveList){
				if(null == arr){
					continue;
				}
				arr.setCreatedEmpCode(createdEmpCode);
				arr.setCreatedTm((new Date()));
				//设置为:普通班次
				arr.setArrangeType(2);
				//设置为:有效
				arr.setValid(1);
				saveListBatch.add(arr);
				if(saveListBatch.size() == 1000){
					scheduleArrangeDao.saveBatch(saveListBatch);
					saveListBatch.clear();
				}
			}
			if(!saveListBatch.isEmpty()){
				scheduleArrangeDao.saveBatch(saveListBatch);
			}
		}
	}
	/**
	 * 填充数据
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void fillData(List<Map> data,HSSFSheet sheet){
		if(null == data || data.isEmpty()){
			return ;
		}
		if(null == sheet){
			return ;
		}
		if(data.size() > this.MAX_SIZE){
			throw new BizException(I18nEl.i18n_def("scheduleArrange.biz.31","数据过多(超过65530条)，请选择查询条件过滤数据"));
		}
		//获取样式
		final int totalColumn = 10;
		HSSFCellStyle[] style = new HSSFCellStyle[totalColumn];
		HSSFRow styleRow = sheet.getRow(1);
		if(styleRow != null){
			for(int i=0;i<totalColumn;i++){
				style[i] = styleRow.getCell(i)==null?null:styleRow.getCell(i).getCellStyle();
			}
		}
		
		HSSFCell cell;
		HSSFRow row;
		//数据填充起始行
		int startRowIdx = 1;
		//总列数
		final int columnTotal = 10;
		//序号
		int rownum = 1;
		//填充数据
		for(int i=0;i<data.size();i++){
			Map m = data.get(i);
			if(null == m){
				continue;
			}
			row = sheet.getRow(startRowIdx);
			if(null == row){
				row = sheet.createRow(startRowIdx);
			}
			//记录列号
			int columnIdx = 0;
			//序号列
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			cell.setCellValue(rownum);
			++rownum;
			++columnIdx;
			//班次代码
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("ARRANGE_NO")
					&& !FileUtil.isEmpty(m.get("ARRANGE_NO").toString())){
				cell.setCellValue(m.get("ARRANGE_NO").toString());
			}
			++columnIdx;
			//是否有效
			cell = row.getCell(columnIdx);
			if(null == cell){
				cell = row.createCell(columnIdx);
			}
			if(null != style[columnIdx]){
				cell.setCellStyle(style[columnIdx]);
			}
			if(null != m.get("VALID")
					&& !FileUtil.isEmpty(m.get("VALID").toString())){
				cell.setCellValue(m.get("VALID").toString());
			}
			++columnIdx;
			//线路明细
			if(null != m.get("ARRANGE_TYPE") 
					&& m.get("ARRANGE_TYPE").toString().equals("2")){
				/**非机动班填充明细***/
				String arrangeNoCompare = (null ==  m.get("ARRANGE_NO")?"":m.get("ARRANGE_NO").toString());
				List<ScheduleInfo> infos = new ArrayList<ScheduleInfo>();
				//向下遍历数据
				int j=i;
				for(;j<data.size();j++){
					Map mbak = data.get(j);
					if(null == mbak){
						continue;
					}
					String arrangeNoForCompare = "";
					if(null != mbak.get("ARRANGE_NO") 
							&& !FileUtil.isEmpty(mbak.get("ARRANGE_NO").toString())){
						arrangeNoForCompare = mbak.get("ARRANGE_NO").toString();
					}
					//不是同一个配班时退出循环
					if(!arrangeNoCompare.equals(arrangeNoForCompare)){
						break;
					}
					//转换map为班次信息实体
					ScheduleInfo info = new ScheduleInfo();
					if(null != mbak.get("INFO_START_TM")
							&& !FileUtil.isEmpty(mbak.get("INFO_START_TM").toString())){
						info.setStartTm(mbak.get("INFO_START_TM").toString());
					}
					if(null != mbak.get("INFO_END_TM")
							&& !FileUtil.isEmpty(mbak.get("INFO_END_TM").toString())){
						info.setEndTm(mbak.get("INFO_END_TM").toString());
					}
					if(null != mbak.get("START_DEPT")
							&& !FileUtil.isEmpty(mbak.get("START_DEPT").toString())){
						info.setStartDeptCodeName(mbak.get("START_DEPT").toString());
					}
					if(null != mbak.get("END_DEPT")
							&& !FileUtil.isEmpty(mbak.get("END_DEPT").toString())){
						info.setEndDeptCodeName(mbak.get("END_DEPT").toString());
					}
					if(null != mbak.get("INFO_VALID")
							&& !FileUtil.isEmpty(mbak.get("INFO_VALID").toString())){
						info.setValidTxt(mbak.get("INFO_VALID").toString());
					}
					if(null != mbak.get("DEPT")
							&& !FileUtil.isEmpty(mbak.get("DEPT").toString())){
						info.setDeptCodeName(mbak.get("DEPT").toString());
					}
					if(null != mbak.get("VEHICLE_CODE")
							&& !FileUtil.isEmpty(mbak.get("VEHICLE_CODE").toString())){
						info.setVehicleCode(mbak.get("VEHICLE_CODE").toString());
					}
					infos.add(info);
				}
				//退回到当前行
				i = j-1;
				//填充班次明细
				if(null == infos || infos.isEmpty()){
					throw new BizException(I18nEl.i18n_def_arg1("scheduleArrange.biz.25"
							,"有异常数据(没有班次明细),异常数据:{0}",arrangeNoCompare));
				}
				//班次明细排序
				try{
					infos = this.sortList(infos);
				}catch(Exception e){
					log.error("info:sortList failure",e);
				}
				/**合并行**/
				//从当前行开始写
				int rowIdx = startRowIdx;
				//标识是否有取下一行
				boolean flg = false;
				for(ScheduleInfo info:infos){
					if(null == info){
						continue;
					}
					//取行
					row = sheet.getRow(rowIdx);
					if(null == row){
						row = sheet.createRow(rowIdx);
					}
					//设置需要合并行的样式
					for(int cellIdx=0;cellIdx<3;cellIdx++){
						cell = row.getCell(cellIdx);
						if(null == cell){
							cell = row.createCell(cellIdx);
						}
						if(null != style[cellIdx]){
							cell.setCellStyle(style[cellIdx]);
						}
					}
					int colIdx = 3;
					//出车时间
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getStartTm())){
						cell.setCellValue(info.getStartTm());
					}
					++colIdx;
					//收车时间
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getEndTm())){
						cell.setCellValue(info.getEndTm());
					}
					++colIdx;
					//始发网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getStartDeptCodeName())){
						cell.setCellValue(info.getStartDeptCodeName());
					}
					++colIdx;
					//目的网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getEndDeptCodeName())){
						cell.setCellValue(info.getEndDeptCodeName());
					}
					++colIdx;
					//是否有效
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getValidTxt())){
						cell.setCellValue(info.getValidTxt());
					}
					++colIdx;
					//归属网点
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getDeptCodeName())){
						cell.setCellValue(info.getDeptCodeName());
					}
					++colIdx;
					//车牌号
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
					if(!ArrFileUtil.isEmpty(info.getVehicleCode())){
						cell.setCellValue(info.getVehicleCode());
					}
					++colIdx;
					//取下一行
					flg = true;
					++rowIdx;
				}
				//退回到当前行
				if(flg){
					--rowIdx;
				}
				//有多行则合并行
				if(startRowIdx != rowIdx){
					int firstRow = startRowIdx,lastRow = rowIdx;
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, 2, 2));
				}
				//设置行为当前行
				startRowIdx = rowIdx;
			}else{
				/**机动班填充出收车时间***/
				int colIdx = 3;
				//出车时间
				cell = row.getCell(colIdx);
				if(null == cell){
					cell = row.createCell(colIdx);
				}
				if(null != style[colIdx]){
					cell.setCellStyle(style[colIdx]);
				}
				if(null != m.get("START_TM")
						&& !FileUtil.isEmpty(m.get("START_TM").toString())){
					cell.setCellValue(m.get("START_TM").toString());
				}
				++colIdx;
				//收车时间
				cell = row.getCell(colIdx);
				if(null == cell){
					cell = row.createCell(colIdx);
				}
				if(null != style[colIdx]){
					cell.setCellStyle(style[colIdx]);
				}
				if(null != m.get("END_TM")
						&& !FileUtil.isEmpty(m.get("END_TM").toString())){
					cell.setCellValue(m.get("END_TM").toString());
				}
				++colIdx;
				//设置剩余单元格样式
				for(;colIdx<columnTotal;colIdx++){
					cell = row.getCell(colIdx);
					if(null == cell){
						cell = row.createCell(colIdx);
					}
					if(null != style[colIdx]){
						cell.setCellStyle(style[colIdx]);
					}
				}
			}
			++startRowIdx;
		}
	}
	/**
	 * 获取已经校验过的归属网点
	 * @param depts
	 * @param id
	 * @return
	 */
	private ArrDepartment getDeptById(List<ArrDepartment> depts,Long id){
		if(null == depts || depts.isEmpty() || null == id){
			return null;
		}
		for(ArrDepartment d:depts){
			if(null == d || null == d.getId()){
				continue;
			}
			if(d.getId().compareTo(id)==0){
				return d;
			}
		}
		return null;
	}
	/**
	 * 校验班次代码是否重复
	 * @param arrangeNoList
	 * @param arrangeNo
	 * @return
	 */
	private boolean checkArrangeNoRepeat(List<String> arrangeNoList,String arrangeNo){
		if(null == arrangeNoList || arrangeNoList.isEmpty() || ArrFileUtil.isEmpty(arrangeNo)){
			return false;
		}
		for(String no:arrangeNoList){
			if(null == no){
				continue;
			}
			if(no.equals(arrangeNo)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取合并单元格idx(同一个合并单元格idx一样)
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int isMergedRegion(HSSFSheet sheet, int row, int column) {
		if(null == sheet){
			return -1;
		}
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			Region ca = sheet.getMergedRegionAt(i);
			int firstColumn = ca.getColumnFrom();
			int lastColumn = ca.getColumnTo();
			int firstRow = ca.getRowFrom();
			int lastRow = ca.getRowTo();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * 获取合并单元格的值
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getMergedCellValue(HSSFSheet sheet, int row, int column) {
		String val = "";
		if(null == sheet){
			return val;
		}
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			Region ca = sheet.getMergedRegionAt(i);
			int firstColumn = ca.getColumnFrom();
			int lastColumn = ca.getColumnTo();
			int firstRow = ca.getRowFrom();
			int lastRow = ca.getRowTo();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					//是合并单元格，则拿合并单元格的值
					for(int j=firstRow;j<=lastRow;j++){
						for(int k=firstColumn;k<=lastColumn;k++){
							//空行不处理
							if(null == sheet.getRow(j)){
								continue;
							}
							HSSFCell cell = sheet.getRow(j).getCell(k);
							if(null == cell){
								continue;
							}
							String cellVal = this.getHSSFCellValue(cell);
							//如果还没有取到值，则设置值(合并单元格，只有一个单元格有值)
							if(ArrFileUtil.isEmpty(val)){
								val = cellVal;
							}
						}
					}
				}
			}
		}
		return val;
	}
	/**
	 * 获取合并单元格的行数
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int getMergedRangeRow(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			Region ca = sheet.getMergedRegionAt(i);
			int firstColumn = ca.getColumnFrom();
			int lastColumn = ca.getColumnTo();
			int firstRow = ca.getRowFrom();
			int lastRow = ca.getRowTo();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					//是合并单元格，则拿合并单元格的值
					return lastRow-firstRow+1;
				}
			}
		}
		return 0;
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
				|| ArrFileUtil.isEmpty(entity.getStartTm())
				|| ArrFileUtil.isEmpty(entity.getEndTm())){
			return false;
		}
		//校验冲突
		for(ScheduleInfo l:list){
			//异常数据不做比较
			if(null == l
					|| ArrFileUtil.isEmpty(l.getStartTm())
					|| ArrFileUtil.isEmpty(l.getEndTm()) ){
				continue;
			}
			//校验班次时间是否冲突
			if(compareCross(entity,l)){
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
	private boolean checkRepeat(List<ScheduleInfo> list,ScheduleInfo entity){
		if(null == list || list.isEmpty() || null == entity){
			return false;
		}
		//异常数据不做比较
		if(ArrFileUtil.isEmpty(entity.getStartTm())
					|| ArrFileUtil.isEmpty(entity.getEndTm())
					|| null == entity.getStartDept()
					|| null == entity.getStartDept().getDeptCode()
					|| null == entity.getEndDept()
					|| null == entity.getEndDept().getDeptCode()){
			return false;
		}
		for(ScheduleInfo info:list){
			//异常数据不做比较
			if(null == info || ArrFileUtil.isEmpty(info.getStartTm())
					|| ArrFileUtil.isEmpty(info.getEndTm())
					|| null == info.getStartDept()
					|| null == info.getStartDept().getDeptCode()
					|| null == info.getEndDept()
					|| null == info.getEndDept().getDeptCode()){
				continue;
			}
			//出收车时间、始发目的网点相同-重复
			if(entity.getStartTm().equals(info.getStartTm()) 
					&& entity.getEndTm().equals(info.getEndTm())
					&& entity.getStartDept().getDeptCode().equals(info.getStartDept().getDeptCode())
					&& entity.getEndDept().getDeptCode().equals(info.getEndDept().getDeptCode())){
				return true;
			}
		}
		return false;
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
		if(null != val){
			val = val.trim();
		}
		return val;
	}
	/**
	 * 生成班次代码
	 * @param list
	 * @param deptCode
	 * @param arrangeType 1.机动班次 2.普通班次
	 * @return
	 */
	private String generateArrangeNo(long maxNo,String deptCode,int arrangeType){
		//班次代码=最大班次代码+1
		long no = maxNo + 1;
		if(1 == arrangeType){
			return deptCode+"机动-"+this.fillZero(no);
		}else{
			return deptCode+"-"+this.fillZero(no);
		}
	}
	/**
	 * 不足8位用0填充为8位
	 * @param no
	 * @return
	 */
	private String fillZero(long no){
		if(no < 1){
			return "00000001";
		}
		String num = ""+no;
		if(num.length()>=8){
			return num;
		}
		String arrNo = "00000001";
		switch(num.length()){
		case 1:arrNo="0000000"+num;break;
		case 2:arrNo="000000"+num;break;
		case 3:arrNo="00000"+num;break;
		case 4:arrNo="0000"+num;break;
		case 5:arrNo="000"+num;break;
		case 6:arrNo="00"+num;break;
		case 7:arrNo="0"+num;break;
		}
		return arrNo;
	}
	/**
	 * 比较时间冲突
	 * @param newStart
	 * @param newEnd
	 * @param oldStart
	 * @param oldEnd
	 * @return 有冲突返回true
	 */
	private boolean compareCross(ScheduleInfo newInfo,
			ScheduleInfo oldInfo) {
		if(null == newInfo || null == oldInfo){
			return false;
		}
		String newStart = newInfo.getStartTm();
		String newEnd = newInfo.getEndTm();
		String oldStart = oldInfo.getStartTm();
		String oldEnd = oldInfo.getEndTm();
		if(ArrFileUtil.isEmpty(newStart)
				||ArrFileUtil.isEmpty(newEnd)
				||ArrFileUtil.isEmpty(oldStart)
				||ArrFileUtil.isEmpty(oldEnd)){
			return false;
		}
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
	 * 比较下班时间不能等于上班时间
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
		// 上班时间不能等于下班时间
		if (compareTm(newStartPart1, newStartPart2, newEndPart1, newEndPart2) == 0) {
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
	 * List排序
	 * @param infos
	 * @return
	 */
	private List<ScheduleInfoArrange> sortInfoArrange(List<ScheduleInfoArrange> infoarrs){
		Collections.sort(infoarrs, new Comparator<ScheduleInfoArrange>(){
			public int compare(ScheduleInfoArrange o1,ScheduleInfoArrange o2) {
				if(null == o1 || null == o2 || null == o1.getScheduleInfo() || null == o2.getScheduleInfo()){
					return 0;
				}
				Integer o1part1=null,o1part2=null,o2part1=null,o2part2=null;
				try{
					o1part1 = Integer.parseInt(o1.getScheduleInfo().getStartTm().split(":", -1)[0]);
					o1part2 = Integer.parseInt(o1.getScheduleInfo().getStartTm().split(":", -1)[1]);
					o2part1 = Integer.parseInt(o2.getScheduleInfo().getStartTm().split(":", -1)[0]);
					o2part2 = Integer.parseInt(o2.getScheduleInfo().getStartTm().split(":", -1)[1]);
				}catch(Exception e){
					log.error("info:sortInfoArrange",e);
				}
				if(null == o1part1 || null == o1part2 || null == o2part1 || null == o2part2){
					return 0;
				}
				return this.compareTm(o1part1, o1part2, o2part1, o2part2);
			}
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
			
		});
		return infoarrs;
	}
	/**
	 * List排序
	 * @param infos
	 * @return
	 */
	private List<ScheduleInfo> sortList(List<ScheduleInfo> infos){
		Collections.sort(infos, new Comparator<ScheduleInfo>(){
			public int compare(ScheduleInfo o1,ScheduleInfo o2) {
				if(null == o1 || null == o2){
					return 0;
				}
				Integer o1part1=null,o1part2=null,o2part1=null,o2part2=null;
				try{
					o1part1 = Integer.parseInt(o1.getStartTm().split(":", -1)[0]);
					o1part2 = Integer.parseInt(o1.getStartTm().split(":", -1)[1]);
					o2part1 = Integer.parseInt(o2.getStartTm().split(":", -1)[0]);
					o2part2 = Integer.parseInt(o2.getStartTm().split(":", -1)[1]);
				}catch(Exception e){
					log.error("info:sortList",e);
				}
				if(null == o1part1 || null == o1part2 || null == o2part1 || null == o2part2){
					return 0;
				}
				return this.compareTm(o1part1, o1part2, o2part1, o2part2);
			}
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
			
		});
		return infos;
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
	/**
	 * 根据配班班次代码获取配班信息
	 * @param arrangeNo 配班班次代码
	 * @return
	 */
	public ScheduleArrange findArrByArrangeNo(String arrangeNo) {
		return this.scheduleArrangeDao.findArrByArr(arrangeNo);
	}
	/**
	 *  修改是否有效
	 * @throws Exception 
	 */
	public void updateValid(Long recordId, Integer valid) throws Exception {}
	/**
	 * 置为无效-清除排班
	 * @throws Exception 
	 */
	public void updateInvalid(Long[] recordIds,Integer valid) throws Exception{
		if(null == valid){
			throw new BizException("请选择是否有效");
		}
		if(valid.compareTo(1)!=0 && valid.compareTo(0)!=0){
			throw new BizException("是否有效取值有误(取值范围:1,0)");
		}
		if(null == recordIds || recordIds.length < 1){
			throw new BizException("请选择记录");
		}
		if(valid.compareTo(1)==0){
			//修改有效
			if(recordIds.length>1){
				throw new BizException("修改为有效时只能选择一笔记录");
			}
			for(Long id:recordIds){
				ScheduleArrange old = this.scheduleArrangeDao.load(id);
				if(null == old){
					throw new BizException("记录已被删除");
				}
				if(old.getValid().compareTo(1)==0){
					continue;
				}
				for(ScheduleInfoArrange sia:old.getScheduleArrangeInfos()){
					if(null != sia && null != sia.getScheduleInfo() && sia.getScheduleInfo().getValid().compareTo(0)==0){
						throw new BizException(String.format("班次代码%s不允许修改为有效(出车时间为%s的线路已失效)", 
								old.getArrangeNo(),sia.getScheduleInfo().getStartTm()));
					}
					if(null != sia.getScheduleInfo() && sia.getScheduleInfo().getIsUsed().compareTo(1)==0){
						throw new BizException(String.format("班次代码%s不允许修改为有效(出车时间为%s的线路已被其他班次使用)", 
								old.getArrangeNo(),sia.getScheduleInfo().getStartTm()));
					}
				}
				for(ScheduleInfoArrange sia:old.getScheduleArrangeInfos()){
					if(null == sia.getScheduleInfo()){
						sia.setInfoIdBak(sia.getScheduleInfo().getId());
					}
				}
				//记录日志
				ScheduleArrange entity = new ScheduleArrange();
				entity.setId(id);
				entity.setValid(valid);
				LogUtils.getInstance().updateLog(old, entity);
				old.setValid(valid);
				for(ScheduleInfoArrange infoArr:old.getScheduleArrangeInfos()){
					if(null == infoArr){
						continue;
					}
					if(null == infoArr.getScheduleInfo() || null == infoArr.getScheduleInfo().getId()){
						continue;
					}
					infoArr.setInfoIdBak(infoArr.getScheduleInfo().getId());
					infoArr.getScheduleInfo().setIsUsed(1);
					infoArr.getScheduleInfo().setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					infoArr.getScheduleInfo().setModifiedTm(ArrCommonUtil.currentTm());
				}
			}
		}else{
			//修改为无效
			for(Long id:recordIds){
				ScheduleArrange old = this.scheduleArrangeDao.load(id);
				if(null == old){
					throw new BizException("记录已被删除");
				}
				if(old.getIsUsed().compareTo(0) == 0){
					throw new BizException("该操作只能选择已排班记录");
				}
				//无效-无效：不处理
				if(old.getValid().compareTo(0)==0){
					continue;
				}
				//记录日志
				ScheduleArrange entity = new ScheduleArrange();
				entity.setId(id);
				entity.setValid(valid);
				LogUtils.getInstance().updateLog(old, entity);
				old.setValid(valid);
				for(ScheduleInfoArrange infoArr:old.getScheduleArrangeInfos()){
					if(null == infoArr){
						continue;
					}
					infoArr.setInfoIdBak(null);
					if(null == infoArr.getScheduleInfo() || null == infoArr.getScheduleInfo().getId()){
						continue;
					}
					//有效-无效：释放
					if(null == infoArr.getScheduleInfo().getIsUsed() 
							|| infoArr.getScheduleInfo().getIsUsed().compareTo(1)==0){
						infoArr.getScheduleInfo().setIsUsed(2);
						infoArr.getScheduleInfo().setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
						infoArr.getScheduleInfo().setModifiedTm(ArrCommonUtil.currentTm());
					}
				}
			}
			scheduleArrangeJdbcDao.updateInvalid(recordIds,this.getCurrentUser().getEmployee().getCode());
		}
	}
}
