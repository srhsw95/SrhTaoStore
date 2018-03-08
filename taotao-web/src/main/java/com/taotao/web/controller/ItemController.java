package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;



/**
 * 商品详情页面
 * @author  Srhsw95
 * @version 2017年2月11日 下午5:02:45
 */
@RequestMapping("item")
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView itemDetail(@PathVariable(value="itemId")Long itemId){
        //创建ModelAndView 以及其所指向的页面
        ModelAndView mv=new ModelAndView("item");
        Item item=this.itemService.queryById(itemId);
        mv.addObject("item", item);
        
        //查询商品描述数据
        ItemDesc itemDesc=this.itemService.queryDescByItemId(itemId);
        mv.addObject("itemDesc", itemDesc);
        
        //查询商品参数信息
        String itemParam=this.itemService.queryItemParamItemByItemId(itemId);
        mv.addObject("itemParam", itemParam);
        return mv;
    }
}


