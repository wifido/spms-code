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

import com.sf.module.ossinterface.util.BigFileDataHandlerParament;

/**
 * ESB大文件处理业务接口
 * @author 文俊 (337291) Jun 17, 2014 
 */

public interface IBigFileServiceHandlerBiz  extends IBigFileDataHandlerBiz {
	
    /**
     * <pre>
     * 保存 处理数据任务日志
     * state [设置-9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发]
     * </pre>
     * @author 文俊 (337291)
     * @date Jun 21, 2014 
     * @param handlerParament
     * @param state 
     * @return
     */
    public Long saveLog(BigFileDataHandlerParament handlerParament, Integer state);
    
}
