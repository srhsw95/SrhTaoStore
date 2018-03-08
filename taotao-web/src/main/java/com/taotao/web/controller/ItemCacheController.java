package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;



/**
 * @author  Srhsw95
 * @version 2017年2月13日 下午7:14:30
 */
@Controller
@RequestMapping("item/cache")
public class ItemCacheController {
    
    @Autowired
    private RedisService redisService;
    
    
    /**
     * 接收商品Id 删除缓存
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable(value="itemId")Long itemId){
        try {
            this.redisService.del(ItemService.ITEM_KEY+itemId.toString());
            this.redisService.del(ItemService.ITEM_DESC_KEY+itemId.toString());
            this.redisService.del(ItemService.ITEM_PARAM_ITEM_KEY+itemId.toString());
            //返回204 操作成功
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //返回系统异常
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


