package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.sso.query.api.UserQueryService;
import com.taotao.sso.query.bean.User;



/**
 * @author  Srhsw95
 * @version 2017年2月17日 下午10:02:29
 */
@Service
public class UserService {
    /**
     * 查询用户信息url
     */
    @Autowired
    private UserQueryService userQueryService;
    
    /**
     * 根据token信息查询用户信息
     * @param token
     * @return
     */
    public User queryUserByToken(String token){
        try {
           User user = userQueryService.queryUserByToken(token);
           return user;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}


