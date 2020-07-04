package com.sxnsh.exam.controller;

import com.sxnsh.exam.entity.ExamUser;
import com.sxnsh.exam.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yexz
 * @date 2020/3/17
 */
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 根据用户id校验用户是否正确
     * @param userId 客户编号
     * @return 客户信息
     */
    @GetMapping("user/{userId}")
    public int checkUser(@PathVariable("userId") String userId){
        ExamUser user = userService.findUserById(userId);
        return user.getId();
    }

}
