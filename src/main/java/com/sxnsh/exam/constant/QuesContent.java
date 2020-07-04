package com.sxnsh.exam.constant;

import java.util.HashMap;

/**
 * @author yexz
 */
public class QuesContent {

    public static final HashMap<String, String> questionType = new HashMap<String, String>();

    public static final HashMap<String, String> questionScope = new HashMap<String, String>();

    public static final HashMap<String, String> optionRightOrWrong = new HashMap<String, String>();

    static{
        questionType.put("单选题","S");
        questionType.put("多选题","M");
        questionType.put("判断题","J");
        questionType.put("","O");


        questionScope.put("法律法规","A");
        questionScope.put("监管规定","B");
        questionScope.put("人行规定","C");
        questionScope.put("省联社制度","D");
        questionScope.put("助学贷款知识","E");
        questionScope.put("扶贫贷款知识","F");
        questionScope.put("征信知识","G");
        questionScope.put("票据知识","H");
        questionScope.put("信贷基础知识","I");
        questionScope.put("信贷系统操作","J");
        questionScope.put("客户信息系统操作","K");
        questionScope.put("","O");

        optionRightOrWrong.put("正确","1");
        optionRightOrWrong.put("错误","0");




    }
}
