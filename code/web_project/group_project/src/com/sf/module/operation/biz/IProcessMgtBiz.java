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

import java.io.File;
import java.util.HashMap;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.operation.action.dto.ProcessDto;
import com.sf.module.operation.domain.ProcessMgt;

/**
 *
 * 月度工序实体的业务接口
 * @author houjingyu  2014-07-08
 *
 */
public interface IProcessMgtBiz extends IBiz {
	public IPage<ProcessMgt> findPage(ProcessDto dto, int pageSize, int pageIndex);
	public String getExcel(ProcessDto dto);
	public HashMap<String, Object> saveImport(File uploadFile, ProcessDto schedulMgt);
	public boolean searchNoticesCount();
	public void saveConfirmStatus(Long deptid,String ym);
	public void loadSendProcessMail();
	public boolean getCanConfirm(Long deptid,String ym);
	public boolean processMgtIsConfirm(Long deptid, String ym);
	public void deleteProcessMgt(Long deptid, String ym);
	public String getConfirmExport();
}