package com.sf.module.driver.domain;

import com.sf.framework.base.domain.BaseEntity;

public class DriverLineConfigureRelation extends BaseEntity{
    private Long configureId;
    private Long lineId;
    private Long order;

    public long getConfigureId() {
        return configureId;
    }

    public void setConfigureId(long configureId) {
        this.configureId = configureId;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }
}
