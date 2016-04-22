/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.domain.Constants;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.dao.IMonthConfirmStatusDao;
import com.sf.module.operation.dao.ISchedulMgtDao;
import com.sf.module.operation.dao.ISchedulMgtJdbcDao;
import com.sf.module.operation.dao.ISchedulingDao;
import com.sf.module.operation.dao.ISchedulingJdbcDao;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;
import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.domain.SchedulingBase;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.report.biz.SchedulingModifyBiz;

/**
 * 
 * 排班的业务实现类
 * 
 * @author houjingyu 2014-06-17
 * 
 */
public class SchedulingBiz extends BaseBiz implements ISchedulingBiz {

	/**
	 * 排班的Dao接口
	 */
	private ISchedulingDao schedulingDao;
	private ISchedulingJdbcDao schedulingJdbcDao;
	private IMonthConfirmStatusDao monthConfirmStatusDao;
	private ISchedulMgtDao schedulMgtDao;
	private static final int commitStatus =0;
	private static final int synchroStatus =0;
	private ISchedulMgtJdbcDao schedulMgtJdbcDao;

	private SchedulingModifyBiz schedulingModifyBiz;

	public ISchedulMgtJdbcDao getSchedulMgtJdbcDao() {
		return schedulMgtJdbcDao;
	}

	public void setSchedulMgtJdbcDao(ISchedulMgtJdbcDao schedulMgtJdbcDao) {
		this.schedulMgtJdbcDao = schedulMgtJdbcDao;
	}

	public void setSchedulingModifyBiz(SchedulingModifyBiz schedulingModifyBiz) {
		this.schedulingModifyBiz = schedulingModifyBiz;
	}

	/**
	 * 设置排班的Dao接口
	 */
	public void setSchedulingDao(ISchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

	public void setSchedulingJdbcDao(ISchedulingJdbcDao schedulingJdbcDao) {
		this.schedulingJdbcDao = schedulingJdbcDao;
	}

	public void setMonthConfirmStatusDao(
			IMonthConfirmStatusDao monthConfirmStatusDao) {
		this.monthConfirmStatusDao = monthConfirmStatusDao;
	}

	public void setSchedulMgtDao(ISchedulMgtDao schedulMgtDao) {
		this.schedulMgtDao = schedulMgtDao;
	}
	
	private int replaceTheColonParseInt(String string) {
		return Integer.parseInt(string.replace(":", ""));
	}
	
	private List<ScheduledEntity> buildSaveObject(ScheduleDto dto) throws ParseException {
		List<ScheduledEntity> entities = new ArrayList<ScheduledEntity>();
		
		String[] employees = dto.getEmpCodes().split(",");
		Long departmentId= dto.getDeptId();
		String scheduleCode = dto.getSheduleCode();

		String[] dateTimes = dto.getSheduleDts().split(",");

		List<String> list = Arrays.asList(dateTimes);
		Collections.sort(list);
		
		String endTime = null;
		ScheduledEntity entity = null;
		for (int i = 0; i < employees.length; i++) {
			for (int j = 0; j < list.size(); j++) {
				if (Arrays.asList(Constants.DEFAULT_CLASS).contains(scheduleCode)) {
					continue;
				}
				
				Calendar currentCalendar = Calendar.getInstance();
				currentCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(j)));
				
				String currentYearMonthDay = new SimpleDateFormat("yyyy-MM-dd").format(currentCalendar.getTime());
				String currentYearMonth = new SimpleDateFormat("yyyy-MM").format(currentCalendar.getTime());
				
				Calendar preCalendar = Calendar.getInstance();
				preCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(j)));
				preCalendar.add(Calendar.DATE, -1);
				
				String preYearMonthDay = new SimpleDateFormat("yyyy-MM-dd").format(preCalendar.getTime());
				
				if(!dto.getSheduleDts().contains(preYearMonthDay)) {
					 List<SchedulingBase> schedulingBases = schedulMgtJdbcDao.getLastMonthLastDayScheInfo(departmentId, employees[i], preYearMonthDay);
					 
					 if(schedulingBases.size() > 0) {
						 SchedulingBase schedulingBase = schedulingBases.get(0);
						 
						 endTime = getMaxEndTime(endTime, schedulingBase);
						 
						 entity = new ScheduledEntity(endTime != "", preYearMonthDay, schedulingBase.getStart1Time(), endTime);
						 entities.add(entity);
					 }
				}
				
				List<SchedulingBase> schedulingBases = schedulMgtJdbcDao.getScheInfo(departmentId, scheduleCode, currentYearMonth);
				
				SchedulingBase schedulingBase = schedulingBases.get(0);
				
				endTime = getMaxEndTime(endTime, schedulingBase);
				entity = new ScheduledEntity(endTime != "", currentYearMonthDay, schedulingBase.getStart1Time(), endTime);
				entities.add(entity);
				
				Calendar nextCalendar = Calendar.getInstance();
				nextCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(j)));
				int day = nextCalendar.get(Calendar.DATE);
				nextCalendar.set(Calendar.DATE, day+1);
				String nextYearMonthDay = new SimpleDateFormat("yyyy-MM-dd").format(nextCalendar.getTime());
				
				if (dto.getSheduleDts().contains(nextYearMonthDay)) {
					continue;
				} else {
					List<SchedulingBase> nextSchedulingBases = schedulMgtJdbcDao.getLastMonthLastDayScheInfo(departmentId, employees[i], nextYearMonthDay);
					
					if(nextSchedulingBases.size() > 0) {
						 SchedulingBase nextSchedulingBase = nextSchedulingBases.get(0);
						 
						 endTime = getMaxEndTime(endTime, nextSchedulingBase);
						 
						 entity = new ScheduledEntity(endTime != "", nextYearMonthDay, nextSchedulingBase.getStart1Time(), endTime);
						 entities.add(entity);
					 }
				}
			}
		}
		return entities;
	}
	
	private String getMaxEndTime(String endTime, SchedulingBase schedulingBase) {
		endTime = "";
		if(StringUtil.isNotBlank(schedulingBase.getStart3Time()) && 
				( replaceTheColonParseInt(schedulingBase.getStart3Time()) > replaceTheColonParseInt(schedulingBase.getEnd3Time())
				|| replaceTheColonParseInt(schedulingBase.getStart2Time()) > replaceTheColonParseInt(schedulingBase.getEnd2Time())
				|| replaceTheColonParseInt(schedulingBase.getStart1Time()) > replaceTheColonParseInt(schedulingBase.getEnd1Time()))) {
			 endTime = schedulingBase.getEnd3Time();
		 } else if(StringUtil.isNotBlank(schedulingBase.getStart2Time()) && 
				( replaceTheColonParseInt(schedulingBase.getStart2Time()) > replaceTheColonParseInt(schedulingBase.getEnd2Time())
				|| replaceTheColonParseInt(schedulingBase.getStart1Time()) > replaceTheColonParseInt(schedulingBase.getEnd1Time()))) {
			 endTime = schedulingBase.getEnd2Time();
		 } else if( replaceTheColonParseInt(schedulingBase.getStart1Time()) > replaceTheColonParseInt(schedulingBase.getEnd1Time())){
			 endTime = schedulingBase.getEnd1Time();
		 }
		return endTime;
	}
	
	private void saveOrUpdateMgt(ScheduleDto dto) {
		String[] dt = dto.getSheduleDts().split(",");
		String[] emp = dto.getEmpCodes().split(",");
		Long deptid = dto.getDeptId();
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		String schecode = dto.getSheduleCode();
		String errorMsg = "";
		
		for (int i = 0; i < dt.length; i++) {
			Date d = CommonUtil.strToDate(dt[i]);
			String ym = CommonUtil.getYm(d);
			
			if (DateUtil.validConfirmDate(ym)) {
				if (DateUtil.isBeOverdue(ym))
					throw new BizException("已逾期，不能导入该月排班！");
			} else {
				throw new BizException("最多新增上个月、当前月以及下个月的排班！");
			}
			if (map.containsKey(ym)) {
				map.get(ym).add(Integer.valueOf(CommonUtil.getDayNum(d)));
			} else {
				List<Integer> daynumList = new ArrayList<Integer>();
				map.put(ym, daynumList);
				map.get(ym).add(Integer.valueOf(CommonUtil.getDayNum(d)));
			}
		}
		
		try {
			List<ScheduledEntity> entities = buildSaveObject(dto);

			for (int i = 0; i < entities.size(); i++) {
				ScheduledEntity entity = entities.get(i);
				
				if (i + 1 >= entities.size()) {
					continue;
				}
				ScheduledEntity nextEntity = entities.get(i + 1);

				if (entity.isNextContrast()) {
					if (replaceTheColonParseInt(entity.getEndTime()) >= replaceTheColonParseInt(nextEntity.getStartTime())) {
						errorMsg = String.format("%s与%s的班别时间存在冲突!", entity.getDayOfMonth(), nextEntity.dayOfMonth);
						throw new BizException(errorMsg);
					}
				}
			}
		} catch (Exception e) {
			throw new BizException(errorMsg);
		}
		
		
		try {
			for (int j = 0; j < emp.length; j++) {
				for (String key : map.keySet()) {
					SchedulMgt tmp = schedulMgtDao.findByCondition(deptid, key,
							emp[j]);
					List<Integer> dlist = map.get(key);
					OutEmployee employee = schedulMgtJdbcDao.getEmpByCode(deptid, emp[j]);
					int invalidScheduleNum = 0;
					if (tmp == null) {
						SchedulMgt mgt = new SchedulMgt();
						for (Integer daynum : dlist) {							
							Date d = CommonUtil.getYmd(key + '-' + CommonUtil.addZero(daynum));
							if ((null != employee.getDimissionDt() && d.after(employee.getDimissionDt()))
									|| d.before(employee.getSfDate())) {
								invalidScheduleNum++;
								continue;
							}
							PropertyUtils.setProperty(mgt, "day" + daynum,
									schecode);
						}
						if(invalidScheduleNum == dlist.size())
							continue;
						mgt.setDeptId(deptid);
						mgt.setYm(key);
						mgt.setEmpCode(employee.getEmpCode());
						mgt.setEmpName(employee.getEmpName());
						mgt.setWorkType(employee.getWorkType());
						mgt.setCreateEmpCode(this.getCurrentUser()
								.getUsername());
						mgt.setCreateTm(new Date());
						mgt.setModifiedTm(new Date());
						mgt.setCommitStatus(commitStatus);
						mgt.setSynchroStatus(synchroStatus);
						Long mgtId = schedulMgtDao.save(mgt);
						saveOrUpdateDetail(deptid, employee,
								dto.getSheduleCode(), mgtId, key, dlist);
					} else {
						for (Integer daynum : dlist) {
							Date d = CommonUtil.getYmd(key + '-' + CommonUtil.addZero(daynum));
							if ((null != employee.getDimissionDt() && d.after(employee.getDimissionDt()))
									|| d.before(employee.getSfDate())) {
								invalidScheduleNum++;
								continue;
							}
							PropertyUtils.setProperty(tmp, "day" + daynum,
									schecode);
						}
						if(invalidScheduleNum == dlist.size())
							continue;
						Long mgtId = tmp.getId();
						tmp.setModifiedEmpCode(this.getCurrentUser()
								.getUsername());
						tmp.setModifiedTm(new Date());
						tmp.setCommitStatus(commitStatus);
						tmp.setSynchroStatus(synchroStatus);
						tmp.setEmpCode(employee.getEmpCode());
						tmp.setEmpName(employee.getEmpName());
						tmp.setWorkType(employee.getWorkType());
						schedulMgtDao.update(tmp);
						saveOrUpdateDetail(deptid, employee,
								dto.getSheduleCode(), mgtId, key, dlist);

					}
				}
			}
		} catch (Exception e) {
			log.error("error:", e);
			throw new BizException("保存失败！");
		}

	}
	
	
	public static class ScheduledEntity{
		private boolean nextContrast;
		private String dayOfMonth;
		private String startTime;
		private String endTime;
		
		public ScheduledEntity(boolean nextContrast, String dayOfMonth, String startTime, String endTime) {
			this.nextContrast = nextContrast;
			this.dayOfMonth = dayOfMonth;
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public ScheduledEntity(){
			
		}
		
		public boolean isNextContrast() {
			return nextContrast;
		}

		public void setNextContrast(boolean nextContrast) {
			this.nextContrast = nextContrast;
		}

		public String getDayOfMonth() {
			return dayOfMonth;
		}
		public void setDayOfMonth(String dayOfMonth) {
			this.dayOfMonth = dayOfMonth;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
	}
	
	// 保存
	public void saveMgtAndDetail(ScheduleDto dto) {
		saveOrUpdateMgt(dto);
	}

	// 修改
	public void updateMgtAndDetail(ScheduleDto dto) {
		saveOrUpdateMgt(dto);
	}

	// 修改或保存明细排班
	private void saveOrUpdateDetail(Long deptid, OutEmployee outEmployee,
			String scheCode, Long mgtid, String key, List<Integer> list) {
		for (Integer it : list) {
			Date d = CommonUtil.getYmd(key + '-' + CommonUtil.addZero(it));
			
			if ((null != outEmployee.getDimissionDt() && d.after(outEmployee.getDimissionDt()))
					|| d.before(outEmployee.getSfDate())) {
				continue;
			}
			Scheduling s = schedulingDao.findByCondition(d, outEmployee.getEmpCode());
			if (s == null) {
				Scheduling obj = new Scheduling();
				obj.setDeptId(deptid);
				obj.setEmpCode(outEmployee.getEmpCode());
				obj.setSheduleDt(d);
				obj.setScheduleCode(scheCode);
				obj.setSheduleMonId(mgtid);
				obj.setCreateEmpCode(this.getCurrentUser().getUsername());
				obj.setCreateTm(new Date());
				obj.setModifiedTm(new Date());
				obj.setSynchroStatus(synchroStatus);
				obj.setCommitStatus(0);
				schedulingDao.save(obj);
			} else {
				String oldScheduleCode=s.getScheduleCode();
				s.setDeptId(deptid);
				s.setScheduleCode(scheCode);
				s.setModifiedEmpCode(this.getCurrentUser().getUsername());
				s.setModifiedTm(new Date());
				s.setSynchroStatus(synchroStatus);
				s.setCommitStatus(0);
				schedulingDao.update(s);
				
				// 运作排班修改记录，当修改排班时，插入修改记录
				if(!oldScheduleCode.equals(s.getScheduleCode())&&!s.getSheduleDt().after(new Date())){
					this.schedulingModifyBiz.addFormScheduling(s);
				}
			}
		}
	}

	// 校验记录的有效性
	public Map<String, Object> getSaveValid(ScheduleDto dto) {
		Map<String, Object> mp = new HashMap<String, Object>();
		String[] dt = dto.getSheduleDts().split(",");
		String[] emp = dto.getEmpCodes().split(",");
		Long deptId = dto.getDeptId();
		// Long scheId = dto.getSheduleId();
		String schecode = dto.getSheduleCode();
		HashMap<String, List<Date>> ymDtMap = getYmDtMap(deptId,dt);
		for (int j = 0; j < emp.length; j++) {
			for (String key : ymDtMap.keySet()) {
				String ym = key;
				// 先根据离职日期和转网点的日期来确定哪些选择的日期为有效的
				Date[] d = schedulingJdbcDao.getValidDateRange(deptId, emp[j],
						ym);
				List<Date> dtlist = checkValidDateRange(emp[j],
						ymDtMap.get(key), d);
				// 合并数据
				List<Scheduling> lst = mergeData(deptId, ym, emp[j], dtlist,
						schecode);
				//
				Collections.sort(lst, new Comparator<Scheduling>() {
					public int compare(Scheduling arg0, Scheduling arg1) {
						return arg0.getSheduleDt().compareTo(
								arg1.getSheduleDt());
					}
				});
				// 员工少于4天休
				String s = checkRestCountOfMonthLess4(emp[j], ym, lst);
				if (StringUtils.isNotBlank(s)) {
					mp.put("flag2", true);
					mp.put("msg2", s);
				} else {
					mp.put("flag2", false);
				}

			}
		}

		return mp;
	}

	// 对某一员工某月份所有天数的排班数据和准备添加或更新的数据进行合并,将最新的数据覆盖旧数据
	private List<Scheduling> mergeData(Long deptId, String ym, String empCode,
			List<Date> dtlist, String scheCode) {
		List<Scheduling> dbList = schedulingJdbcDao.getScheBy(deptId, ym,
				empCode);
		for (Date dt : dtlist) {
			if (ym.equals(CommonUtil.getYm(dt))) {
				Scheduling ss = new Scheduling();
				ss.setDeptId(deptId);
				ss.setEmpCode(empCode);
				ss.setSheduleDt(dt);
				// ss.setSheduleId(scheId);
				ss.setScheduleCode(scheCode);
				if (dbList.contains(ss)) {
					Scheduling old = dbList.get(dbList.indexOf(ss));
					ss.setId(old.getId());
					ss.setSheduleMonId(old.getSheduleMonId());
					dbList.remove(ss);
					dbList.add(ss);
				} else {
					dbList.add(ss);
				}
			}
		}
		return dbList;
	}

	// 检查连续3天排班有效性
	private String checkThreeDayScheValid(String empcode, String ym,
			List<Scheduling> lst) {
		if (lst != null && lst.size() > 0) {
			for (int k = 2, j = 1, i = 0; k < lst.size(); k++, j++, i++) {
				if (eqScheduleCode(lst.get(i).getScheduleCode(), lst.get(k)
						.getScheduleCode())
						&& eqScheduleCode(lst.get(j).getScheduleCode(), lst
								.get(k).getScheduleCode())
						&& eqScheduleCode(lst.get(j).getScheduleCode(), lst
								.get(i).getScheduleCode())) {
					return String.format("[%s,%s,%s]连续三天存在不同班次",
							CommonUtil.getYmdStr(lst.get(i).getSheduleDt()),
							CommonUtil.getYmdStr(lst.get(j).getSheduleDt()),
							CommonUtil.getYmdStr(lst.get(k).getSheduleDt()));
				}
			}

		}

		return "";
	}

	/**
	 * !(1 | 1 == 0)
	 * 
	 * @return
	 */
	public boolean eqScheduleCode(String srcCode, String destCode) {
		if ("休".equals(srcCode)) {
			srcCode = destCode;
		}
		if ("休".equals(destCode)) {
			destCode = srcCode;
		}

		return !destCode.equals(srcCode);
	}

	// 获取所选的日期的月份
	private HashMap<String, List<Date>> getYmDtMap(Long deptid, String[] dt) {
		HashMap<String, List<Date>> monthDtMap = new HashMap<String, List<Date>>();
		// 将该员工所选排班日期的月份放进set
		for (int i = 0; i < dt.length; i++) {
			Date d = CommonUtil.getYmd(dt[i]);
			String ym = CommonUtil.getYm(d);
//			checkCurrentDtValid(deptid, ym, d);
			if (monthDtMap.containsKey(ym)) {
				monthDtMap.get(ym).add(d);
			} else {
				List<Date> daynumList = new ArrayList<Date>();
				monthDtMap.put(ym, daynumList);
				monthDtMap.get(ym).add(d);
			}
		}
		return monthDtMap;
	}

	// 员工当月除休外排超过3个班别
	private void checkIsBeyond3(String empCode, String ym, List<Scheduling> lst) {
		HashSet<String> set = new HashSet<String>();
		if (lst != null && lst.size() > 0) {
			for (int i = 0; i < lst.size(); i++) {
				Scheduling o = lst.get(i);
				if (!"休".equals(o.getScheduleCode()))
					set.add(o.getScheduleCode());
			}
			if (set != null && set.size() > 3) {
				String s = set.toString();
				throw new BizException(String.format("工号%s %s月的班别%s在一个月超过3个!",
						empCode, ym, s));
			}
		}
	}

	// 员工少于4天休校验
	private String checkRestCountOfMonthLess4(String empCode, String ym,
			List<Scheduling> lst) {
		if (CommonUtil.getLastDayOfMonth(CommonUtil.getYmd(ym + "-01")) >= 30) {
			int count = 0;
			for (Scheduling s : lst) {
				if ("休".equals(s.getScheduleCode()) || "SW".equals(s.getScheduleCode()) || "OFF".equals(s.getScheduleCode())) {
					count++;
				}
			}
			if (count < 4) {
				return String.format("工号%s %s月份排休少于4天", empCode, ym);
			}
		}
		return "";
	}

	//

	// 检查排班日期是否在班别的生效失效范围内
	private void checkScheDtInDateRange(Long deptId, String scheCode, Date dt) {
		boolean flag = schedulingJdbcDao.checkScheDtInDateRange(deptId,
				scheCode, dt);
		if (!flag)
			throw new BizException(String.format("日期%s不在班别生效失效范围内",
					CommonUtil.getYmdStr(dt)));
	}

	// 检查小组失效日期对排班日期的影响
	// 检查员工离职日期对排班日期的影响
	public void checkGroupDtEmpDt(Long deptId, String empCode, Date dt) {

		int i = schedulingJdbcDao.getGroupDtEmpDtValid(deptId, empCode, dt);
		if (i == 0) {
			throw new BizException(String.format("工号%s员工所在的小组在%s或之前已失效",
					empCode, CommonUtil.getYmdStr(dt)));
		}
	}

	private List<Date> checkValidDateRange(String empCode, List<Date> list,
			Date[] dts) {
		List<Date> dtList = new ArrayList<Date>();
		for (Date dt : list) {
			// 如果为空，日期不在范围内
			if (null != dts[0] && null != dts[1]
					&& CommonUtil.compareTowDate(dts[0], dt) <= 0
					&& CommonUtil.compareTowDate(dt, dts[1]) <= 0) {
				dtList.add(dt);
			} else {
				throw new BizException(String.format("工号%s员工%s不在有效日期范围内,请检查该员工的离职日期或转网点生效日期!", empCode,
						CommonUtil.getYmdStr(dt)));
			}
		}
		return dtList;
	}
}