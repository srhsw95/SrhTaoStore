package com.taotao.web.mq.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;



/**
 * 消息监听处理类
 * @author  Srhsw95
 * @version 2017年2月21日 下午8:56:30
 */
public class ItemMQHandler {
    @Autowired
    private RedisService redisService;
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    /**
     * 具体的处理操作
     * 清空缓存中的商品数据，完成数据同步信息
     * @param msg
     */
    public void excute(String msg){
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String itemId=jsonNode.get("itemId").toString();
            this.redisService.del(ItemService.ITEM_KEY+itemId);
            this.redisService.del(ItemService.ITEM_DESC_KEY+itemId);
            this.redisService.del(ItemService.ITEM_PARAM_ITEM_KEY+itemId);
        } catch (Exception e) {
            // 监听到消息后的如果有异常一直抛出，会不断进行重试，这边需要捕获吃掉异常
            e.printStackTrace();
        }
    }
    
}


