package com.sxnsh.exam.vo;

import com.sxnsh.exam.entity.ExamQuestion;
import com.sxnsh.exam.entity.ExamTemplate;
import lombok.Data;

import java.util.List;

/**
 * @author yexz
 * @date 2020/3/18
 */
@Data
public class ExamPaper {
    /**试卷的基础描述信息**/
    private ExamTemplate examTemplate;
//    /**试题**/
//    private List<QuestForTest> questionList;
    /**单选试题及选项**/
    private List<QuestForTest> singleQuesList;
    /**多选试题及选项**/
    private List<QuestForTest> mutiQuesList;
    /**判断题试题**/
    private List<QuestForTest> judgeQuesList;

}
