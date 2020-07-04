package com.sxnsh.exam.vo;

import lombok.Data;

/**
 * @author yexz
 */
@Data
public class ExamResult {
    /**总分**/
    private String score;
    /**单选题总分**/
    private String singleTScore;
    /**做对的单选题数目**/
    private String correctSingleNum;
    /**多选题总分**/
    private String mutiTScore;
    /**做对的多选题数目**/
    private String correctMutiNum;
    /**判断题总分**/
    private String judgeTScore;
    /**做对的判断题数目**/
    private String correctJudgeNum;
    /**备用一**/
    private String remark;
    /**备用二**/
    private String remark2;
}
