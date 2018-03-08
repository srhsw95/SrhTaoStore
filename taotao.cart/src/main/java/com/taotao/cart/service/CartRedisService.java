package com.taotao.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.common.service.RedisService;



/**
 * 将购物车信息保存至redis中 按照hash方式进行保存
 * @author  Srhsw95
 * @version 2017年3月1日 下午7:24:16
 */
@Service
public class CartRedisService {
    private static final ObjectMapper MAPPER=new ObjectMapper();
    
    /**
     * 购物车在浏览器保存时间
     */
    private static final Integer MAX_AGE = 60 * 60 * 24 * 30 * 3;
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private ItemService itemService;
    
    private static final String KEY_STR="CATR_";
    
    /**
     * 添加商品至购物车
     * @param itemId
     * @param cartKey
     */
    public void addItemToCart(Long itemId, String cartKey) {
        //判断该商品是否存在购物车中
        //存在 数量加1 不存在 查找 添加
        try {
            String key=KEY_STR+cartKey;
            String itemIdStr=String.valueOf(itemId);
            String value = this.redisService.hget(key,itemIdStr);
            Cart cart=null;
            if(StringUtils.isBlank(value)){
                //商品不存在
                //查询商品数据后写入
                Item item = itemService.queryById(itemId);
                if(null!=item){
                    cart=new Cart();
                    cart.setItemId(itemId);
                    String[] split = StringUtils.split(item.getImage(),',');
                    cart.setItemImage(split[0]);
                    cart.setItemPrice(item.getPrice());
                    cart.setItemTitle(item.getTitle());
                    cart.setNum(1);
                    cart.setCreated(new Date());
                    cart.setUpdated(cart.getCreated());
                }
            }else{
                //商品存在 数量增加
                cart=MAPPER.readValue(value, Cart.class);
                cart.setNum(cart.getNum()+1);
            }
            this.redisService.hset(key, itemIdStr, MAPPER.writeValueAsString(cart), MAX_AGE);
            //设置这个key的最后访问时间
            this.redisService.hset(key, "update", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 查找redis中的商品数据
     * @param cartKey 未登录用户标识信息
     * @return List<Cart> 购物车集合信息 
     */
    public List<Cart> queryCartList(String cartKey) {
        List<Cart> carts=new ArrayList<Cart>();
        if(StringUtils.isBlank(cartKey)){
            return carts;
        }
        try {
            String key=KEY_STR+cartKey;
            Map<String, String> cartMap = redisService.hgetAll(key);
            Set<Entry<String, String>> entrySet = cartMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                if(!StringUtils.equals(entry.getKey(),"update")){
                    Cart cart = MAPPER.readValue(entry.getValue(), Cart.class);
                    carts.add(cart);
                }
            }
            //刷新生存时间
            this.redisService.expire(key, MAX_AGE);
            //设置这个key的最后访问时间
            this.redisService.hset(key, "update", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return carts;
    }
    
    /**
     * 更新商品数量
     * @param itemId 商品ID
     * @param num    商品数量
     * @param cartKey  未登录用户标识
     */
    public void updateCartItemNum(Long itemId, Integer num, String cartKey) {
        try {
            String key=KEY_STR+cartKey;
            // TODO Auto-generated method stub
            String value = this.redisService.hget(key, String.valueOf(itemId));
            if(StringUtils.isBlank(value)){
                return ;
            }
            Cart cart=MAPPER.readValue(value, Cart.class);
            cart.setNum(num);
            cart.setUpdated(new Date());
            this.redisService.hset(key, String.valueOf(itemId), MAPPER.writeValueAsString(cart), MAX_AGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除商品信息
     * @param itemId
     * @param cartKey
     */
    public void deleteCartItem(Long itemId, String cartKey) {
        String key=KEY_STR+cartKey;
        if(StringUtils.isBlank(cartKey)){
            return ;
        }
        this.redisService.hdel(key, String.valueOf(itemId));
        //刷新生存时间
        this.redisService.expire(key, MAX_AGE);
        //设置这个key的最后访问时间
        this.redisService.hset(key, "update", String.valueOf(System.currentTimeMillis()));
    }
}


