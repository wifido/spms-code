/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-29     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;

/**
 *
 * IPreScheduleBizDao处理类
 *
 */
public interface IPreScheduleDao extends IEntityDao<PreSchedule> {

	/**
	 * 分页查询排班信息
	 * @param deptCode		--网点代码
	 * @param empCode		--驾驶员工号
	 * @param classType		--班次类型
	 * @param yearMonth		--年月份
	 * @param start			--开始数据行号
	 * @param limit			--每页显示的条数
	 * @return
	 */
	IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex,Long userId);

	/**
	 * /**
	 * 根据配班班次代码，查找出该用户权限网点下的所有班次，填充到下拉框中
	 * @param arrangeNo		--班次代码
	 * @return
	 */
	Map findArrangeNos(Long deptId,String arrangeNo, Long userId);

	/**
	 * 根据网点ID，驾驶员工号,月度，查找出该用户权限网点下的所有驾驶员，填充到下拉框中
	 * @param deptId	--网点ID
	 * @param empCode	--驾驶员工号
	 * @param userId	当前用户的ID
	 * @return
	 */
	Map findDrivers(Long deptId, String empCode,String yearMonth, Long userId);

	/**
	 * 调班页面获取驾驶员信息
	 * @param optEmpCode
	 * @param optDate
	 * @return
	 */
	List<Object> findOptDriver(String optEmpCode, String optDate,String deptCode,Long userId);

	/**
	 * 根据主表主键和附表的日期查找出附表中记录调班过程的字段值remark
	 * @param id
	 * @param dateStr
	 * @return
	 */
	List<Object> findOptInfo(Long id, String dateStr);

	/**
	 * 检查是否有重复的数据
	 * @param empCode
	 * @param yearMonth
	 * @return
	 */
	Integer listRepeat(String empCode, String yearMonth);
	/**
	 * 根据驾驶员ID获取预排班草稿表中的已经安排排班的驾驶员
	 */
	public Integer findDriverById(String yearMonth,Long id);
	/**
	 * 根据驾驶员id加载驾驶员
	 * @param empCode
	 * @return
	 */
	PreSchedule listByDriver(String yearMonth,Long driverId);
}
