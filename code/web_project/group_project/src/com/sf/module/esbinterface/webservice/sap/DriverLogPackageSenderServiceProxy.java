package com.sf.module.esbinterface.webservice.sap;

import static com.sf.module.esbinterface.util.Constants.SYSTEM_ID;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.xml.ws.Holder;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.sf.module.esbinterface.util.Constants;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.*;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY;
import com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD;

@Component
public class DriverLogPackageSenderServiceProxy {
	public static final String TO_SYSTEM = "SAP-ECC";
	public static final String SAP_DIR_PREFIX = "/";
	private static final Logger log = LoggerFactory.getLogger("PushDriverLog");
	private String serviceUrl;

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void notifyServer(String uploadPath, String zipFileName, String dataType) throws Exception {
		SHEAD shead = createRequestHead();
		SBODY sbody = createRequestBodyType(uploadPath, zipFileName, dataType);
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

	private SBODY createRequestBodyType(String uploadPath, String zipFileName, String dataType) {
		SBODY sbody = new SBODY();
		sbody.setFilePath(SAP_DIR_PREFIX + uploadPath + SAP_DIR_PREFIX);
		sbody.setFileName(zipFileName);
		sbody.setIsZip(Constants.IS_ZIP);
		sbody.setTimestamp(String.valueOf(new Date().getTime()));
		sbody.setDataType(dataType);
		return sbody;
	}

	private EsbSoapHeaderType createRequestSoapHeadType() {
		EsbSoapHeaderType esbSoapHeader = new EsbSoapHeaderType();
		ObjectFactory objectFactory = new ObjectFactory();
		esbSoapHeader.setTo(objectFactory.createEsbSoapHeaderTypeTo(TO_SYSTEM));
		esbSoapHeader.setFrom(objectFactory.createEsbSoapHeaderTypeFrom(SYSTEM_ID));
		return esbSoapHeader;
	}

	private void doRequest(
	        SHEAD shead,
	        SBODY sbody,
	        EsbSoapHeaderType esbSoapHeader,
	        Holder<FileDeliveryServiceResponseBodyType.SHEAD> shead1,
	        Holder<FileDeliveryServiceResponseBodyType.SBODY> sbody1,
	        Holder<EsbSoapHeaderType> esbSoapHeader1) throws Exception {

		String failDesc = "";
		for (int i = 1; i <= Constants.MAX_UPLOAD_NOTIFY_RETRY_TIME; i++) {
			try {

				getService().fileDeliveryService(shead, sbody, esbSoapHeader, shead1, sbody1, esbSoapHeader1);

				if ("SUCCESSFUL".equalsIgnoreCase(sbody1.value.getResult())) {
					log.info("send message to ESB sucess!---");
					return;
				}

				if (i == Constants.MAX_UPLOAD_NOTIFY_RETRY_TIME) { // 达到最大重试次数，抛出异常，退出循环
					log.info("send message to esb fail!:" + failDesc);
					throw new Exception("send message to esb fail!:" + failDesc);
				}

				failDesc = sbody1.value.getResult();
				TimeUnit.SECONDS.sleep(Constants.MAX_WAIT_SECOND_TIME);

			} catch (Exception e) {
				log.error("send message to  ESB exception at the " + i + "st time:" + e.getMessage());
				failDesc = e.getMessage();
				TimeUnit.SECONDS.sleep(Constants.MAX_WAIT_SECOND_TIME);
			}
		}
	}

	private ESBServerPortType getService() {
		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
		jaxWsProxyFactoryBean.setServiceClass(ESBServerPortType.class);
		jaxWsProxyFactoryBean.setAddress(serviceUrl);
		return (ESBServerPortType) jaxWsProxyFactoryBean.create();
	}
}
