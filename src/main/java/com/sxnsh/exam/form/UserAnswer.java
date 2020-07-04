package com.sxnsh.exam.form;

import lombok.Data;

/**
 * @author yexz
 * @date  2020/3/19
 */
@Data
public class UserAnswer {
    /**题目ID**/
    private String questionId;
    /**用户填写的答案  使用对应的value**/
    private String answer;
    /**备用一**/
    private String remark1;
    /**备用二**/
    private String remark2;
}
