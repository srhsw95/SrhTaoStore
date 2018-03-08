package com.taotao.sso.query.api;

import com.taotao.sso.query.bean.User;



/**
 * @author  Srhsw95
 * @version 2017年2月26日 下午8:27:51
 */
public interface UserQueryService {
    /**
     * 根据token信息查询用户数据
     * @param token
     * @return
     */
    public User queryUserByToken(String token);
}


