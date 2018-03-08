package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.sso.query.bean.User;



/**
 * @author  Srhsw95
 * @version 2017年2月22日 下午8:40:51
 */
@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ItemService itemService;
    
    /**
     * 添加商品至购物车
     * 判断：加入的商品在购物车中是否存在，如果存在，将数量相加  不存在，添加至数据库
     * @param itemId
     */
    public void addItemToCart(Long itemId) {
        Cart record=new Cart();
        record.setItemId(itemId);
        User user = UserThreadLocal.getUser();
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if(null==cart){
            //说明不存在
            cart=new Cart();
            cart.setUserId(user.getId());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            //商品基本数据，通过后台系统查询
            Item item = this.itemService.queryById(itemId);
            if(null!=item){
                cart.setItemId(itemId);
                cart.setItemTitle(item.getTitle());
                cart.setItemImage(StringUtils.split(item.getImage(), ',')[0]);
                cart.setItemPrice(item.getPrice());
                cart.setNum(1);//TODO
                //保存至数据库
                this.cartMapper.insert(cart);
            }
            //抛出业务异常
        }else{
            //存在，数量相加，默认数量为1  TODO
            cart.setNum(cart.getNum()+1);
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }
    
    /**
     * 查询用户购物车信息
     * @return
     */
    public List<Cart> quertCartList() {
        return this.quertCartListByUserId(UserThreadLocal.getUser().getId());
    }
    
    /**
     * 更新购物车商品数量
     * @param itemId
     * @param num
     */
    public void updateCartItemNum(Long itemId, Integer num) {
        User user = UserThreadLocal.getUser();
        //更新数据
        Cart record=new Cart();
        record.setNum(num);
        record.setUpdated(new Date());
        //更新条件
        Example example=new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", user.getId());
        this.cartMapper.updateByExampleSelective(record, example);
    }
    
    /**
     * 更新购物车商品数量
     * @param itemId
     * @param num
     */
    public void updateCartItemNumAndUserId(Long userId,Long itemId, Integer num) {
        //更新数据
        Cart record=new Cart();
        record.setNum(num);
        record.setUpdated(new Date());
        //更新条件
        Example example=new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", userId);
        this.cartMapper.updateByExampleSelective(record, example);
    }
    
    /**
     * 删除商品信息
     * @param itemId
     */
    public void deleteCartItem(Long itemId) {
        Cart record=new Cart();
        record.setItemId(itemId);
        record.setUserId(UserThreadLocal.getUser().getId());
        this.cartMapper.delete(record);
    }

    /**
     * 根据用户ID查询购物车信息
     * @param userId
     * @return
     */
    public List<Cart> quertCartListByUserId(Long userId) {
        Example example=new Example(Cart.class);
        //设置其排序条件
        example.setOrderByClause("created DESC");
        //设置查询条件
        example.createCriteria().andEqualTo("userId", userId);
        //按照加入购物车的时间进行倒序排序
        return this.cartMapper.selectByExample(example);
    }
    
    
    /**
     * 添加商品至购物车
     * 
     * @param cart
     */
    public void insertCart(Cart cart) {
       if(null!=cart){
           this.cartMapper.insert(cart);
       }
    }
}


