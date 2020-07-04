package com.sxnsh.exam.controller;

import com.github.pagehelper.PageInfo;
import com.sxnsh.exam.domain.TableDomain;
import com.sxnsh.exam.entity.ExamRecord;
import com.sxnsh.exam.service.AdminService;
import com.sxnsh.exam.util.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yexz
 * @date  20200327
 */
@RestController
public class AdminController {

    @Resource
    AdminService adminService;
    /**
     * 获取考试记录列表
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("getRecords")
    public TableDomain getRecordList(@RequestParam("page") int page, @RequestParam("limit") int limit
            , @RequestParam(value = "id" , required = false) String id,@RequestParam(value = "orgId" , required = false) String orgId){

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(page);
        pageRequest.setPageSize(limit);

        PageInfo<ExamRecord> pageList = adminService.getRecordPageInfo(pageRequest,id,orgId);
        TableDomain tableDomain = new TableDomain();
        tableDomain.setCode("0");
        tableDomain.setCount(pageList.getTotal());
        tableDomain.setData(pageList.getList());

        return tableDomain;
    }

}
