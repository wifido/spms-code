package com.sf.module.esbinterface.biz;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.esbinterface.dao.IHrsEmpInitDao;
import com.sf.module.esbinterface.domain.HrEmpInfo;

import java.util.List;

public class HrsEmpInitDataHandlerBiz extends BaseBiz implements IBigFileDataHandlerBiz {
    private IHrsEmpInitDao empInitDao;

    public void setEmpInitDao(IHrsEmpInitDao empInitDao) {
        this.empInitDao = empInitDao;
    }

    public void saveBatch(List<HrEmpInfo> entities) {
        empInitDao.saveBatch(entities);
    }
}
