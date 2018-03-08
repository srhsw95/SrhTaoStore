package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;



/**
 * @author  Srhsw95
 * @version 2017年2月8日 下午1:18:25
 */
@RequestMapping("item/desc")
@Controller
public class ItemDescContoller {
    @Autowired
    private ItemDescService itemDescService;
    
    /**
     * 查询商品描述信息
     * @param id
     * @return
     */
    @RequestMapping(value="{id}",method=RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDesc(@PathVariable("id")Long id){
        try {
            ItemDesc itemDesc = itemDescService.queryById(id);
            if(null==itemDesc){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
}


