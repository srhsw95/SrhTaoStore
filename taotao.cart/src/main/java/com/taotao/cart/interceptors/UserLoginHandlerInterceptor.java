package com.taotao.cart.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.service.UserService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.query.bean.User;



/**
 * 用户登陆拦截器
 * @author  Srhsw95
 * @version 2017年2月17日 下午9:55:23
 */
public class UserLoginHandlerInterceptor implements HandlerInterceptor{
    private static final String COOKIE_NAME="TT_TOKEN";
    
    @Autowired
    private UserService userService;
    /**
     * 前置方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            return true;
        }
        //调用sso系统所提供的的接口来查询user
        User user = userService.queryUserByToken(token);
        if(null!=user){
            //登陆成功
           //将用户对象放入本地线程中,方便controller与service中使用
            UserThreadLocal.setUser(user);
            return true;
        }
        //登陆超时
        return true;
    }
    
    /**
     * handler方法后
     * 也就是Controller 方法调用之后执行，但是它会在DispatcherServlet 进行视图返回渲染之前被调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 后置方法
     * 也就是在DispatcherServlet 渲染了对应的视图之后执行。
     * afterCompletion 方法都只能是在当前所属的Interceptor 的preHandle 方法的返回值为true 时才能被调
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        //使用完成后，清空user
        UserThreadLocal.setUser(null);//情况本地线程中的user对象数据，
    }

}


