package com.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Item;
import com.taotao.web.pojo.Order;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;

/**
 * @author Srhsw95
 * @version 2017年2月17日 下午9:35:10
 */
@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable(value = "itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = itemService.queryById(itemId);
        mv.addObject("item", item);
        return mv;
    }
    
    /**
     * 基于购物车下单 目前实现购物车中的全部商品下单
     * 针对某一个，一些 商品下单 TODO
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toCartOrder(){
        ModelAndView mv=new ModelAndView("order-cart");
        List<Cart> carts=cartService.queryCartList();
        mv.addObject("carts",carts);
        return mv;
    }
    /**
     * 提交订单
     * @param order
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String orderId = this.orderService.submitOrder(order);
            if (StringUtils.isEmpty(orderId)) {
                result.put("status", 500);
            } else {
                result.put("status", 200);
                result.put("data", orderId);
            }
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result.put("status", 500);
        return result;
    }
    
    
    @RequestMapping(value="success",method=RequestMethod.GET)
    public ModelAndView submitSuccess(@RequestParam("id")Long orderId){
        ModelAndView mv=new ModelAndView("success");
        //订单数据
        Order order=this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        //添加时效信息 当前时间向后推两天
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
