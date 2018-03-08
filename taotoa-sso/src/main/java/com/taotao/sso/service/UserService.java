package com.taotao.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

/**
 * @author Srhsw95
 * @version 2017年2月13日 下午10:04:01
 */
@Service
public class UserService {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 60;
    
    private static final String COOKIE_CART_STR="TT_CART";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 校验输入信息
     * 
     * @param param
     * @param type 1.用户名 2.手机号 3.邮箱
     * @return
     */
    public Boolean check(String param, Integer type) {
        User record = new User();
        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            return null;
        }
        User user = this.userMapper.selectOne(record);
        return user != null;
    }

    /**
     * 注册
     * 
     * @param user
     * @return
     */
    public Boolean doGegister(User user) {
        // 初始化处理
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 密码加密处理 MD5加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        return this.userMapper.insert(user) == 1;
    }

    /**
     * 进行登陆操作
     * 
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public String doLogin(HttpServletRequest request,String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            // 用户不存在
            return null;
        }
        
        if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(password))) {
            //密码错误
            return null;
        }
        // 登陆成功 将用户的信息保存至redis中;
        String token = DigestUtils.md5Hex(username + System.currentTimeMillis());
        
        // token前添加表示，用于后期统计 ，保存序列化后的用户数据
        redisService.set("TOKEN_" + token, MAPPER.writeValueAsString(user), REDIS_TIME);
        //登陆成功之后，发送消息至其他团队就行了;购物车那边监听到消息之后，进行合并操作;
        //用户id 以及未登录时cookie值
        try {
            String tt_cart = CookieUtils.getCookieValue(request, COOKIE_CART_STR);
            if(StringUtils.isNotBlank(tt_cart)){
                String userId=String.valueOf(user.getId());
                HashMap<String, String> cartMsg=new HashMap<String,String>();
                cartMsg.put("TT_CART", tt_cart);
                cartMsg.put("USER_ID", userId);
                String message = MAPPER.writeValueAsString(cartMsg);
                rabbitTemplate.convertAndSend("userLogin", message);
            }
        } catch (Exception e) {
            // 发送消息错误
            e.printStackTrace();
        }
        
        return token;
    }

    /**
     * 查询用户信息,更新用户时间
     * @param token
     * @return
     */
    public User queryUserByToken(String token) {
        String key="TOKEN_"+token;
        String jsonDate = redisService.get(key);
        try {
            if(StringUtils.isNotEmpty(jsonDate)){
                //重新设置登陆时间
                redisService.expire(key, REDIS_TIME);
                return MAPPER.readValue(jsonDate, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //登陆超时
        return null;
    }

    public Boolean logOut(String token) {
        String key="TOKEN_"+token;
        Long del = redisService.del(key);
        return del==1L;
    }
}
