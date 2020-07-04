package com.sxnsh.exam.entity;

import lombok.Data;

@Data
public class ExamUser {

  private int id;
  private String userId;
  private String userName;
  private String password;
  private String certNum;
  private String organization;
  private String clientType;
  private String creator;
  private String remark;
  private String remark2;
}
