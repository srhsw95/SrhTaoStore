package com.taotao.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.common.utils.CookieUtils;

/**
 * @author Srhsw95
 * @version 2017年2月23日 下午7:15:37
 */
@Service
public class CartCookieService {
    /**
     * cookie中购物车name
     */
    private static final String COOKIE_NAME = "TT_CART";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 购物车在浏览器保存时间
     */
    private static final Integer MAX_AGE = 60 * 60 * 24 * 30 * 3;

    @Autowired
    private ItemService itemService;

    /**
     * 添加商品值购物车
     * 
     * @param itemId
     * @param request
     * @param response
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 从cookie中获取购物车信息 作解码操作
        String jsonDate = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        List<Cart> carts = null;
        carts = queryCartList(jsonDate);
        Cart cart = null;
        // 判断商品是否存在与这个集合中
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 该商品已存在于购物车中
                cart = c;
                break;
            }
        }
        if (null == cart) {
            // 说明不存在
            // 写入
            // 说明不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            // 商品基本数据，通过后台系统查询
            Item item = this.itemService.queryById(itemId);
            if (null != item) {
                cart.setItemId(itemId);
                cart.setItemTitle(item.getTitle());
                cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
                cart.setItemPrice(item.getPrice());
                cart.setNum(1);// TODO
                carts.add(cart);
            }
            // 抛出业务异常
        } else {
            // 修改数量
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
        }
        // 将购物车信息写入到cookie中
        CookieUtils /* cookie保存时间 是否编码(此方法默认为utf-8) */
        .setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), MAX_AGE, true);
    }

    /**
     * 从cookie中获取购物车信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public List<Cart> queryCartList(HttpServletRequest request) throws Exception {
        String jsonDate = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        List<Cart> carts = null;
        carts = queryCartList(jsonDate);

        return carts;
    }

    /**
     * 查询购物车信息
     * 
     * @param jsonDate
     * @return
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     */
    private List<Cart> queryCartList(String jsonDate) throws IOException, JsonParseException,
            JsonMappingException {
        List<Cart> carts;
        if (StringUtils.isEmpty(jsonDate)) {
            // 没有，则创建一个list 将cart对象信息放入 ,写入cookie
            carts = new ArrayList<Cart>();
        } else {// 进行反序列化
            // 有 则判断list中是否有这个货物，购物车信息，有则数量变更 无则添加至list中
            // 如何将json数据转换为集合类型数据
            carts = MAPPER.readValue(jsonDate,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
        return carts;
    }

    /**
     * 更新购物车商品个数信息
     * 
     * @param request
     * @param response
     * @param num
     * @param itemId
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public void updateCartItemNum(HttpServletRequest request, HttpServletResponse response, Long itemId,
            Integer num) throws Exception {
        String jsonDate = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        List<Cart> carts = null;
        carts = queryCartList(jsonDate);
        Cart cart = null;
        // 判断商品是否存在与这个集合中
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                // 该商品已存在于购物车中
                cart = c;
                break;
            }
        }
        if (null != cart) {
            cart.setNum(num);
            cart.setUpdated(new Date());
        } else {
            // 非法参数，什么都不做
            return;
        }
        // 将购物车信息写入到cookie中
        CookieUtils
                .setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), MAX_AGE, true);
    }
    
    /**
     * 删除购物车中的商品信息
     * @param itemId
     * @param request
     * @param response
     * @throws Exception
     */
    public void deleteCartItem(Long itemId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String jsonDate = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        List<Cart> carts = null;
        carts = queryCartList(jsonDate);
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getItemId().longValue() == itemId.longValue()) {
                carts.remove(i);
            }
        }
        // 将购物车信息写入到cookie中
        CookieUtils
                .setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts), MAX_AGE, true);
    }
}
