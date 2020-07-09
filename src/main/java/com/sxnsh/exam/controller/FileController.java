package com.sxnsh.exam.controller;

import com.sxnsh.exam.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yexz
 */
@RestController
public class FileController {

    @Resource
    FileService fileService;
    /**
     * 传输题库文件路径并解析成数据写入数据库
     * @param filepath  文件路径
     * @return 结果
     */
    @GetMapping("file/{filePath}")
    public int transFileToData(@PathVariable("filePath") String filepath){
        filepath = "D:\\工作\\项目-在线考试\\在线考试系统\\题库\\营销经理考试\\考试题库导入模板1.xls";
        return fileService.transFileToData(filepath);
    }

}
