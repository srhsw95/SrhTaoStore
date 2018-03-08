package com.taotao.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Item;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;



/**
 * @author  Srhsw95
 * @version 2017年2月22日 下午9:11:28
 */
@Service
public class ItemService {
 private static final ObjectMapper MAPPER=new ObjectMapper();
    
    @Autowired
    private ApiService apiService;
    
    private final static String QUERY_ITEM_URL="http://manage.taotao.com/rest/api/item/";
    /**
     * 缓存服务
     */
    @Autowired
    private RedisService redisService;
    
    
    /**
     * 根据商品ID查询商品数据
     * 
     * @return
     */
    public Item queryById(Long itemId){
        try{
            String result = this.apiService.doGet(QUERY_ITEM_URL+itemId.toString());
            return MAPPER.readValue(result, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


