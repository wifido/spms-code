/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-6-19     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;

/**
 *
 * IPreScheduleDraftDao处理类
 *
 */
public interface IPreScheduleDraftDao extends IEntityDao<PreScheduleDraft> {
	/**
	 * 根据驾驶员id加载驾驶员
	 * @param empCode
	 * @return
	 */
	PreScheduleDraft listByDriver(String yearMonth,Long driverId);
	/**
	 * 根据月份加载
	 * @param yearMonth
	 * @return
	 */
	List<PreScheduleDraft> listAllByMonth(String yearMonth);
	/**
	 * 根据驾驶员ID获取预排班草稿表中的已经安排排班的驾驶员
	 * @param id 		--驾驶员ID
	 * @return
	 */
	Integer findDriverById(String yearMonth,Long id);

	/**
	 * 分页才查询预排班草稿
	 * @param deptCode
	 * @param empCode
	 * @param classType
	 * @param yearMonth
	 * @param start
	 * @param limit
	 * @param userId
	 * @return
	 */
	IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex, Long userId);
	/**
	 * 获取报表数据--实际排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> listRealReport(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);
	
	/**
	 * 获取报表数据总数--实际排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	Long listRealReportCount(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);
	/**
	 * 获取报表数据--预排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> listPreReport(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);
	
	/**
	 * 获取报表数据总数--预排班
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	Long listPreReportCount(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);
	/**
	 * 获取报表数据--配班信息
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> listArrReport(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);
	
	/**
	 * 获取报表数据总数--配班信息
	 * @param deptCode
	 * @param empCode
	 * @param yearMonth
	 * @param userId
	 * @return
	 */
	Long listArrReportCount(String deptCode, String empCode, String yearMonth,Long userId,Integer classType);

	/**
	 * 判断网点是否是该用户的权限下网点
	 * @param deptCode
	 * @param userId
	 * @return
	 */
	boolean isDeptCodeForUser(String deptCode, Long userId);

	/**
	 * 网点代码下是否有该驾驶员 
	 * @param driverDeptId
	 * @param deptId
	 * @return
	 */
	boolean isDriverForDept(Long driverDeptId, String deptCode);

	/**
	 * 判断驾驶员是否已经排班
	 * @param empCode
	 * @param yearMonth
	 * @return
	 */
	Integer listRepeat(String empCode, String yearMonth);
	/**
	 * 判断班次是否被下一个预排班使用
	 * @param yearMonth
	 * @param arrangeId
	 * @return
	 */
	String isExistByArrange(Long arrangeId);
}
