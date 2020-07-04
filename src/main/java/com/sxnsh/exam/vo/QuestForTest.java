package com.sxnsh.exam.vo;

import com.sxnsh.exam.entity.ExamOptions;
import lombok.Data;

import java.util.List;
@Data
public class QuestForTest {
    /**题目序号 --主键**/
    private int id;
    /**题目内容**/
    private String content;
    /**题目范围
     * A-法律法规 B-监管规定 C-人行规定 D-省联社制度 E-助学贷款知识 F-扶贫贷款知识  G-征信知识 H-票据知识  I-信贷基础知识 J-信贷系统操作 K-客户信息系统操作**/
    private String scope;
    /**题目范围  S-单选 M-多选 J-判断**/
    private String type;
    /**题目答案  --用于练习模式中展示答案**/
    private String answer;
    /**备用**/
    private String remark;
    /**备用二**/
    private String remark2;
    /**选项**/
    private List<ExamOptions> options;
}
