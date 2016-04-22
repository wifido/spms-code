package com.sf.module.pushserver.domain;

import java.util.Date;
import com.sf.framework.base.domain.BaseEntity;

public class MessageHistory extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Long msgId;
	private String msg;
	private String userId;
	private Long status;
	private Date sendTime;
	private Date createDate;
	private String failDesc;
	private String hostName;

	public String getHostName() {
    	return hostName;
    }

	public void setHostName(String hostName) {
    	this.hostName = hostName;
    }
	
	public String getFailDesc() {
		return failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
