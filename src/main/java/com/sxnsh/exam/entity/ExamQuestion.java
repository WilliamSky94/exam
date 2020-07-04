package com.sxnsh.exam.entity;

import lombok.Data;

@Data
public class ExamQuestion {

  /**题目序号 --主键**/
  private int id;
  /**题目内容**/
  private String content;
  /**题目范围
   * A-法律法规 B-监管规定 C-人行规定 D-省联社制度 E-助学贷款知识 F-扶贫贷款知识  G-征信知识 H-票据知识  I-信贷基础知识 J-信贷系统操作 K-客户信息系统操作**/
  private String scope;
  /**题目范围  S-单选 M-多选 J-判断**/
  private String type;
  /**正确答案  单选是选项的ID 多选是多个选项ID，按照顿号隔开  判断题是是或者否的ID**/
  private String answer;
  /**备用**/
  private String remark;
  /**备用二**/
  private String remark2;

}
