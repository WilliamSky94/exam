package com.sxnsh.exam.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sxnsh.exam.entity.ExamRecord;
import com.sxnsh.exam.mapper.ExamMapper;
import com.sxnsh.exam.util.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yexz
 * @date  20200327
 */
@Service
@Slf4j
public class AdminService {

    @Resource
    ExamMapper examMapper;

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    public PageInfo<ExamRecord> getRecordPageInfo(PageRequest pageRequest, String id, String orgId){

        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<ExamRecord> recordList = examMapper.findRecordByPage(id,orgId);


        return  new PageInfo<ExamRecord>(recordList);

    }
}
