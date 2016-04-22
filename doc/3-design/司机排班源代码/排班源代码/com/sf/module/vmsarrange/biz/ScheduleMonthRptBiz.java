/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-26     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.vmsarrange.dao.IScheduleArrangeDao;
import com.sf.module.vmsarrange.dao.IScheduleMonthRptDao;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.domain.ScheduleInfo;
import com.sf.module.vmsarrange.domain.ScheduleInfoArrange;
import com.sf.module.vmsarrange.domain.ScheduleMonthRpt;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 *
 * ScheduleMonthRptBiz处理类
 *
 */
public class ScheduleMonthRptBiz extends BaseBiz implements
		IScheduleMonthRptBiz {
	private IScheduleMonthRptDao scheduleMonthRptDao;
	private IScheduleArrangeDao  scheduleArrangeDao;

	public void setScheduleArrangeDao(IScheduleArrangeDao scheduleArrangeDao) {
		this.scheduleArrangeDao = scheduleArrangeDao;
	}

	public void setScheduleMonthRptDao(IScheduleMonthRptDao scheduleMonthRptDao) {
		this.scheduleMonthRptDao = scheduleMonthRptDao;
	}
	//获取指定记录，指定日期排班明细数据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findByCondition(Long id, String yearMonth, Integer dayNum) {
		if(null == id || ArrFileUtil.isEmpty(yearMonth) || null == dayNum){
			throw new BizException("获取明细失败，提交的参数为空，请重试");
		}
		ScheduleMonthRpt sm = this.scheduleMonthRptDao.load(id);
		if(null == sm){
			throw new BizException("获取明细失败，记录不存在，请刷新页面");
		}
		int day = dayNum.intValue();
		String arrangeNo = "";
		switch(day){
			case 1:arrangeNo = (ArrFileUtil.isEmpty(sm.getOne()))?"":sm.getOne();break;
			case 2:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwo()))?"":sm.getTwo();break;
			case 3:arrangeNo = (ArrFileUtil.isEmpty(sm.getThree()))?"":sm.getThree();break;
			case 4:arrangeNo = (ArrFileUtil.isEmpty(sm.getFour()))?"":sm.getFour();break;
			case 5:arrangeNo = (ArrFileUtil.isEmpty(sm.getFive()))?"":sm.getFive();break;
			case 6:arrangeNo = (ArrFileUtil.isEmpty(sm.getSix()))?"":sm.getSix();break;
			case 7:arrangeNo = (ArrFileUtil.isEmpty(sm.getSeven()))?"":sm.getSeven();break;
			case 8:arrangeNo = (ArrFileUtil.isEmpty(sm.getEight()))?"":sm.getEight();break;
			case 9:arrangeNo = (ArrFileUtil.isEmpty(sm.getNine()))?"":sm.getNine();break;
			case 10:arrangeNo = (ArrFileUtil.isEmpty(sm.getTen()))?"":sm.getTen();break;
			case 11:arrangeNo = (ArrFileUtil.isEmpty(sm.getEleven()))?"":sm.getEleven();break;
			case 12:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwelve()))?"":sm.getTwelve();break;
			case 13:arrangeNo = (ArrFileUtil.isEmpty(sm.getThirteen()))?"":sm.getThirteen();break;
			case 14:arrangeNo = (ArrFileUtil.isEmpty(sm.getFourteen()))?"":sm.getFourteen();break;
			case 15:arrangeNo = (ArrFileUtil.isEmpty(sm.getFifteen()))?"":sm.getFifteen();break;
			case 16:arrangeNo = (ArrFileUtil.isEmpty(sm.getSixteen()))?"":sm.getSixteen();break;
			case 17:arrangeNo = (ArrFileUtil.isEmpty(sm.getSeventeen()))?"":sm.getSeventeen();break;
			case 18:arrangeNo = (ArrFileUtil.isEmpty(sm.getEighteen()))?"":sm.getEighteen();break;
			case 19:arrangeNo = (ArrFileUtil.isEmpty(sm.getNineteen()))?"":sm.getNineteen();break;
			case 20:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwenty()))?"":sm.getTwenty();break;
			case 21:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyOne()))?"":sm.getTwentyOne();break;
			case 22:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyTwo()))?"":sm.getTwentyTwo();break;
			case 23:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyThree()))?"":sm.getTwentyThree();break;
			case 24:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyFour()))?"":sm.getTwentyFour();break;
			case 25:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyFive()))?"":sm.getTwentyFive();break;
			case 26:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentySix()))?"":sm.getTwentySix();break;
			case 27:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentySeven()))?"":sm.getTwentySeven();break;
			case 28:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyEight()))?"":sm.getTwentyEight();break;
			case 29:arrangeNo = (ArrFileUtil.isEmpty(sm.getTwentyNine()))?"":sm.getTwentyNine();break;
			case 30:arrangeNo = (ArrFileUtil.isEmpty(sm.getThirty()))?"":sm.getThirty();break;
			case 31:arrangeNo = (ArrFileUtil.isEmpty(sm.getThirtyOne()))?"":sm.getThirtyOne();break;
		}
		//根据班次代码获取详细的班次信息
		ScheduleArrange sa = null;
		if(!ArrFileUtil.isEmpty(arrangeNo) && arrangeNo.indexOf("-")!=-1){
			String[] arrs = arrangeNo.split("\\[",-1);
			for(int i=0;i<arrs.length;i++){
				if(null == arrs[i]){
					continue;
				}
				if(arrs[i].indexOf("-")!=-1){
					arrangeNo = arrs[i];
					break;
				}
			}
			if(!ArrFileUtil.isEmpty(arrangeNo)){
				sa = scheduleArrangeDao.findArrByArr(arrangeNo);
			}
		}
		//拼接年月日
		String dateStr = yearMonth;
		if(day < 10){
			dateStr += "-"+"0"+day;
		}else{
			dateStr += "-"+day;
		}
		//整合数据返回到ext
		Map map = new HashMap();
		map.put("areaDeptName", sm.getAreaDeptName());
		map.put("deptCode", sm.getDeptCode());
		map.put("driverName", (null == sm.getDriver()?"":sm.getDriver().getDriverName()));
		map.put("driverCode", (null == sm.getDriver()?"":sm.getDriver().getEmpCode()));
		map.put("classType", sm.getClassType());
		map.put("arrangeNo", arrangeNo);
		map.put("isValid", (null == sa?"":sa.getValid()));
		map.put("dayDt", dateStr);
		List listSi = new ArrayList();
		Map temp;
		if(null != sa && null != sa.getScheduleArrangeInfos() && !sa.getScheduleArrangeInfos().isEmpty()){
			for(ScheduleInfoArrange sia :sa.getScheduleArrangeInfos()){
				if(null == sia || null == sia.getScheduleInfo()){
					continue;
				}
				ScheduleInfo si = sia.getScheduleInfo();
				temp = new HashMap();
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
		if(null != sa && sa.getArrangeType().compareTo(1)==0){
			temp = new HashMap();
			temp.put("startTm", sa.getStartTm());
			temp.put("endTm", sa.getEndTm());
			temp.put("valid", sa.getValid());
			listSi.add(temp);
		}
		map.put("scheduleInfos", listSi);
		return map;
	}
}
