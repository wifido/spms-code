package com.sf.module.esbinterface.webservice.sap;

import com.sf.module.esbinterface.fileUpload.SchedulePlanUploader;
import com.sf.module.esbinterface.util.Constants;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.ESBServerPortType;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.EsbSoapHeaderType;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.ObjectFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.sf.module.esbinterface.util.Constants.SYSTEM_ID;
import static com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY;
import static com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD;

public class PackageSenderServiceProxy {
    public static final String TO_SYSTEM = "SAP-ECC";
    public static final String SAP_DIR_PREFIX = "/";
    private static final Logger LOG = LoggerFactory.getLogger(PackageSenderServiceProxy.class.getName());
    private String serviceUrl;

    public static void main(String[] args) throws Exception {
        new PackageSenderServiceProxy().notifyServer();
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void notifyServer() throws Exception {
        SHEAD shead = createRequestHead();
        SBODY sbody = createRequestBodyType();
        EsbSoapHeaderType esbSoapHeader = createRequestSoapHeadType();

        Holder<FileDeliveryServiceResponseBodyType.SHEAD> responseShead = new Holder<FileDeliveryServiceResponseBodyType.SHEAD>();
        Holder<FileDeliveryServiceResponseBodyType.SBODY> responseSbody = new Holder<FileDeliveryServiceResponseBodyType.SBODY>();
        Holder<EsbSoapHeaderType> esbSoapHeader1 = new Holder<EsbSoapHeaderType>();

        doRequest(shead, sbody, esbSoapHeader, responseShead, responseSbody, esbSoapHeader1);
    }

    private SHEAD createRequestHead() {
        SHEAD shead = new SHEAD();
        shead.setSYSTEMID(SYSTEM_ID);
        return shead;
    }

    private SBODY createRequestBodyType() {
        SBODY sbody = new SBODY();
        sbody.setFilePath(SAP_DIR_PREFIX + Constants.SF_UPLOAD_PATH + SAP_DIR_PREFIX);
        sbody.setFileName(SchedulePlanUploader.uploadZipFileName);
//        sbody.setMd5(Constants.IS_MD5);
        sbody.setIsZip(Constants.IS_ZIP);
        sbody.setTimestamp(String.valueOf(new Date().getTime()));
        sbody.setDataType(Constants.DATA_TYPE);
        return sbody;
    }

    private EsbSoapHeaderType createRequestSoapHeadType() {
        EsbSoapHeaderType esbSoapHeader = new EsbSoapHeaderType();
        ObjectFactory objectFactory = new ObjectFactory();
        esbSoapHeader.setTo(objectFactory.createEsbSoapHeaderTypeTo(TO_SYSTEM));
        esbSoapHeader.setFrom(objectFactory.createEsbSoapHeaderTypeFrom(SYSTEM_ID));
        return esbSoapHeader;
    }

    private void doRequest(SHEAD shead, SBODY sbody, EsbSoapHeaderType esbSoapHeader, Holder<FileDeliveryServiceResponseBodyType.SHEAD> shead1, Holder<FileDeliveryServiceResponseBodyType.SBODY> sbody1, Holder<EsbSoapHeaderType> esbSoapHeader1) throws Exception {
        for (int i = 1; i <= Constants.MAX_UPLOAD_NOTIFY_RETRY_TIME; i++) {
            try {
                getService().fileDeliveryService(shead, sbody, esbSoapHeader, shead1, sbody1, esbSoapHeader1);

                if (dealResult(sbody1, i)) continue; //继续循环发送

                break;//成功的话就跳出循环
            } catch (Exception e) {
                LOG.error("packageAndSend() SPMS send to ESB exception at the " + i + "st time:" + e.getMessage());

                if (i == Constants.MAX_UPLOAD_NOTIFY_RETRY_TIME) { //重试最大次还是失败，则抛出异常，跳出循环
                    throw new Exception("Send message to ESB exception dataType:");
                }
                TimeUnit.SECONDS.sleep(Constants.MAX_WAIT_SECOND_TIME);
                continue; //继续循环发送
            }
        }
    }

    private boolean dealResult(Holder<FileDeliveryServiceResponseBodyType.SBODY> sbody1, int i) throws Exception {
        if (!"SUCCESSFUL".equalsIgnoreCase(sbody1.value.getResult())) { //ESB处理失败
            LOG.error("packageAndSend() ESB process  FAILED at the " + i + "st time");

            if (i == Constants.MAX_UPLOAD_NOTIFY_RETRY_TIME) { //达到最大重试次数，抛出异常，退出循环
                throw new Exception("ESB process FAILED");
            }
            TimeUnit.SECONDS.sleep(Constants.MAX_WAIT_SECOND_TIME);
            return true;
        }
        LOG.info("packageAndSend() ESB process SUCCESS at the " + i + "st time");
        return false;
    }

    private ESBServerPortType getService() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(ESBServerPortType.class);
        jaxWsProxyFactoryBean.setAddress(serviceUrl);
        return (ESBServerPortType) jaxWsProxyFactoryBean.create();
    }
}
