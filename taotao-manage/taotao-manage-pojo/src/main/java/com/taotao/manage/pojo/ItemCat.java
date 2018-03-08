package com.taotao.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品类目类，用于显示 处理商品类目
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月7日 下午9:58:42
 * we can not wait,we can not stop!
 */
@Table(name = "tb_item_cat")
public class ItemCat extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private Boolean isParent;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
    
    /**
     * 为了easyUi的tree组件显示 添加text属性
     * @return
     */
    public String getText() {
        return this.getName();
    }
    
    /**
     * tree组件用于显示是文件夹还是文件  closed 文件夹  open 文件
     * @return
     */
    public String getState(){
        return this.getIsParent()?"closed":"open";
    }
}
