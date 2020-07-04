package com.sxnsh.exam.entity;

import lombok.Data;

/**
 * @author yexz
 */
@Data
public class ExamRecord {

  private long id;
  private String templateId;
  private String templateName;
  private String userId;
  private String userName;
  private String organization;
  private String orgName;
  private String score;
  private int correctSingleNum;
  private String singleTScore;
  private int correctMutiNum;
  private String mutiTScore;
  private int correctJudgeNum;
  private String judgeTScore;
  private String userAnswerList;
  private String testTime;
  private String remark;
  private String remark2;


}
