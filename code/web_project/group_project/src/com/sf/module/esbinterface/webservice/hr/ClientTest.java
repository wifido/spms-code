package com.sf.module.esbinterface.webservice.hr;

import com.sf.module.esbinterface.webservice.stub.bigfileservice.*;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import javax.xml.ws.Holder;

public class ClientTest {

    public static void main(String[] args) throws Exception {
        BigFileServicePortType service = getService(BigFileServicePortType.class, null);
        SHEADType1 sheadType1 = new SHEADType1();
        SBODYType2 sbodyType2 = new SBODYType2();
        EsbSoapHeaderType esbSoapHeaderType = new EsbSoapHeaderType();
        Holder<SHEADType3> sheadType3Holder = new Holder<SHEADType3>();
        Holder<SBODYType5> sbodyType5Holder = new Holder<SBODYType5>();
        Holder<EsbSoapHeaderType> esbSoapHeaderTypeHolder = new Holder<EsbSoapHeaderType>();
        service.bigFileService(sheadType1, sbodyType2, esbSoapHeaderType, sheadType3Holder, sbodyType5Holder
                , esbSoapHeaderTypeHolder);
    }

    public static <T> T getService(Class<T> serviceClass, String uriKey
    ) throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        String wsdlString = "http://localhost:8080/ws/BigFileReceiveData?wsdl";
        factory.setAddress(wsdlString);
        return factory.create(serviceClass);
    }
}
