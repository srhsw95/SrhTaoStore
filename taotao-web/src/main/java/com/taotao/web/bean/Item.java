package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;



/**
 * 商品信息
 * @author  Srhsw95
 * @version 2017年2月11日 下午7:10:56
 */
public class Item extends com.taotao.manage.pojo.Item{
    
    /**
     * 返回图片数组
     * @return
     */
    public String[] getImages(){
        return StringUtils.split(super.getImage(), ",");
    }
}


