package com.taotao.sso.query.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.query.api.UserQueryService;
import com.taotao.sso.query.bean.User;



/**
 * @author  Srhsw95
 * @version 2017年2月26日 下午8:43:30
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 60;
    
    @Autowired
    private RedisService redisService;
    
    
    /**
     * 根据token信息查询用户数据
     */
    @Override
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

}


