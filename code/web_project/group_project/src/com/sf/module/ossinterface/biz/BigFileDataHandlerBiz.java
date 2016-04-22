package com.sf.module.ossinterface.biz;

import javax.annotation.Resource;

import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.framework.server.core.integration.SpringBeanLoader;
import com.sf.module.ossinterface.dao.IDataControlJdbcDao;
import com.sf.module.ossinterface.domain.EsbBigFileResend;
import com.sf.module.ossinterface.util.BigFileDataHandlerParament;

/**
 * ESB大文件处理业务接口
 *
 * @author 文俊 (337291) Jun 17, 2014
 */
public class BigFileDataHandlerBiz extends BaseBiz implements
			IBigFileServiceHandlerBiz {

	private IDataControlJdbcDao dataControlJdbcDao;

	public static IBigFileServiceHandlerBiz getInstance() {
		return (IBigFileServiceHandlerBiz)getBean("bigFileDataHandlerBiz");
	}

	private static IBigFileDataHandlerBiz getHandlerBean(String beanId) {
		return (IBigFileDataHandlerBiz) getBean(beanId);
	}

	private static Object getBean(String beanId) {
		return SpringBeanLoader.getBean(beanId);
	}

	public void setDataControlJdbcDao(IDataControlJdbcDao dataControlJdbcDao) {
		this.dataControlJdbcDao = dataControlJdbcDao;
	}

	@Resource
	private IEsbBigFileResendBiz esbBigFileResendBiz;
    public void setEsbBigFileResendBiz(IEsbBigFileResendBiz esbBigFileResendBiz) {
		this.esbBigFileResendBiz = esbBigFileResendBiz;
	}

	/**
     * <pre>
     * 保存 处理数据任务日志
     * state [设置-9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发]
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 21, 2014
     * @param handlerParament
     * @param state
     * @return
     */
	public Long saveLog(BigFileDataHandlerParament handlerParament, Integer state) {
		EsbBigFileResend entity = new EsbBigFileResend();
		if (handlerParament != null) {
			entity.setDataType(handlerParament.getDataType());
			entity.setIsZip(handlerParament.getIsZip());
			entity.setJournalId(handlerParament.getJournalId());
			entity.setMd5Code(handlerParament.getMd5Code());
			entity.setRemoteFileName(handlerParament.getRemoteFileName());
			entity.setRemotePath(handlerParament.getRemotePath());
			entity.setSelfSystemId(handlerParament.getSelfSystemId());
			entity.setSystemId(handlerParament.getSystemId());
			entity.setTimeStamp(handlerParament.getTimeStamp());
			entity.setState(state == null ? EsbBigFileResend.START_DOWN : state);
		} else {
			entity.setState(Integer.valueOf(-9));
		}

		return this.esbBigFileResendBiz.save(entity );
	}

	/**
	 * <pre>
	 * 根据 systemId 和 dataType 处理数据
	 * </pre>
	 *
	 * @author 文俊 (337291)
	 * @date Jun 21, 2014
	 * @param handlerParament
	 */
	public boolean handler(BigFileDataHandlerParament handlerParament) {
		System.out.println(handlerParament);
		super.log.info("Executing operation handler: ", handlerParament);

		Long logId = null;
		IBigFileDataHandlerBiz bean = null;
		String journalId = null;
//		List<File> listFile = null;
		try {
			logId = handlerParament.getLogId();
			journalId = handlerParament.getJournalId();
			/**
			 * 更新日志,开始解析
			 */
			this.esbBigFileResendBiz.update(logId, EsbBigFileResend.START_PARSER);

			if (handlerParament.getIsSuccess()) {
//				listFile = handlerParament.getFileList();
				String beanId = String.format("%s_%s", handlerParament.getSystemId(),
						handlerParament.getDataType());
				bean = getHandlerBean(beanId);
				if (bean == null) {
					super.log.error(String.format("getHandlerBean(beanId:%s) Failure "), beanId);
					this.esbBigFileResendBiz.update(logId, EsbBigFileResend.PARSER_FAILURE);
				} else {
					boolean handler = bean.handler(handlerParament);
					if (handler) {
						dataControlJdbcDao.calculteOssEmp(handlerParament.getDataType(), handlerParament.getJournalId());
					}
					this.esbBigFileResendBiz.update(logId, handler ? EsbBigFileResend.SUCCESS : EsbBigFileResend.PARSER_FAILURE);
				}
			} else {
				/**
				 * 下载失败
				 */
				logId = saveLog(handlerParament, EsbBigFileResend.DOWNlOAD_FAILURE);
			}
			super.log.info("Complete operation handler");
		} catch (Exception e) {
			e.printStackTrace();
			if (logId == null) {
				/**
				 * 解析失败
				 */
				saveLog(handlerParament, EsbBigFileResend.PARSER_FAILURE);
			} else {
				/**
				 * 更新日志,解析失败
				 */
				this.esbBigFileResendBiz.update(logId, EsbBigFileResend.PARSER_FAILURE);
			}
			if (bean != null) {
				bean.tranData(journalId);
			}

			super.log.error(String.format("bean.handler(handlerParament:%s) Failure", handlerParament), e);
			throw new RuntimeException(e);
		}
		// TODO w.j 定时删除文件
//		finally {
//			if (listFile != null && !listFile.isEmpty()) {
//				deleteFile(listFile.toArray(new File[listFile.size()]));
//			}
//		}

		return true;
	}

	public void tranData(String journalId) {
		System.out.println(journalId);
	}

//	private void deleteFile(File[] files) {  
//	    try {
//            for (File file : files) {
//                if (file == null || !file.exists()) {
//                    continue;
//                }
//                if (file.isDirectory()) {
//                    File[] subFiles = file.listFiles();
//                    if (subFiles != null && subFiles.length > 0) {
//                        deleteFile(file.listFiles());
//                    }
//                }
//                
//                log.info("[ClearEsbDownloadTempdir] Removing file Temp " + file.getPath());
//                System.out.println(file.delete());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("ClearEsbDownloadTempdir Exception: ", e);
//        }
//	} 

}
