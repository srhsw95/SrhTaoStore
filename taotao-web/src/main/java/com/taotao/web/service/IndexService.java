package com.taotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;



/**
 * @author  Srhsw95
 * @version 2017年2月10日 下午4:28:10
 */
@Service
public class IndexService {
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    
    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;
    
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;
    
    @Autowired
    private ApiService apiService;
    
    /**
     * 查询首页大广告
     * @return
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public String queryIndexAD1() {
        try {
            String url=TAOTAO_MANAGE_URL+INDEX_AD1_URL;
            String josnDate = this.apiService.doGet(url);
            if(StringUtils.isEmpty(josnDate)){
                return null;
            }
            //解析json数据
            JsonNode jsonNode = MAPPER.readTree(josnDate);
            ArrayNode rows= (ArrayNode) jsonNode.get("rows");
            List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
            for (JsonNode row : rows) {
                Map<String,Object> map=new LinkedHashMap<String, Object>();
                map.put("srcB", row.get("pic").asText());
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("heightB", 240);
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查找右上角小广告
     * @return
     */
    public String queryIndexAD2() {
        try {
            String url=TAOTAO_MANAGE_URL+INDEX_AD2_URL;
            String josnDate = this.apiService.doGet(url);
            if(StringUtils.isEmpty(josnDate)){
                return null;
            }
            //解析json数据
            JsonNode jsonNode = MAPPER.readTree(josnDate);
            ArrayNode rows= (ArrayNode) jsonNode.get("rows");
            List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
            for (JsonNode row : rows) {
                Map<String,Object> map=new LinkedHashMap<String, Object>();
                map.put("width", 310);
                map.put("height", 70);
                map.put("src", row.get("pic").asText());
                map.put("href", row.get("url").asText());
                map.put("alt", row.get("title").asText());
                map.put("widthB", 210);
                map.put("heightB", 70);
                map.put("srcB", row.get("pic").asText());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}


