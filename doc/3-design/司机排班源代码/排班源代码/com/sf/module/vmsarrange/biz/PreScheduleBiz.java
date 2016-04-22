/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-29     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import com.sf.framework.base.IPage;
import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.presentation.taglib.I18nEl;
import com.sf.module.cmscommon.biz.DepartmentCacheBiz;
import com.sf.module.department.domain.Department;
import com.sf.module.frameworkimplExtend.util.StringUtil;
import com.sf.module.vmsarrange.dao.IArrDriverDao;
import com.sf.module.vmsarrange.dao.IPreScheduleDao;
import com.sf.module.vmsarrange.dao.IPreScheduleDraftDao;
import com.sf.module.vmsarrange.dao.IScheduleArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleMonthRptDao;
import com.sf.module.vmsarrange.dao.ITransferClassesDFDao;
import com.sf.module.vmsarrange.dao.ITransferClassesRLDao;
import com.sf.module.vmsarrange.domain.ArrDriver;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;
import com.sf.module.vmsarrange.domain.ScheduleMonthRpt;
import com.sf.module.vmsarrange.domain.TransferClassesDF;
import com.sf.module.vmsarrange.domain.TransferClassesRL;
import com.sf.module.vmsarrange.dto.ParamsDto;
import com.sf.module.vmsarrange.log.LogUtils;
import com.sf.module.vmsarrange.util.ArrCommonUtil;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsinfo.util.FileUtil;

/**
 *
 * PreScheduleBiz处理类
 *
 */
public class PreScheduleBiz extends BaseBiz implements IPreScheduleBiz {
	private IPreScheduleDao preScheduleDao;
	private ITransferClassesDFDao transferClassesDFDao;
	private IPreScheduleDraftDao preScheduleDraftDao;
	private IScheduleArrangeDao	scheduleArrangeDao;
	private IArrDriverDao arrDriverDao;
	private ITransferClassesRLDao transferClassesRLDao;
	private IScheduleMonthRptDao scheduleMonthRptDao;
	public void setScheduleMonthRptDao(IScheduleMonthRptDao scheduleMonthRptDao) {
		this.scheduleMonthRptDao = scheduleMonthRptDao;
	}
	public void setTransferClassesRLDao(ITransferClassesRLDao transferClassesRLDao) {
		this.transferClassesRLDao = transferClassesRLDao;
	}
	public void setScheduleArrangeDao(IScheduleArrangeDao scheduleArrangeDao) {
		this.scheduleArrangeDao = scheduleArrangeDao;
	}
	public void setArrDriverDao(IArrDriverDao arrDriverDao) {
		this.arrDriverDao = arrDriverDao;
	}
	public void setPreScheduleDraftDao(IPreScheduleDraftDao preScheduleDraftDao) {
		this.preScheduleDraftDao = preScheduleDraftDao;
	}
	public void setTransferClassesDFDao(ITransferClassesDFDao transferClassesDFDao) {
		this.transferClassesDFDao = transferClassesDFDao;
	}
	public void setPreScheduleDao(IPreScheduleDao preScheduleDao) {
		this.preScheduleDao = preScheduleDao;
	}
	/**
	 * 班次代码下拉框取数
	 */
	public IPage<ScheduleArrange> listArrangePage(Long deptId,Integer arrangeType, String arrangeNo,int pageSize, int pageIndex){
		return scheduleArrangeDao.listArrangePage(deptId,arrangeType, arrangeNo, pageSize, pageIndex, this.getUserId());
	}
	/**
	 * 分页查询排班信息
	 * @param deptCode		--网点代码
	 * @param empCode		--驾驶员工号
	 * @param classType		--班次类型
	 * @param yearMonth		--年月份
	 * @param start			--开始数据行号
	 * @param limit			--每页显示的条数
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex) throws Exception {
		if(null == yearMonth){
			return null;
		}
		IPage page = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//取当月-精确到月
		Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
		//取次月-精确到月
		Date nextMonth = DateUtils.truncate(DateUtils.addMonths(current, 1),Calendar.MONTH);
		//取选中月份-精确到月
		Date chooseMonth = sdf.parse(yearMonth);
		
		//单月之后-查询预排班
		if(chooseMonth.after(current)){
			page = this.preScheduleDraftDao.findByPage(deptCode, empCode, classType,
					yearMonth, pageSize, pageIndex,super.getUserId());
			//获取区部代码和名称
			if(null != page && !page.isEmpty()
					&& null != page.getData() && !page.getData().isEmpty()){
				for(Object o:page){
					if(null == o){
						continue;
					}
					PreScheduleDraft p = (PreScheduleDraft)o;
					Department d = null;
					if(null != p && null != p.getDepartment() && null != p.getDepartment().getAreaCode()){
						d = DepartmentCacheBiz.getDepartmentByCode(p.getDepartment().getAreaCode());
					}
					if(null != d){
						p.setAreaDeptCode(d.getDeptCode());
						p.setAreaDeptName(d.getDeptName());
					}
					o = p;
				}
			}
		}
		//当前月之前的月份-查询报表数据
		if(chooseMonth.before(current)){
			page = scheduleMonthRptDao.findByPage(deptCode, empCode, classType,
					yearMonth, pageSize, pageIndex,super.getUserId());
			//获取区部代码和名称
			if(null != page && !page.isEmpty()
					&& null != page.getData() && !page.getData().isEmpty()){
				for(Object o:page){
					if(null == o){
						continue;
					}
					ScheduleMonthRpt p = (ScheduleMonthRpt)o;
					Department d = null;
					if(null != p && null != p.getDepartment() && null != p.getDepartment().getAreaCode()){
						d = DepartmentCacheBiz.getDepartmentByCode(p.getDepartment().getAreaCode());
					}
					if(null != d){
						p.setAreaDeptCode(d.getDeptCode());
						p.setAreaDeptName(d.getDeptName());
					}
					o = p;
				}
			}
		}
		//当前月-查询实际排班
		if(chooseMonth.compareTo(current) == 0){
			page = preScheduleDao.findByPage(deptCode, empCode, classType,
					yearMonth, pageSize, pageIndex,super.getUserId());
			//获取区部代码和名称
			if(null != page && !page.isEmpty()
					&& null != page.getData() && !page.getData().isEmpty()){
				for(Object o:page){
					if(null == o){
						continue;
					}
					PreSchedule p = (PreSchedule)o;
					Department d = null;
					if(null != p && null != p.getDepartment() && null != p.getDepartment().getAreaCode()){
						d = DepartmentCacheBiz.getDepartmentByCode(p.getDepartment().getAreaCode());
					}
					if(null != d){
						p.setAreaDeptCode(d.getDeptCode());
						p.setAreaDeptName(d.getDeptName());
					}
					o = p;
				}
			}
		}
		return page;
	}

	/**
	 * 保存预排班信息
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void savePreSchedule(List<PreSchedule> preSchedules) throws Exception {
		if(preSchedules == null || preSchedules.isEmpty()){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		List<String> empCodes = new ArrayList<String>();
		//存放校验过持证要求的驾驶员和班次代码
		List<Map> quasiArrangeList = new ArrayList<Map>();
		for(int i=0;i<preSchedules.size();i++){
			PreSchedule preSchedule = preSchedules.get(i);
			//校验数据是否重复
			if(preSchedule==null){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.1"
						,"保存失败,第{0}行数据不存在", i+1));
			}
			if(null==preSchedule.getDriver() || ArrFileUtil.isEmpty( preSchedule.getDriver().getEmpCode())){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.2"
						,"保存失败,第{0}行数据的驾驶员不能为空", i+1));
			}
			if(preSchedule.getClassType()==null){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.3"
						,"保存失败,第{0}行数据的班次类别不能为空", i+1));
			}
			ArrDriver driver = this.arrDriverDao.findDriverByCode(preSchedule.getDriver().getEmpCode());
			if(driver == null){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.4"
						,"保存失败,第{0}行数据的驾驶员工号无效", i+1));
			}
			if(null == driver.getDepartment() || null == driver.getDepartment().getId()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.5"
						,"保存失败,第{0}行数据的驾驶员所属网点为空", i+1));
			}
			preSchedule.setDepartment(driver.getDepartment());
			//判断驾驶员是否重复
			if(empCodes.contains(driver.getEmpCode())){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.6"
						,"保存失败,第{0}行数据的驾驶员重复", i+1));
			}else{
				empCodes.add(driver.getEmpCode());
			}
			//判断该驾驶员是否已经排班过
			Integer p = this.preScheduleDraftDao.findDriverById(this.getNextMonth(),driver.getId());
			if(p > 0){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.7"
						,"保存失败,第{0}行数据的驾驶员已经安排排班，不能重复排班", i+1));
			}
			//收集1号到31号的班次代码，统一判断
			List<Map> daysList = new ArrayList<Map>();
			daysList = getDaysData(preSchedule);
			if(null == daysList || daysList.isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.8"
						,"保存失败,第{0}行数据没有选择班次代码", i+1));
			}
			//计划休息天数
			int plan_days = 0;
			//保存合法的班次明细
			List<TransferClassesDF> classDfs = new ArrayList<TransferClassesDF>();
			Date current = new Date();
			ScheduleArrange lastArrange = null;
			//判断1号到31号的班次代码是否合法
			for(int j=0;j<daysList.size();j++){
				Map map = (Map) daysList.get(j);
				String day = (String) map.get("day");
				String arrangeNo = (String) map.get("arrangeNo");
				if(ArrFileUtil.isEmpty(arrangeNo)){
					throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.53"
							,"保存失败，第{0}行1到{1}号的班次代码有空值",i+1,daysList.size()));
				}
				if((preSchedule.getClassType().compareTo(1) == 0 || preSchedule.getClassType().compareTo(2) == 0)
						&& arrangeNo.indexOf("机动")!=-1){
					throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.10"
							,"保存失败,第{0}行{1}号班次类别为正常或顶班时，排班不能出现机动班次",i+1,day));
				}
				TransferClassesDF classDf = new TransferClassesDF();
				classDf.setArrangeNo(arrangeNo);
				String dayDtStr = this.getNextMonth();
				if(day.length()<2){
					dayDtStr += "-0"+day;
				}else{
					dayDtStr += "-"+day;
				}
				classDf.setDayDt(ArrCommonUtil.getYYYY_MM_DDFmt().parse(dayDtStr));
				if(arrangeNo.equals("休") || arrangeNo.equals("假")){
					plan_days = plan_days+1;
					classDf.setArrangeDf(null);
					lastArrange = null;
				}else{
					ScheduleArrange scheduleArrange = this.scheduleArrangeDao.findArrByArrangeNo(arrangeNo,driver.getDepartment().getId(),this.getCurrentUser().getId());
					if(null == scheduleArrange){
						throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.11"
								,"保存失败,第{0}行{1}号班次代码不存在(只取驾驶员归属网点下的班次)",i+1,day));
					}
					if(scheduleArrange.getValid().compareTo(0) == 0){
						throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.12"
								,"保存失败,第{0}行{1}号班次代码已失效，请重新选择",i+1,day));
					}
					if(null == lastArrange){
						lastArrange = scheduleArrange;
					}else{
						if(checkTimeCon(lastArrange,scheduleArrange)){
							throw new BizException(I18nEl.i18n_def_arg3("preschedule.biz.13"
									,"保存失败,第{0}行{1}号与{2}号排班有时间冲突",(i+1),j,(j+1)));
						}
						lastArrange = scheduleArrange;
					}
					Map hasChecked = null;
					//取key值
					String key = driver.getQuasiDrivingType()+","+scheduleArrange.getArrangeNo();
					//遍历已经校验过的集合
					if(null != quasiArrangeList && !quasiArrangeList.isEmpty()){
						for(Map o:quasiArrangeList){
							if(null == o){
								continue;
							}
							if(o.containsKey(key)){
								hasChecked = o;
							}
						}
					}
					String msg;
					if(null == hasChecked){
						//未校验过，则校验
						msg = this.isQuasiDriving(driver.getQuasiDrivingType(), scheduleArrange);
						Map m = new HashMap();
						m.put(key, msg);
						quasiArrangeList.add(m);
					}else{
						//已经校验过，则直接取结果
						msg = (null==hasChecked.get(key)?null:hasChecked.get(key).toString());
					}
					if(!ArrFileUtil.isEmpty(msg)){
						throw new BizException(I18nEl.i18n_def_arg3("preschedule.biz.14"
								,"保存失败,第{0}行驾驶员所持证件不满足班次代码{1}所配车辆的持证要求{2}",
								(i+1),scheduleArrange.getArrangeNo(),msg));
					}
					if(scheduleArrange.getIsUsed().compareTo(1) != 0){
						scheduleArrange.setIsUsed(1);
						this.scheduleArrangeDao.update(scheduleArrange);
					}
					scheduleArrange.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					scheduleArrange.setModifiedTm(ArrCommonUtil.currentTm());
					classDf.setArrangeDf(scheduleArrange);
				}
				classDf.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
				classDf.setCreatedTm(current);
				classDfs.add(classDf);
			}
			if(null == classDfs || classDfs.isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.15"
						,"保存失败,第{0}行数据合法班次为空,请重新填写",i+1));
			}
			preSchedule.setYearMonth(this.getNextMonth());
			preSchedule.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
			preSchedule.setCreatedTm(new Date());
			preSchedule.setDriver(driver);
			preSchedule.setDraftFlag(1);
			preSchedule.setPlanDay(plan_days);
			preSchedule.setRealDay(plan_days);
			preSchedule.setRate("100");
			//转换对象，保存预排班草稿信息
			PreScheduleDraft psd = copyBeanProperties(preSchedule);
			Set<TransferClassesDF> set = new HashSet<TransferClassesDF>();
			set.addAll(classDfs);
			psd.setTransferClassesDFs(set);
			psd.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
			psd.setCreatedTm(current);
			this.preScheduleDraftDao.save(psd);
		}
	}
	
	/**
	 * 修改预排班草稿
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateSchedule(List<PreSchedule> preSchedules) throws Exception {
		if(preSchedules == null || preSchedules.isEmpty()){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		List<Date> needRemove = new ArrayList<Date>();
		Date current = new Date();
		List<TransferClassesDF> classDfs = new ArrayList<TransferClassesDF>();
		List<Map> quasiArrangeList = new ArrayList<Map>();
		for(int i=0;i<preSchedules.size();i++){
			needRemove.clear();
			classDfs.clear();
			PreSchedule preSchedule = preSchedules.get(i);
			if(null == preSchedule.getId()){
				continue;
			}
			PreScheduleDraft oldDraft = this.preScheduleDraftDao.load(preSchedule.getId());
			if(null == oldDraft){
				throw new BizException(I18nEl.i18n_def("scheduleinfo.biz.36","记录已经被删除，请刷新页面"));
			}
			if(null == oldDraft.getYearMonth()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.16"
						,"数据异常(月份为空),异常数据Id:{0}",oldDraft.getId()));
			}
			if(!oldDraft.getYearMonth().matches("^\\d{4}-\\d{2}$")){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.17"
						,"数据异常(月份格式有误),异常数据Id:{0}",oldDraft.getId()));
			}
			/*if(preSchedule.getClassType()==null){
				throw new BizException("班次类型不能为空");
			}*/
			//转换为MAP(集合size=次月实际天数)
			List daysList = getDaysData(preSchedule);
			if(null == daysList){
				daysList = new ArrayList();
			}
			//记录计划休息的天数
			int planDays = 0;
			//校验修改的参数是否合法
			ScheduleArrange lastArrange = null;
			for(int j=0;j<daysList.size();j++){
				Map map = (Map) daysList.get(j);
				String day = (String) map.get("day");
				String arrangeNo = (String) map.get("arrangeNo");
				if(ArrFileUtil.isEmpty(arrangeNo)){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.18"
							,"1到{0}号的班次代码有空值",daysList.size()));
				}
				if((oldDraft.getClassType().compareTo(1) == 0 || oldDraft.getClassType().compareTo(2) == 0)
						&& arrangeNo.indexOf("机动")!=-1){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.19"
							,"{0}班次类别为正常或顶班时，排班不能出现机动班次",day));
				}
				String oldArrangeNo = getArrangeByDay(oldDraft, Integer.parseInt(day));
				//校验时间是否交叉
				ScheduleArrange scheduleArrange = null;
				if(arrangeNo.equals("休") || arrangeNo.equals("假")){
					planDays = planDays+1;
					lastArrange = null;
				}else{
					scheduleArrange = this.scheduleArrangeDao.findArrByArrangeNo(arrangeNo,oldDraft.getDepartment().getId(),this.getCurrentUser().getId());
					if(null == scheduleArrange || null == scheduleArrange.getId()){
						throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.20"
								,"{0}号班次代码不存在(只取驾驶员归属网点下的班次)",day));
					}
					if(scheduleArrange.getValid().compareTo(0) == 0){
						throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.21"
								,"{0}号班次代码已失效，请重新选择",day));
					}
					if(null == lastArrange){
						lastArrange = scheduleArrange;
					}else{
						if(checkTimeCon(lastArrange,scheduleArrange)){
							throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.22"
									,"{0}号与{1}号排班有时间冲突",j,(j+1)));
						}
						lastArrange = scheduleArrange;
					}
				}
				//班次代码不变即：未做修改
				if(!ArrFileUtil.isEmpty(oldArrangeNo) && oldArrangeNo.equals(arrangeNo)){
					continue;
				}
				//班次代码改变了，则记录需要删除原记录的日期
				String dayDtStr = this.getNextMonth();
				if(day.length()<2){
					dayDtStr += "-0"+day;
				}else{
					dayDtStr += "-"+day;
				}
				//添加到需要移除的明细集合
				needRemove.add(ArrCommonUtil.getYYYY_MM_DDFmt().parse(dayDtStr));
				TransferClassesDF classDf = new TransferClassesDF();
				classDf.setArrangeNo(arrangeNo);
				classDf.setDayDt(ArrCommonUtil.getYYYY_MM_DDFmt().parse(dayDtStr));
				if(arrangeNo.equals("休") || arrangeNo.equals("假")){
					classDf.setArrangeDf(null);
				}else{
					/***校验车辆持证要求**/
					Map hasChecked = null;
					//取key值
					String key = oldDraft.getDriver().getQuasiDrivingType()+","+scheduleArrange.getArrangeNo();
					//遍历已经校验过的集合
					if(null != quasiArrangeList && !quasiArrangeList.isEmpty()){
						for(Map o:quasiArrangeList){
							if(null == o){
								continue;
							}
							if(o.containsKey(key)){
								hasChecked = o;
							}
						}
					}
					String msg;
					if(null == hasChecked){
						//未校验过，则校验
						msg = this.isQuasiDriving(oldDraft.getDriver().getQuasiDrivingType(), scheduleArrange);
						Map m = new HashMap();
						m.put(key, msg);
						quasiArrangeList.add(m);
					}else{
						//已经校验过，则直接取结果
						msg = (null==hasChecked.get(key)?null:hasChecked.get(key).toString());
					}
					if(!ArrFileUtil.isEmpty(msg)){
						throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.23"
								,"驾驶员所持证件不满足班次代码{0}所配车辆的持证要求{1}",
								scheduleArrange.getArrangeNo(),msg));
					}
					
					//添加到已使用班次集合
					if(scheduleArrange.getIsUsed().compareTo(1) != 0){
						scheduleArrange.setIsUsed(1);
						this.scheduleArrangeDao.update(scheduleArrange);
					}
					scheduleArrange.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					scheduleArrange.setModifiedTm(ArrCommonUtil.currentTm());
					classDf.setArrangeDf(scheduleArrange);
				}
				classDf.setCreatedEmpCode(this.getCurrentUser().getEmployee().getCode());
				classDf.setCreatedTm(current);
				classDfs.add(classDf);
				
			}
			
			//没有修改班次信息则不处理
			if(null == classDfs || classDfs.isEmpty()){
				LogUtils.getInstance().updateLog(oldDraft, preSchedule);
				oldDraft.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
				oldDraft.setModifiedTm(current);
				//oldDraft.setClassType(preSchedule.getClassType());
				continue ;
			}
			PreScheduleDraft newDraft = this.copyBeanProperties(preSchedule);
			if(null == oldDraft.getTransferClassesDFs() || oldDraft.getTransferClassesDFs().isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.24"
						,"数据异常(没有排班明细),异常记录id:{0}",oldDraft.getId()));
			}
			//移除旧明细
			List<TransferClassesDF> needRemoveClass = new ArrayList<TransferClassesDF>();
			String modifiedInfo = "";
			if(null != needRemove && !needRemove.isEmpty()){
				modifiedInfo+="remove:";
				for(Date dt:needRemove){
					if(null == dt){
						continue;
					}
					for(TransferClassesDF old:oldDraft.getTransferClassesDFs()){
						if(null == old || null == old.getDayDt()){
							continue;
						}
						if(dt.compareTo(old.getDayDt()) == 0){
							modifiedInfo+=ArrCommonUtil.getYYYY_MM_DDFmt().format(old.getDayDt())+"("+old.getArrangeNo()+"),";
							needRemoveClass.add(old);
						}
					}
				}
			}
			if(null != needRemoveClass && !needRemoveClass.isEmpty()){
				oldDraft.getTransferClassesDFs().removeAll(needRemoveClass);
			}
			//添加新明细
			if(null != classDfs && !classDfs.isEmpty()){
				modifiedInfo+="add:";
				for(TransferClassesDF df:classDfs){
					if(null == df || null == df.getDayDt()){
						continue;
					}
					modifiedInfo+=ArrCommonUtil.getYYYY_MM_DDFmt().format(df.getDayDt())+"("+df.getArrangeNo()+"),";
				}
				oldDraft.getTransferClassesDFs().addAll(classDfs);
			}
			newDraft.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			newDraft.setModifiedTm(current);
			if(null != modifiedInfo && modifiedInfo.getBytes().length>4000){
				modifiedInfo = modifiedInfo.substring(0,1000);
			}
			newDraft.setModifiedInfo(modifiedInfo);
			newDraft.setDriver(oldDraft.getDriver());
			//记录日志
	        LogUtils.getInstance().updateLog(oldDraft, newDraft);
			oldDraft.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			oldDraft.setModifiedTm(current);
			//oldDraft.setClassType(preSchedule.getClassType());
			oldDraft.setPlanDay(planDays);
			oldDraft.setRealDay(planDays);
			oldDraft.setOne(preSchedule.getOne());
			oldDraft.setTwo(preSchedule.getTwo());
			oldDraft.setThree(preSchedule.getThree());
			oldDraft.setFour(preSchedule.getFour());
			oldDraft.setFive(preSchedule.getFive());
			oldDraft.setSix(preSchedule.getSix());
			oldDraft.setSeven(preSchedule.getSeven());
			oldDraft.setEight(preSchedule.getEight());
			oldDraft.setNine(preSchedule.getNine());
			oldDraft.setTen(preSchedule.getTen());
			oldDraft.setEleven(preSchedule.getEleven());
			oldDraft.setTwelve(preSchedule.getTwelve());
			oldDraft.setThirteen(preSchedule.getThirteen());
			oldDraft.setFourteen(preSchedule.getFourteen());
			oldDraft.setFifteen(preSchedule.getFifteen());
			oldDraft.setSixteen(preSchedule.getSixteen());
			oldDraft.setSeventeen(preSchedule.getSeventeen());
			oldDraft.setEighteen(preSchedule.getEighteen());
			oldDraft.setNineteen(preSchedule.getNineteen());
			oldDraft.setTwenty(preSchedule.getTwenty());
			oldDraft.setTwentyOne(preSchedule.getTwentyOne());
			oldDraft.setTwentyTwo(preSchedule.getTwentyTwo());
			oldDraft.setTwentyThree(preSchedule.getTwentyThree());
			oldDraft.setTwentyFour(preSchedule.getTwentyFour());
			oldDraft.setTwentyFive(preSchedule.getTwentyFive());
			oldDraft.setTwentySix(preSchedule.getTwentySix());
			oldDraft.setTwentySeven(preSchedule.getTwentySeven());
			oldDraft.setTwentyEight(preSchedule.getTwentyEight());
			oldDraft.setTwentyNine(preSchedule.getTwentyNine());
			oldDraft.setThirty(preSchedule.getThirty());
			oldDraft.setThirtyOne(preSchedule.getThirtyOne());
			
		}
	}

	/**
	 * 查找有效网点根据网点ID
	 * 有效：返回true,
	 * 无效:返回false,
	 */
	public Department getDeptById(Long deptId) {
		if(null == deptId){
			return null;
		}
		Department dept = null;
		try{
			dept = DepartmentCacheBiz.getDepartmentByID(deptId);
		}catch(Exception e){
			
		}
		if(null != dept && dept.getDeleteFlg()){
			return null;
		}
		return dept;
	}
	
	/**
	 * 检测是否有数据重复,以及是否连续6天排班
	 */
	@SuppressWarnings({ "rawtypes" })
	public String testRepeat(List<PreSchedule> preSchedules) {
		if(preSchedules == null || preSchedules.isEmpty()){
			return null;
		}
		String msg6 = "";
		boolean msg6Flg;
		List<PreScheduleDraft> dfs = preScheduleDraftDao.listAllByMonth(this.getNextMonth());
		List<String> checkedArrangeDay = new ArrayList<String>();
		for(int i=0;i<preSchedules.size();i++){
			PreSchedule preSchedule = preSchedules.get(i);
			if(null == preSchedule){
				continue;
			}
			//未排班，判断是否连续6天排班
			msg6Flg = isSixDayWork(i, preSchedule);
			if(msg6Flg && null != preSchedule.getDriver()){
				msg6+=preSchedule.getDriver().getEmpCode()+",";
			}
		}
		StringBuilder repeatMsg = new StringBuilder();
		if(!ArrFileUtil.isEmpty(msg6)){
			msg6 = msg6.replaceAll(",$", "");
			repeatMsg.append(msg6+"连续出勤6天，存在疲劳驾驶风险;");
		}
		int count = 0;
		boolean flg = false;
		int max = 62;
		for(int i=0;i<preSchedules.size();i++){
			if(count > max){
				flg = true;
				break;
			}
			PreSchedule preSchedule = preSchedules.get(i);
			List<Map> daysList = this.getDaysData(preSchedule);
			for(int j=0;j<daysList.size();j++){
				Map m = daysList.get(j);
				if(null == m || null == m.get("day") || null == m.get("arrangeNo")){
					continue;
				}
				if(m.get("arrangeNo").toString().equals("休")||m.get("arrangeNo").toString().equals("假")){
					continue;
				}
				String key = m.get("arrangeNo").toString()+"-"+m.get("day").toString();
				if(!checkedArrangeDay.contains(key)){
					String empCodes = this.checkDriverArrange(m.get("arrangeNo").toString(),j,
							preSchedule.getDriver().getEmpCode(),dfs, preSchedules);
					if(!ArrFileUtil.isEmpty(empCodes)){
						if(!"超过10个不予提示请自行核对".equals(empCodes)){
							empCodes = preSchedule.getDriver().getEmpCode()+","+empCodes;
						}else{
							empCodes+=empCodes+"("+preSchedule.getDriver().getEmpCode()+")";
						}
						++count;
						if(count > max){
							flg = true;
							break;
						}
						repeatMsg.append(m.get("day").toString()+"号班次代码("+m.get("arrangeNo").toString()+")重复的驾驶员有:"+empCodes+";");
					}
					checkedArrangeDay.add(key);
				}
			}
		}
		if(flg){
			repeatMsg.append("重复班次过多，已终止校验，请自行核对;");
		}
		return repeatMsg.toString();
	}
	/**
	 * 校验同一天是否多笔
	 * @param arrangeNo
	 * @param day
	 * @param empCode
	 * @param dfs
	 * @param preSchedules
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String checkDriverArrange(String arrangeNo,int day,String empCode,List<PreScheduleDraft> dfs,List<PreSchedule> preSchedules){
		if(ArrFileUtil.isEmpty(arrangeNo) || null == dfs || dfs.isEmpty() || ArrFileUtil.isEmpty(empCode)){
			return null;
		}
		String msg = "";
		int count = 0;
		for(PreScheduleDraft df:dfs){
			if(null == df || null == df.getDriver()){
				continue;
			}
			//不与自己比较
			if(df.getDriver().getEmpCode().equals(empCode)){
				continue;
			}
			PreSchedule pre = this.draftToPre(df);
			List<Map> daysList = this.getDaysData(pre);
			Map m = daysList.get(day);
			if(null == m || null == m.get("day") || null == m.get("arrangeNo")){
				continue;
			}
			if(m.get("arrangeNo").toString().equals(arrangeNo)){
				if(null != pre.getDriver()){
					++count;
					if(count > 15){
						break;
					}
					msg += pre.getDriver().getEmpCode()+",";
				}
			}
		}
		if(null != preSchedules && !preSchedules.isEmpty()){
			for(PreSchedule pre:preSchedules){
				if(null == pre || null == pre.getDriver()){
					continue;
				}
				//不与自己比较
				if(pre.getDriver().getEmpCode().equals(empCode)){
					continue;
				}
				List<Map> daysList = this.getDaysData(pre);
				Map m = daysList.get(day);
				if(null == m || null == m.get("day") || null == m.get("arrangeNo")){
					continue;
				}
				if(m.get("arrangeNo").toString().equals(arrangeNo)){
					if(null != pre.getDriver()){
						++count;
						if(count > 15){
							break;
						}
						msg += pre.getDriver().getEmpCode()+",";
					}
				}
			}
		}
		if(count > 9){
			return "超过10个不予提示请自行核对";
		}
		msg = msg.replaceAll(",$", "");
		return msg;
	}
	
	/**
	 * 根据网点ID，配班班次代码，查找出该用户权限网点下的所有配班班次，填充到下拉框中
	 */
	@SuppressWarnings("rawtypes")
	public Map findArrangeNos(Long deptId,String arrangeNo) {
		return preScheduleDao.findArrangeNos(deptId,arrangeNo,super.getUserId());
	}

	/**
	 * 根据网点ID，驾驶员工号,月度，查找出该用户权限网点下的所有驾驶员，填充到下拉框中
	 */
	@SuppressWarnings("rawtypes")
	public Map findDrivers(Long deptId, String empCode) {
		return preScheduleDao.findDrivers(deptId,empCode,this.getNextMonth(),super.getUserId());
	}
	/**
	 * 判断是否是连续6天排班
	 * @param msg_6
	 * @param i
	 * @param preSchedule
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean isSixDayWork(int i, PreSchedule preSchedule) {
		//收集1号到31号的班次代码，统一判断
		List<Map> daysList = new ArrayList<Map>();
		daysList = getDaysData(preSchedule);
		//是否连续6天安排排班
		int count = 0;
		//判断1号到31号的班次代码是否合法
		for(int j=0;j<daysList.size();j++){
			Map map = (Map) daysList.get(j);
			String arrangeNo = (String) map.get("arrangeNo");
			if(ArrFileUtil.isEmpty(arrangeNo)){
				throw new BizException(I18nEl.i18n_def_arg2("preschedule.biz.25"
						,"第{0}行数据1号到{1}号的班次代码有空值",i+1,daysList.size()));
			}
			if(count >= 6){
				return true;
			}
			if(!arrangeNo.equals("休") && !arrangeNo.equals("假")){
				++count;
			}else{
				count = 0;
			}
		}
		if(count >= 6){
			return true;
		}
		return false;
	}
	//获取指定天的班次代码
	private String getArrangeByDay(PreScheduleDraft pre,int day){
		String arrangeNo = null;
		switch(day){
		case 1:arrangeNo=pre.getOne();break;
		case 2:arrangeNo=pre.getTwo();break;
		case 3:arrangeNo=pre.getThree();break;
		case 4:arrangeNo=pre.getFour();break;
		case 5:arrangeNo=pre.getFive();break;
		case 6:arrangeNo=pre.getSix();break;
		case 7:arrangeNo=pre.getSeven();break;
		case 8:arrangeNo=pre.getEight();break;
		case 9:arrangeNo=pre.getNine();break;
		case 10:arrangeNo=pre.getTen();break;
		case 11:arrangeNo=pre.getEleven();break;
		case 12:arrangeNo=pre.getTwelve();break;
		case 13:arrangeNo=pre.getThirteen();break;
		case 14:arrangeNo=pre.getFourteen();break;
		case 15:arrangeNo=pre.getFifteen();break;
		case 16:arrangeNo=pre.getSixteen();break;
		case 17:arrangeNo=pre.getSeventeen();break;
		case 18:arrangeNo=pre.getEighteen();break;
		case 19:arrangeNo=pre.getNineteen();break;
		case 20:arrangeNo=pre.getTwenty();break;
		case 21:arrangeNo=pre.getTwentyOne();break;
		case 22:arrangeNo=pre.getTwentyTwo();break;
		case 23:arrangeNo=pre.getTwentyThree();break;
		case 24:arrangeNo=pre.getTwentyFour();break;
		case 25:arrangeNo=pre.getTwentyFive();break;
		case 26:arrangeNo=pre.getTwentySix();break;
		case 27:arrangeNo=pre.getTwentySeven();break;
		case 28:arrangeNo=pre.getTwentyEight();break;
		case 29:arrangeNo=pre.getTwentyNine();break;
		case 30:arrangeNo=pre.getThirty();break;
		case 31:arrangeNo=pre.getThirtyOne();break;
		}
		return arrangeNo;
	}
	/**
	 * 获取查看明细按钮的信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findByCondition(Long id, String yearMonth, Integer dayNum) {
		if(FileUtil.isEmpty(yearMonth) || null == dayNum || null == id){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.26","获取明细失败，提交的参数为空，请重试"));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查找主记录
		PreSchedule ps =  this.preScheduleDao.load(id);
		if(null == ps){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.27","获取明细失败，记录不存在，请刷新页面"));
		}
		//获取需调班年月日
		String dateStr = yearMonth;
		int day = dayNum.intValue();
		if(day < 10){
			dateStr += "-"+"0"+day;
		}else{
			dateStr += "-"+day;
		}
        TransferClassesRL newTc = null;
		if(ps!=null){
			Set<TransferClassesRL> set = ps.getTransferClassesRLs();
			if(set != null && set.size() > 0) {
				for(TransferClassesRL tc: set) {
					String dayDt = sdf.format(tc.getDayDt());
					if(dayDt.equals(dateStr)) {
						newTc = tc;
					}
				}
			}
		}
		if(null == newTc){
			return null;
		}
		//封装数据结构给前台方便调用
		Map map = new HashMap();
		map.put("scheduleId", ps.getId());
		map.put("driverCode", ps.getDriver().getEmpCode());
		map.put("deptId",ps.getDepartment().getId());
		map.put("driverName", ps.getDriver().getDriverName());
		map.put("classType", ps.getClassType());
		map.put("arrangeNo", newTc.getArrangeRl()==null ? "" : newTc.getArrangeRl().getArrangeNo());
		map.put("isValid", newTc.getArrangeRl()==null ? "" : newTc.getArrangeRl().getValid());
		map.put("dayDt", newTc.getDayDt());
		map.put("remark", newTc.getRemark());
		List listSi = new ArrayList();
		if(null != newTc.getArrangeRl() && null != newTc.getArrangeRl().getScheduleArrangeInfos()
				&& !newTc.getArrangeRl().getScheduleArrangeInfos().isEmpty()){
			for(ScheduleInfoArrange sia:newTc.getArrangeRl().getScheduleArrangeInfos()){
				ScheduleInfo si = sia.getScheduleInfo();
				Map temp = new HashMap();
				temp.put("startTm", si.getStartTm());
				temp.put("endTm", si.getEndTm());
				if(null != si.getStartDept()){
					temp.put("startDept", si.getStartDept().getDeptCode()+"/"+si.getStartDept().getDeptName());
				}
				if(null != si.getEndDept()){
					temp.put("endDept", si.getEndDept().getDeptCode()+"/"+si.getEndDept().getDeptName());
				}
				temp.put("valid", si.getValid());
				listSi.add(temp);
			}
			
		}
		//机动班
		if(null != newTc.getArrangeRl()
				&& newTc.getArrangeRl().getArrangeType().compareTo(1)==0
				&& (null ==  newTc.getArrangeRl().getScheduleArrangeInfos() 
						|| newTc.getArrangeRl().getScheduleArrangeInfos().isEmpty())){
			Map temp = new HashMap();
			temp.put("startTm", newTc.getArrangeRl().getStartTm());
			temp.put("endTm", newTc.getArrangeRl().getEndTm());
			temp.put("valid",  newTc.getArrangeRl().getValid());
			listSi.add(temp);
		}
		map.put("scheduleInfos", listSi);
		return map;
	}

	/**
	 * 获取调班过程日志信息
	 */
	public List<Object> findOptInfoByCondition(Long id, String yearMonth,
			String dayNum) {
		if(id==0 || id ==null){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.28","查看调班过程日志的ID为空"));
		}
		if(StringUtil.isEmpty(yearMonth) || StringUtil.isEmpty(dayNum)){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.29","查看调班过程日志的年月日为空"));
		}
		int month = Integer.parseInt(yearMonth.substring(yearMonth.indexOf("-")+1));
		int year = Integer.parseInt(yearMonth.substring(0, yearMonth.indexOf("-")));
		int day = Integer.parseInt(dayNum);
		Calendar c = Calendar.getInstance();  
        c.set(year, month-1, day);
        Date dayDt = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(dayDt);
        List<Object> list = preScheduleDao.findOptInfo(id,dateStr);
		return list;
	}
	/**
	 * 调班页面中的获取被调班人的信息
	 */
	public List<Object> findOptDriver(String optEmpCode, String optDate,Long deptId) {
		if(null == optEmpCode || optEmpCode.equals("")){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.30","查看调班过程的驾驶员为空"));
		}
		if(null == optDate || optDate.equals("")){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.29","查看调班过程的年月日为空"));
		}
		if(null == deptId || deptId ==0){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.28","查看调班过程的记录ID为空"));
		}
		String deptCode = "";
		Department dept = DepartmentCacheBiz.getDepartmentByID(deptId);
		if(dept!=null){
			deptCode = dept.getDeptCode();
		}
		List<Object> list = this.preScheduleDao.findOptDriver(optEmpCode,optDate,deptCode,super.getUserId());
		return list;
	}

	/**
	 * 调班保存
	 * @throws ParseException 
	 */
	public void saveOptInfo(ParamsDto paramsDto,Integer ignore) throws Exception {
		if(null == paramsDto){
			throw new BizException(I18nEl.i18n_def("common.biz.nullObj","提交的参数为空，请重新提交"));
		}
		if(null == paramsDto.getFlag()){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.31","调班类型不能为空"));
		}
		String flag = paramsDto.getFlag();
		if(!flag.equals("1") && !flag.equals("2") && !flag.equals("3")){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.32","调班类型取值有误，取值范围:1,2,3"));
		}
		if(ArrFileUtil.isEmpty(paramsDto.getDayDtOld())){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.33","需调班人的日期不能为空"));
		}
		if(null == paramsDto.getScheduleIdOld()){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.34","需调班人的记录ID不能为空"));
		}
		//需调班人的记录ID
		Long scheduleIdOld = paramsDto.getScheduleIdOld();
		//需调班人的日期
		String dayDtOld = paramsDto.getDayDtOld();
		//请假类型
		String vacationComboValue = paramsDto.getVacationComboValue();
		//需调班人的对象信息
		PreSchedule preScheduleOld = this.preScheduleDao.load(scheduleIdOld);
		if(preScheduleOld == null ){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.35","需调班记录不存在，请刷新页面"));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(!dayDtOld.matches("^\\d{4}-\\d{2}-\\d{2}$")){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.36","需调班人日期格式不对，正确格式例如:2014-06-01"));
		}
		String dayStr = dayDtOld.substring(dayDtOld.lastIndexOf("-")+1);
		int day = Integer.parseInt(dayStr);
		if(day < 0 || day > 31){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.37","需调班人日期格式不对:日期不能大于31号或小于0号"));
		}
		//根据调班类型分三种情况去调班：请假、调休、调班
		//1.请假
		if(flag.equals("1")){
			if(ArrFileUtil.isEmpty(vacationComboValue)){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.38","请假类型不能为空"));
			}
			//更新需调班人的班次代码假
			switch(day){
			case 1:preScheduleOld.setOne("假");break;
			case 2:preScheduleOld.setTwo("假");break;
			case 3:preScheduleOld.setThree("假");break;
			case 4:preScheduleOld.setFour("假");break;
			case 5:preScheduleOld.setFive("假");break;
			case 6:preScheduleOld.setSix("假");break;
			case 7:preScheduleOld.setSeven("假");break;
			case 8:preScheduleOld.setEight("假");break;
			case 9:preScheduleOld.setNine("假");break;
			case 10:preScheduleOld.setTen("假");break;
			case 11:preScheduleOld.setEleven("假");break;
			case 12:preScheduleOld.setTwelve("假");break;
			case 13:preScheduleOld.setThirteen("假");break;
			case 14:preScheduleOld.setFourteen("假");break;
			case 15:preScheduleOld.setFifteen("假");break;
			case 16:preScheduleOld.setSixteen("假");break;
			case 17:preScheduleOld.setSeventeen("假");break;
			case 18:preScheduleOld.setEighteen("假");break;
			case 19:preScheduleOld.setNineteen("假");break;
			case 20:preScheduleOld.setTwenty("假");break;
			case 21:preScheduleOld.setTwentyOne("假");break;
			case 22:preScheduleOld.setTwentyTwo("假");break;
			case 23:preScheduleOld.setTwentyThree("假");break;
			case 24:preScheduleOld.setTwentyFour("假");break;
			case 25:preScheduleOld.setTwentyFive("假");break;
			case 26:preScheduleOld.setTwentySix("假");break;
			case 27:preScheduleOld.setTwentySeven("假");break;
			case 28:preScheduleOld.setTwentyEight("假");break;
			case 29:preScheduleOld.setTwentyNine("假");break;
			case 30:preScheduleOld.setThirty("假");break;
			case 31:preScheduleOld.setThirtyOne("假");break;
			}
			//子表中查找出具体哪一天的数据，进行更新
			if(null == preScheduleOld.getTransferClassesRLs() || preScheduleOld.getTransferClassesRLs().isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.24"
						,"数据异常(排班明细为空),异常记录ID:{0}",preScheduleOld.getId()));
			}
			for(TransferClassesRL rl:preScheduleOld.getTransferClassesRLs()){
				if(null == rl || null == rl.getId()){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(sdf.format(rl.getDayDt()).equals(dayDtOld)){
					if(null == rl.getArrangeNo()){
						throw new BizException(I18nEl.i18n_def("preschedule.biz.40","需调班人指定日期未排班，请刷新页面"));
					}
					if(rl.getArrangeNo().equals("休")||rl.getArrangeNo().equals("假")){
						throw new BizException(I18nEl.i18n_def("preschedule.biz.41","需调班人指定日期为休或假，不允许调班，请刷新页面"));
					}
					//这个remark值必须先赋值,获取之前的班次代码做记录
					rl.setSafeRemark(rl.getArrangeNo() + "-->" + vacationComboValue);
					//设置班次代码为：假
					rl.setArrangeNo("假");
					//设置班次id为空
					rl.setArrangeRl(null);
					rl.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					rl.setModifiedTm(ArrCommonUtil.currentTm());
				}
			}
			//实际休息天数加1
			preScheduleOld.setRealDay(preScheduleOld.getRealDay()+1);
			preScheduleOld.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			preScheduleOld.setModifiedTm(ArrCommonUtil.currentTm());
		}
		//2.休
		if(flag.equals("2")){
			//更新需调班人为:休
			switch(day){
			case 1:preScheduleOld.setOne("休");break;
			case 2:preScheduleOld.setTwo("休");break;
			case 3:preScheduleOld.setThree("休");break;
			case 4:preScheduleOld.setFour("休");break;
			case 5:preScheduleOld.setFive("休");break;
			case 6:preScheduleOld.setSix("休");break;
			case 7:preScheduleOld.setSeven("休");break;
			case 8:preScheduleOld.setEight("休");break;
			case 9:preScheduleOld.setNine("休");break;
			case 10:preScheduleOld.setTen("休");break;
			case 11:preScheduleOld.setEleven("休");break;
			case 12:preScheduleOld.setTwelve("休");break;
			case 13:preScheduleOld.setThirteen("休");break;
			case 14:preScheduleOld.setFourteen("休");break;
			case 15:preScheduleOld.setFifteen("休");break;
			case 16:preScheduleOld.setSixteen("休");break;
			case 17:preScheduleOld.setSeventeen("休");break;
			case 18:preScheduleOld.setEighteen("休");break;
			case 19:preScheduleOld.setNineteen("休");break;
			case 20:preScheduleOld.setTwenty("休");break;
			case 21:preScheduleOld.setTwentyOne("休");break;
			case 22:preScheduleOld.setTwentyTwo("休");break;
			case 23:preScheduleOld.setTwentyThree("休");break;
			case 24:preScheduleOld.setTwentyFour("休");break;
			case 25:preScheduleOld.setTwentyFive("休");break;
			case 26:preScheduleOld.setTwentySix("休");break;
			case 27:preScheduleOld.setTwentySeven("休");break;
			case 28:preScheduleOld.setTwentyEight("休");break;
			case 29:preScheduleOld.setTwentyNine("休");break;
			case 30:preScheduleOld.setThirty("休");break;
			case 31:preScheduleOld.setThirtyOne("休");break;
			}
			//子表中查找出具体哪一天的数据，进行更新
			if(null == preScheduleOld.getTransferClassesRLs() || preScheduleOld.getTransferClassesRLs().isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.24"
						,"数据异常(排班明细为空),异常记录ID:{0}",preScheduleOld.getId()));
			}
			for(TransferClassesRL rl:preScheduleOld.getTransferClassesRLs()){
				if(null == rl || null == rl.getId()){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(sdf.format(rl.getDayDt()).equals(dayDtOld)){
					if(null == rl.getArrangeNo()){
						throw new BizException(I18nEl.i18n_def("preschedule.biz.40","需调班人指定日期未排班，请刷新页面"));
					}
					if(rl.getArrangeNo().equals("休")||rl.getArrangeNo().equals("假")){
						throw new BizException(I18nEl.i18n_def("preschedule.biz.41","需调班人指定日期为休或假，不允许调班，请刷新页面"));
					}
					//这个remark值必须先赋值,获取之前的班次代码做记录
					rl.setSafeRemark(rl.getArrangeNo() + "-->" + "休");
					//设置班次代码为：休
					rl.setArrangeNo("休");
					//设置班次id为空
					rl.setArrangeRl(null);
					rl.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
					rl.setModifiedTm(ArrCommonUtil.currentTm());
				}
			}
			//实际休息天数加1
			preScheduleOld.setRealDay(preScheduleOld.getRealDay()+1);
			preScheduleOld.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			preScheduleOld.setModifiedTm(ArrCommonUtil.currentTm());
		}
		//标识需调班人和被调班人是否为同一个人
		boolean sameFlg = false;
		if(null != paramsDto.getScheduleIdOld() && null != paramsDto.getScheduleId()){
			if(paramsDto.getScheduleIdOld().compareTo(paramsDto.getScheduleId()) == 0){
				sameFlg = true;
			}
		}
		//3.调班-和自己调
		if(flag.equals("3") && sameFlg){
			throw new BizException(I18nEl.i18n_def("preschedule.biz.42","自己不能与自己调班"));
		}
		//4.调班-和别人调班
		if(flag.equals("3") && !sameFlg){
			if(null == paramsDto.getScheduleId()){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.43","被调班人的记录ID不能为空"));
			}
			//被调班人的记录ID
			Long scheduleId = paramsDto.getScheduleId();
			//1、查找需调班子表中查找出具体哪一天的数据
			TransferClassesRL oldClass = null;
			if(null == preScheduleOld.getTransferClassesRLs() || preScheduleOld.getTransferClassesRLs().isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.24"
						,"数据异常(排班明细为空),异常记录ID:{0}",preScheduleOld.getId()));
			}
			for(TransferClassesRL rl:preScheduleOld.getTransferClassesRLs()){
				if(null == rl || null == rl.getId()){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(sdf.format(rl.getDayDt()).equals(dayDtOld)){
					oldClass = rl;
				}
			}
			if(null == oldClass){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.44","未找到需调班人指定日期排班明细"));
			}
			if(null == oldClass.getArrangeNo()){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.40","需调班人指定日期未排班，请刷新页面"));
			}
			if(oldClass.getArrangeNo().equals("休")||oldClass.getArrangeNo().equals("假")){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.41","需调班人指定日期为休或假，不允许调班，请刷新页面"));
			}
			//2、查找被调班子表中查找出具体哪一天的数据
			PreSchedule preSchedule = this.preScheduleDao.load(scheduleId);
			if(null == preSchedule){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.45","被调班人记录不存在，请刷新页面"));
			}
			if(null == preSchedule.getTransferClassesRLs() || preSchedule.getTransferClassesRLs().isEmpty()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.24"
						,"数据异常(排班明细为空),异常记录ID:{0}",preSchedule.getId()));
			}
			TransferClassesRL newClass = null;
			for(TransferClassesRL rl:preSchedule.getTransferClassesRLs()){
				if(null == rl || null == rl.getId()){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(sdf.format(rl.getDayDt()).equals(dayDtOld)){
					newClass = rl;
				}
			}
			if(null == newClass){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.46","未找到被调班人指定日期排班明细"));
			}
			if(null == newClass.getArrangeNo()){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.47","被调班人指定日期未排班，请刷新页面"));
			}
			if(newClass.getArrangeNo().equals("假")){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.48","被调班人指定日期为假，不允许调班"));
			}
			if(oldClass.getArrangeNo().equals(newClass.getArrangeNo())){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.49","被调班人和需调班人是同一个班次代码，不允许调班"));
			}
			//需调班人不是机动班，则不能调为机动
			if(preScheduleOld.getClassType().compareTo(3)!=0 && newClass.getArrangeNo().indexOf("机动")!=-1){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.91","需调班人班次类别不为机动时，不能调为机动班次"));
			}
			//被调班人不是机动班，则不能调为机动
			if(preSchedule.getClassType().compareTo(3)!=0 && oldClass.getArrangeNo().indexOf("机动")!=-1){
				throw new BizException(I18nEl.i18n_def("preschedule.biz.92","被调班人班次类别不为机动时，不能调为机动班次"));
			}
			/***判断准驾车型和持证要求是否相符**/
			//3、需调班人的准驾车型是否符合新调班的准驾驶车型
			if(null == preScheduleOld.getDriver()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.50"
						,"数据异常(驾驶员为空),异常记录ID:{0}",preScheduleOld.getId()));
			}
			String quasiDrivingTypeOld = preScheduleOld.getDriver().getQuasiDrivingType();
			if(null != newClass.getArrangeRl()){
				String msg = this.isQuasiDriving(quasiDrivingTypeOld, newClass.getArrangeRl());
				if(!ArrFileUtil.isEmpty(msg)){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.51"
							,"需调班人所持证件不满足新班次的车辆持证要求{0}",msg));
				}
			}
			//4、被调班人的准驾车型是否符合新调班的准驾驶车型
			if(null == preSchedule.getDriver()){
				throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.50"
						,"数据异常(驾驶员为空),异常记录ID:{0}",preScheduleOld.getId()));
			}
			String quasiDrivingType = preSchedule.getDriver().getQuasiDrivingType();
			if(null != oldClass.getArrangeRl()){
				String msg = this.isQuasiDriving(quasiDrivingType, oldClass.getArrangeRl());
				if(!ArrFileUtil.isEmpty(msg)){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.52"
							,"被调班人所持证件不满足新班次的车辆持证要求{0}",msg));
				}
			}
			//5.调换两个人的班次信息
			preScheduleOld.getTransferClassesRLs().remove(oldClass);
			preSchedule.getTransferClassesRLs().remove(newClass);
			//需调班人的工号
			String empCodeOld = preScheduleOld.getDriver().getEmpCode();
			//被调班人的工号
			String empCode = preSchedule.getDriver().getEmpCode();
			//需调班人的备注
			String remarkOld = oldClass.getArrangeNo()+
			"-->"+newClass.getArrangeNo()+"(原归属于:"+empCode+")";
			//被调班人的备注
			String remark = newClass.getArrangeNo()+
			"-->"+oldClass.getArrangeNo()+"(原归属于:"+empCodeOld+")";
			//需调班人信息
			TransferClassesRL newClassCopy = new TransferClassesRL();
			newClassCopy.setArrangeNo(newClass.getArrangeNo());
			newClassCopy.setArrangeRl(newClass.getArrangeRl());
			newClassCopy.setCreatedEmpCode(newClass.getCreatedEmpCode());
			newClassCopy.setCreatedTm(newClass.getCreatedTm());
			newClassCopy.setDayDt(sdf.parse(dayDtOld));
			newClassCopy.setIsTransfer(1);
			newClassCopy.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			newClassCopy.setModifiedTm(new Date());
			newClassCopy.setSafeRemark(remarkOld);
			preScheduleOld.getTransferClassesRLs().add(newClassCopy);
			//被调班人信息
			TransferClassesRL oldClassCopy = new TransferClassesRL();
			oldClassCopy.setArrangeNo(oldClass.getArrangeNo());
			oldClassCopy.setArrangeRl(oldClass.getArrangeRl());
			oldClassCopy.setCreatedEmpCode(oldClass.getCreatedEmpCode());
			oldClassCopy.setCreatedTm(oldClass.getCreatedTm());
			oldClassCopy.setDayDt(sdf.parse(dayDtOld));
			oldClassCopy.setIsTransfer(1);
			oldClassCopy.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			oldClassCopy.setModifiedTm(new Date());
			oldClassCopy.setSafeRemark(remark);
			preSchedule.getTransferClassesRLs().add(oldClassCopy);
			
			//6.更新主表对应日期的信息
			switch(day){
			case 1:preScheduleOld.setOne(newClass.getArrangeNo());break;
			case 2:preScheduleOld.setTwo(newClass.getArrangeNo());break;
			case 3:preScheduleOld.setThree(newClass.getArrangeNo());break;
			case 4:preScheduleOld.setFour(newClass.getArrangeNo());break;
			case 5:preScheduleOld.setFive(newClass.getArrangeNo());break;
			case 6:preScheduleOld.setSix(newClass.getArrangeNo());break;
			case 7:preScheduleOld.setSeven(newClass.getArrangeNo());break;
			case 8:preScheduleOld.setEight(newClass.getArrangeNo());break;
			case 9:preScheduleOld.setNine(newClass.getArrangeNo());break;
			case 10:preScheduleOld.setTen(newClass.getArrangeNo());break;
			case 11:preScheduleOld.setEleven(newClass.getArrangeNo());break;
			case 12:preScheduleOld.setTwelve(newClass.getArrangeNo());break;
			case 13:preScheduleOld.setThirteen(newClass.getArrangeNo());break;
			case 14:preScheduleOld.setFourteen(newClass.getArrangeNo());break;
			case 15:preScheduleOld.setFifteen(newClass.getArrangeNo());break;
			case 16:preScheduleOld.setSixteen(newClass.getArrangeNo());break;
			case 17:preScheduleOld.setSeventeen(newClass.getArrangeNo());break;
			case 18:preScheduleOld.setEighteen(newClass.getArrangeNo());break;
			case 19:preScheduleOld.setNineteen(newClass.getArrangeNo());break;
			case 20:preScheduleOld.setTwenty(newClass.getArrangeNo());break;
			case 21:preScheduleOld.setTwentyOne(newClass.getArrangeNo());break;
			case 22:preScheduleOld.setTwentyTwo(newClass.getArrangeNo());break;
			case 23:preScheduleOld.setTwentyThree(newClass.getArrangeNo());break;
			case 24:preScheduleOld.setTwentyFour(newClass.getArrangeNo());break;
			case 25:preScheduleOld.setTwentyFive(newClass.getArrangeNo());break;
			case 26:preScheduleOld.setTwentySix(newClass.getArrangeNo());break;
			case 27:preScheduleOld.setTwentySeven(newClass.getArrangeNo());break;
			case 28:preScheduleOld.setTwentyEight(newClass.getArrangeNo());break;
			case 29:preScheduleOld.setTwentyNine(newClass.getArrangeNo());break;
			case 30:preScheduleOld.setThirty(newClass.getArrangeNo());break;
			case 31:preScheduleOld.setThirtyOne(newClass.getArrangeNo());break;
			}
			switch(day){
			case 1:preSchedule.setOne(oldClass.getArrangeNo());break;
			case 2:preSchedule.setTwo(oldClass.getArrangeNo());break;
			case 3:preSchedule.setThree(oldClass.getArrangeNo());break;
			case 4:preSchedule.setFour(oldClass.getArrangeNo());break;
			case 5:preSchedule.setFive(oldClass.getArrangeNo());break;
			case 6:preSchedule.setSix(oldClass.getArrangeNo());break;
			case 7:preSchedule.setSeven(oldClass.getArrangeNo());break;
			case 8:preSchedule.setEight(oldClass.getArrangeNo());break;
			case 9:preSchedule.setNine(oldClass.getArrangeNo());break;
			case 10:preSchedule.setTen(oldClass.getArrangeNo());break;
			case 11:preSchedule.setEleven(oldClass.getArrangeNo());break;
			case 12:preSchedule.setTwelve(oldClass.getArrangeNo());break;
			case 13:preSchedule.setThirteen(oldClass.getArrangeNo());break;
			case 14:preSchedule.setFourteen(oldClass.getArrangeNo());break;
			case 15:preSchedule.setFifteen(oldClass.getArrangeNo());break;
			case 16:preSchedule.setSixteen(oldClass.getArrangeNo());break;
			case 17:preSchedule.setSeventeen(oldClass.getArrangeNo());break;
			case 18:preSchedule.setEighteen(oldClass.getArrangeNo());break;
			case 19:preSchedule.setNineteen(oldClass.getArrangeNo());break;
			case 20:preSchedule.setTwenty(oldClass.getArrangeNo());break;
			case 21:preSchedule.setTwentyOne(oldClass.getArrangeNo());break;
			case 22:preSchedule.setTwentyTwo(oldClass.getArrangeNo());break;
			case 23:preSchedule.setTwentyThree(oldClass.getArrangeNo());break;
			case 24:preSchedule.setTwentyFour(oldClass.getArrangeNo());break;
			case 25:preSchedule.setTwentyFive(oldClass.getArrangeNo());break;
			case 26:preSchedule.setTwentySix(oldClass.getArrangeNo());break;
			case 27:preSchedule.setTwentySeven(oldClass.getArrangeNo());break;
			case 28:preSchedule.setTwentyEight(oldClass.getArrangeNo());break;
			case 29:preSchedule.setTwentyNine(oldClass.getArrangeNo());break;
			case 30:preSchedule.setThirty(oldClass.getArrangeNo());break;
			case 31:preSchedule.setThirtyOne(oldClass.getArrangeNo());break;
			}
			//校验是否连续6天排班
			if(null != ignore && ignore.compareTo(2) == 0){
				List<TransferClassesRL> rls = new ArrayList<TransferClassesRL>();
				rls.addAll(preSchedule.getTransferClassesRLs());
				List<TransferClassesRL> oldRls = new ArrayList<TransferClassesRL>();
				oldRls.addAll(preScheduleOld.getTransferClassesRLs());
				boolean day6New = check6Days(rls);
				boolean day6Old = false;
				//被调班人有班次，则校验需调班人
				if(null != newClass.getArrangeRl() && null != newClass.getArrangeRl().getId()){
					day6Old = check6Days(oldRls);
				}
				if(day6New && day6Old){
					throw new BizException("day6");
				}
				if(day6Old){
					throw new BizException("day6Old");
				}
				if(day6New){
					throw new BizException("day6New");
				}
			}
			/***计算排班吻合率**/
			//7、需调班人的排班吻合率
			int countOld = 0;
			for(TransferClassesRL rl:preScheduleOld.getTransferClassesRLs()){
				if(null == rl){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(null != rl.getIsTransfer() && rl.getIsTransfer().compareTo(1) == 0){
					countOld += 1;
				}
			}
			//吻合率=（月度天数-调班）/月度天数*100%
			int sizeOld = preScheduleOld.getTransferClassesRLs().size();
			double rateOld = ((double)(sizeOld-countOld)/(double)(sizeOld)) *100;
			BigDecimal rateBigOld = new BigDecimal(rateOld);
			rateBigOld = rateBigOld.setScale(2,BigDecimal.ROUND_HALF_UP);
			preScheduleOld.setRate(rateBigOld.toString());
			
			//8、被调班人的排班吻合率
			int countNew = 0;
			for(TransferClassesRL rl:preSchedule.getTransferClassesRLs()){
				if(null == rl){
					continue;
				}
				if(null == rl.getDayDt()){
					throw new BizException(I18nEl.i18n_def_arg1("preschedule.biz.39"
							,"数据异常(排班明细日期为空),异常记录ID:{0}",rl.getId()));
				}
				if(null != rl.getIsTransfer() && rl.getIsTransfer().compareTo(1) == 0){
					countNew += 1;
				}
			}
			//吻合率=（月度天数-调班）/月度天数*100%
			int sizeNew = preScheduleOld.getTransferClassesRLs().size();
			double rateNew = ((double)(sizeNew-countNew)/(double)(sizeNew)) *100;
			BigDecimal rateBigNew = new BigDecimal(rateNew);
			rateBigNew = rateBigNew.setScale(2,BigDecimal.ROUND_HALF_UP);
			preSchedule.setRate(rateBigNew.toString());
			//9、计算休息天数
			if(null == newClass.getArrangeRl() || null == newClass.getArrangeRl().getId()){
				//被调班人为休或假，则需调班人实际休息日加1，被调班人实际休息日减1
				preScheduleOld.setRealDay(preScheduleOld.getRealDay()+1);
				preSchedule.setRealDay(preSchedule.getRealDay()-1);
			}
			preScheduleOld.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			preScheduleOld.setModifiedTm(ArrCommonUtil.currentTm());
			preSchedule.setModifiedEmpCode(this.getCurrentUser().getEmployee().getCode());
			preSchedule.setModifiedTm(ArrCommonUtil.currentTm());
		}
	}
	//判断是否连续6天上班
	private boolean check6Days(List<TransferClassesRL> list){
		if(null == list || list.isEmpty()){
			return false;
		}
		list = this.sortClassList(list);
		int count = 0;
		for(TransferClassesRL t:list){
			if(null == t){
				continue;
			}
			if(count >= 6){
				return true;
			}
			if(null != t.getArrangeNo() && !t.getArrangeNo().equals("休") && !t.getArrangeNo().equals("假")){
				++count;
			}else{
				count = 0;
			}
		}
		if(count >= 6){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取新增页面标题的区部分部
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findAreaDeptName(Long deptId) {
		Department dept = this.getDeptById(deptId);
		if(dept==null){
			return new HashMap();
		}
		Map map = new HashMap();
		map.put("deptName", dept.getDeptName());
		map.put("deptCode", dept.getDeptCode());
		Department deptArea = DepartmentCacheBiz.getDepartmentByCode(dept.getAreaDeptCode());
		if(null!=deptArea && !deptArea.getDeleteFlg()){
			map.put("areaDeptName", deptArea.getDeptName());
			map.put("areaDeptCode", deptArea.getDeptCode());
		}
		return map;
	}
	/**
	 * 获取1号到31号的数据，集合起来，方便校验
	 * @param preSchedule
	 * @param daysList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> getDaysData(PreSchedule preSchedule) {
		List<Map> daysList = new ArrayList<Map>();
		//获取次月
		String yearMonth = getNextMonth();
		int month = Integer.parseInt(yearMonth.substring(yearMonth.indexOf("-")+1));
		int year = Integer.parseInt(yearMonth.substring(0, yearMonth.indexOf("-")));
		int maxDate = maxDate(year,month);
		
		Map daysMap;
		//1号
		daysMap = new HashMap(); 
		daysMap.put("day", "1");
		daysMap.put("arrangeNo", preSchedule.getOne());
		daysList.add(daysMap);
		//2号
		daysMap = new HashMap();
		daysMap.put("day", "2");
		daysMap.put("arrangeNo", preSchedule.getTwo());
		daysList.add(daysMap);
		//3号
		daysMap = new HashMap();
		daysMap.put("day", "3");
		daysMap.put("arrangeNo", preSchedule.getThree());
		daysList.add(daysMap);
		//4号
		daysMap = new HashMap();
		daysMap.put("day", "4");
		daysMap.put("arrangeNo", preSchedule.getFour());
		daysList.add(daysMap);
		//5号
		daysMap = new HashMap();
		daysMap.put("day", "5");
		daysMap.put("arrangeNo", preSchedule.getFive());
		daysList.add(daysMap);
		//6号
		daysMap = new HashMap();
		daysMap.put("day", "6");
		daysMap.put("arrangeNo", preSchedule.getSix());
		daysList.add(daysMap);
		//7号
		daysMap = new HashMap();
		daysMap.put("day", "7");
		daysMap.put("arrangeNo", preSchedule.getSeven());
		daysList.add(daysMap);
		//8号
		daysMap = new HashMap();
		daysMap.put("day", "8");
		daysMap.put("arrangeNo", preSchedule.getEight());
		daysList.add(daysMap);
		//9号
		daysMap = new HashMap();
		daysMap.put("day", "9");
		daysMap.put("arrangeNo", preSchedule.getNine());
		daysList.add(daysMap);
		//10号
		daysMap = new HashMap();
		daysMap.put("day", "10");
		daysMap.put("arrangeNo", preSchedule.getTen());
		daysList.add(daysMap);
		//11号
		daysMap = new HashMap();
		daysMap.put("day", "11");
		daysMap.put("arrangeNo", preSchedule.getEleven());
		daysList.add(daysMap);
		//12号
		daysMap = new HashMap();
		daysMap.put("day", "12");
		daysMap.put("arrangeNo", preSchedule.getTwelve());
		daysList.add(daysMap);
		//13号
		daysMap = new HashMap();
		daysMap.put("day", "13");
		daysMap.put("arrangeNo", preSchedule.getThirteen());
		daysList.add(daysMap);
		//14号
		daysMap = new HashMap();
		daysMap.put("day", "14");
		daysMap.put("arrangeNo", preSchedule.getFourteen());
		daysList.add(daysMap);
		//15号
		daysMap = new HashMap();
		daysMap.put("day", "15");
		daysMap.put("arrangeNo", preSchedule.getFifteen());
		daysList.add(daysMap);
		//16号
		daysMap = new HashMap();
		daysMap.put("day", "16");
		daysMap.put("arrangeNo", preSchedule.getSixteen());
		daysList.add(daysMap);
		//17号
		daysMap = new HashMap();
		daysMap.put("day", "17");
		daysMap.put("arrangeNo", preSchedule.getSeventeen());
		daysList.add(daysMap);
		//18号
		daysMap = new HashMap();
		daysMap.put("day", "18");
		daysMap.put("arrangeNo", preSchedule.getEighteen());
		daysList.add(daysMap);
		//19号
		daysMap = new HashMap();
		daysMap.put("day", "19");
		daysMap.put("arrangeNo", preSchedule.getNineteen());
		daysList.add(daysMap);
		//20号
		daysMap = new HashMap();
		daysMap.put("day", "20");
		daysMap.put("arrangeNo", preSchedule.getTwenty());
		daysList.add(daysMap);
		//21号
		daysMap = new HashMap();
		daysMap.put("day", "21");
		daysMap.put("arrangeNo", preSchedule.getTwentyOne());
		daysList.add(daysMap);
		//22号
		daysMap = new HashMap();
		daysMap.put("day", "22");
		daysMap.put("arrangeNo", preSchedule.getTwentyTwo());
		daysList.add(daysMap);
		//23号
		daysMap = new HashMap();
		daysMap.put("day", "23");
		daysMap.put("arrangeNo", preSchedule.getTwentyThree());
		daysList.add(daysMap);
		//24号
		daysMap = new HashMap();
		daysMap.put("day", "24");
		daysMap.put("arrangeNo", preSchedule.getTwentyFour());
		daysList.add(daysMap);
		//25号
		daysMap = new HashMap();
		daysMap.put("day", "25");
		daysMap.put("arrangeNo", preSchedule.getTwentyFive());
		daysList.add(daysMap);
		//26号
		daysMap = new HashMap();
		daysMap.put("day", "26");
		daysMap.put("arrangeNo", preSchedule.getTwentySix());
		daysList.add(daysMap);
		//27号
		daysMap = new HashMap();
		daysMap.put("day", "27");
		daysMap.put("arrangeNo", preSchedule.getTwentySeven());
		daysList.add(daysMap);
		//28号
		daysMap = new HashMap();
		daysMap.put("day", "28");
		daysMap.put("arrangeNo", preSchedule.getTwentyEight());
		daysList.add(daysMap);
		if(maxDate==29){
			//29号
			daysMap = new HashMap();
			daysMap.put("day", "29");
			daysMap.put("arrangeNo", preSchedule.getTwentyNine());
			daysList.add(daysMap);
		}
		if(maxDate==30){
			//29号
			daysMap = new HashMap();
			daysMap.put("day", "29");
			daysMap.put("arrangeNo", preSchedule.getTwentyNine());
			daysList.add(daysMap);
			//30号
			daysMap = new HashMap();
			daysMap.put("day", "30");
			daysMap.put("arrangeNo", preSchedule.getThirty());
			daysList.add(daysMap);
		}
		if(maxDate==31){
			//29号
			daysMap = new HashMap();
			daysMap.put("day", "29");
			daysMap.put("arrangeNo", preSchedule.getTwentyNine());
			daysList.add(daysMap);
			//30号
			daysMap = new HashMap();
			daysMap.put("day", "30");
			daysMap.put("arrangeNo", preSchedule.getThirty());
			daysList.add(daysMap);
			//31号
			daysMap = new HashMap();
			daysMap.put("day", "31");
			daysMap.put("arrangeNo", preSchedule.getThirtyOne());
			daysList.add(daysMap);
		}
		return daysList;
	}
	/**
	 * 验是否符合准驾车型
	 * @param quasiDrivingType 驾驶员持证
	 * @param scheduleArrange 配班信息
	 * @return
	 */
	private String isQuasiDriving(String quasiDrivingType,ScheduleArrange scheduleArrange){
		//班次为休或假，默认符合
		if(null == scheduleArrange || null == scheduleArrange.getScheduleArrangeInfos() 
				|| scheduleArrange.getScheduleArrangeInfos().isEmpty()){
			return null;
		}
		//持证为空，不符合
		if(ArrFileUtil.isEmpty(quasiDrivingType)){
			return "(驾驶员持证为空)";
		}
		String[] quasiDrivingTypes = quasiDrivingType.split(",");
		for(ScheduleInfoArrange infoArr:scheduleArrange.getScheduleArrangeInfos()){
			if(null == infoArr || null == infoArr.getScheduleInfo()
					|| null == infoArr.getScheduleInfo().getVehicle() 
					|| null == infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType()
					|| ArrFileUtil.isEmpty(infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType().getHolderCertsNeed())){
				continue;
			}
			String holderCertsNeed = infoArr.getScheduleInfo().getVehicle().getVehicleDrivingType().getHolderCertsNeed();
			String[] holderCertsNeeds = holderCertsNeed.split(",");
			//班次明细任意一笔持证要求不符合，则返回false
			if(!compareArr(holderCertsNeeds,quasiDrivingTypes)){
				return String.format("(车辆持证要求为%s/驾驶员持证为%s)",holderCertsNeed,quasiDrivingType);
			}
		}
		//校验通过，符合
		return null;
	}
	
	/**
	 * 判断是否包含持证要求
	 * @param strs1
	 * @param strs2
	 * @return
	 */
	private boolean compareArr(String[] need,String[] owner){
		//持证要求和所持证都为空，符合
		if((null == need || need.length < 1) && (null == owner || owner.length < 1)){
			return true;
		}
		//持证要求不为空和所持证为空，不符合
		if((null == owner || owner.length < 1) && (null != need && need.length > 0)){
			return false;
		}
		//持证要求为空和所持证不为空，符合
		if((null != owner && owner.length > 0) && (null == need || need.length < 1)){
			return true;
		}
		//遍历持证要求，查找是否有对应的持证
	    for(int i=0;i<need.length;i++){
	    	for(int j=0;j<owner.length;j++){
	    		if(ArrFileUtil.isEmpty(owner[j])){
	    			continue;
	    		}
	    		if(owner[j].equals(need[i])){
	                 return true;
	            }
	        }
	    }
	    return false;
	}
	/**
	 * 获取下个月的年月份
	 * @return eg:2014-02
	 */
	private String getNextMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//取当月-精确到月
		Date current = DateUtils.truncate((new Date()),Calendar.MONTH);
		//取次月-精确到月
		Date nextMonth = DateUtils.truncate(DateUtils.addMonths(current, 1),Calendar.MONTH);
		return sdf.format(nextMonth);
	}
	
	/**
	 * 获取指定月最大天数
	 * @param year
	 * @param month
	 * @return
	 */
	private int maxDate(int year,int month){
		Calendar a= Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month-1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 对象之间转换
	 * @param preSchedule
	 * @return
	 */
	private PreScheduleDraft copyBeanProperties(PreSchedule preSchedule){
		PreScheduleDraft psd = new PreScheduleDraft();
		psd.setId(preSchedule.getId());
		psd.setClassType(preSchedule.getClassType());
		psd.setDepartment(preSchedule.getDepartment());
		psd.setDraftFlag(preSchedule.getDraftFlag());
		psd.setDriver(preSchedule.getDriver());
		psd.setEight(preSchedule.getEight());
		psd.setEighteen(preSchedule.getEighteen());
		psd.setEleven(preSchedule.getEleven());
		psd.setFifteen(preSchedule.getFifteen());
		psd.setFive(preSchedule.getFive());
		psd.setFour(preSchedule.getFour());
		psd.setFourteen(preSchedule.getFourteen());
		psd.setCreatedEmpCode(preSchedule.getCreatedEmpCode());
		psd.setCreatedTm(preSchedule.getCreatedTm());
		psd.setModifiedEmpCode(preSchedule.getModifiedEmpCode());
		psd.setModifiedTm(preSchedule.getModifiedTm());
		psd.setNine(preSchedule.getNine());
		psd.setNineteen(preSchedule.getNineteen());
		psd.setOne(preSchedule.getOne());
		psd.setPlanDay(preSchedule.getPlanDay());
		psd.setRate(preSchedule.getRate());
		psd.setRealDay(preSchedule.getRealDay());
		psd.setSeven(preSchedule.getSeven());
		psd.setSeventeen(preSchedule.getSeventeen());
		psd.setSix(preSchedule.getSix());
		psd.setSixteen(preSchedule.getSixteen());
		psd.setTen(preSchedule.getTen());
		psd.setThirteen(preSchedule.getThirteen());
		psd.setThirty(preSchedule.getThirty());
		psd.setThirtyOne(preSchedule.getThirtyOne());
		psd.setThree(preSchedule.getThree());
		psd.setTwelve(preSchedule.getTwelve());
		psd.setTwenty(preSchedule.getTwenty());
		psd.setTwentyEight(preSchedule.getTwentyEight());
		psd.setTwentyFive(preSchedule.getTwentyFive());
		psd.setTwentyFour(preSchedule.getTwentyFour());
		psd.setTwentyNine(preSchedule.getTwentyNine());
		psd.setTwentyOne(preSchedule.getTwentyOne());
		psd.setTwentySeven(preSchedule.getTwentySeven());
		psd.setTwentySix(preSchedule.getTwentySix());
		psd.setTwentyThree(preSchedule.getTwentyThree());
		psd.setTwentyTwo(preSchedule.getTwentyTwo());
		psd.setTwo(preSchedule.getTwo());
		psd.setYearMonth(preSchedule.getYearMonth());
		return psd;
	}
	/**
	 * 对象之间转换
	 * @param preSchedule
	 * @return
	 */
	private PreSchedule draftToPre(PreScheduleDraft preSchedule){
		PreSchedule psd = new PreSchedule();
		psd.setId(preSchedule.getId());
		psd.setClassType(preSchedule.getClassType());
		psd.setDepartment(preSchedule.getDepartment());
		psd.setDraftFlag(preSchedule.getDraftFlag());
		psd.setDriver(preSchedule.getDriver());
		psd.setEight(preSchedule.getEight());
		psd.setEighteen(preSchedule.getEighteen());
		psd.setEleven(preSchedule.getEleven());
		psd.setFifteen(preSchedule.getFifteen());
		psd.setFive(preSchedule.getFive());
		psd.setFour(preSchedule.getFour());
		psd.setFourteen(preSchedule.getFourteen());
		psd.setCreatedEmpCode(preSchedule.getCreatedEmpCode());
		psd.setCreatedTm(preSchedule.getCreatedTm());
		psd.setModifiedEmpCode(preSchedule.getModifiedEmpCode());
		psd.setModifiedTm(preSchedule.getModifiedTm());
		psd.setNine(preSchedule.getNine());
		psd.setNineteen(preSchedule.getNineteen());
		psd.setOne(preSchedule.getOne());
		psd.setPlanDay(preSchedule.getPlanDay());
		psd.setRate(preSchedule.getRate());
		psd.setRealDay(preSchedule.getRealDay());
		psd.setSeven(preSchedule.getSeven());
		psd.setSeventeen(preSchedule.getSeventeen());
		psd.setSix(preSchedule.getSix());
		psd.setSixteen(preSchedule.getSixteen());
		psd.setTen(preSchedule.getTen());
		psd.setThirteen(preSchedule.getThirteen());
		psd.setThirty(preSchedule.getThirty());
		psd.setThirtyOne(preSchedule.getThirtyOne());
		psd.setThree(preSchedule.getThree());
		psd.setTwelve(preSchedule.getTwelve());
		psd.setTwenty(preSchedule.getTwenty());
		psd.setTwentyEight(preSchedule.getTwentyEight());
		psd.setTwentyFive(preSchedule.getTwentyFive());
		psd.setTwentyFour(preSchedule.getTwentyFour());
		psd.setTwentyNine(preSchedule.getTwentyNine());
		psd.setTwentyOne(preSchedule.getTwentyOne());
		psd.setTwentySeven(preSchedule.getTwentySeven());
		psd.setTwentySix(preSchedule.getTwentySix());
		psd.setTwentyThree(preSchedule.getTwentyThree());
		psd.setTwentyTwo(preSchedule.getTwentyTwo());
		psd.setTwo(preSchedule.getTwo());
		psd.setYearMonth(preSchedule.getYearMonth());
		return psd;
	}
	/**
	 * 校验时间是否有冲突
	 * @param arrangeNo
	 * @param day
	 * @param empCode
	 * @param dfs
	 * @param preSchedules
	 * @return
	 */
	private boolean checkTimeCon(ScheduleArrange first,ScheduleArrange second){
		if(null == first || null == first.getScheduleArrangeInfos() || first.getScheduleArrangeInfos().isEmpty()
				|| null == second || null == second.getScheduleArrangeInfos() || second.getScheduleArrangeInfos().isEmpty()){
			return false;
		}
		//1.取班次明细
		List<ScheduleInfo> firstInfos = new ArrayList<ScheduleInfo>();
		for(ScheduleInfoArrange info:first.getScheduleArrangeInfos()){
			if(null == info || null == info.getScheduleInfo()){
				continue;
			}
			firstInfos.add(info.getScheduleInfo());
		}
		if(null == firstInfos || firstInfos.isEmpty()){
			return false;
		}
		List<ScheduleInfo> secondInfos = new ArrayList<ScheduleInfo>();
		for(ScheduleInfoArrange info:second.getScheduleArrangeInfos()){
			if(null == info || null == info.getScheduleInfo()){
				continue;
			}
			secondInfos.add(info.getScheduleInfo());
		}
		if(null == secondInfos || secondInfos.isEmpty()){
			return false;
		}
		//2.班次明细排序取最早和最晚
		firstInfos = this.sortList(firstInfos);
		secondInfos = this.sortList(secondInfos);
		//3.取first最晚、取second最早
		ScheduleInfo lastInfo = firstInfos.get(firstInfos.size()-1);
		ScheduleInfo earlyInfo = secondInfos.get(0);
		//4.判断first是否跨天
		Integer firstStartPart1 = Integer.parseInt(lastInfo.getStartTm().split(":", -1)[0]);
		Integer firstStartPart2 = Integer.parseInt(lastInfo.getStartTm().split(":", -1)[1]);
		Integer firstEndPart1 = Integer.parseInt(lastInfo.getEndTm().split(":", -1)[0]);
		Integer firstEndPart2 = Integer.parseInt(lastInfo.getEndTm().split(":", -1)[1]);
		if (compareTm(firstStartPart1, firstStartPart2, firstEndPart1, firstEndPart2) > 0) {
			//5.first跨天，则比较是否与second交叉
			Integer secondStartPart1 = Integer.parseInt(earlyInfo.getStartTm().split(":", -1)[0]);
			Integer secondStartPart2 = Integer.parseInt(earlyInfo.getStartTm().split(":", -1)[1]);
			//6.first跨天收车时间比second最早出车时间大，则交叉
			if(compareTm(firstEndPart1, firstEndPart2, secondStartPart1, secondStartPart2) > 0){
				return true;
			}
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
	private List<ScheduleInfo> sortList(List<ScheduleInfo> infos){
		if(null == infos || infos.isEmpty()){
			return infos;
		}
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
	 * List排序
	 * @param infos
	 * @return
	 */
	private List<TransferClassesRL> sortClassList(List<TransferClassesRL> list){
		if(null == list || list.isEmpty()){
			return list;
		}
		Collections.sort(list, new Comparator<TransferClassesRL>(){
			public int compare(TransferClassesRL o1,TransferClassesRL o2) {
				if(null == o1 || null == o2 || o1.getDayDt() == null || o2.getDayDt() == null){
					return 0;
				}
				return o1.getDayDt().compareTo(o2.getDayDt());
			}
		});
		return list;
	}
}
