package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

/**
 * @author Srhsw95
 * @version 2017年2月9日 下午3:32:36
 */
@Controller
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 新增内容
     * 
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {
            content.setId(null);
            contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 查询展示商品信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContent(@RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "page",defaultValue="1") Long page,@RequestParam(value = "rows",defaultValue="10") Long rows) {
        try {
            Content record=new Content();
            record.setCategoryId(categoryId);
            EasyUIResult easyUIResult = contentService.queryListByCategoryId(categoryId,page.intValue(),rows.intValue());
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    
    /**
     * 删除操作
     * @param ids
     * @return
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public ResponseEntity<Void> deleteContent(@RequestParam(value="ids") List<Object> ids){
        try {
            contentService.deleteByIds(ids, Content.class, "id");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
