package cn.itcast.usermanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 * 通用的页面跳转逻辑
 * @author  Srhsw95
 * @version 2017年2月5日 下午3:09:06
 */
@Controller
@RequestMapping("page")
public class PageController {
    
    /**
     * 返回视图名
     * 具体的页面跳转逻辑
     * method=RequestMethod.GET 只能通过get请求
     * @param pageName
     * @return
     */
    @RequestMapping(value="{pageName}",method=RequestMethod.GET)
    public String toPage(@PathVariable("pageName")String pageName){
        return pageName;
    }
}


