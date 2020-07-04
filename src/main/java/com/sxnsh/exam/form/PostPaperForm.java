package com.sxnsh.exam.form;

import lombok.Data;

import java.util.List;

/**
 * @author yexz
 */
@Data
public class PostPaperForm {
    /**试卷编号**/
    private String templateId;
    /**考试人**/
    private String userName;
    /**员工编号**/
    private String userId;
    /**用户所属机构**/
    private String organization;
    /**备用字段一**/
    private String remark;
    /**备用字段二**/
    private String remark1;
    /**用户答题数据**/
    List<UserAnswer> answerList;
}
