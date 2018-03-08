package cn.itcast.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itcast.usermanage.bean.EasyUIResult;
import cn.itcast.usermanage.mapper.UserMapper;
import cn.itcast.usermanage.pojo.User;

/**
 * 查询用户操作相关
 * 
 * @author Srhsw95
 * @version 2017年2月5日 下午5:22:01
 */
@Service
public class UserService {

    // 目前只有UserMapper接口 Mybatis的动态代理 扫描器实现这一接口
    // （通过mapper接口扫描器来实现<applicationContext-mybatis.xml中的mapper接口扫描器来实现>）
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户列表
     * 
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryUserList(Integer page, Integer rows) {

        // 设置分页参数
        PageHelper.startPage(page, rows);
        // 查询User数据 没有条件查询 userMapper进行查询后的对象 是经过分页助手包装后的对象
        // 在进行查询的的时候，已经查询了第几页的第几个数据了
        
        //添加查询条件
        Example example=new Example(User.class);
        example.setOrderByClause("updated DESC");
//        List<User> users = this.userMapper.select(null);
        List<User> users = this.userMapper.selectByExample(example);

        // 获取分页后的信息
        PageInfo<User> pageInfo = new PageInfo<User>(users);

        // 封装为EasyUIResult 返回
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
    
    /**
     * 查询
     * @param id
     * @return
     */
    public User queryUserById(Long id) {
        return this.userMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 新增
     * @param user
     * @return
     */
    public Boolean saveUser(User user) {
        return  this.userMapper.insertSelective(user)==1;
    }

    public Boolean update(User user) {
        return  this.userMapper.updateByPrimaryKeySelective(user)==1;
    }

    public Boolean deleteUser(Long id) {
        return this.userMapper.deleteByPrimaryKey(id)==1;
    }

}
