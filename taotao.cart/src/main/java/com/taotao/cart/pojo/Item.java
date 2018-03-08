package com.taotao.cart.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品表
 * 
 * @author srhsw95_Administrator
 * @email srhsw95@163.com
 * @version 2017年2月7日 下午9:59:12 we can not wait,we can not stop!
 */
public class Item {

    /**
     * 商品ID
     */
    @Id
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 卖点
     */
    private String sellPoint;

    /**
     * 价格
     */
    private Long price;

    /**
     * 库存
     */
    private Integer num;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 所属类目
     */
    private Long cid;

    /**
     * 商品状态 1、正常 2、下架 3、删除
     */
    private Integer status;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", sellPoint=" + sellPoint + ", price=" + price
                + ", num=" + num + ", barcode=" + barcode + ", image=" + image + ", cid=" + cid + ", status="
                + status + "]";
    }

}
