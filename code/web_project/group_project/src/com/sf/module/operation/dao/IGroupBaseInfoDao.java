package com.sf.module.operation.dao;

import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.module.operation.domain.GroupBaseInfo;

public interface IGroupBaseInfoDao extends IEntityDao<GroupBaseInfo> {
	/**
	 * 页面查询功能
	 * 
	 * @param queryParam
	 * @param isRecursion
	 * @param userId
	 * @return
	 */
	IPage<GroupBaseInfo> findPageByPage(GroupBaseInfo groupBaseInfo, Boolean isRecursion, Long userId, int pageSize, int pageIndex);

	/**
	 * 验证网点与小组名称是否存在
	 * 
	 * @param groupName
	 * @param deptId
	 * @return
	 */
	boolean GroupNameExistInDept(String groupName, long deptId);

	/**
	 * 验证网点代码是否存在
	 * 
	 * @param groupCode
	 * @return
	 */
	boolean GroupCodeExistInAll(String groupCode);

	/**
	 * 查询所有满足条件数据
	 * 
	 * @param groupBaseInfo
	 * @return
	 */
	List<GroupBaseInfo> findAllBaseInfos(GroupBaseInfo groupBaseInfo, Boolean isRecursion, Long userId);

	/**
	 * 失效日期一周前进行窗口提醒
	 * 
	 * @param deptId
	 * @return
	 */
	boolean noticeHasEmployee(Long deptId);

	/**
	 * 返回即将失效的组别数据
	 */
	List<GroupBaseInfo> noticeShowList(Long deptId);

	public boolean queryGroupValidity(Long groupId, Long departmentId);

}
