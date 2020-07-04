package com.sxnsh.exam.controller;

import com.sxnsh.exam.entity.ExamOrganization;
import com.sxnsh.exam.entity.ExamRecord;
import com.sxnsh.exam.entity.ExamTemplate;
import com.sxnsh.exam.form.PostPaperForm;
import com.sxnsh.exam.service.ExamService;
import com.sxnsh.exam.vo.ExamPaper;
import com.sxnsh.exam.vo.ExamResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yexz
 * @date
 */
@RestController
public class ExamController {

    @Resource
    ExamService examService;

    /**
     * 获取试卷题目
     * @param templateId 试卷编号
     * @return 试卷信息
     */
    @GetMapping("getPaper/{templateId}")
    public ExamPaper getPaperByTemplateId(@PathVariable ("templateId") String templateId){
        return examService.findExamPaperByTempId(templateId);
    }

    /**
     * 提交答案  获得分数
     * @param postPaperForm 提交的试卷信息
     * @return Score
     */
    @PostMapping("postAnswer")
    public ExamResult handInPaper(@RequestBody PostPaperForm postPaperForm){
        return examService.calculateScore(postPaperForm);
    }

    /**
     * 获取所有有效的试卷
     * @return List<ExamTemplate>
     */
    @GetMapping("getAllExam")
    public List<ExamTemplate> findAllExam(){
        return examService.findAllExam();
    }

    /**
     * 获取所有非一级机构
     * @return List<ExamTemplate>
     */
    @GetMapping("getAllOrgs")
    public List<ExamOrganization> findAllOrgs(){
        return examService.findAllOrgs();
    }
}
