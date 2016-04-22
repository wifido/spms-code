/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     Sep 4, 2013           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.operation.domain;

import com.sf.framework.base.domain.BaseEntity;

/**
 * 
 * @author 文俊 (337291) Sep 4, 2013 
 */

public abstract class CSVDomain extends BaseEntity implements ICSVDomain {

    
    /**
     * @author 文俊 (337291)
     * @date Sep 4, 2013 
     * 
     */
    private static final long serialVersionUID = 9048363264730415557L;

    public StringBuilder appendStr(StringBuilder sb, Object obj) {
        if (obj != null) {
            String str = obj.toString();
            if (obj instanceof String) {
                sb.append("\"").append(str.replaceAll("\"", "\"\"")).append("\r\"");
            } else {
                sb.append(str);
            }
            
        }
        return sb;
    }
    
    public CSVDomain appendObj(StringBuilder sb, Object obj) {
        this.appendStr(sb, obj).append(",");
        return this;
    }
    
    public CSVDomain appendNumber(StringBuilder sb, Object obj) {
        if (obj != null) {
            sb.append(obj.toString());
        }
        sb.append(",");
        return this;
    }

}
