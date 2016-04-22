package com.sf.module.pushserver.domain;

import java.util.List;

public class BatchUserMessageDto {
	private List<UserMessageDto> userMessageBatchDto;

	public BatchUserMessageDto() {
	}

	public BatchUserMessageDto(List<UserMessageDto> userMessageBatchDto) {
		this.userMessageBatchDto = userMessageBatchDto;
	}

	public List<UserMessageDto> getUserMessageBatchDto() {
		return userMessageBatchDto;
	}

	public void setUserMessageBatchDto(List<UserMessageDto> userMessageBatchDto) {
		this.userMessageBatchDto = userMessageBatchDto;
	}
}
