package com.taotao.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.service.RedisService;
import com.taotao.common.utils.CookieUtils;

/**
 * 刷新商品信息拦截器 用户未登陆状态下
 * 
 * @author Srhsw95
 * @version 2017年3月1日 下午9:51:55
 */
public class ItemHandlerInterceptor implements HandlerInterceptor {
    private static final String TT_CART_FLUSH = "TT_CART_FLUSH";

    private static final String TT_CART = "TT_CART";

    private static final String KEY_STR = "CATR_";

    @Autowired
    private RedisService redisService;

    /**
     * 购物车在浏览器保存时间
     */
    private static final Integer MAX_AGE = 60 * 60 * 24 * 30 * 3;

    /**
     * 记录浏览器刷新标识
     */
    private static final Integer REFUSH_TIME = 60 * 60 * 24;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 刷新购物车的cookie和redis中数据的生存时间
        String cookieValue = CookieUtils.getCookieValue(request, TT_CART_FLUSH);
        if (StringUtils.isBlank(cookieValue)) {
            // 今天没有刷新
            String ttCart = CookieUtils.getCookieValue(request, TT_CART);
            if (StringUtils.isNotBlank(ttCart)) {
                String key = KEY_STR + ttCart;
                // 更新redis时间
                this.redisService.expire(key, MAX_AGE);
                // 更新cookie时间
                CookieUtils.setCookie(request, response, TT_CART, ttCart, MAX_AGE);
                // 添加cookie寿命表示
                CookieUtils.setCookie(request, response, TT_CART_FLUSH, "TRUE", REFUSH_TIME);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}
