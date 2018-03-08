package com.taotao.web.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.bean.User;
import com.taotao.web.pojo.Order;
import com.taotao.web.threadlocal.UserThreadLocal;



/**
 * @author  Srhsw95
 * @version 2017年2月18日 下午7:19:15
 */
@Service
public class OrderService {
    @Autowired
    private ApiService apiService;
    
    private static final String CREATE_ORDER_URL="http://order.taotao.com/order/create";
    private static final String QUERY_ORDER_URL="http://order.taotao.com/order/query/";
    private static final ObjectMapper MAPPER=new ObjectMapper();
    private static final Integer OK=200;
    
    /**
     * 提交订单到订单系统
     * @param order
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String submitOrder(Order order) throws ClientProtocolException, IOException  {
        //添加用户登陆信息
        User user=UserThreadLocal.getUser();
        order.setUserId(user.getId()); 
        order.setBuyerNick(user.getUsername());
        try {
            String jsonDate = MAPPER.writeValueAsString(order);
            HttpResult result = this.apiService.doPostJson(CREATE_ORDER_URL,jsonDate);
            if(result.getCode().intValue()==OK){
                String body = result.getBody();
                JsonNode jsonNode = MAPPER.readTree(body);
                if(jsonNode.get("status").asInt()==OK){
                    //提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Order queryOrderById(Long orderId) {
        // TODO Auto-generated method stub
        try {
            String jsonData = this.apiService.doGet(QUERY_ORDER_URL+orderId);
            Order order = MAPPER.readValue(jsonData, Order.class);
            return order;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}


