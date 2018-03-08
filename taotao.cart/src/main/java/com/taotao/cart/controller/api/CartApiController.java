package com.taotao.cart.controller.api;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;



/**
 * 对外提供的购物车信息的接口
 * @author  Srhsw95
 * @version 2017年2月23日 下午9:08:19
 */
@RequestMapping("api/cart")
@Controller
public class CartApiController {
    @Autowired
    private CartService cartService;
    
    /**
     * 对外提供接口服务，根据用户iD查询购物车列表
     * @param userId
     * @return
     */
    @RequestMapping(value="{userId}",method=RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable(value="userId")Long userId){
        try {
            List<Cart> carts=cartService.quertCartListByUserId(userId);
            if(CollectionUtils.isEmpty(carts)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}


