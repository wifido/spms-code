/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-17     wen.jun       创建
 **********************************************/

package com.sf.module.ossinterface.biz;

import java.util.List;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.ossinterface.domain.HrEmpInfoAlter;

/**
 *
 * HR系统接口增量,新员工入职或者员工离职/员工的岗位调动或者部门调动的业务接口
 * @author wen.jun  2014-06-17
 *
 */
public interface IHrEmpInfoAlterBiz extends IBiz {
	
    /**
     * <pre>
     * 保存员工增量信息
     * </pre>
     * @author 文俊 (337291)
     * @date May 28, 2014 
     * @param list
     */
    public void save(List<HrEmpInfoAlter> entities);
}