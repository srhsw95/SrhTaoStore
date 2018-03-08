package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;



/**
 * 
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月8日 下午5:00:43
 * we can not wait,we can not stop!
 */
@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryItemParamItemById(@PathVariable("itemId")Long itemId){
        try {
            ItemParamItem itemParamItem=new ItemParamItem();
            itemParamItem.setItemId(itemId);
            ItemParamItem queryOne = itemParamItemService.queryOne(itemParamItem);
            if(null!=queryOne){
                return ResponseEntity.ok(queryOne);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


