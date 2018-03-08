package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.ItemSearchService;

/**
 * @author Srhsw95
 * @version 2017年2月19日 上午12:06:59
 */
@Controller
public class SearchController {
    @Autowired
    private ItemSearchService itemSearchService;

    public static final Integer ROWS = 32;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(value = "q") String keyWord,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        try {
            keyWord=new String(keyWord.getBytes("ISO-8859-1"),"utf-8");
            // 搜索关键字
            mv.addObject("query", keyWord);

            SearchResult result = this.itemSearchService.search(keyWord, page, ROWS);
            // 搜索的结果数据
            mv.addObject("itemList", result.getList());
            // 页数
            mv.addObject("page", page);
            int total = result.getTotal().intValue();

            int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
            // 总页数
            mv.addObject("pages", pages);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mv;
    }

}
