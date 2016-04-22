package com.sf.module.esbinterface.biz;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.esbinterface.domain.HrEmpInfo;

import java.util.List;

public interface IBigFileDataHandlerBiz extends IBiz {
    public void saveBatch(List<HrEmpInfo> entities);
}
