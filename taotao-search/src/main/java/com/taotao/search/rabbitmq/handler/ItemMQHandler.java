package com.taotao.search.rabbitmq.handler;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;

/**
 * 监听消息，处理搜索系统数据
 * 
 * @author Srhsw95
 * @version 2017年2月21日 下午9:26:40
 */
public class ItemMQHandler {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private HttpSolrServer httpSolrServer;
    
    @Autowired
    private ItemService itemService;
    /**
     * 监听操作
     */
    public void excute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String itemId = jsonNode.get("itemId").asText();
            String type = jsonNode.get("type").asText();
            
            if (StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")) {
                //如果是新增 或者 更新 
                Item item = this.itemService.queryByItemId(Long.valueOf(itemId));
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            } else if (StringUtils.equals(type, "delete")) {
                //如果是删除
                this.httpSolrServer.deleteById(itemId);
                //提交
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
