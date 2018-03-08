package com.taotao.web.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.bean.User;
import com.taotao.web.bean.Cart;
import com.taotao.web.threadlocal.UserThreadLocal;



/**
 * 处理购物车信息服务
 * @author  Srhsw95
 * @version 2017年2月23日 下午9:09:13
 */
@Service
public class CartService {
    @Autowired
    private ApiService apiService;
    
    private static final String  CART_URL="http://cart.taotao.com/service/api/cart/";
    
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    /**
     * 查询购物车信息
     * @return
     */
    public List<Cart> queryCartList(){
        List<Cart> carts=null;
        try {
            User user=UserThreadLocal.getUser();
            String jsonDate = this.apiService.doGet(CART_URL+user.getId());
            if(StringUtils.isNotEmpty(jsonDate)){
                carts = MAPPER.readValue(jsonDate, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class)); 
                return carts;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return carts;
    }
}


