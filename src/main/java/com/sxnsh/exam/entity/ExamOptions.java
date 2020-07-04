package com.sxnsh.exam.entity;

import lombok.Data;

/**
 * @author yexz
 */
@Data
public class ExamOptions {

  /**题目序号**/
  private int id;
  /**所属题目ID**/
  private String questionId;
  /**选项内容**/
  private String content;
  /**选项编号**/
  private String optionNum;
  /**备注一**/
  private String remark;
  /**备注二**/
  private String remark2;


}
