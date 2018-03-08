package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;



/**
 * @author  Srhsw95
 * @version 2017年2月7日 下午4:05:25
 */
@Controller
@RequestMapping("item/cat")
public class ItemCatController {
    
    @Autowired
    private ItemCatService itemCatService;
    
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatListByParentId(@RequestParam(value="id",defaultValue="0")Long pid){
        try {
            ItemCat record=new ItemCat();
            record.setParentId(pid);
            List<ItemCat> list = itemCatService.queryListByWhere(record);
            if(null==list||list.isEmpty()){
                //如果查询的结果为空
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


