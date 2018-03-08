package com.taotao.cart.mq.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.common.service.RedisService;
import com.taotao.common.utils.CookieUtils;

/**
 * @author Srhsw95
 * @version 2017年3月2日 下午8:24:40
 */
public class CartMQHandler {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String KEY_STR = "CATR_";

    private static final String COOKIE_NAME = "TT_CART";

    @Autowired
    private RedisService redisService;

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 监听到队列中的消息之后的进行合并购物车操作
     * 
     * @param msg
     * @throws IOException
     * @throws JsonProcessingException
     */
    public void excute(String msg) throws JsonProcessingException, IOException {
        try {
            if (StringUtils.isNoneBlank(msg)) {
                // 解析消息数据
                JsonNode readTree = MAPPER.readTree(msg);
                // 获取未登录前的用户标识
                String ttCart = readTree.get(COOKIE_NAME).asText();
                // 获取未登录时的购物车信息
                List<Cart> redisCarts = getRedisCarts(ttCart);
                // 获取登陆后的用户Id
                String userId = readTree.get("USER_ID").asText();
                // 查询用户的购物车数据
                List<Cart> carts = cartService.quertCartListByUserId(Long.valueOf(userId));
                // 合并购物车数据
                addRedisCartsToDbCarts(redisCarts, carts, userId);
                // 清除redis中的数据
                redisService.del(KEY_STR + ttCart);
                // 清楚cookie数据
//            CookieUtils.deleteCookie(request, response, COOKIE_NAME);
            }
        } catch (Exception e) {
            // 监听中存在异常，会一直循环执行
            e.printStackTrace();
            //日志记录
        }
    }

    /**
     * 合并Redis中的购物车数据
     * 
     * @param redisCarts
     * @param carts
     */
    private void addRedisCartsToDbCarts(List<Cart> redisCarts, List<Cart> carts, String userId) {
        Map<String, Cart> itemIds = new HashMap<String, Cart>();
        for (Cart cart : carts) {
            itemIds.put(String.valueOf(cart.getItemId()), cart);
        }
        HashSet<String> set = new HashSet<String>();
        set.addAll(itemIds.keySet());

        for (Cart redisCart : redisCarts) {
            boolean bol = set.add(String.valueOf(redisCart.getItemId()));
            if (!bol) {
                // 包含，则进行熟练添加
                Cart dbcart = itemIds.get(String.valueOf(redisCart.getItemId()));
                cartService.updateCartItemNumAndUserId(Long.valueOf(userId),redisCart.getItemId(), dbcart.getNum() + redisCart.getNum());
            } else {
                // 不包含，直接添加至数据库
                redisCart.setUserId(Long.valueOf(userId));
                cartService.insertCart(redisCart);
            }
        }
    }

    /**
     * 查询redis中的购物车信息
     * 
     * @param ttCart cookie中的未登录时的用户标识
     * @return
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     */
    private List<Cart> getRedisCarts(String ttCart) throws IOException, JsonParseException,
            JsonMappingException {
        List<Cart> carts = new ArrayList<Cart>();
        // 获取未登录前的后购物车hash数据
        Map<String, String> hgetAll = redisService.hgetAll(KEY_STR + ttCart);
        if (null == hgetAll) {
            return carts;
        }
        // 货物hash值
        Set<Entry<String, String>> entrySet = hgetAll.entrySet();
        // 创建list容器,接收解析后的购物车数据

        for (Entry<String, String> entry : entrySet) {
            if (!StringUtils.equals(entry.getKey(), "update")) {
                String value = entry.getValue();
                Cart cart = MAPPER.readValue(value, Cart.class);
                carts.add(cart);
            }
        }
        return carts;
    }
}
