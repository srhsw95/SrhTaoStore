package com.taotao.manage.mapper;

import java.util.List;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.pojo.Content;



/**
 * 内容处理Mapper
 * @author  Srhsw95
 * @version 2017年2月9日 下午3:27:39
 */
public interface ContentMapper extends Mapper<Content>{
    /**
     * 根据categoryId查询内容列表，并且按照时间排序
     * @param categoryId
     * @return
     */
    public List<Content> queryContentList(Long categoryId);
        
}


