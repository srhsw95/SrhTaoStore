package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;



/**
 * @author  Srhsw95
 * @version 2017年2月8日 下午8:23:48
 */
@Controller
@RequestMapping("index")
public class IndexController {
    
    @Autowired
    private IndexService indexService;
    /**
     * 首页
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView("index");
        //首页大广告
        String indexAD1 =this.indexService.queryIndexAD1();
        mv.addObject("indexAD1",indexAD1);
        
        //首页小广告
        String indexAD2 =this.indexService.queryIndexAD2();
        mv.addObject("indexAD2",indexAD2);
        return mv;
    }
}


