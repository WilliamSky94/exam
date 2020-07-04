package com.sxnsh.exam.entity;

import lombok.Data;

@Data
public class ExamTemplate {

  /**试卷模板ID**/
  private int id;
  /**试卷描述**/
  private String templateDes;
  /**考试名称**/
  private String templateName;
  /**试卷总分**/
  private String totalScore;
  /**限制时间**/
  private String timeLimit;
  /**单选题数目**/
  private String singleNum;
  /**单选题每题分数**/
  private String singleScore;
  /**单选题抽取题目规则 形式为A-15，B-10，C-3 这样，前面是范围 后面是题目数目**/
  private String singleExtarctRule;
  /**多选题数目**/
  private String mutiNum;
  /**多选题每题分数**/
  private String mutiScore;
  /**多选题抽取题目规则 形式为A-15，B-10，C-3 这样，前面是范围 后面是题目数目**/
  private String mutiExtarctRule;
  /**判断题数目**/
  private String judgeNum;
  /**判断题每题分数**/
  private String judgeScore;
  /**判断题抽取题目规则 形式为A-15，B-10，C-3 这样，前面是范围 后面是题目数目**/
  private String judgeExtarctRule;
  /**抽取的题库类型，和exam_question的examType对应  CL-creditLoan信贷考试**/
  private String questBankType;
  /**创建用户**/
  private String creator;
  /**是否考试模式 0-否 1-是  练习模式可以查看答案，正式考试模式不能查看答案 **/
  private String isPractice;
  /**备用一**/
  private String remark;
  /**备用二**/
  private String remark2;

}
