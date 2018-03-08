package cn.itcast.usermanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.usermanage.pojo.User;
import cn.itcast.usermanage.service.UserService;

/**
 * @author Srhsw95
 * @version 2017年2月6日 下午12:19:56
 */
@Controller
@RequestMapping("new/user")
public class NewUserController {
    @Autowired
    private UserService userService;

    // 根据用户ID查询用户信息
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> queryUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.queryUserById(id);
            if (null == user) {
                // 资源不存在，响应404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 资源存在，响应200
            // return ResponseEntity.status(HttpStatus.OK).body(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // 出现异常 响应500
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增用户
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveUser(User user) {
        try {
            Boolean bool = this.userService.saveUser(user);
            // 保存成功
            if (bool) {
                // 新增成功 响应201 build()方法可以替代.body(null)
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 更新资源
     */
    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> updateUser(User user){
        try {
            Boolean bool = this.userService.update(user);
            //更新成功
            if(bool){
                //更新成功，响应200
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 删除资源 
     */
    @RequestMapping(method=RequestMethod.DELETE)        //注意这边的参数，是类似于post而非走的get形式，通过url的path路径传递参数，也就不用@PathVariable
    public ResponseEntity<Void> deleteUser(@RequestParam(value="id",defaultValue="0")Long id){
        try {
            if(id.longValue()==0){
                //没有传递参数  响应状态码
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Boolean bool = this.userService.deleteUser(id);
            if(bool){
                //204 文件删除成功
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
