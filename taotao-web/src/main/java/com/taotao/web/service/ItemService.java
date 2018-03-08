package com.taotao.web.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;



/**
 * @author  Srhsw95
 * @version 2017年2月11日 下午5:08:46
 * http://manage.taotao.com/rest/api/item/
 */
@Service
public class ItemService {
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private PropertiesService propertiesService;
    
    public final static String ITEM_KEY="TAOTAO_WEB_ITEM";
    
    public final static String ITEM_DESC_KEY="TAOTAO_WEB_ITEM_DESC";
    
    public final static String ITEM_PARAM_ITEM_KEY="TAOTAO_WEB_ITEM_PARAM_ITEM";
    
    private final static Integer REDIS_TIME=60*60*24;
    /**
     * 缓存服务
     */
    @Autowired
    private RedisService redisService;
    /**
     * 根据商品ID查询商品数据
     * @return
     */
    public Item queryById(Long itemId){
        try {
            try {
                String jsonDate = redisService.get(ITEM_KEY+itemId);
                if(StringUtils.isNotBlank(jsonDate)){
                    return MAPPER.readValue(jsonDate, Item.class);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String result = this.apiService.doGet(this.getItemUrl()+itemId.toString());
            if(StringUtils.isNotBlank(result)){
                try {
                    redisService.set(ITEM_KEY+itemId, result, REDIS_TIME);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return MAPPER.readValue(result, Item.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询商品详情数据
     * @param itemId
     * @return
     */
    public ItemDesc queryDescByItemId(Long itemId) {
        try {
            try {
                String jsonDate = redisService.get(ITEM_DESC_KEY+itemId);
                if(StringUtils.isNotBlank(jsonDate)){
                    return MAPPER.readValue(jsonDate, ItemDesc.class);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String result = this.apiService.doGet(this.getItemDescUrl()+itemId.toString());
            if(StringUtils.isNotBlank(result)){
                try {
                    redisService.set(ITEM_DESC_KEY+itemId, result, REDIS_TIME);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return MAPPER.readValue(result, ItemDesc.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    /**
     * 查询产品参数
     * @param itemId
     * @return
     */
    public String queryItemParamItemByItemId(Long itemId) {
        try {
            try {
                String cachDate = redisService.get(ITEM_PARAM_ITEM_KEY+itemId);
                if(StringUtils.isNotEmpty(cachDate)){
                    return cachDate;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String result = this.apiService.doGet(this.getItemParamItemUrl()+itemId.toString());
            if(StringUtils.isNotBlank(result)){
                ItemParamItem itemParamItem = MAPPER.readValue(result, ItemParamItem.class);
                String paramData = itemParamItem.getParamData();
                ArrayNode arrayNode = (ArrayNode)MAPPER.readTree(paramData);
                StringBuilder sb = new StringBuilder();
                sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

                for (JsonNode param : arrayNode) {
                    sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText()
                            + "</th></tr>");
                    ArrayNode params = (ArrayNode) param.get("params");
                    for (JsonNode p : params) {
                        sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>"
                                + p.get("v").asText() + "</td></tr>");
                    }
                }

                sb.append("</tbody></table>");
                try {
                    redisService.set(ITEM_PARAM_ITEM_KEY+itemId, sb.toString(), REDIS_TIME);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return sb.toString();
            }
            return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * 获取Item_url参数
     * @return
     */
    private String getItemUrl(){
        return propertiesService.TAOTAO_MANAGE_URL+propertiesService.TAOTAO_ITEM;
    }
    
    /**
     * 获取ItemDesc_url参数
     * @return
     */
    private String getItemDescUrl(){
        return propertiesService.TAOTAO_MANAGE_URL+propertiesService.TAOTAO_ITEM_DESC;
    }
    
    /**
     * 获取ItemParamItem_Url
     * @return
     */
    private String getItemParamItemUrl(){
        return propertiesService.TAOTAO_MANAGE_URL+propertiesService.TAOTAO_ITEM_PARAM_ITEM;
    }

    
}


