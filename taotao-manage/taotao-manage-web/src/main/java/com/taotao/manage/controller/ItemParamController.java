package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

/**
 * 
 * @author srhsw95_Administrator
 * @email srhsw95@163.com
 * @version 2017年2月8日 下午5:01:19 we can not wait,we can not stop!
 */
@RequestMapping("item/param")
@Controller
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品类目ID查询规格参数模板
     * 
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemParamId(@PathVariable("itemCatId") Long itemCatId) {
        try {
            ItemParam record = new ItemParam();
            record.setItemCatId(itemCatId);
            ItemParam itemParam = this.itemParamService.queryOne(record);
            if (null != itemParam) {
                return ResponseEntity.ok(itemParam);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = { "{itemCatId}" }, method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId,
            @RequestParam("paramData") String paramData) {
        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            itemParam.setParamData(paramData);
            this.itemParamService.save(itemParam);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 查询模板列表
     * @return
     */
    @RequestMapping(value="/list",method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParamList(@RequestParam("page") Long page,@RequestParam Long rows){
        try {
            EasyUIResult easyUIResult = this.itemParamService.queryItemParamList(page.intValue(),rows.intValue());
            if(null!=easyUIResult&&easyUIResult.getTatal()>0){
                return ResponseEntity.ok(easyUIResult);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
