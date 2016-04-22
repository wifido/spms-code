/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-08     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.dao.IMonthConfirmStatusDao;
import com.sf.module.operation.dao.IProcessConfirmStatusDao;
import com.sf.module.operation.dao.IProcessDetailDao;
import com.sf.module.operation.dao.IProcessMgtDao;
import com.sf.module.operation.dao.IProcessMgtJdbcDao;
import com.sf.module.operation.dao.ISchedulingDao;
import com.sf.module.operation.dao.ISchedulingJdbcDao;
import com.sf.module.operation.domain.MonthConfirmStatus;
import com.sf.module.operation.domain.ProcessConfirmStatus;
import com.sf.module.operation.domain.ProcessDetail;
import com.sf.module.operation.domain.ProcessMgt;
import com.sf.module.operation.domain.Scheduling;
import com.sf.module.operation.util.CommonUtil;
import com.sf.module.operation.dao.IProcessDao;
import com.sf.module.operation.domain.Process;

/**
 * 
 * 工序每日明细的业务实现类
 * 
 * @author houjingyu 2014-07-08
 * 
 */
public class ProcessDetailBiz extends BaseBiz implements IProcessDetailBiz {

	/**
	 * 工序每日明细的Dao接口
	 */
	private IProcessDetailDao processDetailDao;
	private IProcessDao processDao;
	private IProcessMgtDao processMgtDao;
	private IProcessMgtJdbcDao processMgtJdbcDao;
	private IMonthConfirmStatusDao monthConfirmStatusDao;
	private ISchedulingJdbcDao schedulingJdbcDao;
	private IProcessConfirmStatusDao processConfirmStatusDao;
	private ISchedulingDao  schedulingDao;
	/**
	 * 设置工序每日明细的Dao接口
	 */
	public void setProcessDetailDao(IProcessDetailDao processDetailDao) {
		this.processDetailDao = processDetailDao;
	}

	public void setProcessDao(IProcessDao processDao) {
		this.processDao = processDao;
	}

	public void setProcessMgtDao(IProcessMgtDao processMgtDao) {
		this.processMgtDao = processMgtDao;
	}
	
	public void setSchedulingDao(ISchedulingDao schedulingDao) {
		this.schedulingDao = schedulingDao;
	}

	public void setMonthConfirmStatusDao(
			IMonthConfirmStatusDao monthConfirmStatusDao) {
		this.monthConfirmStatusDao = monthConfirmStatusDao;
	}

	public void setSchedulingJdbcDao(ISchedulingJdbcDao schedulingJdbcDao) {
		this.schedulingJdbcDao = schedulingJdbcDao;
	}

	public void setProcessConfirmStatusDao(
			IProcessConfirmStatusDao processConfirmStatusDao) {
		this.processConfirmStatusDao = processConfirmStatusDao;
	}

	public List<Process> findByDeptId(Long deptid) {
		return processDao.findByDeptId(deptid);
	}
	
	public void setProcessMgtJdbcDao(IProcessMgtJdbcDao processMgtJdbcDao) {
		this.processMgtJdbcDao = processMgtJdbcDao;
	}

	private void checkCurrentDtValid(Long deptid, String ym, Date dt) {
		String currentYm = CommonUtil.getYm(new Date());
		Date dtBefore7Days = CommonUtil.getDayAfterNum(CommonUtil.currentDt(),
				-7);
		Date lastDate = CommonUtil.getLastDateOfMonth(new Date());

		if (currentYm.equals(ym)) {//如果是当月的，只能修改7天前到月底的
			if(CommonUtil.compareTowDate(dtBefore7Days, dt) > 0
					|| CommonUtil.compareTowDate(dt, lastDate) > 0){
				throw new BizException(String.format("只有%s至%s的数据才能新增和修改!",
						CommonUtil.getYmdStr(dtBefore7Days),
						CommonUtil.getYmdStr(lastDate)));
			}
		}else{
			// 检查该月排工序是否确认
			checkIsConfirm(deptid, ym);
			// 检查该月排班是否已确认
			getScheIsConfirm(deptid, ym);
		}
	}
	private void saveOrUpdateMgt(ProcessDto dto) {
		String[] dt = dto.getProcessDts().split(",");
		String[] emp = dto.getEmpCodes().split(",");
		Long deptid = dto.getDeptId();
		String processCode = dto.getProcessCode();
		HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		for (int i = 0; i < dt.length; i++) {
			Date d = CommonUtil.strToDate(dt[i]);
			String ym = CommonUtil.getYm(d);
//			checkCurrentDtValid(deptid,ym,d);
			if (map.containsKey(ym)) {
				map.get(ym).add(Integer.valueOf(CommonUtil.getDayNum(d)));
			} else {
				List<Integer> daynumList = new ArrayList<Integer>();
				map.put(ym, daynumList);
				map.get(ym).add(Integer.valueOf(CommonUtil.getDayNum(d)));
			}
		}
	/*	for (String key : map.keySet()) {
			// 检查该月排工序是否确认
			checkIsConfirm(deptid, key);
			// 检查该月排班是否已确认
			getScheIsConfirm(deptid, key);
		}*/
		for (int j = 0; j < emp.length; j++) {
			for (String key : map.keySet()) {
				// 获取有效的排班数据
				List<Scheduling> l = schedulingJdbcDao.getScheBy(deptid, key,emp[j]);
				if(l.isEmpty()){
					throw new BizException(String.format("工号%s未排班,不能进行排工序！", emp[j]));
				}
				ProcessMgt tmp = processMgtDao.findByCondition(deptid, key,
						emp[j]);
				List<ProcessDetail> pList = new ArrayList<ProcessDetail>();
				List<Integer> dlist = map.get(key);
				if (tmp == null) {
					ProcessMgt mgt = new ProcessMgt();
					ProcessDetail p = null;
					mgt.setDeptId(deptid);
					mgt.setYm(key);
					mgt.setEmpCode(emp[j]);
					for (Integer daynum : dlist) {
						// 检查当天排班的班别是否为休，为休则不处理
						Date d = CommonUtil.getYmd(key + '-'
								+ CommonUtil.addZero(daynum));
						Scheduling os = new Scheduling();
						os.setDeptId(deptid);
						os.setEmpCode(emp[j]);
						os.setSheduleDt(d);
						if (l != null && l.contains(os)) {
							Scheduling sc = this.schedulingDao
									.findByCondition(d, emp[j]);
							p = processMgtJdbcDao.findBy(deptid, d,
									emp[j]);
							if (sc != null) {
								if(p==null){
									p =  new ProcessDetail();
									p.setDeptId(deptid);
									p.setEmpCode(emp[j]);
								}
								try {
									if ("休".equals(sc.getScheduleCode()) || "OFF".equals(sc.getScheduleCode()) || "SW".equals(sc.getScheduleCode())) {
										PropertyUtils.setProperty(mgt, "day"
												+ daynum, "休");
										p.setProcessDt(d);
										p.setProcessCode("休");
										
									} else {
										PropertyUtils.setProperty(mgt, "day"
												+ daynum, processCode);
										p.setProcessDt(d);
										p.setProcessCode(processCode);
									}
								} catch (Exception e) {
									log.error("throw Exception:", e);
									throw new BizException("操作失败!");
								}
								pList.add(p);
							} else {
								throw new BizException(String.format(
										"工号%s第%d天未排班,不能进行排工序！", emp[j], daynum));
							}
						} 
						
						
					}
					mgt.setCreateEmpCode(this.getCurrentUser().getUsername());
					mgt.setCreateTm(new Date());
					mgt.setModifiedTm(new Date());
					mgt.setCommitStatus(0);
					Long mgtId = processMgtDao.save(mgt);
					saveOrUpdateDetail(pList,mgtId);
				} else {
					ProcessDetail p = null;
					tmp.setDeptId(deptid);
					tmp.setYm(key);
					tmp.setEmpCode(emp[j]);
					for (Integer daynum : dlist) {
						// 检查当天排班的班别是否为休，为休则不处理
						Date d = CommonUtil.getYmd(key + '-'
								+ CommonUtil.addZero(daynum));
						Scheduling os = new Scheduling();
						os.setDeptId(deptid);
						os.setEmpCode(emp[j]);
						os.setSheduleDt(d);
						if (l != null && l.contains(os)) {
							Scheduling sc = this.schedulingDao
									.findByCondition(d, emp[j]);
							p = this.processMgtJdbcDao.findBy(deptid, d,
									emp[j]);
							if (sc != null) {
								if(p==null){
									p =  new ProcessDetail();
									p.setDeptId(deptid);
									p.setEmpCode(emp[j]);
								}
								try {
									if ("休".equals(sc.getScheduleCode()) || "OFF".equals(sc.getScheduleCode()) || "SW".equals(sc.getScheduleCode())) {
										PropertyUtils.setProperty(tmp, "day"
												+ daynum, "休");
										p.setProcessDt(d);
										p.setProcessCode("休");
									} else {
										PropertyUtils.setProperty(tmp, "day"
												+ daynum, processCode);
										p.setProcessDt(d);
										p.setProcessCode(processCode);
									}
								} catch (Exception e) {
									log.error("throw Exception:", e);
									throw new BizException("操作失败!");
								}
								pList.add(p);
							} else {
								throw new BizException(String.format(
										"工号%s第%d天未排班,不能进行排工序！", emp[j], daynum));
							}
						}else{
							throw new BizException(String.format(
									"工号%s第%d天未排班,不能进行排工序！", emp[j], daynum));
						}
						
						
					}
					tmp.setCreateEmpCode(this.getCurrentUser().getUsername());
					tmp.setCreateTm(new Date());
					tmp.setModifiedTm(new Date());
					tmp.setCommitStatus(0);
					processMgtDao.update(tmp);
					saveOrUpdateDetail(pList,tmp.getId());
				}
			}
		}
	}
    private void saveOrUpdateDetail(List<ProcessDetail> pList,Long mgtId){
    	if(pList!=null&&pList.size()>0){
    		for (ProcessDetail p : pList) {
        		if(p!=null&&p.getId()!=null){
        			if(p.getProcessMonId()==null)
        			p.setProcessMonId(mgtId);
        			p.setModifiedEmpCode(this.getCurrentUser().getUsername());
        			p.setModifiedTm(new Date());
        			processDetailDao.update(p);
        		}else{
        			if(p.getProcessMonId()==null)
        			p.setProcessMonId(mgtId);
        			p.setCreateEmpCode(this.getCurrentUser().getUsername());
        			p.setCreateTm(new Date());
        			p.setModifiedTm(new Date());
        			processDetailDao.save(p);
        		}
    		}
    	}
    }
	public void saveMgtAndDetail(ProcessDto dto) {
		saveOrUpdateMgt(dto);
	}

	public void updateMgtAndDetail(ProcessDto dto) {
		saveOrUpdateMgt(dto);
	}


	// 校验排班是否确认
	private void getScheIsConfirm(Long deptid, String ym) {
		// 排班未确认则提示
		MonthConfirmStatus mcs = monthConfirmStatusDao.findBy(deptid, ym);
		if (mcs == null) {
			throw new BizException(ym + "月份排班未确认，不能进行排工序！");
		}
	}
	//校验工序是否确认
	private void checkIsConfirm(Long deptid, String ym) {
		//ProcessConfirmStatus mcs = processConfirmStatusDao.findBy(deptid, ym);
		//if (mcs != null) {
		//	throw new BizException(ym + "月份工序已确认，不能对该月份的数据进行新增或修改！");
		//}
	}
}