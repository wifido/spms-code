package com.sf.module.esbinterface.webservice.sap;

import com.sf.esb.client.bigfileresend.tcasinterface.BigFileResendDataPortType;
import com.sf.esb.client.bigfileresend.tcasinterface.SBODYType2;
import com.sf.esb.client.bigfileresend.tcasinterface.SHEADType1;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class FileResendServiceProxy {
    public static void main(String[] args) throws Exception {
        BigFileResendDataPortType port = getService(BigFileResendDataPortType.class, null);

        SHEADType1 shead = new SHEADType1();
        SBODYType2 sbody = new SBODYType2();
        com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType esbSoapHeader1 = new com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType();
        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3> shead1 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3>();
        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5> sbody1 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5>();

        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType> esbSoapHeader2 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType>();

        port.bigFileResendData(shead, sbody, esbSoapHeader1, shead1, sbody1, esbSoapHeader2);
    }

    public static <T> T getService(Class<T> serviceClass, String uriKey
    ) throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        String wsdlString = "http://localhost:8080"
                + "/ws/BigFileResendData?wsdl";
        factory.setAddress(wsdlString);
        return factory.create(serviceClass);
    }


}
