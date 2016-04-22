package com.sf.module.esbinterface.job;

import static com.sf.module.esbinterface.fileutil.ZipUtil.doZip;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sf.module.common.cache.SpmsSysConfigCache;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.esbinterface.biz.DriverLogBiz;
import com.sf.module.esbinterface.domain.DriverLog;
import com.sf.module.esbinterface.fileutil.DriverLogFileUtil;
import com.sf.module.esbinterface.fileutil.FileUtil;
import com.sf.module.esbinterface.util.Constants;
import com.sf.module.esbinterface.webservice.sap.DriverLogPackageSenderServiceProxy;
import com.sf.sftp.client.ISftpClient;
import com.sf.sftp.client.SftpClientManager;
import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;

@Component
public class PushDriverLogJob {
	private static final String ON_PUSH_SWITCH = "1";
	private static final Logger log = LoggerFactory.getLogger("PushDriverLog");
	private static final String CONFIG_KEY_SWITCH_PUSH_DRIVER_LOG_TO_SAP = "SWITCH_PUSH_DRIVER_LOG_TO_SAP";
	private static final String CONFIG_KEY_ESB_WS_SAP_DELIVERY_SERVICE_URL = "ESB_WS_SAP_DELIVERY_SERVICE_URL";
	public static final String DATA_TYPE = "HCM-IN_PTKZ";
	private String SAP_SFTP_UPLOAD_FILE_PATH;
	private String uploadZipFileName;
	private String esbServiceUrl;

	@Autowired
	private DriverLogBiz driverLogBiz;
	@Autowired
	private DriverLogPackageSenderServiceProxy serviceProxy;

	public static final int MAX_ITEM_IN_ONE_FILE = 50000;

	@Scheduled(cron = "0 30 0 * * ?")
	public void push() throws Exception {
		//判断是否开启推送服务
		String  pushSwitch= SpmsSysConfigCache.getCacheByKeyName(CONFIG_KEY_SWITCH_PUSH_DRIVER_LOG_TO_SAP);
		log.info("push switch is----:"+pushSwitch);
		if(!ON_PUSH_SWITCH.equals(pushSwitch)){
			return;
		}
		
		log.info("push driver log data to SAP begin!----------");
		
		// 更新行车日志状态为处理中 
		log.info("update driver log status to processing!----");
		driverLogBiz.updateLogStatusToProcessing();
		
		// 获取需要推送的行车日志数量
		int totalSize = driverLogBiz.countOfDriverLog();
		log.info(String.format("need to push count:%s", totalSize));

		if (totalSize < 1)
			return;
		
//		//执行处理处理行车日志脚本
//		log.info("excute procedure for hander driver source log!------");
//		driverLogBiz.excuteProcedureForGetDriveLog();


		// 初始化参数
		initConfigParam();

		// 删除文件数据
		log.info("delete local send file!--------");
		FileUtil.deleteDirectoryFiles(DriverLogFileUtil.UPLOAD_FILE_LOCAL_SAVE_PATH);
		long startTm = System.currentTimeMillis();

		// 生成本地xml文件
		log.info("create local xml file!----------");
		writeDataToXMLFile(totalSize);

		// 生成上传压缩文件
		log.info("create upload zip file!------");
		File zipFile = doZip(DriverLogFileUtil.getSavePath());

		// 调用上传接口
		UploadStrategyInfoBean uploadStrategyInfo = getUploadStrategyInfoBean();
		
		
		String filePath = System.getenv().get("CONF_PATH") + "/classes/SAPESBInfo.properties";
		log.info("esb config file path:" + filePath);
		ISftpClient sftpClient = SftpClientManager.getSftpClient(filePath);

		String failDesc = "";
		// 上传文件
		try {
			log.info("upload file begin !----------");
			boolean upload = sftpClient.upload(zipFile, uploadStrategyInfo);
			log.info("result-->" + upload + "--upload file cost time------>" + (System.currentTimeMillis() - startTm));
			if (upload) {

				// 上传文件成功，调用提醒服务接口，通知SAP 处理
				log.info("upload success ! request notify Server!----- ");
				serviceProxy.notifyServer(SAP_SFTP_UPLOAD_FILE_PATH, uploadZipFileName, DATA_TYPE);

				// 调用提醒服务接口成功后，更新数据状态为success
				driverLogBiz.handPushSucess();
				log.info("hand success data to db success !--------");
				return;
			}
			failDesc = " upload file fail  ";
		} catch (Exception e) {
			if (e.getClass().getName().equals("com.sf.sftp.core.exception.SfSftpESBHostConnectionException")) {
				failDesc = "sftp主机连接有问题！";
			} else {
				failDesc = e.getMessage();
			}
			log.error("push driver log to sap error!---:" + e.toString() + e.getMessage());
		}
		driverLogBiz.handPushFail(failDesc);
		log.info("hand fail data to db success !--------");
	}

	private void initConfigParam() throws IOException {
		log.info("init param begin!-----");
		SAP_SFTP_UPLOAD_FILE_PATH = "VOL_sftp1/SAP_in/" + new DateTime().toString("yyyyMMdd") + "/" + DATA_TYPE;
		uploadZipFileName = Constants.FILE_PREFIX + UUID.randomUUID().toString().replace("-", "") + ".zip";
		esbServiceUrl = SpmsSysConfigCache.getCacheByKeyName(CONFIG_KEY_ESB_WS_SAP_DELIVERY_SERVICE_URL);
		serviceProxy.setServiceUrl(esbServiceUrl);
		log.info("SAP_SFTP_UPLOAD_FILE_PATH:" + SAP_SFTP_UPLOAD_FILE_PATH);
		log.info("uploadZipFileName:" + uploadZipFileName);
		log.info("serviceUrl:" + esbServiceUrl);
	}

	private void writeDataToXMLFile(int count) throws Exception {
		String batchFileNumber = DateUtil.formatDate(new Date(), DateFormatType.yyyyMMdd_HHmmss);
		int totalPage = Double.valueOf(Math.ceil(count / (float) MAX_ITEM_IN_ONE_FILE)).intValue();
		log.info("MAX_ITEM_IN_ONE_FILE-----:" + MAX_ITEM_IN_ONE_FILE);
		log.info("total xml files acount-----:" + totalPage);
		List<DriverLog> logList;
		if (totalPage == 1) {
			logList = driverLogBiz.listDriverLog(MAX_ITEM_IN_ONE_FILE, 0);
			DriverLogFileUtil.writeFile(logList, batchFileNumber, logList.size(), 1);
			return;
		}
		for (int i = 0; i < totalPage; i++) {
			logList = driverLogBiz.listDriverLog(MAX_ITEM_IN_ONE_FILE, i);
			DriverLogFileUtil.writeFile(logList, batchFileNumber, count, i + 1);
		}
	}

	private UploadStrategyInfoBean getUploadStrategyInfoBean() {
		UploadStrategyInfoBean uploadStrategyInfo = new UploadStrategyInfoBean();
		uploadStrategyInfo.setBlMd5(true);
		uploadStrategyInfo.setBlZipCompress(false);
		uploadStrategyInfo.setResume(true);
		uploadStrategyInfo.setStrRemotePath(SAP_SFTP_UPLOAD_FILE_PATH);
		uploadStrategyInfo.setStrRemoteFileName(uploadZipFileName);
		return uploadStrategyInfo;
	}
}
