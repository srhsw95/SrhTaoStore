package com.taotao.manage.controller;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

/**
 * @author Srhsw95
 * @version 2017年2月7日 下午10:41:34
 */
@Controller
@RequestMapping("item")
public class ItemConteoller {

    // 商品服务
    @Autowired
    private ItemService itemService;

    /**
     * 保存商品服务
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        // 初始值
        try {
            if (StringUtils.isEmpty(item.getTitle())) {
                // 如果商品标题为空 400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Boolean saveItem = itemService.saveItem(item, desc,itemParams);
            if (!saveItem) {
                // 抛出异常
                throw new RuntimeErrorException(null, "保存操作失败");
            }
            // 创建成功 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 系统错误 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 更新商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
        // 初始值
        try {
            if (StringUtils.isEmpty(item.getTitle())) {
                // 如果商品标题为空 400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Boolean saveItem = itemService.updateItem(item, desc,itemParams);
            if (!saveItem) {
                // 抛出异常
                throw new RuntimeErrorException(null, "保存操作失败");
            }
            // 创建成功 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 系统错误 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询商品服务 http://manage.taotao.com/rest/item?page=1&rows=30
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> listItem(@RequestParam(value = "page") Long page,
            @RequestParam(value = "rows") Long rows) {
        try {
            EasyUIResult easyUIResult = itemService.queryItemList(page.intValue(), rows.intValue());
            // PageInfo<Item> pageInfo = itemService.queryPageListByWhere(null, page.intValue(),
            // rows.intValue());
            if (null != easyUIResult && null != easyUIResult.getRows() && easyUIResult.getTatal() > 0) {
                return ResponseEntity.ok(easyUIResult);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 系统错误 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 物理删除
     * 
     * @param ids
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItem(@RequestParam("ids") List<Object> ids) {
        try {
            itemService.deleteItemsByIds(ids);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 系统错误 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
