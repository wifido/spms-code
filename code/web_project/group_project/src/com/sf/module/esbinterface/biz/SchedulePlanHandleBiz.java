package com.sf.module.esbinterface.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.esbinterface.dao.ISchedulePlanHandlerDao;
import com.sf.module.esbinterface.fileutil.ScheduleNode;

public class SchedulePlanHandleBiz extends BaseBiz implements ISchedulePlanHandlerBiz {
    private ISchedulePlanHandlerDao schedulePlanHandlerDao;

    public void setSchedulePlanHandlerDao(ISchedulePlanHandlerDao schedulePlanHandlerDao) {
        this.schedulePlanHandlerDao = schedulePlanHandlerDao;
    }

    @Override
    public List<ScheduleNode> getNeedUploadSchedules() {
        int count = schedulePlanHandlerDao.lockSchedulePlan(UploadStatus.NOUPLOAD);

        if (count > 0) {
        	ScheduleNode scheduleNode = null;
        	List<ScheduleNode> scheduleNodeList = new ArrayList<ScheduleNode>();
        	List<HashMap<String,Object>> schedulePlabList = schedulePlanHandlerDao.getSchedulePlansByStatus(UploadStatus.UPLOADING);
        	for (HashMap<String,Object> map : schedulePlabList) {
        		scheduleNode = new ScheduleNode(Long.parseLong(map.get("ID").toString()),
        				map.get("EMP_CODE").toString(),
        				map.get("BEGIN_DATE").toString(),
        				map.get("END_DATE").toString(),
        				map.get("BEGIN_TM") != null ? map.get("BEGIN_TM").toString() : "",
        				map.get("END_TM") != null ? map.get("END_TM").toString() : "",
        				map.get("TMR_DAY_FLAG") != null ? 	map.get("TMR_DAY_FLAG").toString() : "",
        				map.get("OFF_DUTY_FLAG") != null ? map.get("OFF_DUTY_FLAG").toString() : "",
        				map.get("CLASS_SYSTEM")!= null ? map.get("CLASS_SYSTEM").toString() : "");
        		scheduleNodeList.add(scheduleNode);
        	}
            return scheduleNodeList;
        }
        return null;
    }

    @Override
    public List<ScheduleNode> getNeedResendSchedules() {
        int count = schedulePlanHandlerDao.lockSchedulePlan(UploadStatus.FAILED);

        if (count > 0) {
        	ScheduleNode scheduleNode = null;
        	List<ScheduleNode> scheduleNodeList = new ArrayList<ScheduleNode>();
        	List<HashMap<String,Object>>  schedulePlabList = schedulePlanHandlerDao.getSchedulePlansByStatus(UploadStatus.UPLOADING);
        	for (HashMap<String,Object> map : schedulePlabList) {
        		scheduleNode = new ScheduleNode(Long.parseLong(map.get("ID").toString()),
        				map.get("EMP_CODE").toString(),
        				map.get("BEGIN_DATE").toString(),
        				map.get("END_DATE").toString(),
        				map.get("BEGIN_TM") != null ? map.get("BEGIN_TM").toString() : "",
        				map.get("END_TM") != null ? map.get("END_TM").toString() : "",
        				map.get("TMR_DAY_FLAG") != null ? 	map.get("TMR_DAY_FLAG").toString() : "",
        				map.get("OFF_DUTY_FLAG") != null ? map.get("OFF_DUTY_FLAG").toString() : "",
        				map.get("CLASS_SYSTEM")!= null ? map.get("CLASS_SYSTEM").toString() : "");
        		scheduleNodeList.add(scheduleNode);
        	}
            return scheduleNodeList;
        }
        return null;
    }

    @Override
    public int updateScheduleStatusToSuccess() {
        return schedulePlanHandlerDao.updateSchedulePlanToSuccess();
    }

    @Override
    public int updateScheduleStatusToFailure(String errorInformation) {
        return schedulePlanHandlerDao.updateSchedulePlanToFailure(errorInformation);
    }
}
