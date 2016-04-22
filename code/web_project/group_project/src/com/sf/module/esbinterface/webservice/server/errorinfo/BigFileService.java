package com.sf.module.esbinterface.webservice.server.errorinfo;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.6
 * 2014-10-31T16:40:22.633+08:00
 * Generated source version: 2.7.6
 * 
 */
@WebServiceClient(name = "BigFileService", 
                  wsdlLocation = "file:/D:/esb/BigFileService/BigFileService.wsdl",
                  targetNamespace = "http://www.sf-express.com/esb/service/BigFileService/wsdl") 
public class BigFileService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.sf-express.com/esb/service/BigFileService/wsdl", "BigFileService");
    public final static QName BigFileServiceEndpoint = new QName("http://www.sf-express.com/esb/service/BigFileService/wsdl", "BigFileServiceEndpoint");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/esb/BigFileService/BigFileService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(BigFileService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/D:/esb/BigFileService/BigFileService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public BigFileService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BigFileService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BigFileService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns BigFileServicePortType
     */
    @WebEndpoint(name = "BigFileServiceEndpoint")
    public BigFileServicePortType getBigFileServiceEndpoint() {
        return super.getPort(BigFileServiceEndpoint, BigFileServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BigFileServicePortType
     */
    @WebEndpoint(name = "BigFileServiceEndpoint")
    public BigFileServicePortType getBigFileServiceEndpoint(WebServiceFeature... features) {
        return super.getPort(BigFileServiceEndpoint, BigFileServicePortType.class, features);
    }

}
