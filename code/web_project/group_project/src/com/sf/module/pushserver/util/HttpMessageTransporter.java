package com.sf.module.pushserver.util;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.sf.module.common.cache.SpmsSysConfigCache;
import com.sf.module.pushserver.domain.BatchUserMessageDto;
import com.sf.module.pushserver.domain.UserMessageDto;

@Component
public class HttpMessageTransporter implements MessageTransporter {

	private static final String PUSH_API_BATCH = "/push/api/messages/batch";

	private static final String PUSH_API = "/push/api/messages";

	private static final Logger log = LoggerFactory.getLogger("pushMessageLog");

	private String apiAddress;

	private String batchApiAddress;

	private static final String CONFIG_KEY_PN_SERVER_HOST_ADDRESS = "PN_SERVER_HOST_ADDRESS";

	@Override
	public int send(UserMessageDto userMessageDto) throws IOException {
		setConfiguredHostAddress();
		String requestContent = Json.toJson(userMessageDto);
		HttpResponse response = PNClient.doPost(apiAddress, requestContent);
		return response.getStatusLine().getStatusCode();
	}

	@Override
	public int sendBatch(BatchUserMessageDto batchUserMessageDto) throws IOException {
		setConfiguredHostAddress();
		String requestContent = Json.toJson(batchUserMessageDto);
		HttpResponse response = PNClient.doPost(batchApiAddress, requestContent);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 500) {
			String errorMsgId = EntityUtils.toString(response.getEntity());
			log.info("errorMsgIds:{}", errorMsgId);
		}
		return statusCode;
	}

	private void setConfiguredHostAddress() {
		String hostAddress = SpmsSysConfigCache.getCacheByKeyName(CONFIG_KEY_PN_SERVER_HOST_ADDRESS);
		apiAddress = hostAddress + PUSH_API;
		batchApiAddress = hostAddress + PUSH_API_BATCH;
	}
}
