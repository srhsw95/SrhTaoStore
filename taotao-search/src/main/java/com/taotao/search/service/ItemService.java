package com.taotao.search.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.pojo.Item;



/**
 * @author  Srhsw95
 * @version 2017年2月21日 下午9:40:16
 */
@Service
public class ItemService {
    /**
     * 查询商品信息url
     */
    private static final String ITEM_QUERY_URL="http://manage.taotao.com/rest/api/item/";
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    @Autowired
    private ApiService apiService;
    
    /**
     * 根据商品id查询商品信息
     * @param itemId
     * @return
     */
    public Item queryByItemId(Long itemId){
        try {
            String result = this.apiService.doGet(ITEM_QUERY_URL+itemId.toString());
            if(StringUtils.isNotBlank(result)){
                return MAPPER.readValue(result, Item.class);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}


