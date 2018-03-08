package cn.itcast.usermanage.mapper;

import cn.itcast.usermanage.pojo.User;

import com.github.abel533.mapper.Mapper;



/**
 * @author  Srhsw95
 * @version 2017年2月5日 下午5:03:31
 */
public interface UserMapper extends Mapper<User>{
    /**
     * 继承了Mapper之后，可以不进行crud的编写了
     * 不许要再进行mapper文件编写了
     * 但是只能进行单表操作
     * 
     */
}


