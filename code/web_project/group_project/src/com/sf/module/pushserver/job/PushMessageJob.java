package com.sf.module.pushserver.job;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sf.module.common.cache.SpmsSysConfigCache;
import com.sf.module.pushserver.biz.MessageBiz;
import com.sf.module.pushserver.domain.Message;
import com.sf.module.pushserver.util.HttpMessageTransporter;

@Component
public class PushMessageJob {
	private static final String CONFIG_KEY_SWITCH_PUSH_MESSAGE_TO_PNSERVER = "SWITCH_PUSH_MESSAGE_TO_PNSERVER";
	private static final Logger log = LoggerFactory.getLogger("pushMessageLog");
	private static final String ON_PUSH_SWITCH = "1";

	@Autowired
	private MessageBiz messageBiz;

	@Autowired
	private HttpMessageTransporter httpMessageTransporter;

	@Scheduled(cron = "0 0/5 * * * ?")
	public void pushMssege() throws Exception {
		log.info("push message to PnServer begin!----");
		
		//判断是否开启推送服务
		String  pushSwitch= SpmsSysConfigCache.getCacheByKeyName(CONFIG_KEY_SWITCH_PUSH_MESSAGE_TO_PNSERVER);
		log.info("push message switch is----:"+pushSwitch);
		if(!ON_PUSH_SWITCH.equals(pushSwitch)){
			return;
		}
		
		//锁定要发送的消息
		messageBiz.lockMessage();
		
		//获取待发送消息
		List<Message> msgs = messageBiz.getNeedSendMessage();
		log.info("message acount-----:" + msgs.size());
		if (msgs.isEmpty()) {
			return;
		}
		
		//遍历发布
		for (Message message : msgs) {
			try {
				int statusCode = httpMessageTransporter.send(message.buildMsgDto());
				if (statusCode == 201) {
					messageBiz.handSendSucessMessage(message);
					continue;
				}
				message.setFailDesc(String.valueOf(statusCode));
			} catch (IOException e) {
				message.setFailDesc(e.getMessage());
			}
			messageBiz.handSendFailMessage(message);
		}
	}
}
