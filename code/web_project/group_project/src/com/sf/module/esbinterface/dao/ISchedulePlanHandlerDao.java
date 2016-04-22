package com.sf.module.esbinterface.dao;

import java.util.HashMap;
import java.util.List;

import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.esbinterface.biz.UploadStatus;
import com.sf.module.operation.domain.SchedulingBase;

public interface ISchedulePlanHandlerDao extends IEntityDao<SchedulingBase> {
    public int updateSchedulePlanToSuccess();

    public int lockSchedulePlan(UploadStatus uploadStatus);

    public int updateSchedulePlanToFailure(String errorInformation);

    public int resetSchedulePlanToNormal();

    List<HashMap<String,Object>> getSchedulePlansByStatus(UploadStatus uploading);
}
