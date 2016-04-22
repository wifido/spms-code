package com.sf.module.common.biz;

import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.ISendMailDao;
import com.sf.module.common.domain.MailConfiguration;

@SuppressWarnings({"rawtypes", "unused"})
public class SendMailBiz extends BaseBiz implements ISendMailBiz {
    static Logger LOG = LoggerFactory.getLogger(SendMailBiz.class);
    private MailConfiguration mailConfiguration;
    private ISendMailDao sendMailDao;

    public MailConfiguration getMailConfiguration() {
        return mailConfiguration;
    }

    public void setMailConfiguration(MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;
    }

    public void setSendMailDao(ISendMailDao sendMailDao) {
        this.sendMailDao = sendMailDao;
    }

    public boolean sendMail(String toEmail, String content, File attachment, String attachmentName, String subject) {
        JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
        javaMail.setHost(mailConfiguration.getHost());
        javaMail.setPassword(mailConfiguration.getPassword());
        javaMail.setUsername(mailConfiguration.getUsername());
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", mailConfiguration.getAuth());
        javaMail.setJavaMailProperties(prop);
        MimeMessage message = javaMail.createMimeMessage();
        MimeMessageHelper messageHelp;
        try {
            messageHelp = new MimeMessageHelper(message, true, "GBK");
            messageHelp.setFrom(mailConfiguration.getReceiver());
            messageHelp.setTo(toEmail);
            messageHelp.setSubject(subject);
            messageHelp.setText(content, true);
			if (null != attachment)
				messageHelp.addAttachment(attachmentName, attachment);
            javaMail.send(message);
        } catch (Exception e) {
            LOG.error("The exception from the AbstractSendMail.sendMail", e);
            return false;
        }
        return true;
    }

    public boolean updateEmailStartStatus() {
        return sendMailDao.updateEmailStartStatus();
    }

    public boolean updateEmailEndStatus() {
        return sendMailDao.updateEmailEndStatus();
    }
}