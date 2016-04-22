package com.sf.module.pushserver.biz;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.sf.module.esbinterface.util.VMUtils;
import com.sf.module.pushserver.dao.MessageDao;
import com.sf.module.pushserver.dao.MessageHistoryDao;
import com.sf.module.pushserver.domain.Message;
import com.sf.module.pushserver.domain.MessageHistory;

@Component
public class MessageBiz {
	@Resource
	private MessageDao messageDao;
	@Autowired
	private MessageHistoryDao messageHistoryDao;

	public List<Message> getNeedSendMessage() {
		DetachedCriteria dc = DetachedCriteria.forClass(Message.class);
		dc.add(Restrictions.eq("hostName", VMUtils.getCurrentMachineName()));
		dc.add(Restrictions.sqlRestriction(" CREATE_TIME >= sysdate-2 "));
		dc.add(Restrictions.eq("status", 3L));
		return messageDao.findBy(dc);
	}

	@Transactional
	public void lockMessage() {
		messageDao.lockMessage();
	}

	@Transactional
	public void updateMessage(Message message) {
		messageDao.update(message);
	}

	@Transactional
	public void handSendSucessMessage(Message message) {
		MessageHistory bulidMessageHistory = message.bulidMessageHistory();
		messageHistoryDao.save(bulidMessageHistory);
		messageDao.remove(message);
	}

	@Transactional
	public void handSendFailMessage(Message message) {
		message.setStatus(2L);
		message.setSendTime(new Date());
		messageDao.update(message);
	}

}
