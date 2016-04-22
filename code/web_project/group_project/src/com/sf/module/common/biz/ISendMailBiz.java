package com.sf.module.common.biz;

import java.io.File;

import com.sf.framework.server.base.biz.IBiz;

public interface ISendMailBiz extends IBiz {
	public boolean sendMail(String toEmail, String content, File attachment, String attachmentName, String subject);
	public boolean updateEmailStartStatus();
	public boolean updateEmailEndStatus();
}
