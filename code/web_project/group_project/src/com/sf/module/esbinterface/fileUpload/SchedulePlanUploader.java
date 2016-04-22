package com.sf.module.esbinterface.fileUpload;

import static com.sf.module.esbinterface.fileutil.ZipUtil.doZip;
import static com.sf.sftp.client.SftpClientManager.*;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.google.common.collect.Lists;
import com.sf.module.esbinterface.biz.ISchedulePlanHandlerBiz;
import com.sf.module.esbinterface.fileutil.FileUtil;
import com.sf.module.esbinterface.fileutil.ScheduleFileUtil;
import com.sf.module.esbinterface.fileutil.ScheduleNode;
import com.sf.module.esbinterface.util.Constants;
import com.sf.module.esbinterface.webservice.sap.PackageSenderServiceProxy;
import com.sf.sftp.client.ISftpClient;
import com.sf.sftp.client.SftpClientManager;
import com.sf.sftp.client.infoBean.UploadStrategyInfoBean;

public class SchedulePlanUploader {
	public static final Logger logger = LoggerFactory.getLogger(SchedulePlanUploader.class);
	public static final int MAX_ITEM_IN_ONE_FILE = 10000;
	public static final String UPLOAD_FILE_ERROR = "upload file error ";
	public static final boolean DO_NOT_COMPRESS = false;
	public static String uploadZipFileName = Constants.DEFAULT_UPLOAD_FILE_NAME;
	private ISchedulePlanHandlerBiz schedulePlanHandlerBiz;
	private PackageSenderServiceProxy senderServiceProxy;

	private static UploadStrategyInfoBean getUploadStrategyInfoBean() {
		UploadStrategyInfoBean uploadStrategyInfo = new UploadStrategyInfoBean();
		uploadStrategyInfo.setBlMd5(true);
		uploadStrategyInfo.setBlZipCompress(DO_NOT_COMPRESS);
		uploadStrategyInfo.setResume(true);
		uploadStrategyInfo.setStrRemotePath(Constants.SF_UPLOAD_PATH);
		uploadZipFileName = Constants.FILE_PREFIX + UUID.randomUUID().toString().replace("-", "") + ".zip";
		uploadStrategyInfo.setStrRemoteFileName(uploadZipFileName);
		return uploadStrategyInfo;
	}

	public void setSenderServiceProxy(PackageSenderServiceProxy senderServiceProxy) {
		this.senderServiceProxy = senderServiceProxy;
	}

	public void setSchedulePlanHandlerBiz(ISchedulePlanHandlerBiz schedulePlanHandlerBiz) {
		this.schedulePlanHandlerBiz = schedulePlanHandlerBiz;
	}

	public void resendFile() throws Exception {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				FileUtil.deleteDirectoryFiles(ScheduleFileUtil.UPLOAD_FILE_LOCAL_SAVE_PATH);
				List<ScheduleNode> todayNeedUploadSchedules = schedulePlanHandlerBiz
						.getNeedResendSchedules();
				try {
					doUpload(todayNeedUploadSchedules);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		thread.setName("spms2sap_resendFile_" + System.currentTimeMillis());
		thread.start();
	}

	@Scheduled(cron = "0 30 1 * * ?")
	public void upload() throws Exception {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 删除文件数据
				FileUtil.deleteDirectoryFiles(ScheduleFileUtil.UPLOAD_FILE_LOCAL_SAVE_PATH);

				// db 数据读取
				List<ScheduleNode> todayNeedUploadSchedules = schedulePlanHandlerBiz
						.getNeedUploadSchedules();

				try {
					doUpload(todayNeedUploadSchedules);
				} catch (Exception e) {
					logger.error(e.getMessage());
					schedulePlanHandlerBiz.updateScheduleStatusToFailure(e
							.getMessage());
				}
			}
		});
		thread.setName("spms2sap_upload_" + System.currentTimeMillis());
		thread.start();
	}

	private void doUpload(List<ScheduleNode> todayNeedUploadSchedules) throws Exception {
		if (todayNeedUploadSchedules == null || todayNeedUploadSchedules.isEmpty())
			return;

		// 生成上传压缩包
		long startTm = System.currentTimeMillis();
		List<File> sendFiles = writeFileRecursion(todayNeedUploadSchedules);
		logger.info("write file cost time------>" + (System.currentTimeMillis() - startTm));

		startTm = System.currentTimeMillis();

		// 调用上传接口
		UploadStrategyInfoBean uploadStrategyInfo = getUploadStrategyInfoBean();

		// 调用webservice 接口通知
		try {
			File zipFile = doZip(ScheduleFileUtil.getEsbFilePath());

			String filePath = System.getenv().get("CONF_PATH") + "/classes/SAPESBInfo.properties";
			logger.info("esb config file filePath----:" + filePath);
			//ISftpClient sftpClient = SftpClientManager.getSftpClientInstance(filePath2, SftpClientManager.SAP_CLIENT_TYPE);
			ISftpClient sftpClient = SftpClientManager.getSftpClient(filePath);
			// 修改读取ESB配置规则
			boolean upload = sftpClient.upload(zipFile, uploadStrategyInfo);
			logger.info("result-->" + upload + "--upload file cost time------>" + (System.currentTimeMillis() - startTm));

			if (!upload) {
				logger.error(UPLOAD_FILE_ERROR);
				schedulePlanHandlerBiz.updateScheduleStatusToFailure(UPLOAD_FILE_ERROR);
				return;
			}
			senderServiceProxy.notifyServer();
		} catch (Exception e) {
			logger.error(e.getClass().getName() + " --getHolder().getStpClient().upload with size = " + sendFiles.size() + e.getMessage());
			schedulePlanHandlerBiz.updateScheduleStatusToFailure(e.getMessage());
		}
		schedulePlanHandlerBiz.updateScheduleStatusToSuccess();
	}

	private List<File> writeFileRecursion(List<ScheduleNode> todayNeedUploadSchedules) throws Exception {
		int size = todayNeedUploadSchedules.size();
		List<File> files = Lists.newArrayList();

		if (size > MAX_ITEM_IN_ONE_FILE) {
			List<ScheduleNode> scheduleNodes = todayNeedUploadSchedules.subList(0, MAX_ITEM_IN_ONE_FILE);
			files.add(ScheduleFileUtil.writeFile(scheduleNodes));
			files.addAll(writeFileRecursion(todayNeedUploadSchedules.subList(MAX_ITEM_IN_ONE_FILE, size)));
		} else {
			files.add(ScheduleFileUtil.writeFile(todayNeedUploadSchedules));
		}

		logger.info(" write files size = " + files.size());
		return files;
	}

}
