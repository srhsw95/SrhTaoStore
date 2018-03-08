package com.taotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;



/**
 * @author  Srhsw95
 * @version 2017年2月7日 下午10:45:47
 */
@Service
public class ItemService extends BaseService<Item>{
    private static final ObjectMapper MAPPER=new ObjectMapper();
    //商品描述服务
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemParamItemService itemParamItemService;
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 新增商品
     * @param item
     * @param desc
     * @return
     */
    public Boolean saveItem(Item item,String desc,String itemParams){
        //保存商品
        item.setStatus(1);
        item.setId(null);//处于安全的考虑 强制设置ID为null,通过数据库自增获取
        Integer count1 = super.save(item);
        
        //保存商品描述
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = itemDescService.save(itemDesc);
        
        //保存商品规格
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = itemParamItemService.save(itemParamItem);
        
        sendMessage(item.getId(),"insert");
        return count1.intValue()==1&&count2.intValue()==1&&count3==1;
    }
    
    /**
     * 更新商品
     * @param item
     * @param desc
     * @return
     */
    public Boolean updateItem(Item item, String desc,String itemParams) {
        //更新商品
        Integer count1 = super.updateSelective(item);
        //更新商品描述
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = itemDescService.updateSelective(itemDesc);
        //更新商品参数信息
        Integer count3 = itemParamItemService.updateItemParamItem(item.getId(), itemParams);
        
        /*try {
            //通知其他系统，该商品已经更新
            apiService.doPost(TAOTAO_WEB_URL+"/item/cache/"+item.getId()+".html");
        } catch (Exception e) {
            // 异常捕获
            e.printStackTrace();
        }*/
        //发送消息至MQ交换机，通知其他系统
        sendMessage(item.getId(),"update");
        return count1.intValue()==1&&count2.intValue()==1 &&count3.intValue()==1;
    }
    
    /**
     * 消息发送
     * @param item
     */
    private void sendMessage(Long itemId,String type) {
        try {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("itemId", itemId);
            map.put("type", type);
            map.put("date", System.currentTimeMillis());
            String msg = MAPPER.writeValueAsString(map);
            this.rabbitTemplate.convertAndSend("item."+type, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteItemsByIds(List<Object> ids) {
        super.deleteByIds(ids, Item.class, "id");
        for (Object object : ids) {
            //发送删除消息 
            sendMessage(Long.valueOf(object.toString()),"delete");
        }
        itemDescService.deleteByIds(ids, ItemDesc.class, "itemId");
    }
    
    /**
     * 根据创建时间分页查询
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryItemList(int page, int rows) {
        PageHelper.startPage(page, rows);
        Example example=new Example(Item.class);
        example.setOrderByClause("created DESC");
        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo=new PageInfo<Item>(items);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
}


