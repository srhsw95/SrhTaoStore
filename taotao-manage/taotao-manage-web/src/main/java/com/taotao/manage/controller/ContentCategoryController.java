package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;



/**
 * 根据父节点ID查询内容分类列表
 * @author  Srhsw95
 * @version 2017年2月9日 下午3:33:43
 */
@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    
    /**
     * 查询分类信息
     * @param pid
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryListByParentId(@RequestParam(value="id",defaultValue="0") Long pid){
        try {
            ContentCategory record=new ContentCategory();
            record.setParentId(pid);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if(null==list||list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    } 
    
    /**
     * 保存子节点 更新父节点
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
        try {
            this.contentCategoryService.saveContentCategory(contentCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 重命名节点名称
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> renameContentCategory(ContentCategory contentCategory){
        try {
            this.contentCategoryService.updateSelective(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 删除节点信息
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory){
        try {
            //删除节点信息，必要情况下啊，更新父节点,同时还需要删除子节点
            this.contentCategoryService.deleteContentCategory(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
}


