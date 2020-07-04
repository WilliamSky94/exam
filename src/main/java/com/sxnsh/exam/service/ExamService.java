package com.sxnsh.exam.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sxnsh.exam.entity.*;
import com.sxnsh.exam.form.PostPaperForm;
import com.sxnsh.exam.form.UserAnswer;
import com.sxnsh.exam.mapper.ExamMapper;
import com.sxnsh.exam.vo.ExamPaper;
import com.sxnsh.exam.vo.QuestForTest;
import com.sxnsh.exam.vo.ExamResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yexz
 * @date 2020/03/20
 */
@Service
@Slf4j
public class ExamService {

    @Resource
    ExamMapper examMapper;

    public static final String singleQuestion = "S";
    public static final String mutiQuestion = "M";
    public static final String judgeQuestion = "J";



    /**
     *  * fetch data by templateId
     * @desc  获取对应模板编号的试卷
     * @param  templateId
     * @return ExamPaper exampaper
     */
    public ExamPaper findExamPaperByTempId(String templateId){

        ExamPaper examPaper = new ExamPaper();
        if(templateId == null || "".equals(templateId)){
            return null;
        }

        ExamTemplate examTemplate = examMapper.findTemplateById(templateId);
        if(examTemplate != null){
            List<QuestForTest> singleQuesList = this.getRandomSingleQuesList(examTemplate);
            List<QuestForTest> mutiQuesList = this.getRandomMutiQuesList(examTemplate);
            List<QuestForTest> judgeQuesList = this.getRandomJudgeQuesList(examTemplate);

            examPaper.setExamTemplate(examTemplate);
            examPaper.setSingleQuesList(singleQuesList);
            examPaper.setMutiQuesList(mutiQuesList);
            examPaper.setJudgeQuesList(judgeQuesList);
        }else{
            log.error("试题模板ID错误，没有查询到模板信息");
            return null;
        }

        return examPaper;

    }

//    /**
//     *  * fetch data by examtemplate
//     * @desc  内部函数---根据试卷模板随机生成试卷
//     * @param  examTemplate
//     * @return List<QuestForTest>
//     *     题目的抽取规则在各自的extractRule字段中，形为A-5,B-5,C-5,D-5,E-5,F-5  中间用逗号隔开是不同范围的题目，A-5 表示范围是A的题目抽取5道出来
//     */
//    private List<QuestForTest> randomCreateQuest(ExamTemplate examTemplate){
//
//        List<QuestForTest> questList =  new ArrayList<QuestForTest>();
//
//
//    }

    /**
     *  * fetch data by examtemplate
     * @desc  内部函数---根据试卷模板获取对应条件随机的单选题
     * @param  examTemplate
     * @return List<QuestForTest>
     *     题目的抽取规则在各自的extractRule字段中，形为A-5,B-5,C-5,D-5,E-5,F-5  中间用逗号隔开是不同范围的题目，A-5 表示范围是A的题目抽取5道出来
     */
    private List<QuestForTest> getRandomSingleQuesList(ExamTemplate examTemplate){
        /**处理单选题**/
        List<QuestForTest> sigleList = new ArrayList<QuestForTest>();
        String sigleExtractRule = examTemplate.getSingleExtarctRule().trim();
        String sigleRules[] = sigleExtractRule.split(",");
        for(int i =0; i < sigleRules.length; i ++){
            String tempString = sigleRules[i];
            /**获取单选题抽取的考察范围  就是法律法规之类的**/
            String quesScope = tempString.split("-")[0];
            /**获取当前抽取的考察范围内的题目的数目**/
            String quesNum = tempString.split("-")[1];
            /**单选的题目类型码值**/
            String quesType = "S";
            Map<String, Object> paramaterMap = new HashMap<String,Object>();
            paramaterMap.put("scope",quesScope);
            paramaterMap.put("examType",examTemplate.getQuestBankType());
            paramaterMap.put("type",quesType);
            paramaterMap.put("limitNum",Integer.parseInt(quesNum));


            /**按照范围  题库类型  随机抽取数目  和 题型  抽取对应的单选题出来**/
            List<QuestForTest> tempSigleQueList = examMapper.findRandQuesByParam(paramaterMap);

            if(tempSigleQueList != null && tempSigleQueList.size() > 0){
                /**处理选项信息**/
                for(QuestForTest question : tempSigleQueList) {
                    Map<String,Object> paraMap = new HashMap<String, Object>();
                    paraMap.put("quesId",String.valueOf(question.getId()));
                    paraMap.put("limitNum",10);

                    List<ExamOptions> tempOptionLists = examMapper.findRandOptionsByQuesId(paraMap);
                    if(tempOptionLists != null && tempOptionLists.size() > 0){
                        question.setOptions(tempOptionLists);
                    }else{
                        log.error("The quesid: "  + question.getId() + "  has  no options!!!" );
                    }

                }


            }
            sigleList.addAll(tempSigleQueList);
        }
        return sigleList;
    }

    /**
     *  * fetch data by examtemplate
     * @desc  内部函数---根据试卷模板获取对应条件随机的多选题
     * @param  examTemplate
     * @return List<QuestForTest>
     *     题目的抽取规则在各自的extractRule字段中，形为A-5,B-5,C-5,D-5,E-5,F-5  中间用逗号隔开是不同范围的题目，A-5 表示范围是A的题目抽取5道出来
     */
    private List<QuestForTest> getRandomMutiQuesList(ExamTemplate examTemplate){
        /**处理多选题**/
        List<QuestForTest> mutiQuesList = new ArrayList<QuestForTest>();

        String mutiExtractRule = examTemplate.getMutiExtarctRule();
        String mutiRules[] = mutiExtractRule.split(",");
        if(mutiRules == null || mutiRules.length == 0 ){
            log.error("多选题的选取规则配置错误，请仔细核对，试卷模板Id：  " + examTemplate.getId());
            return null;
        }

        for(int i = 0 ; i < mutiRules.length; i++){
            String tempString = mutiRules[i];
            /**获取多选题抽取的考察范围  就是法律法规之类的**/
            String quesScope = tempString.split("-")[0];
            /**获取当前抽取的考察范围内的题目的数目**/
            String quesNum = tempString.split("-")[1];
            /**多选的题目类型码值**/
            String quesType = "M";

            Map<String, Object> paramaterMap = new HashMap<String,Object>();
            paramaterMap.put("scope",quesScope);
            paramaterMap.put("examType",examTemplate.getQuestBankType());
            paramaterMap.put("type",quesType);
            paramaterMap.put("limitNum",Integer.parseInt(quesNum));

            /**按照范围  题库类型  随机抽取数目  和 题型  抽取对应的多选题出来**/
            List<QuestForTest> tempMutiQuesList = examMapper.findRandQuesByParam(paramaterMap);

            if(tempMutiQuesList != null && tempMutiQuesList.size() > 0){
                /**处理选项信息**/
                for (QuestForTest question: tempMutiQuesList) {

                    Map<String,Object> paraMap = new HashMap<String, Object>();
                    paraMap.put("quesId",String.valueOf(question.getId()));
                    paraMap.put("limitNum",10);

                    List<ExamOptions> tempOptionList = examMapper.findRandOptionsByQuesId(paraMap);
                    if(tempOptionList != null && tempMutiQuesList.size() > 0){
                        question.setOptions(tempOptionList);
                    }else{
                        log.error("The quesid: "  + question.getId() + "  has  no options!!!");
                    }
                }
            }

            mutiQuesList.addAll(tempMutiQuesList);
        }
        return mutiQuesList;
    }

    /**
     *  * fetch data by examtemplate
     * @desc  内部函数---根据试卷模板获取对应条件随机的判断题
     * @param  examTemplate
     * @return List<QuestForTest>
     *     题目的抽取规则在各自的extractRule字段中，形为A-5,B-5,C-5,D-5,E-5,F-5  中间用逗号隔开是不同范围的题目，A-5 表示范围是A的题目抽取5道出来
     */
    private List<QuestForTest> getRandomJudgeQuesList(ExamTemplate examTemplate){
        /**处理判断题**/
        List<QuestForTest> judgeQuesList = new ArrayList<QuestForTest>();

        String judgeExtractRule = examTemplate.getJudgeExtarctRule();
        String judgeRules[] = judgeExtractRule.split(",");

        if(judgeRules == null || judgeRules.length == 0){
            log.error("判断题的抽取规则有误，请仔细核对，试卷模板ID： " + examTemplate.getId());
        }

        for(int i = 0 ; i < judgeRules.length; i ++){
            String tempString = judgeRules[i];
            /**获取判断题抽取的考察范围  就是法律法规之类的**/
            String quesScope = tempString.split("-")[0];
            /**获取当前抽取的考察范围内的题目的数目**/
            String quesNum = tempString.split("-")[1];
            /**多选的题目类型码值**/
            String quesType = "J";

            Map<String, Object> paramaterMap = new HashMap<String,Object>();
            paramaterMap.put("scope",quesScope);
            paramaterMap.put("examType",examTemplate.getQuestBankType());
            paramaterMap.put("type",quesType);
            paramaterMap.put("limitNum",Integer.parseInt(quesNum));

            /**按照范围  题库类型  随机抽取数目  和 题型  抽取对应的判断题出来**/
            List<QuestForTest> tempJudgeQuesList = examMapper.findRandQuesByParam(paramaterMap);

            /**判断题的选项不处理，在前端生成就行 **/

            judgeQuesList.addAll(tempJudgeQuesList);
        }

        return judgeQuesList;
    }

    /**
     *  * calculate score
     * @desc  获取对应模板编号的试卷
     * @param  paperForm
     * @return userId
     */
    public ExamResult calculateScore(PostPaperForm paperForm){

        ExamResult examResult = null;
        try {
            examResult = new ExamResult();
            List<UserAnswer> answerList = paperForm.getAnswerList();
            if(paperForm.getTemplateId() == null || "".equals(paperForm.getTemplateId())){
                log.error("试卷模板ID为空，不能获取试卷模板信息，上传参数错误！");
                return null;
            }
            /**获取试卷模板配置，以便获取每种题型的分数**/
            ExamTemplate examTemplate = examMapper.findTemplateById(paperForm.getTemplateId());
            BigDecimal singleScore = new BigDecimal(examTemplate.getSingleScore());
            BigDecimal mutiScore = new BigDecimal(examTemplate.getMutiScore());
            BigDecimal judgeScore = new BigDecimal(examTemplate.getJudgeScore());
            /**统计各题型总分 答对的题型数目**/
            BigDecimal singleTScore = new BigDecimal(0);
            BigDecimal mutiTScore = new BigDecimal(0);
            BigDecimal judgeTScore = new BigDecimal(0);
            int correctSingleNum = 0;
            int correctMutiNum = 0;
            int correctJudeNum = 0;

            if(answerList != null && answerList.size() > 0){
                BigDecimal totalScore = new BigDecimal(0);
                for (UserAnswer answer: answerList) {
                    ExamQuestion examQuestion = examMapper.findQuestionByQuesId(Integer.parseInt(answer.getQuestionId().trim()));
                    String correctAnswer = examQuestion.getAnswer();
                    /**答题正确按照题目类型 和模板中配置的分数计分**/
                    if(answer.getAnswer().trim() != null && correctAnswer.equals(answer.getAnswer().trim()) &&(singleQuestion.equals(examQuestion.getType()) || judgeQuestion.equals(examQuestion.getType()))){
                        if(singleQuestion.equals(examQuestion.getType())){
                            correctSingleNum++;
                        }else if(judgeQuestion.equals(examQuestion.getType())){
                            correctJudeNum++;
                        }else{
                            log.error("出现没有的题型 ，请核对数据！");
                        }
                    }else if(answer.getAnswer().trim() != null && mutiQuestion.equals(examQuestion.getType())){
                        if(correctAnswer.length() == answer.getAnswer().length()){
                            boolean isRight = true;
                            /**判断多选题答案是否正确**/
                            for(int i = 0; i < answer.getAnswer().length(); i ++){
                                if(correctAnswer.indexOf(answer.getAnswer().charAt(i)) < 0){
                                    isRight = false;
                                }
                            }
                            if(isRight){
                                correctMutiNum++;
                            }
                        }
                    }

                }
                /**计算总分**/
                singleTScore = singleScore.multiply(new BigDecimal(correctSingleNum));
                mutiTScore = mutiScore.multiply(new BigDecimal(correctMutiNum));
                judgeTScore = judgeScore.multiply(new BigDecimal(correctJudeNum));
                totalScore = singleTScore.add(mutiTScore).add(judgeTScore);

                /**记录本次答题信息入库**/
                ExamRecord record = new ExamRecord();
                record.setTemplateId(paperForm.getTemplateId());
                record.setUserId(paperForm.getUserId());
                record.setUserName(paperForm.getUserName());
                record.setOrganization(paperForm.getOrganization());
                record.setScore(String.valueOf(totalScore));
                record.setCorrectSingleNum(correctSingleNum);
                record.setSingleTScore(String.valueOf(singleTScore));
                record.setCorrectMutiNum(correctMutiNum);
                record.setMutiTScore(String.valueOf(mutiTScore));
                record.setCorrectJudgeNum(correctJudeNum);
                record.setJudgeTScore(String.valueOf(judgeTScore));
                record.setUserAnswerList(JSON.toJSONString(answerList));


                examMapper.inserExamRecord(record);

                examResult.setScore(String.valueOf(totalScore));
                examResult.setSingleTScore(String.valueOf(singleTScore));
                examResult.setMutiTScore(String.valueOf(mutiTScore));
                examResult.setJudgeTScore(String.valueOf(judgeTScore));
                examResult.setCorrectSingleNum(String.valueOf(correctSingleNum));
                examResult.setCorrectMutiNum(String.valueOf(correctMutiNum));
                examResult.setCorrectJudgeNum(String.valueOf(correctJudeNum));

            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return examResult;
    }

    /**
     *  * 获取所有考试
     * @desc  获取所有考试
     * @return List
     */
    public List<ExamTemplate> findAllExam(){
        return examMapper.findAllExam();
    }

    /**
     *  * 获取所有机构信息
     * @desc  获取所有机构
     * @return List
     */
    public List<ExamOrganization> findAllOrgs() {
        return examMapper.findAllOrgs();
    }
}
