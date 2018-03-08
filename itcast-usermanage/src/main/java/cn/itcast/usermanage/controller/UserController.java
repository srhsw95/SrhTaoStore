package cn.itcast.usermanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.usermanage.bean.EasyUIResult;
import cn.itcast.usermanage.service.UserService;



/**
 * 进行用户的相关操作的controller
 * @author  Srhsw95
 * @version 2017年2月5日 下午5:06:29
 */
@Controller
@RequestMapping("user")
public class UserController {
    
    /**
     * 用户操作服务
     */
    @Autowired
    private UserService userService;
    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value="list",method=RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryUserList(@RequestParam("page") Integer page,@RequestParam("rows") Integer rows){
        EasyUIResult easyUIResult = this.userService.queryUserList(page,rows);
        return easyUIResult;
    }
}


