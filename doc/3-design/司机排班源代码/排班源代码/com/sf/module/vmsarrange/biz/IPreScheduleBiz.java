/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-28     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.biz;

import java.util.List;
import java.util.Map;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.ScheduleArrange;
import com.sf.module.vmsarrange.dto.ParamsDto;

/**
 *
 * IPreScheduleBiz处理类
 *
 */
public interface IPreScheduleBiz extends IBiz {

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
	@SuppressWarnings("rawtypes")
	IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex)  throws Exception;
	/**
	 * 保存预排班草稿
	 * @param preSchedules
	 */
	void savePreSchedule(List<PreSchedule> preSchedules) throws Exception;

	/**
	 * 修改预排班草稿数据
	 * @param preSchedules
	 */
	void updateSchedule(List<PreSchedule> preSchedules) throws Exception;
	/**
	 * 根据网点ID，配班班次代码，查找出该用户权限网点下的所有班次，填充到下拉框中
	 * @param deptId	 --网点ID
	 * @param arrangeNo  --班次代码
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map findArrangeNos(Long deptId,String arrangeNo);
	/**
	 * 根据网点ID，驾驶员工号，月度,查找出该用户权限网点下的所有驾驶员，填充到下拉框中
	 * @param deptId 	--网点ID
	 * @param empCode	--驾驶员工号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map findDrivers(Long deptId, String empCode);
	/**
	 * 检测是否有数据覆盖
	 * @param preSchedules		--实体对象
	 * @return
	 */
	String testRepeat(List<PreSchedule> preSchedules);
	/**
	 * 获取具体一天的班次信息明细
	 * @param id
	 * @param dayNum 
	 * @param yearMonth 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map findByCondition(Long id, String yearMonth, Integer dayNum);
	/**
	 * 获取调班信息
	 * @param id
	 * @param yearMonth
	 * @param dayNum
	 * @return
	 */
	List<Object> findOptInfoByCondition(Long id, String yearMonth, String dayNum);
	/**
	 * 调班页面获取被调班驾驶员信息
	 * @param optEmpCode	--被调班人的工号
	 * @param optDate		--日期
	 * @return
	 */
	List<Object> findOptDriver(String optEmpCode, String optDate,Long deptId);
	/**
	 * 调班保存信息
	 * @param paramsDto
	 * @return
	 */
	void saveOptInfo(ParamsDto paramsDto,Integer ignore) throws Exception;
	/**
	 * 获取标题部分的区部,分部
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map findAreaDeptName(Long deptId);
	/**
	 * 班次代码下拉框取数
	 * @return
	 */
	IPage<ScheduleArrange> listArrangePage(Long deptId,Integer arrangeType, String arrangeNo,int pageSize, int pageIndex);
}
