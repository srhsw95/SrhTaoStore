package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ItemParamMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemParam;



/**
 * 模板服务
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月8日 下午4:59:06
 * we can not wait,we can not stop!
 */
@Service
public class ItemParamService extends BaseService<ItemParam>{
    @Autowired
    private ItemParamMapper itemParamMapper;
    /**
     * 查询商品信息模板列表
     * @param intValue
     * @param intValue2
     */
    public EasyUIResult queryItemParamList(int page, int rows) {
        // TODO Auto-generated method stub
        PageHelper.startPage(page, rows);
        Example example=new Example(Item.class);
        example.setOrderByClause("created DESC");
        List<ItemParam> itemParams = this.itemParamMapper.selectByExample(example);
        PageInfo<ItemParam> pageInfo=new PageInfo<ItemParam>(itemParams);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}


