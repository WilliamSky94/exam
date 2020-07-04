package com.sxnsh.exam.service;

import com.sxnsh.exam.entity.ExamUser;
import com.sxnsh.exam.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yexz
 * @date
 */
@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    /**
     * 查找用户信息
     * @param userId 客户编号
     * @return
     */
    public ExamUser findUserById(String userId){
        ExamUser user = new ExamUser();
        if(userId != null && !"".equals(userId)){
            user = userMapper.findUserById(userId);
        }
        return user;
    }

}
