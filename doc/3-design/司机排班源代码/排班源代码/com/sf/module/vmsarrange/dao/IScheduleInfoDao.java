/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-30     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleInfo;

/**
 * 
 * 班次信息dao接口
 * @author 方芳 (350614) 2014-5-30
 */
public interface IScheduleInfoDao extends IEntityDao<ScheduleInfo> {
	/**
	 * 查找重复数据的条数
	 * @param startTm
	 * @param endTm
	 * @param startDeptId
	 * @param endDeptId
	 * @return
	 */
	public Integer listRepeat(final String startTm,final String endTm,final Long startDeptId,final Long endDeptId,final Long id);
	/**
	 * 查找指定车牌号的所有班次信息
	 * @param vehicleCode
	 * @return
	 */
	public List<ScheduleInfo> listByCode(String vehicleCode);
	/**
	 * 查找分页数据
	 * @param deptId
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @return
	 */
	public IPage<ScheduleInfo> listPage(Long deptId,Integer dataSource,String vehicleCode,Integer valid,Integer isUsed,int pageSize,int pageIndex,Long userId);
	/**
	 * 为配班查找分页数据
	 * @param deptId
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @return
	 */
	public IPage<ScheduleInfo> listPageForArrange(Long deptId,Long recordId,String startTm, int pageSize,
			int pageIndex, Long userId);
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteByIds(List<Long> ids);
	/**
	 * 导出报表
	 * @param deptId
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @param startIdx
	 * @param endIdx
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> listReport(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed,int startIdx,int endIdx,Long userId);
	/**
	 * 查找报表数据总数
	 * @param deptId
	 * @param dataSource
	 * @param vehicleCode
	 * @param valid
	 * @param startIdx
	 * @param endIdx
	 * @param userId
	 * @return
	 */
	public Long listReportCount(Long deptId,Integer dataSource,
			String vehicleCode,Integer valid,Integer isUsed,Long userId);
	/**
	 * 查找班次明细信息
	 * @param startTm
	 * @param endTm
	 * @param startDeptId
	 * @param endDeptId
	 * @param deptId
	 * @param vehicleCode
	 * @return
	 */
	public ScheduleInfo listInfo(String startTm, String endTm,
			String startDeptCode, String endDeptCode, String vehicleCode,Long userId);
	/**
	 * 查找班次明细信息-通过部门id
	 * @param startTm
	 * @param endTm
	 * @param startDeptId
	 * @param endDeptId
	 * @param vehicleCode
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> listInfoById(String startTm, String endTm,
			Long startDeptId, Long endDeptId, String vehicleCode,Long userId);
	/**
	 * 查找手工录入线路
	 * @param startTm
	 * @param endTm
	 * @param startDeptId
	 * @param endDeptId
	 * @param userId
	 * @return
	 */
	public ScheduleInfo listInfoByStartEnd(String startTm, String endTm,
			Long startDeptId, Long endDeptId,Long userId);
	/**
	 * 查找手工录入线路
	 * @param startTm
	 * @param endTm
	 * @param startDeptId
	 * @param endDeptId
	 * @param userId
	 * @return
	 */
	public ScheduleInfo listInfoByStartEndDept(String startTm, String endTm,
			Long startDeptId, Long endDeptId,Long deptId,Long userId);
}
