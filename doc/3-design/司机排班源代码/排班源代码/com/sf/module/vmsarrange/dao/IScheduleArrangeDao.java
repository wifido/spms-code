/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-6     方芳                         创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.ScheduleArrange;

/**
 * 
 * 配班管理dao接口
 * @author 方芳 (350614) 2014-6-6
 */
public interface IScheduleArrangeDao extends IEntityDao<ScheduleArrange> {
	/**
	 * 查询配班分页数据
	 * @param deptId
	 * @param arrangeNo
	 * @param valid
	 * @param pageSize
	 * @param pageIndex
	 * @param userId
	 * @return
	 */
	public IPage<ScheduleArrange> listPage(Long deptId,String arrangeNo,Integer valid,Integer isUsed,int pageSize,int pageIndex,Long userId);
	/**
	 * 查询配班分页数据-班次下拉框
	 * @param deptId
	 * @param arrangeNo
	 * @param pageSize
	 * @param pageIndex
	 * @param userId
	 * @return
	 */
	public IPage<ScheduleArrange> listArrangePage(Long deptId,Integer arrangeType, String arrangeNo,int pageSize, int pageIndex, Long userId);
	/**
	 * 查找指定班次代码
	 * @param deptCode
	 * @return
	 */
	public int listArrange(String arrangeNo);
	/**
	 * 获取报表数据总数
	 * @param deptId
	 * @param arrangeNo
	 * @param valid
	 * @param userId
	 * @return
	 */
	public Long listReportCount(Long deptId,String arrangeNo,Integer valid,Integer isUsed,Long userId);
	/**
	 * 获取报表数据
	 * @param deptId
	 * @param arrangeNo
	 * @param valid
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> listReport(Long deptId,String arrangeNo,Integer valid,Integer isUsed,Long userId);
	/**
	 * 查找机动班重复数据的条数
	 * @param startTm
	 * @param endTm
	 * @param deptId
	 * @return
	 */
	public Integer listRepeat(String startTm, String endTm, Long deptId,Long id);
	
	/**
	 * 根据配班班次代码获取配班信息
	 * @param arrangeNo 配班班次代码
	 * @return
	 */
	public ScheduleArrange findArrByArrangeNo(String arrangeNo,Long deptId,Long userId);
	/**
	 * 根据配班班次代码集合获取配班信息
	 * @param arrangeNo
	 * @param deptId
	 * @param userId
	 * @return
	 */
	public List<ScheduleArrange> findArrByArrangeNoList(List<String> arrangeNo,Long deptId,Long userId);
	/**
	 * 根据班次代码获取班次信息
	 * @param arrangeNo
	 * @return
	 */
	public ScheduleArrange findArrByArr(String arrangeNo);
	/**
	 * 置为无效-下个月预排班没有使用则可以置为无效
	 * @param arrangeId
	 * @return
	 */
	public int updateInvalid(final Long arrangeId);
	/**
	 * 判断是否被有效的班次使用
	 * @param infoId
	 * @return
	 */
	public String isExistByInfo(Long infoId);
	/**
	 * 判断是否被其他班次使用-不与自己比较
	 * @param infoId
	 * @return
	 */
	public String isExistByInfoId(Long infoId,Long arrangeId);
}
