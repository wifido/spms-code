/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import java.io.File;
import java.util.HashMap;
import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.operation.action.dto.ScheduleDto;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.domain.SchedulMgt;

/**
 * 
 * 排班管理的业务接口
 * 
 * @author houjingyu 2014-06-20
 * 
 */
public interface ISchedulMgtBiz extends IBiz {

	public IPage<SchedulMgt> findPage(ScheduleDto dto, int pageSize, int pageIndex);

	public String getExcel(ScheduleDto dto);

	public String getConfirmExport();

	public HashMap<String, Object> saveImport(File uploadFile, ScheduleDto schedulMgt);

	public boolean searchNoticesCount();

	public void saveImportConfirmData(String dataStr);

	public void loadSendScheMail();

	public boolean getCanConfirm(Long deptid, String ym);

	public IPage<OutEmployee> findEmpPage(ScheduleDto dto, int pageSize, int pageIndex);

	public void insertConfirmLog(Long deptId, String ym);

	public void delete(ScheduleDto dto, String schedulingIds);

	public boolean getToConfirmThe(Long deptid, String schedulingIds);

	public String exportNotScheduling(Long deptid, String ym);

	public void commitConfirmScheduling(Long deptid, String ym) throws Exception;

}