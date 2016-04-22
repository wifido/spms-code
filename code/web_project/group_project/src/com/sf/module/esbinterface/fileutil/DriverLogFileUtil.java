package com.sf.module.esbinterface.fileutil;

import static com.google.common.collect.Maps.newHashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sf.framework.util.xml.EasyXMLStreamWriter;
import com.sf.framework.util.xml.WstxUtil;
import com.sf.module.esbinterface.domain.DriverLog;

public class DriverLogFileUtil {
	public static final Logger logger = LoggerFactory.getLogger("PushDriverLog");
	public static final String UPLOAD_FILE_LOCAL_SAVE_PATH = getSavePath();

	public static String getSavePath() {
		return Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/driverLogSendFile";
	}

	private static final String bakPostfix = ".bak";
	private static final String destPostfix = ".xml";
	private static final String DEFAULT_ENCODE = "utf-8";

	public static File writeFile(List<DriverLog> logList, String batch_number, int totalPageSize, int pageIndex) throws Exception {
		WstxUtil xmlUtil = WstxUtil.getInstance();
		String fileName = UUID.randomUUID().toString();
		File dirPath = new File(UPLOAD_FILE_LOCAL_SAVE_PATH);
		logger.info(" write driver log File  dir path  = " + dirPath);
		if (!dirPath.exists())
			dirPath.mkdir();

		File bakFile = new File(dirPath, new StringBuffer(fileName).append(bakPostfix).toString());
		File destFile = new File(dirPath, new StringBuffer(fileName).append(destPostfix).toString());
		FileOutputStream fos = new FileOutputStream(bakFile);

		EasyXMLStreamWriter streamWriter = xmlUtil.getEasyXMLStreamWriter(fos, DEFAULT_ENCODE);
		streamWriter.setShowEmptyAttrFlg(false);

		Map<String, String> newHashMap = newHashMap();
		newHashMap.put("batch_number", String.valueOf(batch_number));
		newHashMap.put("file_number", String.valueOf(pageIndex));
		newHashMap.put("size", String.valueOf(totalPageSize));

		streamWriter.writeStartElement("ZSHR_IF_PT0038", newHashMap);
		streamWriter.writeStartElement("PT0038", null);
		for (DriverLog log : logList) {
			log.bulidXmlFileElement(streamWriter);
		}
		streamWriter.writeEndElement();
		streamWriter.writeEndElement();

		streamWriter.close();
		FileUtil.fileChannelCopy(bakFile, destFile);
		logger.info("destFile.exists() = " + destFile.exists());
		logger.info("write data to xml file success!------ pageIndex: " + pageIndex);
		return destFile;
	}

	public static void main(String[] args) {

	}

}
