/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Jun 17, 2014           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.ossinterface.biz;

import com.sf.framework.server.base.biz.IBiz;
import com.sf.module.ossinterface.util.BigFileDataHandlerParament;

/**
 * ESB大文件处理业务接口
 * @author 文俊 (337291) Jun 17, 2014 
 */

public interface IBigFileDataHandlerBiz  extends IBiz {
    /**
     * <pre>
     * 根据 systemId 和 dataType 处理数据
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 21, 2014 
     * @param handlerParament
     * @return
     */
    public boolean handler(BigFileDataHandlerParament handlerParament) ;
    
    /**
     * <pre>
     * 根据流水号(journalId)处理失败,回滚
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 24, 2014 
     * @param journalId
     */
    public void tranData(String journalId) ;
    
}
