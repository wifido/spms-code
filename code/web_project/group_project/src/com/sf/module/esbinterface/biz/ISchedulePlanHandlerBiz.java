package com.sf.module.esbinterface.biz;

import com.sf.module.esbinterface.fileutil.ScheduleNode;

import java.util.List;

public interface ISchedulePlanHandlerBiz {
    public List<ScheduleNode> getNeedUploadSchedules();

    public List<ScheduleNode> getNeedResendSchedules();

    public int updateScheduleStatusToSuccess();

    int updateScheduleStatusToFailure(String errorInformation);
}
