package com.sf.module.pushserver.util;

import com.sf.module.pushserver.domain.BatchUserMessageDto;
import com.sf.module.pushserver.domain.UserMessageDto;

public interface MessageTransporter {

	public int send(UserMessageDto userMessageDto) throws Exception;

	public int sendBatch(BatchUserMessageDto batchUserMessageDto) throws Exception;
}
