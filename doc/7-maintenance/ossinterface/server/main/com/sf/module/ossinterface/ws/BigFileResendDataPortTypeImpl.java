
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.sf.module.ossinterface.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sf.esb.client.bigfileresend.tcasinterface.BigFileResendDataPortType;
import com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType;
import com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5;
import com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2014-05-29T17:49:26.301+08:00
 * Generated source version: 2.5.0
 * 
 */
/*
@javax.jws.WebService(
                      serviceName = "BigFileResendData",
                      portName = "BigFileResendDataEndpoint",
                      targetNamespace = "http://www.sf-express.com/esb/service/BigFileResendData/wsdl",
                      endpointInterface = "com.sf.esb.client.bigfileresend.tcasinterface.BigFileResendDataPortType")
*/
public class BigFileResendDataPortTypeImpl implements BigFileResendDataPortType/*, WSCallback<BigFileResendDataPortType>*/ {

    //private static final Logger LOG = Logger.getLogger(BigFileResendDataPortTypeImpl.class.getName());
    private static final Logger LOG = LoggerFactory.getLogger(BigFileResendDataPortTypeImpl.class);
    
    /**
     * <pre>
     * 数据下载失败，请求重发
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 21, 2014 
     * @param sourceSystemId
     * @param dataType
     * @param timeStamp
     * @param selfSystemId
     * @param journalId
     * @return
     */
    public static String bigFileResendData(String sourceSystemId, String dataType, String timeStamp, String selfSystemId, String journalId) {
      //下载失败，请求数据重发
        com.sf.esb.client.bigfileresend.tcasinterface.SHEADType1 sheadResend = new com.sf.esb.client.bigfileresend.tcasinterface.SHEADType1();
        //调用方系统标识
        sheadResend.setSYSTEMID(selfSystemId);
        
        com.sf.esb.client.bigfileresend.tcasinterface.SBODYType2 sbodyResend = new com.sf.esb.client.bigfileresend.tcasinterface.SBODYType2();
        //数据源系统标识
        sbodyResend.setSource(sourceSystemId);
        //流水号
        sbodyResend.setJournalId(journalId);
        //需求数据类型
        sbodyResend.setDataType(dataType);
        //需求数据时间
        sbodyResend.setTimestamp(timeStamp);
        com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType esbSoapHeader1 = new com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType();
        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3> shead1 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3>();
        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5> sbody1 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5>();
        javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType> esbSoapHeader2 = new javax.xml.ws.Holder<com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType>();
        
        new BigFileResendDataPortTypeImpl().bigFileResendData(sheadResend, sbodyResend, esbSoapHeader1, shead1, sbody1, esbSoapHeader2);
        
        return sbody1.value == null ? null : sbody1.value.getResult(); 
    }

    /* (non-Javadoc)
     * @see com.sf.esb.client.bigfileresend.tcasinterface.BigFileResendDataPortType#bigFileResendData(com.sf.esb.client.bigfileresend.tcasinterface.SHEADType1  shead ,)com.sf.esb.client.bigfileresend.tcasinterface.SBODYType2  sbody ,)com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType  esbSoapHeader1 ,)com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3  shead1 ,)com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5  sbody1 ,)com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType  esbSoapHeader2 )*
     */
    public void bigFileResendData(com.sf.esb.client.bigfileresend.tcasinterface.SHEADType1 shead,com.sf.esb.client.bigfileresend.tcasinterface.SBODYType2 sbody,EsbSoapHeaderType esbSoapHeader1,javax.xml.ws.Holder<SHEADType3> shead1,javax.xml.ws.Holder<SBODYType5> sbody1,javax.xml.ws.Holder<EsbSoapHeaderType> esbSoapHeader2) { 
        LOG.info("Executing operation bigFileResendData");
        System.out.println(shead);
        System.out.println(sbody);
        System.out.println(esbSoapHeader1);
        try {
            com.sf.esb.client.bigfileresend.tcasinterface.SHEADType3 shead1Value = null;
            shead1.value = shead1Value;
            com.sf.esb.client.bigfileresend.tcasinterface.SBODYType5 sbody1Value = null;
            sbody1.value = sbody1Value;
            com.sf.esb.client.bigfileresend.tcasinterface.EsbSoapHeaderType esbSoapHeader2Value = null;
            esbSoapHeader2.value = esbSoapHeader2Value;
            
            //发送数据重发请求
//            WSUtil.call(BigFileResendDataPortType.class, this, null);
            
            BigFileResendDataPortType port = WSUtil.getServiceEsb(BigFileResendDataPortType.class, null);
            port.bigFileResendData(shead, sbody, esbSoapHeader1, shead1, sbody1, esbSoapHeader2);
            
            System.out.println(sbody1.value.getResult());
            LOG.info("Success operation bigFileResendData");
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            LOG.error("operation bigFileResendData Failure", ex);
            throw new RuntimeException(ex);
        }
    }

  
//    @Override
//    public <R> R doCallback(BigFileResendDataPortType service) throws Exception {
//        service.bigFileResendData(shead, sbody, esbSoapHeader1, shead1, sbody1, esbSoapHeader2);
//        return null;
//    } 
    

}