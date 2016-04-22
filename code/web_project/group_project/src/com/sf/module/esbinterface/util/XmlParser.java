package com.sf.module.esbinterface.util;

import com.google.common.collect.Maps;
import com.sf.framework.util.xml.WstxUtil;
import com.sf.module.esbinterface.domain.ScheduleWithErrorInfo;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class XmlParser {
    private OnSaveHandler<ScheduleWithErrorInfo> onSaveHandler;

    public XmlParser(OnSaveHandler onSaveHandler) {
        this.onSaveHandler = onSaveHandler;
    }

    public List<ScheduleWithErrorInfo> parserScheduleError(File file) {
        WstxUtil xmlUtil = WstxUtil.getInstance();
        List<ScheduleWithErrorInfo> scheduleWithErrorInfoList = newArrayList();

        try {
            XMLStreamReader xmlStreamReader = xmlUtil.getXMLStreamReader(new FileInputStream(file), "UTF-8");

            String itemText = "";
            ScheduleWithErrorInfo scheduleWithErrorInfo = null;
            Map<String, String> headerMap = Maps.newHashMap();

            while (xmlStreamReader.hasNext()) {
                switch (xmlStreamReader.next()) {
                    case XMLEvent.START_ELEMENT:
                        String nodeName = xmlStreamReader.getName().getLocalPart();
                        if (nodeName.equalsIgnoreCase("documents")) {
                            int attributeCount = xmlStreamReader.getAttributeCount();
                            for (int idx = 0; idx < attributeCount; idx++) {
                                headerMap.put(xmlStreamReader.getAttributeLocalName(idx),
                                        xmlStreamReader.getAttributeValue(idx));
                            }
                        } else if (nodeName.equalsIgnoreCase("item")) {
                            scheduleWithErrorInfo = new ScheduleWithErrorInfo();
                        }

                        break;
                    case XMLEvent.CHARACTERS:
                        itemText = xmlStreamReader.getText();
                        break;
                    case XMLEvent.END_ELEMENT:
                        String endNodeName = xmlStreamReader.getName().getLocalPart();
                        if (endNodeName.equals("item")) {
                            scheduleWithErrorInfoList.add(scheduleWithErrorInfo);
                            if (scheduleWithErrorInfoList.size() > 10000) {
                                //save batch
//                            OnSaveHandler
                                onSaveHandler.save(scheduleWithErrorInfoList);
                                scheduleWithErrorInfoList.clear();
                            }
                        }
                        composeProperty(scheduleWithErrorInfo, itemText, endNodeName);
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scheduleWithErrorInfoList;
    }

    /**
     * " <ZHRXH>1</ZHRXH>\n" +
     * " <PERNR>460873</PERNR>\n" +
     * " <BEGDA>20141002</BEGDA>\n" +
     * " <ENDDA>20141002</ENDDA>\n" +
     * " <BEGUZ>060000</BEGUZ>\n" +
     * " <ENDUZ>080000</ENDUZ>\n" +
     * " <VTKEN/>\n" +
     * " <TPROG/>\n" +
     * " <ZHRPBXT>2</ZHRPBXT>\n" +
     * " <ZHRCLBZ/>\n" +
     * " <MESSAGE>ERROR</MESSAGE>" +
     *
     * @param scheduleWithErrorInfo
     * @param itemText
     * @param endNodeName
     */
    private void composeProperty(ScheduleWithErrorInfo scheduleWithErrorInfo, String itemText, String endNodeName) {
        if (endNodeName.equalsIgnoreCase("PERNR")) {
            scheduleWithErrorInfo.setEmpCode(itemText);
        } else if (endNodeName.equalsIgnoreCase("BEGDA")) {
            scheduleWithErrorInfo.setBeginDate(itemText);
        } else if (endNodeName.equalsIgnoreCase("BEGDA")) {
            scheduleWithErrorInfo.setEndDate(itemText);
        } else if (endNodeName.equalsIgnoreCase("BEGUZ")) {
            scheduleWithErrorInfo.setBeginTime(itemText);
        } else if (endNodeName.equalsIgnoreCase("ENDUZ")) {
            scheduleWithErrorInfo.setEndTime(itemText);
        } else if (endNodeName.equalsIgnoreCase("VTKEN")) {
            scheduleWithErrorInfo.setDayFlag(itemText);
        } else if (endNodeName.equalsIgnoreCase("TPROG")) {
            scheduleWithErrorInfo.setOffDutyFlag(itemText);
        } else if (endNodeName.equalsIgnoreCase("MESSAGE")) {
            scheduleWithErrorInfo.setError(itemText);
        }
    }

    public static enum XmlNodeType {
        START_ELEMENT(1),
        CHARACTERS(4),
        END_ELEMENT(2);

        final int value;

        XmlNodeType(int value) {
            this.value = value;
        }
    }

    public interface OnSaveHandler<T> {
        public void save(List<T> beans);
    }
}
