package com.taotao.manage.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;



/**
 * 商品参数服务
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月8日 下午4:58:13
 * we can not wait,we can not stop!
 */
@Service
public class ItemParamItemService extends BaseService<ItemParamItem>{
    
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;
    /**
     * 根据条件更新(自定义更新)
     * 使用通用mapper 如何实现自定义 按条件更新的方法如下
     * @return
     */
    public Integer updateItemParamItem(Long itemId,String paramData){
        //创建更新对象 只更新不为空的字段
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setParamData(paramData);
        itemParamItem.setUpdated(new Date());
        
        //创建示例
        Example example=new Example(ItemParamItem.class);
        //根据itemId条件更新
        example.createCriteria().andEqualTo("itemId", itemId);
        //执行更新操作
        return this.itemParamItemMapper.updateByExampleSelective(itemParamItem, example);
    }
}


