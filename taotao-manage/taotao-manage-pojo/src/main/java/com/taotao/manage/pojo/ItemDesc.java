package com.taotao.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品描述表
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月7日 下午9:59:26
 * we can not wait,we can not stop!
 */
@Table(name = "tb_item_desc")
public class ItemDesc extends BasePojo{
    
    /**
     *对应tb_item中的id
     */
    @Id
    private Long itemId;
    
    /**
     * 商品描述
     */
    private String itemDesc;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
    
    

}
