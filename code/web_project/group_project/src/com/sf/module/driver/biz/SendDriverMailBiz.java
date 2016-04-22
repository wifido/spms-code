package com.sf.module.driver.biz;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.authorization.dao.IRoleDao;
import com.sf.module.authorization.dao.IUserDao;
import com.sf.module.driver.dao.SendDriverMailDao;
import com.sf.module.organization.dao.IEmployeeDao;
import com.sf.module.organization.domain.Employee;

@Component
public class SendDriverMailBiz extends BaseBiz {
	@Resource
	private IRoleDao roleDao;
	
	@Resource
	private IUserDao userDao;
	
	@Resource
	private IEmployeeDao employeeDao;
	
	@Resource
	private SendDriverMailDao sendDriverMailDao;
	
	public Long findRoleIdByName(String roleName) {
		List<Long> roleIds = roleDao.findRoleIdsByName(roleName);
		if (roleIds.size() < 1)
			return 0L;
		return roleIds.get(0);
	}

	public List<Long> findUsersHasRole(final Long roleId) {
		return userDao.findUsersHasRole(roleId);
	}
	
	public List<Long> findUserHasdepts(final Long userId) {
		return userDao.findUserHasdepts(userId);
	}
	
	public String findUser(final Long userId) {
		if(null == sendDriverMailDao.queryUsers(userId))
			return "";
		return  sendDriverMailDao.queryUsers(userId).get("USERNAME").toString();
	}
	
	public Employee getEmployeeByCode(String employeeCode) {
		return null == employeeDao.getEmployeeByCode(employeeCode) ? new Employee() :  employeeDao.getEmployeeByCode(employeeCode);
	}
	
	public String generateAccessories(Long userId) {
		List list = sendDriverMailDao.generateAccessories(userId);
		if (list.size() == 0) {
			return "";
		}
		SendMailFileHandler sendMailFileHandler = new SendMailFileHandler();
		sendMailFileHandler.write(list, "异常报表");
	    return sendMailFileHandler.getTargetFilePath();
	}
}
