package com.taotao.manage.pojo;

import java.util.Date;

public abstract class BasePojo {
    /**
     * 创建时间
     */
    private Date created;
    
    /**
     * 更改时间
     */
    private Date updated;
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getUpdated() {
        return updated;
    }
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
