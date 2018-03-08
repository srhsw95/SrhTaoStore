package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



/**
 * @author  Srhsw95
 * @version 2017年2月8日 上午12:54:03
 */
@Service
public class PropertiesService {
    /**
     * 直接读取properties
     * 1新建properties
     * 2添加至spring的配置文件中
     * 3直接在这边使用@value引入
     * 4注意spring容器与springMVC容器关系 他们 是父子容器， 父容器可以读子容器的东西  子容器不可以读取父容器中的东西
     */
    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;
    
    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;
}


