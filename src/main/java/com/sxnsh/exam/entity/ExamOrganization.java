package com.sxnsh.exam.entity;


import lombok.Data;

/**
 * @author yexz
 * @date 20200331
 */
@Data
public class ExamOrganization {
  private long id;
  private long companyid;
  private String qywxdepid;
  private long parentid;
  private long fgempid;
  private long zgempid;
  private long xgempid;
  /**机构代码**/
  private String code;
  /**机构名称**/
  private String name;
  private String phone;
  private double longitude;
  private double latitude;
  private String address;
  private long level;
  private String depth;
  private String deptype;
  private String isvirtual;
  private long sortno;
  private String remark;
  private String delflag;


}
