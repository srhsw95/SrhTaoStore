package com.taotao.manage.controller.api;

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
 * @version 2017年2月11日 下午5:14:01
 */
@Controller
@RequestMapping("api/item/desc")
public class ApiItemDescController {
    @Autowired
    private ItemDescService itemDescService;
    
    /**
     * 查询商品信息
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemDescId}",method=RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemById(@PathVariable(value="itemDescId")Long itemDescId){
        try {
            ItemDesc itemDesc = itemDescService.queryById(itemDescId);
            if(null!=itemDesc){
                return ResponseEntity.ok(itemDesc);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


