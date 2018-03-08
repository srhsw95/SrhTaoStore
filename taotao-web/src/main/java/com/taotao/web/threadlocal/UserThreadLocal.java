package com.taotao.web.threadlocal;

import com.taotao.sso.query.bean.User;




/**
 * @author  Srhsw95
 * @version 2017年2月18日 下午8:54:24
 */
public class UserThreadLocal {
    public static final ThreadLocal<User> LOCAL=new ThreadLocal<User>();
    
    private UserThreadLocal(){
        
    }
    
    public static void setUser(User user){
        LOCAL.set(user);
    }
    
    public static User getUser(){
        return LOCAL.get();
    }
}


