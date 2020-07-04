package com.sxnsh.exam.mapper;

import com.sxnsh.exam.entity.*;
import com.sxnsh.exam.vo.QuestForTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yexz
 * @date 2020/03/17
 */
@Mapper
public interface ExamMapper {
    /**
     *  * fetch data by tablename
     * @desc  查询某个表中最近插入的id的值   用来实现类似oracle序列的功能 查出来的值+1就是 nextvalue   表名作为参数传递进来只能是${}
     * @param tableName
     * @return int
     */
    int findLastInsertId(@Param("tableName") String tableName);

    /**
     *  * fetch data by rule id
     * @desc  插入问题
     * @param  question
     * @return
     */
    public void insertQuestion(ExamQuestion question);

    /**
     *  * fetch data by rule id
     * @desc  插入选项---单个
     * @param  option
     * @return
     */
    void inserOption(ExamOptions option);

    /**
     *  * fetch data by rule id
     * @desc  插入选项---单个
     * @param  optionsList
     * @return
     */
    void insertOptions(List<ExamOptions> optionsList);

    /**
     *  * fetch data by templateId
     * @desc  查询试卷配置信息
     * @param  templateId
     * @return examtemplate
     */
    ExamTemplate findTemplateById(@Param("templateId") String templateId);

    /**
     *  * fetch data by templateId
     * @desc  查询试卷配置信息
     * @param  map
     * @return List<QuestForTest>
     */
    List<QuestForTest> findRandQuesByParam(Map<String,Object> map);

    /**
     *  * fetch data by quesId
     * @desc  查询题目对应的选项 随机排序的  不同类型的参数必须用map来传递，否则出现转化错误
     * @param  map 包含 查询询的选项数目这里只是用来做固定参数 查询的问题id
     * @return List<ExamOptions>
     */
    List<ExamOptions>  findRandOptionsByQuesId(Map<String, Object> map);

    /**
     *  * fetch data by quesId
     * @desc  查询题目编号对应的答案
     * @param  questionId 查询的题目Id
     * @return ExamQuestion
     */
     ExamQuestion findQuestionByQuesId(@Param("questionId") int questionId );

    /**
     *  * insert data
     * @desc  记录用户考试结果信息
     * @param  examRecord 考试记录信息
     * @return ExamQuestion
     */
     void inserExamRecord(ExamRecord examRecord);

    /**
     *  * get data
     * @desc  记录用户考试结果信息
     * @return List<ExamTemplate>
     */
     List<ExamTemplate> findAllExam();

    /**
     *  * get data
     * @param id  试卷模板id
     * @param orgId  机构ID
     * @desc  查询答题记录
     * @return List<ExamRecord>
     */
     List<ExamRecord> findRecordByPage(@Param("id") String id, @Param("orgId") String orgId);

    /**
     *  * get data
     * @desc  查询所有机构信息
     * @return List<ExamOrganization>
     */
    List<ExamOrganization> findAllOrgs();


}
