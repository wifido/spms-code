
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.sf.module.esbinterface.webservice.stub.fileDeliveryServices;

import javax.jws.WebService;
import java.util.logging.Logger;

/**
 * This class was generated by Apache CXF 2.2.10
 * Tue Aug 26 17:14:15 CST 2014
 * Generated source version: 2.2.10
 */

@WebService(
        serviceName = "FileDeliveryServices",
        portName = "ESBServerSoapEndpoint",
        targetNamespace = "http://www.sf-express.com/esb/service/FileDeliveryServices/wsdl",
        wsdlLocation = "file:/E:/temp/esbwsdl/FileDeliveryServices/FileDeliveryServices.wsdl",
        endpointInterface = "com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.ESBServerPortType")

public class ESBServerPortTypeImpl implements ESBServerPortType {

    private static final Logger LOG = Logger.getLogger(ESBServerPortTypeImpl.class.getName());

    /* (non-Javadoc)
     * @see com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.ESBServerPortType#fileDeliveryService(com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD  shead ,)com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY  sbody ,)com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.EsbSoapHeaderType  esbSoapHeader ,)com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SHEAD  shead1 ,)com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SBODY  sbody1 ,)com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.EsbSoapHeaderType  esbSoapHeader1 )*
     */
    public void fileDeliveryService(com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SHEAD shead, com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceBodyType.SBODY sbody, EsbSoapHeaderType esbSoapHeader, javax.xml.ws.Holder<com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SHEAD> shead1, javax.xml.ws.Holder<com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SBODY> sbody1, javax.xml.ws.Holder<EsbSoapHeaderType> esbSoapHeader1) {
        LOG.info("Executing operation fileDeliveryService");
        System.out.println(shead);
        System.out.println(sbody);
        System.out.println(esbSoapHeader);
        try {
            com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SHEAD shead1Value = null;
            shead1.value = shead1Value;
            com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.FileDeliveryServiceResponseBodyType.SBODY sbody1Value = null;
            sbody1.value = sbody1Value;
            com.sf.module.esbinterface.webservice.stub.fileDeliveryServices.EsbSoapHeaderType esbSoapHeader1Value = null;
            esbSoapHeader1.value = esbSoapHeader1Value;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
