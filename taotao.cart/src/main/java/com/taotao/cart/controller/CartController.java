package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartRedisService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.query.bean.User;

/**
 * @author Srhsw95
 * @version 2017年2月22日 下午8:38:48
 */
@RequestMapping("cart")
@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Autowired
    private CartCookieService cartCookieService;
    
    @Autowired
    private CartRedisService cartRedisService;
    
    /**
     * 购物车在浏览器保存时间
     */
    private static final Integer MAX_AGE = 60 * 60 * 24 * 30 * 3;
    
    /**
     * cookie中购物车name
     */
    private static final String COOKIE_NAME = "TT_CART";
    /**
     * 至购物车页面
     * 
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView cartList(@CookieValue(value=COOKIE_NAME,defaultValue="")String cartKey) {
        ModelAndView mv = new ModelAndView("cart");
        User user = UserThreadLocal.getUser();
        List<Cart> cartList=null;
        if (null == user) {
            try {
                //从cookie中获取购物车信息
                cartList =this.cartRedisService.queryCartList(cartKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 登陆状态
            cartList = this.cartService.quertCartList();
        }
        mv.addObject("cartList", cartList);
        return mv;
    }

    /**
     * 添加商品值购物车
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable(value = "itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response,@CookieValue(value=COOKIE_NAME,defaultValue="")String cartKey) {
        User user = UserThreadLocal.getUser();
        if (null == user) {
            if(StringUtils.isBlank(cartKey)){
                //第一次加入购物车，生成key
                cartKey=DigestUtils.md5Hex(itemId+String.valueOf(System.currentTimeMillis()));
                CookieUtils.setCookie(request, response, COOKIE_NAME, cartKey, MAX_AGE); 
            }
            // 未登录状态 商品 Id Response
            try {
                this.cartRedisService.addItemToCart(itemId, cartKey);
//                this.cartCookieService.addItemToCart(itemId, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 登陆状态 将商品添加至购物车
            this.cartService.addItemToCart(itemId);
        }
        // 重定向到购物车列表页面
        return "redirect:/cart/list.html";
    }

    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable(value = "itemId") Long itemId,
            @PathVariable(value = "num") Integer num,@CookieValue(value=COOKIE_NAME,defaultValue="")String cartKey) {
        User user = UserThreadLocal.getUser();
        if (null == user) {
            // 未登录状态
            try {
                this.cartRedisService.updateCartItemNum(itemId,num,cartKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 更新购物车商品数量
            this.cartService.updateCartItemNum(itemId, num);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String delete(@PathVariable(value = "itemId") Long itemId,@CookieValue(value=COOKIE_NAME,defaultValue="")String cartKey) {
        User user = UserThreadLocal.getUser();
        if (null == user) {
            // 未登录状态
            try {
                this.cartRedisService.deleteCartItem(itemId,cartKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 更新购物车商品数量
            this.cartService.deleteCartItem(itemId);
        }
        // 重定向到购物车列表页面
        return "redirect:/cart/list.html";
    }
}
