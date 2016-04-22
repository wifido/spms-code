package com.sf.module.esbinterface.fileutil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.framework.util.xml.EasyXMLStreamWriter;
import com.sf.framework.util.xml.WstxUtil;
import com.sf.framework.util.xml.XMLUtil;

public class ScheduleFileUtil {
    public static final Logger logger = LoggerFactory.getLogger(ScheduleFileUtil.class);
    public static final String UPLOAD_FILE_LOCAL_SAVE_PATH = getEsbFilePath();

    public static String getEsbFilePath() {
//        return ApplicationContext.getContext().getSystemConfig().getProperty("esbFilePath");
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/sendFile";
    }

    private static final String bakPostfix = ".bak";
    private static final String destPostfix = ".xml";
    private static final String DEFAULT_ENCODE = "UTF-8";

    public static File writeFile(List<ScheduleNode> scheduleNodes) throws Exception {
        return writeFile(scheduleNodes, WstxUtil.getInstance(), false);
    }

    private static File writeFile(List<ScheduleNode> scheduleNodes
            , XMLUtil xmlUtil, boolean isShow) throws Exception {
        String fileName = UUID.randomUUID().toString();
        File dirPath = new File(UPLOAD_FILE_LOCAL_SAVE_PATH);
        logger.info("  writeFile  dir path  = " + dirPath);
        if (!dirPath.exists()) dirPath.mkdir();

        File bakFile = new File(dirPath, new StringBuffer(fileName).append(bakPostfix).toString());
        File destFile = new File(dirPath, new StringBuffer(fileName).append(destPostfix).toString());
        FileOutputStream fos = new FileOutputStream(bakFile);

        writeXml(scheduleNodes, xmlUtil, fos, isShow);

        FileUtil.fileChannelCopy(bakFile, destFile);

        logger.info("destFile.exists() = " + destFile.exists());

        return destFile;
    }

    private static void writeXml(List<ScheduleNode> scheduleNodes, XMLUtil xmlUtil, FileOutputStream fos, boolean isShow) throws XMLStreamException {
        EasyXMLStreamWriter streamWriter = xmlUtil.getEasyXMLStreamWriter(fos, DEFAULT_ENCODE);
        streamWriter.setShowEmptyAttrFlg(isShow);

        streamWriter.writeStartElement("ZSHR_WORK_TIME_FILE", null);
        streamWriter.writeEmptyTxtElement("COMMENT", null);
        streamWriter.writeStartElement("DOCUMENTS", null);

        writeItems(scheduleNodes, streamWriter);

        streamWriter.writeEndElement();//Document
        streamWriter.writeEndElement();//ZSTHR_WORK_TIME_FILES

        streamWriter.close();
    }

    private static void writeItems(List<ScheduleNode> scheduleNodes, EasyXMLStreamWriter streamWriter) throws XMLStreamException {
        for (int i = 0; i < scheduleNodes.size(); i++) {
            ScheduleNode scheduleNode = scheduleNodes.get(i);
            streamWriter.writeStartElement("item", null);
            streamWriter.writeContentElement("ZHRXH", null, scheduleNode.getZhrxh()); //序号
            streamWriter.writeContentElement("PERNR", null, scheduleNode.getPernr());//员工编号
            streamWriter.writeContentElement("BEGDA", null, scheduleNode.getBegda());//班次开始日期
            streamWriter.writeContentElement("ENDDA", null, scheduleNode.getEndda());//班次结束日期
            streamWriter.writeContentElement("BEGUZ", null, scheduleNode.getBeguz());//班次开始时间
            streamWriter.writeContentElement("ENDUZ", null, scheduleNode.getEnduz());//班次结束时间
            streamWriter.writeContentElement("VTKEN", null, scheduleNode.getVtken());//前一天标识
            streamWriter.writeContentElement("TPROG", null, scheduleNode.getTprog());//    休息标识	TPROG
            streamWriter.writeContentElement("ZHRPBXT", null, scheduleNode.getZhrpbxt());//     排班系统(数据来源)
            streamWriter.writeContentElement("ZHRCLBZ", null, scheduleNode.getZhrclbz());//前一天标识
            streamWriter.writeEndElement();
        }
    }

    public static void main(String[] args) throws Exception {
        List<ScheduleNode> scheduleNodes = Arrays.asList(new ScheduleNode(1L, "2", "3", "4", "5", "6", "7", "8", "9"));
        writeFile(scheduleNodes);
    }
}
