package com.sf.module.esbinterface.biz;

import com.sf.module.esbinterface.domain.BigFileDataHandlerParameter;
import com.sf.module.esbinterface.domain.HrEmpInfo;
import com.sf.module.esbinterface.util.BeanUtil;
import com.sf.module.esbinterface.util.HREmpXMLReader;

import java.io.File;
import java.util.List;

public class HrsEmpDataHandler {

    public void handlerEmpData(final BigFileDataHandlerParameter handlerParameter) {

        if (!handlerParameter.isSuccess()) {
            //TODO save log
            return;
        }

        if (!handlerParameter.getFileList().isEmpty()) {
            for (File file : handlerParameter.getFileList()) {
                try {
                    new HREmpXMLReader().readEmployeeFromFile(file, new HREmpXMLReader.OnSaveHandler() {
                        @Override
                        public void onSave(List<HrEmpInfo> hrEmpInfos) {
                            String beanId = String.format("%s_%s", handlerParameter.getSystemId(),
                                    handlerParameter.getDataType());
                            IBigFileDataHandlerBiz dataHandlerBiz = BeanUtil.getHandlerBean(beanId);
                            dataHandlerBiz.saveBatch(hrEmpInfos);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //save procedure
        }
    }

}
