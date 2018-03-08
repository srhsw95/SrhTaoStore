package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;



/**
 * @author  Srhsw95
 * @version 2017年2月9日 下午3:30:24
 */
@Service
public class ContentCategoryService extends BaseService<ContentCategory>{
    
    /**
     * 保存子节点 更新父节点
     * @param contentCategory
     * @return
     */
    public ContentCategory saveContentCategory(ContentCategory contentCategory) {
        // TODO Auto-generated method stub
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        this.save(contentCategory);
        //判断该节点的父节点是否为true 不是则改为true
        ContentCategory parent = this.queryById(contentCategory.getParentId());
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            this.update(parent);
        }
        return contentCategory;
    }
    
    /**
     * 删除子节点  更新父节点
     * @param contentCategory
     */
    public void deleteContentCategory(ContentCategory contentCategory) {
        //递归查找该节点下的所有自己节点 
        List<Object> ids=new ArrayList<Object>();
        //递归查找本节点以及所有子节点ID
        findAllSubNode(ids, contentCategory.getId());
        //删除所有子节点
        this.deleteByIds(ids, ContentCategory.class, "id");
        
        //查询父节点是否还有子节点
        ContentCategory record =new  ContentCategory();
        record.setParentId(contentCategory.getParentId());
        List<ContentCategory> queryListByWhere = this.queryListByWhere(record);
        //是否还有其他子节点
        //还有则不进行任何其他操作了
        if(null==queryListByWhere||queryListByWhere.isEmpty()){
            //更新父节点信息
            ContentCategory record2 =new  ContentCategory();
            record2.setId(contentCategory.getParentId());
            record2.setIsParent(false);
            super.updateSelective(record2);
            //这边有就更新，没有就不更新了呗
        }
    }
    
    /**
     * 递归查找该节点下的所有子节点
     * @param ids 查找到的结果集合
     * @param pid 父节点id
     */
    public void findAllSubNode(List<Object> ids,Long pid){
        ids.add(pid);
        //创建查询对象
        ContentCategory record=new ContentCategory();
        record.setParentId(pid);
        List<ContentCategory> queryList = this.queryListByWhere(record);
        for (ContentCategory contentCategory : queryList) {
            this.findAllSubNode(ids,contentCategory.getId());
        }
    }

}


