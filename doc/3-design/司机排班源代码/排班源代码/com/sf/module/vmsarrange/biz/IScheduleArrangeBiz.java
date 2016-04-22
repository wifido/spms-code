/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.vmsarrange.domain.ScheduleArrange;

/**
 *
 * 配班管理业务接口类
 *
 */
public interface IScheduleArrangeBiz extends IBiz {
	/**
	 * 查询分页数据
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @param deptId
	 * @param userId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<ScheduleArrange> listPage(Long deptId, String arrangeNo,
			Integer valid,Integer isUsed, int pageSize, int pageIndex);
	/**
	 * 新增配班数据
	 * @param entity
	 */
	public void saveEntity(ScheduleArrange entity);
	/**
	 * 修改配班数据
	 * @param entity
	 */
	public void updateEntity(ScheduleArrange entity)  throws Exception;
	/**
	 * 导入配班数据
	 * @param filePath
	 * @param fileName
	 * @param ignore
	 */
	public void saveFile(File uploadFile, String fileName);
	/**
	 * 导出报表
	 * @param deptId
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @return
	 */
	public String listReport(Long deptId, String arrangeNo,
			Integer valid,Integer isUsed);
	/**
	 * 生成班次代码
	 * @param deptCode
	 * @param arrangeType 1.机动班 2.普通班
	 * @return
	 */
	public String saveArrangeNo(String deptCode,int arrangeType) throws Exception;
	
	/**
	 * 根据班次代码，查找配班对象
	 * @param arrangeNo
	 * @return
	 */
	public ScheduleArrange findArrByArrangeNo(String arrangeNo);
	/**
	 * 置为无效
	 * @param recordId
	 * @throws Exception
	 */
	public void updateValid(Long recordId,Integer valid) throws Exception;
	/**
	 * 置为无效-清除排班
	 */
	public void updateInvalid(Long[] recordIds,Integer valid)  throws Exception;
	/**
	 * 校验是否超过16个小时
	 * @param entity
	 * @return
	 */
	public String listCheckEntity(ScheduleArrange entity);
}
