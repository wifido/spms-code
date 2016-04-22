/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-03     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.vmsarrange.domain.ArrDepartment;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.domain.ScheduleInfo;

/**
 *
 * 班次信息业务接口类
 *
 */
public interface IScheduleInfoBiz extends IBiz {
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
	public IPage<ScheduleInfo> listPage(Long deptId, Integer dataSource,
			String vehicleCode, Integer valid,Integer isUsed,int pageSize,int pageIndex);
	/**
	 * 为配班查找分页数据
	 * @param deptId
	 * @param pageSize
	 * @param pageIndex
	 * @param userId
	 * @return
	 */
	public IPage<ScheduleInfo> listPageForArrange(Long deptId,Long recordId, String startTm,
			int pageSize, int pageIndex);
	/**
	 * 新增班次信息
	 * @param entity
	 */
	public void saveEntity(ScheduleInfo entity);
	/**
	 * 修改班次信息
	 * @param entity
	 */
	public void updateEntity(ScheduleInfo entity) throws Exception;
	/**
	 * 修改是否有效
	 * @param recordId
	 * @param valid
	 */
	public void updateValid(Long recordId, Integer valid) throws Exception ;
	/**
	 * 查找车型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> listModelBase(String modelBase);
	/**
	 * 分页查找车辆
	 * @param vehicleCode
	 * @param brandBase
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public IPage<ArrVehicle> listVehiclePage(String vehicleCode,String modelBase,int pageSize,int pageIndex);
	/**
	 * 查找全网所有部门-无权限控制
	 * @return
	 */
	public IPage<ArrDepartment> listAllDepts(String deptCode,int pageSize,int pageIndex);
	/**
	 * 查找有权限的部门
	 * @return
	 */
	public IPage<ArrDepartment> listAllUserDepts(String deptCode,int pageSize,int pageIndex);
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteByIds(List<Long> ids) throws Exception ;
	/**
	 * 导入路径优化线路
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
	public String listReport(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed);
}
