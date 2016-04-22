/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     May 27, 2013           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.common.domain;

/**
 * 
 * @author 文俊 (337291) May 27, 2013 
 */

public interface ICSVDomain {
    /**
     * 按照一定顺序以逗号连接的字符串
     * @author 文俊 (337291)
     * @date May 27, 2013 
     * @return
     */
    String toCSVString(Object[] args) ;
    
}
