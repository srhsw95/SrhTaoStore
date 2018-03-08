package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Srhsw95
 * @version 2017年2月11日 下午5:29:51
 */
@Service
public class PropertiesService {
    @Value("${TAOTAO_MANAGE_URL}")
    public String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1_URL}")
    public String INDEX_AD1_URL;

    @Value("${INDEX_AD2_URL}")
    public String INDEX_AD2_URL;

    @Value("${TAOTAO_ITEM}")
    public String TAOTAO_ITEM;
    
    @Value("${TAOTAO_ITEM_DESC}")
    public String TAOTAO_ITEM_DESC;
    
    @Value("${TAOTAO_ITEM_PARAM_ITEM}")
    public String TAOTAO_ITEM_PARAM_ITEM;
    
}
