package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;



/**
 * @author  Srhsw95
 * @version 2017年2月9日 下午3:31:05
 */
@Service
public class ContentService extends BaseService<Content>{
    @Autowired
    private ContentMapper contentMapper;
    
    /**
     * 查询内容列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryListByCategoryId(Long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        List<Content> list = this.contentMapper.queryContentList(categoryId);
        PageInfo<Content> pageInfo=new PageInfo<Content>(list);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

}


