package com.sf.module.esbinterface.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.framework.util.xml.WstxUtil;
import com.sf.module.esbinterface.domain.HrEmpInfo;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class HREmpXMLReader {

    public List<HrEmpInfo> readEmployeeFromFile(File file, OnSaveHandler onSaveHandler) throws Exception {
        WstxUtil xmlUtil = WstxUtil.getInstance();
        XMLStreamReader xmlStreamReader = xmlUtil.getXMLStreamReader(new FileInputStream(file), "UTF-8");

        HrEmpInfo hrEmpInfo = null;
        String currentTxt = null;
        List<HrEmpInfo> hrEmpInfoList = Lists.newArrayList();
        Map<String, String> headerMap = Maps.newHashMap();
        while (xmlStreamReader.hasNext()) {
            switch (xmlStreamReader.next()) {
                case XMLEvent.START_ELEMENT:
                    String nodeName = xmlStreamReader.getName().getLocalPart();
                    if (nodeName.equalsIgnoreCase("emps")) {
                        int attributeCount = xmlStreamReader.getAttributeCount();
                        for (int idx = 0; idx < attributeCount; idx++) {
                            headerMap.put(xmlStreamReader.getAttributeLocalName(idx),
                                    xmlStreamReader.getAttributeValue(idx));
                        }
                    } else if (nodeName.equalsIgnoreCase("emp")) {
                        hrEmpInfo = new HrEmpInfo();
                        hrEmpInfo.setXmlSize(Long.parseLong(headerMap.get("size")));
                        hrEmpInfo.setBatchNumber(headerMap.get("batch_number"));
                        hrEmpInfo.setErrmsg(headerMap.get("errmsg"));
                    }

                    break;

                case XMLEvent.CHARACTERS:
                    currentTxt = xmlStreamReader.getText();
                    break;
                case XMLEvent.END_ELEMENT:
                    String endNodeName = xmlStreamReader.getName().getLocalPart();
                    if (endNodeName.equals("emp")) {
                        hrEmpInfoList.add(hrEmpInfo);
                        if (hrEmpInfoList.size() > 10000) {
                            //save batch
//                            OnSaveHandler
                            hrEmpInfoList.clear();
                        }
                    }
                    composeHrEmpProperty(hrEmpInfo, currentTxt, endNodeName);
                    break;
            }

        }
        return hrEmpInfoList;
    }

    private void composeHrEmpProperty(HrEmpInfo hrEmpInfo, String currentTxt, String endNodeName) throws Exception {
        if (endNodeName.equalsIgnoreCase("PERSON_TYPE")) {
            hrEmpInfo.setPersonType(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("EMP_NUM")) {
            hrEmpInfo.setEmpCode(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("CURR_ORG_NAME")) {
            hrEmpInfo.setEmpDesc(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("JOB_NAME")) {
            hrEmpInfo.setEmpDutyName(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("MAIL_ADDRESS")) {
            hrEmpInfo.setEmpEmail(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("SEX")) {
            hrEmpInfo.setEmpGender(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("MOBILE_PHONE")) {
            hrEmpInfo.setEmpMobile(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("LAST_NAME")) {
            hrEmpInfo.setEmpName(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("OFFICE_PHONE")) {
            hrEmpInfo.setEmpOfficephone(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("EMP_CATEGORY")) {
            hrEmpInfo.setEmpTypeName(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("EMP_ID")) {
            hrEmpInfo.setHrsEmpId(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("POSITION_NAME")) {
            hrEmpInfo.setPositionName(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("ATTRIBUTE10")) {
            hrEmpInfo.setDutySerial(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("SF_DATE")) {
            hrEmpInfo.setSfDate(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("CANCEL_FLAG")) {
            hrEmpInfo.setCancelFlag(currentTxt);
        } else if (endNodeName.equalsIgnoreCase("NET_CODE")) {
            hrEmpInfo.setDeptCode(currentTxt);
        }
    }

    public static interface OnSaveHandler {
        public void onSave(List<HrEmpInfo> hrEmpInfos);
    }
}
