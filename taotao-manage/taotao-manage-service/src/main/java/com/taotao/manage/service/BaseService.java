package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;



/**
 * @author  Srhsw95
 * @version 2017年2月7日 下午8:40:15
 */
public abstract class BaseService<T extends BasePojo> {
    /**
     * 1、queryById
       2、queryAll
       3、queryOne
       4、queryListByWhere
       5、queryPageListByWhere
       6、save
       7、update
       8、deleteById
       9、deleteByIds
       10、deleteByWhere
     */
    
    /**
     * spring4 支持泛型注入
     */
    @Autowired
    private Mapper<T> mapper;
    /**
     * 01、根据主键查询
     * @param Id
     * @return
     */
    public T queryById(Long Id){
        return mapper.selectByPrimaryKey(Id);
    }
    
    /**
     *02、查询所有
     * @return
     */
    public List<T> queryAll(){
        return mapper.select(null);
    }
    
    /**
     *03、查询特定对象 根据条件查询  如果查询出多条 抛出异常
     * @return
     */
    public T queryOne(T record){
        return mapper.selectOne(record);
    }
    
    /**
     * 04、根据条件查询多个
     */
    public List<T> queryListByWhere(T record){
        return mapper.select(record);
    }
    
    /**
     * 05、根据条件分页查询
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record,Integer page,Integer rows){
        PageHelper.startPage(page, rows);
        List<T> list = mapper.select(record);
        //将list放入pageInfo
        return new PageInfo<T>(list);
    }
    
    /**
     * 06、新增对象
     * @param record
     * @return
     */
    public Integer save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insert(record);
    }
    
    /**
     * 选择不为null的字段进行插入
     * @return
     */
    public Integer saveSelective(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insertSelective(record);
    }
    
    
    /**
     * 07、更新对象,根据主键更新
     */
    public Integer update(T record){
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKey(record);
    }
    
    /**
     * 08、根据已选择的进行更新，不为null的进行更新
     * @param record
     * @return
     */
    public Integer updateSelective(T record){
        record.setUpdated(new Date());
        record.setCreated(null);//强制创建时间为null,永远不会进行更新
        return mapper.updateByPrimaryKeySelective(record);
    }
    
    /*deleteById
    9、deleteByIds
    10、deleteByWhere*/
    
    /**
     * 根据主键ID删除数据 物理删除
     * 逻辑删除使用update即可
     * 删除
     * @return
     */
    public Integer deleteById(Long id){
        return mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 批量删除数据
     * @param ids
     * @param clazz
     * @param property
     * @return
     */
    public Integer deleteByIds(List<Object> ids,Class<T> clazz,String property){
        Example example=new Example(clazz);
        //设置条件
        example.createCriteria().andIn(property, ids);
        return mapper.deleteByExample(example);
    }
    
    /**
     * 根据条件删除数据
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return mapper.delete(record);
    }
    
//    /**
//     * 获取具体的mapper对象,子类务必实现这个getMapper方法
//     * @return
//     */
//    public abstract Mapper<T> getMapper();
}


