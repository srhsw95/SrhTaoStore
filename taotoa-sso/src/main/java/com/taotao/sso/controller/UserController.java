package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

/**
 * 跳转注册页面
 * 
 * @author Srhsw95
 * @version 2017年2月13日 下午9:18:46
 */
@RequestMapping("user")
@Controller
public class UserController {
    private static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;
    
   
    /**
     * 跳转注册页面
     * 
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    /**
     * 跳转登陆页面
     * 
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 检测数据是否可用
     * 
     * @return
     */
    @RequestMapping(value = "{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable(value = "param") String param,
            @PathVariable(value = "type") Integer type) {
        try {
            Boolean bool = userService.check(param, type);
            if (null == bool) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(bool);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 注册操作
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doGegister(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            // 未通过校验
            result.put("status", "400");
            List<String> msgs = new ArrayList<String>();
            // 获取错误信息
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                msgs.add(objectError.getDefaultMessage());
            }
            result.put("data", "参数有误：" + StringUtils.join(msgs, " | "));
            return result;
        }
        try {
            Boolean bool = this.userService.doGegister(user);
            if (bool) {
                // 注册成功
                result.put("status", "200");
            } else {
                result.put("status", "500");
                result.put("data", "重新注册");
            }
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result.put("status", "500");
            result.put("data", "重新注册");
        }
        return result;
    }

    /**
     * 登陆操作
     * 
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String token = this.userService.doLogin(request,user.getUsername(), user.getPassword());
            if (StringUtils.isEmpty(token)) {
                // 登陆失败
                result.put("status", 500);
                return result;
            }
            // 登陆成功 保存token到cookie中
            CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            result.put("status", 200);
        } catch (Exception e) {
            // 登陆失败
            result.put("status", 500);
            return result;
        }
        return result;
    }

    /**
     * 获取缓存中的用户信息
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "query/{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryUserByToken(token);
            if (null != user) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        // 清除缓存
        Boolean logOut = this.userService.logOut(token);
        if (logOut) {
            // 清楚cookie
            CookieUtils.deleteCookie(request, response, COOKIE_NAME);
            return mv;
        }
        return null;
    }
}
