package com.sxnsh.exam.mapper;

import com.sxnsh.exam.entity.ExamUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yexz
 * @date   2020/3/17
 */
@Mapper
public interface UserMapper {
    /**
     * fetch data by rule id
     *
     * @param userId
     * @return examUser
     */
    ExamUser findUserById(@Param("userId") String userId);
}
