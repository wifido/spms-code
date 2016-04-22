package com.sf.module.pushserver.domain;

public class UserMessageDto {

	public String userId;
	public String appId;
	private String fromSource;
	public String message;
	public String callbackUrl;
	public int timeout;
	public int priority;
	private String mId;
	public String deviceId;

	public UserMessageDto(String mId, String appId, String userId, String deviceId, String message, String callbackUrl, int timeout, int priority) {
		this.mId = mId;
		this.deviceId = deviceId;
		this.userId = userId;
		this.appId = appId;
		this.message = message;
		this.callbackUrl = callbackUrl;
		this.timeout = timeout;
		this.priority = priority;
	}

	public UserMessageDto(String mId, String appId, String fromSource, String userId, String deviceId, String message, String callbackUrl, int timeout,
	        int priority) {
		this.mId = mId;
		this.fromSource = fromSource;
		this.deviceId = deviceId;
		this.userId = userId;
		this.appId = appId;
		this.message = message;
		this.callbackUrl = callbackUrl;
		this.timeout = timeout;
		this.priority = priority;
	}
}
