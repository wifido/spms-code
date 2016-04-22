/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-07-02     houjingyu       创建
 **********************************************/

package com.sf.module.operation.biz;

import com.sf.framework.server.base.biz.IBiz;

/**
 *
 * 提交确认状态的业务接口
 * @author houjingyu  2014-07-02
 *
 */
public interface IMonthConfirmStatusBiz extends IBiz {
	public void saveConfirmStatus(Long deptid,String ym);
}