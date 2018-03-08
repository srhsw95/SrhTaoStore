package com.taotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

/**
 * @author Srhsw95
 * @version 2017年2月8日 下午8:56:42
 */
@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 对外提供接口服务，查询所有类目数据
     * jsonp 应用
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatList(@RequestParam(value="callback",defaultValue="")String callback) {
        try {
            ItemCatResult queryAllToTree = itemCatService.queryAllToTree();
            if(null==queryAllToTree){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(queryAllToTree);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
