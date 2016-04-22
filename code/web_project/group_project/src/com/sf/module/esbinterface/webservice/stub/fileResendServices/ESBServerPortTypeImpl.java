
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.sf.module.esbinterface.webservice.stub.fileResendServices;

import com.sf.module.esbinterface.fileUpload.SchedulePlanUploader;

import java.util.logging.Logger;

/**
 * This class was generated by Apache CXF 2.2.10
 * Tue Aug 26 14:45:31 CST 2014
 * Generated source version: 2.2.10
 */

@javax.jws.WebService(
        serviceName = "FileResendServices",
        portName = "ESBServerSoapEndpoint",
        targetNamespace = "http://www.sf-express.com/esb/service/FileResendServices/wsdl",
        endpointInterface = "com.sf.module.esbinterface.webservice.stub.fileResendServices.ESBServerPortType")

public class ESBServerPortTypeImpl implements ESBServerPortType {

    private static final Logger LOG = Logger.getLogger(ESBServerPortTypeImpl.class.getName());
    private SchedulePlanUploader schedulePlanUploader;

    public void setSchedulePlanUploader(SchedulePlanUploader schedulePlanUploader) {
        this.schedulePlanUploader = schedulePlanUploader;
    }

    /* (non-Javadoc)
         * @see com.sf.module.esbinterface.webservice.stub.fileResendServices.ESBServerPortType#fileResendService(com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceBodyType.SHEAD  shead ,)com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceBodyType.SBODY  sbody ,)com.sf.module.esbinterface.webservice.stub.fileResendServices.EsbSoapHeaderType  esbSoapHeader ,)com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SHEAD  shead1 ,)com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SBODY  sbody1 ,)com.sf.module.esbinterface.webservice.stub.fileResendServices.EsbSoapHeaderType  esbSoapHeader1 )*
         */
    public void fileResendService(com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceBodyType.SHEAD shead, com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceBodyType.SBODY sbody, EsbSoapHeaderType esbSoapHeader, javax.xml.ws.Holder<com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SHEAD> shead1, javax.xml.ws.Holder<com.sf.module.esbinterface.webservice.stub.fileResendServices.FileResendServiceResponseBodyType.SBODY> sbody1, javax.xml.ws.Holder<EsbSoapHeaderType> esbSoapHeader1) {
        LOG.info("Executing operation fileResendService");
        try {
            schedulePlanUploader.resendFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOG.info("Executing FileDeliveryServiceBiz.callMethod............");
    }

}
